package com.flabser.dataengine.system;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.catalina.realm.RealmBase;

import com.flabser.dataengine.DatabaseUtil;
import com.flabser.dataengine.activity.Activity;
import com.flabser.dataengine.activity.IActivity;
import com.flabser.dataengine.pool.DatabasePoolException;
import com.flabser.dataengine.pool.IDBConnectionPool;
import com.flabser.server.Server;
import com.flabser.users.ApplicationProfile;
import com.flabser.users.User;

public class SystemDatabase implements ISystemDatabase {
	public static final String jdbcDriver = "org.postgresql.Driver";
	private IDBConnectionPool dbPool;
	private static final SimpleDateFormat sqlDateTimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	static String connectionURL = "jdbc:postgresql://localhost/2next_system";
	static String dbUser = "postgres";
	static String dbUserPwd = "1";

	// TODO Need to bring the setting out

	public SystemDatabase() throws DatabasePoolException, InstantiationException, IllegalAccessException, ClassNotFoundException {
		dbPool = new com.flabser.dataengine.pool.DBConnectionPool();
		dbPool.initConnectionPool(jdbcDriver, connectionURL, dbUser, dbUserPwd);
		Connection conn = dbPool.getConnection();
		try {
			conn.setAutoCommit(false);
			createUserTable(DDEScripts.getUsersDDE(), "USERS");
			createUserTable(DDEScripts.getAppsDDE(), "APPS");
			createUserTable(DDEScripts.getUserAppsDDE(), "USERAPPS");
			createUserTable(DDEScripts.getUserRolesDDE(), "USERROLES");
			createUserTable(DDEScripts.getUsersActivityDDE(), "USERSACTIVITY");
			createUserTable(DDEScripts.getHolidaysDDE(), "HOLIDAYS");
			conn.commit();
		} catch (Throwable e) {
			Server.logger.errorLogEntry(e);
			e.printStackTrace();
			DatabaseUtil.debugErrorPrint(e);
		} finally {
			dbPool.returnConnection(conn);
		}
	}

	@Override
	public IActivity getActivity() {
		return new Activity(dbPool);
	}

	@Override
	public User checkUserHash(String login, String pwd, String hashAsText) {
		User user = new User();
		Connection conn = dbPool.getConnection();
		try {
			conn.setAutoCommit(false);
			Statement s = conn.createStatement();
			String sql = "select * from USERS where LOGIN = '" + login + "'";
			ResultSet rs = s.executeQuery(sql);
			String password = "";

			if (rs.next()) {
				if (pwd != null && rs.getString("PWDHASH") != null) {
					password = rs.getString("PWDHASH").trim();
					if (!password.trim().equals("")) {
						String pwdHash = "";
						if (password.length() < 11) {
							pwdHash = pwd.hashCode() + "";
						} else {
							pwdHash = RealmBase.Digest(pwd, "MD5", "UTF-8");
						}
						int hash = rs.getInt("LOGINHASH");
						if (checkHash(hashAsText, hash)) {
							user = initUser(conn, rs, login);
							user.isAuthorized = true;
						} else if (checkHashPSW(pwdHash, password)) {
							user = initUser(conn, rs, login);
							user.isAuthorized = true;
							if (password.length() < 11) {
								pswToPswHash(user, pwd);
							}
						}
					} else {
						password = rs.getString("PWD");
						int hash = rs.getInt("LOGINHASH");
						if (checkHash(hashAsText, hash)) {
							user = initUser(conn, rs, login);
							user.isAuthorized = true;
						} else if (pwd.equals(password)) {
							user = initUser(conn, rs, login);
							user.isAuthorized = true;
							pswToPswHash(user, password);
						}
					}
				} else if (hashAsText != null) {
					password = rs.getString("PWD");
					int hash = rs.getInt("LOGINHASH");
					if (checkHash(hashAsText, hash)) {
						user = initUser(conn, rs, login);
						user.isAuthorized = true;
					} else if (pwd.equals(password)) {
						user = initUser(conn, rs, login);
						user.isAuthorized = true;
						pswToPswHash(user, password);
					}
				}
			}
			rs.close();
			s.close();
			conn.commit();
			return user;
		} catch (Throwable e) {
			DatabaseUtil.debugErrorPrint(e);
			return null;
		} finally {
			dbPool.returnConnection(conn);
		}
	}

	private void pswToPswHash(User user, String pwd) throws SQLException {
		Connection conn = dbPool.getConnection();
		conn.setAutoCommit(false);
		// String pwdHsh = pwd.hashCode()+"";
		// String pwdHsh = getMD5Hash(pwd);
		String pwdHsh = RealmBase.Digest(pwd, "MD5", "UTF-8");

		String userUpdateSQL = "update USERS set PWD='', PWDHASH='" + pwdHsh + "'" + " where DOCID=" + user.id;
		PreparedStatement pst = conn.prepareStatement(userUpdateSQL);
		pst.executeUpdate();
		conn.commit();
		conn.close();
		// user.setPasswordHash(pwd);
		// user.setPassword("");
	}

	private User initUser(Connection conn, ResultSet rs, String login) throws SQLException {
		User user = new User();
		user.fill(rs);
		if (user.isValid) {
			fillUserApp(conn, user);
		}
		return user;

	}

	@Override
	public int getAllUsersCount(String condition) {
		int count = 0;
		String wherePiece = "";
		Connection conn = dbPool.getConnection();
		try {
			conn.setAutoCommit(false);
			if (!condition.equals("")) {
				wherePiece = "WHERE " + condition;
			}
			Statement s = conn.createStatement();
			String sql = "select count(*) from USERS " + wherePiece;
			ResultSet rs = s.executeQuery(sql);
			if (rs.next()) {
				count = rs.getInt(1);
			}
			rs.close();
			s.close();
			conn.commit();
			return count;
		} catch (Throwable e) {
			DatabaseUtil.debugErrorPrint(e);
			return 0;
		} finally {
			dbPool.returnConnection(conn);
		}
	}

	@Override
	public int getUsersCount(String condition) {
		int count = 0;
		String wherePiece = "";
		Connection conn = dbPool.getConnection();
		try {
			conn.setAutoCommit(false);
			if (!condition.equals("")) {
				wherePiece = "WHERE " + condition;
			}
			Statement s = conn.createStatement();
			String sql = "select count(*) from USERS " + wherePiece;
			ResultSet rs = s.executeQuery(sql);

			if (rs.next()) {
				count = rs.getInt(1);
			}

			rs.close();
			s.close();
			conn.commit();
		} catch (Throwable e) {
			DatabaseUtil.debugErrorPrint(e);
		} finally {
			dbPool.returnConnection(conn);
		}
		return count;
	}

	@Override
	public HashMap<String, User> getAllAdministrators() {
		HashMap<String, User> users = new HashMap<String, User>();
		Connection conn = dbPool.getConnection();
		try {
			conn.setAutoCommit(false);
			Statement s = conn.createStatement();
			String sql = "select * from USERS WHERE ISSUPERVISOR = 1";
			ResultSet rs = s.executeQuery(sql);

			while (rs.next()) {
				User user = new User();
				user.fill(rs);
				user.isValid = true;
				users.put(user.getLogin(), user);
			}

			rs.close();
			s.close();
			conn.commit();
			return users;
		} catch (Throwable e) {
			DatabaseUtil.debugErrorPrint(e);
			return null;
		} finally {
			dbPool.returnConnection(conn);
		}
	}

	@Override
	public User getUserByVerifyCode(String code) {
		User user = null;
		Connection conn = dbPool.getConnection();
		try {
			conn.setAutoCommit(false);
			Statement s = conn.createStatement();
			String sql = "select * from USERS where USERS.VERIFYCODE='" + code + "'";
			ResultSet rs = s.executeQuery(sql);

			if (rs.next()) {
				user = new User();
				user.fill(rs);
				if (user.isValid) {
					fillUserApp(conn, user);
				}
			}
			rs.close();
			s.close();
			conn.commit();

		} catch (Throwable e) {
			DatabaseUtil.debugErrorPrint(e);
			user = null;
		} finally {
			dbPool.returnConnection(conn);
		}
		return user;
	}

	public User getUserByEmail(String email) {
		User user = null;
		Connection conn = dbPool.getConnection();
		try {
			conn.setAutoCommit(false);
			Statement s = conn.createStatement();
			String sql = "select * from USERS where USERS.EMAIL='" + email + "'";
			ResultSet rs = s.executeQuery(sql);

			if (rs.next()) {
				user = new User();
				user.fill(rs);
				if (user.isValid) {
					fillUserApp(conn, user);
				}
			}
			rs.close();
			s.close();
			conn.commit();

		} catch (Throwable e) {
			DatabaseUtil.debugErrorPrint(e);
			user = null;
		} finally {
			dbPool.returnConnection(conn);
		}
		return user;
	}

	@Override
	public User getUser(int id) {
		User user = null;
		Connection conn = dbPool.getConnection();
		try {
			conn.setAutoCommit(false);
			Statement s = conn.createStatement();
			String sql = "select * from USERS where USERS.ID=" + id;
			ResultSet rs = s.executeQuery(sql);

			if (rs.next()) {
				user = new User();
				user.fill(rs);
				if (user.isValid) {
					fillUserApp(conn, user);
				}
			}
			rs.close();
			s.close();
			conn.commit();

		} catch (Throwable e) {
			DatabaseUtil.debugErrorPrint(e);
			user = null;
		} finally {
			dbPool.returnConnection(conn);
		}
		return user;
	}

	@Override
	public User getUser(String id) {
		User user = null;
		Connection conn = dbPool.getConnection();
		try {
			conn.setAutoCommit(false);
			Statement s = conn.createStatement();
			String sql = "select * from USERS where USERS.LOGIN='" + id + "'";
			ResultSet rs = s.executeQuery(sql);

			if (rs.next()) {
				user = new User();
				user.fill(rs);
				if (user.isValid) {
					fillUserApp(conn, user);
				}
			}
			rs.close();
			s.close();
			conn.commit();

		} catch (Throwable e) {
			DatabaseUtil.debugErrorPrint(e);
			user = null;
		} finally {
			dbPool.returnConnection(conn);
		}
		return user;
	}

	@Override
	public int deleteUser(int id) {
		Connection conn = dbPool.getConnection();
		try {
			conn.setAutoCommit(false);
			String delEnApp = "delete from USERAPPS where USERID = " + id;
			PreparedStatement pst = conn.prepareStatement(delEnApp);
			pst.executeUpdate();
			String delUserTab = "delete from USERS where DOCID = " + id;
			pst = conn.prepareStatement(delUserTab);
			pst.executeUpdate();
			conn.commit();
			pst.close();
			return 1;
		} catch (Throwable e) {
			DatabaseUtil.errorPrint(e);
			return -1;
		} finally {
			dbPool.returnConnection(conn);
		}

	}

	public boolean hasUserTable(String tableName) {
		Connection conn = dbPool.getConnection();
		try {
			conn.setAutoCommit(false);
			Statement s = conn.createStatement();
			String sql = "select * from " + tableName;
			s.executeQuery(sql);
			s.close();
			conn.commit();
			return true;
		} catch (Throwable e) {
			return false;
		} finally {
			dbPool.returnConnection(conn);
		}
	}

	public boolean createUserTable(String createTableScript, String tableName) {
		Connection conn = dbPool.getConnection();
		boolean createUserTab = false;
		try {
			conn.setAutoCommit(false);
			Statement s = conn.createStatement();
			if (!hasUserTable(tableName)) {
				if (s.execute(createTableScript)) {
					Server.logger.errorLogEntry("Unable to create table \"" + tableName + "\"");
				}
			}

			createUserTab = true;
			s.close();
			conn.commit();
		} catch (Throwable e) {
			DatabaseUtil.debugErrorPrint(e);
			createUserTab = false;
		} finally {
			dbPool.returnConnection(conn);
		}
		return createUserTab;
	}

	@Override
	public int insert(User user) {
		Connection conn = dbPool.getConnection();
		try {
			conn.setAutoCommit(false);
			int key = 0;
			Statement stmt = conn.createStatement();
			String sql = "insert into USERS(USERNAME, LOGIN, EMAIL, PWD, ISSUPERVISOR, PRIMARYREGDATE, REGDATE, LOGINHASH, PWDHASH, DEFAULTDBPWD,"
					+ "LASTDEFAULTURL, STATUS, VERIFYCODE)" + " values('"
					+ user.getUserName()
					+ "','"
					+ user.getLogin()
					+ "',"
					+ "'"
					+ user.getEmail()
					+ "','"
					+ user.getPwd()
					+ "',"
					+ user.getIsSupervisor()
					+ ",'"
					+ sqlDateTimeFormat.format(user.getPrimaryRegDate())
					+ "','"
					+ sqlDateTimeFormat.format(user.getRegDate())
					+ "',"
					+ user.getHash()
					+ ", '"
					+ user.getPasswordHash()
					+ "','"
					+ user.getDefaultDbPwd()
					+ "','"
					+ user.lastURL
					+ "',"
					+ user.getStatus().getCode()
					+ ",'"
					+ user.getVerifyCode() + "')";
			PreparedStatement pst = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
			pst.executeUpdate();
			ResultSet rs = pst.getGeneratedKeys();
			if (rs.next()) {
				key = rs.getInt(1);
			}

			for (ApplicationProfile app : user.getApplications()) {
				String insertURL = "insert into USERAPPS(USERID, APPIDD)values(" + user.id + ", " + app.id + ")";
				pst = conn.prepareStatement(insertURL);
				pst.executeUpdate();

			}

			conn.commit();
			pst.close();
			stmt.close();
			return key;
		} catch (Throwable e) {
			DatabaseUtil.debugErrorPrint(e);
			return -1;
		} finally {
			dbPool.returnConnection(conn);
		}
	}

	@Override
	public int update(User user) {
		Connection conn = dbPool.getConnection();
		try {
			conn.setAutoCommit(false);

			String userUpdateSQL = "update USERS set LOGIN='" + user.getLogin() + "', USERNAME='" + user.getUserName() + "'," + "EMAIL='" + user.getEmail()
					+ "', PWD='" + user.getPwd() + "', ISSUPERVISOR = " + user.getIsSupervisor() + "," + "REGDATE='"
					+ sqlDateTimeFormat.format(user.getRegDate()) + "'," + "LOGINHASH = " + (user.getLogin() + user.getPwd()).hashCode() + ", " + "PWDHASH = '"
					+ user.getPasswordHash() + "', DEFAULTDBPWD='" + user.getDefaultDbPwd() + "', LASTDEFAULTURL = '" + user.lastURL + "'," + "STATUS = "
					+ user.getStatus().getCode() + ", VERIFYCODE='" + user.getVerifyCode() + "'" + " where ID=" + user.id;
			PreparedStatement pst = conn.prepareStatement(userUpdateSQL);
			pst.executeUpdate();
			conn.commit();
			String delSQL = "delete from USERAPPS where USERID = " + user.id;
			pst = conn.prepareStatement(delSQL);
			pst.executeUpdate();

			for (ApplicationProfile app : user.getApplications()) {
				String insertURL = "insert into USERAPPS(USERID, APPID)values(" + user.id + ", " + app.id + ")";
				PreparedStatement pst0 = conn.prepareStatement(insertURL);
				pst0.executeUpdate();

			}
			conn.commit();
			pst.close();
			return user.id;
		} catch (Throwable e) {
			DatabaseUtil.debugErrorPrint(e);
			return -1;
		} finally {
			dbPool.returnConnection(conn);
		}
	}

	@Override
	public ArrayList<User> getUsers(String keyWord) {
		ArrayList<User> users = new ArrayList<User>();

		Connection conn = dbPool.getConnection();
		try {
			conn.setAutoCommit(false);
			Statement s = conn.createStatement();
			String sql = "select * from USERS where USERS.USERID LIKE '" + keyWord + "%'";
			ResultSet rs = s.executeQuery(sql);

			while (rs.next()) {
				User user = new User();
				user.fill(rs);
				users.add(user);
			}
			rs.close();
			s.close();
			conn.commit();
		} catch (Throwable e) {
			DatabaseUtil.debugErrorPrint(e);
		} finally {
			dbPool.returnConnection(conn);
		}
		return users;
	}

	private boolean checkHash(String hashAsString, int hash) {
		try {
			int userHash = Integer.parseInt(hashAsString);
			if (userHash == hash) {
				return true;
			} else {
				return false;
			}
		} catch (NumberFormatException nfe) {
			return false;
		}
	}

	private boolean checkHashPSW(String hashPSW, String hashPSWDB) {
		try {

			// int userHash = hashPSW;
			if (hashPSW.equals(hashPSWDB)) {
				return true;
			} else {
				return false;
			}
		} catch (NumberFormatException nfe) {
			return false;
		}
	}

	@Override
	public ArrayList<User> getAllUsers(String condition, int calcStartEntry, int pageSize) {
		ArrayList<User> users = new ArrayList<User>();
		String wherePiece = "";
		Connection conn = dbPool.getConnection();
		try {
			conn.setAutoCommit(false);
			if (!condition.equals("")) {
				wherePiece = "WHERE " + condition;
			}
			Statement s = conn.createStatement();
			String sql = "select * from USERS " + wherePiece + " LIMIT " + pageSize + " OFFSET " + calcStartEntry;
			ResultSet rs = s.executeQuery(sql);

			while (rs.next()) {
				User user = new User();
				user.fill(rs);
				/*
				 * if (user.isValid) { fillUserApp(conn, user); }
				 */
				user.isValid = true;
				users.add(user);
			}

			rs.close();
			s.close();
			conn.commit();
			return users;
		} catch (Throwable e) {
			DatabaseUtil.debugErrorPrint(e);
			return null;
		} finally {
			dbPool.returnConnection(conn);
		}
	}

	@Override
	public IApplicationDatabase getApplicationDatabase() throws InstantiationException, IllegalAccessException, ClassNotFoundException {
		return new ApplicationDatabase();
	}

	@Override
	public int insert(ApplicationProfile ap) {
		Connection conn = dbPool.getConnection();
		try {
			conn.setAutoCommit(false);
			int key = 0;
			Statement stmt = conn.createStatement();
			String sql = "insert into APPS(APPTYPE, APPID, APPNAME, OWNER, DBTYPE, DBHOST, DBNAME, DBLOGIN, DBPWD) values('" + ap.appType + "','" + ap.appID
					+ "','" + ap.appName + "','" + ap.owner + "'," + ap.dbType.getCode() + ",'" + ap.dbHost + "','" + ap.dbName + "','" + ap.dbLogin + "','"
					+ ap.dbPwd + "')";

			PreparedStatement pst = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);

			pst.executeUpdate();
			ResultSet rs = pst.getGeneratedKeys();
			while (rs.next()) {
				key = rs.getInt(1);
			}
			conn.commit();
			pst.close();
			stmt.close();
			return key;
		} catch (Throwable e) {
			DatabaseUtil.debugErrorPrint(e);
			return -1;
		} finally {
			dbPool.returnConnection(conn);
		}
	}

	@Override
	public int update(ApplicationProfile ap) {
		Connection conn = dbPool.getConnection();
		try {
			conn.setAutoCommit(false);
			Statement stmt = conn.createStatement();
			String sql = "update APPS set APPNAME='" + ap.appName + "', OWNER='" + ap.owner + "',DBHOST='" + ap.dbHost + "', DBLOGIN = '" + ap.dbLogin
					+ "',DBPWD='" + ap.dbPwd + "' where ID=" + ap.id;

			PreparedStatement pst = conn.prepareStatement(sql);
			pst.executeUpdate();
			conn.commit();
			pst.close();
			stmt.close();
			return ap.id;
		} catch (Throwable e) {
			DatabaseUtil.debugErrorPrint(e);
			return -1;
		} finally {
			dbPool.returnConnection(conn);
		}
	}

	@Override
	public int deleteApplicationProfile(int id) {
		// TODO Auto-generated method stub
		return 0;
	}

	private void fillUserApp(Connection conn, User user) throws SQLException {
		Statement s = conn.createStatement();
		ResultSet rs = s.executeQuery("select * from USERAPPS where USERID = " + user.id);
		if (rs.next()) {
			Statement s2 = conn.createStatement();
			ResultSet rs2 = s2.executeQuery("select * from APPS where ID = " + rs.getInt("APPID"));
			if (rs2.next()) {
				ApplicationProfile ap = new ApplicationProfile(rs2);
				user.addApplication(ap);
			}
			rs2.close();
			s2.close();
		}
		rs.close();
		s.close();
	}

	@Override
	public ArrayList<ApplicationProfile> getAllApps(String condition, int calcStartEntry, int pageSize) {
		ArrayList<ApplicationProfile> apps = new ArrayList<ApplicationProfile>();
		String wherePiece = "";
		Connection conn = dbPool.getConnection();
		try {
			conn.setAutoCommit(false);
			if (!condition.equals("")) {
				wherePiece = "WHERE " + condition;
			}
			Statement s = conn.createStatement();
			String sql = "select * from APPS " + wherePiece + " LIMIT " + pageSize + " OFFSET " + calcStartEntry;
			ResultSet rs = s.executeQuery(sql);

			while (rs.next()) {
				ApplicationProfile app = new ApplicationProfile(rs);
				apps.add(app);
			}

			rs.close();
			s.close();
			conn.commit();
			return apps;
		} catch (Throwable e) {
			DatabaseUtil.debugErrorPrint(e);
			return null;
		} finally {
			dbPool.returnConnection(conn);
		}
	}

}
