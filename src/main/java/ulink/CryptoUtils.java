package ulink;

import sun.misc.BASE64Encoder;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

/**
 * {@inheritDoc}
 * 
 * @author Aleksandr Rudakov <aleksandr@itpeople.ee>
 */
public class CryptoUtils {

	/**
	 * {@inheritDoc}
	 */
	public static PrivateKey loadRSAPrivateKeyFromFile(File privKeyFile) throws IOException {
		
		byte[] encodedKey = new byte[(int)privKeyFile.length()];
		FileInputStream keyInputStream = new FileInputStream(privKeyFile);
		keyInputStream.read(encodedKey);
		keyInputStream.close();
		

		KeyFactory keyFactory = null;
		try {
			keyFactory = KeyFactory.getInstance("RSA");
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException(e);
		}
		
		// decode private key
		PKCS8EncodedKeySpec privSpec = new PKCS8EncodedKeySpec(encodedKey);
		RSAPrivateKey privKey = null;
		try {
			privKey = (RSAPrivateKey) keyFactory
					.generatePrivate(privSpec);
		} catch (InvalidKeySpecException e) {
			throw new RuntimeException(e);
		}

		return privKey;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public static PrivateKey loadRSAPrivateKeyFromFile(String filename) throws IOException {
		
		File privKeyFile = new File(filename);
		return loadRSAPrivateKeyFromFile(privKeyFile);
	}

	/**
	 * {@inheritDoc}
	 */
	public static byte[] signRSA(byte[] data, PrivateKey privateKey)
			throws InvalidKeyException {

		try {
			Signature rsa = Signature.getInstance(getSignatureMethod());
			rsa.initSign(privateKey);

			rsa.update(data);
			return rsa.sign();

		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException(e);
		} catch (SignatureException e) {
			throw new RuntimeException(e);
		}
	}

    public static void saveKeyPair(String path, KeyPair keyPair) throws IOException {
		PrivateKey privateKey = keyPair.getPrivate();
		PublicKey publicKey = keyPair.getPublic();

        BASE64Encoder encoder=new BASE64Encoder();

		// Store Public Key.
		X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(
				publicKey.getEncoded());
		FileOutputStream fos = new FileOutputStream(path + "/public.key");

        fos.write("-----BEGIN PUBLIC KEY-----\n".getBytes());
        fos.write(encoder.encode(x509EncodedKeySpec.getEncoded()).getBytes());
        fos.write("\n".getBytes());
        fos.write("-----END PUBLIC KEY-----".getBytes());
		fos.close();

		// Store Private Key.
		PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(
				privateKey.getEncoded());
		fos = new FileOutputStream(path + "/private.key");

        fos.write("-----BEGIN PRIVATE KEY-----\n".getBytes());
        fos.write(encoder.encode(pkcs8EncodedKeySpec.getEncoded()).getBytes());
        fos.write("\n".getBytes());
        fos.write("-----END PRIVATE KEY-----".getBytes());
		fos.close();
	}


	/**
	 * {@inheritDoc}
	 */
	public static boolean isValidRSASignature(byte[] data, byte[] signature,
			PublicKey publicKey) throws InvalidKeyException {
		Signature rsa;
		try {
			rsa = Signature.getInstance(getSignatureMethod());
			rsa.initVerify(publicKey);

			rsa.update(data);
			return rsa.verify(signature);

		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException(e);
		} catch (SignatureException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public static KeyPair generateRSAKeyPair() {
		try {
			KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");

			SecureRandom random = SecureRandom.getInstance("SHA1PRNG", "SUN");
			random.setSeed(33);
			keyGen.initialize(1024, random);

			return keyGen.generateKeyPair();

		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException(e);
		} catch (NoSuchProviderException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Returns the current signature method. Currently only RSA is
	 * supported. Other methods will be listed as need in them appears.
	 */
	private static String getSignatureMethod() {
		return "SHA1withRSA";
	}
}
