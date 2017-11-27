package eu.bittrade.libs.steemj.base.models.error;

import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.Arrays;

/**
 * @author <a href="http://steemit.com/@dez1337">dez1337</a>
 */
public class SteemErrorData {
    private int code;
    private String name;
    private String message;
    private SteemStack[] stack;

    public int getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public String getMessage() {
        return message;
    }

    public SteemStack[] getStack() {
        return stack;
    }

    @Override
    public String toString() {
        return "SteemErrorData{" +
                "code=" + code +
                ", name='" + name + '\'' +
                ", message='" + message + '\'' +
                ", stack=" + Arrays.toString(stack) +
                '}';
    }
}
