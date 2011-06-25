package ulink;

import java.math.BigDecimal;
import java.text.DecimalFormat;

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
    protected String getDataJson() {
        String ret = "{\"amount\":" + Util.money(getAmount()) + ",\"currency\":\"" + getCurrency() + "\"";
        if (order != null) {
            ret += ",\"order\":" + getOrder().toJson();
        }
        return ret + "}";
    }
}
