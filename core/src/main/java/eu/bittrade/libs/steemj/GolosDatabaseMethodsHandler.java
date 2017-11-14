package eu.bittrade.libs.steemj;


import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Nonnull;

import eu.bittrade.libs.steemj.base.models.AccountName;
import eu.bittrade.libs.steemj.base.models.AppliedOperation;
import eu.bittrade.libs.steemj.base.models.BlockHeader;
import eu.bittrade.libs.steemj.base.models.ChainProperties;
import eu.bittrade.libs.steemj.base.models.Config;
import eu.bittrade.libs.steemj.base.models.Discussion;
import eu.bittrade.libs.steemj.base.models.DiscussionQuery;
import eu.bittrade.libs.steemj.base.models.ExtendedAccount;
import eu.bittrade.libs.steemj.base.models.ExtendedLimitOrder;
import eu.bittrade.libs.steemj.base.models.FeedHistory;
import eu.bittrade.libs.steemj.base.models.GlobalProperties;
import eu.bittrade.libs.steemj.base.models.LiquidityBalance;
import eu.bittrade.libs.steemj.base.models.OrderBook;
import eu.bittrade.libs.steemj.base.models.Permlink;
import eu.bittrade.libs.steemj.base.models.Price;
import eu.bittrade.libs.steemj.base.models.ProfileImage;
import eu.bittrade.libs.steemj.base.models.RewardFund;
import eu.bittrade.libs.steemj.base.models.Route;
import eu.bittrade.libs.steemj.base.models.ScheduledHardfork;
import eu.bittrade.libs.steemj.base.models.SignedBlockWithInfo;
import eu.bittrade.libs.steemj.base.models.SignedTransaction;
import eu.bittrade.libs.steemj.base.models.Story;
import eu.bittrade.libs.steemj.base.models.TrendingTag;
import eu.bittrade.libs.steemj.base.models.UserFeed;
import eu.bittrade.libs.steemj.base.models.Vote;
import eu.bittrade.libs.steemj.base.models.VoteState;
import eu.bittrade.libs.steemj.base.models.Witness;
import eu.bittrade.libs.steemj.base.models.WitnessSchedule;
import eu.bittrade.libs.steemj.communication.BlockAppliedCallback;
import eu.bittrade.libs.steemj.communication.CommunicationHandler;
import eu.bittrade.libs.steemj.communication.dto.RequestWrapperDTO;
import eu.bittrade.libs.steemj.configuration.SteemJConfig;
import eu.bittrade.libs.steemj.enums.DiscussionSortType;
import eu.bittrade.libs.steemj.enums.RequestMethods;
import eu.bittrade.libs.steemj.enums.RewardFundType;
import eu.bittrade.libs.steemj.enums.SteemApis;
import eu.bittrade.libs.steemj.exceptions.SteemCommunicationException;
import eu.bittrade.libs.steemj.util.SteemJUtils;

/**
 * Created by yuri yurivladdurain@gmail.com
 */

class GolosDatabaseMethodsHandler implements DatabaseMethods {

    @Nonnull
    private final SteemJConfig config;
    @Nonnull
    private final CommunicationHandler communicationHandler;
    @Nonnull
    private SteemJ steemJ;

    GolosDatabaseMethodsHandler(@Nonnull SteemJConfig config,
                                @Nonnull CommunicationHandler communicationHandler,
                                @Nonnull SteemJ steemJ) {
        this.config = config;
        this.communicationHandler = communicationHandler;
        this.steemJ = steemJ;
    }

    @Override
    public Integer getAccountCount() throws SteemCommunicationException {
        return steemJ.getAccountCount();
    }

    @Override
    public Map<Integer, AppliedOperation> getAccountHistory(AccountName accountName, int from, int limit)
            throws SteemCommunicationException {
        return steemJ.getAccountHistory(accountName, from, limit);
    }

    @Override
    public List<ExtendedAccount> getAccounts(List<AccountName> accountNames) throws SteemCommunicationException {
        return steemJ.getAccounts(accountNames);
    }

    @Override
    public List<Vote> getAccountVotes(AccountName accountName) throws SteemCommunicationException {
        return steemJ.getAccountVotes(accountName);
    }

    @Override
    public SignedBlockWithInfo getBlock(long blockNumber) throws SteemCommunicationException {
        return steemJ.getBlock(blockNumber);
    }

    @Override
    public BlockHeader getBlockHeader(long blockNumber) throws SteemCommunicationException {
        return steemJ.getBlockHeader(blockNumber);
    }

    @Override
    public Discussion getContent(AccountName authorName, Permlink permlink) throws SteemCommunicationException {
        return steemJ.getContent(authorName, permlink);
    }

    @Override
    public ScheduledHardfork getNextScheduledHarfork() throws SteemCommunicationException {
        return steemJ.getNextScheduledHarfork();
    }

    @Override
    public List<VoteState> getActiveVotes(AccountName author, Permlink permlink) throws SteemCommunicationException {
        return steemJ.getActiveVotes(author, permlink);
    }

    @Override
    public List<String> getActiveWitnesses() throws SteemCommunicationException {
        RequestWrapperDTO requestObject = new RequestWrapperDTO();
        requestObject.setApiMethod(RequestMethods.GET_ACTIVE_WITNESSES);
        requestObject.setSteemApi(SteemApis.DATABASE_API);
        String[] parameters = {};
        requestObject.setAdditionalParameters(parameters);
        return communicationHandler.performRequest(requestObject, String.class);
    }

    @Override
    public ChainProperties getChainProperties() throws SteemCommunicationException {
        return steemJ.getChainProperties();
    }

    @Override
    public Config getConfig() throws SteemCommunicationException {
        return steemJ.getConfig();
    }

    @Override
    public List<Discussion> getContentReplies(AccountName author, Permlink permlink) throws SteemCommunicationException {
        return steemJ.getContentReplies(author, permlink);
    }

    @Override
    public Object[] getConversionRequests(AccountName account) throws SteemCommunicationException {
        return steemJ.getConversionRequests(account);
    }

    @Override
    public Price getCurrentMedianHistoryPrice() throws SteemCommunicationException {
        return steemJ.getCurrentMedianHistoryPrice();
    }

    @Override
    public List<Discussion> getDiscussionsBy(DiscussionQuery discussionQuery, DiscussionSortType sortBy)
            throws SteemCommunicationException {
        return steemJ.getDiscussionsBy(discussionQuery, sortBy);
    }

    @Override
    public List<Discussion> getDiscussionsByAuthorBeforeDate(AccountName author, Permlink permlink, long date, int limit)
            throws SteemCommunicationException {
        return steemJ.getDiscussionsByAuthorBeforeDate(author, permlink, SteemJUtils.transformDateToString(new Date(date)), limit);
    }

    @Override
    public List<Discussion> getDiscussionsByAuthorBeforeDate(AccountName author, Permlink permlink, String date, int limit)
            throws SteemCommunicationException, ParseException {
        return steemJ.getDiscussionsByAuthorBeforeDate(author, permlink, date, limit);
    }

    @Override
    public GlobalProperties getDynamicGlobalProperties() throws SteemCommunicationException {
        return steemJ.getDynamicGlobalProperties();
    }

    @Override
    public FeedHistory getFeedHistory() throws SteemCommunicationException {
        return steemJ.getFeedHistory();
    }

    @Override
    public String getHardforkVersion() throws SteemCommunicationException {
        return steemJ.getHardforkVersion();
    }

    @Override
    public List<LiquidityBalance> getLiquidityQueue(AccountName accoutName, int limit) throws SteemCommunicationException {
        return steemJ.getLiquidityQueue(accoutName, limit);
    }

    @Override
    @Nonnull
    public List<String> getMinerQueue() throws SteemCommunicationException {
        RequestWrapperDTO requestObject = new RequestWrapperDTO();
        requestObject.setApiMethod(RequestMethods.GET_MINER_QUEUE);
        requestObject.setSteemApi(SteemApis.DATABASE_API);
        String[] parameters = {};
        requestObject.setAdditionalParameters(parameters);
        List<String> out = communicationHandler.performRequest(requestObject, String.class);
        if (out == null) out = new ArrayList<>();
        return out;
    }

    @Override
    public List<ExtendedLimitOrder> getOpenOrders(AccountName accountName) throws SteemCommunicationException {
        return steemJ.getOpenOrders(accountName);
    }

    @Override
    public OrderBook getOrderBookUsingDatabaseApi(int limit) throws SteemCommunicationException {
        return steemJ.getOrderBookUsingDatabaseApi(limit);
    }

    @Override
    public List<AppliedOperation> getOpsInBlock(int blockNumber, boolean onlyVirtual)
            throws SteemCommunicationException {
        return steemJ.getOpsInBlock(blockNumber, onlyVirtual);
    }

    @Override
    public List<String[]> getPotentialSignatures() throws SteemCommunicationException {
        return steemJ.getPotentialSignatures();
    }

    @Override
    public List<Discussion> getRepliesByLastUpdate(AccountName username, Permlink permlink, int limit)
            throws SteemCommunicationException {
        return steemJ.getRepliesByLastUpdate(username, permlink, limit);
    }


    @Override
    public String getTransactionHex(SignedTransaction signedTransaction) throws SteemCommunicationException {
        return steemJ.getTransactionHex(signedTransaction);
    }

    @Override
    public List<TrendingTag> getTrendingTags(String firstTag, int limit) throws SteemCommunicationException {
        return steemJ.getTrendingTags(firstTag, limit);
    }

    @Override
    public Witness getWitnessByAccount(AccountName witnessName) throws SteemCommunicationException {
        return steemJ.getWitnessByAccount(witnessName);
    }

    @Override
    public List<Witness> getWitnessByVote(AccountName witnessName, int limit) throws SteemCommunicationException {
        return steemJ.getWitnessByVote(witnessName, limit);
    }

    @Override
    public int getWitnessCount() throws SteemCommunicationException {
        return steemJ.getWitnessCount();
    }

    @Override
    public List<Witness> getWitnesses() throws SteemCommunicationException {
        return steemJ.getWitnesses();
    }

    @Override
    public WitnessSchedule getWitnessSchedule() throws SteemCommunicationException {
        return steemJ.getWitnessSchedule();
    }

    @Override
    public List<String> lookupAccounts(String pattern, int limit) throws SteemCommunicationException {
        return steemJ.lookupAccounts(pattern, limit);
    }

    @Override
    public List<String> lookupWitnessAccounts(String pattern, int limit) throws SteemCommunicationException {
        return steemJ.lookupWitnessAccounts(pattern, limit);
    }

    @Override
    public Boolean verifyAuthority(SignedTransaction signedTransaction) throws SteemCommunicationException {
        return steemJ.verifyAuthority(signedTransaction);
    }

    @Override
    public void setBlockAppliedCallback(BlockAppliedCallback blockAppliedCallback) throws SteemCommunicationException {
        steemJ.setBlockAppliedCallback(blockAppliedCallback);
    }

    @Override
    public List<String[]> getKeyReferences(String[] publicKeys) throws SteemCommunicationException {
        return steemJ.getKeyReferences(publicKeys);
    }

    @Override
    public RewardFund getRewardFund(RewardFundType rewordFundType) throws SteemCommunicationException {
        return steemJ.getRewardFund(rewordFundType);
    }

    @Override
    public String getAccountAvatar(AccountName name) throws SteemCommunicationException {
        RequestWrapperDTO requestObject = new RequestWrapperDTO();
        requestObject.setSteemApi(SteemApis.DATABASE_API);
        requestObject.setApiMethod(RequestMethods.GET_STATE);
        String[] parameters = {"@" + name.getName(), "{ truncate_body: 12 }"};
        requestObject.setAdditionalParameters(parameters);

        List<ProfileImage> response = communicationHandler.performRequest(requestObject, ProfileImage.class);
        if (!response.isEmpty()) {
            return response.get(0).getProfilePath();
        }
        return null;
    }

    @Override
    public String getAccountAvatar(String blogName, AccountName authorName, Permlink permlink) throws SteemCommunicationException {
        Story story = getStoryByRoute(blogName, authorName, permlink);
        for (ExtendedAccount account : story.getInvolvedAccounts()) {
            if (account.getName().getName().equals(authorName.getName())) {
                try {
                    if (account.getJsonMetadata() == null || account.getJsonMetadata().length() == 0)
                        return null;
                    JsonNode node = CommunicationHandler.getObjectMapper().readTree(account.getJsonMetadata());
                    if (!node.has("profile")) return null;
                    node = node.get("profile");
                    if (node != null)
                        node = node.get("profile_image");
                    if (node != null) return node.asText();

                } catch (IOException e) {
                    System.out.println("error parsing metadata " + account.getJsonMetadata());
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    @Override
    public Story getStoryByRoute(String blogName, AccountName authorName, Permlink permlink) throws SteemCommunicationException {
        RequestWrapperDTO requestObject = new RequestWrapperDTO();
        requestObject.setSteemApi(SteemApis.DATABASE_API);
        requestObject.setApiMethod(RequestMethods.GET_STATE);
        String[] parameters = {new Route(blogName, authorName, permlink).constructDiscussionRoute()};
        requestObject.setAdditionalParameters(parameters);

        List<Story> response = communicationHandler.performRequest(requestObject, Story.class);
        if (!response.isEmpty()) {
            return response.get(0);
        }

        return null;
    }

    //{"id":37,"method":"call","params":[0,"get_state",["@yuri-vlad-second/feed"]]}
    @Override
    public List<Discussion> getUserFeed(AccountName userName) throws SteemCommunicationException {
        RequestWrapperDTO requestObject = new RequestWrapperDTO();
        requestObject.setSteemApi(SteemApis.DATABASE_API);
        requestObject.setApiMethod(RequestMethods.GET_STATE);
        String[] parameters = {new Route(null, userName, null).constructBlogRoute()};
        requestObject.setAdditionalParameters(parameters);

        List<UserFeed> response = communicationHandler.performRequest(requestObject, UserFeed.class);
        if (!response.isEmpty()) {
            return response.get(0).getDiscussions();
        }
        return null;
    }
}
