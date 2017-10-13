package eu.bittrade.libs.steemj;

import eu.bittrade.libs.steemj.base.models.SignedTransaction;
import eu.bittrade.libs.steemj.communication.CommunicationHandler;
import eu.bittrade.libs.steemj.configuration.SteemJConfig;
import eu.bittrade.libs.steemj.exceptions.SteemCommunicationException;

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
        steemJ.broadcastTransaction(transaction);
    }

    @Override
    @Nonnull
    public Boolean broadcastTransactionSynchronous(@Nonnull SignedTransaction trx) throws SteemCommunicationException {
        return steemJ.broadcastTransactionSynchronous(trx);
    }
}
