package eu.bittrade.libs.golosj.util;

import java.util.List;

/**
 * Created by yuri on 05.10.17.
 */

public class StringUtils {
    public static <T> String join(T[] array, String  separator) {
        return array == null?null:join((Object[])array, separator, 0, array.length);
    }
    public static <T> String join(List<T> list, String  separator) {
        return list == null?null:join(list.toArray(), separator, 0, list.size());
    }
    public static String join(Object[] array, String separator, int startIndex, int endIndex) {
        if(array == null) {
            return null;
        } else {
            int noOfItems = endIndex - startIndex;
            if(noOfItems <= 0) {
                return "";
            } else {
                StringBuilder buf = new StringBuilder(noOfItems * 16);

                for(int i = startIndex; i < endIndex; ++i) {
                    if(i > startIndex) {
                        buf.append(separator);
                    }

                    if(array[i] != null) {
                        buf.append(array[i]);
                    }
                }

                return buf.toString();
            }
        }
    }
}
