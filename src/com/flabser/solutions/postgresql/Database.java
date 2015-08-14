package com.flabser.solutions.postgresql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Query;

import com.flabser.dataengine.DatabaseCore;
import com.flabser.dataengine.DatabaseUtil;
import com.flabser.dataengine.IDatabase;
import com.flabser.dataengine.IDeployer;
import com.flabser.dataengine.ft.IFTIndexEngine;
import com.flabser.dataengine.pool.DatabasePoolException;
import com.flabser.dataengine.system.entities.ApplicationProfile;
import com.flabser.restful.data.IAppEntity;
import com.flabser.server.Server;
import com.flabser.users.User;

public class Database extends DatabaseCore implements IDatabase {

	public static final SimpleDateFormat sqlDateTimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	public static final String driver = "org.postgresql.Driver";
	private String dbURI;

	@Override
	public void init(ApplicationProfile appProfile) throws InstantiationException, IllegalAccessException, ClassNotFoundException,
			DatabasePoolException {
		super.appProfile = appProfile;
		dbURI = appProfile.getURI();
		initConnectivity(driver, appProfile);
	}

	@Override
	public int getVersion() {
		return 1;
	}

	@Override
	public IFTIndexEngine getFTSearchEngine() throws InstantiationException, IllegalAccessException, ClassNotFoundException,
			DatabasePoolException {
		IFTIndexEngine ftEng = new FTIndexEngine();
		ftEng.init(appProfile);
		return ftEng;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<IAppEntity> select(String condition, User user) {
		Query q = entityManager.createQuery(condition);
		return q.getResultList();
	}

	@Override
	public ArrayList<IAppEntity> select(String condition, Class<IAppEntity> objClass, User user) {
		ArrayList<IAppEntity> o = new ArrayList<>();
		Connection conn = pool.getConnection();
		try {
			conn.setAutoCommit(false);
			Statement s = conn.createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);

			String sql = condition;
			ResultSet rs = s.executeQuery(sql);

			while (rs.next()) {
				IAppEntity grObj = objClass.newInstance();

				/*
				 * Field[] f = FieldUtils.getAllFields(objClass);
				 * 
				 * 
				 * for (Field field : f) { if
				 * (field.isAnnotationPresent(EntityField.class)) { EntityField
				 * anottation = field.getAnnotation(EntityField.class); String
				 * dfn = anottation.value(); if (dfn.equalsIgnoreCase("")) { dfn
				 * = field.getName(); } Method entityMethod =
				 * grObj.getClass().getMethod
				 * (Util.fieldToSetter(field.getName()), field.getType());
				 * Method rsMethod =
				 * ResultSet.class.getMethod(Util.fieldToGetter
				 * (field.getType().getSimpleName()), String.class);
				 * entityMethod.invoke(grObj, rsMethod.invoke(rs, dfn)); } }
				 */
				// grObj.init(rs);
				o.add(grObj);

			}
			conn.commit();
			s.close();
			rs.close();

		} catch (SQLException e) {
			DatabaseUtil.errorPrint(dbURI, e);
		} catch (Exception e) {
			Server.logger.errorLogEntry(e);
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

	@Override
	public void insert(IAppEntity a, User user) {
		entityManager.getTransaction().begin();
		entityManager.persist(a);
		entityManager.getTransaction().commit();

	}

}
