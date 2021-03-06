package eu.bittrade.libs.golosj;

import eu.bittrade.libs.golosj.base.models.SignedTransaction;
import eu.bittrade.libs.golosj.communication.CommunicationHandler;
import eu.bittrade.libs.golosj.communication.dto.RequestWrapperDTO;
import eu.bittrade.libs.golosj.configuration.SteemJConfig;
import eu.bittrade.libs.golosj.enums.RequestMethods;
import eu.bittrade.libs.golosj.enums.SteemApis;
import eu.bittrade.libs.golosj.exceptions.SteemCommunicationException;

import javax.annotation.Nonnull;

/**
 * Created by yuri yurivladdurain@gmail.com .
 */

@SuppressWarnings("unused")
class GolosNetworkBroadcastMethodsHandler implements NetworkBroadcastMethods {
    @Nonnull
    private final SteemJConfig config;
    @Nonnull
    private final CommunicationHandler communicationHandler;
    @Nonnull
    private SteemJ steemJ;

    GolosNetworkBroadcastMethodsHandler(@Nonnull SteemJConfig config,
                                        @Nonnull CommunicationHandler communicationHandler,
                                        @Nonnull SteemJ steemJ) {
        this.config = config;
        this.communicationHandler = communicationHandler;
        this.steemJ = steemJ;
    }

    @Override
    public void broadcastTransaction(@Nonnull SignedTransaction transaction) throws SteemCommunicationException {
        RequestWrapperDTO requestObject = new RequestWrapperDTO();
        requestObject.setApiMethod(RequestMethods.BROADCAST_TRANSACTION);
        requestObject.setSteemApi(SteemApis.NETWORK_BROADCAST_API);

        // TODO: transaction.sign();
        Object[] parameters = {transaction};
        requestObject.setAdditionalParameters(parameters);

        communicationHandler.performRequest(requestObject, Object.class);
    }

    @Override
    @Nonnull
    public Boolean broadcastTransactionSynchronous(@Nonnull SignedTransaction trx) throws SteemCommunicationException {
        return steemJ.broadcastTransactionSynchronous(trx);
    }
}
