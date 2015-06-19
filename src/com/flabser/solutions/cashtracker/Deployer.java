package com.flabser.solutions.cashtracker;

import java.sql.Connection;
import java.sql.Statement;

import com.flabser.appenv.AppEnv;
import com.flabser.dataengine.DatabaseCore;
import com.flabser.dataengine.DatabaseUtil;
import com.flabser.dataengine.IDeployer;
import com.flabser.dataengine.pool.DatabasePoolException;
import com.flabser.users.ApplicationProfile;


public class Deployer extends DatabaseCore implements IDeployer {

	ApplicationProfile appProfile;

	@Override
	public void init(ApplicationProfile appProfile) throws InstantiationException, IllegalAccessException,
			ClassNotFoundException, DatabasePoolException {
		this.appProfile = appProfile;
		pool = getPool(Database.driver, appProfile);
	}

	@Override
	public int deploy() {
		Connection conn = pool.getConnection();
		try {
			conn = pool.getConnection();
			conn.setAutoCommit(false);
			createTable(DDEScripts.getBudgetDDE(), "BUDGETS");
			createTable(DDEScripts.getAccountDDE(), "ACCOUNTS");
			createTable(DDEScripts.getCategoryDDE(), "CATEGORIES");
			createTable(DDEScripts.getTransactionDDE(), "TRANSACTIONS");
			createTable(DDEScripts.getCostCenterDDE(), "COSTCENTERS");
			createTable(DDEScripts.getTagDDE(), "TAGS");
			conn.commit();
		} catch (Throwable e) {
			AppEnv.logger.errorLogEntry(e);
			e.printStackTrace();
			DatabaseUtil.debugErrorPrint(e);

		} finally {
			pool.returnConnection(conn);
		}
		return 0;
	}

	@Override
	public int remove() {
		return 0;
	}

	private boolean createTable(String createTableScript, String tableName) {
		Connection conn = pool.getConnection();
		boolean createUserTab = false;
		try {
			conn.setAutoCommit(false);
			Statement s = conn.createStatement();
			if (!hasTable(tableName)) {
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
			pool.returnConnection(conn);
		}
		return createUserTab;
	}

	private boolean hasTable(String tableName) {
		Connection conn = pool.getConnection();
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
			pool.returnConnection(conn);
		}
	}
}
