package ulink;

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
        for(OrderItem item : items) {
            ret.append(item.toJson());
            if (item != items.get(items.size() - 1)) {
                ret.append(',');
            }
        }
        ret.append(']');
        return ret.toString();
    }
}
