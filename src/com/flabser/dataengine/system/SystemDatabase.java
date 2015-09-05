package com.flabser.dataengine.system;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.sql.Array;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.catalina.realm.RealmBase;

import com.flabser.dataengine.DatabaseCore;
import com.flabser.dataengine.DatabaseUtil;
import com.flabser.dataengine.activity.Activity;
import com.flabser.dataengine.activity.IActivity;
import com.flabser.dataengine.pool.DatabasePoolException;
import com.flabser.dataengine.system.entities.ApplicationProfile;
import com.flabser.dataengine.system.entities.UserGroup;
import com.flabser.dataengine.system.entities.UserRole;
import com.flabser.env.EnvConst;
import com.flabser.env.Environment;
import com.flabser.restful.data.AttachedFile;
import com.flabser.rule.constants.RunMode;
import com.flabser.server.Server;
import com.flabser.users.User;
import com.flabser.users.UserStatusType;

@SuppressWarnings({ "SqlDialectInspection", "SqlNoDataSourceInspection" })
public class SystemDatabase extends DatabaseCore implements ISystemDatabase{
	public static final String jdbcDriver = "org.postgresql.Driver";

	public SystemDatabase() throws DatabasePoolException, InstantiationException, IllegalAccessException, ClassNotFoundException {
		pool = new com.flabser.dataengine.pool.DBConnectionPool();
		pool.initConnectionPool(jdbcDriver, EnvConst.CONN_URI, EnvConst.DB_USER, EnvConst.DB_PWD);

		HashMap<String, String> queries = new HashMap<>();
		queries.put("USERS", DDEScripts.USERS_DDE);
		queries.put("APPS", DDEScripts.APPS_DDE);
		queries.put("USERSACTIVITY", DDEScripts.USERS_ACTIVITY_DDE);
		queries.put("HOLIDAYS", DDEScripts.HOLIDAYS_DDE);
		queries.put("GROUPS", DDEScripts.GROUPS_DDE);
		queries.put("ROLES", DDEScripts.ROLES_DDE);

		createTable(queries);
	}

	@Override
	public IActivity getActivity() {
		return new Activity(pool);
	}

	@Override
	public User checkUserHash(String login, String pwd, String loginHash) {
		User user = initUser(login);
		if (user == null) {
			return new User();
		}

		if (checkHash(loginHash, user.getLoginHash())) {
			user.isAuthorized = true;
			return user;
		}

		if (pwd == null) {
			pwd = "";
		}
		if (user.getPasswordHash() != null && user.getPasswordHash().trim().length() > 0) {
			String pwdHash = user.getPasswordHash().length() < 11 ? String.valueOf(pwd.hashCode()) : RealmBase.Digest(pwd, "MD5", "UTF-8");
			if (user.getPasswordHash().equals(pwdHash)) {
				user.isAuthorized = true;
				pswToPswHash(user, pwd);
			}
		} else {
			if (pwd.equals(user.getPwd())) {
				user.isAuthorized = true;
				pswToPswHash(user, pwd);
			}
		}

		if (user.isAuthorized == false) {
			user = new User();
		}
		return user;
	}

	private void pswToPswHash(User user, String pwd) {

		Connection conn = pool.getConnection();
		String pwdHsh = RealmBase.Digest(pwd, "MD5", "UTF-8");

		try (PreparedStatement pst = conn.prepareStatement("update USERS set PWD = ?, PWDHASH = ? where ID = ?")) {
			pst.setString(1, null);
			pst.setString(2, pwdHsh);
			pst.setLong(3, user.id);
			pst.executeUpdate();

			conn.commit();
		} catch (SQLException e) {
			DatabaseUtil.debugErrorPrint(e);
		} finally {
			pool.returnConnection(conn);
		}
	}

	private User initUser(String login) {

		Connection conn = pool.getConnection();
		try (PreparedStatement getUser = conn.prepareStatement("select ID from users where login = ? limit 1;")) {

			getUser.setString(1, login);
			try (ResultSet rs = getUser.executeQuery()) {
				if (rs.next()) {
					return getUser(rs.getInt("ID"));
				}
			}

			conn.commit();
		} catch (SQLException e) {
			DatabaseUtil.debugErrorPrint(e);
		} finally {
			pool.returnConnection(conn);
		}

		return null;
	}

	public ArrayList<UserRole> getUserRoles(Collection<Integer> ids) {
		ArrayList<UserRole> result = new ArrayList<>();
		Connection conn = pool.getConnection();
		try (Statement getApps = conn.createStatement();
				ResultSet rs = getApps.executeQuery("select id, name, description, app_id, is_on from " + "(select unnest(ARRAY"
						+ ids.stream().collect(Collectors.toList()) + "::integer[]) as g_id) as ids inner join roles on ids.g_id = ID ")) {

			while (rs.next()) {
				result.add(new UserRole(rs.getInt("id"), rs.getString("name"), rs.getString("description"), rs.getInt("app_id"), RunMode
						.getType(rs.getInt("is_on"))));
			}

			conn.commit();
		} catch (SQLException e) {
			DatabaseUtil.debugErrorPrint(e);
		} finally {
			pool.returnConnection(conn);
		}

		return result;
	}

	public List<UserGroup> getUserGroups(Collection<Integer> ids) {
		List<UserGroup> result = new ArrayList<>();
		Connection conn = pool.getConnection();
		try (Statement getApps = conn.createStatement();
				ResultSet rs = getApps.executeQuery("select id, name, description, roles_id from " + "(select unnest(ARRAY"
						+ ids.stream().collect(Collectors.toList()) + "::integer[]) as g_id) as ids inner join groups on ids.g_id = ID ")) {

			while (rs.next()) {
				result.add(new UserGroup(rs.getInt("id"), rs.getString("name"), rs.getString("description"), new HashSet<>(
						getUserRoles(Arrays.asList((Integer[]) getObjectArray(rs.getArray("roles_id")))))));
			}

			conn.commit();
		} catch (SQLException e) {
			DatabaseUtil.debugErrorPrint(e);
		} finally {
			pool.returnConnection(conn);
		}

		return result;
	}

	public static Object[] getObjectArray(Array array) throws SQLException {
		return array == null ? new Object[0] : (Object[]) array.getArray();
	}

	public List<ApplicationProfile> getApplicationProfiles(Collection<Integer> ids) {
		List<ApplicationProfile> result = new ArrayList<>();
		Connection conn = pool.getConnection();
		try (Statement getApps = conn.createStatement();
				ResultSet rs = getApps.executeQuery("select ID, APPTYPE, APPID, APPNAME, OWNER, DBTYPE, DBHOST, DBNAME, STATUS, STATUSDATE"
						+ ", ARRAY(select id FROM roles where app_id = apps.ID) as roles_id from " + "(select unnest(ARRAY"
						+ ids.stream().collect(Collectors.toList()) + "::integer[]) as app_id) as ids inner join apps on ids.app_id = ID ")) {

			while (rs.next()) {
				result.add(new ApplicationProfile(rs.getInt("ID"), rs.getString("APPTYPE"), rs.getString("APPID"), rs.getString("APPNAME"),
						rs.getString("OWNER"), rs.getInt("DBTYPE"), rs.getString("DBHOST"), rs.getString("DBNAME"), rs.getInt("STATUS"), rs
						.getDate("STATUSDATE"), getUserRoles(Arrays.asList((Integer[]) getObjectArray(rs.getArray("roles_id"))))));
			}

			conn.commit();
		} catch (SQLException e) {
			DatabaseUtil.debugErrorPrint(e);
		} finally {
			pool.returnConnection(conn);
		}

		return result;

	}

	// risk of sql injection
	@Override
	public int getAllUsersCount(String condition) {
		int count = 0;
		String wherePiece = "";
		if (!condition.equals("")) {
			wherePiece = "WHERE " + condition;
		}

		Connection conn = pool.getConnection();
		try (Statement s = conn.createStatement(); ResultSet rs = s.executeQuery("select count(*) from USERS " + wherePiece)) {

			if (rs.next()) {
				count = rs.getInt(1);
			}
			conn.commit();
			return count;
		} catch (SQLException e) {
			DatabaseUtil.debugErrorPrint(e);
			return 0;
		} finally {
			pool.returnConnection(conn);
		}
	}

	// risk of sql injection
	// identical to getAllUsersCount(String condition)
	@Override
	public int getUsersCount(String condition) {
		return getAllUsersCount(condition);
	}

	@Override
	public HashMap<String, User> getAllAdministrators() {
		HashMap<String, User> users = new HashMap<>();
		Connection conn = pool.getConnection();

		try (Statement s = conn.createStatement();
				ResultSet rs = s.executeQuery("SELECT ARRAY(SELECT ID FROM USERS WHERE ISSUPERVISOR = TRUE) as IDS")) {

			if (rs.next()) {
				List<User> userList = getUsers((Integer[]) getObjectArray(rs.getArray("IDS")));
				userList.forEach(u -> users.put(u.getLogin(), u));
			}

			conn.commit();
			return users;
		} catch (Throwable e) {
			DatabaseUtil.debugErrorPrint(e);
		} finally {
			pool.returnConnection(conn);
		}

		return null;
	}

	@Override
	public User getUserByVerifyCode(String code) {
		Connection conn = pool.getConnection();
		try (PreparedStatement pstmt = conn.prepareStatement("select ID from USERS where USERS.VERIFYCODE = ?")) {

			conn.setAutoCommit(true);
			pstmt.setString(1, code);
			try (ResultSet rs = pstmt.executeQuery()) {
				if (rs.next()) {
					return getUser(rs.getInt("ID"));
				}
			}

		} catch (SQLException e) {
			DatabaseUtil.debugErrorPrint(e);
		} finally {
			pool.returnConnection(conn);
		}

		return null;
	}

	@SuppressWarnings("unused")
	public User getUserByEmail(String email) {

		Connection conn = pool.getConnection();
		try (PreparedStatement pstmt = conn.prepareStatement("select ID from USERS where USERS.EMAIL = ?")) {
			pstmt.setString(1, email);

			try (ResultSet rs = pstmt.executeQuery()) {
				if (rs.next()) {
					return getUser(rs.getInt("ID"));
				}
			}

			conn.commit();
		} catch (SQLException e) {
			DatabaseUtil.debugErrorPrint(e);
		} finally {
			pool.returnConnection(conn);
		}

		return null;
	}

	private List<User> getUsers(Integer... ids) {

		Connection conn = pool.getConnection();
		List<User> result = new ArrayList<>();
		if (ids == null) {
			return result;
		}

		try (Statement stmt = conn.createStatement();
				ResultSet rs = stmt
						.executeQuery("select ID, USERNAME, PRIMARYREGDATE, REGDATE, LOGIN, EMAIL, ISSUPERVISOR, PWD, PWDHASH, DBPWD, LOGINHASH, VERIFYCODE, STATUS, APPS, ROLES, GROUPS "
								+ "FROM (SELECT unnest(ARRAY"
								+ Arrays.toString(ids)
								+ "::integer[]) as u_id) as ids inner join users on ids.u_id = ID")) {

			while (rs.next()) {
				result.add(new User(rs.getInt("ID"), rs.getString("USERNAME"), rs.getDate("PRIMARYREGDATE"), rs.getDate("REGDATE"), rs
						.getString("LOGIN"), rs.getString("EMAIL"), rs.getBoolean("ISSUPERVISOR"), rs.getString("PWD"), rs
						.getString("PWDHASH"), rs.getString("DBPWD"), rs.getInt("LOGINHASH"), rs.getString("VERIFYCODE"), UserStatusType
						.getType(rs.getInt("STATUS")), new HashSet<>(getUserGroups(Arrays.asList((Integer[]) getObjectArray(rs
								.getArray("GROUPS"))))), new HashSet<>(
										getUserRoles(Arrays.asList((Integer[]) getObjectArray(rs.getArray("ROLES"))))), getApplicationProfiles(Arrays
												.asList((Integer[]) getObjectArray(rs.getArray("APPS")))), true));
			}

			conn.commit();
		} catch (SQLException e) {
			DatabaseUtil.debugErrorPrint(e);
		} finally {
			pool.returnConnection(conn);
		}

		return result;
	}

	@Override
	public User getUser(int id) {
		List<User> users = getUsers(id);
		if (users.size() == 0) {
			return null;
		}
		return users.get(0);
	}

	@Override
	public User getUser(long id) {
		List<User> users = getUsers((int)id);
		if (users.size() == 0) {
			return null;
		}
		return users.get(0);
	}

	// identical to initUser(String login)
	@Override
	public User getUser(String id) {
		return initUser(id);
	}

	@Override
	public int deleteUser(int id) {

		Connection conn = pool.getConnection();
		try (PreparedStatement pst = conn.prepareStatement("delete from USERS where ID = ?")) {

			pst.setInt(1, id);
			pst.executeUpdate();

			conn.commit();
			return 1;
		} catch (SQLException e) {
			DatabaseUtil.errorPrint(e);
		} finally {
			pool.returnConnection(conn);
		}

		return -1;
	}

	public void createTable(HashMap<String, String> queries) {
		Connection conn = pool.getConnection();

		try (Statement stmt = conn.createStatement()) {

			conn.setAutoCommit(false);
			Set<String> tables = new HashSet<>();
			DatabaseMetaData dbmd = conn.getMetaData();
			String[] types = { "TABLE" };
			try (ResultSet rs = dbmd.getTables(null, null, "%", types)) {
				while (rs.next()) {
					tables.add(Optional.ofNullable(rs.getString("table_name")).orElse("").toLowerCase());
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
			pool.returnConnection(conn);
		}
	}

	@Override
	public int insert(User user) {
		Connection conn = pool.getConnection();

		try (PreparedStatement insertUser = conn
				.prepareStatement(
						"insert into USERS(USERNAME, LOGIN, EMAIL, PWD, ISSUPERVISOR, PRIMARYREGDATE, REGDATE, LOGINHASH, PWDHASH, LASTDEFAULTURL, STATUS, VERIFYCODE, APPS, ROLES, GROUPS, DBPWD,AVATAR, AVATARNAME) values(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?,?,?)",
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
			insertUser.setString(16, user.getDbPwd());
			AttachedFile aFile = user.getAvatar();
			if(aFile != null){
				File userTmpDir = new File(Environment.tmpDir + File.separator + user.getLogin());
				if (userTmpDir.exists()) {
					String uploadedFileLocation = userTmpDir + File.separator + aFile.tempID;
					File avatarFile = new File(uploadedFileLocation);
					if (avatarFile.exists()) {
						try {
							InputStream is = new FileInputStream(avatarFile);
							insertUser.setBinaryStream(17, is, (int)avatarFile.length());
							insertUser.setString(18, aFile.realFileName);
						} catch (FileNotFoundException e) {
							Server.logger.errorLogEntry(e);
						}

					}
				}
			}

			insert(user.getUserRoles());

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
			pool.returnConnection(conn);
		}
	}

	@Override
	public int update(User user) {
		Connection conn = pool.getConnection();

		try (PreparedStatement updateUser = conn.prepareStatement("update USERS set " + "USERNAME = ?, " + "LOGIN = ?, " + "EMAIL = ?, "
				+ "PWD = ?, " + "ISSUPERVISOR = ?, " + "PRIMARYREGDATE = ?, " + "REGDATE = ?, " + "LOGINHASH = ?, " + "PWDHASH = ?, "
				+ "LASTDEFAULTURL = ?, " + "STATUS = ?, " + "VERIFYCODE = ?, " + "APPS = ?, " + "ROLES = ?, " + "GROUPS = ?, DBPWD = ? "
				+ "where id = ?")) {

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
			updateUser.setString(16, user.getDbPwd());
			updateUser.setLong(17, user.id);

			AttachedFile aFile = user.getAvatar();
			if(aFile != null){
				File userTmpDir = new File(Environment.tmpDir + File.separator + user.getOldLogin());
				if (userTmpDir.exists()) {
					String uploadedFileLocation = userTmpDir + File.separator + aFile.tempID;
					File avatarFile = new File(uploadedFileLocation);
					if (avatarFile.exists()) {
						try {
							InputStream is = new FileInputStream(avatarFile);
							updateUser.setBinaryStream(17, is, (int)avatarFile.length());
							updateUser.setString(18, aFile.realFileName);
						} catch (FileNotFoundException e) {
							Server.logger.errorLogEntry(e);
						}

					}
				}
			}

			updateUser.executeUpdate();

			conn.commit();
			return (int) user.id;
		} catch (Throwable e) {
			DatabaseUtil.debugErrorPrint(e);
			return -1;
		} finally {
			pool.returnConnection(conn);
		}
	}

	@Override
	public ArrayList<User> getUsers(String keyWord) {
		ArrayList<User> users = new ArrayList<>();

		Connection conn = pool.getConnection();
		try (PreparedStatement pst = conn.prepareStatement("select ARRAY(SELECT ID from USERS where USERS.LOGIN LIKE ?) AS IDS")) {
			conn.setAutoCommit(true);
			pst.setString(1, keyWord + "%");
			try (ResultSet rs = pst.executeQuery()) {
				if (rs.next()) {
					return (ArrayList<User>) getUsers((Integer[]) getObjectArray(rs.getArray("IDS")));
				}
			}
		} catch (SQLException e) {
			DatabaseUtil.debugErrorPrint(e);
		} finally {
			pool.returnConnection(conn);
		}

		return users;
	}

	private boolean checkHash(String hashAsString, int hash) {
		try {
			return Integer.parseInt(hashAsString) == hash;
		} catch (NumberFormatException ignored) {
		}
		return false;
	}

	// risk of sql injection
	@Override
	public ArrayList<User> getAllUsers(String condition, int calcStartEntry, int pageSize) {

		ArrayList<User> users = null;
		String wherePiece = "";
		if (!condition.equals("")) {
			wherePiece = "WHERE " + condition;
		}

		Connection conn = pool.getConnection();
		try (Statement s = conn.createStatement();
				ResultSet rs = s.executeQuery("select ARRAY(select ID from USERS " + wherePiece + " LIMIT " + pageSize + " OFFSET "
						+ calcStartEntry + ") as IDS;")) {

			if (rs.next()) {
				users = (ArrayList<User>) getUsers((Integer[]) getObjectArray(rs.getArray("IDS")));
			}

			conn.commit();
		} catch (SQLException e) {
			DatabaseUtil.debugErrorPrint(e);
		} finally {
			pool.returnConnection(conn);
		}

		return users;
	}

	@Override
	public IApplicationDatabase getApplicationDatabase() throws InstantiationException, IllegalAccessException, ClassNotFoundException {
		return new ApplicationDatabase();
	}

	public void insert(Collection<UserRole> roles) {

		Connection conn = pool.getConnection();

		try (PreparedStatement pstmt = conn.prepareStatement("" + "with sel as ( select id from roles where name = ? and app_id = ?), "
				+ " ins as ( insert into roles (name, description, app_id, is_on) " + "    select ?, ?, ?, ? "
				+ "    where not exists (select id from roles where name = ? and app_id = ?) " + "    returning id " + ") "
				+ "select id from ins " + "union all " + "select id from sel")) {

			for (UserRole role : roles) {
				if (role.getId() > 0) {
					continue;
				}

				pstmt.setString(1, role.getName());
				pstmt.setInt(2, role.getAppId());

				pstmt.setString(3, role.getName());
				pstmt.setString(4, role.getDescription());
				pstmt.setInt(5, role.getAppId());
				pstmt.setInt(6, role.getIsOn().getCode());

				pstmt.setString(7, role.getName());
				pstmt.setInt(8, role.getAppId());

				try (ResultSet rs = pstmt.executeQuery()) {
					if (rs.next()) {
						role.setId(rs.getInt(1));
					}
				}
			}

			conn.commit();
		} catch (SQLException e) {
			e.printStackTrace();
			DatabaseUtil.debugErrorPrint(e);
		} finally {
			pool.returnConnection(conn);
		}
	}

	@Override
	public int insert(ApplicationProfile ap) {
		Connection conn = pool.getConnection();

		try (PreparedStatement pst = conn
				.prepareStatement(
						"insert into APPS(APPNAME, OWNER, DBHOST, DBNAME, APPTYPE, APPID, DBTYPE, STATUS, STATUSDATE) values(?, ?, ?, ?, ?, ?, ?, ?, ?);",
						PreparedStatement.RETURN_GENERATED_KEYS)) {

			pst.setString(1, ap.appName);
			pst.setString(2, ap.owner);
			pst.setString(3, ap.dbHost);
			pst.setString(4, ap.dbName);
			pst.setString(5, ap.appType);
			pst.setString(6, ap.appID);
			pst.setInt(7, ap.dbType.getCode());
			pst.setInt(8, ap.status.getCode());
			pst.setDate(9, ap.getStatusDate() != null ? new java.sql.Date(ap.getStatusDate().getTime()) : null);

			pst.executeUpdate();
			try (ResultSet rs = pst.getGeneratedKeys()) {
				if (rs.next()) {
					ap.id = rs.getInt(1);
					ap.getRoles().stream().forEach(r -> r.setAppId(ap.id));
				}
			}

			insert(ap.getRoles());

			try (Statement s = conn.createStatement()) {
				s.executeUpdate("delete from roles where app_id = " + ap.id +
						(ap.getRoles().size() > 0
								? " and id not in " + ap.getRoles().stream().map(r -> String.valueOf(r.getId())).collect(Collectors.joining(", ", "(", ")"))
										: "")
						);
			}

			conn.commit();
			return ap.id;
		} catch (SQLException e) {
			DatabaseUtil.debugErrorPrint(e);
			return -1;
		} finally {
			pool.returnConnection(conn);
		}
	}

	@Override
	public int update(ApplicationProfile ap) {
		Connection conn = pool.getConnection();

		try (PreparedStatement pst = conn
				.prepareStatement("UPDATE APPS SET APPNAME = ?, OWNER = ?, DBHOST = ?, DBNAME = ?,  APPTYPE = ?, APPID = ?, DBTYPE = ?, STATUS = ?, STATUSDATE = ? WHERE ID = ?;")) {

			pst.setString(1, ap.appName);
			pst.setString(2, ap.owner);
			pst.setString(3, ap.dbHost);
			pst.setString(4, ap.dbName);
			pst.setString(5, ap.appType);
			pst.setString(6, ap.appID);
			pst.setInt(7, ap.dbType.getCode());
			pst.setInt(8, ap.status.getCode());
			pst.setDate(9, ap.getStatusDate() != null ? new java.sql.Date(ap.getStatusDate().getTime()) : null);
			pst.setInt(10, ap.id);

			pst.executeUpdate();
			insert(ap.getRoles());

			try (Statement s = conn.createStatement()) {
				s.executeUpdate("delete from roles where app_id = " + ap.id +
						(ap.getRoles().size() > 0
								? " and id not in " + ap.getRoles().stream().map(r -> String.valueOf(r.getId())).collect(Collectors.joining(", ", "(", ")"))
										: "")
						);
			}

			conn.commit();
			return ap.id;
		} catch (SQLException e) {
			DatabaseUtil.debugErrorPrint(e);
			return -1;
		} finally {
			pool.returnConnection(conn);
		}
	}

	@Override
	public int deleteApplicationProfile(int id) {
		Connection conn = pool.getConnection();

		try {
			PreparedStatement pst = conn.prepareStatement("DELETE FROM APPS WHERE ID = ?;");
			pst.setInt(1, id);
			pst.executeUpdate();

			conn.commit();
			return 0;
		} catch (SQLException e) {
			DatabaseUtil.debugErrorPrint(e);
			return -1;
		} finally {
			pool.returnConnection(conn);
		}
	}

	// risk of sql injection
	@Override
	public ArrayList<ApplicationProfile> getAllApps(String condition, int calcStartEntry, int pageSize) {
		ArrayList<ApplicationProfile> apps = new ArrayList<>();
		String wherePiece = "";
		if (!condition.equals("")) {
			wherePiece = "WHERE " + condition;
		}

		Connection conn = pool.getConnection();
		try (Statement s = conn.createStatement();
				ResultSet rs = s.executeQuery("select * from APPS " + wherePiece + " LIMIT " + pageSize + " OFFSET " + calcStartEntry)) {

			while (rs.next()) {
				apps.add(new ApplicationProfile(rs));
			}

			conn.commit();
			return apps;
		} catch (SQLException e) {
			DatabaseUtil.debugErrorPrint(e);
			return null;
		} finally {
			pool.returnConnection(conn);
		}
	}


}
