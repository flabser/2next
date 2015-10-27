package com.flabser.scheduler.tasks;

import java.io.File;
import java.util.ArrayList;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.flabser.env.Environment;
import com.flabser.server.Server;

public class TempFileCleaner implements Job {
	private static ArrayList<String> fileToDelete = new ArrayList<String>();
	protected boolean isFirstStart = true;
	private int ac;

	public TempFileCleaner() {

	}

	@Override
	public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
		Server.logger.infoLogEntry("start temp file cleaner tasks");
		ac = 0;
		if (isFirstStart){
			File folder = new File(Environment.tmpDir);
			if (folder.exists()){
				File[] list = folder.listFiles();
				for(int i = list.length; --i>=0;){
					File file = list[i];
					delete(file);
				}
			}
			isFirstStart = false;
		}else{
			for(String filePath: fileToDelete){
				File file = new File(filePath);
				while(file.getParentFile() != null && !file.getParentFile().getName().equals("tmp")){
					file = file.getParentFile();
				}
				if(file.getParentFile() == null) {
					file = null;
				}
				delete(file);
			}
		}
		if (ac > 0){
			Server.logger.debugLogEntry(ac + " temporary files were deleted");
		}

	}

	public static void addFileToDelete(String filePath) {
		fileToDelete.add(filePath);
	}

	public void delete(File file) {
		if(file == null || !file.exists()) {
			return;
		}
		if(file.isDirectory()){
			for(File f : file.listFiles()) {
				delete(f);
			}
			if (file.delete()) {
				ac ++ ;
			}
		}else{
			if (file.delete()) {
				ac ++ ;
			}
		}
	}
}
