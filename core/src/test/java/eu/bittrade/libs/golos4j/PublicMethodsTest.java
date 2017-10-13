package eu.bittrade.libs.golos4j;


import eu.bittrade.libs.steemj.Golos4J;
import eu.bittrade.libs.steemj.IntegrationTest;
import eu.bittrade.libs.steemj.apis.follow.enums.FollowType;
import eu.bittrade.libs.steemj.apis.follow.model.*;
import eu.bittrade.libs.steemj.base.models.*;
import eu.bittrade.libs.steemj.base.models.operations.AccountCreateOperation;
import eu.bittrade.libs.steemj.base.models.operations.Operation;
import eu.bittrade.libs.steemj.base.models.operations.VoteOperation;
import eu.bittrade.libs.steemj.enums.AssetSymbolType;
import eu.bittrade.libs.steemj.enums.DiscussionSortType;
import eu.bittrade.libs.steemj.exceptions.SteemCommunicationException;
import eu.bittrade.libs.steemj.exceptions.SteemResponseError;
import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.hamcrest.MatcherAssert;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigInteger;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.*;

public class PublicMethodsTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(PublicMethodsTest.class);
    private static final AccountName ACCOUNT = new AccountName("lenutsa");
    private static final AccountName WITNESS_ACCOUNT = new AccountName("itsmine-78");
    private static final Permlink PERMLINK = new Permlink("kriptopirozhkovo-khardfokovyi-post");
    private Golos4J golos4J;

    @Before
    public void setup() {
        golos4J = Golos4J.getInstance();
    }

    @Test
    public void testGetBlockHeader() throws Exception {
        final BlockHeader blockHeader = golos4J.getDatabaseMethods().getBlockHeader(8000L);

        assertThat(blockHeader.getTimestamp().getDateTime(), equalTo("2016-10-18T17:45:09"));
        assertThat(blockHeader.getWitness(), equalTo("primus"));
    }

    @Category({IntegrationTest.class})
    @Test
    public void testGetOpsInBlock() throws Exception {
        final List<AppliedOperation> appliedOperationsOnlyVirtual = golos4J.getDatabaseMethods().getOpsInBlock(30003, false);

        assertThat(appliedOperationsOnlyVirtual.size(), equalTo(2));
        assertThat(appliedOperationsOnlyVirtual.get(0).getOpInTrx(), equalTo(0));
        assertThat(appliedOperationsOnlyVirtual.get(0).getTrxInBlock(), equalTo(0));
        assertThat(appliedOperationsOnlyVirtual.get(0).getVirtualOp(), equalTo(0L));
        MatcherAssert.assertThat(appliedOperationsOnlyVirtual.get(0).getOp(), instanceOf(VoteOperation.class));
    }

    @Category({IntegrationTest.class})
    @Test
    public void testActiveVotes() throws Exception {
        final List<Vote> votes = golos4J.getDatabaseMethods().getAccountVotes(ACCOUNT);
        final List<VoteState> activeVotesForArticle = golos4J.getDatabaseMethods().getActiveVotes(ACCOUNT, PERMLINK);

        assertNotNull("expect votes", votes);
        assertThat("expect account has votes", votes.size(), greaterThan(0));

        boolean foundSelfVote = false;

        for (final VoteState vote : activeVotesForArticle) {
            if (ACCOUNT.equals(vote.getVoter())) {
                foundSelfVote = true;
                break;
            }
        }

        assertTrue("expect self vote for article of account", foundSelfVote);
    }

    @Test
    public void testGetConfig() throws Exception {
        final Config config = golos4J.getDatabaseMethods().getConfig();
        final boolean isTestNet = config.getIsTestNet();
        final String steemitNullAccount = config.getSteemitNullAccount();
        final String initMinerName = config.getSteemitInitMinerName();

        assertEquals("expect main net", false, isTestNet);
        assertEquals("expect the null account to be null", "null", steemitNullAccount);
        assertEquals("expect the init miner name to be initminer", "cyberfounder", initMinerName);
    }

    @Test
    public void testCurrentMedianHistoryPrice() throws Exception {
        final Asset base = golos4J.getDatabaseMethods().getCurrentMedianHistoryPrice().getBase();
        final Asset quote = golos4J.getDatabaseMethods().getCurrentMedianHistoryPrice().getQuote();

        assertThat("expect current median price greater than zero", base.getAmount(), greaterThan(0.00));
        Assert.assertEquals("expect current median price symbol", AssetSymbolType.GBG, base.getSymbol());
        assertThat("expect current median price greater than zero", quote.getAmount(), greaterThan(0.00));
        Assert.assertEquals("expect current median price symbol", AssetSymbolType.GOLOS, quote.getSymbol());
    }

    @Test
    public void testGetActiveWitnesses() throws Exception {
        final List<String> activeWitnesses = golos4J.getDatabaseMethods().getActiveWitnesses();

        // The active witness changes from time to time, so we just check if
        // something is returned.
        assertThat(activeWitnesses.size(), greaterThan(0));
        assertThat(activeWitnesses.get(25), not(isEmptyOrNullString()));
    }

    @Test
    public void testGetApiByName() throws Exception {
        golos4J.getLoginMethods().updateApiAllApi();

    }

    @Test
    public void testGetChainProperties() throws Exception {
        final ChainProperties properties = golos4J.getDatabaseMethods().getChainProperties();

        assertNotNull("expect properties", properties);
        assertThat("expect sbd interest rate", properties.getSdbInterestRate(), greaterThanOrEqualTo(0));
    }

    @Test
    public void testGetContent() throws Exception {
        final Discussion discussion = golos4J.getDatabaseMethods().getContent(ACCOUNT, PERMLINK);

        assertNotNull("expect discussion", discussion);
        assertEquals("expect correct author", ACCOUNT, discussion.getAuthor());
    }

    @Test
    public void testGetApiCount() throws Exception {
        assertThat("expect the number of accounts greater than 122908", golos4J.getDatabaseMethods().getAccountCount(), greaterThan(81754));
    }

    @Test
    public void testAccountHistory() throws Exception {
        try {
            final Map<Integer, AppliedOperation> accountHistorySetOne = golos4J.
                    getDatabaseMethods()
                    .getAccountHistory(ACCOUNT, 1000, 1000);
            Operation firstOperation = accountHistorySetOne.get(0).getOp();
            assertTrue("the first operation for each account is the 'account_create_operation'",
                    firstOperation instanceof AccountCreateOperation);
        } catch (SteemResponseError e) {
            System.err.println(e.getError());
        }
    }

    @Test
    public void testAccounts() throws Exception {
        List<ExtendedAccount> accounts = golos4J.getDatabaseMethods().getAccounts(Collections.singletonList(new AccountName("lenutsa")));
        assertEquals(1, accounts.size());

    }

    @Test
    public void testVoits() throws Exception {
        final List<Vote> votes = golos4J.getDatabaseMethods().getAccountVotes(ACCOUNT);
        assertThat(votes, new BaseMatcher<List<Vote>>() {
            @Override
            public boolean matches(Object o) {
                return votes.size() > 0;
            }

            @Override
            public void describeTo(Description description) {

            }
        });
    }


    @Test
    public void testOperationParsing() throws SteemCommunicationException {
        SignedBlockWithInfo blockContainingVoteOperation = golos4J.getDatabaseMethods().getBlock(30003);
        Operation voteOperation = blockContainingVoteOperation.getTransactions().get(0).getOperations()
                .get(0);

        assertThat(voteOperation, instanceOf(VoteOperation.class));
        assertThat(((VoteOperation) voteOperation).getAuthor().getName(), equalTo("sashko"));
        assertThat(((VoteOperation) voteOperation).getVoter().getName(), equalTo("t3ran13"));

        assertThat(blockContainingVoteOperation.getTimestamp().getDateTime(), equalTo("2016-10-19T12:13:27"));
        assertThat(blockContainingVoteOperation.getWitness(), equalTo("testz"));

    }

    @Test
    public void testGetNextScheduledHarfork() throws Exception {
        final ScheduledHardfork hardforkSchedule = golos4J.getDatabaseMethods().getNextScheduledHarfork();

        assertTrue(hardforkSchedule.getHardforkVersion().toString().matches("[0-9\\.]+"));
        assertTrue(hardforkSchedule.getLiveTime().getDateTime().matches("[0-9\\-:T]+"));
    }

    @Test
    public void testGetContentReplies() throws Exception {
        final List<Discussion> replies = golos4J.getDatabaseMethods().getContentReplies(ACCOUNT, PERMLINK);

        assertNotNull("expect replies", replies);
        assertThat("expect replies greater than zero", replies.size(), greaterThan(0));
    }

    @Test
    public void testGetDiscussionBy() throws Exception {

        final DiscussionSortType[] sortTypes = new DiscussionSortType[]
                {
                        DiscussionSortType.GET_DISCUSSIONS_BY_TRENDING,
                        DiscussionSortType.GET_DISCUSSIONS_BY_CREATED,
                        DiscussionSortType.GET_DISCUSSIONS_BY_ACTIVE,
                        DiscussionSortType.GET_DISCUSSIONS_BY_CASHOUT,
                        DiscussionSortType.GET_DISCUSSIONS_BY_VOTES,
                        DiscussionSortType.GET_DISCUSSIONS_BY_CHILDREN,
                        DiscussionSortType.GET_DISCUSSIONS_BY_HOT,
                        DiscussionSortType.GET_DISCUSSIONS_BY_CASHOUT,
                        DiscussionSortType.GET_DISCUSSIONS_BY_TRENDING30
                };

        DiscussionQuery discussionQuery = new DiscussionQuery();
        discussionQuery.setLimit(1);
        discussionQuery.setTruncateBody(100);
        for (final DiscussionSortType type : sortTypes) {
            final List<Discussion> discussions = golos4J.getDatabaseMethods().getDiscussionsBy(discussionQuery, type);
            assertNotNull("expect discussions", discussions);
            assertThat("expect discussions in " + type + " greater than zero", discussions.size(),
                    greaterThanOrEqualTo(0));
        }
    }

    @Test
    public void testGetDiscussionsByAuthorBeforeDate() throws Exception {
        final List<Discussion> repliesByLastUpdate = golos4J.getDatabaseMethods().getDiscussionsByAuthorBeforeDate(ACCOUNT, PERMLINK,
                "2017-10-10T14:08:51", 8);

        assertEquals("expect that 8 results are returned", repliesByLastUpdate.size(), 8);
        assertEquals("expect " + ACCOUNT + " to be the first returned author",
                repliesByLastUpdate.get(0).getAuthor(), ACCOUNT);
        assertEquals("expect " + PERMLINK + " to be the first returned permlink", PERMLINK,
                repliesByLastUpdate.get(0).getPermlink());
    }

    @Test
    public void testGetDynamicGlobalProperties() throws Exception {
        final GlobalProperties properties = golos4J.getDatabaseMethods().getDynamicGlobalProperties();

        assertNotNull("expect properties", properties);
        assertThat("expect head block number", properties.getHeadBlockNumber(), greaterThan(6000000L));
        assertTrue(properties.getHeadBlockId().toString().matches("[0-9a-f]{40}"));
        assertThat(properties.getHeadBlockId().getNumberFromHash(), greaterThan(123));
        assertThat(properties.getTotalPow(), greaterThan(new BigInteger("123")));
    }

    @Test
    public void testGetLiquidityQueue() throws Exception {
        final List<LiquidityBalance> repliesByLastUpdate = golos4J.getDatabaseMethods().getLiquidityQueue(WITNESS_ACCOUNT, 5);

        assertEquals("expect that 5 results are returned", repliesByLastUpdate.size(), 5);
        assertEquals("expect " + WITNESS_ACCOUNT + " to be the first returned account", WITNESS_ACCOUNT,
                repliesByLastUpdate.get(0).getAccount());
    }

    @Test
    public void testGetRepliesByLastUpdate() throws Exception {
        final List<Discussion> repliesByLastUpdate = golos4J.getDatabaseMethods().getRepliesByLastUpdate(ACCOUNT, PERMLINK, 9);

        assertEquals("expect that 9 results are returned", repliesByLastUpdate.size(), 9);
        assertEquals("expect " + ACCOUNT + " to be the first returned author", ACCOUNT,
                repliesByLastUpdate.get(0).getAuthor());
    }

    @Test
    public void testGetWitnessByAccount() throws Exception {
        final Witness activeWitnessesByVote = golos4J.getDatabaseMethods().getWitnessByAccount(WITNESS_ACCOUNT);

        assertEquals("expect " + WITNESS_ACCOUNT + " to be the owner of the returned witness account", WITNESS_ACCOUNT,
                activeWitnessesByVote.getOwner());
    }

    @Test
    public void testGetWitnessesByVote() throws Exception {
        final List<Witness> activeWitnessesByVote = golos4J.getDatabaseMethods().getWitnessByVote(new AccountName("pfunk"), 10);

        assertEquals("expect that 10 results are returned", activeWitnessesByVote.size(), 10);
        assertEquals("expect pfunk to be the first returned witness", "pfunk",
                activeWitnessesByVote.get(0).getOwner().getName());
    }

    @Test
    public void testHardforkVersion() throws Exception {
        final String hardforkVersion = golos4J.getDatabaseMethods().getHardforkVersion();

        assertNotNull("Expect hardfork version to be present.", hardforkVersion);
        assertTrue(hardforkVersion.matches("[0-9]+[\\.]{1}[0-9]{2}[\\.]{1}[0-9]+"));
    }

    @Test
    public void testInvalidAccountVotes() throws Exception {
        // Force an error response:
        try {
            golos4J.getDatabaseMethods().getAccountVotes(new AccountName("dferyery4t"));
        } catch (final SteemResponseError steemResponseError) {
            // success
        } catch (final Exception e) {
            LOGGER.error("An unexpected Exception occured.", e);
            fail(e.toString());
        }
    }

    @Test
    public void testLogin() throws Exception {
        final boolean success = golos4J.getLoginMethods().login(new AccountName("gilligan"), "s.s.minnow");

        assertTrue("expect login to always return success: true", success);
    }

    @Test
    public void testLookupAccount() throws Exception {
        final List<String> accounts = golos4J.getDatabaseMethods().lookupAccounts(ACCOUNT.getName(), 10);

        assertNotNull("expect accounts", accounts);
        assertThat("expect at least one account", accounts.size(), greaterThan(0));
    }

    @Test
    public void testLookupWitnessAccount() throws Exception {
        final List<String> accounts = golos4J.getDatabaseMethods().lookupWitnessAccounts("phenom", 10);

        assertNotNull("expect accounts", accounts);
        assertThat("expect at least one account", accounts.size(), greaterThan(0));
    }

    @Test
    public void testMinerQueue() throws Exception {
        final List<String> minerQueue = golos4J.getDatabaseMethods().getMinerQueue();

        assertThat("expect the number of miners greater than 0", minerQueue.size(), greaterThan(15));
    }

    @Test
    public void testTrendingTags() throws Exception {
        final List<TrendingTag> trendingTags = golos4J.getDatabaseMethods().getTrendingTags(null, 10);

        assertNotNull("expect trending tags", trendingTags);
        assertThat("expect trending tags size > 0", trendingTags.size(), greaterThan(0));
    }

    @Test
    public void testVersion() throws Exception {
        final SteemVersionInfo version = golos4J.getLoginMethods().getVersion();

        assertNotEquals("expect non-empty blockchain version", "", version.getBlockchainVersion());
        assertNotEquals("expect non-empty fc revision", "", version.getFcRevision());
        assertNotEquals("expect non-empty steem revision", "", version.getSteemRevision());
    }

    @Category({IntegrationTest.class})
    @Test
    public void testWitnessCount() throws Exception {
        final int witnessCount = golos4J.getDatabaseMethods().getWitnessCount();

        assertThat("expect the number of witnesses greater than 13071", witnessCount, greaterThan(1422));
    }

    @Test
    public void testWitnessSchedule() throws Exception {
        final WitnessSchedule witnessSchedule = golos4J.getDatabaseMethods().getWitnessSchedule();

        assertNotNull("expect hardfork version", witnessSchedule);
        assertThat(witnessSchedule.getTop19Weight(), equalTo((short) 1));
    }

    @Test
    public void testGetFollowers() throws Exception {
        final List<FollowApiObject> followers = golos4J.getFollowApiMethods().getFollowers(ACCOUNT,
                ACCOUNT, FollowType.BLOG, (short) 100);

        assertThat(followers.size(), equalTo(100));
        MatcherAssert.assertThat(followers.get(0).getFollower(), equalTo(new AccountName("tarimta")));
    }

    @Test
    public void testGetFollowCount() throws Exception {
        final FollowCountApiObject followCount = golos4J.getFollowApiMethods().getFollowCount(ACCOUNT);

        assertThat(followCount.getFollowerCount(), greaterThan(200));
        assertThat(followCount.getFollowingCount(), greaterThan(100));
    }

    @Test
    public void testGetFeedEntries() throws Exception {
        final List<FeedEntry> feedEntries = golos4J.getFollowApiMethods().getFeedEntries(ACCOUNT, 0, (short) 100);

        assertThat(feedEntries.size(), equalTo(100));
        assertTrue(feedEntries.get(0).getPermlink().getLink().matches("[a-z0-9\\-]+"));
    }

    @Test
    public void testGetFeed() throws Exception {
        final List<CommentFeedEntry> feed = golos4J.getFollowApiMethods().getFeed(ACCOUNT, 0, (short) 100);

        assertThat(feed.size(), equalTo(100));
        assertTrue(feed.get(0).getComment().getAuthor().getName().matches("[a-z\\-_0-9]+"));
    }

    @Test
    public void testGetBlogEntries() throws Exception {
        final List<BlogEntry> blogEntries = golos4J.getFollowApiMethods().getBlogEntries(ACCOUNT, 0, (short) 10);

        assertThat(blogEntries.size(), equalTo(10));
        MatcherAssert.assertThat(blogEntries.get(0).getBlog(), equalTo(ACCOUNT));
    }

    @Test
    public void testGetBlog() throws Exception {
        final List<CommentBlogEntry> blog = golos4J.getFollowApiMethods().getBlog(ACCOUNT, 0, (short) 10);

        assertThat(blog.size(), equalTo(10));
        assertThat(blog.get(0).getBlog(), equalTo(new AccountName("lenutsa")));
    }

    @Test
    public void testGetAccountReputation() throws Exception {
        final List<AccountReputation> accountReputations = golos4J.getFollowApiMethods().getAccountReputations(ACCOUNT, 10);

        assertThat(accountReputations.size(), equalTo(10));
        assertThat(accountReputations.get(0).getReputation(), greaterThan(51509992764100L));
    }

    @Test
    public void testGetRebloggedBy() throws Exception {
        final List<AccountName> accountNames = golos4J.getFollowApiMethods().getRebloggedBy(ACCOUNT,
                PERMLINK);

        assertThat(accountNames.size(), greaterThan(0));
        assertThat(accountNames.get(0), equalTo(ACCOUNT));
    }

    @Test
    public void testGetBlogAuthors() throws Exception {
        final List<PostsPerAuthorPair> blogAuthors = golos4J.getFollowApiMethods().getBlogAuthors(ACCOUNT);

        assertThat(blogAuthors.size(), greaterThan(0));
        assertThat(blogAuthors.get(1).getAccount(), equalTo(new AccountName("ami")));
    }

    @Test
    public void testOrderBook() throws Exception {
        final OrderBook orderBook = golos4J.getDatabaseMethods().getOrderBookUsingDatabaseApi(1);

        assertThat(orderBook.getAsks().size(), equalTo(1));
        assertThat(orderBook.getBids().get(0).getSbd(), greaterThan(1L));
    }

    @Test
    public void testGetFeedHistory() throws Exception {
        final FeedHistory feedHistory = golos4J.getDatabaseMethods().getFeedHistory();

        assertThat(feedHistory.getCurrentPrice().getBase().getAmount(), greaterThan(1.0));
        assertThat(feedHistory.getPriceHistory().size(), greaterThan(1));
    }

}
