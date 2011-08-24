package ulink;

import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 *
 */
public class PaymentRequest extends AbstractRequest {

    private BigDecimal amount;
    private String currency;
    private Order order;

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = new BigDecimal(amount);
    }

    public void setAmount(String amount) {
        this.amount = new BigDecimal(amount);
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getType() {
        return "pay";
    }

    @Override
    public Map<String,Object> getJsonData() {
        Map<String,Object> localData = new HashMap<String, Object>();
        localData.put("amount", Util.money(getAmount()));
        localData.put("currency", getCurrency());

        Map<String,Object> data = super.getJsonData();
        data.put("data", localData);

        if (order != null) {
           localData.put("order", getOrder().getJsonData());
        }
        return data;
    }

    public static PaymentRequest createFromJson(JSONObject json) throws JSONException {
        JSONObject data = json.getJSONObject("data");
        PaymentRequest request = new PaymentRequest();
        request.setAmount(new BigDecimal(data.getString("amount")));
        request.setCurrency(data.getString("currency"));
        if (json.has("id")) {
            request.setClientTransactionId(json.getInt("id"));
        }
        if (data.has("order")) {
            request.setOrder(Order.createFromJson(data.getJSONArray("order")));
        }
        return request;
    }
}
