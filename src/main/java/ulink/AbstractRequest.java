package ulink;

import java.math.BigDecimal;

/**
 *
 */
public abstract class AbstractRequest implements Request {

    int timestamp;
    int clientTransactionId;

    public int getTimestamp() {
        if (timestamp > 0) {
            return timestamp;
        }
        return (int) (System.currentTimeMillis() / 1000L);
    }

    public void setTimestamp(int timestamp) {
        this.timestamp = timestamp;
    }

    public String toJson() {
        return "{\"type\":\""+ getType() +"\",\"timestamp\":" + getTimestamp() + ",\"data\":" + getDataJson() + "}";
    }

    protected abstract String getDataJson();
}
