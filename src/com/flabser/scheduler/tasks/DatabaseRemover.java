package com.flabser.scheduler.tasks;

import java.sql.SQLException;
import java.util.ArrayList;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.flabser.dataengine.DatabaseFactory;
import com.flabser.dataengine.system.IApplicationDatabase;
import com.flabser.dataengine.system.ISystemDatabase;
import com.flabser.dataengine.system.entities.ApplicationProfile;
import com.flabser.server.Server;
import com.flabser.users.ApplicationStatusType;

//TODO To realize
public class DatabaseRemover implements Job {

	public DatabaseRemover() {

	}

	@Override
	public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
		process(ApplicationStatusType.READY_TO_REMOVE);
	}

	public void process(ApplicationStatusType type){
		Server.logger.infoLogEntry("start database remover task");
		ISystemDatabase sysDb = DatabaseFactory.getSysDatabase();
		//ArrayList<ApplicationProfile> apps = sysDb.getAllApps("status=" + type.getCode(), 0, 100);
		ArrayList<ApplicationProfile> apps = sysDb.getAllApps("status=" + type.getCode() + " or status=" + ApplicationStatusType.DATABASE_NOT_CREATED.getCode(), 0, 100);
		for (ApplicationProfile ap : apps) {
			String appID = ap.appID;
			Server.logger.infoLogEntry("application " + appID + " prepared to delete. It will be try to deleted");
			try {
				IApplicationDatabase appDb = sysDb.getApplicationDatabase();

				if (appDb.removeDatabase(ap.dbName) == 0) {
					if (sysDb.deleteApplicationProfile(ap.id) == 0) {
						Server.logger.infoLogEntry("application " + appID + " has been deleted completly");
					} else {
						Server.logger.warningLogEntry("application profile " + appID + " has not been deleted");
					}
				} else {
					Server.logger.warningLogEntry("a database of the application profile " + appID + " has not been deleted");
				}
			} catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
				Server.logger.errorLogEntry(e);
			} catch (SQLException e) {
				Server.logger.errorLogEntry(e);
			}
		}
	}
}
