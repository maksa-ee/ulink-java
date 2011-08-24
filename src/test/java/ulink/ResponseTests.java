package ulink;

import org.json.JSONException;
import org.junit.Test;

import java.math.BigDecimal;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * Created by IntelliJ IDEA.
 * User: alex
 * Date: 8/16/11
 * Time: 3:20 PM
 * To change this template use File | Settings | File Templates.
 */
public class ResponseTests {

    @Test
    public void testSuccessAuthResponse() {

        AuthResponse response = new AuthResponse();
        response.setSuccess(true);
        response.setTimestamp(123);
        response.setClientTransactionId(456);
        assertEquals("{\"type\":\"auth-response\",\"timestamp\":123,\"id\":456,\"data\":{},\"success\":true,\"errors\":[],\"errorCodes\":[]}", response.toJson());
    }

    @Test
    public void testFailedAuthResponse() {

        AuthResponse response = new AuthResponse();
        response.setSuccess(false);
        response.setTimestamp(123);
        response.setClientTransactionId(456);
        response.getErrors().add("Wrong signature");
        response.getErrorCodes().add(17987);
        assertEquals("{\"type\":\"auth-response\",\"timestamp\":123,\"id\":456,\"data\":{},\"success\":false,\"errors\":[\"Wrong signature\"],\"errorCodes\":[17987]}", response.toJson());
    }

    @Test
    public void testSuccessPayRequest() {
        PaymentResponse request = new PaymentResponse();
        request.setAmount(new BigDecimal(23.50));
        request.setCurrency("EUR");
        request.setTimestamp(123);
        request.setClientTransactionId(456);
        request.setSuccess(true);
        request.setTest(true);

        assertEquals("{\"type\":\"pay-response\",\"timestamp\":123,\"id\":456,\"data\":{\"amount\":\"23.50\",\"currency\":\"EUR\"},\"success\":true,\"test\":true,\"errors\":[],\"errorCodes\":[]}", request.toJson());
    }

    @Test
    public void testFailedPayRequest() {
        PaymentResponse response = new PaymentResponse();
        response.setAmount(new BigDecimal(23.50));
        response.setCurrency("EUR");
        response.setTimestamp(123);
        response.setClientTransactionId(456);
        response.setSuccess(false);
        response.getErrors().add("Wrong signature");
        response.getErrorCodes().add(17987);

        assertEquals("{\"type\":\"pay-response\",\"timestamp\":123,\"id\":456,\"data\":{\"amount\":\"23.50\",\"currency\":\"EUR\"},\"success\":false,\"test\":false,\"errors\":[\"Wrong signature\"],\"errorCodes\":[17987]}", response.toJson());
    }

    @Test
    public void testClientTransactionIdPay() throws JSONException {

        String json = "{\"type\":\"pay-response\",\"timestamp\":123,\"id\":456,\"data\":{\"amount\":\"23.50\",\"currency\":\"EUR\"},\"success\":true,\"test\":false,\"errors\":[],\"errorCodes\":[]}";
        Response response = (Response) RequestFactory.createFromJson(json);

        assertEquals(456, response.getClientTransactionId());

    }

    @Test
    public void createRequestWithOrderFromJson() throws JSONException {
        String json = "{\"type\":\"pay-response\",\"timestamp\":123,\"id\":456,\"data\":{\"amount\":\"23.50\",\"currency\":\"EUR\"},\"success\":true,\"test\":true,\"errors\":[\"Wrong signature\"],\"errorCodes\":[17987]}";

        Response response = (Response) RequestFactory.createFromJson(json);
        assertEquals(PaymentResponse.class, response.getClass());
        assertEquals(123, response.getTimestamp());

        PaymentResponse paymentResponse = (PaymentResponse) response;
        assertEquals(new BigDecimal("23.50"), paymentResponse.getAmount());
        assertEquals("EUR", paymentResponse.getCurrency());
        assertEquals(123, paymentResponse.getTimestamp());
        assertTrue(paymentResponse.isSuccess());
        assertEquals(1, paymentResponse.getErrors().size());
        assertEquals("Wrong signature", paymentResponse.getErrors().get(0));
        assertEquals(1, paymentResponse.getErrorCodes().size());
        assertEquals(17987, (Object) paymentResponse.getErrorCodes().get(0));
        assertEquals(true, paymentResponse.isTest());
    }
}
