package ulink;

import org.junit.Test;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;

import static org.junit.Assert.*;
import static ulink.CryptoUtils.generateRSAKeyPair;
import static ulink.CryptoUtils.sign;

/**
 *
 */
public class IntegrationTests {

    @Test
    public void paymentOut() throws IOException, MallformedPemKeyException, InvalidKeyException {

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

        String pemPubKey = "-----BEGIN PUBLIC KEY-----\n" +
                "MFwwDQYJKoZIhvcNAQEBBQADSwAwSAJBANDiE2+Xi/WnO+s120NiiJhNyIButVu6\n" +
                "zxqlVzz0wy2j4kQVUC4ZRZD80IY+4wIiX2YxKBZKGnd2TtPkcJ/ljkUCAwEAAQ==\n" +
                "-----END PUBLIC KEY-----";
        PublicKey pubKey = CryptoUtils.readPemPublicKey(pemPubKey);



        Order order = new Order();
        order.addItem(new OrderItem("Milk","Puhlqj ez", 25.90));
        order.addItem(new OrderItem("Mja4ik","Puhlqj mja4", 9.00));

        PaymentRequest request = new PaymentRequest();
        request.setAmount(34.90);
        request.setCurrency(Request.CURRENCY_EURO);
        request.setOrder(order);

        String requestJson = request.toJson();
        requestJson = CryptoUtils.seal(requestJson, pubKey);

        TransportPacket packet = new TransportPacket();
        packet.setRequest(requestJson);
        packet.setSignature(sign(requestJson.getBytes(), privateKey));
        packet.setClientId(15);

        System.out.println(packet.toJson());
    }

    @Test
    public void paymentIn() {
        String rawData = "";
    }
}
