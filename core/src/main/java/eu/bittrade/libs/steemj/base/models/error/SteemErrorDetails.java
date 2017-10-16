package eu.bittrade.libs.steemj.base.models.error;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import eu.bittrade.libs.steemj.base.models.deserializer.ToStringDreserializer;
import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * @author <a href="http://steemit.com/@dez1337">dez1337</a>
 */
public class SteemErrorDetails {
    @JsonDeserialize(using = ToStringDreserializer.class)
    private String data;
    private int code;
    private String message;

    public String getData() {
        return data;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
