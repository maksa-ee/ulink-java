package ulink;

import java.util.Map;

/**
 *
 */
public interface Request {

    public static final String CURRENCY_EURO = "EUR";
    public static final String CURRENCY_US_DOLLAR = "USD";

    /**
     * @return short alias for request type
     */
    public String getType();

    public int getTimestamp();

    /**
     * Converts request to json string
     */
    public Map<String,Object> getJsonData();

    public int getClientTransactionId();

    public String getGoBackUrl();
    public String getResponseUrl();
}
