package eu.bittrade.libs.golosj.exceptions;

import eu.bittrade.libs.golosj.base.models.error.SteemError;

/**
 * A custom Exception to handle timeouts.
 * 
 * @author<a href="http://steemit.com/@dez1337">dez1337</a>
 */
public class SteemResponseError extends SteemCommunicationException {
    private static final long serialVersionUID = 147694337695115012L;

    private final transient SteemError steemError;

    public SteemResponseError(SteemError steemError) {
        super();
        this.steemError = steemError;
    }

    public SteemResponseError(String message, SteemError steemError) {
        super(message);
        this.steemError = steemError;
    }

    public SteemError getError() {
        return steemError;
    }

    @Override
    public String toString() {
        return "SteemResponseError{" +
                "steemError=" + steemError +
                '}';
    }
}
