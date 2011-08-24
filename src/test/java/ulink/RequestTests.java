package ulink;

import org.json.JSONException;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 *
 */
public class RequestTests {

    @Test
    public void testAuthRequest() {
        AuthRequest request = new AuthRequest();
        request.setTimestamp(123);
        request.setClientTransactionId(456);

        assertEquals("{\"type\":\"auth\",\"timestamp\":123,\"id\":456,\"response-url\":null,\"back-url\":null,\"data\":{}}", request.toJson());
    }

    @Test
    public void testPayRequest() {
        PaymentRequest request = new PaymentRequest();
        request.setAmount(new BigDecimal(23.50));
        request.setCurrency("EUR");
        request.setTimestamp(123);
        request.setClientTransactionId(456);
        request.setGoBackUrl("http://local/");
        request.setResponseUrl("http://local2/");

        assertEquals("{\"type\":\"pay\",\"timestamp\":123,\"id\":456,\"response-url\":\"http://local2/\",\"back-url\":\"http://local/\",\"data\":{" +
                    "\"amount\":\"23.50\",\"currency\":\"EUR\"" +
                "}}", request.toJson());
    }

    @Test
    public void createRequestWithOrderFromJson() throws JSONException {
        String json = "{\"timestamp\":123,\"id\":456,\"response-url\":\"http://local2/\",\"back-url\":\"http://local/\", \"data\":{" +
                    "\"amount\":\"23.50\",\"currency\":\"EUR\",order:[" +
                "{\"name\":\"foo\",\"descr\":\"bar\",\"qty\":3,\"price\":\"12.80\"}," +
                "{\"name\":\"foo2\",\"descr\":\"bar2\",\"qty\":1,\"price\":\"12.90\"}" +
                "]" +
                "},\"type\":\"pay\"}";

        Request request = RequestFactory.createFromJson(json);
        assertEquals(PaymentRequest.class, request.getClass());
        assertEquals(123, request.getTimestamp());

        assertEquals("http://local/", request.getGoBackUrl());
        assertEquals("http://local2/", request.getResponseUrl());

        PaymentRequest paymentRequest = (PaymentRequest) request;
        assertEquals(new BigDecimal("23.50"), paymentRequest.getAmount());
        assertEquals("EUR", paymentRequest.getCurrency());
        assertEquals(123, paymentRequest.getTimestamp());
        assertEquals(456, paymentRequest.getClientTransactionId());
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
