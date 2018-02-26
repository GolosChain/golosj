package eu.bittrade.libs.steemj.base.models.error;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author <a href="http://steemit.com/@dez1337">dez1337</a>
 */
public class SteemError {
    @JsonProperty("id")
    private int responseId;
    @JsonProperty("error")
    private SteemErrorDetails steemErrorDetails;

    public int getResponseId() {
        return responseId;
    }

    public SteemErrorDetails getSteemErrorDetails() {
        return steemErrorDetails;
    }

    @Override
    public String toString() {
        return "SteemError{" +
                "responseId=" + responseId +
                ", steemErrorDetails=" + steemErrorDetails +
                '}';
    }
}
