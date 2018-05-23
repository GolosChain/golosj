package eu.bittrade.libs.steemj;


import com.fasterxml.jackson.core.type.TypeReference;
import eu.bittrade.libs.steemj.base.models.*;
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

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.text.ParseException;
import java.util.*;

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
        RequestWrapperDTO requestObject = new RequestWrapperDTO();
        if (Golos4J.getInstance().getCurrentHardforkVersion() == Golos4J.HardForkVersion.HF_17)
            requestObject.setSteemApi(SteemApis.DATABASE_API);
        else requestObject.setSteemApi(SteemApis.ACCOUNT_HISTORY);
        requestObject.setApiMethod(RequestMethods.GET_ACCOUNT_HISTORY);
        String[] parameters = {accountName.getName(), String.valueOf(from), String.valueOf(limit)};
        requestObject.setAdditionalParameters(parameters);

        Map<Integer, AppliedOperation> accountActivities = new HashMap<>();
        List<Object[]> history = communicationHandler.performRequest(requestObject, Object[].class);
        if (history.isEmpty() || history.get(0) == null) return new HashMap<>();

        for (Object[] accountActivity : history) {
            accountActivities.put((Integer) accountActivity[0], (AppliedOperation) CommunicationHandler
                    .getObjectMapper().convertValue(accountActivity[1], new TypeReference<AppliedOperation>() {
                    }));
        }

        return accountActivities;
    }

    @Override
    public List<ExtendedAccount> getAccounts(List<AccountName> accountNames) throws SteemCommunicationException {
        return steemJ.getAccounts(accountNames);
    }

    @Override
    public List<Vote> getAccountVotes(AccountName accountName) throws SteemCommunicationException {
        return getAccountVotes(accountName, -1);
    }

    @Nonnull
    @Override
    public List<Vote> getAccountVotes(@Nonnull AccountName accountName, int voteLimit) throws SteemCommunicationException {
        return communicationHandler.performRequest(getAccountVotesRW(accountName, voteLimit), Vote.class);
    }

    public RequestWrapperDTO getAccountVotesRW(@Nonnull AccountName accountName, int voteLimit) {
        RequestWrapperDTO requestObject = new RequestWrapperDTO();
        requestObject.setSteemApi(SteemApis.SOCIAL_NETWORK);
        requestObject.setApiMethod(RequestMethods.GET_ACCOUNT_VOTES);
        String[] parameters;
        if (voteLimit < 0)
            parameters = new String[]{accountName.getName()};
        else parameters = new String[]{accountName.getName(), String.valueOf(voteLimit)};
        requestObject.setAdditionalParameters(parameters);
        return requestObject;
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
        return getContent(authorName, permlink, -1);
    }

    @Nullable
    @Override
    public Discussion getContent(@Nonnull AccountName author, @Nonnull Permlink permlink,
                                 int voteLimit) throws SteemCommunicationException {
        return communicationHandler.performRequest(getContentRequestWrapper(author, permlink, voteLimit), Discussion.class).get(0);
    }


    @Nullable
    @Override
    public DiscussionLight getContentLight(@Nonnull AccountName author,
                                           @Nonnull Permlink permlink) throws SteemCommunicationException {
        return getContentLight(author, permlink, -1);


    }

    @Nullable
    @Override
    public DiscussionLight getContentLight(@Nonnull AccountName author,
                                           @Nonnull Permlink permlink, int voteLimit) throws SteemCommunicationException {
        return communicationHandler.performRequest(getContentRequestWrapper(author, permlink, voteLimit), DiscussionLight.class).get(0);


    }

    private RequestWrapperDTO getContentRequestWrapper(@Nonnull AccountName author, @Nonnull Permlink permlink,
                                                       int voteLimit) {
        RequestWrapperDTO requestObject = new RequestWrapperDTO();
        requestObject.setApiMethod(RequestMethods.GET_CONTENT);
        requestObject.setSteemApi(SteemApis.SOCIAL_NETWORK);
        String[] parameters;
        if (voteLimit < 0)
            parameters = new String[]{author.getName(), permlink.getLink()};
        else parameters = new String[]{author.getName(), permlink.getLink(), String.valueOf(voteLimit)};
        requestObject.setAdditionalParameters(parameters);
        return requestObject;
    }

    @Override
    public ScheduledHardfork getNextScheduledHarfork() throws SteemCommunicationException {
        return steemJ.getNextScheduledHarfork();
    }

    @Override
    public List<VoteState> getActiveVotes(AccountName author, Permlink permlink) throws SteemCommunicationException {
        return getActiveVotes(author, permlink, -1);
    }

    @Nonnull
    @Override
    public List<VoteState> getActiveVotes(@Nonnull AccountName author, @Nonnull Permlink permlink, int voteLimit) throws SteemCommunicationException {
        return communicationHandler.performRequest(getActiveVotesRW(author, permlink, voteLimit), VoteState.class);
    }

    private RequestWrapperDTO getActiveVotesRW(@Nonnull AccountName author, @Nonnull Permlink permlink, int voteLimit) {
        RequestWrapperDTO requestObject = new RequestWrapperDTO();
        requestObject.setApiMethod(RequestMethods.GET_ACTIVE_VOTES);
        requestObject.setSteemApi(SteemApis.SOCIAL_NETWORK);
        String[] parameters;
        if (voteLimit < 0)
            parameters = new String[]{author.getName(), permlink.getLink()};
        else parameters = new String[]{author.getName(), permlink.getLink(), String.valueOf(voteLimit)};
        requestObject.setAdditionalParameters(parameters);
        return requestObject;
    }

    @Override
    public List<String> getActiveWitnesses() throws SteemCommunicationException {
        RequestWrapperDTO requestObject = new RequestWrapperDTO();
        requestObject.setApiMethod(RequestMethods.GET_ACTIVE_WITNESSES);
        if (Golos4J.getInstance().getCurrentHardforkVersion() == Golos4J.HardForkVersion.HF_17)
            requestObject.setSteemApi(SteemApis.DATABASE_API);
        else requestObject.setSteemApi(SteemApis.WITNESS_API);
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
        return getContentReplies(author, permlink, -1);
    }

    @Nonnull
    @Override
    public List<Discussion> getAllContentReplies(AccountName author, Permlink permlink, int voteLimit) throws SteemCommunicationException {
        RequestWrapperDTO requestObject = new RequestWrapperDTO();
        requestObject.setApiMethod(RequestMethods.GET_ALL_CONTENT_REPLIES);
        requestObject.setSteemApi(SteemApis.SOCIAL_NETWORK);
        String[] parameters;
        if (voteLimit < 0)
            parameters = new String[]{author.getName(), permlink.getLink()};
        else parameters = new String[]{author.getName(), permlink.getLink(), String.valueOf(voteLimit)};
        requestObject.setAdditionalParameters(parameters);

        return communicationHandler.performRequest(requestObject, Discussion.class);
    }

    @Nonnull
    @Override
    public List<DiscussionLight> getContentRepliesLight(AccountName author, Permlink permlink) throws SteemCommunicationException {
        return getContentRepliesLight(author, permlink, -1);
    }

    @Nonnull
    @Override
    public List<Discussion> getContentReplies(AccountName author, Permlink permlink, int voteLimit) throws SteemCommunicationException {
        return communicationHandler.performRequest(getGetContentRepliesRW(author, permlink, voteLimit), Discussion.class);
    }

    @Nonnull
    @Override
    public List<DiscussionLight> getContentRepliesLight(AccountName author, Permlink permlink, int voteLimit) throws SteemCommunicationException {
        return communicationHandler.performRequest(getGetContentRepliesRW(author, permlink, voteLimit), DiscussionLight.class);
    }

    private RequestWrapperDTO getGetContentRepliesRW(AccountName author, Permlink permlink, int voteLimit) {
        RequestWrapperDTO requestObject = new RequestWrapperDTO();
        requestObject.setApiMethod(RequestMethods.GET_CONTENT_REPLIES);
        requestObject.setSteemApi(SteemApis.SOCIAL_NETWORK);
        String[] parameters;
        if (voteLimit < 0)
            parameters = new String[]{author.getName(), permlink.getLink()};
        else parameters = new String[]{author.getName(), permlink.getLink(), String.valueOf(voteLimit)};
        requestObject.setAdditionalParameters(parameters);

        return requestObject;
    }

    @Override
    public Object[] getConversionRequests(AccountName account) throws SteemCommunicationException {
        return steemJ.getConversionRequests(account);
    }

    @Override
    public Price getCurrentMedianHistoryPrice() throws SteemCommunicationException {
        RequestWrapperDTO requestObject = new RequestWrapperDTO();
        requestObject.setApiMethod(RequestMethods.GET_CURRENT_MEDIAN_HISTORY_PRICE);

        if (Golos4J.getInstance().getCurrentHardforkVersion() == Golos4J.HardForkVersion.HF_17)
            requestObject.setSteemApi(SteemApis.DATABASE_API);
        else requestObject.setSteemApi(SteemApis.WITNESS_API);
        String[] parameters = {};
        requestObject.setAdditionalParameters(parameters);

        return communicationHandler.performRequest(requestObject, Price.class).get(0);
    }

    @Override
    public List<Discussion> getDiscussionsBy(DiscussionQuery discussionQuery, DiscussionSortType sortBy)
            throws SteemCommunicationException {
        RequestWrapperDTO requestObject = new RequestWrapperDTO();

        requestObject.setApiMethod(RequestMethods.valueOf(sortBy.name()));
        if (Golos4J.getInstance().getCurrentHardforkVersion() == Golos4J.HardForkVersion.HF_17)
            requestObject.setSteemApi(SteemApis.SOCIAL_NETWORK);
        else requestObject.setSteemApi(SteemApis.TAGS);
        Object[] parameters = {discussionQuery};
        requestObject.setAdditionalParameters(parameters);

        return communicationHandler.performRequest(requestObject, Discussion.class);
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
        RequestWrapperDTO requestObject = new RequestWrapperDTO();
        requestObject.setApiMethod(RequestMethods.GET_FEED_HISTORY);
        if (Golos4J.getInstance().getCurrentHardforkVersion() == Golos4J.HardForkVersion.HF_17)
            requestObject.setSteemApi(SteemApis.DATABASE_API);
        else requestObject.setSteemApi(SteemApis.WITNESS_API);
        String[] parameters = {};
        requestObject.setAdditionalParameters(parameters);

        return communicationHandler.performRequest(requestObject, FeedHistory.class).get(0);
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
        if (Golos4J.getInstance().getCurrentHardforkVersion() == Golos4J.HardForkVersion.HF_17)
            requestObject.setSteemApi(SteemApis.DATABASE_API);
        else requestObject.setSteemApi(SteemApis.WITNESS_API);
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
        if (Golos4J.getInstance().getCurrentHardforkVersion() == Golos4J.HardForkVersion.HF_17)
            requestObject.setSteemApi(SteemApis.SOCIAL_NETWORK);
        else requestObject.setSteemApi(SteemApis.TAGS);
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
        RequestWrapperDTO requestObject = new RequestWrapperDTO();
        requestObject.setApiMethod(RequestMethods.GET_OPS_IN_BLOCK);
        if (Golos4J.getInstance().getCurrentHardforkVersion() == Golos4J.HardForkVersion.HF_17)
            requestObject.setSteemApi(SteemApis.DATABASE_API);
        else requestObject.setSteemApi(SteemApis.OPERATION_HISTORY);
        String[] parameters = {String.valueOf(blockNumber), String.valueOf(onlyVirtual)};
        requestObject.setAdditionalParameters(parameters);

        return communicationHandler.performRequest(requestObject, AppliedOperation.class);
    }

    @Override
    public List<String[]> getPotentialSignatures() throws SteemCommunicationException {
        return steemJ.getPotentialSignatures();
    }

    @Override
    public Transaction getTransaction(long transactionId) throws SteemCommunicationException {
        RequestWrapperDTO requestObject = new RequestWrapperDTO();
        requestObject.setApiMethod(RequestMethods.GET_TRANSACTION);
        if (Golos4J.getInstance().getCurrentHardforkVersion() == Golos4J.HardForkVersion.HF_17)
            requestObject.setSteemApi(SteemApis.DATABASE_API);
        else requestObject.setSteemApi(SteemApis.OPERATION_HISTORY);
        String[] parameters = {String.valueOf(transactionId)};
        requestObject.setAdditionalParameters(parameters);

        return communicationHandler.performRequest(requestObject, Transaction.class).get(0);
    }

    @Override
    public List<Discussion> getRepliesByLastUpdate(AccountName startParentAuthor, Permlink permlink, int limit)
            throws SteemCommunicationException {
        return getRepliesByLastUpdate(startParentAuthor, permlink, limit, -1);
    }

    @Nonnull
    @Override
    public List<Discussion> getRepliesByLastUpdate(@Nonnull AccountName startParentAuthor, @Nonnull Permlink startPermlink,
                                                   int limit, int voteLimit) throws SteemCommunicationException {
        RequestWrapperDTO requestObject = new RequestWrapperDTO();
        requestObject.setApiMethod(RequestMethods.GET_REPLIES_BY_LAST_UPDATE);
        requestObject.setSteemApi(SteemApis.SOCIAL_NETWORK);
        Object[] parameters;
        if (voteLimit < 0)
            parameters = new Object[]{startParentAuthor, startPermlink.getLink(), String.valueOf(limit)};
        else parameters = new Object[]{startParentAuthor, startPermlink.getLink(), String.valueOf(limit), voteLimit};
        requestObject.setAdditionalParameters(parameters);

        return communicationHandler.performRequest(requestObject, Discussion.class);
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
        RequestWrapperDTO requestObject = new RequestWrapperDTO();
        requestObject.setApiMethod(RequestMethods.GET_WITNESS_BY_ACCOUNT);

        if (Golos4J.getInstance().getCurrentHardforkVersion() == Golos4J.HardForkVersion.HF_17)
            requestObject.setSteemApi(SteemApis.DATABASE_API);
        else requestObject.setSteemApi(SteemApis.WITNESS_API);

        String[] parameters = {witnessName.getName()};
        requestObject.setAdditionalParameters(parameters);

        return communicationHandler.performRequest(requestObject, Witness.class).get(0);
    }

    @Override
    public List<Witness> getWitnessByVote(AccountName witnessName, int limit) throws SteemCommunicationException {
        RequestWrapperDTO requestObject = new RequestWrapperDTO();
        requestObject.setApiMethod(RequestMethods.GET_WITNESSES_BY_VOTE);
        if (Golos4J.getInstance().getCurrentHardforkVersion() == Golos4J.HardForkVersion.HF_17)
            requestObject.setSteemApi(SteemApis.DATABASE_API);
        else requestObject.setSteemApi(SteemApis.WITNESS_API);
        String[] parameters = {witnessName.getName(), String.valueOf(limit)};
        requestObject.setAdditionalParameters(parameters);

        return communicationHandler.performRequest(requestObject, Witness.class);
    }

    @Override
    public int getWitnessCount() throws SteemCommunicationException {
        RequestWrapperDTO requestObject = new RequestWrapperDTO();
        requestObject.setApiMethod(RequestMethods.GET_WITNESS_COUNT);
        if (Golos4J.getInstance().getCurrentHardforkVersion() == Golos4J.HardForkVersion.HF_17)
            requestObject.setSteemApi(SteemApis.DATABASE_API);
        else requestObject.setSteemApi(SteemApis.WITNESS_API);
        String[] parameters = {};
        requestObject.setAdditionalParameters(parameters);

        return communicationHandler.performRequest(requestObject, Integer.class).get(0);
    }

    @Override
    public List<Witness> getWitnesses(List<Integer> witnessIndex) throws SteemCommunicationException {
        RequestWrapperDTO requestObject = new RequestWrapperDTO();
        requestObject.setApiMethod(RequestMethods.GET_WITNESSES);
        if (Golos4J.getInstance().getCurrentHardforkVersion() == Golos4J.HardForkVersion.HF_17)
            requestObject.setSteemApi(SteemApis.DATABASE_API);
        else requestObject.setSteemApi(SteemApis.WITNESS_API);
        Integer[][] parameters = {witnessIndex.toArray(new Integer[witnessIndex.size()])};
        requestObject.setAdditionalParameters(parameters);

        return communicationHandler.performRequest(requestObject, Witness.class);

    }

    @Nonnull
    @Override
    public WitnessSchedule getWitnessSchedule() throws SteemCommunicationException {
        RequestWrapperDTO requestObject = new RequestWrapperDTO();
        requestObject.setApiMethod(RequestMethods.GET_WITNESS_SCHEDULE);
        if (Golos4J.getInstance().getCurrentHardforkVersion() == Golos4J.HardForkVersion.HF_17)
            requestObject.setSteemApi(SteemApis.DATABASE_API);
        else requestObject.setSteemApi(SteemApis.WITNESS_API);
        String[] parameters = {};
        requestObject.setAdditionalParameters(parameters);

        return communicationHandler.performRequest(requestObject, WitnessSchedule.class).get(0);
    }

    @Override
    public List<String> lookupAccounts(String pattern, int limit) throws SteemCommunicationException {
        return steemJ.lookupAccounts(pattern, limit);
    }

    @Nonnull
    @Override
    public List<String> lookupWitnessAccounts(String pattern, int limit) throws SteemCommunicationException {

        RequestWrapperDTO requestObject = new RequestWrapperDTO();
        requestObject.setApiMethod(RequestMethods.LOOKUP_WITNESS_ACCOUNTS);
        if (Golos4J.getInstance().getCurrentHardforkVersion() == Golos4J.HardForkVersion.HF_17)
            requestObject.setSteemApi(SteemApis.DATABASE_API);
        else requestObject.setSteemApi(SteemApis.WITNESS_API);
        String[] parameters = {pattern, String.valueOf(limit)};
        requestObject.setAdditionalParameters(parameters);

        return communicationHandler.performRequest(requestObject, String.class);
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
    public DiscussionWithComments getStoryWithRepliesAndInvolvedAccounts(AccountName authorName,
                                                                         Permlink permlink,
                                                                         int voteLimit) throws SteemCommunicationException {
        if (Golos4J.getInstance().getCurrentHardforkVersion() == Golos4J.HardForkVersion.HF_17) voteLimit = -1;
        Discussion discussion = getContent(authorName, permlink, voteLimit);
        if (discussion == null) return null;

        List<Discussion> comments = getAllContentReplies(discussion.getAuthor(), discussion.getPermlink(), voteLimit);

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
}
