package ulink;

import org.junit.Test;

import java.security.InvalidKeyException;
import java.security.KeyPair;

import static ulink.CryptoUtils.generateRSAKeyPair;
import static ulink.CryptoUtils.sign;

/**
 *
 */
public class UtilTests {

    @Test
    public void signTransportPacket() throws InvalidKeyException {

        KeyPair keyPair = generateRSAKeyPair();

        Order order = new Order();
        order.addItem(new OrderItem("Ezik","Puhlqj ez", 25.90));
        order.addItem(new OrderItem("Mja4ik","Puhlqj mja4", 9.00));

        PaymentRequest request = new PaymentRequest();
        request.setAmount(34.90);
        request.setCurrency(Request.CURRENCY_EURO);
        request.setOrder(order);

        String requestJson = request.toJson();

        TransportPacket packet = new TransportPacket();
        packet.setRequest(requestJson);
        packet.setSignature(sign(requestJson.getBytes(), keyPair.getPrivate()));
        packet.setClientId(15);

//        System.out.println(packet.toJson());
    }
}
