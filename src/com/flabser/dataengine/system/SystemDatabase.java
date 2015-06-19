package com.flabser.dataengine.system;

import org.apache.catalina.realm.RealmBase;

import com.flabser.appenv.AppEnv;
import com.flabser.dataengine.DatabaseUtil;
import com.flabser.dataengine.activity.*;
import com.flabser.dataengine.pool.DatabasePoolException;
import com.flabser.dataengine.pool.IDBConnectionPool;
import com.flabser.users.User;
import com.flabser.users.ApplicationProfile;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;

public class SystemDatabase implements ISystemDatabase {
	public static final String jdbcDriver = "org.postgresql.Driver";
	private IDBConnectionPool dbPool;
	private static final SimpleDateFormat sqlDateTimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	static String connectionURL = "jdbc:postgresql://localhost/2next_system";
	static String dbUser = "postgres";
	static String dbUserPwd = "3287";

	public SystemDatabase() throws DatabasePoolException, InstantiationException, IllegalAccessException,
			ClassNotFoundException {
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
			AppEnv.logger.errorLogEntry(e);
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

	public User checkUser(String login, String pwd, User user) {
		Connection conn = dbPool.getConnection();

		try {
			conn.setAutoCommit(false);
			Statement s = conn.createStatement();
			String sql = "select * from USERS, APPS where USERS.DOCID = APPS.DOCID and USERID = '" + login + "'";
			ResultSet rs = s.executeQuery(sql);
			String password = "";

			if (rs.next()) {

				if (rs.getString("PWDHASH") != null) {
					if (!rs.getString("PWDHASH").trim().equals("")) {
						password = rs.getString("PWDHASH");
						String pwdHash = "";
						if (password.length() < 30) {
							pwdHash = pwd.hashCode() + "";
						} else {
							// pwdHash = getMD5Hash(pwd);
							RealmBase rb = null;
							pwdHash = rb.Digest(pwd, "MD5", "UTF-8");

						}

						if (pwdHash.equals(password)) {
							user = initUser(conn, rs, login);
							user.isAuthorized = true;
							if (password.length() < 11) {// !!!!
								pswToPswHash(user, pwd);
							}

						}
					} else {
						password = rs.getString("PWD");
						if (pwd.equals(password)) {
							user = initUser(conn, rs, login);
							user.isAuthorized = true;
							pswToPswHash(user, password);
						}
					}
				} else {
					password = rs.getString("PWD");
					if (pwd.equals(password)) {
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

	public User checkUser(String login, String pwd, String hashAsText, User user) {
		Connection conn = dbPool.getConnection();
		try {
			conn.setAutoCommit(false);
			Statement s = conn.createStatement();
			String sql = "select * from USERS, APPS where USERS.DOCID = APPS.DOCID and USERID = '" + login + "'";
			ResultSet rs = s.executeQuery(sql);
			String password = "";

			if (rs.next()) {

				// .trim().equals("")
				if (rs.getString("PWDHASH") != null) {
					if (!rs.getString("PWDHASH").trim().equals("")) {
						password = rs.getString("PWDHASH");
						String pwdHash = pwd;
						int hash = rs.getInt("LOGINHASH");
						if (checkHash(hashAsText, hash)) {
							user = initUser(conn, rs, login);
							user.isAuthorized = true;
						} else if (checkHashPSW(pwdHash, password)) {
							user = initUser(conn, rs, login);
							user.isAuthorized = true;
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

	public User checkUserHash(String login, String pwd, String hashAsText, User user) {
		Connection conn = dbPool.getConnection();
		try {
			conn.setAutoCommit(false);
			Statement s = conn.createStatement();
			String sql = "select * from USERS where LOGIN = '" + login + "'";
			ResultSet rs = s.executeQuery(sql);
			String password = "";

			if (rs.next()) {

				// .trim().equals("")
				if (rs.getString("PWDHASH") != null) {
					if (!rs.getString("PWDHASH").trim().equals("")) {
						password = rs.getString("PWDHASH");
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
		RealmBase rb = null;
		String pwdHsh = rb.Digest(pwd, "MD5", "UTF-8");

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
		if (user.isValid) fillUserApp(conn, user);
		return user;

	}

	@Override
	public int getAllUsersCount(String condition) {
		int count = 0;
		String wherePiece = "";
		Connection conn = dbPool.getConnection();
		try {
			conn.setAutoCommit(false);
			if (!condition.equals(""))
				wherePiece = "WHERE " + condition;
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
			if (!condition.equals(""))
				wherePiece = "WHERE " + condition;
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
					AppEnv.logger.errorLogEntry("Unable to create table \"" + tableName + "\"");
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

	public int insert(User user) {
		Connection conn = dbPool.getConnection();
		try {
			conn.setAutoCommit(false);
			int key = 0;
			Statement stmt = conn.createStatement();
			String sql = "insert into USERS(USERNAME, LOGIN, EMAIL, PWD, ISSUPERVISOR, PRIMARYREGDATE, REGDATE, LOGINHASH, PWDHASH, "
					+ "LASTDEFAULTURL, STATUS, VERIFYCODE)" + " values('"
					+ user.getUserName()
					+ "','"
					+ user.getLogin()
					+ "',"
					+ "'"
					+ user.getEmail()
					+ "','"
					+ user.getPassword()
					+ "',"
					+ user.getIsSupervisor()
					+ ",'"
					+ sqlDateTimeFormat.format(new java.util.Date())
					+ "','"
					+ sqlDateTimeFormat.format(user.getRegDate())
					+ "',"
					+ (user.getLogin() + user.getPassword()).hashCode()
					+ ", '"
					+ user.getPasswordHash()
					+ "','"
					+ user.lastURL + "'," + user.getStatus().getCode() + ",'" + user.getVerifyCode() + "')";
			PreparedStatement pst = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
			pst.executeUpdate();
			ResultSet rs = pst.getGeneratedKeys();
			if (rs.next()) {
				key = rs.getInt(1);
			}

			for (ApplicationProfile app : user.enabledApps.values()) {
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
			String pwdHsh = "";
			String pwd = "";
			if (user.getPasswordHash() != null && !user.getPasswordHash().trim().equals("")) {
				pwdHsh = user.getPasswordHash();
			} else {
				pwd = user.getPassword();
			}
			String userUpdateSQL = "update USERS set LOGIN='" + user.getLogin() + "', USERNAME='" + user.getUserName()
					+ "'," + "EMAIL='" + user.getEmail() + "', PWD='" + pwd + "', ISSUPERVISOR = "
					+ user.getIsSupervisor() + "," + "REGDATE='" + sqlDateTimeFormat.format(user.getRegDate()) + "',"
					+ "LOGINHASH = " + (user.getLogin() + user.getPassword()).hashCode() + ", " + "PWDHASH = '"
					+ user.getPasswordHash() + "', " + "LASTDEFAULTURL = '" + user.lastURL + "'," + "STATUS = "
					+ user.getStatus().getCode() + ", VERIFYCODE='" + user.getVerifyCode() + "'" + " where ID="
					+ user.id;
			PreparedStatement pst = conn.prepareStatement(userUpdateSQL);
			pst.executeUpdate();
			conn.commit();
			String delSQL = "delete from USERAPPS where USERID = " + user.id;
			pst = conn.prepareStatement(delSQL);
			pst.executeUpdate();

			for (ApplicationProfile app : user.enabledApps.values()) {
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
			if (!condition.equals(""))
				wherePiece = "WHERE " + condition;
			Statement s = conn.createStatement();
			String sql = "select * from USERS " + wherePiece + " LIMIT " + pageSize + " OFFSET " + calcStartEntry;
			ResultSet rs = s.executeQuery(sql);

			while (rs.next()) {
				User user = new User();
				user.fill(rs);
				if (user.isValid) {
					fillUserApp(conn, user);
				}
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
	public IApplicationDatabase getApplicationDatabase() throws InstantiationException, IllegalAccessException,
			ClassNotFoundException {
		return new ApplicationDatabase();
	}

	@Override
	public int insert(ApplicationProfile ap) {
		Connection conn = dbPool.getConnection();
		try {
			conn.setAutoCommit(false);
			int key = 0;
			Statement stmt = conn.createStatement();
			String sql = "insert into APPS(APPNAME, OWNER, DBHOST, DBNAME, DBLOGIN, DBPWD) values(" + "'" + ap.appName
					+ "','" + ap.owner + "','" + ap.dbHost + "','" + ap.dbName + "','" + ap.dbLogin + "','" + ap.dbPwd
					+ "')";

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
			String sql = "update APPS set APPNAME='" + ap.appName + "', OWNER='" + ap.owner
					+ "',DBHOST='" + ap.dbHost + "', DBNAME='" + ap.dbName + "', DBLOGIN = '"
					+ ap.dbLogin + "',DBPWD='" + ap.dbPwd + "'";	

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
				user.enabledApps.put(ap.appName, ap);
			}
			rs2.close();
			s2.close();
		}
		rs.close();
		s.close();
	}

	
}
