package ulink;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 *
 */
public class AuthRequest extends AbstractRequest {

    public String getType() {
        return "auth";
    }

    public Map<String, Object> getJsonData() {
        Map<String, Object> data = super.getJsonData();
        data.put("data", new HashMap<String, Object>());
        return data;
    }

    public static AuthRequest createFromJson(JSONObject json) {
        return new AuthRequest();
    }
}
