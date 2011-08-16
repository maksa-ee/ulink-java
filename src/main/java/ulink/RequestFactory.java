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
            request = AuthRequest.createFromJson(jsonData);
        } else if (type.equals("pay")) {
            request = PaymentRequest.createFromJson(jsonData);
        } else if (type.equals("auth-response")) {
            request = AuthResponse.createFromJson(jsonData);
        } else if (type.equals("pay-response")) {
            request = PaymentResponse.createFromJson(jsonData);
        } else {
            throw new JSONException("type should be one of auth, pay, auth-response or pay-response. Given: " + type);
        }
        request.setTimestamp(jsonData.getInt("timestamp"));
        return request;
    }
}
