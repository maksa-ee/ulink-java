package ulink;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

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

    public Map<String, Object> getJsonData() {
        Map<String,Object> data = new HashMap<String,Object>();
        data.put("name", getName());
        data.put("descr", getDescription());
        data.put("qty", getQuantity());
        data.put("price", Util.money(getOneItemPrice()));
        return data;
    }
}
