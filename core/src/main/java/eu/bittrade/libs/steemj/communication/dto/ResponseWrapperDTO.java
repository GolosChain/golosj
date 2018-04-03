package eu.bittrade.libs.steemj.communication.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * As every response starts with an id and a result element, this wrapper class
 * can carry every kind of responses.
 *
 * @author <a href="http://steemit.com/@dez1337">dez1337</a>
 */

public class ResponseWrapperDTO<T> {

    private int responseId;
    @JsonProperty("jsonrpc")
    private String rpcType;
    private List<T> result;

    @JsonCreator
    public ResponseWrapperDTO(@JsonProperty("result") List<T> result) {
        this.result = result;
    }

    @JsonProperty("id")
    public int getResponseId() {
        return responseId;
    }

    public List<T> getResult() {
        return result;
    }

    public boolean isEmpty() {
        return result == null || result.size() == 0 || result.get(0) == null;
    }

    @Override
    public String toString() {
        return "ResponseWrapperDTO{" +
                "responseId=" + responseId +
                ", result=" + result +
                '}';
    }
}
