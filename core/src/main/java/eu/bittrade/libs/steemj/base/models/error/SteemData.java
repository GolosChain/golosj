package eu.bittrade.libs.steemj.base.models.error;

import java.util.Arrays;
import java.util.Map;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author <a href="http://steemit.com/@dez1337">dez1337</a>
 */
public class SteemData {
    private String name;
    // TODO: The error only contains "what" or "api" depending on the kind.
    private String what;
    private String id;
    private Object[] posting;
    private String type;
    private Map<String, Integer> api;
    @JsonProperty("call.method")
    private String callMethod;
    @JsonProperty("call.params")
    private Object[] callParams;

    public Object[] getPosting() {
        return posting;
    }

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }

    public String getWhat() {
        return what;
    }

    public String getType() {
        return type;
    }

    public Map<String, Integer> getApi() {
        return api;
    }

    public String getCallMethod() {
        return callMethod;
    }

    public Object[] getCallParams() {
        return callParams;
    }

    @Override
    public String toString() {
        return "SteemData{" +
                "name='" + name + '\'' +
                ", what='" + what + '\'' +
                ", id='" + id + '\'' +
                ", posting=" + Arrays.toString(posting) +
                ", type='" + type + '\'' +
                ", api=" + api +
                ", callMethod='" + callMethod + '\'' +
                ", callParams=" + Arrays.toString(callParams) +
                '}';
    }
}
