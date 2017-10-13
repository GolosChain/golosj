package eu.bittrade.libs.steemj;

import eu.bittrade.libs.steemj.base.models.AccountName;
import eu.bittrade.libs.steemj.base.models.SteemVersionInfo;
import eu.bittrade.libs.steemj.enums.SteemApis;
import eu.bittrade.libs.steemj.exceptions.SteemCommunicationException;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 *
 */

@SuppressWarnings("unused")
public interface LoginMethods {

    /**
     * Returns the id of an api or null if no api with the given name could be
     * found.
     *
     * @param apiName The name of the api.
     * @return The id for the given api name or null, if the api is not active
     * or does not exist.
     * @throws SteemCommunicationException <ul>
     *                                     <li>If the server was not able to answer the request in the
     *                                     given time (see
     *                                     {@link eu.bittrade.libs.steemj.configuration.SteemJConfig#setResponseTimeout(long)
     *                                     setResponseTimeout}).</li>
     *                                     <li>If there is a connection problem.</li>
     *                                     <li>If the SteemJ is unable to transform the JSON response
     *                                     into a Java object.</li>
     *                                     <li>If the Server returned an error object.</li>
     *                                     </ul>
     */
    @Nullable
    Integer getApiByName(@Nonnull SteemApis apiName) throws SteemCommunicationException;

    @Nullable
    Integer getApiByName(@Nonnull String apiName) throws SteemCommunicationException;

    /**
     * updates all api's id, and set them to to ApiMethods enum id filed
     */
    void updateApiAllApi() throws SteemCommunicationException;

    /**
     * Get the version information of the connected node.
     *
     * @return The steem version that the connected node is running.
     * @throws SteemCommunicationException <ul>
     *                                     <li>If the server was not able to answer the request in the
     *                                     given time (see
     *                                     {@link eu.bittrade.libs.steemj.configuration.SteemJConfig#setResponseTimeout(long)
     *                                     setResponseTimeout}).</li>
     *                                     <li>If there is a connection problem.</li>
     *                                     <li>If the SteemJ is unable to transform the JSON response
     *                                     into a Java object.</li>
     *                                     <li>If the Server returned an error object.</li>
     *                                     </ul>
     */
    SteemVersionInfo getVersion() throws SteemCommunicationException;

    /**
     * Login under the use of the credentials which are stored in the config
     * object.
     * <p>
     * <p>
     * <b>Notice:</b> The login method is only needed to access protected apis.
     * For some apis like the broadcast_api a call of this method with empty
     * strings can be enough to access them.
     *
     * @return true if the login was successful. False otherwise.
     * @throws SteemCommunicationException <ul>
     *                                     <li>If the server was not able to answer the request in the
     *                                     given time (see
     *                                     {@link eu.bittrade.libs.steemj.configuration.SteemJConfig#setResponseTimeout(long)
     *                                     setResponseTimeout}).</li>
     *                                     <li>If there is a connection problem.</li>
     *                                     <li>If the SteemJ is unable to transform the JSON response
     *                                     into a Java object.</li>
     *                                     <li>If the Server returned an error object.</li>
     *                                     </ul>
     */
    Boolean login() throws SteemCommunicationException;

    /**
     * Login under the use of the specified credentials.
     * <p>
     * <p>
     * <b>Notice:</b> The login method is only needed to access protected apis.
     * For some apis like the broadcast_api a call of this method with empty
     * strings can be enough to access them.
     *
     * @param accountName The username used to login.
     * @param password    The password.
     * @return true if the login was successful. False otherwise.
     * @throws SteemCommunicationException <ul>
     *                                     <li>If the server was not able to answer the request in the
     *                                     given time (see
     *                                     {@link eu.bittrade.libs.steemj.configuration.SteemJConfig#setResponseTimeout(long)
     *                                     setResponseTimeout}).</li>
     *                                     <li>If there is a connection problem.</li>
     *                                     <li>If the SteemJ is unable to transform the JSON response
     *                                     into a Java object.</li>
     *                                     <li>If the Server returned an error object.</li>
     *                                     </ul>
     */
    Boolean login(@Nonnull AccountName accountName, @Nonnull String password) throws SteemCommunicationException;
}
