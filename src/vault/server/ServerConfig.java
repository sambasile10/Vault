package vault.server;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Properties;

import vault.client.ClientConfig;
import vault.client.Log;
import vault.client.LogType;

public class ServerConfig {
	
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
		
	}

}
