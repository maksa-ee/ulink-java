package ulink;

import com.sun.deploy.util.SystemUtils;
import org.bouncycastle.openssl.PEMReader;
import org.bouncycastle.openssl.PEMWriter;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;

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
	public static byte[] sign(byte[] data, PrivateKey privateKey)
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

    public static String convertPrivateToPem(PrivateKey privateKey) throws IOException {
        StringWriter strWriter = new StringWriter();
        PEMWriter writer = new PEMWriter(strWriter);
        writer.writeObject(privateKey);
        writer.flush();
        return strWriter.toString();
    }

    public static String convertPublicToPem(PublicKey publicKey) throws IOException {
        StringWriter strWriter = new StringWriter();
        PEMWriter writer = new PEMWriter(strWriter);
        writer.writeObject(publicKey);
        writer.flush();
        return strWriter.toString();
    }

    public static PrivateKey readPemPrivateKey(String pemKey) throws MallformedPemKeyException, IOException {

        Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());

        PEMReader reader = new PEMReader(new StringReader(pemKey));
        KeyPair pair = (KeyPair) reader.readObject();
        return pair.getPrivate();
    }

    public static PublicKey readPemPublicKey(String pemKey) throws IOException {
        Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());

        PEMReader reader = new PEMReader(new StringReader(pemKey));
        return (PublicKey) reader.readObject();
    }

    public static byte[] generateRandomBytes(int count) {
        try {
            // Create a secure random number generator
            SecureRandom sr = SecureRandom.getInstance("SHA1PRNG");

            // Get 1024 random bits
            byte[] bytes = new byte[count];
            sr.nextBytes(bytes);


            // Create two secure number generators with the same seed
            int seedByteCount = 10;
            byte[] seed = sr.generateSeed(seedByteCount);

            sr = SecureRandom.getInstance("SHA1PRNG");
            sr.setSeed(seed);
            SecureRandom sr2 = SecureRandom.getInstance("SHA1PRNG");
            sr2.setSeed(seed);

            return bytes;
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    public static String seal(String myData, PublicKey publicKey) {

        BASE64Encoder base64Encoder = new BASE64Encoder();

        try {

            byte[] plainKey = generateRandomBytes(128/8);

            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.ENCRYPT_MODE, publicKey);
            byte[] chiperKey = cipher.doFinal(plainKey);

            SecretKey skeySpec = new SecretKeySpec(plainKey, "RC4");
            cipher = Cipher.getInstance("RC4");
            cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
            byte[] chiperText = cipher.doFinal(myData.getBytes());

            return base64Encoder.encode(chiperText) + ":" + base64Encoder.encode(chiperKey);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static String unseal(String sealed, PrivateKey privateKey) {

        String[] parts = sealed.split(":");
        String cipherText = parts[0];
        String cipherKey = parts[1];

        BASE64Decoder base64Decoder = new BASE64Decoder();

        try {
            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.DECRYPT_MODE, privateKey);
            byte[] plainKey = cipher.doFinal(base64Decoder.decodeBuffer(cipherKey));

            SecretKey skeySpec = new SecretKeySpec(plainKey, "RC4");
            cipher = Cipher.getInstance("RC4");
            cipher.init(Cipher.DECRYPT_MODE, skeySpec);
            byte[] plaintext = cipher.doFinal(base64Decoder.decodeBuffer(cipherText));

            return new String(plaintext, "UTF-8");

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
