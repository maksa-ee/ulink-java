package ulink;

import org.junit.Test;
import static org.junit.Assert.*;
import sun.misc.BASE64Encoder;
import static org.mockito.Mockito.*;

/**
 *
 */
public class TransportPacketTests {

    @Test
    public void testTransportPacket() {
        byte[] signature = "=signature=".getBytes();
        BASE64Encoder encoder = new BASE64Encoder();
        String encodedSignature = encoder.encode(signature);

        TransportPacket packet = new TransportPacket();
        packet.setRequest("{bar:\"foo\"}");
        packet.setSignature(signature);
        packet.setClientId(15);

        assertEquals(
                "ulink:" + Protocol.VERSION + ":15:{bar:\"foo\"}:" + encodedSignature,
                packet.toJson()
        );
    }
}
