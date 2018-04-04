package eu.bittrade.libs.steemj;


import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import eu.bittrade.libs.steemj.base.models.AccountName;
import eu.bittrade.libs.steemj.base.models.AppliedOperation;
import eu.bittrade.libs.steemj.base.models.BlockHeader;
import eu.bittrade.libs.steemj.base.models.ChainProperties;
import eu.bittrade.libs.steemj.base.models.Config;
import eu.bittrade.libs.steemj.base.models.Discussion;
import eu.bittrade.libs.steemj.base.models.DiscussionLight;
import eu.bittrade.libs.steemj.base.models.DiscussionQuery;
import eu.bittrade.libs.steemj.base.models.DiscussionWithComments;
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
import eu.bittrade.libs.steemj.base.models.TrendingTag;
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

    @Nullable
    @Override
    public DiscussionLight getContentLight(@Nonnull AccountName author, @Nonnull Permlink permlink) throws SteemCommunicationException {
        Golos4J.HardForkVersion version = Golos4J.getInstance().getCurrentHardforkVersion();
        RequestWrapperDTO requestObject = new RequestWrapperDTO();
        requestObject.setApiMethod(RequestMethods.GET_CONTENT);
        if (version == Golos4J.HardForkVersion.HF_16) {
            requestObject.setSteemApi(SteemApis.DATABASE_API);
        } else {
            requestObject.setSteemApi(SteemApis.SOCIAL_NETWORK);
        }

        String[] parameters = {author.getName(), permlink.getLink()};
        requestObject.setAdditionalParameters(parameters);

        return communicationHandler.performRequest(requestObject, DiscussionLight.class).get(0);
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

    @Nonnull
    @Override
    public List<DiscussionLight> getDiscussionsLightBy(@Nonnull DiscussionQuery discussionQuery, @Nonnull DiscussionSortType sortBy) throws SteemCommunicationException {
        RequestWrapperDTO requestObject = new RequestWrapperDTO();
        requestObject.setApiMethod(RequestMethods.valueOf(sortBy.name()));
        requestObject.setSteemApi(SteemApis.SOCIAL_NETWORK);
        Object[] parameters = {discussionQuery};
        requestObject.setAdditionalParameters(parameters);
        List<DiscussionLight> out = communicationHandler.performRequest(requestObject, DiscussionLight.class);
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
    public Map<String, String> getAccountAvatar(List<AccountName> name) throws SteemCommunicationException {
        RequestWrapperDTO requestObject = new RequestWrapperDTO();
        requestObject.setSteemApi(SteemApis.DATABASE_API);
        requestObject.setApiMethod(RequestMethods.GET_ACCOUNTS);

        String[] innerParameters = new String[name.size()];
        for (int i = 0; i < name.size(); i++) {
            innerParameters[i] = name.get(i).getName();
        }

        String[][] parameters = {innerParameters};
        requestObject.setAdditionalParameters(parameters);

        List<ProfileImage> response = communicationHandler.performRequest(requestObject, ProfileImage.class);
        HashMap<String, String> out = new HashMap<>(response.size());
        for (int i = 0; i < name.size(); i++) {
            out.put(name.get(i).getName(), null);
        }
        if (!response.isEmpty()) {

            for (int i = 0; i < response.size(); i++) {
                ProfileImage avatar = response.get(i);
                if (avatar != null) out.put(name.get(i).getName(), avatar.getProfilePath());

            }
            return out;
        }
        return null;
    }


    @Override
    public DiscussionWithComments getStoryByRoute(String blogName, AccountName authorName, Permlink permlink) throws SteemCommunicationException {
        if (Golos4J.getInstance().getCurrentHardforkVersion() == Golos4J.HardForkVersion.HF_17)
            return getStoryByRouteHf17(authorName, permlink);

        RequestWrapperDTO requestObject = new RequestWrapperDTO();
        requestObject.setSteemApi(SteemApis.DATABASE_API);
        requestObject.setApiMethod(RequestMethods.GET_STATE);
        String[] parameters = {new Route(blogName, authorName, permlink).constructDiscussionRoute()};
        requestObject.setAdditionalParameters(parameters);

        List<DiscussionWithComments> response = communicationHandler.performRequest(requestObject, DiscussionWithComments.class);
        if (!response.isEmpty()) {
            return response.get(0);
        }

        return null;

    }

    private DiscussionWithComments getStoryByRouteHf17(AccountName authorName, Permlink permlink) throws SteemCommunicationException {
        RequestWrapperDTO requestObject = new RequestWrapperDTO();
        requestObject.setSteemApi(SteemApis.SOCIAL_NETWORK);
        requestObject.setApiMethod(RequestMethods.GET_CONTENT);
        String[] parameters = new String[]{authorName.getName(), permlink.getLink()};
        requestObject.setAdditionalParameters(parameters);

        List<Discussion> response = communicationHandler.performRequest(requestObject, Discussion.class);
        if (response.isEmpty()) return null;

        Discussion discussion = response.get(0);

       /* requestObject = new RequestWrapperDTO();
        requestObject.setSteemApi(SteemApis.SOCIAL_NETWORK);
        requestObject.setApiMethod(RequestMethods.GET_ALL_CONTENT_REPLIES);
        parameters = new String[]{authorName.getName(), permlink.getLink()};
        requestObject.setAdditionalParameters(parameters);


*/


        List<Discussion> comments = getAllContentReplies(discussion.getAuthor(), discussion.getPermlink());

        List<AccountName> accountNames = new ArrayList<>();
        accountNames.add(discussion.getAuthor());
        for (int i = 0; i < comments.size(); i++) {
            if (comments.get(i) != null) {
                AccountName name = comments.get(i).getAuthor();
                if (!accountNames.contains(name)) accountNames.add(name);
            }
        }

        List<ExtendedAccount> accounts = getAccounts(accountNames);

        DiscussionWithComments discussionWithComments = new DiscussionWithComments();

        List<Discussion> all = new ArrayList<>(comments.size() + 1);
        all.add(discussion);

        for (int i = 0; i < comments.size(); i++) {
            Discussion comment = comments.get(i);
            if (comment != null) all.add(comment);

        }


        discussionWithComments.setDiscussions(all);
        discussionWithComments.setInvolvedAccounts(accounts);


        return discussionWithComments;
    }

    private List<Discussion> getAllContentReplies(AccountName nameOfPost, Permlink permlinkOfPost) throws SteemCommunicationException {
        ArrayList<Discussion> out = new ArrayList<>();

        List<Discussion> replies = getContentReplies(nameOfPost, permlinkOfPost);

        for (Discussion discussion : replies) {
            if (discussion != null) out.add(discussion);
        }
        ArrayList<Discussion> supplementary = new ArrayList<>();
        for (Discussion discussion : out) {
            if (discussion.getChildren() != 0) {
                supplementary.addAll(getAllContentReplies(discussion.getAuthor(), discussion.getPermlink()));
            }
        }
        out.addAll(supplementary);
        for (Discussion discussion : out) {
            if (discussion.getNetVotes() != 0) {
                discussion.setActiveVotes(getActiveVotes(discussion.getAuthor(), discussion.getPermlink()));
            }
        }
        return out;
    }
}
