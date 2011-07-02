package ulink;

import org.json.JSONObject;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 *
 */
public abstract class AbstractRequest implements Request {

    int timestamp;
    int clientTransactionId;

    public int getClientTransactionId() {
        return clientTransactionId;
    }

    public void setClientTransactionId(int clientTransactionId) {
        this.clientTransactionId = clientTransactionId;
    }

    public int getTimestamp() {
        if (timestamp > 0) {
            return timestamp;
        }
        return (int) (System.currentTimeMillis() / 1000L);
    }

    public void setTimestamp(int timestamp) {
        this.timestamp = timestamp;
    }

    public Map<String,Object> getJsonData() {
        Map<String,Object> data = new LinkedHashMap<String, Object>();
        data.put("type", getType());
        data.put("timestamp", getTimestamp());
        data.put("id", getClientTransactionId());
        return data;
    }

    public String toJson() {
        JSONObject jsonObject = new JSONObject(getJsonData());
        return jsonObject.toString();
    }
}
