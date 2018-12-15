package vault.shared;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import org.apache.commons.codec.digest.DigestUtils;

public class Crypto {
	
	public static String hash(String input) {
		return DigestUtils.sha256Hex(input);
	}
	
	public static String hash(File file) {
		try {
			return DigestUtils.sha256Hex(new FileInputStream(file));
		} catch (IOException e) {
			e.printStackTrace();
			return new String("error");
		}
	}

}
