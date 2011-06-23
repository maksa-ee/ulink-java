package ulink;

import sun.misc.BASE64Encoder;

import java.util.Date;

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
}
