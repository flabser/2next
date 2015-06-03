package com.flabser.runtimeobj;

import java.io.File;
import java.util.*;

import com.flabser.dataengine.Const;
import com.flabser.util.Util;

public class RuntimeObjUtil implements Const {
	private static final int v = 60 * 60 * 1000;

	int minPriority;
	int maxPriority;
	int minComplication;
	int maxComplication;

	public static int getDiffBetweenDays(Calendar currentDate, Calendar ctrlDate, Calendar[] holidays, boolean sixWorkdays){
		int dayOfWeek, workDayCount, allDayCount, weekCount;
		allDayCount = (int) Math.floor((ctrlDate.getTimeInMillis()  - currentDate.getTimeInMillis())/(v * 24));
		weekCount = (int) Math.ceil(allDayCount/7);
		workDayCount = (sixWorkdays ? weekCount * 6 : weekCount * 5);
		dayOfWeek = currentDate.get(Calendar.DAY_OF_WEEK);

		for(int i = 0; i < allDayCount - weekCount*7; i++){
			if((sixWorkdays ? dayOfWeek!=1 : (dayOfWeek!=1&&dayOfWeek!=7))){
				workDayCount++;
			}
			if(dayOfWeek!=7){
				dayOfWeek++;
			}else{
				dayOfWeek=1;
			}
		}

		Calendar[] tHolidays = (Calendar[])holidays.clone();

		for(int i = currentDate.get(Calendar.YEAR); i <= ctrlDate.get(Calendar.YEAR); i++){
			for(int j = 0; j < tHolidays.length;j++){
				tHolidays[j].set(Calendar.YEAR, i+1900);
				if(currentDate.getTime().compareTo(tHolidays[j].getTime()) * 
						tHolidays[j].getTime().compareTo(ctrlDate.getTime()) > 0){
					workDayCount--;
				}
			}
		}
		return workDayCount;
	}

	public static String cutHTMLText(String text, int length){	
		String result = Util.removeHTMLTags(text);
		result = cutText(result, length);
		return result;

	}

	public static String cutText(String text, int length){
		if(text.length()<=length) return text;
		int indOfSpace = 0;
		for(int i = length-3; i>=0; i--){
			if(text.charAt(i)==' '){
				indOfSpace = i;
				break;
			}
		}
		return text.substring(0, indOfSpace)+"...";
	}


	public static int countMaxPage(int colCount, int pageSize){
		float mp = (float)colCount/(float)pageSize;		
		float d = Math.round(mp);

		int maxPage = (int) d;
		if (mp > d){
			maxPage ++;
		}		
		if (maxPage < 1) maxPage = 1;
		return maxPage;
	}

	public static int calcStartEntry(int pageNum, int pageSize) {
		int pageNumMinusOne = pageNum;
		pageNumMinusOne--;
		return pageNumMinusOne * pageSize;
	}

	public static void checkUploadedFiles(String dirPath, List<String> deletedFiles) {
		File dir = new File(dirPath);
		if (dir.exists() && dir.isDirectory()) {
			int folderNum = 1;
			File folder = new File(dirPath + Integer.toString(folderNum));
			while (folder.exists()) {
				File[] listFiles = folder.listFiles();
				for (int i = listFiles.length; --i >= 0;) {
					if (deletedFiles.contains(listFiles[i].getName())) {
						listFiles[i].delete();
					}					
				}
				folderNum ++;
				folder = new File(dirPath + Integer.toString(folderNum));
			}
		}
	}

	

}
