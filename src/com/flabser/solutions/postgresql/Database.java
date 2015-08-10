package com.flabser.solutions.postgresql;

import com.flabser.dataengine.DatabaseCore;
import com.flabser.dataengine.DatabaseUtil;
import com.flabser.dataengine.IDatabase;
import com.flabser.dataengine.IDeployer;
import com.flabser.dataengine.ft.IFTIndexEngine;
import com.flabser.dataengine.pool.DatabasePoolException;
import com.flabser.dataengine.system.entities.ApplicationProfile;
import com.flabser.script._IObject;
import com.flabser.users.User;

import java.sql.Array;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class Database extends DatabaseCore implements IDatabase {

	public static final SimpleDateFormat sqlDateTimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	public static final String driver = "org.postgresql.Driver";
	private String dbURI;
	private ApplicationProfile appProfile;

	@Override
	public void init(ApplicationProfile appProfile) throws InstantiationException, IllegalAccessException, ClassNotFoundException, DatabasePoolException {
		this.appProfile = appProfile;
		dbURI = appProfile.getURI();
		pool = getPool(driver, appProfile);
	}

	@Override
	public int getVersion() {
		return 1;
	}

	@Override
	public IFTIndexEngine getFTSearchEngine() throws InstantiationException, IllegalAccessException, ClassNotFoundException, DatabasePoolException {
		IFTIndexEngine ftEng = new FTIndexEngine();
		ftEng.init(appProfile);
		return ftEng;
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

	@SuppressWarnings({"SqlNoDataSourceInspection", "SqlDialectInspection"})
    @Override
	public ArrayList<_IObject> select(String condition, Class<? extends _IObject> objClass, User user) {
		ArrayList<_IObject> iObjectList = new ArrayList<>();
		Connection conn = pool.getConnection();

		try (Statement stmt = conn.createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY)){

            conn.setAutoCommit(true);
			try(ResultSet rs = stmt.executeQuery(condition)) {
                while (rs.next()) {
                    _IObject grObj = objClass.newInstance();
                    grObj.init(rs);
                    iObjectList.add(grObj);
                }
            }

            if(iObjectList.size() == 0) return iObjectList;

            String tableName = iObjectList.get(0).getTableName();
            String sql =
                    "select array( " +
                    "    select " + tableName + ".id from " + tableName +
                    "    inner join ( " +
                    "        select unnest(array [" +  iObjectList.stream().map(ob -> String.valueOf(ob.getId())).collect(Collectors.joining(", ")) + "])::integer as id " +
                    "    ) as ids on " + tableName + ".id = ids.id where " + tableName + ".readers_engine_field @> ARRAY['" + user.getLogin() + "']::character varying[] " +
                    ")::integer[] as ids";

            try(ResultSet rs = stmt.executeQuery(sql)) {
                if (!rs.next()) return new ArrayList<>();

                Array ids = rs.getArray("ids");
                List<Integer> idsList = ids != null ? Arrays.asList((Integer[]) ids.getArray()) : Collections.EMPTY_LIST;
                List<_IObject> result = iObjectList.stream().filter(obj -> idsList.contains((int) obj.getId())).collect(Collectors.toList());
                return new ArrayList<>(result);
            }

		} catch (SQLException e) {
			DatabaseUtil.errorPrint(dbURI, e);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			pool.returnConnection(conn);
		}

		return iObjectList;
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
			conn.commit();
			rs.close();
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
			conn.commit();
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
			conn.commit();
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
