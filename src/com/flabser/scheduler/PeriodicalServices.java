package com.flabser.scheduler;

import com.flabser.scheduler.tasks.TempFileCleaner;
import com.flabser.server.Server;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;
import static org.quartz.DateBuilder.*;
import static org.quartz.JobBuilder.newJob;
import static org.quartz.TriggerBuilder.newTrigger;

import java.util.Date;

public class PeriodicalServices {
    private Scheduler sched;

    public PeriodicalServices() {
        SchedulerFactory sf = new StdSchedulerFactory();
        try {
            sched = sf.getScheduler();
            Date runTime = evenMinuteDate(new Date());

            JobDetail job = newJob(TempFileCleaner.class)
                    .withIdentity("job1", "group1")
                    .build();

            Trigger trigger = newTrigger()
                    .withIdentity("trigger1", "group1")
                    .startAt(runTime)
                    .build();

            sched.scheduleJob(job, trigger);
            Server.logger.normalLogEntry(job.getKey() + " will run at: " + runTime);

            sched.start();

        } catch (SchedulerException e) {
            Server.logger.errorLogEntry(e);
        }
    }

    public void stop(){
        try {
            sched.shutdown(true);
        } catch (SchedulerException e) {
            Server.logger.errorLogEntry(e);
        }
    }
}
