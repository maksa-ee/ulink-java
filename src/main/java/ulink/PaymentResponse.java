package ulink;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: alex
 * Date: 8/16/11
 * Time: 3:41 PM
 * To change this template use File | Settings | File Templates.
 */
public class PaymentResponse extends PaymentRequest implements Response {
    protected boolean isSuccess;

    protected boolean isTest;

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
        return "pay-response";
    }

    public boolean isTest() {
        return isTest;
    }

    public void setTest(boolean test) {
        isTest = test;
    }


    public Map<String, Object> getJsonData() {
        Map<String, Object> data = super.getJsonData();
        data.put("success", isSuccess());
        data.put("test", isTest());
        data.put("errors", getErrors());
        data.put("errorCodes", getErrorCodes());
        return data;
    }

    public static PaymentResponse createFromJson(JSONObject json) throws JSONException {

        JSONObject data = json.getJSONObject("data");

        PaymentResponse response = new PaymentResponse();
        if (json.has("id")) {
            response.setClientTransactionId(json.getInt("id"));
        }
        response.setAmount(new BigDecimal(data.getString("amount")));
        response.setCurrency(data.getString("currency"));
        if (data.has("order")) {
            response.setOrder(Order.createFromJson(data.getJSONArray("order")));
        }
        response.setSuccess(json.getBoolean("success"));
        response.setTest(json.getBoolean("test"));


        ArrayList<String> errors = new ArrayList<String>();
        JSONArray jsonArray = json.getJSONArray("errors");
        if (jsonArray != null) {
           for (int i=0;i<jsonArray.length();i++){
              errors.add(jsonArray.getString(i));
           }
        }
        response.setErrors(errors);

        ArrayList<Integer> errorCodes = new ArrayList<Integer>();
        JSONArray jsonArray2 = json.getJSONArray("errorCodes");
        if (jsonArray2 != null) {
           for (int i=0;i<jsonArray2.length();i++){
              errorCodes.add(jsonArray2.getInt(i));
           }
        }
        response.setErrorCodes(errorCodes);

        return response;
    }
}
