package ulink;

import java.math.BigDecimal;
import java.text.DecimalFormat;

/**
 *
 */
public class Util {
    public static String escapeJsonString(String stringToEscape) {
        return stringToEscape
                    .replaceAll("[\"\\\\]", "\\\\$0");
    }

    public static String money(BigDecimal amount) {
        DecimalFormat formatter = new DecimalFormat("#.00");
        return "\"" + formatter.format(amount) + "\"";
    }
}
