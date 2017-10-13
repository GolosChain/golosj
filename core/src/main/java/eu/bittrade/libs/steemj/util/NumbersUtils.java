package eu.bittrade.libs.steemj.util;

/**
 * Created by yuri on 09.10.17.
 */

public class NumbersUtils {
    private NumbersUtils() {
    }

    public static long toUnSignedLong(int in) {
        return ((long) in) & 0xffffffffL;
    }
}
