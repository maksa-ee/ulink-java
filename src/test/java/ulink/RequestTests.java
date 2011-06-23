package ulink;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public class RequestTests {

    @Test
    public void testAuthRequest() {
        AuthRequest request = new AuthRequest();
        request.setTimestamp(123);
        assertEquals("{type:\"auth\",timestamp:123,data:{}}", request.toJson());
    }

    @Test
    public void testPayRequest() {
        PaymentRequest request = new PaymentRequest();
        request.setAmount(new BigDecimal(23.50));
        request.setCurrency("EUR");
        request.setTimestamp(123);

        assertEquals("{type:\"auth\",timestamp:123,data:{" +
                    "amount:23.50,currency:\"EUR\"" +
                "}}", request.toJson());
    }

    @Test
    public void testPayRequestWithOrder() {

        Order order = mock(Order.class);
        when(order.toJson()).thenReturn("foo");

        PaymentRequest request = new PaymentRequest();
        request.setAmount(new BigDecimal(23.50));
        request.setCurrency("EUR");
        request.setTimestamp(123);
        request.setOrder(order);

        assertEquals("{type:\"auth\",timestamp:123,data:{" +
                    "amount:23.50,currency:\"EUR\"" +
                ",order:foo}}", request.toJson());
    }

    @Test
    public void orderListToJson() {
        List<OrderItem> items = new ArrayList<OrderItem>();
        OrderItem item1 = mock(OrderItem.class);
        when(item1.toJson()).thenReturn("foo");
        OrderItem item2 = mock(OrderItem.class);
        when(item2.toJson()).thenReturn("bar");

        items.add(item1);
        items.add(item2);

        Order order = new Order();
        order.setItems(items);

        assertEquals("[foo,bar]", order.toJson());
    }

    @Test
    public void orderItemToJson() {
        OrderItem item = new OrderItem();
        item.setName("foo");
        item.setDescription("Tom's \"big\" dog");
        item.setQuantity(2);
        item.setOneItemPrice(new BigDecimal(35.90));

        assertEquals("{name:\"foo\",descr:\"Tom's \\\"big\\\" dog\",qty:2,price:35.90}", item.toJson());
    }
}
