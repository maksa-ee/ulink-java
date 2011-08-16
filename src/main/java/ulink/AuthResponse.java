package ulink;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: alex
 * Date: 8/16/11
 * Time: 3:23 PM
 * To change this template use File | Settings | File Templates.
 */
public class AuthResponse extends AuthRequest {

    private boolean isSuccess;

    private List<String> errors = new ArrayList<String>();
    private List<Integer> errorCodes = new ArrayList<Integer>();

    public List<String> getErrors() {
        return errors;
    }

    public void setErrors(List<String> errors) {
        this.errors = errors;
    }

    public List<Integer> getErrorCodes() {
        return errorCodes;
    }

    public void setErrorCodes(List<Integer> errorCodes) {
        this.errorCodes = errorCodes;
    }

    public boolean isSuccess() {
        return isSuccess;
    }

    public void setSuccess(boolean success) {
        isSuccess = success;
    }

    public String getType() {
        return "auth-response";
    }
    public Map<String, Object> getJsonData() {
        Map<String, Object> data = super.getJsonData();
        data.put("success", isSuccess());
        data.put("errors", getErrors());
        data.put("errorCodes", getErrorCodes());
        return data;
    }
}
