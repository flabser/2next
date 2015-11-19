package com.flabser.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.flabser.server.Server;

public class Util {
	public static final SimpleDateFormat dateTimeFormat = new SimpleDateFormat("dd.MM.yyyy kk:mm:ss");
	public static final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd.MM.yyyy");
	public static final Pattern pEntity = Pattern.compile("\\G&(#\\d+|\\w+);");
	public static final Pattern pTag = Pattern.compile("<(?:\"[^\"]*\"['\"]*|'[^']*'['\"]*|[^'\">])+>");
	public static final Pattern pAtEnd = Pattern.compile("\\G\\z");
	public static final Pattern pWord = Pattern.compile("\\G(\\w|\\pL)+");
	public static final Pattern pNonHtml = Pattern
			.compile("\\G([^(\\w|\\p{L})]|\\p{Ps}|\\p{Pe}|\\p{Pi}|\\p{Pf}|\\p{P}|\\p{S})+");

	public static boolean getRandomBoolean() {
		Random random = new Random();
		return random.nextBoolean();
	}

	public static int generateRandom() {
		Random random = new Random();
		return Math.abs(random.nextInt());
	}

	public static int generateRandomShort() {
		Random random = new Random();
		return (short) random.nextInt(Short.MAX_VALUE + 1);
	}

	public static String generateRandomAsText() {
		return Integer.toString(generateRandom());
	}

	public static Object getRandomFromList(List<?> list) {
		int index = ThreadLocalRandom.current().nextInt(list.size());
		return list.get(index);
	}

	public static boolean isGroupName(String userID) {
		if (userID != null && userID.length() != 0) {
			return userID.startsWith("[") && userID.endsWith("]");
		}
		return false;
	}

	public static String getFileName(String fn, String tmpFolder) {
		int folderNum = 1;
		File dir = new File(tmpFolder + File.separator + Integer.toString(folderNum));
		while (dir.exists()) {
			folderNum++;
			dir = new File(tmpFolder + File.separator + Integer.toString(folderNum));
		}
		dir.mkdirs();
		fn = dir + File.separator + fn;
		return fn;
	}

	private static byte[] getDigestFromFile(InputStream is) {
		byte[] result;
		try {
			MessageDigest md = MessageDigest.getInstance("SHA");
			byte[] b = new byte[1048576];
			int len = 0;
			while ((len = is.read(b)) > 0) {
				md.update(b, 0, len);
			}
			is.close();
			result = md.digest();
		} catch (NoSuchAlgorithmException e) {
			Server.logger.errorLogEntry("Util, не удалось инициализировать алгоритм шифрования");
			return null;
		} catch (IOException e) {
			Server.logger.errorLogEntry("Util, не удалось произвести чтение файла ");
			return null;
		}
		return result;
	}

	private static byte[] getDigestFromFile(String filePath) {
		byte[] result;
		try {
			MessageDigest md = MessageDigest.getInstance("SHA");
			File file = new File(filePath);
			InputStream is = new FileInputStream(file);
			byte[] b = new byte[1048576];
			int len = 0;
			while ((len = is.read(b)) > 0) {
				md.update(b, 0, len);
			}
			is.close();
			result = md.digest();
		} catch (FileNotFoundException e) {
			Server.logger.errorLogEntry(
					"Util, не удалось получить контрольную сумму файла " + filePath + ": файл не найден");
			return null;
		} catch (NoSuchAlgorithmException e) {
			Server.logger.errorLogEntry("Util, не удалось инициализировать алгоритм шифрования");
			return null;
		} catch (IOException e) {
			Server.logger.errorLogEntry("Util, не удалось произвести чтение файла " + filePath);
			return null;
		}
		return result;
	}

	private static String getHexHash(byte[] data) {
		StringBuffer sb = new StringBuffer();
		for (byte b : data) {
			sb.append(String.format("%02X", b & 0xff));
		}
		return sb.toString();
	}

	public static String getHexHash(String filePath) {
		return getHexHash(getDigestFromFile(filePath));
	}

	public static String getHexHash(InputStream is) {
		return getHexHash(getDigestFromFile(is));
	}

	public static boolean compareFile(String filePath1, String filePath2) {
		return MessageDigest.isEqual(getDigestFromFile(filePath1), getDigestFromFile(filePath2));
	}

	public static boolean checkFileHash(String filePath, String hexHash) {
		return hexHash.equals(getHexHash(filePath));
	}

	public static File getRandomFileIn(File dir) {

		String[] children = dir.list();
		if (children == null) {
			return null;
		} else {
			int i = 0;
			int max = children.length - 1;
			int min = 1;
			while (i < children.length) {
				Random rand = new Random();
				int randomNum = rand.nextInt(max - min + 1) + min;
				String filename = dir + File.separator + children[randomNum];
				if (filename.contains(".JPG") || filename.contains(".jpg")) {
					return new File(filename);
				}
				i++;
			}
			return null;

		}
	}

	public static String convertDataTimeToString(Calendar date) {
		try {
			return dateTimeFormat.format(date.getTime());
		} catch (Exception e) {
			if (date != null) {
				Server.logger.errorLogEntry("Util, Не удалось преобразовать время в текст " + date);
			}
			// Server.logger.errorLogEntry(e);
			return "";
		}
	}

	public static String removeHTMLTags(String text) {
		if (text == null) {
			return "";
		}
		Matcher m = pTag.matcher(text);
		while (!m.usePattern(pAtEnd).find()) {
			if (m.usePattern(pWord).find()) {
			} else if (m.usePattern(pTag).find()) {
				text = text.replaceAll("<br>", " ").replaceAll("<[^>]+>", "");
			} else if (m.usePattern(pEntity).find()) {
				if (m.group().equals("&quot;")) {
					text = text.replaceAll(m.group(), "\"");
				} else if (m.group().equals("&amp;")) {
					text = text.replaceAll(m.group(), "&");
				} else if (m.group().equals("&nbsp;")) {
					text = text.replaceAll(m.group(), "\n");
				} else if (m.group().equals("&laquo;")) {
					text = text.replaceAll(m.group(), "«");
				} else if (m.group().equals("&raquo;")) {
					text = text.replaceAll(m.group(), "»");
				} else if (m.group().equals("&lt;")) {
					text = text.replaceAll(m.group(), "<");
				} else if (m.group().equals("&gt;")) {
					text = text.replaceAll(m.group(), ">");
				} else {
					text = text.replaceAll(m.group(), "");
				}
			} else if (m.usePattern(pNonHtml).find()) {
			} else {
				if (m.usePattern(Pattern.compile("\\G(?s).{1,12}")).find()) {
					Server.logger.errorLogEntry("Bad char before '" + m.group() + "'");
				}
				return text.trim();
			}
		}
		return text.trim();
	}

	public static double convertBytesToKilobytes(long a) {
		double k = Math.round(a / 1024.0 * 100000.0) / 100000.0;
		return k;

	}

	public static String generateRandomAsText(String setOfTheLetters) {
		return generateRandomAsText(setOfTheLetters, 16);
	}

	public static String generateRandomAsText(String setOfTheLetters, int len) {
		Random r = new Random();
		String key = "";
		char[] letters = new char[setOfTheLetters.length() + 10];

		for (int i = 0; i < 10; i++) {
			letters[i] = Character.forDigit(i, 10);
		}

		for (int i = 0; i < setOfTheLetters.length(); i++) {
			letters[i + 10] = setOfTheLetters.charAt(i);
		}

		for (int i = 0; i < len; i++) {
			key += letters[Math.abs(r.nextInt()) % letters.length];
		}

		return key;
	}

	public static boolean addrIsCorrect(String email) {
		String validate = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
				+ "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

		Pattern pattern = Pattern.compile(validate);
		Matcher matcher = pattern.matcher(email);

		return matcher.matches();
	}

	public static String getTimeDiffInMilSec(long start_time) {
		long time = System.currentTimeMillis() - start_time;
		return Long.toString(time);
	}

	public static String getTimeDiffInSec(long start_time) {
		long time = System.currentTimeMillis() - start_time;
		int sec = (int) time / 1000;
		return Long.toString(sec);
	}

	public static boolean pwdIsStrong(String email) {
		// TODO need to check weaking of the password
		return true;

	}

	public static int getLineNumber() {
		return Thread.currentThread().getStackTrace()[1].getLineNumber();
	}

	public static String readFile(String file) {
		BufferedReader reader = null;
		try {
			File f = new File(file);
			reader = new BufferedReader(new FileReader(file));
			String line = null;
			StringBuilder stringBuilder = new StringBuilder();
			String ls = System.getProperty("line.separator");

			while ((line = reader.readLine()) != null) {
				stringBuilder.append(line);
				stringBuilder.append(ls);
			}

			return stringBuilder.toString();
		} catch (FileNotFoundException e) {
			Server.logger.errorLogEntry(e);
		} catch (IOException e) {
			Server.logger.errorLogEntry(e);
		} finally {
			try {
				if (reader != null) {
					reader.close();
				}
			} catch (IOException e) {
				Server.logger.errorLogEntry(e);
			}
		}
		return "";
	}

}
