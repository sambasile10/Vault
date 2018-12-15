package vault.shared;

import vault.client.Log;

public class Vault {
	
	public static final String VERSION = "2.0";
	
	public static void main(String[] args) {
		if(args[0].equalsIgnoreCase("client")) {
			
			Log.writeNormal("// ");
		}
	}
	
	private static void logOutputVaultInitRuntime() {
		Log.writeNormal("// VAULT (unified, version=" + VERSION + ") //");
		Log.writeNormal("Programmed by Sam Basile");
	}

}
