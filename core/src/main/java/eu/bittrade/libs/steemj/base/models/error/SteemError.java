package eu.bittrade.libs.steemj.base.models.error;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author <a href="http://steemit.com/@dez1337">dez1337</a>
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class SteemError {
    @JsonProperty("id")
    private int responseId;
    @JsonProperty("error")
    private SteemErrorDetails steemErrorDetails;
    @JsonProperty("jsonrpc")
    private String rpcVersion;

    public int getResponseId() {
        return responseId;
    }

    public void setResponseId(int responseId) {
        this.responseId = responseId;
    }

    public void setSteemErrorDetails(SteemErrorDetails steemErrorDetails) {
        this.steemErrorDetails = steemErrorDetails;
    }

    public String getRpcVersion() {
        return rpcVersion;
    }

    public void setRpcVersion(String rpcVersion) {
        this.rpcVersion = rpcVersion;
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
