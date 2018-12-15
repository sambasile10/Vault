package vault.client;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Properties;

public class ClientConfig {
	
	public static File RUNNING_DIR, VAULT_DIR, CONFIG_FILE;
	public static String SERVER_IP, CLIENT_UUID;
	public static int FS_REFRESH_SECONDS, COMM_PORT, USE_LOG_FILE, DATA_BLOCK_SIZE;
	
	private static Properties configFile;
	
	public static boolean loadConfiguration() {
		try {
			RUNNING_DIR = new File(ClientConfig.class.getProtectionDomain().getCodeSource().getLocation().toURI());
			Log.write(LogType.IO, "Vault runtime at: " + RUNNING_DIR.getAbsolutePath());
		} catch (URISyntaxException e) { e.printStackTrace(); return false; }
		
		VAULT_DIR = new File(RUNNING_DIR + "/store");
		CONFIG_FILE = new File(RUNNING_DIR + "/client.cfg");
		Log.write(LogType.IO, "Loading configuration from " + CONFIG_FILE.getAbsolutePath());
		Log.write(LogType.IO, "Vault storage will be synchronized with: " + VAULT_DIR.getAbsolutePath());
		
		configFile = new Properties();
		
		try {
			configFile.load(new FileReader(CONFIG_FILE));
		} catch (IOException e) {
			Log.write(LogType.IO, "Error loading the configuration file!");
			e.printStackTrace();
			return false;
		}
		
		SERVER_IP = configFile.getProperty("server");
		CLIENT_UUID = configFile.getProperty("uuid");
		COMM_PORT = Integer.parseInt(configFile.getProperty("port"));
		FS_REFRESH_SECONDS = Integer.parseInt(configFile.getProperty("fsRefreshSeconds"));
		USE_LOG_FILE = Integer.parseInt(configFile.getProperty("useLogFile"));
		DATA_BLOCK_SIZE = Integer.parseInt(configFile.getProperty("transferDataLength"));
		
		if(validateConfiguration()) {
			Log.write(LogType.IO, "Configuration validated with no errors!");
		} else {
			Log.write(LogType.IO, "[CRITICAL] Configuration failed to validate, check log for details!");
			return false;
		}
		
		return true;
	}
	
	private static boolean validateConfiguration() {
		Log.write(LogType.NET, "Server address set to: " + SERVER_IP + ", communicating over TCP on port " + COMM_PORT);
		Log.write(LogType.NET, "Client UUID // " + CLIENT_UUID);
		Log.write(LogType.IO, "File system refresh interval set to " + FS_REFRESH_SECONDS + " seconds");
		Log.write(LogType.IO, "Log file usage has set integer of " + USE_LOG_FILE);
		Log.write(LogType.NET, "Transfer data block size set to " + DATA_BLOCK_SIZE + " bytes");
		
		if(FS_REFRESH_SECONDS > 120) {
			Log.write(LogType.IO, "[WARN] File system refresh set above 2 minutes, this works but sync will be out of realtime!");
		} else if(FS_REFRESH_SECONDS < 20) {
			Log.write(LogType.IO, "[WARN] File system refresh set below 20 seconds, this will likely cause thread out-of-sync errors!");
		}
		
		if(!(USE_LOG_FILE == 0 || USE_LOG_FILE == 1)) {
			Log.write(LogType.IO, "[CRITICAL] Invalid configuration, parameter 'useLogFile' must be set to either 0 or 1!");
			return false;
		}
		
		return true;
	}

}
