package ulink;

import org.json.JSONObject;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.security.InvalidKeyException;
import java.security.PublicKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 *
 */
public class TransportPacket {

    private String request;

    private byte[] signature;

    int clientId;

    public int getClientId() {
        return clientId;
    }

    public void setClientId(int clientId) {
        this.clientId = clientId;
    }

    public byte[] getSignature() {
        return signature;
    }

    public void setSignature(byte[] signature) {
        this.signature = signature;
    }

    public String getRequest() {
        return request;
    }

    public void setRequest(String request) {
        this.request = request;
    }


    public String toJson() {
        return "ulink:" + Protocol.VERSION + ":" + getClientId() + ":" + getRequest() + ":" + CryptoUtils.base64Encode(signature);
    }

    public static TransportPacket createFromJson(String encodedPacket) {
        BASE64Decoder decoder = new BASE64Decoder();
        String parts[] = encodedPacket.split(":");
        TransportPacket packet = new TransportPacket();
        packet.setRequest(parts[3]);
        try {
            packet.setSignature(decoder.decodeBuffer(parts[4]));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        packet.setClientId(Integer.parseInt(parts[2]));
        return packet;
    }

    public boolean validateAgainstKey(PublicKey pubKey) {
        try {
            return CryptoUtils.isValidRSASignature(
                    getRequest().getBytes(),
                    getSignature(),
                    pubKey
            );
        } catch (Exception e) {
            return false;
        }
    }
}
