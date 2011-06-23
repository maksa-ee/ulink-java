package ulink;

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

    /**
     * Converts request to json string
     */
    public String toJson();
}
