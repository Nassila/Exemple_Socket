package comSecurise;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.Signature;
import java.security.SignatureException;
import java.util.Base64;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

public class outil {
	public static String chiffrement(PublicKey key, String message) {
		// Chiffrement du message
		byte[] bytes = null;
		try {
			Cipher chiffreur = Cipher.getInstance("RSA");
			chiffreur.init(Cipher.ENCRYPT_MODE, key);
			bytes = chiffreur.doFinal(message.getBytes());
		} catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | IllegalBlockSizeException
				| BadPaddingException e1) {
			System.err.println("Erreur lors du chiffrement : " + e1);
			System.exit(-1);
		}
		String out = Base64.getEncoder().encodeToString(bytes);
		return out;
	}

	public static String dechiffrement(PrivateKey key, String message) {
		// tab de byte
		byte[] messageCode = Base64.getDecoder().decode(message);
		// Déchiffrement du message
		byte[] bytes = null;
		try {
			Cipher dechiffreur = Cipher.getInstance("RSA");
			dechiffreur.init(Cipher.DECRYPT_MODE, key);
			bytes = dechiffreur.doFinal(messageCode);
		} catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | IllegalBlockSizeException
				| BadPaddingException e) {
			System.err.println("Erreur lors du déchiffrement : " + e);
			System.exit(-1);
		}

		String out = new String(bytes);
		return out;
	}

	public static String signature(PrivateKey key, String message)
			throws NoSuchAlgorithmException, InvalidKeyException, SignatureException {

		byte[] messages = message.getBytes();
		Signature signature = Signature.getInstance("SHA1withRSA");
		signature.initSign(key, new SecureRandom());
		signature.update(messages);
		byte[] signatureBytes = signature.sign();
		String sig = new String(signatureBytes);
		return sig;

	}

	public static Boolean verifierSignature(PublicKey key, String message, String sig)
			throws NoSuchAlgorithmException, InvalidKeyException, SignatureException {

		byte[] messages = message.getBytes();
		byte[] sigs = message.getBytes();
		Signature signature = Signature.getInstance("SHA1withRSA");
		signature.initVerify(key);
		signature.update(messages);
		return signature.verify(sigs);
	}

}
