package ulink;

import sun.misc.BASE64Encoder;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 *
 */
public class AuthRequest extends AbstractRequest {

    public String getType() {
        return "auth";
    }


    @Override
    protected String getDataJson() {
        return "{}";
    }

    public Map<String,Object> getJsonData() {
        return new HashMap<String, Object>();
    }
}
