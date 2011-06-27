package ulink;

import org.junit.Test;
import sun.misc.BASE64Encoder;

import java.io.IOException;
import java.security.*;
import java.util.prefs.BackingStoreException;

import static org.junit.Assert.*;

/**
 *
 */
public class CryptoUtilsTests {

    @Test
    public void base64encode() {
        String longLongLongText = "adfhajkdsfhglewrhgjerhkgjewrhkgjhewkgjhkewhjgewrkgjekjahfkjahdskfljhewruifhqewuifhiqewufhiuqwhfiugqwuifgqwuifgquiwegfiuqg";

        // are these strange new line sybols are comming from base64 encode function?
        assertFalse(CryptoUtils.base64Encode(longLongLongText.getBytes()).contains("\n"));
    }

    @Test
    public void generateKeyPair() throws IOException {
        KeyPair pair = CryptoUtils.generateRSAKeyPair();
        assertNotNull(pair.getPrivate());
        assertNotNull(pair.getPublic());

        String privatePem = CryptoUtils.convertPrivateToPem(pair.getPrivate());
        assertTrue(privatePem.startsWith("-----BEGIN RSA PRIVATE KEY-----"));

        String publicPem = CryptoUtils.convertPublicToPem(pair.getPublic());
        assertTrue(publicPem.startsWith("-----BEGIN PUBLIC KEY-----"));
    }

    @Test
    public void readAndVerifyWithRealKeysKey() throws MallformedPemKeyException, IOException, InvalidKeyException {
        String pemPrivKey = "-----BEGIN RSA PRIVATE KEY-----\n" +
        "MIIBOgIBAAJBANDiE2+Xi/WnO+s120NiiJhNyIButVu6zxqlVzz0wy2j4kQVUC4Z\n" +
        "RZD80IY+4wIiX2YxKBZKGnd2TtPkcJ/ljkUCAwEAAQJAL151ZeMKHEU2c1qdRKS9\n" +
        "sTxCcc2pVwoAGVzRccNX16tfmCf8FjxuM3WmLdsPxYoHrwb1LFNxiNk1MXrxjH3R\n" +
        "6QIhAPB7edmcjH4bhMaJBztcbNE1VRCEi/bisAwiPPMq9/2nAiEA3lyc5+f6DEIJ\n" +
        "h1y6BWkdVULDSM+jpi1XiV/DevxuijMCIQCAEPGqHsF+4v7Jj+3HAgh9PU6otj2n\n" +
        "Y79nJtCYmvhoHwIgNDePaS4inApN7omp7WdXyhPZhBmulnGDYvEoGJN66d0CIHra\n" +
        "I2SvDkQ5CmrzkW5qPaE2oO7BSqAhRZxiYpZFb5CI\n" +
        "-----END RSA PRIVATE KEY-----";

        PrivateKey privKey = CryptoUtils.readPemPrivateKey(pemPrivKey);
        assertNotNull(privKey);

        String pemPubKey = "-----BEGIN PUBLIC KEY-----\n" +
"MFwwDQYJKoZIhvcNAQEBBQADSwAwSAJBANDiE2+Xi/WnO+s120NiiJhNyIButVu6\n" +
"zxqlVzz0wy2j4kQVUC4ZRZD80IY+4wIiX2YxKBZKGnd2TtPkcJ/ljkUCAwEAAQ==\n" +
"-----END PUBLIC KEY-----";

        PublicKey pubKey = CryptoUtils.readPemPublicKey(pemPubKey);
        assertNotNull(pubKey);


        String myData = "foobarbaz";

        KeyPair pair = CryptoUtils.generateRSAKeyPair();

        byte[] signatrue = CryptoUtils.sign(myData.getBytes(), pair.getPrivate());

        assertNotNull(signatrue);

        assertTrue(CryptoUtils.isValidRSASignature(myData.getBytes(), signatrue, pair.getPublic()));
        assertFalse(CryptoUtils.isValidRSASignature("foo".getBytes(), signatrue, pair.getPublic()));


//        String sealed = "rXgLdMKdBcph:lMu2afMKMBw9FwOJZgegJ82gvMj/9AxRH4TqL6zjTjrH8SnyQe6wbgzv2wZEenz6nyLb6dC6CyfxghkXbctFVw==";
//        String sealed = CryptoUtils.seal(myData, pair.getPublic());
//        String opened = CryptoUtils.unseal(sealed, pair.getPrivate());
//        System.out.println(opened);

//        assertFalse(myData.equals(sealed));
//        assertEquals(myData, opened);
    }


    @Test
    public void signAndVerifySignature() throws InvalidKeyException, IOException {

        String myData = "foobarbaz";

        KeyPair pair = CryptoUtils.generateRSAKeyPair();

        byte[] signatrue = CryptoUtils.sign(myData.getBytes(), pair.getPrivate());

        assertNotNull(signatrue);

        assertTrue(CryptoUtils.isValidRSASignature(myData.getBytes(), signatrue, pair.getPublic()));
        assertFalse(CryptoUtils.isValidRSASignature("foo".getBytes(), signatrue, pair.getPublic()));

        String sealed = CryptoUtils.seal(myData, pair.getPublic());
        String opened = CryptoUtils.unseal(sealed, pair.getPrivate());

        assertFalse(myData.equals(sealed));
        assertEquals(myData, opened);
    }

    @Test
    public void testUnsealPhpData() throws IOException, MallformedPemKeyException {

        String key = "-----BEGIN RSA PRIVATE KEY-----\n" +
                "MIIBOgIBAAJBANDiE2+Xi/WnO+s120NiiJhNyIButVu6zxqlVzz0wy2j4kQVUC4Z\n" +
                "RZD80IY+4wIiX2YxKBZKGnd2TtPkcJ/ljkUCAwEAAQJAL151ZeMKHEU2c1qdRKS9\n" +
                "sTxCcc2pVwoAGVzRccNX16tfmCf8FjxuM3WmLdsPxYoHrwb1LFNxiNk1MXrxjH3R\n" +
                "6QIhAPB7edmcjH4bhMaJBztcbNE1VRCEi/bisAwiPPMq9/2nAiEA3lyc5+f6DEIJ\n" +
                "h1y6BWkdVULDSM+jpi1XiV/DevxuijMCIQCAEPGqHsF+4v7Jj+3HAgh9PU6otj2n\n" +
                "Y79nJtCYmvhoHwIgNDePaS4inApN7omp7WdXyhPZhBmulnGDYvEoGJN66d0CIHra\n" +
                "I2SvDkQ5CmrzkW5qPaE2oO7BSqAhRZxiYpZFb5CI\n" +
                "-----END RSA PRIVATE KEY-----";

        PrivateKey privateKey = CryptoUtils.readPemPrivateKey(key);

        String sealed = "vKkoXSkBCiF2@Yv8Z050myrf+4WogfUP5/D+PBjLNXv10C/ZsR2SI9GfuME2AMoZ504LqdJth7gA9Kc+1xZbv5jBAs/4CiKc1xQ==";
        String opened = CryptoUtils.unseal(sealed, privateKey);

        assertEquals("foobarbaz", opened);
    }
}
