package com.flabser.dataengine.system;

import org.apache.catalina.realm.RealmBase;

import com.flabser.appenv.AppEnv;
import com.flabser.dataengine.Const;
import com.flabser.dataengine.DatabaseUtil;
import com.flabser.dataengine.activity.*;
import com.flabser.dataengine.pool.DatabasePoolException;
import com.flabser.dataengine.pool.IDBConnectionPool;
import com.flabser.users.TempUser;
import com.flabser.users.User;
import com.flabser.users.ApplicationProfile;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;


public class SystemDatabase implements ISystemDatabase, Const {
	public static boolean isValid;
	public static String jdbcDriver = "org.postgresql.Driver";
	private IDBConnectionPool dbPool;
	static String connectionURL = "jdbc:postgresql://localhost/nbsystem";
	static String dbUser = "postgres";
	static String dbUserPwd = "3287";

	public SystemDatabase() throws DatabasePoolException, InstantiationException, IllegalAccessException, ClassNotFoundException{		
		dbPool = new com.flabser.dataengine.pool.DBConnectionPool();
		dbPool.initConnectionPool(jdbcDriver, connectionURL, dbUser, dbUserPwd);
		Connection conn = dbPool.getConnection();
		try{
			conn.setAutoCommit(false);
			createUserTable(DDEScripts.getUsersDDE(), "USERS");
			createUserTable(DDEScripts.getEnabledAppDDE(), "APPS");
			createUserTable(DDEScripts.getUserRolesDDE(), "USER_ROLES");
			createUserTable(DDEScripts.getGroupsDDE(), "GROUPS");
			createUserTable(DDEScripts.getUserGroupsDDE(), "USER_GROUPS");
			createUserTable(DDEScripts.getUsersActivityDDE(), "USERS_ACTIVITY");
			createUserTable(DDEScripts.getHolidaysDDE(), "HOLIDAYS");
         	isValid = true;
			conn.commit();
		}catch(Throwable e){
            AppEnv.logger.errorLogEntry(e);
            e.printStackTrace();
			DatabaseUtil.debugErrorPrint(e);
		}finally{
			dbPool.returnConnection(conn);
		}
	}

	@Override
	public IActivity getActivity() {	
		return new Activity(dbPool);
	}

	
	public int calcStartEntry(int pageNum, int pageSize){
		int pageNumMinusOne = pageNum;
		pageNumMinusOne -- ;
		return pageNumMinusOne * pageSize;
	}

	public User checkUser(String login, String pwd, User user) {
		Connection conn = dbPool.getConnection();
		
		try{
			conn.setAutoCommit(false);
			Statement s = conn.createStatement();
			String sql = "select * from USERS, APPS where USERS.DOCID = APPS.DOCID and USERID = '" + login + "'";
			ResultSet rs = s.executeQuery(sql);
			String password = "";

			if (rs.next()){
				
				if(rs.getString("PWDHASH") != null){
					if(!rs.getString("PWDHASH").trim().equals("")){
						password = rs.getString("PWDHASH");
						String pwdHash = "";
						if(password.length() < 30){
							pwdHash = pwd.hashCode() + "";
						}else{
							//pwdHash = getMD5Hash(pwd);
							RealmBase rb = null;
							pwdHash = rb.Digest(pwd, "MD5", "UTF-8");
							
							
						}
						
						if(pwdHash.equals(password)){
							user = initUser(conn, rs, login);
							user.isAuthorized = true;
							if(password.length() < 11){//!!!!
								pswToPswHash(user, pwd);
							}
							
						}
					}else{
						password = rs.getString("PWD");
						if(pwd.equals(password)){
							user = initUser(conn, rs, login);
							user.isAuthorized = true;	
							pswToPswHash(user, password);
						}	
					}		
				}else{
					password = rs.getString("PWD");
					if(pwd.equals(password)){
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
		}catch(Throwable e){		
			DatabaseUtil.debugErrorPrint(e);
			return null;
		} finally {
			dbPool.returnConnection(conn);		
		}
	}

	public User checkUser(String login, String pwd, String hashAsText, User user) {
		Connection conn = dbPool.getConnection();		
		try{
			conn.setAutoCommit(false);
			Statement s = conn.createStatement();
			String sql = "select * from USERS, APPS where USERS.DOCID = APPS.DOCID and USERID = '" + login + "'";
			ResultSet rs = s.executeQuery(sql);
			String password = "";

			if (rs.next()){			
				
				//.trim().equals("")
				if(rs.getString("PWDHASH") != null){
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
				}else{
					password = rs.getString("PWD");
					int hash = rs.getInt("LOGINHASH");
					if(checkHash(hashAsText, hash)){
						user = initUser(conn, rs, login);
						user.isAuthorized = true;
					}else if(pwd.equals(password)){
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
		}catch(Throwable e){		
			DatabaseUtil.debugErrorPrint(e);
			return null;
		} finally {
			dbPool.returnConnection(conn);		
		}
	}
	
	public User checkUserHash(String login, String pwd, String hashAsText, User user) {
		Connection conn = dbPool.getConnection();	
		try{
			conn.setAutoCommit(false);
			Statement s = conn.createStatement();
			String sql = "select * from USERS, APPS where USERS.DOCID = APPS.DOCID and USERID = '" + login + "'";
			ResultSet rs = s.executeQuery(sql);
			String password = "";

			if (rs.next()){			
				
				//.trim().equals("")
				if(rs.getString("PWDHASH")!= null){
					if (!rs.getString("PWDHASH").trim().equals("")) {
						password = rs.getString("PWDHASH");
						String pwdHash = "";
						if(password.length() < 11){
							pwdHash = pwd.hashCode() + "";
						}else{
							pwdHash = RealmBase.Digest(pwd, "MD5", "UTF-8");
						}
						int hash = rs.getInt("LOGINHASH");
						if (checkHash(hashAsText, hash)) {
							user = initUser(conn, rs, login);
							user.isAuthorized = true;
						} else if (checkHashPSW(pwdHash, password)) {
							user = initUser(conn, rs,  login);
							user.isAuthorized = true;
							if(password.length() < 11){
								pswToPswHash(user, pwd);
							}
						}
					}else{
						password = rs.getString("PWD");
						int hash = rs.getInt("LOGINHASH");
						if(checkHash(hashAsText, hash)){
							user = initUser(conn, rs, login);
							user.isAuthorized = true;
						}else if(pwd.equals(password)){
							user = initUser(conn, rs, login);
							user.isAuthorized = true;
							pswToPswHash(user, password);
							//user.setPassword("");
							//user.setPasswordHash(password.hashCode()+"");
							
							//checkUserHash(login, pwd, hashAsText, user);
							
						}
					}
				}else{
					password = rs.getString("PWD");
					int hash = rs.getInt("LOGINHASH");
					if(checkHash(hashAsText, hash)){
						user = initUser(conn, rs, login);
						user.isAuthorized = true;
					}else if(pwd.equals(password)){
						user = initUser(conn, rs, login);
						user.isAuthorized = true;
						pswToPswHash(user, password);
						//user.setPassword("");
						//user.setPasswordHash(password.hashCode()+"");
						//checkUserHash(login, pwd, hashAsText, user);
					}
				}
			}
			rs.close();
			s.close();
			conn.commit();
			return user;
		}catch(Throwable e){		
			DatabaseUtil.debugErrorPrint(e);
			return null;
		} finally {
			dbPool.returnConnection(conn);		
		}
	}
	
	private void pswToPswHash(User user, String pwd) throws SQLException{
		Connection conn = dbPool.getConnection();
		conn.setAutoCommit(false);
		//String pwdHsh = pwd.hashCode()+"";
		//String pwdHsh = getMD5Hash(pwd);
		RealmBase rb = null;
		String pwdHsh = rb.Digest(pwd, "MD5", "UTF-8");
		
		String userUpdateSQL = "update USERS set PWD='', PWDHASH='" + pwdHsh + "'" +
					" where DOCID=" + user.docID;
		PreparedStatement pst = conn.prepareStatement(userUpdateSQL);
		pst.executeUpdate();
		conn.commit();
		conn.close();
	//	user.setPasswordHash(pwd);
	//	user.setPassword("");
	}
	
	private User initUser(Connection conn, ResultSet rs, String login) throws SQLException{
		boolean isNext = true;
		User user = new User();		
		user.fill(rs);			
		while(isNext||rs.next()){
			ApplicationProfile ap = new ApplicationProfile(rs);			
			user.enabledApps.put(ap.appName, ap);
			isNext = false;
		}	
		return user;		
		
	}

	@Override
	public int getAllUsersCount(String condition) {
		int count = 0;
		String wherePiece = "";
		Connection conn = dbPool.getConnection();
		try{	
			conn.setAutoCommit(false);
			if (!condition.equals(""))wherePiece = "WHERE " + condition; 
			Statement s = conn.createStatement();
			String sql = "select count(*) from USERS " + wherePiece;
			ResultSet rs = s.executeQuery(sql);
			if(rs.next()){
				count = rs.getInt(1);
			}
			rs.close();
			s.close();		
			conn.commit();
			return count;		}
		catch(Throwable e){			
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
		try{	
			conn.setAutoCommit(false);
			if (!condition.equals(""))wherePiece = "WHERE " + condition; 
			Statement s = conn.createStatement();
			String sql = "select count(*) from USERS " + wherePiece;
			ResultSet rs = s.executeQuery(sql);

			if(rs.next()){
				count = rs.getInt(1);
			}

			rs.close();
			s.close();	
			conn.commit();
		}
		catch(Throwable e){		
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
		try{
			conn.setAutoCommit(false);
			Statement s = conn.createStatement();
			String sql = "select * from USERS WHERE ISADMIN = 1";
			ResultSet rs = s.executeQuery(sql);

			while(rs.next()){
				User user = new User();
				user.fill(rs);			
				user.isValid = true;			
				users.put(user.getUserID(),user);
			}

			rs.close();
			s.close();	
			conn.commit();
			return users;
		}
		catch(Throwable e){		
			DatabaseUtil.debugErrorPrint(e);
			return null;
		} finally {	
			dbPool.returnConnection(conn);			
		}
	}

	public User getUser(String userID) {
		User user = new User();
		return reloadUserData(user, userID); 		
	}

	public User reloadUserData(User user, String userID) {

		Connection conn = dbPool.getConnection();
		try{
			conn.setAutoCommit(false);
			Statement s = conn.createStatement();			
			String sql = "select * from USERS where USERS.USERID='" + userID + "'";
			ResultSet rs = s.executeQuery(sql);

			if(rs.next()){
				user.fill(rs);				
				if (user.isValid){
					String addSQL = "select * from APPS where APPS.DOCID=" + user.docID;
					Statement statement = conn.createStatement();	
					ResultSet resultSet = statement.executeQuery(addSQL);
					while(resultSet.next()){
						ApplicationProfile ap = new ApplicationProfile(resultSet);
						user.enabledApps.put(ap.appName, ap);
					}
					resultSet.close();
					statement.close();
				}				
			}else{
				user.setUserID(userID);					
			}
			rs.close();	
			s.close();
			conn.commit();

		}catch(Throwable e){	
			DatabaseUtil.debugErrorPrint(e);
		}finally{	
			dbPool.returnConnection(conn);
		}
		return user;
	}

	public User getUser(int docID) {
		return null;
		/*User user = new User();
		Connection conn = dbPool.getConnection();
		try{
			conn.setAutoCommit(false);
			Statement s = conn.createStatement();			
			String sql = "select * from USERS, APPS where USERS.DOCID=APPS.DOCID and " +
					"USERS.DOCID=" + docID;
			String sql = "select * from USERS where USERS.DOCID=" + docID;
			ResultSet rs = s.executeQuery(sql);				
			if(rs.next()){
				user.fill(rs);			
				if (user.isValid){
					String addSQL = "select * from APPS where APPS.DOCID=" + docID;
					Statement statement = conn.createStatement();	
					ResultSet resultSet = statement.executeQuery(addSQL);
					while(resultSet.next()){
						UserApplicationProfile ap = new UserApplicationProfile(resultSet.getString("APP"),resultSet.getInt("LOGINMODE"));
						String qaSQL = "select * from QA where QA.DOCID=" + docID + " AND QA.APP='" + ap.appName + "'";
						Statement s1 = conn.createStatement();	
						ResultSet rs1 = s1.executeQuery(qaSQL);
						while(rs1.next()){
							ap.getQuestionAnswer().add(ap.new QuestionAnswer(rs1.getString("QUESTION"),rs1.getString("ANSWER")));
						}						
						user.enabledApps.put(ap.appName, ap);
					}
					resultSet.close();
					statement.close();
				}
			}else{
				user.setNewDoc(true);
			}
			rs.close();
			s.close();
			conn.commit();
		}catch(Throwable e){
			DatabaseUtil.debugErrorPrint(e);
		}finally{	
			dbPool.returnConnection(conn);
		}
		return user;*/
	}



	public boolean deleteUser(int docID) {
		Connection conn = dbPool.getConnection();
		try{
			conn.setAutoCommit(false);
			String delEnApp = "delete from APPS where DOCID = "+docID;
			PreparedStatement  pst = conn.prepareStatement(delEnApp);
			pst.executeUpdate();
			String delUserTab="delete from USERS where DOCID = " + docID;
			pst = conn.prepareStatement(delUserTab);			
			pst.executeUpdate();			
			conn.commit();
			pst.close();
			return true;
		}catch(Throwable e){
            DatabaseUtil.errorPrint(e);
			return false;
		} finally {	
			dbPool.returnConnection(conn);
		}

	} 


	public boolean hasUserTable(String tableName){
		Connection conn = dbPool.getConnection();
		try{	
			conn.setAutoCommit(false);
			Statement s = conn.createStatement();
			String sql = "select * from " + tableName;
			s.executeQuery(sql);
			s.close();
			conn.commit();
			return true;
		}catch(Throwable e){
			return false;
		} finally {
			dbPool.returnConnection(conn);
		}
	}

	public boolean createUserTable(String createTableScript, String tableName){
		Connection conn = dbPool.getConnection();
		boolean createUserTab = false;
		try{
			conn.setAutoCommit(false);
			Statement s = conn.createStatement();
			if(!hasUserTable(tableName)){
				if (s.execute(createTableScript)){
					AppEnv.logger.errorLogEntry("Unable to create table \"" + tableName + "\"");
				}
			}

			createUserTab = true;			
			s.close();
			conn.commit();
		}catch(Throwable e){
			DatabaseUtil.debugErrorPrint(e);
			createUserTab = false;
		} finally {		
			dbPool.returnConnection(conn);	
		}
		return createUserTab;
	}

	
	public int insert(User user) {
		Connection conn = dbPool.getConnection();
		try{
			conn.setAutoCommit(false);
			int key = 0;
			Statement stmt = conn.createStatement();
			
			ResultSet rs = stmt.executeQuery("select max(docid) from users");
			
			if(rs.next())
					key = rs.getInt(1) + 1;	
			String insertUser = "insert into USERS(DOCID, USERID, EMAIL, ISADMIN, LOGINHASH, PWDHASH )" +
						"values(" + 
					    key + ", " +
					    "'" + user.getUserID() + "', "  + 
						"'" + user.getEmail() + "'," + 
						 + user.getIsAdmin() + "," + 
						(user.getUserID() + user.getPassword()).hashCode() + ", '" + user.getPasswordHash() + "')";
			

			PreparedStatement pst = conn.prepareStatement(insertUser/*, PreparedStatement.RETURN_GENERATED_KEYS*/);
			pst.executeUpdate();
			 rs = pst.getGeneratedKeys();
			while(rs.next()){
				key = rs.getInt(1);
			}

		

			for(ApplicationProfile app: user.enabledApps.values()){				
				String insertURL = "insert into APPS(DOCID, APP)values(" + key + ", '" + app.appName +"')";
				pst = conn.prepareStatement(insertURL);
				pst.executeUpdate();	
				
			}

			conn.commit();
			pst.close();
			stmt.close();
			return 1;
		}catch(Throwable e){
			DatabaseUtil.debugErrorPrint(e);
			return - 1;
		}finally{		
			dbPool.returnConnection(conn);
		}
	}

	@Override
	public int update(User user) {
		return 0;
		/*Connection conn = dbPool.getConnection();
		try{
			conn.setAutoCommit(false);
			String pwdHsh="";
			String pwd="";
			if(user.getPasswordHash()!=null && !user.getPasswordHash().trim().equals("")){
				pwdHsh=user.getPasswordHash();
			}else{
				pwd = user.getPassword(); 
			}
			String userUpdateSQL = "update USERS set USERID='" + user.getUserID() + "'," +
					" EMAIL='" + user.getEmail() + "', INSTMSGADDR='" + user.getInstMsgAddress() + "'," + 
					"PWD='" + pwd + "', " +
					"ISADMIN = " + user.getIsAdmin() + "," +
					"LOGINHASH = " + (user.getUserID() + user.getPassword()).hashCode() +", " +
                    "PUBLICKEY = '" + user.getPublicKey() + "', PWDHASH='" + pwdHsh + "'" +
					" where DOCID=" + user.docID;
			PreparedStatement pst = conn.prepareStatement(userUpdateSQL);
			pst.executeUpdate();
			conn.commit();
			String delSQL = "delete from APPS where DOCID = " + user.docID;
			pst = conn.prepareStatement(delSQL);
			pst.executeUpdate();

			delSQL = "delete from QA where DOCID = " + user.docID;
			pst = conn.prepareStatement(delSQL);
			pst.executeUpdate();

			for(UserApplicationProfile app: user.enabledApps.values()){
				String insertURL = "insert into APPS(DOCID, APP, LOGINMODE)values (" + user.docID + ", '" + app.appName + "'," + app.loginMode + ")";
				PreparedStatement pst0 = conn.prepareStatement(insertURL);
				pst0.executeUpdate();

				delSQL = "delete from QA where DOCID = " + user.docID + " AND QA.APP='" + app.appName + "'";
				PreparedStatement pst2 = conn.prepareStatement(delSQL);
				pst2.executeUpdate();

				if (app.loginMode == UserApplicationProfile.LOGIN_AND_QUESTION){
					for(UserApplicationProfile.QuestionAnswer qa : app.getQuestionAnswer()){
						insertURL = "insert into QA(DOCID, APP, QUESTION, ANSWER)values(" + user.docID + ",'" + app.appName + "','" + qa.controlQuestion +"','" + qa.answer + "')";
						PreparedStatement pst1 = conn.prepareStatement(insertURL);
						pst1.executeUpdate();
					}					
				}				
			}
			conn.commit();
			pst.close();
			return 1;
		}catch(Throwable e){
			DatabaseUtil.debugErrorPrint(e);
			return -1;
		}finally{	
			dbPool.returnConnection(conn);
		}*/
	}

	@Override
	public User reloadUserData(User user, int hash) {	
		/*Connection conn = dbPool.getConnection();
		try {
			conn.setAutoCommit(false);
			Statement s = conn.createStatement();
			String sql = "select * from USERS where LOGINHASH = " + hash;
			ResultSet rs = s.executeQuery(sql);

			if (rs.next()){
				user.fill(rs);
				if (user.isValid){
					String addSQL = "select * from APPS where APPS.DOCID=" + user.docID;
					Statement statement = conn.createStatement();	
					ResultSet resultSet = statement.executeQuery(addSQL);
					while(resultSet.next()){
						UserApplicationProfile ap = new UserApplicationProfile(resultSet.getString("APP"),resultSet.getInt("LOGINMODE"));
						user.enabledApps.put(ap.appName,ap);
					}
					resultSet.close();
					statement.close();
				}				
			}else{
				user.setUserID("anonymous");			
			}

			rs.close();
			s.close();
			conn.commit();
		}catch(Throwable e){
			DatabaseUtil.debugErrorPrint(e);
		}finally{
			dbPool.returnConnection(conn);
		}*/
		return user;
	}

	@Override
	public ArrayList<User> getUsers(String keyWord) {
		ArrayList<User> users = new ArrayList<User>(); 

		Connection conn = dbPool.getConnection();
		try{
			conn.setAutoCommit(false);
			Statement s = conn.createStatement();			
			String sql = "select * from USERS where USERS.USERID LIKE '" + keyWord + "%'";
			ResultSet rs = s.executeQuery(sql);

			while(rs.next()){
				User user = new User();
				user.fill(rs);	
				users.add(user);
			}
			rs.close();
			s.close();
			conn.commit();
		}catch(Throwable e){			
			DatabaseUtil.debugErrorPrint(e);
		} finally {	
			dbPool.returnConnection(conn);		
		}
		return users;
	}

	// TempUser
	public TempUser getTempUser(String userid){

		return new TempUser();
	}

	public int insert(TempUser user) {
		Connection conn = dbPool.getConnection();

		try {
			conn.setAutoCommit(false);

			String insertTempUser = "insert into TEMP_USERS (USERID) values('" + user.getCurrentUserID() + "')";

			PreparedStatement pst = conn.prepareStatement(insertTempUser);
			pst.executeUpdate();

			conn.commit();
			pst.close();
			return 1;
		}catch(Throwable e){
			DatabaseUtil.debugErrorPrint(e);
			return - 1;
		}finally{		
			dbPool.returnConnection(conn);
		}
	}

	public int update(TempUser user) {
		return -1;
	}

	private boolean checkHash(String hashAsString, int hash){
		try{
			int userHash = Integer.parseInt(hashAsString);
			if(userHash == hash){				
				return true;
			}else{
				return false;
			}
		}catch( NumberFormatException nfe){
			return false;
		}
	}
	
	private boolean checkHashPSW(String hashPSW, String hashPSWDB){
		try{

			//int userHash = hashPSW;
			if(hashPSW.equals(hashPSWDB)){				
				return true;
			}else{
				return false;
			}
		}catch( NumberFormatException nfe){
			return false;
		}
	}

	@Override
	public ArrayList<User> getAllUsers(String condition, int calcStartEntry, int pageSize) {
		ArrayList<User> users = new ArrayList<User>();
		String wherePiece = "";
		Connection conn = dbPool.getConnection();
		try{	
			conn.setAutoCommit(false);
			if (!condition.equals(""))wherePiece = "WHERE " + condition; 
			Statement s = conn.createStatement();
			String sql = "select * from USERS " + wherePiece + " LIMIT " + pageSize + " OFFSET " + calcStartEntry;
			ResultSet rs = s.executeQuery(sql);

			while(rs.next()){
				User user = new User();
				user.fill(rs);
				if (user.isValid){
					String addSQL = "select * from APPS where APPS.DOCID=" + user.docID;
					Statement statement = conn.createStatement();	
					ResultSet resultSet = statement.executeQuery(addSQL);
					while(resultSet.next()){
						ApplicationProfile ap = new ApplicationProfile(resultSet);
						user.enabledApps.put(ap.appName, ap);
					}
					resultSet.close();
					statement.close();
				}
				user.isValid = true;			
				users.add(user);
			}

			rs.close();
			s.close();	
			conn.commit();
			return users;
		}
		catch(Throwable e){		
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

	

}
