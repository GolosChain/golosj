package eu.bittrade.libs.golosj;


import com.fasterxml.jackson.core.type.TypeReference;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import eu.bittrade.libs.golosj.base.models.AccountName;
import eu.bittrade.libs.golosj.base.models.AppliedOperation;
import eu.bittrade.libs.golosj.base.models.BlockHeader;
import eu.bittrade.libs.golosj.base.models.ChainProperties;
import eu.bittrade.libs.golosj.base.models.Config;
import eu.bittrade.libs.golosj.base.models.Discussion;
import eu.bittrade.libs.golosj.base.models.DiscussionLight;
import eu.bittrade.libs.golosj.base.models.DiscussionQuery;
import eu.bittrade.libs.golosj.base.models.DiscussionWithComments;
import eu.bittrade.libs.golosj.base.models.ExtendedAccount;
import eu.bittrade.libs.golosj.base.models.ExtendedLimitOrder;
import eu.bittrade.libs.golosj.base.models.FeedHistory;
import eu.bittrade.libs.golosj.base.models.GlobalProperties;
import eu.bittrade.libs.golosj.base.models.OrderBook;
import eu.bittrade.libs.golosj.base.models.Permlink;
import eu.bittrade.libs.golosj.base.models.Price;
import eu.bittrade.libs.golosj.base.models.ProfileImage;
import eu.bittrade.libs.golosj.base.models.RewardFund;
import eu.bittrade.libs.golosj.base.models.ScheduledHardfork;
import eu.bittrade.libs.golosj.base.models.SignedBlockWithInfo;
import eu.bittrade.libs.golosj.base.models.SignedTransaction;
import eu.bittrade.libs.golosj.base.models.Transaction;
import eu.bittrade.libs.golosj.base.models.TrendingTag;
import eu.bittrade.libs.golosj.base.models.Vote;
import eu.bittrade.libs.golosj.base.models.VoteState;
import eu.bittrade.libs.golosj.base.models.Witness;
import eu.bittrade.libs.golosj.base.models.WitnessSchedule;
import eu.bittrade.libs.golosj.communication.BlockAppliedCallback;
import eu.bittrade.libs.golosj.communication.CommunicationHandler;
import eu.bittrade.libs.golosj.communication.dto.RequestWrapperDTO;
import eu.bittrade.libs.golosj.configuration.SteemJConfig;
import eu.bittrade.libs.golosj.enums.DiscussionSortType;
import eu.bittrade.libs.golosj.enums.RequestMethods;
import eu.bittrade.libs.golosj.enums.RewardFundType;
import eu.bittrade.libs.golosj.enums.SteemApis;
import eu.bittrade.libs.golosj.exceptions.SteemCommunicationException;
import eu.bittrade.libs.golosj.exceptions.SteemTransformationException;
import eu.bittrade.libs.golosj.util.SteemJUtils;

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
        RequestWrapperDTO requestObject = new RequestWrapperDTO();
        requestObject.setApiMethod(RequestMethods.GET_ACCOUNT_COUNT);
        requestObject.setSteemApi(SteemApis.DATABASE_API);
        String[] parameters = {};
        requestObject.setAdditionalParameters(parameters);

        return communicationHandler.performRequest(requestObject, Integer.class).get(0);
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
        RequestWrapperDTO requestObject = new RequestWrapperDTO();
        requestObject.setSteemApi(SteemApis.DATABASE_API);
        requestObject.setApiMethod(RequestMethods.GET_ACCOUNTS);

        // The API expects an array of arrays here.
        String[] innerParameters = new String[accountNames.size()];
        for (int i = 0; i < accountNames.size(); i++) {
            innerParameters[i] = accountNames.get(i).getName();
        }

        String[][] parameters = {innerParameters};

        requestObject.setAdditionalParameters(parameters);
        return communicationHandler.performRequest(requestObject, ExtendedAccount.class);
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
        RequestWrapperDTO requestObject = new RequestWrapperDTO();
        requestObject.setApiMethod(RequestMethods.GET_BLOCK);
        requestObject.setSteemApi(SteemApis.DATABASE_API);
        String[] parameters = {String.valueOf(blockNumber)};
        requestObject.setAdditionalParameters(parameters);

        return communicationHandler.performRequest(requestObject, SignedBlockWithInfo.class).get(0);

    }

    @Override
    public BlockHeader getBlockHeader(long blockNumber) throws SteemCommunicationException {
        RequestWrapperDTO requestObject = new RequestWrapperDTO();
        requestObject.setApiMethod(RequestMethods.GET_BLOCK_HEADER);
        requestObject.setSteemApi(SteemApis.DATABASE_API);
        String[] parameters = {String.valueOf(blockNumber)};
        requestObject.setAdditionalParameters(parameters);

        return communicationHandler.performRequest(requestObject, BlockHeader.class).get(0);

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
        else
            parameters = new String[]{author.getName(), permlink.getLink(), String.valueOf(voteLimit)};
        requestObject.setAdditionalParameters(parameters);
        return requestObject;
    }

    @Override
    public ScheduledHardfork getNextScheduledHarfork() throws SteemCommunicationException {
        RequestWrapperDTO requestObject = new RequestWrapperDTO();
        requestObject.setApiMethod(RequestMethods.GET_NEXT_SCHEDULED_HARDFORK);
        requestObject.setSteemApi(SteemApis.DATABASE_API);
        String[] parameters = {};
        requestObject.setAdditionalParameters(parameters);

        return communicationHandler.performRequest(requestObject, ScheduledHardfork.class).get(0);
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
        else
            parameters = new String[]{author.getName(), permlink.getLink(), String.valueOf(voteLimit)};
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
        RequestWrapperDTO requestObject = new RequestWrapperDTO();
        requestObject.setApiMethod(RequestMethods.GET_CHAIN_PROPERTIES);
        requestObject.setSteemApi(SteemApis.DATABASE_API);
        String[] parameters = {};
        requestObject.setAdditionalParameters(parameters);

        return communicationHandler.performRequest(requestObject, ChainProperties.class).get(0);
    }

    @Override
    public Config getConfig() throws SteemCommunicationException {
        RequestWrapperDTO requestObject = new RequestWrapperDTO();
        requestObject.setApiMethod(RequestMethods.GET_CONFIG);
        requestObject.setSteemApi(SteemApis.DATABASE_API);
        String[] parameters = {};
        requestObject.setAdditionalParameters(parameters);

        return communicationHandler.performRequest(requestObject, Config.class).get(0);
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
        else
            parameters = new String[]{author.getName(), permlink.getLink(), String.valueOf(voteLimit)};
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
        else
            parameters = new String[]{author.getName(), permlink.getLink(), String.valueOf(voteLimit)};
        requestObject.setAdditionalParameters(parameters);

        return requestObject;
    }

    @Override
    public Object[] getConversionRequests(AccountName account) throws SteemCommunicationException {
        RequestWrapperDTO requestObject = new RequestWrapperDTO();
        requestObject.setApiMethod(RequestMethods.GET_CONVERSION_REQUESTS);
        requestObject.setSteemApi(SteemApis.DATABASE_API);
        String[] parameters = {account.getName()};
        requestObject.setAdditionalParameters(parameters);

        return communicationHandler.performRequest(requestObject, Object[].class).get(0);
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
        return getDiscussionsByAuthorBeforeDate(author, permlink, date, limit, -1);
    }

    @Nonnull
    @Override
    public List<Discussion> getDiscussionsByAuthorBeforeDate(@Nonnull AccountName author, @Nonnull Permlink permlink,
                                                             long date, int limit, int voteLimit) throws SteemCommunicationException {
        return communicationHandler.performRequest(getDiscussionsByAuthorBeforeDateRW(author, permlink,
                SteemJUtils.transformDateToString(new Date(date)), limit, voteLimit), Discussion.class);
    }

    private RequestWrapperDTO getDiscussionsByAuthorBeforeDateRW(@Nonnull AccountName author, @Nonnull Permlink permlink,
                                                                 String date, int limit, int voteLimit) throws SteemCommunicationException {
        RequestWrapperDTO requestObject = new RequestWrapperDTO();

        requestObject.setApiMethod(RequestMethods.GET_DISCUSSIONS_BY_AUTHOR_BEFORE_DATE);
        if (Golos4J.getInstance().getCurrentHardforkVersion() == Golos4J.HardForkVersion.HF_17)
            requestObject.setSteemApi(SteemApis.SOCIAL_NETWORK);
        else requestObject.setSteemApi(SteemApis.TAGS);


        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(SteemJConfig.getInstance().getDateTimePattern());
        simpleDateFormat.setTimeZone(TimeZone.getTimeZone(SteemJConfig.getInstance().getTimeZoneId()));
        Date beforeDate;
        try {
            beforeDate = simpleDateFormat.parse(date);
        } catch (ParseException e) {
            throw new SteemTransformationException("Could not parse the received date to a Date object.", e);
        }
        String[] parameters;
        if (voteLimit < 0)
            parameters = new String[]{author.getName(), permlink.getLink(), simpleDateFormat.format(beforeDate),
                    String.valueOf(limit)};
        else
            parameters = new String[]{author.getName(), permlink.getLink(), simpleDateFormat.format(beforeDate),
                    String.valueOf(limit), String.valueOf(voteLimit)};
        requestObject.setAdditionalParameters(parameters);
        return requestObject;
    }

    @Override
    public List<Discussion> getDiscussionsByAuthorBeforeDate(AccountName author, Permlink permlink, String date, int limit)
            throws SteemCommunicationException, ParseException {
        return communicationHandler.performRequest(getDiscussionsByAuthorBeforeDateRW(author, permlink, date, limit, -1), Discussion.class);

    }

    @Override
    public GlobalProperties getDynamicGlobalProperties() throws SteemCommunicationException {
        RequestWrapperDTO requestObject = new RequestWrapperDTO();
        requestObject.setApiMethod(RequestMethods.GET_DYNAMIC_GLOBAL_PROPERTIES);
        requestObject.setSteemApi(SteemApis.DATABASE_API);
        String[] parameters = {};
        requestObject.setAdditionalParameters(parameters);

        return communicationHandler.performRequest(requestObject, GlobalProperties.class).get(0);
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
        RequestWrapperDTO requestObject = new RequestWrapperDTO();
        requestObject.setApiMethod(RequestMethods.GET_OPEN_ORDERS);
        requestObject.setSteemApi(SteemApis.DATABASE_API);
        String[] parameters = {accountName.getName()};
        requestObject.setAdditionalParameters(parameters);

        return communicationHandler.performRequest(requestObject, ExtendedLimitOrder.class);
    }

    @Override
    public OrderBook getOrderBookUsingDatabaseApi(int limit) throws SteemCommunicationException {
        RequestWrapperDTO requestObject = new RequestWrapperDTO();
        requestObject.setApiMethod(RequestMethods.GET_ORDER_BOOK);
        requestObject.setSteemApi(SteemApis.DATABASE_API);
        String[] parameters = {String.valueOf(limit)};
        requestObject.setAdditionalParameters(parameters);

        return communicationHandler.performRequest(requestObject, OrderBook.class).get(0);
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
    public List<Discussion> getRepliesByLastUpdate(@Nonnull AccountName startParentAuthor,
                                                   Permlink startPermlink,
                                                   int limit,
                                                   int voteLimit) throws SteemCommunicationException {
        RequestWrapperDTO requestObject = new RequestWrapperDTO();
        requestObject.setApiMethod(RequestMethods.GET_REPLIES_BY_LAST_UPDATE);
        requestObject.setSteemApi(SteemApis.SOCIAL_NETWORK);
        Object[] parameters;
        String link = "";
        if (startPermlink != null) link = startPermlink.getLink();
        if (voteLimit < 0)
            parameters = new Object[]{startParentAuthor, link, String.valueOf(limit)};
        else parameters = new Object[]{startParentAuthor, link, String.valueOf(limit), voteLimit};
        requestObject.setAdditionalParameters(parameters);

        return communicationHandler.performRequest(requestObject, Discussion.class);
    }

    @Nonnull
    @Override
    public List<DiscussionLight> getRepliesLightByLastUpdate(@Nonnull AccountName startParentAuthor, Permlink startPermlink, int limit) throws SteemCommunicationException {
        RequestWrapperDTO requestObject = new RequestWrapperDTO();
        requestObject.setApiMethod(RequestMethods.GET_REPLIES_BY_LAST_UPDATE);
        requestObject.setSteemApi(SteemApis.SOCIAL_NETWORK);
        Object[] parameters;
        String link = "";
        if (startPermlink != null) link = startPermlink.getLink();
        parameters = new Object[]{startParentAuthor, link, String.valueOf(limit)};
        requestObject.setAdditionalParameters(parameters);

        return communicationHandler.performRequest(requestObject, DiscussionLight.class);
    }

    @Override
    public String getTransactionHex(SignedTransaction signedTransaction) throws SteemCommunicationException {
        RequestWrapperDTO requestObject = new RequestWrapperDTO();
        requestObject.setApiMethod(RequestMethods.GET_TRANSACTION_HEX);
        requestObject.setSteemApi(SteemApis.DATABASE_API);

        Object[] parameters = {signedTransaction};
        requestObject.setAdditionalParameters(parameters);

        return communicationHandler.performRequest(requestObject, String.class).get(0);
    }

    @Nonnull
    @Override
    public List<TrendingTag> getTagsUsedByAuthor(@Nonnull AccountName author) throws SteemCommunicationException {
        RequestWrapperDTO requestObject = new RequestWrapperDTO();
        requestObject.setApiMethod(RequestMethods.GET_TAGS_USED_BY_AUTHOR);
        if (Golos4J.getInstance().getCurrentHardforkVersion() == Golos4J.HardForkVersion.HF_17)
            requestObject.setSteemApi(SteemApis.SOCIAL_NETWORK);
        else requestObject.setSteemApi(SteemApis.TAGS);
        String[] parameters = {author.getName()};
        requestObject.setAdditionalParameters(parameters);

        return communicationHandler.performRequest(requestObject, TrendingTag.class);
    }

    @Override
    public List<TrendingTag> getTrendingTags(String firstTag, int limit) throws SteemCommunicationException {
        RequestWrapperDTO requestObject = new RequestWrapperDTO();
        requestObject.setApiMethod(RequestMethods.GET_TRENDING_TAGS);
        if (Golos4J.getInstance().getCurrentHardforkVersion() == Golos4J.HardForkVersion.HF_17)
            requestObject.setSteemApi(SteemApis.SOCIAL_NETWORK);
        else requestObject.setSteemApi(SteemApis.TAGS);
        String[] parameters = {firstTag, String.valueOf(limit)};
        requestObject.setAdditionalParameters(parameters);

        return communicationHandler.performRequest(requestObject, TrendingTag.class);
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
        RequestWrapperDTO requestObject = new RequestWrapperDTO();
        requestObject.setApiMethod(RequestMethods.LOOKUP_ACCOUNTS);
        requestObject.setSteemApi(SteemApis.DATABASE_API);
        String[] parameters = {pattern, String.valueOf(limit)};
        requestObject.setAdditionalParameters(parameters);

        return communicationHandler.performRequest(requestObject, String.class);
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
        RequestWrapperDTO requestObject = new RequestWrapperDTO();
        requestObject.setApiMethod(RequestMethods.GET_REWARD_FUND);
        requestObject.setSteemApi(SteemApis.DATABASE_API);
        Object[] parameters = {rewordFundType.name().toLowerCase()};
        requestObject.setAdditionalParameters(parameters);

        return communicationHandler.performRequest(requestObject, RewardFund.class).get(0);
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
        if (Golos4J.getInstance().getCurrentHardforkVersion() == Golos4J.HardForkVersion.HF_17)
            voteLimit = -1;
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
