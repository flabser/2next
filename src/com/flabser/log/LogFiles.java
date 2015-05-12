package com.flabser.log;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

import org.apache.commons.io.comparator.LastModifiedFileComparator;

public class LogFiles {
	public File logDir;
	
	private  ArrayList<File> fileList = new ArrayList<File>();
	
	public LogFiles(){
		logDir = new File("." + File.separator + "logs");
		if(logDir.isDirectory()){
			File[] list = logDir.listFiles();
			Arrays.sort(list, LastModifiedFileComparator.LASTMODIFIED_COMPARATOR);
			for(int i = list.length; --i>=0;){
				fileList.add(list[i]);				
			}
		}			
	}
	
	public ArrayList<File>  getLogFileList(){
		return fileList;
	}

	public int getCount() {
		return fileList.size();
	}
}
