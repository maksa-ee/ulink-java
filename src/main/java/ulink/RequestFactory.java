package ulink;

import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 */
public class RequestFactory {


    public static Request createFromJson(String json) throws JSONException {
        JSONObject jsonData = new JSONObject(json);

        AbstractRequest request;
        String type = jsonData.getString("type");
        if (type.equals("auth")) {
            request = AuthRequest.createFromJson(jsonData.getJSONObject("data"));
        } else if (type.equals("pay")) {
            request = PaymentRequest.createFromJson(jsonData.getJSONObject("data"));
        } else {
            throw new JSONException("type should be one of auth or pay. Given: " + type);
        }
        request.setTimestamp(jsonData.getInt("timestamp"));
        return request;
    }
}
