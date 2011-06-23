package ulink;

import java.math.BigDecimal;
import java.util.StringTokenizer;

/**
 *
 */
public class OrderItem {

    String name;
    String description;

    int quantity;
    BigDecimal oneItemPrice;

    public OrderItem() {
    }

    public OrderItem(String name, String description, Double oneItemPrice) {
        this.name = name;
        this.description = description;
        this.oneItemPrice = new BigDecimal(oneItemPrice);
        this.quantity = 1;
    }

    public OrderItem(String name, String description, String oneItemPrice) {
        this.name = name;
        this.description = description;
        this.oneItemPrice = new BigDecimal(oneItemPrice);
        this.quantity = 1;
    }

    public OrderItem(String name, String description, BigDecimal oneItemPrice) {
        this.name = name;
        this.description = description;
        this.oneItemPrice = oneItemPrice;
        this.quantity = 1;
    }

    public OrderItem(String name, String description, BigDecimal oneItemPrice, int quantity) {
        this.name = name;
        this.description = description;
        this.quantity = quantity;
        this.oneItemPrice = oneItemPrice;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getOneItemPrice() {
        return oneItemPrice;
    }

    public void setOneItemPrice(String oneItemPrice) {
        this.oneItemPrice = new BigDecimal(oneItemPrice);
    }

    public void setOneItemPrice(Double oneItemPrice) {
        this.oneItemPrice = new BigDecimal(oneItemPrice);
    }

    public void setOneItemPrice(BigDecimal oneItemPrice) {
        this.oneItemPrice = oneItemPrice;
    }

    public String toJson() {
        return "{name:\"" + Util.escapeJsonString(getName()) +
                "\",descr:\"" + Util.escapeJsonString(getDescription()) +
                "\",qty:" + getQuantity() +
                ",price:" + Util.money(getOneItemPrice()) +
                "}";
    }
}
