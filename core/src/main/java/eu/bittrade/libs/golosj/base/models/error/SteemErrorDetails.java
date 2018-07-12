package eu.bittrade.libs.golosj.base.models.error;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import javax.annotation.Nullable;

import eu.bittrade.libs.golosj.base.models.deserializer.ToStringDeserializer;

/**
 * @author <a href="http://steemit.com/@dez1337">dez1337</a>
 */
public class SteemErrorDetails {
    @JsonDeserialize(using = ToStringDeserializer.class)
    @Nullable
    private String data;
    private int code;
    private String message;

    @Nullable
    public String getData() {
        return data;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public void setData(String data) {
        this.data = data;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "SteemErrorDetails{" +
                "data='" + data + '\'' +
                ", code=" + code +
                ", message='" + message + '\'' +
                '}';
    }
}
