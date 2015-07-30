package com.flabser.dataengine.system;

import com.flabser.dataengine.DatabaseUtil;
import com.flabser.dataengine.activity.Activity;
import com.flabser.dataengine.activity.IActivity;
import com.flabser.dataengine.pool.DatabasePoolException;
import com.flabser.dataengine.pool.IDBConnectionPool;
import com.flabser.dataengine.system.entities.ApplicationProfile;
import com.flabser.dataengine.system.entities.UserGroup;
import com.flabser.dataengine.system.entities.UserRole;
import com.flabser.server.Server;
import com.flabser.users.User;
import com.flabser.users.UserStatusType;
import org.apache.catalina.realm.RealmBase;

import java.sql.Array;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@SuppressWarnings({ "SqlDialectInspection", "SqlNoDataSourceInspection" })
public class SystemDatabase implements ISystemDatabase {
	public static final String jdbcDriver = "org.postgresql.Driver";
	private IDBConnectionPool dbPool;
	private static final SimpleDateFormat sqlDateTimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	static String connectionURL = "jdbc:postgresql://localhost/2Next";
	static String dbUser = "postgres";
	static String dbUserPwd = "smartdoc";

	// TODO Need to bring the setting out

	public SystemDatabase() throws DatabasePoolException, InstantiationException, IllegalAccessException, ClassNotFoundException {
		dbPool = new com.flabser.dataengine.pool.DBConnectionPool();
		dbPool.initConnectionPool(jdbcDriver, connectionURL, dbUser, dbUserPwd);

		HashMap<String, String> queries = new HashMap<>();
		queries.put("USERS", DDEScripts.getUsersDDE());
		queries.put("APPS", DDEScripts.getAppsDDE());
		queries.put("USERSACTIVITY", DDEScripts.getUsersActivityDDE());
		queries.put("HOLIDAYS", DDEScripts.getHolidaysDDE());
		queries.put("GROUPS", DDEScripts.GROUPS_DDE);
		queries.put("ROLES", DDEScripts.ROLES_DDE);

		createTable(queries);
	}

	@Override
	public IActivity getActivity() {
		return new Activity(dbPool);
	}

	@Override
	public User checkUserHash(String login, String pwd, String loginHash) {
		User user = initUser(login);
		if(user == null) return new User();

		if(checkHash(loginHash, user.getLoginHash())){
			user.isAuthorized = true;
			return user;
		}

		if(user.getPasswordHash() != null && user.getPasswordHash().trim().length() > 0) {
			String pwdHash = user.getPasswordHash().length() < 11 ? pwd.hashCode() + "" : RealmBase.Digest(pwd, "MD5", "UTF-8");
			if(user.getPasswordHash().equals(pwdHash)){
				user.isAuthorized = true;
				pswToPswHash(user, pwd);
			}
		} else {
			if(pwd != null && pwd.trim().length() > 0 && pwd.equals(user.getPwd())){
				user.isAuthorized = true;
				pswToPswHash(user, pwd);
			}
		}

		return user;
	}

	private void pswToPswHash(User user, String pwd){

		Connection conn = dbPool.getConnection();
		String pwdHsh = RealmBase.Digest(pwd, "MD5", "UTF-8");

		try (PreparedStatement pst = conn.prepareStatement("update USERS set PWD = '', PWDHASH = ? where ID = ?")){
			pst.setString(1, pwdHsh);
			pst.setInt(2, user.id);
			pst.executeUpdate();
		} catch (SQLException e) {
			DatabaseUtil.debugErrorPrint(e);
		} finally {
			dbPool.returnConnection(conn);
		}
	}

	private User initUser(String login) {

		Connection conn = dbPool.getConnection();
		ResultSet rs = null;
		try (PreparedStatement getUser = conn
				.prepareStatement("select ID, USERNAME, PRIMARYREGDATE, REGDATE, LOGIN, EMAIL, ISSUPERVISOR, PWD, PWDHASH, DEFAULTDBPWD, LOGINHASH, VERIFYCODE, STATUS, APPS, ROLES, GROUPS from users where login = ? limit 1;")) {

			getUser.setString(1, login);
			rs = getUser.executeQuery();

			if (rs.next()) {

				HashMap<String, ApplicationProfile> apps = new HashMap<>();
				getApplicationProfiles(Arrays.asList((Integer[]) getObjectArray(rs.getArray("APPS")))).forEach(app -> apps.put(app.appID, app));

				return new User(rs.getInt("ID"), rs.getString("USERNAME"), rs.getDate("PRIMARYREGDATE"), rs.getDate("REGDATE"), rs.getString("LOGIN"),
						rs.getString("EMAIL"), rs.getBoolean("ISSUPERVISOR"), rs.getString("PWD"), rs.getString("PWDHASH"), rs.getString("DEFAULTDBPWD"),
						rs.getInt("LOGINHASH"), rs.getString("VERIFYCODE"), UserStatusType.getType(rs.getInt("STATUS")), new HashSet<>(
						getUserGroups(Arrays.asList((Integer[]) getObjectArray(rs.getArray("GROUPS"))))), new HashSet<>(
						getUserRoles(Arrays.asList((Integer[]) getObjectArray(rs.getArray("ROLES"))))), apps, true);
			}

		} catch (SQLException e) {
			DatabaseUtil.debugErrorPrint(e);
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
			} catch (SQLException ignored) {
			}
			dbPool.returnConnection(conn);
		}

		return null;
	}

	public List<UserRole> getUserRoles(Collection<Integer> ids) {
		List<UserRole> result = new ArrayList<>();
		Connection conn = dbPool.getConnection();
		try (Statement getApps = conn.createStatement();
			 ResultSet rs = getApps.executeQuery("select id, name, description, app_id, is_on from " + "(select unnest(ARRAY"
					 + ids.stream().collect(Collectors.toList()) + "::integer[]) as g_id) as ids inner join roles on ids.g_id = ID ")) {

			while (rs.next()) {
				result.add(new UserRole(rs.getInt("id"), rs.getString("name"), rs.getString("description"), rs.getInt("app_id"), rs.getBoolean("is_on")));
			}
		} catch (SQLException e) {
			DatabaseUtil.debugErrorPrint(e);
		} finally {
			dbPool.returnConnection(conn);
		}

		return result;
	}

	public List<UserGroup> getUserGroups(Collection<Integer> ids) {
		List<UserGroup> result = new ArrayList<>();
		Connection conn = dbPool.getConnection();
		try (Statement getApps = conn.createStatement();
			 ResultSet rs = getApps.executeQuery("select id, name, description, roles_id from " + "(select unnest(ARRAY"
					 + ids.stream().collect(Collectors.toList()) + "::integer[]) as g_id) as ids inner join groups on ids.g_id = ID ")) {

			while (rs.next()) {
				result.add(new UserGroup(rs.getInt("id"), rs.getString("name"), rs.getString("description"), new HashSet<>(getUserRoles(Arrays
						.asList((Integer[]) getObjectArray(rs.getArray("roles_id")))))));
			}
		} catch (SQLException e) {
			DatabaseUtil.debugErrorPrint(e);
		} finally {
			dbPool.returnConnection(conn);
		}

		return result;
	}

	public static Object[] getObjectArray(Array array) throws SQLException {
		return array == null ? new Object[0] : (Object[]) array.getArray();
	}

	public List<ApplicationProfile> getApplicationProfiles(Collection<Integer> ids) {
		List<ApplicationProfile> result = new ArrayList<>();
		Connection conn = dbPool.getConnection();
		try (Statement getApps = conn.createStatement();
			 ResultSet rs = getApps
					 .executeQuery("select ID, APPTYPE, APPID, APPNAME, OWNER, DBTYPE, DBHOST, DBNAME, DBLOGIN, DBPWD, STATUS, STATUSDATE from "
							 + "(select unnest(ARRAY" + ids.stream().collect(Collectors.toList())
							 + "::integer[]) as app_id) as ids inner join apps on ids.app_id = ID ")) {

			while (rs.next()) {
				result.add(new ApplicationProfile(rs.getInt("ID"), rs.getString("APPTYPE"), rs.getString("APPID"), rs.getString("APPNAME"), rs
						.getString("OWNER"), rs.getInt("DBTYPE"), rs.getString("DBHOST"), rs.getString("DBNAME"), rs.getString("DBLOGIN"), rs
						.getString("DBPWD"), rs.getInt("STATUS"), rs.getDate("STATUSDATE")));
			}
		} catch (SQLException e) {
			DatabaseUtil.debugErrorPrint(e);
		} finally {
			dbPool.returnConnection(conn);
		}

		return result;

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

	public void createTable(HashMap<String, String> queries) {
		Connection conn = dbPool.getConnection();

		try (Statement stmt = conn.createStatement()) {

			conn.setAutoCommit(false);
			Set<String> tables = new HashSet<>();
			DatabaseMetaData dbmd = conn.getMetaData();
			String[] types = { "TABLE" };
			try (ResultSet rs = dbmd.getTables(null, null, "%", types)) {
				while (rs.next()) {
					tables.add((Optional.ofNullable(rs.getString("table_name")).orElse("")).toLowerCase());
				}
			}

			queries.forEach((tableName, query) -> {
				if (!tables.contains(tableName.toLowerCase())) {
					try {
						stmt.executeUpdate(query);
					} catch (SQLException e) {
						Server.logger.errorLogEntry("Unable to create table \"" + tableName + "\"");
						Server.logger.errorLogEntry(e);
					}
				}
			});

			conn.commit();
		} catch (SQLException e) {
			DatabaseUtil.debugErrorPrint(e);
		} finally {
			dbPool.returnConnection(conn);
		}
	}

	// requires that list of applications, roles and groups are exists
	@Override
	public int insert(User user) {
		Connection conn = dbPool.getConnection();

		try (PreparedStatement insertUser = conn
				.prepareStatement(
						"insert into USERS(USERNAME, LOGIN, EMAIL, PWD, ISSUPERVISOR, PRIMARYREGDATE, REGDATE, LOGINHASH, PWDHASH, LASTDEFAULTURL, STATUS, VERIFYCODE, APPS, ROLES, GROUPS, DEFAULTDBPWD) values(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)",
						PreparedStatement.RETURN_GENERATED_KEYS)) {

			insertUser.setString(1, user.getUserName());
			insertUser.setString(2, user.getLogin());
			insertUser.setString(3, user.getEmail());
			insertUser.setString(4, user.getPwd());
			insertUser.setBoolean(5, user.isSupervisor());
			insertUser.setDate(6, new java.sql.Date(new java.util.Date().getTime()));
			insertUser.setDate(7, user.getRegDate() != null ? new java.sql.Date(user.getRegDate().getTime()) : null);
			insertUser.setInt(8, (user.getLogin() + user.getPwd()).hashCode());
			insertUser.setString(9, user.getPasswordHash());
			insertUser.setString(10, user.lastURL);
			insertUser.setInt(11, user.getStatus().getCode());
			insertUser.setString(12, user.getVerifyCode());
			insertUser.setArray(13, conn.createArrayOf("integer", user.getApplications().stream().map(a -> a.id).toArray()));
			insertUser.setArray(14, conn.createArrayOf("integer", user.getUserRoles().stream().map(UserRole::getId).toArray()));
			insertUser.setArray(15, conn.createArrayOf("integer", user.getGroups().stream().map(UserGroup::getId).toArray()));
			insertUser.setString(16, user.getDefaultDbPwd());

			int key = 0;

			insertUser.executeUpdate();
			try (ResultSet rs = insertUser.getGeneratedKeys()) {
				if (rs.next()) {
					key = rs.getInt(1);
				}
			}

			conn.commit();
			return key;
		} catch (SQLException e) {
			DatabaseUtil.debugErrorPrint(e);
			return -1;
		} finally {
			dbPool.returnConnection(conn);
		}
	}

	// requires that list of applications, roles and groups are exists
	@Override
	public int update(User user) {
		Connection conn = dbPool.getConnection();

		try (PreparedStatement updateUser = conn.prepareStatement("update USERS set " + "USERNAME = ?, " + "LOGIN = ?, " + "EMAIL = ?, " + "PWD = ?, "
				+ "ISSUPERVISOR = ?, " + "PRIMARYREGDATE = ?, " + "REGDATE = ?, " + "LOGINHASH = ?, " + "PWDHASH = ?, " + "LASTDEFAULTURL = ?, "
				+ "STATUS = ?, " + "VERIFYCODE = ?, " + "APPS = ?, " + "ROLES = ?, " + "GROUPS = ? " + "where id = ?")) {

			updateUser.setString(1, user.getUserName());
			updateUser.setString(2, user.getLogin());
			updateUser.setString(3, user.getEmail());
			updateUser.setString(4, user.getPwd());
			updateUser.setBoolean(5, user.isSupervisor());
			updateUser.setDate(6, new java.sql.Date(new java.util.Date().getTime()));
			updateUser.setDate(7, user.getRegDate() != null ? new java.sql.Date(user.getRegDate().getTime()) : null);
			updateUser.setInt(8, (user.getLogin() + user.getPwd()).hashCode());
			updateUser.setString(9, user.getPasswordHash());
			updateUser.setString(10, user.lastURL);
			updateUser.setInt(11, user.getStatus().getCode());
			updateUser.setString(12, user.getVerifyCode());
			updateUser.setArray(13, conn.createArrayOf("integer", user.getApplications().stream().map(a -> a.id).toArray()));
			updateUser.setArray(14, conn.createArrayOf("integer", user.getUserRoles().stream().map(UserRole::getId).toArray()));
			updateUser.setArray(15, conn.createArrayOf("integer", user.getGroups().stream().map(UserGroup::getId).toArray()));
			updateUser.setInt(16, user.id);

			updateUser.executeUpdate();

			conn.commit();
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
			return (Integer.parseInt(hashAsString) == hash);
		} catch (NumberFormatException ignored) {}
		return false;
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
			String sql = "insert into APPS(APPNAME, OWNER, DBHOST, DBNAME, DBLOGIN, DBPWD) values(" + "'" + ap.appName + "','" + ap.owner + "','" + ap.dbHost
					+ "','" + ap.dbName + "','" + ap.dbLogin + "','" + ap.dbPwd + "')";

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
			String sql = "update APPS set APPNAME='" + ap.appName + "', OWNER='" + ap.owner + "',DBHOST='" + ap.dbHost + "', DBNAME='" + ap.dbName
					+ "', DBLOGIN = '" + ap.dbLogin + "',DBPWD='" + ap.dbPwd + "'";

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
		ResultSet rs = s.executeQuery("select apps from USERS where id = " + user.id);
		if (rs.next()) {
			Integer[] appsId = (Integer[])getObjectArray(rs.getArray("apps"));
			List<ApplicationProfile> list = getApplicationProfiles(Arrays.asList(appsId));
			for (ApplicationProfile applicationProfile : list) {
				user.getApplicationProfiles().put(applicationProfile.appName, applicationProfile);
			}
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