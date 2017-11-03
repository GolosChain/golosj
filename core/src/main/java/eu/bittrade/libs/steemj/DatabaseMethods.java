package eu.bittrade.libs.steemj;

import java.text.ParseException;
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
import eu.bittrade.libs.steemj.base.models.DiscussionQuery;
import eu.bittrade.libs.steemj.base.models.ExtendedAccount;
import eu.bittrade.libs.steemj.base.models.ExtendedLimitOrder;
import eu.bittrade.libs.steemj.base.models.FeedHistory;
import eu.bittrade.libs.steemj.base.models.GlobalProperties;
import eu.bittrade.libs.steemj.base.models.LiquidityBalance;
import eu.bittrade.libs.steemj.base.models.OrderBook;
import eu.bittrade.libs.steemj.base.models.Permlink;
import eu.bittrade.libs.steemj.base.models.Price;
import eu.bittrade.libs.steemj.base.models.RewardFund;
import eu.bittrade.libs.steemj.base.models.ScheduledHardfork;
import eu.bittrade.libs.steemj.base.models.SignedBlockWithInfo;
import eu.bittrade.libs.steemj.base.models.SignedTransaction;
import eu.bittrade.libs.steemj.base.models.TrendingTag;
import eu.bittrade.libs.steemj.base.models.Vote;
import eu.bittrade.libs.steemj.base.models.VoteState;
import eu.bittrade.libs.steemj.base.models.Witness;
import eu.bittrade.libs.steemj.base.models.WitnessSchedule;
import eu.bittrade.libs.steemj.communication.BlockAppliedCallback;
import eu.bittrade.libs.steemj.enums.DiscussionSortType;
import eu.bittrade.libs.steemj.enums.RewardFundType;
import eu.bittrade.libs.steemj.exceptions.SteemCommunicationException;

/**
 * Created by yuri yurivladdurain@gmail.com
 */

@SuppressWarnings("unused")
public interface DatabaseMethods {
    /**
     * Get the current number of registered Steem accounts.
     *
     * @return The number of accounts.
     * @throws SteemCommunicationException <ul>
     *                                     <li>If the server was not able to answer the request in the
     *                                     given time (see
     *                                     {@link eu.bittrade.libs.steemj.configuration.SteemJConfig#setResponseTimeout(long)
     *                                     setResponseTimeout}).</li>
     *                                     <li>If there is a connection problem.</li>
     *                                     <li>If the SteemJ is unable to transform the JSON response
     *                                     into a Java object.</li>
     *                                     <li>If the Server returned an error object.</li>
     *                                     </ul>
     */
    @Nonnull
    Integer getAccountCount() throws SteemCommunicationException;

    /**
     * Get all operations performed by the specified account.
     *
     * @param accountName   The user name of the account.
     * @param lastIndex     The starting point.
     * @param backwardDepth The maximum number of entries.
     * @return A map containing the activities. The key is the id of the
     * activity.
     * @throws SteemCommunicationException <ul>
     *                                     <li>If the server was not able to answer the request in the
     *                                     given time (see
     *                                     {@link eu.bittrade.libs.steemj.configuration.SteemJConfig#setResponseTimeout(long)
     *                                     setResponseTimeout}).</li>
     *                                     <li>If there is a connection problem.</li>
     *                                     <li>If the SteemJ is unable to transform the JSON response
     *                                     into a Java object.</li>
     *                                     <li>If the Server returned an error object.</li>
     *                                     </ul>
     */
    @Nonnull
    Map<Integer, AppliedOperation> getAccountHistory(@Nonnull AccountName accountName, int lastIndex, int backwardDepth)
            throws SteemCommunicationException;

    /**
     * @param accountNames A list of accounts you want to request the details for.
     * @return A List of accounts found for the given account names.
     * @throws SteemCommunicationException <ul>
     *                                     <li>If the server was not able to answer the request in the
     *                                     given time (see
     *                                     {@link eu.bittrade.libs.steemj.configuration.SteemJConfig#setResponseTimeout(long)
     *                                     setResponseTimeout}).</li>
     *                                     <li>If there is a connection problem.</li>
     *                                     <li>If the SteemJ is unable to transform the JSON response
     *                                     into a Java object.</li>
     *                                     <li>If the Server returned an error object.</li>
     *                                     </ul>
     */
    @Nonnull
    List<ExtendedAccount> getAccounts(@Nonnull List<AccountName> accountNames) throws SteemCommunicationException;


    /**
     * Get a list of all votes done by a specific account.
     *
     * @param accountName The user name of the account.
     * @return A List of votes done by the specified account.
     * @throws SteemCommunicationException <ul>
     *                                     <li>If the server was not able to answer the request in the
     *                                     given time (see
     *                                     {@link eu.bittrade.libs.steemj.configuration.SteemJConfig#setResponseTimeout(long)
     *                                     setResponseTimeout}).</li>
     *                                     <li>If there is a connection problem.</li>
     *                                     <li>If the SteemJ is unable to transform the JSON response
     *                                     into a Java object.</li>
     *                                     <li>If the Server returned an error object.</li>
     *                                     </ul>
     */
    @Nonnull
    List<Vote> getAccountVotes(@Nonnull AccountName accountName) throws SteemCommunicationException;

    /**
     * Get a complete block by a given block number including all transactions
     * of this block.
     *
     * @param blockNumber The id of the block the header should be requested from.
     * @return A complete block.
     * @throws SteemCommunicationException <ul>
     *                                     <li>If the server was not able to answer the request in the
     *                                     given time (see
     *                                     {@link eu.bittrade.libs.steemj.configuration.SteemJConfig#setResponseTimeout(long)
     *                                     setResponseTimeout}).</li>
     *                                     <li>If there is a connection problem.</li>
     *                                     <li>If the SteemJ is unable to transform the JSON response
     *                                     into a Java object.</li>
     *                                     <li>If the Server returned an error object.</li>
     *                                     </ul>
     */
    @Nullable
    SignedBlockWithInfo getBlock(long blockNumber) throws SteemCommunicationException;

    /**
     * Get only the header of a block instead of the complete one.
     *
     * @param blockNumber The id of the block the header should be requested from.
     * @return The header of a block.
     * @throws SteemCommunicationException <ul>
     *                                     <li>If the server was not able to answer the request in the
     *                                     given time (see
     *                                     {@link eu.bittrade.libs.steemj.configuration.SteemJConfig#setResponseTimeout(long)
     *                                     setResponseTimeout}).</li>
     *                                     <li>If there is a connection problem.</li>
     *                                     <li>If the SteemJ is unable to transform the JSON response
     *                                     into a Java object.</li>
     *                                     <li>If the Server returned an error object.</li>
     *                                     </ul>
     */
    @Nullable
    BlockHeader getBlockHeader(long blockNumber) throws SteemCommunicationException;

    /**
     * Get the details of a specific post.
     *
     * @param author   The authors name.
     * @param permlink The permlink of the article.
     * @return The details of a specific post.
     * @throws SteemCommunicationException <ul>
     *                                     <li>If the server was not able to answer the request in the
     *                                     given time (see
     *                                     {@link eu.bittrade.libs.steemj.configuration.SteemJConfig#setResponseTimeout(long)
     *                                     setResponseTimeout}).</li>
     *                                     <li>If there is a connection problem.</li>
     *                                     <li>If the SteemJ is unable to transform the JSON response
     *                                     into a Java object.</li>
     *                                     <li>If the Server returned an error object.</li>
     *                                     </ul>
     */
    @Nullable
    Discussion getContent(@Nonnull AccountName author, @Nonnull Permlink permlink) throws SteemCommunicationException;


    /**
     * TODO: Check what this method is supposed to do. In a fist test it seems
     * to return the time since the current version is active.
     *
     * @return ???
     * @throws SteemCommunicationException <ul>
     *                                     <li>If the server was not able to answer the request in the
     *                                     given time (see
     *                                     {@link eu.bittrade.libs.steemj.configuration.SteemJConfig#setResponseTimeout(long)
     *                                     setResponseTimeout}).</li>
     *                                     <li>If there is a connection problem.</li>
     *                                     <li>If the SteemJ is unable to transform the JSON response
     *                                     into a Java object.</li>
     *                                     <li>If the Server returned an error object.</li>
     *                                     </ul>
     */
    @Nonnull
    ScheduledHardfork getNextScheduledHarfork() throws SteemCommunicationException;


    /**
     * Get the active votes for a given post of a given author.
     *
     * @param author   The authors name.
     * @param permlink The permlink of the article.
     * @return A list of votes for a specific article.
     * @throws SteemCommunicationException <ul>
     *                                     <li>If the server was not able to answer the request in the
     *                                     given time (see
     *                                     {@link eu.bittrade.libs.steemj.configuration.SteemJConfig#setResponseTimeout(long)
     *                                     setResponseTimeout}).</li>
     *                                     <li>If there is a connection problem.</li>
     *                                     <li>If the SteemJ is unable to transform the JSON response
     *                                     into a Java object.</li>
     *                                     <li>If the Server returned an error object.</li>
     *                                     </ul>
     */
    @Nonnull
    List<VoteState> getActiveVotes(@Nonnull AccountName author, @Nonnull Permlink permlink) throws SteemCommunicationException;

    /**
     * Get the account names of the active witnesses.
     *
     * @return A list of account names of the active witnesses.
     * @throws SteemCommunicationException <ul>
     *                                     <li>If the server was not able to answer the request in the
     *                                     given time (see
     *                                     {@link eu.bittrade.libs.steemj.configuration.SteemJConfig#setResponseTimeout(long)
     *                                     setResponseTimeout}).</li>
     *                                     <li>If there is a connection problem.</li>
     *                                     <li>If the SteemJ is unable to transform the JSON response
     *                                     into a Java object.</li>
     *                                     <li>If the Server returned an error object.</li>
     *                                     </ul>
     */
    @Nonnull
    List<String> getActiveWitnesses() throws SteemCommunicationException;


    /**
     * Get the chain properties.
     *
     * @return The chain properties.
     * @throws SteemCommunicationException <ul>
     *                                     <li>If the server was not able to answer the request in the
     *                                     given time (see
     *                                     {@link eu.bittrade.libs.steemj.configuration.SteemJConfig#setResponseTimeout(long)
     *                                     setResponseTimeout}).</li>
     *                                     <li>If there is a connection problem.</li>
     *                                     <li>If the SteemJ is unable to transform the JSON response
     *                                     into a Java object.</li>
     *                                     <li>If the Server returned an error object.</li>
     *                                     </ul>
     */
    @Nonnull
    ChainProperties getChainProperties() throws SteemCommunicationException;

    /**
     * Get the configuration.
     *
     * @return The steem configuration.
     * @throws SteemCommunicationException <ul>
     *                                     <li>If the server was not able to answer the request in the
     *                                     given time (see
     *                                     {@link eu.bittrade.libs.steemj.configuration.SteemJConfig#setResponseTimeout(long)
     *                                     setResponseTimeout}).</li>
     *                                     <li>If there is a connection problem.</li>
     *                                     <li>If the SteemJ is unable to transform the JSON response
     *                                     into a Java object.</li>
     *                                     <li>If the Server returned an error object.</li>
     *                                     </ul>
     */
    @Nonnull
    Config getConfig() throws SteemCommunicationException;

    /**
     * Get the replies of a specific post.
     *
     * @param author   The authors name.
     * @param permlink The permlink of the article.
     * @return A list of discussions or null if the post has no replies.
     * @throws SteemCommunicationException <ul>
     *                                     <li>If the server was not able to answer the request in the
     *                                     given time (see
     *                                     {@link eu.bittrade.libs.steemj.configuration.SteemJConfig#setResponseTimeout(long)
     *                                     setResponseTimeout}).</li>
     *                                     <li>If there is a connection problem.</li>
     *                                     <li>If the SteemJ is unable to transform the JSON response
     *                                     into a Java object.</li>
     *                                     <li>If the Server returned an error object.</li>
     *                                     </ul>
     */
    @Nonnull
    List<Discussion> getContentReplies(AccountName author, Permlink permlink) throws SteemCommunicationException;

    /**
     * TODO: Look up what this is used for and what it can return.
     *
     * @param account The account name.
     * @return Unknown
     * @throws SteemCommunicationException <ul>
     *                                     <li>If the server was not able to answer the request in the
     *                                     given time (see
     *                                     {@link eu.bittrade.libs.steemj.configuration.SteemJConfig#setResponseTimeout(long)
     *                                     setResponseTimeout}).</li>
     *                                     <li>If there is a connection problem.</li>
     *                                     <li>If the SteemJ is unable to transform the JSON response
     *                                     into a Java object.</li>
     *                                     <li>If the Server returned an error object.</li>
     *                                     </ul>
     */
    @Nonnull
    Object[] getConversionRequests(@Nonnull AccountName account) throws SteemCommunicationException;

    /**
     * Grab the current median conversion price of SBD / STEEM.
     *
     * @return The current median price.
     * @throws SteemCommunicationException <ul>
     *                                     <li>If the server was not able to answer the request in the
     *                                     given time (see
     *                                     {@link eu.bittrade.libs.steemj.configuration.SteemJConfig#setResponseTimeout(long)
     *                                     setResponseTimeout}).</li>
     *                                     <li>If there is a connection problem.</li>
     *                                     <li>If the SteemJ is unable to transform the JSON response
     *                                     into a Java object.</li>
     *                                     <li>If the Server returned an error object.</li>
     *                                     </ul>
     */
    @Nonnull
    Price getCurrentMedianHistoryPrice() throws SteemCommunicationException;


    /**
     * Get active discussions for a specified tag.
     *
     * @param discussionQuery A query defining specific search parameters.
     * @param sortBy          Choose the method used for sorting the results.
     * @return A list of discussions matching the given conditions.
     * @throws SteemCommunicationException <ul>
     *                                     <li>If the server was not able to answer the request in the
     *                                     given time (see
     *                                     {@link eu.bittrade.libs.steemj.configuration.SteemJConfig#setResponseTimeout(long)
     *                                     setResponseTimeout}).</li>
     *                                     <li>If there is a connection problem.</li>
     *                                     <li>If the SteemJ is unable to transform the JSON response
     *                                     into a Java object.</li>
     *                                     <li>If the Server returned an error object.</li>
     *                                     </ul>
     */
    @Nonnull
    List<Discussion> getDiscussionsBy(@Nonnull DiscussionQuery discussionQuery, @Nonnull DiscussionSortType sortBy)
            throws SteemCommunicationException;

    /**
     * Get a list of discussion for a given author.
     *
     * @param author   The authors name.
     * @param permlink The permlink of the article.
     * @param date     Only return articles before this date. (This field seems to be
     *                 ignored by the Steem api)
     * @param limit    The number of results you want to receive.
     * @return A list of discussions.
     * @throws SteemCommunicationException <ul>
     *                                     <li>If the server was not able to answer the request in the
     *                                     given time (see
     *                                     {@link eu.bittrade.libs.steemj.configuration.SteemJConfig#setResponseTimeout(long)
     *                                     setResponseTimeout}).</li>
     *                                     <li>If there is a connection problem.</li>
     *                                     <li>If the SteemJ is unable to transform the JSON response
     *                                     into a Java object.</li>
     *                                     <li>If the Server returned an error object.</li>
     *                                     </ul>
     */
    @Nonnull
    List<Discussion> getDiscussionsByAuthorBeforeDate(@Nonnull AccountName author, @Nonnull Permlink permlink, long date, int limit)
            throws SteemCommunicationException;

    /**
     * Get a list of discussion for a given author.
     *
     * @param author   The authors name.
     * @param permlink The permlink of the article.
     * @param date     Only return articles before this date. (This field seems to be
     *                 ignored by the Steem api)
     * @param limit    The number of results you want to receive.
     * @return A list of discussions.
     * @throws SteemCommunicationException <ul>
     *                                     <li>If the server was not able to answer the request in the
     *                                     given time (see
     *                                     {@link eu.bittrade.libs.steemj.configuration.SteemJConfig#setResponseTimeout(long)
     *                                     setResponseTimeout}).</li>
     *                                     <li>If there is a connection problem.</li>
     *                                     <li>If the SteemJ is unable to transform the JSON response
     *                                     into a Java object.</li>
     *                                     <li>If the Server returned an error object.</li>
     *                                     </ul>
     */
    @Nonnull
    List<Discussion> getDiscussionsByAuthorBeforeDate(@Nonnull AccountName author, @Nonnull Permlink permlink, String date, int limit)
            throws SteemCommunicationException, ParseException;

    /**
     * Get the global properties.
     *
     * @return The dynamic global properties.
     * @throws SteemCommunicationException <ul>
     *                                     <li>If the server was not able to answer the request in the
     *                                     given time (see
     *                                     {@link eu.bittrade.libs.steemj.configuration.SteemJConfig#setResponseTimeout(long)
     *                                     setResponseTimeout}).</li>
     *                                     <li>If there is a connection problem.</li>
     *                                     <li>If the SteemJ is unable to transform the JSON response
     *                                     into a Java object.</li>
     *                                     <li>If the Server returned an error object.</li>
     *                                     </ul>
     */
    @Nonnull
    GlobalProperties getDynamicGlobalProperties() throws SteemCommunicationException;

    /**
     * Get the current price and a list of history prices combined in one
     * object.
     *
     * @return The conversion history of SBD / STEEM.
     * @throws SteemCommunicationException <ul>
     *                                     <li>If the server was not able to answer the request in the
     *                                     given time (see
     *                                     {@link eu.bittrade.libs.steemj.configuration.SteemJConfig#setResponseTimeout(long)
     *                                     setResponseTimeout}).</li>
     *                                     <li>If there is a connection problem.</li>
     *                                     <li>If the SteemJ is unable to transform the JSON response
     *                                     into a Java object.</li>
     *                                     <li>If the Server returned an error object.</li>
     *                                     </ul>
     */
    @Nonnull
    FeedHistory getFeedHistory() throws SteemCommunicationException;

    /**
     * Get the hardfork version the node you are connected to is using.
     *
     * @return The hardfork version that the connected node is running on.
     * @throws SteemCommunicationException <ul>
     *                                     <li>If the server was not able to answer the request in the
     *                                     given time (see
     *                                     {@link eu.bittrade.libs.steemj.configuration.SteemJConfig#setResponseTimeout(long)
     *                                     setResponseTimeout}).</li>
     *                                     <li>If there is a connection problem.</li>
     *                                     <li>If the SteemJ is unable to transform the JSON response
     *                                     into a Java object.</li>
     *                                     <li>If the Server returned an error object.</li>
     *                                     </ul>
     */
    @Nonnull
    String getHardforkVersion() throws SteemCommunicationException;


    /**
     * Search for users under the use of their public key(s).
     *
     * @param publicKeys An array containing one or more public keys.
     * @return A list of arrays containing the matching account names.
     * @throws SteemCommunicationException <ul>
     *                                     <li>If the server was not able to answer the request in the
     *                                     given time (see
     *                                     {@link eu.bittrade.libs.steemj.configuration.SteemJConfig#setResponseTimeout(long)
     *                                     setResponseTimeout}).</li>
     *                                     <li>If there is a connection problem.</li>
     *                                     <li>If the SteemJ is unable to transform the JSON response
     *                                     into a Java object.</li>
     *                                     <li>If the Server returned an error object.</li>
     *                                     </ul>
     */
    @Nonnull
    List<String[]> getKeyReferences(@Nonnull String[] publicKeys) throws SteemCommunicationException;

    /**
     * Get the liquidity queue for a specified account.
     *
     * @param accoutName The name of the account you want to request the queue entries
     *                   for.
     * @param limit      Number of results.
     * @return A list of liquidity queue entries.
     * @throws SteemCommunicationException <ul>
     *                                     <li>If the server was not able to answer the request in the
     *                                     given time (see
     *                                     {@link eu.bittrade.libs.steemj.configuration.SteemJConfig#setResponseTimeout(long)
     *                                     setResponseTimeout}).</li>
     *                                     <li>If there is a connection problem.</li>
     *                                     <li>If the SteemJ is unable to transform the JSON response
     *                                     into a Java object.</li>
     *                                     <li>If the Server returned an error object.</li>
     *                                     </ul>
     */
    @Nonnull
    List<LiquidityBalance> getLiquidityQueue(@Nonnull AccountName accoutName, int limit) throws SteemCommunicationException;

    /**
     * Get the current miner queue.
     *
     * @return A list of account names that are in the mining queue.
     * @throws SteemCommunicationException <ul>
     *                                     <li>If the server was not able to answer the request in the
     *                                     given time (see
     *                                     {@link eu.bittrade.libs.steemj.configuration.SteemJConfig#setResponseTimeout(long)
     *                                     setResponseTimeout}).</li>
     *                                     <li>If there is a connection problem.</li>
     *                                     <li>If the SteemJ is unable to transform the JSON response
     *                                     into a Java object.</li>
     *                                     <li>If the Server returned an error object.</li>
     *                                     </ul>
     */
    @Nonnull
    List<String> getMinerQueue() throws SteemCommunicationException;


    /**
     * If specified user name has orders open on the internal STEEM market it
     * will return them.
     *
     * @param accountName The name of the account.
     * @return A list of open orders for this account.
     * @throws SteemCommunicationException <ul>
     *                                     <li>If the server was not able to answer the request in the
     *                                     given time (see
     *                                     {@link eu.bittrade.libs.steemj.configuration.SteemJConfig#setResponseTimeout(long)
     *                                     setResponseTimeout}).</li>
     *                                     <li>If there is a connection problem.</li>
     *                                     <li>If the SteemJ is unable to transform the JSON response
     *                                     into a Java object.</li>
     *                                     <li>If the Server returned an error object.</li>
     *                                     </ul>
     */
    @Nonnull
    List<ExtendedLimitOrder> getOpenOrders(AccountName accountName) throws SteemCommunicationException;

    /**
     * Returns a list of orders on the internal steem market.
     *
     * @param limit The maximum number of results for each category (asks / bids).
     * @return A list of orders on the internal steem market.
     * @throws SteemCommunicationException <ul>
     *                                     <li>If the server was not able to answer the request in the
     *                                     given time (see
     *                                     {@link eu.bittrade.libs.steemj.configuration.SteemJConfig#setResponseTimeout(long)
     *                                     setResponseTimeout}).</li>
     *                                     <li>If there is a connection problem.</li>
     *                                     <li>If the SteemJ is unable to transform the JSON response
     *                                     into a Java object.</li>
     *                                     <li>If the Server returned an error object.</li>
     *                                     </ul>
     */
    @Nonnull
    OrderBook getOrderBookUsingDatabaseApi(int limit) throws SteemCommunicationException;

    /**
     * Get a list of all performed operations for a given block number.
     *
     * @param blockNumber The block number.
     * @param onlyVirtual Define if only virtual operations should be returned or not.
     * @return A list of all performed operations for a given block number.
     * @throws SteemCommunicationException <ul>
     *                                     <li>If the server was not able to answer the request in the
     *                                     given time (see
     *                                     {@link eu.bittrade.libs.steemj.configuration.SteemJConfig#setResponseTimeout(long)
     *                                     setResponseTimeout}).</li>
     *                                     <li>If there is a connection problem.</li>
     *                                     <li>If the SteemJ is unable to transform the JSON response
     *                                     into a Java object.</li>
     *                                     <li>If the Server returned an error object.</li>
     *                                     </ul>
     */
    @Nonnull
    List<AppliedOperation> getOpsInBlock(int blockNumber, boolean onlyVirtual)
            throws SteemCommunicationException;

    // TODO implement this!
    @Nullable
    List<String[]> getPotentialSignatures() throws SteemCommunicationException;

    /**
     * /** Get a list of Content starting from the given post of the given user.
     * The list will be sorted by the Date of the last update.
     *
     * @param username The name of the user.
     * @param permlink The permlink of an article.
     * @param limit    Number of results.
     * @return A list of Content objects.
     * @throws SteemCommunicationException <ul>
     *                                     <li>If the server was not able to answer the request in the
     *                                     given time (see
     *                                     {@link eu.bittrade.libs.steemj.configuration.SteemJConfig#setResponseTimeout(long)
     *                                     setResponseTimeout}).</li>
     *                                     <li>If there is a connection problem.</li>
     *                                     <li>If the SteemJ is unable to transform the JSON response
     *                                     into a Java object.</li>
     *                                     <li>If the Server returned an error object.</li>
     *                                     </ul>
     */
    @Nonnull
    List<Discussion> getRepliesByLastUpdate(@Nonnull AccountName username, @Nonnull Permlink permlink, int limit)
            throws SteemCommunicationException;

    /**
     * Get detailed information of a specific reward fund.
     *
     * @param rewordFundType One of the {@link eu.bittrade.libs.steemj.enums.RewardFundType
     *                       RewardFundType}s.
     * @return A refund object containing detailed information about the
     * requested reward fund.
     * @throws SteemCommunicationException <ul>
     *                                     <li>If the server was not able to answer the request in the
     *                                     given time (see
     *                                     {@link eu.bittrade.libs.steemj.configuration.SteemJConfig#setResponseTimeout(long)
     *                                     setResponseTimeout}).</li>
     *                                     <li>If there is a connection problem.</li>
     *                                     <li>If the SteemJ is unable to transform the JSON response
     *                                     into a Java object.</li>
     *                                     <li>If the Server returned an error object.</li>
     *                                     </ul>
     */
    @Nonnull
    RewardFund getRewardFund(RewardFundType rewordFundType) throws SteemCommunicationException;

    /**
     * Use the Steem API to receive the HEX representation of a signed
     * transaction.
     *
     * @param signedTransaction The signed Transaction object you want to receive the HEX
     *                          representation for.
     * @return The HEX representation.
     * @throws SteemCommunicationException <ul>
     *                                     <li>If the server was not able to answer the request in the
     *                                     given time (see
     *                                     {@link eu.bittrade.libs.steemj.configuration.SteemJConfig#setResponseTimeout(long)
     *                                     setResponseTimeout}).</li>
     *                                     <li>If there is a connection problem.</li>
     *                                     <li>If the SteemJ is unable to transform the JSON response
     *                                     into a Java object.</li>
     *                                     <li>If the Server returned an error object.</li>
     *                                     </ul>
     */
    @Nonnull
    String getTransactionHex(@Nonnull SignedTransaction signedTransaction) throws SteemCommunicationException;

    /**
     * Returns detailed values for tags that match the given conditions.
     *
     * @param firstTag Start the list after this category. An empty String will
     *                 result in starting from the top.
     * @param limit    The number of results.
     * @return A list of tags.
     * @throws SteemCommunicationException <ul>
     *                                     <li>If the server was not able to answer the request in the
     *                                     given time (see
     *                                     {@link eu.bittrade.libs.steemj.configuration.SteemJConfig#setResponseTimeout(long)
     *                                     setResponseTimeout}).</li>
     *                                     <li>If there is a connection problem.</li>
     *                                     <li>If the SteemJ is unable to transform the JSON response
     *                                     into a Java object.</li>
     *                                     <li>If the Server returned an error object.</li>
     *                                     </ul>
     */
    @Nonnull
    List<TrendingTag> getTrendingTags(@Nonnull String firstTag, int limit) throws SteemCommunicationException;

    /**
     * Get the witness information for a given witness account name.
     *
     * @param witnessName The witness name.
     * @return A list of witnesses.
     * @throws SteemCommunicationException <ul>
     *                                     <li>If the server was not able to answer the request in the
     *                                     given time (see
     *                                     {@link eu.bittrade.libs.steemj.configuration.SteemJConfig#setResponseTimeout(long)
     *                                     setResponseTimeout}).</li>
     *                                     <li>If there is a connection problem.</li>
     *                                     <li>If the SteemJ is unable to transform the JSON response
     *                                     into a Java object.</li>
     *                                     <li>If the Server returned an error object.</li>
     *                                     </ul>
     */
    @Nonnull
    Witness getWitnessByAccount(AccountName witnessName) throws SteemCommunicationException;

    /**
     * Get a list of witnesses sorted by the amount of votes. The list begins
     * with the given account name and contains the next witnesses with less
     * votes than given one.
     *
     * @param witnessName The witness name to start from.
     * @param limit       The number of results.
     * @return A list of witnesses.
     * @throws SteemCommunicationException <ul>
     *                                     <li>If the server was not able to answer the request in the
     *                                     given time (see
     *                                     {@link eu.bittrade.libs.steemj.configuration.SteemJConfig#setResponseTimeout(long)
     *                                     setResponseTimeout}).</li>
     *                                     <li>If there is a connection problem.</li>
     *                                     <li>If the SteemJ is unable to transform the JSON response
     *                                     into a Java object.</li>
     *                                     <li>If the Server returned an error object.</li>
     *                                     </ul>
     */
    @Nonnull
    List<Witness> getWitnessByVote(AccountName witnessName, int limit) throws SteemCommunicationException;


    /**
     * Get the current number of active witnesses.
     *
     * @return The number of witnesses.
     * @throws SteemCommunicationException <ul>
     *                                     <li>If the server was not able to answer the request in the
     *                                     given time (see
     *                                     {@link eu.bittrade.libs.steemj.configuration.SteemJConfig#setResponseTimeout(long)
     *                                     setResponseTimeout}).</li>
     *                                     <li>If there is a connection problem.</li>
     *                                     <li>If the SteemJ is unable to transform the JSON response
     *                                     into a Java object.</li>
     *                                     <li>If the Server returned an error object.</li>
     *                                     </ul>
     */
    int getWitnessCount() throws SteemCommunicationException;

    /**
     * Get all witnesses.
     *
     * @return A list of witnesses.
     * @throws SteemCommunicationException <ul>
     *                                     <li>If the server was not able to answer the request in the
     *                                     given time (see
     *                                     {@link eu.bittrade.libs.steemj.configuration.SteemJConfig#setResponseTimeout(long)
     *                                     setResponseTimeout}).</li>
     *                                     <li>If there is a connection problem.</li>
     *                                     <li>If the SteemJ is unable to transform the JSON response
     *                                     into a Java object.</li>
     *                                     <li>If the Server returned an error object.</li>
     *                                     </ul>
     */
    @Nonnull
    List<Witness> getWitnesses() throws SteemCommunicationException;

    /**
     * Get the witness schedule.
     *
     * @return The witness schedule.
     * @throws SteemCommunicationException <ul>
     *                                     <li>If the server was not able to answer the request in the
     *                                     given time (see
     *                                     {@link eu.bittrade.libs.steemj.configuration.SteemJConfig#setResponseTimeout(long)
     *                                     setResponseTimeout}).</li>
     *                                     <li>If there is a connection problem.</li>
     *                                     <li>If the SteemJ is unable to transform the JSON response
     *                                     into a Java object.</li>
     *                                     <li>If the Server returned an error object.</li>
     *                                     </ul>
     */
    @Nonnull
    WitnessSchedule getWitnessSchedule() throws SteemCommunicationException;

    /**
     * Search for accounts.
     *
     * @param pattern The lower case pattern you want to search for.
     * @param limit   The maximum number of account names.
     * @return A list of matching account names.
     * @throws SteemCommunicationException <ul>
     *                                     <li>If the server was not able to answer the request in the
     *                                     given time (see
     *                                     {@link eu.bittrade.libs.steemj.configuration.SteemJConfig#setResponseTimeout(long)
     *                                     setResponseTimeout}).</li>
     *                                     <li>If there is a connection problem.</li>
     *                                     <li>If the SteemJ is unable to transform the JSON response
     *                                     into a Java object.</li>
     *                                     <li>If the Server returned an error object.</li>
     *                                     </ul>
     */
    @Nonnull
    List<String> lookupAccounts(@Nonnull String pattern, int limit) throws SteemCommunicationException;

    /**
     * Search for witness accounts.
     *
     * @param pattern The lower case pattern you want to search for.
     * @param limit   The maximum number of account names.
     * @return A list of matching account names.
     * @throws SteemCommunicationException <ul>
     *                                     <li>If the server was not able to answer the request in the
     *                                     given time (see
     *                                     {@link eu.bittrade.libs.steemj.configuration.SteemJConfig#setResponseTimeout(long)
     *                                     setResponseTimeout}).</li>
     *                                     <li>If there is a connection problem.</li>
     *                                     <li>If the SteemJ is unable to transform the JSON response
     *                                     into a Java object.</li>
     *                                     <li>If the Server returned an error object.</li>
     *                                     </ul>
     */
    @Nonnull
    List<String> lookupWitnessAccounts(String pattern, int limit) throws SteemCommunicationException;

    /**
     * Use the Steem API to verify the required authorities for this
     * transaction.
     *
     * @param signedTransaction A {@link SignedTransaction} transaction which has been signed.
     * @return <code>true</code> if the given transaction has been signed
     * correctly, otherwise an Exception will be thrown.
     * @throws SteemCommunicationException <ul>
     *                                     <li>If the server was not able to answer the request in the
     *                                     given time (see
     *                                     {@link eu.bittrade.libs.steemj.configuration.SteemJConfig#setResponseTimeout(long)
     *                                     setResponseTimeout}).</li>
     *                                     <li>If there is a connection problem.</li>
     *                                     <li>If the SteemJ is unable to transform the JSON response
     *                                     into a Java object.</li>
     *                                     <li>If the Server returned an error object.</li>
     *                                     </ul>
     */
    @Nonnull
    Boolean verifyAuthority(@Nonnull SignedTransaction signedTransaction) throws SteemCommunicationException;

    /**
     * Use this method to register a callback method that is called whenever a
     * new block has been applied.
     * <p>
     * <p>
     * <b>Notice:</b>
     * <p>
     * That there can only be one active Callback. If you call this method
     * multiple times with different callback methods, only the last one will be
     * called.
     * <p>
     * Beside that there is currently no way to cancel a subscription. Once
     * you've registered a callback it will be called until the connection has
     * been closed.
     * </p>
     *
     * @param blockAppliedCallback A class implementing the
     *                             {@link eu.bittrade.libs.steemj.communication.BlockAppliedCallback
     *                             BlockAppliedCallback}.
     * @throws SteemCommunicationException <ul>
     *                                     <li>If the server was not able to answer the request in the
     *                                     given time (see
     *                                     {@link eu.bittrade.libs.steemj.configuration.SteemJConfig#setResponseTimeout(long)
     *                                     setResponseTimeout}).</li>
     *                                     <li>If there is a connection problem.</li>
     *                                     <li>If the SteemJ is unable to transform the JSON response
     *                                     into a Java object.</li>
     *                                     <li>If the Server returned an error object.</li>
     *                                     </ul>
     */
    void setBlockAppliedCallback(@Nonnull BlockAppliedCallback blockAppliedCallback) throws SteemCommunicationException;


    String getAccountAvatar(AccountName name) throws SteemCommunicationException;
}
