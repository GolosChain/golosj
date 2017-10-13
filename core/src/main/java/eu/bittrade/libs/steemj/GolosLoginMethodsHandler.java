package eu.bittrade.libs.steemj;

import eu.bittrade.libs.steemj.base.models.AccountName;
import eu.bittrade.libs.steemj.base.models.SteemVersionInfo;
import eu.bittrade.libs.steemj.communication.CommunicationHandler;
import eu.bittrade.libs.steemj.communication.dto.RequestWrapperDTO;
import eu.bittrade.libs.steemj.configuration.SteemJConfig;
import eu.bittrade.libs.steemj.enums.RequestMethods;
import eu.bittrade.libs.steemj.enums.SteemApis;
import eu.bittrade.libs.steemj.exceptions.SteemCommunicationException;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

/**
 * Created by yuri yurivladdurain@gmail.com
 */

class GolosLoginMethodsHandler implements LoginMethods {
    @Nonnull
    private final SteemJConfig config;
    @Nonnull
    private final CommunicationHandler communicationHandler;
    @Nonnull
    private SteemJ steemJ;

    GolosLoginMethodsHandler(@Nonnull SteemJConfig config,
                             @Nonnull CommunicationHandler communicationHandler,
                             @Nonnull SteemJ steemJ) {
        this.config = config;
        this.communicationHandler = communicationHandler;
        this.steemJ = steemJ;
    }
    @Override
    @Nullable
    public Integer getApiByName(@Nonnull SteemApis apiName) throws SteemCommunicationException {
        return steemJ.getApiByName(apiName.toString().toLowerCase());
    }
    @Override
    @Nullable
    public Integer getApiByName(@Nonnull String apiName) throws SteemCommunicationException {
        return steemJ.getApiByName(apiName.toLowerCase());
    }
    @Override
    public void updateApiAllApi() throws SteemCommunicationException {
        for (SteemApis api : SteemApis.values()) {
            if (api.getApiId() == null) continue;
            RequestWrapperDTO requestObject = new RequestWrapperDTO();
            requestObject.setApiMethod(RequestMethods.GET_API_BY_NAME);
            requestObject.setSteemApi(SteemApis.LOGIN_API);
            String[] parameters = {api.toString().toLowerCase()};
            requestObject.setAdditionalParameters(parameters);

            List<Integer> response = communicationHandler.performRequest(requestObject, Integer.class);
            if (!response.isEmpty() && response.size() > 0 && response.get(0) != null) {
                api.setApiId(response.get(0));
            }
        }
    }

    @Override
    public SteemVersionInfo getVersion() throws SteemCommunicationException {
        return steemJ.getVersion();
    }
    @Override
    public Boolean login() throws SteemCommunicationException {
        return steemJ.login();
    }
    @Override
    public Boolean login(@Nonnull AccountName accountName, @Nonnull String password) throws SteemCommunicationException {
        return steemJ.login(accountName, password);
    }
}
