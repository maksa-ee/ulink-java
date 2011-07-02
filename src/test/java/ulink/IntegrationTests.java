package ulink;

import org.json.JSONException;
import org.junit.Test;
import sun.misc.BASE64Encoder;

import java.io.IOException;
import java.math.BigDecimal;
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

    private String getPrivateKeyPem() {
        return "-----BEGIN RSA PRIVATE KEY-----\n" +
                "MIIBOgIBAAJBANDiE2+Xi/WnO+s120NiiJhNyIButVu6zxqlVzz0wy2j4kQVUC4Z\n" +
                "RZD80IY+4wIiX2YxKBZKGnd2TtPkcJ/ljkUCAwEAAQJAL151ZeMKHEU2c1qdRKS9\n" +
                "sTxCcc2pVwoAGVzRccNX16tfmCf8FjxuM3WmLdsPxYoHrwb1LFNxiNk1MXrxjH3R\n" +
                "6QIhAPB7edmcjH4bhMaJBztcbNE1VRCEi/bisAwiPPMq9/2nAiEA3lyc5+f6DEIJ\n" +
                "h1y6BWkdVULDSM+jpi1XiV/DevxuijMCIQCAEPGqHsF+4v7Jj+3HAgh9PU6otj2n\n" +
                "Y79nJtCYmvhoHwIgNDePaS4inApN7omp7WdXyhPZhBmulnGDYvEoGJN66d0CIHra\n" +
                "I2SvDkQ5CmrzkW5qPaE2oO7BSqAhRZxiYpZFb5CI\n" +
                "-----END RSA PRIVATE KEY-----";
    }

    private String getPublicKeyPem() {
        return "-----BEGIN PUBLIC KEY-----\n" +
                "MFwwDQYJKoZIhvcNAQEBBQADSwAwSAJBANDiE2+Xi/WnO+s120NiiJhNyIButVu6\n" +
                "zxqlVzz0wy2j4kQVUC4ZRZD80IY+4wIiX2YxKBZKGnd2TtPkcJ/ljkUCAwEAAQ==\n" +
                "-----END PUBLIC KEY-----";
    }

    @Test
    public void paymentOut() throws IOException, MallformedPemKeyException, InvalidKeyException, JSONException {

        PrivateKey privateKey = CryptoUtils.readPemPrivateKey(getPrivateKeyPem());
        PublicKey pubKey = CryptoUtils.readPemPublicKey(getPublicKeyPem());



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

        // --------------------
        // System.out.print(packet.toJson()); Here we take encoded packet for PHP tests
        // --------------------

        String rawData = packet.toJson();
        packet = TransportPacket.createFromJson(rawData);
        assertNotNull(packet);

        assertEquals(15, packet.getClientId());
        assertTrue(packet.validateAgainstKey(pubKey));

        Request otherRequest = RequestFactory.createFromJson(
                    CryptoUtils.unseal(packet.getRequest(), privateKey)
                );
        assertEquals(PaymentRequest.class, request.getClass());

        PaymentRequest paymentRequest = (PaymentRequest) otherRequest;
        assertEquals(new BigDecimal("34.90"), paymentRequest.getAmount());
        assertEquals("EUR", paymentRequest.getCurrency());

        order = paymentRequest.getOrder();
        assertNotNull(order);
        assertEquals(2, order.getItems().size());

        OrderItem orderItem1 = order.getItems().get(0);
        OrderItem orderItem2 = order.getItems().get(1);

        assertEquals("Milk", orderItem1.getName());
        assertEquals("Puhlqj ez", orderItem1.getDescription());
        assertEquals(new BigDecimal("25.90"), orderItem1.getOneItemPrice());

        assertEquals("Mja4ik", orderItem2.getName());
        assertEquals("Puhlqj mja4", orderItem2.getDescription());
        assertEquals(new BigDecimal("9.00"), orderItem2.getOneItemPrice());
    }



    @Test
    public void paymentIn() throws IOException, MallformedPemKeyException, JSONException {

        PrivateKey privateKey = CryptoUtils.readPemPrivateKey(getPrivateKeyPem());

        PublicKey pubKey = CryptoUtils.readPemPublicKey(getPublicKeyPem());

        String rawData = "ulink:0.9:15:wQJskeVAOqA3y2sC7puIeZFyrS4cb/VQD0q4LtzJYiT9s1xbR6/RB2AXB5By1WPMKR27OFAuITBOxlH77Mr9vUgW2774nH0/UMEsQK7MwHc7Vxn6sA9BsH744G/OVHVnbTg+May4ZFn/khh5r9++SIGss7GdCTtIV2JDOrHImWzD0mCxFyPEkZTFz8z/VQ+eMMdd7GJmLZcE1A8wyK1RBQCkquDBUtnp2r+fZ7PwZxn548K3tm2NrmQlyi4mQ1nENZHgDMcYB1IiOg7hf1GgJRr6KFrW@IQsWHgnFI3aymgkcs9IUbQ0/yT+A62An7Q0kuqpqr8Kg2N8vU5dvPAhj6JVDgrjdx6SYmTDidT92hLsAboIzUQ==:QN+oCD80TnF5YEswZ5T71kc+tq+6WT2U6Sk1ehapqz0sZVYiPvzE8Z6U6menVIey/XSaTwgENUw3k+Er4cehZw==";

        TransportPacket packet = TransportPacket.createFromJson(rawData);
        assertNotNull(packet);

        assertEquals(15, packet.getClientId());
        assertTrue(packet.validateAgainstKey(pubKey));

        Request request = RequestFactory.createFromJson(
                    CryptoUtils.unseal(packet.getRequest(), privateKey)
                );
        assertEquals(PaymentRequest.class, request.getClass());

        PaymentRequest paymentRequest = (PaymentRequest) request;
        assertEquals(new BigDecimal("34.90"), paymentRequest.getAmount());
        assertEquals("EUR", paymentRequest.getCurrency());

        Order order = paymentRequest.getOrder();
        assertNotNull(order);
        assertEquals(2, order.getItems().size());

        OrderItem orderItem1 = order.getItems().get(0);
        OrderItem orderItem2 = order.getItems().get(1);

        assertEquals("Milk", orderItem1.getName());
        assertEquals("Puhlqj ez", orderItem1.getDescription());
        assertEquals(new BigDecimal("25.90"), orderItem1.getOneItemPrice());

        assertEquals("Mja4ik", orderItem2.getName());
        assertEquals("Puhlqj mja4", orderItem2.getDescription());
        assertEquals(new BigDecimal("9.00"), orderItem2.getOneItemPrice());

    }

    @Test
    public void testSIgnCompare() throws IOException, MallformedPemKeyException, InvalidKeyException {
        String data = "foo";
        String phpVariant = "crc3ok1vpbRlVSU2OdqNvKBuTZ1qZbujRqZRYktRnS8zujIGK8rKj/WKzeUKHqhbzPzB9Oox1uKvzf8B2ALvUQ==";

        String javaVariant = CryptoUtils.base64Encode(CryptoUtils.sign(data.getBytes(),
                CryptoUtils.readPemPrivateKey(getPrivateKeyPem())));

        assertEquals(javaVariant, phpVariant);
    }
}
