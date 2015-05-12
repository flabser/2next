package com.flabser.script;

import java.io.*;
import java.net.URI;
import java.text.DateFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

import com.flabser.runtimeobj.RuntimeObjUtil;
import com.flabser.util.Util;

public class _Helper {

	public static String getNormalizedRichText(String value){
		return value.replace("&nbsp;", " ").replace("<br>", "<br/>").replace("&", "&amp;");
	}

	public static Date getCtrlDate(Date date , int priority, int complication){
		Calendar d = Calendar.getInstance();
		d.setTime(date);
		Calendar ctrlDate = RuntimeObjUtil.getCtrlDate(d, priority, complication);
		return ctrlDate.getTime();
	}

	public static String getDateAsString() throws _Exception{
		return getDateAsString(new Date());
	}

	public static String getDateAsString(Date date) throws _Exception{
		try{
			return Util.simpleDateFormat.format(date);
		} catch (Exception e) {
			throw new _Exception(_ExceptionType.SCRIPT_ENGINE_ERROR,"Date has not parsed :getDateAsString(" + date + ")");
		}
	}

	public static String getDateAsStringSilently(Date date) throws _Exception{
		try{
			return Util.dateTimeFormat.format(date);
		} catch (Exception e) {
			return "";
		}
	}

	public static String getDateAsStringShort() throws _Exception{
		return getDateAsStringShort(new Date());
	}

	public static String getDateAsStringShort(Date date) throws _Exception{
		try {
			return Util.simpleDateFormat.format(date);
		} catch (Exception e) {
			throw new _Exception(_ExceptionType.SCRIPT_ENGINE_ERROR,"Date has not parsed :getDateAsStringShort(" + date + ")");
		}
	}

	public static String getDateAsStringShortSilently(Date date) throws _Exception{
		try {
			return Util.simpleDateFormat.format(date);
		} catch (Exception e) {
			return "";
		}
	}


	/*public static kz.nextbase.script.coordination._Block parseCoordinationBlock(_Session ses, String complexString) throws _Exception{
		kz.nextbase.script.coordination._Block block = new kz.nextbase.script.coordination._Block(ses);
		try{
			StringTokenizer t = new StringTokenizer(complexString,"`");
			while(t.hasMoreTokens()){
				String blockID = t.nextToken();
				if (!blockID.equalsIgnoreCase("new")){
					block.setBlockID(Integer.parseInt(blockID));
				}
				String coordType = t.nextToken();
				if (coordType.equals("par")){
					block.setBlockType(_BlockType.PARALLEL_COORDINATION);	
				}else if(coordType.equals("ser")){
					block.setBlockType(_BlockType.SERIAL_COORDINATION);	
				}else if(coordType.equals("tosign")){
					block.setBlockType(_BlockType.TO_SIGN);
				}

				String delayTime = t.nextToken();
				block.setDelayTime(Integer.parseInt(delayTime));

				try{
					StringTokenizer t1 = new StringTokenizer(t.nextToken(),"^");
					while(t1.hasMoreTokens()){
						String coordinator = t1.nextToken();
						kz.nextbase.script.coordination._Coordinator coord = ses.createCoordinator();

						if (block.getBlockType() == _BlockType.TO_SIGN){
							coord.setAsSigner();
						}else{
							coord.setAsReviewer();
						}
						coord.setUserID(coordinator);
						block.addCoordinator(coord);
					}
					String coordStatus = t.nextToken();
					if (coordStatus.length() > 0) {
						if (coordStatus.equalsIgnoreCase("awaiting")){					
							block.setBlockStatus(_BlockStatusType.AWAITING);
						}
					}
				}catch (java.util.NoSuchElementException nse){

				}


			}
		}catch(Exception e){
			e.printStackTrace();
			throw new _Exception(_ExceptionType.FORMDATA_INCORRECT,"Parser error :parseCoordinationBlock(" + complexString + ")" + e);
		}

		return block;
	}*/

	public static Date convertStringToDate(String dateParam) throws _Exception{
		int numPatterns = 6;
		Pattern[] datePatterns = new Pattern [numPatterns];
		String[] dateFormats = new String[numPatterns];
		boolean flag=true;
		int i=0;
		Matcher matcher;
		SimpleDateFormat sdf = null;

		datePatterns[0] = Pattern.compile("[0-9]{2}-[0-9]{2}-[0-9]{4}[ ]{1,}[0-9]{2}:[0-9]{2}:[0-9]{2}");
		dateFormats[0] = "dd-MM-yyyy HH:mm:ss";
		datePatterns[1] = Pattern.compile("[0-9]{2}-[0-9]{2}-[0-9]{4}");
		dateFormats[1] = "dd-MM-yyyy";	
		datePatterns[2] = Pattern.compile("[0-9]{2}.[0-9]{2}.[0-9]{4}[ ]{1,}[0-9]{2}:[0-9]{2}:[0-9]{2}");
		dateFormats[2] = "dd.MM.yyyy HH:mm:ss";
		datePatterns[3] = Pattern.compile("[0-9]{2}.[0-9]{2}.[0-9]{4}");
		dateFormats[3] = "dd.MM.yyyy";
		datePatterns[4] = Pattern.compile("[0-9]{4}-[0-9]{2}-[0-9]{2}[ ]{1,}[0-9]{2}:[0-9]{2}:[0-9]{2}");
		dateFormats[4] = "yyyy-MM-dd HH:mm:ss";
		datePatterns[5] = Pattern.compile("[0-9]{4}-[0-9]{2}-[0-9]{2}");
		dateFormats[5] = "yyyy-MM-dd";

		while ((i < numPatterns) && flag){
			matcher = datePatterns[i].matcher(dateParam);
			if (matcher.matches()) {
				sdf = new SimpleDateFormat(dateFormats[i]);
				flag=false;
			}
			i++;
		}


		try{
			return sdf.parse(dateParam);
		}catch(ParseException e){
			throw new _Exception(_ExceptionType.FORMDATA_INCORRECT,"Parser error :convertStringToDate(" + dateParam + ")");
		}catch(Exception e){
			throw new _Exception(_ExceptionType.FORMDATA_INCORRECT,"Error :convertStringToDate(" + dateParam + ")");
		}

	}

	public static String getRandomValue(){
		return Util.generateRandomAsText("QWERTYUIOPASDFGHJKLMNBVCXZ1234567890");	 
	}


	public static String removeHTMLTags(String content){
		return Util.removeHTMLTags(content);
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

	public static void unzip(File zipfile, File directory) throws IOException {

		ZipFile zfile = new ZipFile(zipfile);
		Enumeration<? extends ZipEntry> entries = zfile.entries();

		while (entries.hasMoreElements()) {
			ZipEntry entry = entries.nextElement();
			File file = new File(directory, entry.getName());
			if (entry.isDirectory()) {
				file.mkdirs();
			} else {
				file.getParentFile().mkdirs();
				InputStream stream = zfile.getInputStream(entry);
				try {
					copy(stream, file);
				}
				finally {
					stream.close();
				}
			}
		}
	}

	public static String getMonth(int month) {
		Locale locale = new Locale.Builder().setLanguage("ru").setScript("Cyrl").build();
		DateFormatSymbols symbols = new DateFormatSymbols(locale);
		String[] monthNames = symbols.getMonths();
		String month_name = monthNames[month - 1];
		if (month_name.endsWith("т")) {
			month_name += "а";
		} else {
			month_name = month_name.substring(0, month_name.length() - 1) + "я";
		}
		return month_name;
	}

	public static void changeData(File targetFile, Map<String, String> substitutionData) throws IOException {

		BufferedReader br = null;
		String docxTemplate = "";
		try {
			br = new BufferedReader(new InputStreamReader(new FileInputStream(targetFile), "UTF-8"));
			String temp;
			while ((temp = br.readLine()) != null)
				docxTemplate = docxTemplate + temp;
			br.close();
			targetFile.delete();
		}
		catch (IOException e) {
			br.close();
			throw e;
		}

		Iterator substitutionDataIterator = substitutionData.entrySet().iterator();
		while (substitutionDataIterator.hasNext()) {
			Map.Entry<String, String> pair = (Map.Entry<String, String>) substitutionDataIterator.next();
			if (docxTemplate.contains(pair.getKey())) {
				if (pair.getValue() != null)
					docxTemplate = docxTemplate.replace(pair.getKey(), pair.getValue());
				else
					docxTemplate = docxTemplate.replace(pair.getKey(), "NEDOSTAJE");
			}
		}

		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(targetFile);
			fos.write(docxTemplate.getBytes("UTF-8"));
			fos.close();
		}
		catch (IOException e) {
			fos.close();
			throw e;
		}
	}

	public static void zip(File directory, File zipfile) throws IOException {

		URI base = directory.toURI();
		Deque<File> queue = new LinkedList<File>();
		queue.push(directory);
		OutputStream out = new FileOutputStream(zipfile);
		Closeable res = out;

		try {
			ZipOutputStream zout = new ZipOutputStream(out);
			res = zout;
			while (!queue.isEmpty()) {
				directory = queue.pop();
				for (File kid : directory.listFiles()) {
					String name = base.relativize(kid.toURI()).getPath();
					if (kid.isDirectory()) {
						queue.push(kid);
						name = name.endsWith("/") ? name : name + "/";
						zout.putNextEntry(new ZipEntry(name));
					} else {
						if (kid.getName().contains(".docx"))
							continue;
						zout.putNextEntry(new ZipEntry(name));
						copy(kid, zout);
						zout.closeEntry();
					}
				}
			}
		}
		finally {
			res.close();
		}
	}

	public static void deleteTempData(File file) throws IOException {

		if (file.isDirectory()) {

			// directory is empty, then delete it
			if (file.list().length == 0)
				file.delete();
			else {
				// list all the directory contents
				String[] files = file.list();

				for (String temp : files) {
					// construct the file structure
					File fileDelete = new File(file, temp);
					// recursive delete
					deleteTempData(fileDelete);
				}

				// check the directory again, if empty then delete it
				if (file.list().length == 0)
					file.delete();
			}
		} else {
			// if file, then delete it
			file.delete();
		}
	}

	public static void copy(InputStream stream, OutputStream out) throws IOException {

		byte[] buffer = new byte[1024];
		while (true) {
			int readCount = stream.read(buffer);
			if (readCount < 0) {
				break;
			}
			out.write(buffer, 0, readCount);
		}
	}

	public static void copy(File file, OutputStream out) throws IOException {
		InputStream stream = new FileInputStream(file);
		try {
			copy(stream, out);
		} finally {
			stream.close();
		}
	}

	public static void copy(InputStream stream, File file) throws IOException {
		OutputStream out = new FileOutputStream(file);
		try {
			copy(stream, out);
		} finally {
			out.close();
		}
	}

}

