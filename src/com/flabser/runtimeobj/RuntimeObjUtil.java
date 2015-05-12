package com.flabser.runtimeobj;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import com.flabser.dataengine.Const;
import com.flabser.util.Util;

public class RuntimeObjUtil implements Const {
	private static final int v = 60 * 60 * 1000;
	private static final float PRIORITY_FACTOR = 2f;

	int minPriority;
	int maxPriority;
	int minComplication;
	int maxComplication;



	public static Calendar getCtrlDate(Calendar fromTime, int priority, int complication){
		Calendar ctrlDate = Calendar.getInstance(), 
				startDate = Calendar.getInstance(),
				tempDate = Calendar.getInstance();
		float  dayCount;
		SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy.HH.mm.ss");
		try {
			tempDate.setTime(format.parse("01.01.2000.18.00.00"));
		} catch (ParseException e) {
			e.printStackTrace();
		}

		tempDate.set(fromTime.get(Calendar.YEAR), fromTime.get(Calendar.MONTH), 
				fromTime.get(Calendar.DAY_OF_MONTH));

		dayCount  = (priority + complication) / PRIORITY_FACTOR;         //по приоритету
		//        dayCount  = (priority + complication) * COMPLICATION_FACTOR;   //по сложности

		switch(fromTime.get(Calendar.DAY_OF_WEEK)){
		case Calendar.SATURDAY: startDate.setTimeInMillis(tempDate.getTimeInMillis()-24 * v); break;
		case Calendar.SUNDAY:   startDate.setTimeInMillis(tempDate.getTimeInMillis()-48 * v); break;
		case Calendar.MONDAY:
			if(fromTime.getTimeInMillis()<=tempDate.getTimeInMillis()-9 * v){
				startDate.setTimeInMillis(tempDate.getTimeInMillis()-72 * v); break;
			}
		default:
			if(fromTime.getTimeInMillis()>=tempDate.getTimeInMillis()){
				startDate.setTimeInMillis(tempDate.getTimeInMillis()); break;
			}
			if(fromTime.getTimeInMillis()<=tempDate.getTimeInMillis()-9 * v){
				startDate.setTimeInMillis(tempDate.getTimeInMillis()-24 * v); break;
			}
			startDate = (Calendar)fromTime.clone();
		}


		ctrlDate.setTimeInMillis(startDate.getTimeInMillis());
		for(int i = 0; i < (int)dayCount; i++){
			if(ctrlDate.get(Calendar.DAY_OF_WEEK)!=Calendar.FRIDAY){
				ctrlDate.setTimeInMillis(ctrlDate.getTimeInMillis()+24 * v);
			}else{
				ctrlDate.setTimeInMillis(ctrlDate.getTimeInMillis()+72 * v);
			}
		}

		tempDate.set(ctrlDate.get(Calendar.YEAR), ctrlDate.get(Calendar.MONTH), 
				ctrlDate.get(Calendar.DAY_OF_MONTH));

		ctrlDate.setTimeInMillis(ctrlDate.getTimeInMillis()+(int)((dayCount-(int)dayCount) * 8 * 60 * 60 * 1000));

		if(ctrlDate.getTimeInMillis() > tempDate.getTimeInMillis()){
			if(ctrlDate.get(Calendar.DAY_OF_WEEK)!=Calendar.FRIDAY){
				ctrlDate.setTimeInMillis(tempDate.getTimeInMillis() + 15 * v);
			}else{
				ctrlDate.setTimeInMillis(tempDate.getTimeInMillis() + 63 * v);
			}
		}
		return ctrlDate;
	} 

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


	public static String getTypeAttribute(int type){
		String attr = "";
		switch(type){
		case TEXT:
			attr = " type=\"string\"";
			break;
		case DATETIMES:
			attr = " type=\"datetime\"";
			break;
		case NUMBERS:
			attr = " type=\"number\"";
			break;
		case COMPLEX_OBJECT:
			attr = " type=\"complex\"";
			break;
		case AUTHORS:
			attr = " type=\"authors\"";
			break;
		case TEXTLIST:
			attr = " type=\"map\"";
			break;
		case READERS:
			attr = " type=\"readers\"";
			break;
		case FILES:
			attr = " type=\"files\"";
			break;

		}
		return attr;
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
