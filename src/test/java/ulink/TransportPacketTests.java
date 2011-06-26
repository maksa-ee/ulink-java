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
    public void testTransportPacketCompilation() {
        byte[] signature = "=signature=".getBytes();
        BASE64Encoder encoder = new BASE64Encoder();
        String encodedSignature = encoder.encode(signature);

        TransportPacket packet = new TransportPacket();
        packet.setRequest("barnaz");
        packet.setSignature(signature);
        packet.setClientId(15);

        assertEquals(
                "ulink:" + Protocol.VERSION + ":15:barnaz:" + encodedSignature,
                packet.toJson()
        );
    }

    @Test
    public void testTransportPacketDecompilation() {
        BASE64Encoder encoder = new BASE64Encoder();
        String encodedSignature = encoder.encode("foo".getBytes());

        String encodedPacket = "ulink:0.9:15:barnaz:" + encodedSignature;

        TransportPacket packet = TransportPacket.createFromJson(encodedPacket);
        assertEquals("barnaz", packet. getRequest());
        assertEquals("foo", new String(packet.getSignature()));
        assertEquals(15, packet.getClientId());
    }
}
