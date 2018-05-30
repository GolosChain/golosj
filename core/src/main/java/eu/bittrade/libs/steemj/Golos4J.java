package eu.bittrade.libs.steemj;


import eu.bittrade.libs.steemj.base.models.AccountName;
import eu.bittrade.libs.steemj.base.models.Price;
import eu.bittrade.libs.steemj.communication.CommunicationHandler;
import eu.bittrade.libs.steemj.communication.dto.RequestWrapperDTO;
import eu.bittrade.libs.steemj.configuration.PrivateKeyStorage;
import eu.bittrade.libs.steemj.configuration.SteemJConfig;
import eu.bittrade.libs.steemj.enums.PrivateKeyType;
import eu.bittrade.libs.steemj.enums.RequestMethods;
import eu.bittrade.libs.steemj.enums.SteemApis;
import eu.bittrade.libs.steemj.enums.SteemitAddressPrefix;
import eu.bittrade.libs.steemj.exceptions.SteemCommunicationException;
import eu.bittrade.libs.steemj.util.AuthUtils;
import eu.bittrade.libs.steemj.util.ImmutablePair;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;

/**
 * Created by yuri yurivladdurain@gmail.com .
 * 46.101.154.98 (testnet.golos.io)
 * qwerty P5KbaLKyg7rWZNWHVNqewHqQwN7CamUfCpGqMm7872K7oieYwQsM
 */

public class Golos4J {

    public  enum HardForkVersion {
        HF_17, HF_18
    }

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
    private final MarketHistory marketHistoryApiMethodsHandler;
    @Nonnull
    private final FollowApiMethods followApiMethodsHandler;
    @Nonnull
    private final NetworkBroadcastMethods networkBroadcastMethodsHandler;

    @Nonnull
    private final SimplifiedOperations simplifiedOperations;
    @Nonnull
    private final GolosIoSpecificMethods golosIoSpecificMethods;

    private HardForkVersion currentHardforkVersion;

    @Nonnull
    public static synchronized Golos4J getInstance() {
        if (instance == null) instance = new Golos4J(null);
        return instance;
    }

    @Nonnull
    public static synchronized Golos4J getTestnet() {

        if (instance == null) {
            SteemJConfig steemJConfig = SteemJConfig.getInstance();
            // steemJConfig.setChainId("782a3039b478c839e4cb0c941ff4eaeb7df40bdd68bd441afd444b9da763de12");
            steemJConfig.setChainId("5876894a41e6361bde2e73278f07340f2eb8b41c2facd29099de9deef6cdb679");
            steemJConfig.setSteemitAddressPrefix(SteemitAddressPrefix.GLS);
            steemJConfig.setResponseTimeout(30_000);
            steemJConfig.setSocketTimeout(30_000);
            try {
                steemJConfig.setWebSocketEndpointURI(new URI("wss://ws.testnet.golos.io"));
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
            instance = new Golos4J(steemJConfig);
        }
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
            //   steemJConfig.setChainId("5876894a41e6361bde2e73278f07340f2eb8b41c2facd29099de9deef6cdb679");
            steemJConfig.setSteemitAddressPrefix(SteemitAddressPrefix.GLS);
            steemJConfig.setResponseTimeout(180_000);
            steemJConfig.setSocketTimeout(180_000);
            steemJConfig.setSteemJWeight((short) 1000);
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
        this.marketHistoryApiMethodsHandler = new GolosMarketHistoryApiHandler(this.steemJConfig, communicationHandler, steemJ);
        this.followApiMethodsHandler = new GolosFollowApiMethodsHandler(this.steemJConfig, communicationHandler, steemJ);
        this.networkBroadcastMethodsHandler = new GolosNetworkBroadcastMethodsHandler(this.steemJConfig, communicationHandler, steemJ);
        this.simplifiedOperations = new GolosSimplifiedOperationsHandler(this.steemJConfig, communicationHandler, steemJ);
        this.golosIoSpecificMethods = new GolosIoSpecificMethodsHandler(this.steemJConfig, CommunicationHandler.getObjectMapper());
    }

    @Nonnull
    public HardForkVersion getCurrentHardforkVersion() throws SteemCommunicationException {
        if (currentHardforkVersion == null) {

            try {
                RequestWrapperDTO requestObject = new RequestWrapperDTO();
                requestObject.setApiMethod(RequestMethods.GET_CURRENT_MEDIAN_HISTORY_PRICE);
                requestObject.setSteemApi(SteemApis.DATABASE_API);
                String[] parameters = {};
                requestObject.setAdditionalParameters(parameters);
                communicationHandler.performRequest(requestObject, Price.class).get(0);
                currentHardforkVersion = HardForkVersion.HF_17;
            } catch (SteemCommunicationException e) {
                currentHardforkVersion = HardForkVersion.HF_18;
            }
        }
        return currentHardforkVersion;
    }

    @Nonnull
    SteemJConfig getSteemJConfig() {
        return steemJConfig;
    }


    public void addAccount(@Nonnull AccountName account, @Nonnull ImmutablePair<PrivateKeyType, String> key, boolean setIsDefaultAccount) {
        addAccount(account, Collections.singleton(key), setIsDefaultAccount);
    }

    public void addAccount(@Nonnull AccountName account, @Nonnull Set<ImmutablePair<PrivateKeyType, String>> keys, boolean setIsDefaultAccount) {
        steemJConfig.getPrivateKeyStorage().addAccount(account, new ArrayList<>(keys));
        if (setIsDefaultAccount) setDefaultAccount(account);
    }

    public void setDefaultAccount(@Nonnull AccountName account) {
        addAccountIfNeeded(account);
        steemJConfig.setDefaultAccount(account);
    }

    public void addKeysToAccount(@Nonnull AccountName account, @Nonnull ImmutablePair<PrivateKeyType, String> key) {
        addKeysToAccount(account, Collections.singleton(key));
    }

    @Nonnull
    public GolosIoSpecificMethods getGolosIoSpecificMethods() {
        return golosIoSpecificMethods;
    }

    public void addAccountUsingMasterPassword(@Nonnull AccountName account, @Nonnull String masterPassword) {
        Map<PrivateKeyType, String> keys = AuthUtils.generatePrivateWiFs(account.getName(), masterPassword, PrivateKeyType.values());
        Set<ImmutablePair<PrivateKeyType, String>> set = new HashSet<>();
        for (Map.Entry<PrivateKeyType, String> pair : keys.entrySet()) {
            set.add(new ImmutablePair<>(pair.getKey(), pair.getValue()));
        }
        addKeysToAccount(account, set);
        setDefaultAccount(account);
    }

    public void addKeysToAccount(@Nonnull AccountName account, @Nonnull Set<ImmutablePair<PrivateKeyType, String>> keys) {
        addAccountIfNeeded(account);
        for (ImmutablePair<PrivateKeyType, String> pair : keys) {
            steemJConfig.getPrivateKeyStorage().addPrivateKeyToAccount(account, pair);
        }
    }

    private void addAccountIfNeeded(@Nonnull AccountName name) {
        PrivateKeyStorage storage = steemJConfig.getPrivateKeyStorage();
        if (storage.getAccounts() == null || !storage.getAccounts().contains(name)) {
            storage.addAccount(name);
        }
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

}
