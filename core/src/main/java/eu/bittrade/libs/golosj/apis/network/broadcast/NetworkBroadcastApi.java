package eu.bittrade.libs.golosj.apis.network.broadcast;

import eu.bittrade.libs.golosj.base.models.SignedTransaction;
import eu.bittrade.libs.golosj.communication.CommunicationHandler;
import eu.bittrade.libs.golosj.communication.dto.RequestWrapperDTO;
import eu.bittrade.libs.golosj.enums.RequestMethods;
import eu.bittrade.libs.golosj.enums.SteemApis;
import eu.bittrade.libs.golosj.exceptions.SteemCommunicationException;

/**
 * This class implements the network broadcast api which is required to send
 * transactions.
 * 
 * @author <a href="http://steemit.com/@dez1337">dez1337</a>
 */
public class NetworkBroadcastApi {
    /** Add a private constructor to hide the implicit public one. */
    private NetworkBroadcastApi() {
    }

    /*
     * TODO: Implement all methods of this API: (broadcast_transaction)
     * (broadcast_transaction_with_callback) (broadcast_transaction_synchronous)
     * (broadcast_block) (set_max_block_age)
     */
    /**
     * Broadcast a transaction on the Steem blockchain.
     * 
     * @param communicationHandler
     * 
     * @param transaction
     *            A transaction object that has been signed.
     * @throws SteemCommunicationException
     *             <ul>
     *             <li>If the server was not able to answer the request in the
     *             given time (see
     *             {@link eu.bittrade.libs.golosj.configuration.golosjConfig#setResponseTimeout(long)
     *             setResponseTimeout})</li>
     *             <li>If there is a connection problem.</li>
     *             <li>If the golosj is unable to transform the JSON response
     *             into a Java object.</li>
     *             <li>If the Server returned an error object.</li>
     *             </ul>
     */
    public void broadcastTransaction(CommunicationHandler communicationHandler, SignedTransaction transaction)
            throws SteemCommunicationException {
        RequestWrapperDTO requestObject = new RequestWrapperDTO();
        requestObject.setApiMethod(RequestMethods.BROADCAST_TRANSACTION);
        requestObject.setSteemApi(SteemApis.NETWORK_BROADCAST_API);

        // TODO: transaction.sign();
        Object[] parameters = { transaction };
        requestObject.setAdditionalParameters(parameters);

        communicationHandler.performRequest(requestObject, Object.class);
    }

    // TODO implement this!
    public Boolean broadcastTransactionSynchronous(SignedTransaction transaction) throws SteemCommunicationException {
        RequestWrapperDTO requestObject = new RequestWrapperDTO();
        requestObject.setApiMethod(RequestMethods.BROADCAST_TRANSACTION_SYNCHRONOUS);
        requestObject.setSteemApi(SteemApis.NETWORK_BROADCAST_API);
        Object[] parameters = { transaction };
        requestObject.setAdditionalParameters(parameters);

        return null;
    }
}
