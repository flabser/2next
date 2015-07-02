package com.flabser.scheduler.tasks;

import com.flabser.server.Server;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

public class TempFileCleaner implements Job {

    public TempFileCleaner(){

    }

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        Server.logger.normalLogEntry("Start temp file cleaner tasks");
    }
}
