package eu.bittrade.libs.golosj.communication.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import eu.bittrade.libs.golosj.enums.RequestMethods;
import eu.bittrade.libs.golosj.enums.SteemApis;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.atomic.AtomicLong;

/**
 * A wrapper object that carries all required fields for a request.
 *
 * @author <a href="http://steemit.com/@dez1337">dez1337</a>
 */
@JsonPropertyOrder({"id", "method", "params"})
public class RequestWrapperDTO {
    private static final ObjectMapper MAPPER = new ObjectMapper();
    private static final Logger LOGGER = LoggerFactory.getLogger(RequestWrapperDTO.class);

    /**
     * The id of the request (used to identify which answer belongs to which
     * request
     */
    @JsonIgnore
    private static final AtomicLong ID_COUNTER = new AtomicLong(-1);
    @JsonIgnore
    private SteemApis steemApi;
    @JsonIgnore
    private RequestMethods apiMethod;
    @JsonIgnore
    private Object[] additionalParameters;

    private static final String JSONRPC = "2.0";
    private static final String METHOD = "call";

    private long id;

    /**
     * Instantiate a new RequestObject.
     */
    public RequestWrapperDTO() {
        this.id = ID_COUNTER.incrementAndGet();
    }

    /**
     * Get the api type used for this request.
     *
     * @return The selected steem api name.
     */
    public SteemApis getSteemApi() {
        return steemApi;
    }

    /**
     * Set the api type you want to request (@see SteemApis)
     *
     * @param steemApi The name of the api you want to connect to.
     */
    public void setSteemApi(SteemApis steemApi) {
        this.steemApi = steemApi;
    }

    /**
     * Get the api method used for this request.
     *
     * @return The selected steem api method.
     */
    public RequestMethods getApiMethod() {
        return apiMethod;
    }

    /**
     * Set the API-Method (@see RequestMethods).
     *
     * @param apiMethod The api method you want to use.
     */
    public void setApiMethod(RequestMethods apiMethod) {
        this.apiMethod = apiMethod;
    }

    /**
     * Get the additional parameters for this request.
     *
     * @return The additional user parameters.
     */
    public Object[] getAdditionalParameters() {
        return additionalParameters;
    }

    /**
     * Add custom parameters to this request.
     *
     * @param userParameters The additional parameters you want to use.
     */
    public void setAdditionalParameters(Object[] userParameters) {
        this.additionalParameters = userParameters;
    }

    /**
     * Get the complete list of parameters used for this request.
     *
     * @return The final params fields, used for this request.
     */
    public Object[] getParams() {
        Object[] params = new Object[3];
        params[0] = steemApi.toString().toLowerCase();
        params[1] = getApiMethod().toString().toLowerCase();
        params[2] = additionalParameters;

        return params;
    }

    /**
     * Get the API-Method that will be used for this request.
     *
     * @return The value of the "method" field.
     */
    public String getMethod() {
        return METHOD;
    }

    /**
     * Get the json-rpc version.
     *
     * @return The used json-rpc version.
     */
    public String getJsonrpc() {
        return JSONRPC;
    }

    /**
     * Get the id of this request.
     *
     * @return The id of this request.
     */
    public long getId() {
        return id;
    }

    /**
     * Increments the global request id.
     *
     * @return The current value of the global request id after it has been
     * incremented.
     */
    public static long incrementGlobalRequestId() {
        return ID_COUNTER.incrementAndGet();
    }

    @Override
    public String toString() {
        try {
            return MAPPER.writeValueAsString(this);
        } catch (JsonProcessingException e) {
            LOGGER.error("Could not transform object to JSON.", e);
            return "";
        }
    }
}
