package vault.client;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Log {

	public static File logFile;
	public static FileWriter logWriter;
	public static boolean useLogFile = false;
	
	public static boolean init() {
		if(ClientConfig.USE_LOG_FILE == 1) {
			useLogFile = false;
			Log.write(LogType.IO, "Config use log file set to 1 // not using log.txt for output...");
			return true;
		} else {
			logFile = new File(ClientConfig.VAULT_DIR + "/log.txt");
			Log.write(LogType.IO, "Log service will write all data to: " + logFile.getAbsolutePath());
			try {
				if(!logFile.exists()) {
					Log.write(LogType.IO, "Creating new log file!");
					logFile.createNewFile();
				}
				
				logWriter = new FileWriter(logFile, true);
			} catch (IOException e) {
				Log.write(LogType.IO, "Fatal I/O error occured while opening FileWriter for log!");
				e.printStackTrace();
				return false;
			}
		}
		
		useLogFile = true;
		Log.write(LogType.IO, "Log service successfully initialized!");
		return true;
	}
	
	// Write data to System.out with prefix
	public static void write(LogType lt, String output) {
		String lString = "[" + lt.getPrefix() + " :: " + Log.getTimestamp() + "] " + output;
		System.out.println(lString);
		
		if(useLogFile) {
			try {
				logWriter.write(System.getProperty("line.seperator") + lString);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	// Write data to System.out
	public static void writeNormal(String output) {
		System.out.println("[" + Log.getTimestamp() + "] " + output);
	}

	// Get a simple timestamp (hours:minutes:seconds)
	public static String getTimestamp() {
		return new String(new SimpleDateFormat("HH.mm.ss").format(new Date()));
	}

	// Get a full timestamp (year.month.day hours:minutes:seconds)
	public static String getFullTimestamp() {
		return new String(new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date()));
	}

}
