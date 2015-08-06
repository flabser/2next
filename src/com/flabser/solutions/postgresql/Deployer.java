package com.flabser.solutions.postgresql;

import com.flabser.dataengine.DatabaseCore;
import com.flabser.dataengine.IAppDatabaseInit;
import com.flabser.dataengine.IDeployer;
import com.flabser.dataengine.pool.DatabasePoolException;
import com.flabser.dataengine.system.entities.ApplicationProfile;
import com.flabser.server.Server;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.flabser.dataengine.DatabaseUtil.SQLExceptionPrintDebug;


public class Deployer extends DatabaseCore implements IDeployer {

	ApplicationProfile appProfile;

	@Override
	public void init(ApplicationProfile appProfile) throws InstantiationException, IllegalAccessException,
			ClassNotFoundException, DatabasePoolException {
		this.appProfile = appProfile;
		pool = getPool(Database.driver, appProfile);
	}

	@Override
	public int deploy(IAppDatabaseInit dbInit) {
		Connection conn = pool.getConnection();

		try (Statement stmt = conn.createStatement()){

			Set<String> tables = new HashSet<>();
			DatabaseMetaData dbmd = conn.getMetaData();
			String[] types = {"TABLE"};
			try(ResultSet rs = dbmd.getTables(null, null, "%", types)) {
				while (rs.next()) {
					tables.add((Optional.ofNullable(rs.getString("table_name")).orElse("")).toLowerCase());
				}
			}

			dbInit.getTablesDDE().stream().filter( q -> !tables.contains(getTableName(q).toLowerCase())).forEach(query -> {
                try {
                    stmt.executeUpdate(query);
                } catch (SQLException e) {
                    System.out.println(getTableName(query));
                    Server.logger.errorLogEntry("Unable to create table \"" + getTableName(query) + "\"");
                    Server.logger.errorLogEntry(e);
                }
            });

			conn.commit();
			return 0;
		} catch (SQLException e) {
			SQLExceptionPrintDebug(e);
			return -1;
		} finally {
            pool.returnConnection(conn);
		}

	}

    private String getTableName(String query){
        Pattern pattern = Pattern.compile("^\\s*CREATE\\s+TABLE\\s+([\\w-]+)\\s+");
        Matcher matcher = pattern.matcher(query);
        if (matcher.find()) {
            return matcher.group(1);
        }
        return "";
    }

	@Override
	public int remove() {
		return 0;
	}
}
