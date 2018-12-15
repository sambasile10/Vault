package vault.shared.serialobjects;

public class AuthenticationKey {
	
	private String clientIdentifier;
	private byte[] key;
	
	public AuthenticationKey(String clientIdentifier, byte[] key) {
		this.clientIdentifier = clientIdentifier;
		this.key = key;
	}
	
	public String getClientIdentifier() {
		return this.clientIdentifier;
	}
	
	public byte[] getKey() {
		return this.key;
	}

}
