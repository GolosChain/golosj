package eu.bittrade.libs.steemj;

import eu.bittrade.libs.steemj.communication.CommunicationHandler;
import eu.bittrade.libs.steemj.configuration.SteemJConfig;
import eu.bittrade.libs.steemj.exceptions.SteemCommunicationException;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by yuri yurivladdurain@gmail.com .
 */

@SuppressWarnings("unused")
class GolosAccountByKeyMethodsHandler implements AccountByKeyMethods {
    @Nonnull
    private final SteemJConfig config;
    @Nonnull
    private final CommunicationHandler communicationHandler;
    @Nonnull
    private final SteemJ steemJ;

    GolosAccountByKeyMethodsHandler(@Nonnull SteemJConfig config,
                                    @Nonnull CommunicationHandler communicationHandler,
                                    @Nonnull SteemJ steemJ) {
        this.config = config;
        this.communicationHandler = communicationHandler;
        this.steemJ = steemJ;
    }

    @Override
    @Nonnull
    public List<String[]> getKeyReferences(@Nonnull String[] publicKeys) throws SteemCommunicationException {
        List<String[]> keyReferences = Golos4J.getInstance().getSteemJ().getKeyReferences(publicKeys);
        if (keyReferences == null) keyReferences = new ArrayList<>();
        return keyReferences;
    }
}
