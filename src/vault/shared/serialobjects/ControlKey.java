package vault.shared.serialobjects;

public enum ControlKey {
	
	/*
	 * Control Keys are a easy way to make push/pull data requests or pass simple status changes between
	 * the client and the server
	 * 
	 * arg0: int id - the general area the key deals with
	 * arg1: int special - the specific action or status change within the general region (arg0 int)
	 * arg2: string type - the toString() version, for log output and user understandability
	 * 
	 * ControlKeys (arg0, arg1):
	 * 
	 * arg0 (ID) types:
	 * 
	 * 0 = simple status change
	 * 1 = pull request (client downloads data from server)
	 * 2 = push request (client uploads data to server)
	 * 3 = file transfer protocol data establishments [IMPLIES TransferControlKey object used, extends this class]
	 * 5 = authentication
	 */
	
	/*
	 * Final control key types
	 */
	
	//id=0, status changes
	OUT_OF_SYNC(0, 0, "CLIENT AND SERVER OUT OF SYNC: UPDATING"),
	IN_SYNC(0, 1, "CLIENT AND SERVER ARE IN SYNC"),
	
	//id=1, pull requests
	PULL_FILESTRUCTURE(1, 0, "CLI PULL: StoreFileSystemStructure"),
	
	//id=2, push requests
	PUSH_FILESTRUCTURE(2, 0, "CLI PUSH: StoreFileSystemStructure"),
	
	//id=3, transfer statuses [USED BY TransferControlKey which is an extension of this class plus a transfer ID]
	PRETRANSFER_ACCEPTANCE(3, 0, "REMOTE HAS ACCEPTED TRANSFER METADATA"),
	TRANSFER_CONTINUE(3, 1, "REMOTE HAS REQUESTED THE NEXT DATA BLOCK"),
	TRANSFER_LAST(3, 2, "REMOTE INDICATED THE NEXT DATA BLOCK WILL BE ABNORMAL LENGTH"),
	TRANSFER_END_STREAM(3, 3, "REMOTE CLOSED TRANSFER: END OF STREAM"),
	TRANSFER_ERROR(3, -1, "CRITICAL I/O OR NET EXCEPTION OCCURED"),
	TRANSFER_ACKNOWLEDGE(3, 5, "CLIENT HAS REQUESTED UPLOAD PERMISSION"),
	
	//id=5, authentication service
	AUTH_REQUIRED(5, 0, "SERVER REQUIRES AUTHENTICATION"),
	AUTH_FAILURE(5, 1, "SERVER REJECTED AUTHENICATION"),
	AUTH_SUCCESS(5, 2, "SERVER ACCEPTED AUTHENTICATION");
	
	private String type;
	private int id, special;
	
	ControlKey(int id, int special, String type) {
		this.id = id;
		this.special = special;
		this.type = type;
	}
	
	int getID() {
		return this.id;
	}
	
	int getSpecialCode() {
		return this.special;
	}
	
	String getType() {
		return this.type;
	}

}
