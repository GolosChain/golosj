package eu.bittrade.libs.golosj;


import eu.bittrade.libs.golosj.base.models.SignedTransaction;
import eu.bittrade.libs.golosj.base.models.Transaction;
import eu.bittrade.libs.golosj.exceptions.SteemCommunicationException;

import javax.annotation.Nonnull;

/**
 * Created by yuri yurivladdurain@gmail.com .
 */

@SuppressWarnings("unused")
public interface NetworkBroadcastMethods {
    /**
     * Broadcast a transaction on the Steem blockchain.
     *
     * @param transaction
     *            A transaction object that has been signed.
     * @throws SteemCommunicationException
     *             <ul>
     *             <li>If the server was not able to answer the request in the
     *             given time (see
     *             {@link eu.bittrade.libs.golosj.configuration.SteemJConfig#setResponseTimeout(long)
     *             setResponseTimeout}).</li>
     *             <li>If there is a connection problem.</li>
     *             <li>If the SteemJ is unable to transform the JSON response
     *             into a Java object.</li>
     *             <li>If the Server returned an error object.</li>
     *             </ul>
     */
    void broadcastTransaction(@Nonnull SignedTransaction transaction) throws SteemCommunicationException;

    // TODO implement this!
    @Nonnull
    Boolean broadcastTransactionSynchronous(@Nonnull SignedTransaction trx) throws SteemCommunicationException;
}
