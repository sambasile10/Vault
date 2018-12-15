package vault.client;

public enum LogType {
	
	//LogType enum values
	CRYPTO("CRYPTO"), IO("STORAGE"), UI("INTERFACE"), NET("NETWORK"), MT("THREADMAN");
	
	//The string that gets appended to the front of the log string
	private String prefix;
	
	//Non-public constructor
	LogType(String prefix) {
		this.prefix = prefix;
	}
	
	//Returns the prefix
	String getPrefix() {
		return prefix;
	}

}