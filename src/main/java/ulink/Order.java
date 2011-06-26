package ulink;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public class Order {

    private List<OrderItem> items;

    public List<OrderItem> getItems() {
        return items;
    }

    public void setItems(List<OrderItem> items) {
        this.items = items;
    }

    public void addItem(OrderItem item) {
        if (items == null) {
            items = new ArrayList<OrderItem>();
        }
        items.add(item);
    }

    public String toJson() {
        StringBuilder ret = new StringBuilder();
        ret.append('[');

        ret.append(']');
        return ret.toString();
    }

    public List<Object> getJsonData() {
        List<Object> list = new ArrayList<Object>();
        for(OrderItem item : items) {
            list.add(item.getJsonData());
        }
        return list;
    }

    public static Order createFromJson(JSONArray jsonData) throws JSONException {
        Order order = new Order();
        for(int i=0; i < jsonData.length(); i++) {
            JSONObject item = jsonData.getJSONObject(i);
            OrderItem orderItem = new OrderItem();
            orderItem.setName(item.getString("name"));
            orderItem.setDescription(item.getString("descr"));
            orderItem.setQuantity(item.getInt("qty"));
            orderItem.setOneItemPrice(item.getString("price"));
            order.addItem(orderItem);
        }
        return order;
    }
}
