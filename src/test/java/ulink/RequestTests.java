package ulink;

import org.json.JSONException;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 */
public class RequestTests {

    @Test
    public void testAuthRequest() {
        AuthRequest request = new AuthRequest();
        request.setTimestamp(123);

        assertEquals("{\"timestamp\":123,\"data\":{},\"type\":\"auth\"}", request.toJson());
    }

    @Test
    public void testPayRequest() {
        PaymentRequest request = new PaymentRequest();
        request.setAmount(new BigDecimal(23.50));
        request.setCurrency("EUR");
        request.setTimestamp(123);

        assertEquals("{\"timestamp\":123,\"data\":{" +
                    "\"amount\":\"23.50\",\"currency\":\"EUR\"" +
                "},\"type\":\"pay\"}", request.toJson());
    }

    @Test
    public void createRequestWithOrderFromJson() throws JSONException {
        String json = "{\"timestamp\":123,\"data\":{" +
                    "\"amount\":\"23.50\",\"currency\":\"EUR\",order:[" +
                "{\"name\":\"foo\",\"descr\":\"bar\",\"qty\":3,\"price\":\"12.80\"}," +
                "{\"name\":\"foo2\",\"descr\":\"bar2\",\"qty\":1,\"price\":\"12.90\"}" +
                "]" +
                "},\"type\":\"pay\"}";

        Request request = RequestFactory.createFromJson(json);
        assertEquals(PaymentRequest.class, request.getClass());
        assertEquals(123, request.getTimestamp());

        PaymentRequest paymentRequest = (PaymentRequest) request;
        assertEquals(new BigDecimal("23.50"), paymentRequest.getAmount());
        assertEquals("EUR", paymentRequest.getCurrency());
        assertEquals(123, paymentRequest.getTimestamp());
        assertNotNull(paymentRequest.getOrder());
    }

    @Test
    public void createAuthRequestFromJson() throws JSONException {
        String json = "{\"timestamp\":123,\"data\":{},\"type\":\"auth\"}";

        Request request = RequestFactory.createFromJson(json);
        assertEquals(123, request.getTimestamp());
        assertEquals(AuthRequest.class, request.getClass());
    }

    @Test
    public void orderItemToJson() {
        OrderItem item = new OrderItem();
        item.setName("foo");
        item.setDescription("Tom's \"big\" dog");
        item.setQuantity(2);
        item.setOneItemPrice(new BigDecimal(35.90));

        Map<String,Object> data = item.getJsonData();
        assertEquals("foo", data.get("name"));
        assertEquals("Tom's \"big\" dog", data.get("descr"));
        assertEquals(2, data.get("qty"));
        assertEquals("35.90", data.get("price"));
    }
}
