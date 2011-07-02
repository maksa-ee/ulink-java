package ulink;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.Locale;

/**
 *
 */
public class Util {
    public static String escapeJsonString(String stringToEscape) {
        return stringToEscape
                    .replaceAll("[\"\\\\]", "\\\\$0");
    }

    public static String money(BigDecimal amount) {
        DecimalFormatSymbols otherSymbols = new DecimalFormatSymbols();
        otherSymbols.setDecimalSeparator('.');
        otherSymbols.setGroupingSeparator(',');
        DecimalFormat df = new DecimalFormat("#.00", otherSymbols);
        return df.format(amount);
    }
}
