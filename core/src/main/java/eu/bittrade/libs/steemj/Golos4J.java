package eu.bittrade.libs.steemj;

import eu.bittrade.libs.steemj.communication.CommunicationHandler;
import eu.bittrade.libs.steemj.configuration.SteemJConfig;
import eu.bittrade.libs.steemj.enums.SteemitAddressPrefix;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * Created by yuri yurivladdurain@gmail.com .
 */

public class Golos4J {
    @Nullable
    private static Golos4J instance;
    @Nonnull
    private SteemJ steemJ;
    @Nonnull
    private SteemJConfig steemJConfig;
    @Nonnull
    private final CommunicationHandler communicationHandler;
    @Nonnull
    private final DatabaseMethods databaseMethodsHandler;
    @Nonnull
    private final LoginMethods golosLoginMethodsHandler;
    @Nonnull
    private final MarketHistory marketHistoryApiMethodsHandler;
    @Nonnull
    private final FollowApiMethods followApiMethodsHandler;
    @Nonnull
    private final NetworkBroadcastMethods networkBroadcastMethodsHandler;
    @Nonnull
    private final AccountByKeyMethods accountByKeyMethodsHandler;
    @Nonnull
    private final SimplifiedOperations simplifiedOperations;
    @Nonnull
    public static synchronized Golos4J getInstance() {
        if (instance == null) instance = new Golos4J(null);
        return instance;
    }

    @Nonnull
    public static synchronized Golos4J setInstance(@Nullable SteemJConfig golos4JConfig) {
        instance = new Golos4J(golos4JConfig);
        return instance;
    }

    private Golos4J(@Nullable SteemJConfig config) {
        if (config == null) {
            steemJConfig = SteemJConfig.getInstance();
            steemJConfig.setChainId("782a3039b478c839e4cb0c941ff4eaeb7df40bdd68bd441afd444b9da763de12");
            steemJConfig.setSteemitAddressPrefix(SteemitAddressPrefix.GLS);
            steemJConfig.setResponseTimeout(45000);
            try {
                steemJConfig.setWebSocketEndpointURI(new URI("wss://ws.golos.io"));
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
        } else {
            this.steemJConfig = config;
        }
        steemJ = new SteemJ(true);
        this.communicationHandler = steemJ.getCommunicationHandler();
        this.databaseMethodsHandler = new GolosDatabaseMethodsHandler(this.steemJConfig, communicationHandler, steemJ);
        this.golosLoginMethodsHandler = new GolosLoginMethodsHandler(this.steemJConfig, communicationHandler, steemJ);
        this.marketHistoryApiMethodsHandler = new GolosMarketHistoryApiHandler(this.steemJConfig, communicationHandler, steemJ);
        this.followApiMethodsHandler = new GolosFollowApiMethodsHandler(this.steemJConfig, communicationHandler, steemJ);
        this.networkBroadcastMethodsHandler = new GolosNetworkBroadcastMethodsHandler(this.steemJConfig, communicationHandler, steemJ);
        this.accountByKeyMethodsHandler = new GolosAccountByKeyMethodsHandler(this.steemJConfig, communicationHandler, steemJ);
        this.simplifiedOperations = new GolosSimplifiedOperationsHandler(this.steemJConfig, communicationHandler, steemJ);
    }

    @Nonnull
    public SteemJConfig getSteemJConfig() {
        return steemJConfig;
    }

    @Nonnull
    public SteemJ getSteemJ() {
        return steemJ;
    }

    @Nonnull
    public CommunicationHandler getCommunicationHandler() {
        return communicationHandler;
    }

    @Nonnull
    public DatabaseMethods getDatabaseMethods() {
        return databaseMethodsHandler;
    }

    @Nonnull
    public SimplifiedOperations getSimplifiedOperations() {
        return simplifiedOperations;
    }

    @Nonnull
    public LoginMethods getLoginMethods() {
        return golosLoginMethodsHandler;
    }

    @Nonnull
    public MarketHistory getMarketHistoryApiMethods() {
        return marketHistoryApiMethodsHandler;
    }

    @Nonnull
    public FollowApiMethods getFollowApiMethods() {
        return followApiMethodsHandler;
    }

    @Nonnull
    public NetworkBroadcastMethods getNetworkBroadcastMethods() {
        return networkBroadcastMethodsHandler;
    }

    @Nonnull
    public AccountByKeyMethods getAccountByKeyMethodsHandler() {
        return accountByKeyMethodsHandler;
    }
}
