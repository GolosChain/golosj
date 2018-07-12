package eu.bittrade.libs.golosj.communication.dto;

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
    @JsonProperty("id")
    private int responseId;
    @JsonProperty("jsonrpc")
    private String rpcType;
    private List<T> result;


    @JsonCreator
    public ResponseWrapperDTO(@JsonProperty("result") List<T> result) {
        this.result = result;
    }


    public int getResponseId() {
        return responseId;
    }

    public ResponseWrapperDTO(int responseId, String rpcType, List<T> result) {
        this.responseId = responseId;
        this.rpcType = rpcType;
        this.result = result;
    }

    public void setResponseId(int responseId) {
        this.responseId = responseId;
    }

    public void setRpcType(String rpcType) {
        this.rpcType = rpcType;
    }

    public void setResult(List<T> result) {
        this.result = result;
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
