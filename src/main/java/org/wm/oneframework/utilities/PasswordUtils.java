package org.wm.oneframework.utilities;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESedeKeySpec;

import org.apache.commons.codec.binary.Base64;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class PasswordUtils {

	private static final String UNICODE_FORMAT = "UTF8";
	private static final String DESEDE_ENCRYPTION_SCHEME = "DESede";
	private static KeySpec ks;
	private static SecretKeyFactory skf;
	private static Cipher cipher;
	static byte[] arrayBytes;
	private static String myEncryptionKey;
	private static String myEncryptionScheme;
	private static SecretKey key;
	private static Logger logger = LogManager.getLogger(PasswordUtils.class.getName());

	private PasswordUtils() {
	}

	static {
		myEncryptionKey = "PasswordUtilsForEncrytDecrypt";
		myEncryptionScheme = DESEDE_ENCRYPTION_SCHEME;
		try {
			arrayBytes = myEncryptionKey.getBytes(UNICODE_FORMAT);
			ks = new DESedeKeySpec(arrayBytes);
			skf = SecretKeyFactory.getInstance(myEncryptionScheme);
			cipher = Cipher.getInstance(myEncryptionScheme);
			key = skf.generateSecret(ks);
		} catch (InvalidKeyException | UnsupportedEncodingException | InvalidKeySpecException | NoSuchAlgorithmException | NoSuchPaddingException e) {
			logger.warn(e.getMessage());
		}
	}

	public static String encrypt(String unencryptedString) {
		String encryptedString = null;
		try {
			cipher.init(Cipher.ENCRYPT_MODE, key);
			byte[] plainText = unencryptedString.getBytes(UNICODE_FORMAT);
			byte[] encryptedText = cipher.doFinal(plainText);
			encryptedString = new String(Base64.encodeBase64(encryptedText));
		} catch (InvalidKeyException | IllegalBlockSizeException | BadPaddingException | UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return encryptedString;
	}

	public static String decrypt(String encryptedString) {

		String decryptedText = null;
		try {
			cipher.init(Cipher.DECRYPT_MODE, key);
			byte[] encryptedText = Base64.decodeBase64(encryptedString);
			byte[] plainText = cipher.doFinal(encryptedText);
			decryptedText = new String(plainText);
		} catch (InvalidKeyException | IllegalBlockSizeException | BadPaddingException e) {
			logger.warn(e.getMessage());
		}
		return decryptedText;
	}
}
