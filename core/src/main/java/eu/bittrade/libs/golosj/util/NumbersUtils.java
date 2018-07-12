package eu.bittrade.libs.golosj.util;

import static java.lang.Long.MIN_VALUE;
import static java.lang.Long.parseLong;

/**
 * Created by yuri on 09.10.17.
 */

public class NumbersUtils {
    private NumbersUtils() {
    }

    public static long toUnSignedLong(int in) {
        return ((long) in) & 0xffffffffL;
    }

    public static int hashCode(long source) {
        return (int) (source ^ (source >>> 32));
    }

    public static int toUnsignedInt(short x) {
        return ((int) x) & 0xffff;
    }

    public static long toUnsignedLong(int x) {
        return ((long) x) & 0xffffffffL;
    }

    public static String toUnsignedString(long x) {
        if (x >= 0) {
            return Long.toString(x, 10);
        } else {
            long quot = (x >>> 1) / 5;
            long rem = x - quot * 10;
            return Long.toString(quot) + rem;
        }
    }

    public static long parseUnsignedLong(String s) {
        int radix = 10;
        if (s == null) {
            throw new NumberFormatException("null");
        }

        int len = s.length();
        if (len > 0) {
            char firstChar = s.charAt(0);
            if (firstChar == '-') {
                throw new
                        NumberFormatException(String.format("Illegal leading minus sign " +
                        "on unsigned string %s.", s));
            } else {
                if (len <= 12 || // Long.MAX_VALUE in Character.MAX_RADIX is 13 digits
                        (radix == 10 && len <= 18)) { // Long.MAX_VALUE in base 10 is 19 digits
                    return parseLong(s, radix);
                }

                // No need for range checks on len due to testing above.
                long first = parseLong(s.substring(0, len - 1), radix);
                int second = Character.digit(s.charAt(len - 1), radix);
                if (second < 0) {
                    throw new NumberFormatException("Bad digit at end of " + s);
                }
                long result = first * radix + second;
                if (Long.compare(result + MIN_VALUE, first + MIN_VALUE) < 0) {
                    /*
                     * The maximum unsigned value, (2^64)-1, takes at
                     * most one more digit to represent than the
                     * maximum signed value, (2^63)-1.  Therefore,
                     * parsing (len - 1) digits will be appropriately
                     * in-range of the signed parsing.  In other
                     * words, if parsing (len -1) digits overflows
                     * signed parsing, parsing len digits will
                     * certainly overflow unsigned parsing.
                     *
                     * The compareUnsigned check above catches
                     * situations where an unsigned overflow occurs
                     * incorporating the contribution of the final
                     * digit.
                     */
                    throw new NumberFormatException(String.format("String value %s exceeds " +
                            "range of unsigned long.", s));
                }
                return result;
            }
        } else {
            throw new NumberFormatException(s);
        }
    }
}

