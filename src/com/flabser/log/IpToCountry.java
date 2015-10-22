package com.flabser.log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.GZIPInputStream;

public class IpToCountry {

	public final static String DOWNLOAD_LOCATION = "http://software77.net/geo-ip/?DL=1";
	public final static long EXPIRE_DAILY = 24 * 60 * 60 * 1000L;
	public final static long EXPIRE_WEEKLY = 7 * EXPIRE_DAILY;
	public final static long EXPIRE_30DAYS = 30 * EXPIRE_DAILY;
	private final static long DOWNLOAD_LIMIT = 8 * 60 * 60 * 1000L;
	private final static long ATTEMPT_LIMIT = 60 * 1000L;
	private final static String FILE_NAME = "IpToCountry.csv";
	private final static Pattern CSV_LINE = Pattern
			.compile("^\"([0-9]{1,10})\",\"([0-9]{1,10})\",\"[^\"]*\",\"[0-9]*\",\"([A-Z]+)\"");
	private final static int COPY_BUFFER = 65536;
	private final static int INITIAL_ENTRIES = 65536;

	private File folder;
	private URL downloadLocation;
	private long expireTime;

	private long fileLastModified;

	private long lastDownloadTime, lastAttemptTime;
	private DownloadThread thread;
	private Informer informer;

	long[] entryFrom, entryTo;
	String[] entryCode;
	Throwable lastError;

	public interface Informer {

		public void downloadFailed(Throwable t);

		public void loadFailed(Throwable t);

		public void lineError(int errorIndex, String line);

		public void fileDownloaded(int milliseconds, int bytes);

		public void fileLoaded(int entries, int milliseconds, int lineErrors);
	}

	private class DefaultInformer implements Informer {
		@Override
		public void downloadFailed(Throwable t) {
			System.err.println("IpToCountry: An error occurred when downloading from " + downloadLocation + ":");
			t.printStackTrace();
			System.err.println();
		}

		@Override
		public void loadFailed(Throwable t) {
			System.err.println(
					"IpToCountry: An error occurred when loading the new " + "data file " + getDownloadFile() + ":");
			t.printStackTrace();
			System.err.println();
		}

		@Override
		public void lineError(int errorIndex, String line) {
			if (errorIndex == 0) {
				System.err.println("IpToCountry: File line not understood (displaying " + "first occurrence only):");
				System.err.println(line);
				System.err.println();
			}
		}

		@Override
		public void fileDownloaded(int milliseconds, int bytes) {
			System.err
					.println("IpToCountry: Downloaded new data file (" + bytes + " bytes) in " + milliseconds + " ms");
		}

		@Override
		public void fileLoaded(int entries, int milliseconds, int lineErrors) {
			System.err.println("IpToCountry: Loaded data file (" + entries + " entries) in " + milliseconds + " ms; "
					+ lineErrors + " lines were not understood");
		}
	}

	private class DownloadThread extends Thread {

		private DownloadThread() {
			start();
		}

		@Override
		public void run() {

			try {
				File downloadFile = getDownloadFile();

				// Download file
				try {
					downloadFile();
				} catch (Throwable t) {
					synchronized (IpToCountry.this) {
						lastError = t;
					}
					informer.downloadFailed(t);
					return;
				}

				// Load file
				try {
					loadFile(downloadFile);
					renameDownloadedToReal();
				} catch (Throwable t) {
					synchronized (IpToCountry.this) {
						lastError = t;
					}
					informer.loadFailed(t);
				}
			} finally {
				synchronized (IpToCountry.this) {
					thread = null;
					IpToCountry.this.notifyAll();
				}
			}
		}
	}

	public IpToCountry(File folder, String downloadLocation, long expireTime, Informer informer)
			throws IllegalArgumentException, MalformedURLException, IOException, InterruptedException {
		this.folder = folder;
		this.expireTime = expireTime;
		this.downloadLocation = new URL(downloadLocation);
		if (informer != null) {
			this.informer = informer;
		} else {
			this.informer = new DefaultInformer();
		}

		if (expireTime < EXPIRE_DAILY) {
			throw new IllegalArgumentException("Cannot set expire time lower than " + "one day");
		}

		if (!folder.canWrite()) {
			throw new IllegalArgumentException("Folder '" + folder + "' is not " + "writable; check permissions");
		}

		File file = getFile();
		boolean download = false;
		if (file.exists()) {
			loadFile(file);
			download = expired();
		} else {
			download = true;
		}

		// Guarantee that when this completes, the 'entry' arrays will be set
		synchronized (this) {
			if (download) {
				startDownload();
				if (entryFrom == null) {
					if (thread == null) {
						// This should not actually happen
						throw new IOException("Download prevented");
					}
					wait();
					if (entryFrom == null) {
						if (lastError != null) {
							if (lastError instanceof IOException) {
								throw (IOException) lastError;
							} else {
								IOException e = new IOException("Error obtaining database");
								e.initCause(lastError);
								throw e;
							}
						}
						throw new IOException("Unknown error obtaining database");
					}
				}
			}
		}
	}

	public IpToCountry(File folder, Informer informer)
			throws IllegalArgumentException, IOException, InterruptedException {
		this(folder, DOWNLOAD_LOCATION, EXPIRE_30DAYS, informer);
	}

	public String getCountryCode(InetAddress address) throws IllegalArgumentException {
		long addressLong = getAddressAsLong(get4ByteAddress(address));
		synchronized (this) {
			long now = getCurrentTime();
			if (now > fileLastModified + expireTime) {
				startDownload();
			}
			return getCountryCode(addressLong, entryFrom, entryTo, entryCode);
		}
	}

	public static String getCountryCode(long addressLong, long[] entryFrom, long[] entryTo, String[] entryCode) {
		// Binary search for the highest entryFrom that's less than or equal to
		// the specified address.
		int min = 0, max = entryFrom.length == 0 ? 0 : entryFrom.length - 1;
		while (min != max) {
			int half = (min + max) / 2;
			if (half == min) {
				// This special case handles the situation where e.g. min=10,
				// max=11;
				// there half=10 and we could get an endless loop.
				if (entryFrom[max] <= addressLong) {
					min = max;
				} else {
					max = min;
				}
			} else {
				// Standard case; check whether the halfway position is bigger
				// or
				// smaller
				if (entryFrom[half] <= addressLong) {
					min = half;
				} else {
					max = half - 1;
				}
			}
		}

		if (min >= entryFrom.length || entryTo[min] < addressLong || entryFrom[min] > addressLong) {
			return "..";
		} else {
			return entryCode[min];
		}
	}

	public static long getAddressAsLong(byte[] bytes) throws IllegalArgumentException {
		if (bytes.length != 4) {
			throw new IllegalArgumentException("Input must be 4 bytes");
		}
		int i0 = bytes[0] & 0xff, i1 = bytes[1] & 0xff, i2 = bytes[2] & 0xff, i3 = bytes[3] & 0xff;
		return (long) i0 << 24 | (long) i1 << 16 | (long) i2 << 8 | i3;
	}

	public static byte[] get4ByteAddress(InetAddress address) throws IllegalArgumentException {
		byte[] actual = address.getAddress();
		if (actual.length == 4) {
			return actual;
		}

		if (address instanceof Inet6Address) {
			if (((Inet6Address) address).isIPv4CompatibleAddress()) {
				// For compatible addresses, use last 4 bytes
				byte[] bytes = new byte[4];
				System.arraycopy(actual, actual.length - 4, bytes, 0, 4);
				return bytes;
			} else {
				throw new IllegalArgumentException(
						"IPv6 addresses not supported " + "unless IP4 compatible): " + address.getHostAddress());
			}
		}

		throw new IllegalArgumentException("Unknown address type: " + address.getHostAddress());
	}

	public File getFile() {
		return new File(folder, FILE_NAME);
	}

	public File getDownloadFile() {
		return new File(folder, FILE_NAME + ".download");
	}

	public File getOldFile() {
		return new File(folder, FILE_NAME + ".old");
	}

	private boolean expired() {
		return fileLastModified + expireTime < getCurrentTime();
	}

	protected void loadFile(File file) throws IOException {
		BufferedReader reader = null;
		try {
			// String-sharing buffer
			HashMap<String, String> countryCodes = new HashMap<String, String>();

			// Get file time
			long newFileLastModified = file.lastModified();

			// Get start time
			long start = getCurrentTime();

			// Prepare new index buffers
			long[] newEntryFrom = new long[INITIAL_ENTRIES];
			long[] newEntryTo = new long[INITIAL_ENTRIES];
			String[] newEntryCode = new String[INITIAL_ENTRIES];
			int entries = 0;

			reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), "US-ASCII"));
			int lineErrors = 0;
			while (true) {
				// Read next line
				String line = reader.readLine();
				if (line == null) {
					// No more lines
					break;
				}
				// Skip comments (lines beginning # or whitespace) and empty
				// lines
				if (line.isEmpty() || line.startsWith("#") || Character.isWhitespace(line.charAt(0))) {
					continue;
				}

				// Match line
				Matcher m = CSV_LINE.matcher(line);
				if (!m.find()) {
					// Non-matching line; report as warning, the first time
					informer.lineError(lineErrors++, line);
					continue;
				}

				// Line matches!

				// Expand entry arrays if required
				if (entries == newEntryFrom.length) {
					long[] temp = new long[entries * 2];
					System.arraycopy(newEntryFrom, 0, temp, 0, entries);
					newEntryFrom = temp;
					temp = new long[entries * 2];
					System.arraycopy(newEntryTo, 0, temp, 0, entries);
					newEntryTo = temp;
					String[] tempStr = new String[entries * 2];
					System.arraycopy(newEntryCode, 0, tempStr, 0, entries);
					newEntryCode = tempStr;
				}

				// Share country strings, as they are likely to be repeated many
				// times
				String code = m.group(3);
				if (countryCodes.containsKey(code)) {
					code = countryCodes.get(code);
				} else {
					countryCodes.put(code, code);
				}

				// Store new entry
				newEntryFrom[entries] = Long.parseLong(m.group(1));
				newEntryTo[entries] = Long.parseLong(m.group(2));
				newEntryCode[entries] = code;
				entries++;
			}

			// Reallocate arrays to precise length
			long[] temp = new long[entries];
			System.arraycopy(newEntryFrom, 0, temp, 0, entries);
			newEntryFrom = temp;
			temp = new long[entries];
			System.arraycopy(newEntryTo, 0, temp, 0, entries);
			newEntryTo = temp;
			String[] tempStr = new String[entries];
			System.arraycopy(newEntryCode, 0, tempStr, 0, entries);
			newEntryCode = tempStr;

			// Switch arrays with 'real' ones
			synchronized (this) {
				entryFrom = newEntryFrom;
				entryTo = newEntryTo;
				entryCode = newEntryCode;
				fileLastModified = newFileLastModified;
			}

			// Tell informer
			informer.fileLoaded(entries, (int) (getCurrentTime() - start), lineErrors);
		} finally {
			if (reader != null) {
				reader.close();
			}
		}
	}

	private synchronized void startDownload() {
		// If already downloading, ignore
		if (thread != null || lastAttemptTime + ATTEMPT_LIMIT > getCurrentTime()) {
			return;
		}
		thread = new DownloadThread();
	}

	long getCurrentTime() {
		return System.currentTimeMillis();
	}

	public void downloadFile() throws IOException {
		File downloadFile = getDownloadFile();
		GZIPInputStream in = null;
		FileOutputStream out = null;
		long start = getCurrentTime();
		boolean ok = false;

		try {
			// Use this safety check to ensure that we don't download more than
			// once in 8 hours (this should not happen anyway, but just in case)
			if (start < lastDownloadTime + DOWNLOAD_LIMIT) {
				throw new IOException("Unexpected attempt to re-download file "
						+ "within 8 hours, which could cause access to be blocked");
			}
			// This second safety check handles attempts (when the connection
			// fails) to ensure that the attempt rate is not too frequent.
			if (start < lastAttemptTime + ATTEMPT_LIMIT) {
				throw new IOException("Unexpected attempt to re-download file "
						+ "within 1 minute, which could cause access to be blocked");
			}

			lastAttemptTime = start; // Now we're making an attempt
			InputStream remoteStream = openRemoteGzip();
			lastDownloadTime = start; // Now we've probably counted as a server
										// hit
			in = new GZIPInputStream(remoteStream);
			out = new FileOutputStream(downloadFile);

			byte[] buffer = new byte[COPY_BUFFER];
			while (true) {
				int read = in.read(buffer);
				if (read == -1) {
					break;
				}
				out.write(buffer, 0, read);
			}

			ok = true;

			// Tell informer that file was downloaded
			informer.fileDownloaded((int) (getCurrentTime() - start), (int) downloadFile.length());
		} finally {
			// Delete file if there was an error
			if (!ok) {
				downloadFile.delete();
			}
			// Ensure streams are closed whatever the error status
			if (in != null) {
				try {
					in.close();
				} catch (Throwable t) {
				}
			}
			if (out != null) {
				try {
					out.close();
				} catch (Throwable t) {
				}
			}
		}
	}

	public void renameDownloadedToReal() throws IOException {
		File downloadFile = getDownloadFile();
		File file = getFile();
		File oldFile = getOldFile();
		boolean exists = file.exists();
		if (oldFile.exists()) {
			oldFile.delete();
		}
		if (exists && !file.renameTo(oldFile)) {
			throw new IOException("Failed to rename existing file " + file);
		}
		if (!downloadFile.renameTo(file)) {
			// Put old file back
			oldFile.renameTo(file);
			throw new IOException("Failed to rename new file to existing " + file);
		}
		if (exists && !oldFile.delete()) {
			throw new IOException("Failed to delete old file " + oldFile);
		}
	}

	InputStream openRemoteGzip() throws IOException {
		return downloadLocation.openStream();
	}
}
