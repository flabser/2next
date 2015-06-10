package com.flabser.solutions.cashtracker;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import com.flabser.dataengine.DatabaseCore;
import com.flabser.dataengine.DatabaseUtil;
import com.flabser.dataengine.IDatabase;
import com.flabser.dataengine.IDeployer;
import com.flabser.dataengine.ft.IFTIndexEngine;
import com.flabser.dataengine.pool.DatabasePoolException;
import com.flabser.script._IObject;
import com.flabser.users.ApplicationProfile;
import com.flabser.users.User;

public class Database extends DatabaseCore implements IDatabase {
	public static final SimpleDateFormat sqlDateTimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	public static final String driver = "org.postgresql.Driver";
	private String dbURI;

	@Override
	public void init(ApplicationProfile appProfile) throws InstantiationException, IllegalAccessException,
			ClassNotFoundException, DatabasePoolException {
		dbURI = appProfile.getURI();
		pool = getPool(driver, appProfile);
	}

	@Override
	public int getVersion() {
		return 1;
	}

	@Override
	public IFTIndexEngine getFTSearchEngine() {
		return new FTIndexEngine(this);
	}

	@Override
	public ArrayList<Object[]> select(String condition, User user) {
		ResultSet rs = null;
		ArrayList<Object[]> l = new ArrayList<Object[]>();
		Connection conn = pool.getConnection();
		try {
			conn.setAutoCommit(false);
			Statement s = conn.createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);

			String sql = condition;
			rs = s.executeQuery(sql);
			ResultSetMetaData md = rs.getMetaData();
			int cc = md.getColumnCount();

			while (rs.next()) {
				Object[] e = new Object[cc];
				for (int i = 0; i < cc; i++) {
					int columnIndex = i + 1;
					int type = md.getColumnType(columnIndex);
					if (type == Types.VARCHAR || type == Types.CHAR) {
						e[i] = rs.getString(columnIndex);
					} else if (type == Types.NUMERIC || type == Types.INTEGER || type == Types.SMALLINT) {
						e[i] = rs.getLong(columnIndex);
					} else if (type == Types.ARRAY) {
						e[i] = rs.getArray(i + 1).getArray();
					} else {
						e[i] = rs.getString(columnIndex);
					}
				}
				l.add(e);
			}
			conn.commit();
			s.close();
			rs.close();

		} catch (SQLException e) {
			DatabaseUtil.errorPrint(dbURI, e);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			pool.returnConnection(conn);
		}
		return l;
	}

	@SuppressWarnings("rawtypes")
	public ArrayList<_IObject> select(String condition, Class objClass, User user) {
		ArrayList<_IObject> o = new ArrayList<_IObject>();
		Connection conn = pool.getConnection();
		try {
			ResultSet rs = null;
			
			conn.setAutoCommit(false);
			Statement s = conn.createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);			

			String sql = condition;
			rs = s.executeQuery(sql);

			while (rs.next()) {
				_IObject grObj = (_IObject) objClass.newInstance();
				grObj.init(rs);
				o.add(grObj);
			}
			conn.commit();
			s.close();
			rs.close();

		} catch (SQLException e) {
			DatabaseUtil.errorPrint(dbURI, e);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			pool.returnConnection(conn);
		}
		return o;

	}

	@Override
	public int insert(String condition, User user) {
		Connection conn = pool.getConnection();
		try {
			conn.setAutoCommit(false);
			PreparedStatement pst;
			String sql = condition;
			pst = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
			pst.executeUpdate();
			int key = 0;
			ResultSet rs = pst.getGeneratedKeys();
			if (rs.next()) {
				key = rs.getInt(1);
			}
			return key;
		} catch (SQLException e) {
			DatabaseUtil.errorPrint(dbURI, e);
			return -1;
		} finally {
			pool.returnConnection(conn);
		}
	}

	@Override
	public int update(String condition, User user) {
		Connection conn = pool.getConnection();
		try {
			conn.setAutoCommit(false);
			PreparedStatement pst;
			String sql = condition;
			pst = conn.prepareStatement(sql);
			pst.executeUpdate();
			return 1;
		} catch (SQLException e) {
			DatabaseUtil.errorPrint(dbURI, e);
			return -1;
		} finally {
			pool.returnConnection(conn);
		}
	}

	@Override
	public int delete(String condition, User user) {
		Connection conn = pool.getConnection();
		try {
			conn.setAutoCommit(false);
			PreparedStatement pst;
			String sql = condition;
			pst = conn.prepareStatement(sql);
			pst.executeUpdate();
			return 1;
		} catch (SQLException e) {
			DatabaseUtil.errorPrint(dbURI, e);
			return -1;
		} finally {
			pool.returnConnection(conn);
		}
	}

	@Override
	public void shutdown() {
		pool.closeAll();
	}


	@Override
	public IDeployer getDeployer() {
		return new Deployer();
	}

}

