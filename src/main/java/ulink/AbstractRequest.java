package ulink;

import org.json.JSONException;
import org.json.JSONObject;
import sun.org.mozilla.javascript.internal.Function;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

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

    public Map<String,Object> getJsonData() {
        Map<String,Object> data = new HashMap<String, Object>();
        data.put("type", getType());
        data.put("timestamp", getTimestamp());
        return data;
    }

    public String toJson() {
        JSONObject jsonObject = new JSONObject(getJsonData());
        return jsonObject.toString();
    }
}
