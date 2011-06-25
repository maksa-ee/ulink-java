package ulink;

import sun.misc.BASE64Encoder;

import java.util.Date;

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
        BASE64Encoder encoder = new BASE64Encoder();
        return "{protocol:\"" + Protocol.VERSION + "\",clientId:" + getClientId() + ",request:" + getRequest() + ",signature:\"" + encoder.encode(signature) + "\"}";
    }
}
