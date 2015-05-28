package com.flabser.dataengine.activity;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import com.flabser.dataengine.DatabaseUtil;
import com.flabser.dataengine.pool.IDBConnectionPool;
import com.flabser.users.User;

public class Activity implements IActivity {
	protected IDBConnectionPool pool;
    private static final SimpleDateFormat sqlDateTimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    
	public Activity(IDBConnectionPool pool) {
		this.pool = pool;
	}

	@Override
	public int postLogin(String ip, User user) {
		 int key = 0;
	        Connection conn = pool.getConnection();
	        try {
	            conn.setAutoCommit(false);
	            Statement s = conn.createStatement();
	            String sql = "insert into USERS_ACTIVITY(TYPE, DBID, USERID, EVENTTIME, CLIENTIP) values ("
	                    + UsersActivityType.LOGGED_IN.getCode() + ",'system', '" + user.getUserID()+ "', '" + sqlDateTimeFormat.format(new java.util.Date()) +
	                    "','" + ip + "')";
	            s.executeUpdate(sql, Statement.RETURN_GENERATED_KEYS);
	            ResultSet rs = s.getGeneratedKeys();
	            if (rs.next()) {
	                key = rs.getInt(1);
	            }
	            conn.commit();
	            s.close();
	        } catch (SQLException e) {
	            DatabaseUtil.errorPrint(Class.class.getPackage().toString(), e);
	            return -1;
	        } catch (Exception e) {
	            DatabaseUtil.errorPrint(Class.class.getPackage().toString(), e);
	            return -1;
	        } finally {
	            pool.returnConnection(conn);
	        }
	        return key;
	}

	@Override
	public int postLogout(String ip, User user) {
		 int key = 0;
	        Connection conn = pool.getConnection();
	        try {
	            conn.setAutoCommit(false);
	            Statement s = conn.createStatement();
	            String sql = "insert into USERS_ACTIVITY(TYPE, DBID, USERID, EVENTTIME, CLIENTIP) values ("
	                    + UsersActivityType.LOGGED_OUT.getCode() + ",'system', '" + user.getUserID() + "', '" + sqlDateTimeFormat.format(new java.util.Date()) +
	                    "', '" + ip + "')";
	            s.executeUpdate(sql, Statement.RETURN_GENERATED_KEYS);
	            ResultSet rs = s.getGeneratedKeys();
	            if (rs.next()) {
	                key = rs.getInt(1);
	            }
	            conn.commit();
	            s.close();
	        } catch (SQLException e) {
	            DatabaseUtil.errorPrint(Class.class.getPackage().toString(), e);
	            return -1;
	        } catch (Exception e) {
	            DatabaseUtil.errorPrint(Class.class.getPackage().toString(), e);
	            return -1;
	        } finally {
	            pool.returnConnection(conn);
	        }
	        return key;
	}



}
