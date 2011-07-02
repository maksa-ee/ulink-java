package ulink;

import java.io.IOException;
import java.security.PublicKey;

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
        String parts[] = encodedPacket.split(":");
        TransportPacket packet = new TransportPacket();
        packet.setRequest(parts[3]);
        packet.setSignature(CryptoUtils.base64Decode(parts[4]));
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
