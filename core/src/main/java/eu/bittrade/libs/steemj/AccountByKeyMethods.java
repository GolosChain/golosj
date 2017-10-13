package eu.bittrade.libs.steemj;

import eu.bittrade.libs.steemj.exceptions.SteemCommunicationException;

import javax.annotation.Nonnull;
import java.util.List;

/**
 * Created by yuri yurivladdurain@gmail.com .
 */

@SuppressWarnings("unused")
public interface AccountByKeyMethods {

    /**
     * Search for users under the use of their public key(s).
     *
     * @param publicKeys An array containing one or more public keys.
     * @return A list of arrays containing the matching account names.
     * @throws SteemCommunicationException <ul>
     *                                     <li>If the server was not able to answer the request in the
     *                                     given time (see
     *                                     {@link eu.bittrade.libs.steemj.configuration.SteemJConfig#setResponseTimeout(long)}
     *                                     setTimeout})</li>
     *                                     <li>If there is a connection problem.</li>
     *                                     <li>If the SteemJ is unable to transform the JSON response
     *                                     into a Java object.</li>
     *                                     <li>If the Server returned an error object.</li>
     *                                     </ul>
     */
    @Nonnull
    List<String[]> getKeyReferences(@Nonnull String[] publicKeys) throws SteemCommunicationException;
}
