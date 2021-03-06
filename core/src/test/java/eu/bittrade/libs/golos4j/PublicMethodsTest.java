package eu.bittrade.libs.golos4j;


import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import eu.bittrade.libs.golosj.Golos4J;
import eu.bittrade.libs.golosj.apis.follow.enums.FollowType;
import eu.bittrade.libs.golosj.apis.follow.model.AccountReputation;
import eu.bittrade.libs.golosj.apis.follow.model.BlogEntry;
import eu.bittrade.libs.golosj.apis.follow.model.CommentBlogEntry;
import eu.bittrade.libs.golosj.apis.follow.model.CommentFeedEntry;
import eu.bittrade.libs.golosj.apis.follow.model.FeedEntry;
import eu.bittrade.libs.golosj.apis.follow.model.FollowApiObject;
import eu.bittrade.libs.golosj.apis.follow.model.FollowCountApiObject;
import eu.bittrade.libs.golosj.apis.follow.model.PostsPerAuthorPair;
import eu.bittrade.libs.golosj.base.models.AccountName;
import eu.bittrade.libs.golosj.base.models.AppliedOperation;
import eu.bittrade.libs.golosj.base.models.Asset;
import eu.bittrade.libs.golosj.base.models.BlockHeader;
import eu.bittrade.libs.golosj.base.models.ChainProperties;
import eu.bittrade.libs.golosj.base.models.Config;
import eu.bittrade.libs.golosj.base.models.Discussion;
import eu.bittrade.libs.golosj.base.models.DiscussionLight;
import eu.bittrade.libs.golosj.base.models.DiscussionQuery;
import eu.bittrade.libs.golosj.base.models.DiscussionWithComments;
import eu.bittrade.libs.golosj.base.models.ExtendedAccount;
import eu.bittrade.libs.golosj.base.models.FeedHistory;
import eu.bittrade.libs.golosj.base.models.GlobalProperties;
import eu.bittrade.libs.golosj.base.models.Permlink;
import eu.bittrade.libs.golosj.base.models.ScheduledHardfork;
import eu.bittrade.libs.golosj.base.models.SignedBlockWithInfo;
import eu.bittrade.libs.golosj.base.models.TrendingTag;
import eu.bittrade.libs.golosj.base.models.VoteState;
import eu.bittrade.libs.golosj.base.models.Witness;
import eu.bittrade.libs.golosj.base.models.WitnessSchedule;
import eu.bittrade.libs.golosj.base.models.operations.AccountCreateOperation;
import eu.bittrade.libs.golosj.base.models.operations.Operation;
import eu.bittrade.libs.golosj.base.models.operations.VoteOperation;
import eu.bittrade.libs.golosj.enums.AssetSymbolType;
import eu.bittrade.libs.golosj.enums.DiscussionSortType;
import eu.bittrade.libs.golosj.enums.PrivateKeyType;
import eu.bittrade.libs.golosj.exceptions.SteemCommunicationException;
import eu.bittrade.libs.golosj.exceptions.SteemResponseError;
import eu.bittrade.libs.golosj.util.AuthUtils;
import eu.bittrade.libs.golosj.util.SteemJUtils;

import static eu.bittrade.libs.golosj.enums.PrivateKeyType.POSTING;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.isEmptyOrNullString;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class PublicMethodsTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(PublicMethodsTest.class);
    public static final AccountName ACCOUNT = new AccountName("yuri-vlad-second");
    private static final AccountName WITNESS_ACCOUNT = new AccountName("cyberfounder");
    private static final AccountName TESTNET_ACCOUNT_NAME = new AccountName("qwerty");
    private static final Permlink TESTNET_PERMLINK = new Permlink("qwerty");
    public static final Permlink PERMLINK = new Permlink("kriptopirozhkovo-khardfokovyi-post");
    private Golos4J golos4J;
    private boolean useTestnet = false;

    @Before
    public void setup() {
        if (useTestnet) golos4J = Golos4J.getTestnet();
        else golos4J = Golos4J.getInstance();
    }

    @Test
    public void testGetBlockHeader() throws Exception {
        final BlockHeader blockHeader = golos4J.getDatabaseMethods().getBlockHeader(8000L);
        assertNotNull(blockHeader);
        assertNotNull(blockHeader.getPrevious());
        assertNotNull(blockHeader.getTimestamp());
    }

    //    const totalVestingFundSteem = toAsset(total_vesting_fund_steem).amount
//    const totalVestingShares = toAsset(total_vesting_shares).amount
//    const vesting_shares = toAsset(vestingShares).amount
//    return (totalVestingFundSteem * (vesting_shares / totalVestingShares)).toFixed(3)
    //61082.674
    @Test
    public void testConvertToGolosPower() throws Exception {
        GlobalProperties properties = golos4J.getDatabaseMethods().getDynamicGlobalProperties();
        ExtendedAccount account = golos4J.getDatabaseMethods().getAccounts(Collections.singletonList(new AccountName("uanix"))).get(0);
        double totalVestingFundSteem = properties.getTotalVestingFundSteem().getAmount();
        double totalVestingShares = properties.getTotalVestingShares().getAmount();
        double vesting_shares = account.getVestingShares().getAmount();
        double result = (totalVestingFundSteem * (vesting_shares/totalVestingShares));
        System.out.println(result);
    }

    @Test
    public void testProps() throws Exception {
        GlobalProperties props = golos4J.getDatabaseMethods().getDynamicGlobalProperties();
        System.out.println(props);
    }

    @Test
    public void testGetOpsInBlock() throws Exception {
        final List<AppliedOperation> appliedOperationsOnlyVirtual = golos4J.getDatabaseMethods().getOpsInBlock(1, false);

        assertTrue(!appliedOperationsOnlyVirtual.isEmpty());

        //   assertThat(appliedOperationsOnlyVirtual);
        assertThat(appliedOperationsOnlyVirtual.get(0).getTrxInBlock(), greaterThanOrEqualTo(0));
        assertThat(appliedOperationsOnlyVirtual.get(0).getVirtualOp(), greaterThanOrEqualTo(0L));
    }

    @Test
    public void testGetPost() throws Exception {
        DiscussionWithComments comments = golos4J.getDatabaseMethods().getStoryWithRepliesAndInvolvedAccounts(
                new AccountName("yuri-vlad"), new Permlink("b07ce6d4-6134-45d4-b2c0-771e290ce9b2"), -1);
        System.out.println(comments);
    }

    @Test
    public void getProfileImagePath() throws Exception {

        List<AccountName> accountNames = new ArrayList<>();
        accountNames.add(new AccountName("yuri-vlad"));
        accountNames.add(new AccountName("yuri-vlad-second"));

        Map<String, String> avatars = golos4J.getDatabaseMethods().getAccountAvatar(accountNames);

        assertNotNull(avatars);
        assertTrue(avatars.size() > 0);
        System.out.println(avatars);
        assertTrue(avatars.containsKey("yuri-vlad"));
        assertTrue(avatars.containsKey("yuri-vlad-second"));


    }


    @Test
    public void testActiveVotes() throws Exception {
        if (useTestnet) {
            final List<VoteState> activeVotesForArticle = golos4J.getDatabaseMethods().getActiveVotes(TESTNET_ACCOUNT_NAME, TESTNET_PERMLINK);
            assertTrue(!activeVotesForArticle.isEmpty());
            assertNotNull(activeVotesForArticle.get(0));
        } else {
            DiscussionQuery query = new DiscussionQuery();
            query.setSelectedAuthor(ACCOUNT);
            query.setLimit(1);
            Discussion discussion = golos4J.getDatabaseMethods().getDiscussionsBy(query, DiscussionSortType.GET_DISCUSSIONS_BY_BLOG).get(0);

            final List<VoteState> activeVotesForArticle = golos4J.getDatabaseMethods().getActiveVotes(discussion.getAuthor(), discussion.getPermlink());
            assertTrue(!activeVotesForArticle.isEmpty());
            assertNotNull(activeVotesForArticle.get(0));
        }

    }

    @Test
    public void testGetConfig() throws Exception {
        final Config config = golos4J.getDatabaseMethods().getConfig();
        final boolean isTestNet = config.getIsTestNet();
        final String steemitNullAccount = config.getSteemitNullAccount();
        final String initMinerName = config.getSteemitInitMinerName();


        assertEquals("expect the null account to be null", "null", steemitNullAccount);
        assertEquals("expect the init miner name to be initminer", "cyberfounder", initMinerName);

    }

    @Test
    public void testWifsGeneratin() throws Exception {
        System.out.println(AuthUtils.generatePrivateWiFs("sdgsdg", "", new PrivateKeyType[]{POSTING}));
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
        assertThat(activeWitnesses.get(0), not(isEmptyOrNullString()));
    }

    @Test
    public void getFeedHistoryTest() throws Exception {
        FeedHistory history = golos4J.getDatabaseMethods().getFeedHistory();
        assertNotNull(history.getCurrentPrice());
        assertThat(history.getCurrentPrice().getBase().getAmount(), greaterThan(0.0));
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
    }

    @Test
    public void testGetApiCount() throws Exception {
        assertThat("expect the number of accounts greater than 1", golos4J.getDatabaseMethods().getAccountCount(), greaterThan(1));
    }

    @Test
    public void testAccountHistory() throws Exception {
        if (useTestnet) {
            final Map<Integer, AppliedOperation> accountHistorySetOne = golos4J.
                    getDatabaseMethods()
                    .getAccountHistory(TESTNET_ACCOUNT_NAME, 1000, 1000);
            Operation firstOperation = accountHistorySetOne.get(0).getOp();
            assertTrue("the first operation for each account is the 'account_create_operation'",
                    firstOperation instanceof AccountCreateOperation);
        } else {
            final Map<Integer, AppliedOperation> accountHistorySetOne = golos4J.
                    getDatabaseMethods()
                    .getAccountHistory(ACCOUNT, 1000, 1000);
            Operation firstOperation = accountHistorySetOne.get(0).getOp();
            assertTrue("the first operation for each account is the 'account_create_operation'",
                    firstOperation instanceof AccountCreateOperation);
        }

    }


    @Test
    public void testOperationParsing() throws SteemCommunicationException {
        if (useTestnet) return;
        SignedBlockWithInfo blockContainingVoteOperation = golos4J.getDatabaseMethods().getBlock(30003);
        Operation voteOperation = blockContainingVoteOperation.getTransactions().get(0).getOperations()
                .get(0);

        assertThat(voteOperation, instanceOf(VoteOperation.class));
        assertThat(((VoteOperation) voteOperation).getAuthor().getName(), equalTo("sashko"));
        assertThat(((VoteOperation) voteOperation).getVoter().getName(), equalTo("t3ran13"));

        assertThat(blockContainingVoteOperation.getTimestamp().getDateTime(), equalTo("2016-10-19T02:13:27"));
        assertThat(blockContainingVoteOperation.getWitness(), equalTo("testz"));

    }


    @Test
    public void getBlogsPosts() throws SteemCommunicationException {
        if (useTestnet) {
            DiscussionQuery discussionQuery = new DiscussionQuery();
            discussionQuery.setLimit(20);
            discussionQuery.setSelectAuthors(Collections.singletonList(TESTNET_ACCOUNT_NAME));
            List<Discussion> discussions = golos4J.getDatabaseMethods().getDiscussionsBy(discussionQuery, DiscussionSortType.GET_DISCUSSIONS_BY_BLOG);
            Assert.assertTrue(discussions.size() > 0);
        } else {
            DiscussionQuery discussionQuery = new DiscussionQuery();
            discussionQuery.setLimit(20);
            discussionQuery.setSelectAuthors(Collections.singletonList(new AccountName("mariadia")));
            List<Discussion> discussions = golos4J.getDatabaseMethods().getDiscussionsBy(discussionQuery, DiscussionSortType.GET_DISCUSSIONS_BY_BLOG);
            Assert.assertTrue(discussions.size() > 2);
        }
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
                        DiscussionSortType.GET_DISCUSSIONS_BY_CASHOUT
                };

        final DiscussionQuery discussionQuery = new DiscussionQuery();
        discussionQuery.setLimit(1);
        discussionQuery.setTruncateBody(1);

        Executor executor = Executors.newFixedThreadPool(35);
        final CountDownLatch latch = new CountDownLatch(1);
        for (int i = 0; i < 10_000; i++) {
            final int finalI = i;
            final int finalI1 = i;
            executor.execute(new Runnable() {
                @Override
                public void run() {
                    System.out.println(" i = " + finalI);
                    Random random = new Random();
                    DiscussionSortType discussioType = sortTypes[random.nextInt(sortTypes.length)];
                    DiscussionQuery query = new DiscussionQuery();
                    query.setLimit(1);
                    query.setTruncateBody(10);
                    query.setVoteLimit(0);
                    try {
                        Discussion discussion = golos4J.getDatabaseMethods().getDiscussionsBy(discussionQuery, discussioType).get(0);
                        if (discussioType == DiscussionSortType.GET_DISCUSSIONS_BY_HOT) {
                            if (!discussion.getRootTitle().contains("Проект TAPEUR. Токен GOLOS. Из серии Жизнь Замечательных "))
                                throw new RuntimeException("wring discussion type");
                        } else if (discussioType == DiscussionSortType.GET_DISCUSSIONS_BY_TRENDING) {
                            if (!discussion.getRootTitle().contains("Японцы пошли в атаку на Паркинсона"))
                                throw new RuntimeException("wring discussion type");
                        }
                    } catch (SteemCommunicationException e) {
                        throw new RuntimeException(e.getMessage());
                    }
                    if (finalI1 == 9_999) latch.countDown();
                }
            });


        }
        latch.await();

    }

    @Test
    public void testGetDiscussionLightBy() throws Exception {

        final DiscussionSortType[] sortTypes = new DiscussionSortType[]
                {
                        DiscussionSortType.GET_DISCUSSIONS_BY_TRENDING,
                        DiscussionSortType.GET_DISCUSSIONS_BY_CREATED,
                        DiscussionSortType.GET_DISCUSSIONS_BY_ACTIVE,
                        DiscussionSortType.GET_DISCUSSIONS_BY_CASHOUT,
                        DiscussionSortType.GET_DISCUSSIONS_BY_VOTES,
                        DiscussionSortType.GET_DISCUSSIONS_BY_CHILDREN,
                        DiscussionSortType.GET_DISCUSSIONS_BY_HOT,
                        DiscussionSortType.GET_DISCUSSIONS_BY_CASHOUT
                };

        DiscussionQuery discussionQuery = new DiscussionQuery();
        discussionQuery.setLimit(1);
        discussionQuery.setTruncateBody(100);

        for (final DiscussionSortType type : sortTypes) {
            final List<DiscussionLight> discussions = golos4J.getDatabaseMethods().getDiscussionsLightBy(discussionQuery, type);
            assertNotNull("expect discussions", discussions);
            assertThat("expect discussions in " + type + " greater than zero", discussions.size(),
                    greaterThanOrEqualTo(0));
        }


    }

    @Test
    public void testGetDiscussionsByAuthorBeforeDate() throws Exception {
        if (useTestnet) {
            final List<Discussion> repliesByLastUpdate = golos4J.getDatabaseMethods()
                    .getDiscussionsByAuthorBeforeDate(TESTNET_ACCOUNT_NAME, TESTNET_PERMLINK,
                            SteemJUtils.transformDateToString(new Date()), 8);
            assertTrue(!repliesByLastUpdate.isEmpty());
        } else {
            DiscussionQuery discussionQuery = new DiscussionQuery();
            discussionQuery.setLimit(2);
            discussionQuery.setSelectedAuthor(ACCOUNT);
            Discussion discussion =
                    golos4J.getDatabaseMethods().getDiscussionsBy(discussionQuery, DiscussionSortType.GET_DISCUSSIONS_BY_BLOG).get(0);

            final List<Discussion> repliesByLastUpdate =
                    golos4J.getDatabaseMethods().getDiscussionsByAuthorBeforeDate(discussion.getAuthor(), discussion.getPermlink(),
                            SteemJUtils.transformDateToString(new Date()), 8);

            assertTrue(!repliesByLastUpdate.isEmpty());
            assertNotNull(repliesByLastUpdate.get(0));
        }
    }

    @Test
    public void testGetDynamicGlobalProperties() throws Exception {
        final GlobalProperties properties = golos4J.getDatabaseMethods().getDynamicGlobalProperties();

        assertNotNull("expect properties", properties);
        assertThat("expect head block number", properties.getHeadBlockNumber(), greaterThan(10L));
        assertTrue(properties.getHeadBlockId().toString().matches("[0-9a-f]{40}"));
        assertThat(properties.getHeadBlockId().getNumberFromHash(), greaterThan(1));
        assertThat(properties.getTotalPow(), greaterThan(new BigInteger("1")));
    }


    @Test
    public void testGetWitnessByAccount() throws Exception {
        final Witness activeWitnessesByVote = golos4J.getDatabaseMethods().getWitnessByAccount(WITNESS_ACCOUNT);

        assertEquals("expect " + WITNESS_ACCOUNT + " to be the owner of the returned witness account", WITNESS_ACCOUNT,
                activeWitnessesByVote.getOwner());
    }

    @Test
    public void testGetWitnessesByVote() throws Exception {
        final List<Witness> activeWitnessesByVote = golos4J.getDatabaseMethods().getWitnessByVote(WITNESS_ACCOUNT, 10);
        assertTrue(!activeWitnessesByVote.isEmpty());
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
    public void testLookupAccount() throws Exception {
        final List<String> accounts = golos4J.getDatabaseMethods().lookupAccounts(ACCOUNT.getName(), 10);

        assertNotNull("expect accounts", accounts);
        assertThat("expect at least one account", accounts.size(), greaterThan(0));
    }

    @Test
    public void testLookupWitnessAccount() throws Exception {
        final List<String> accounts = golos4J.getDatabaseMethods().lookupWitnessAccounts("cyberfounder", 10);

        assertNotNull("expect accounts", accounts);
        assertThat("expect at least one account", accounts.size(), greaterThan(0));
    }

    @Test
    public void testMinerQueue() throws Exception {
        final List<String> minerQueue = golos4J.getDatabaseMethods().getMinerQueue();

        assertThat("expect the number of miners greater than 0", minerQueue.size(), greaterThan(0));
    }

    @Test
    public void testTrendingTags() throws Exception {
        final List<TrendingTag> trendingTags = golos4J.getDatabaseMethods().getTrendingTags(null, 10);

        assertNotNull("expect trending tags", trendingTags);
        assertThat("expect trending tags size > 0", trendingTags.size(), greaterThan(0));
    }


    @Test
    public void testWitnessCount() throws Exception {
        final int witnessCount = golos4J.getDatabaseMethods().getWitnessCount();

        assertThat("expect the number of witnesses greater than 0", witnessCount, greaterThan(0));
    }

    @Test
    public void testWitnessSchedule() throws Exception {
        final WitnessSchedule witnessSchedule = golos4J.getDatabaseMethods().getWitnessSchedule();

        assertNotNull("expect hardfork version", witnessSchedule);
        assertThat(witnessSchedule.getTop19Weight(), equalTo((short) 1));
    }

    @Test
    public void testGetTagsUsedByAuthor() throws Exception {
        List<TrendingTag> trendingTags = golos4J.getDatabaseMethods().getTagsUsedByAuthor(WITNESS_ACCOUNT);
        assertTrue(!trendingTags.isEmpty());
    }

    @Test
    public void getWitnessesTest() throws Exception {
        ArrayList<Integer> integers = new ArrayList<>();
        integers.add(0);
        integers.add(1);
        List<Witness> witnesses = golos4J.getDatabaseMethods().getWitnesses(integers);
        assertTrue(!witnesses.isEmpty());
    }


    @Test
    public void getWitnessesByAccount() throws Exception {
        Witness witness = golos4J.getDatabaseMethods().getWitnessByAccount(WITNESS_ACCOUNT);
        assertNotNull(witness);
    }

    @Test
    public void testGetFollowers() throws Exception {
        final List<FollowApiObject> followers = golos4J.getFollowApiMethods().getFollowers(ACCOUNT,
                ACCOUNT, FollowType.BLOG, (short) 100);

        assertTrue(followers.size() > 0);
    }

    @Test
    public void testGetFollowCount() throws Exception {
        if (useTestnet) return;
        final FollowCountApiObject followCount = golos4J.getFollowApiMethods().getFollowCount(ACCOUNT);

        assertThat(followCount.getFollowerCount(), greaterThan(0));
        assertThat(followCount.getFollowingCount(), greaterThan(0));
    }

    @Test
    public void testGetFeedEntries() throws Exception {
        final List<FeedEntry> feedEntries = golos4J.getFollowApiMethods().getFeedEntries(useTestnet ? TESTNET_ACCOUNT_NAME : ACCOUNT, 0, (short) 100);

        assertThat(feedEntries.size(), greaterThan(0));
        if (!useTestnet)
            assertNotNull(feedEntries.get(0));
    }

    @Test
    public void testGetFeed() throws Exception {
        final List<CommentFeedEntry> feed = golos4J.getFollowApiMethods().getFeed(useTestnet ? TESTNET_ACCOUNT_NAME : ACCOUNT, 0, (short) 100);

        assertThat(feed.size(), greaterThan(0));
        if (!useTestnet)
            assertNotNull(feed.get(0));
    }

    @Test
    public void testGetBlogEntries() throws Exception {

        final List<BlogEntry> blogEntries =
                golos4J.getFollowApiMethods().getBlogEntries(useTestnet ? TESTNET_ACCOUNT_NAME : ACCOUNT, 0, (short) 10);

        assertThat(blogEntries.size(), greaterThan(0));
        assertNotNull(blogEntries.get(0));
    }

    @Test
    public void testGetBlog() throws Exception {
        final List<CommentBlogEntry> blog = golos4J.getFollowApiMethods()
                .getBlog(useTestnet ? TESTNET_ACCOUNT_NAME : ACCOUNT, 0, (short) 10);

        assertThat(blog.size(), greaterThan(0));
        assertNotNull(blog.get(0));
    }

    @Test
    public void testGetAccountReputation() throws Exception {
        List<AccountReputation> accountReputations = golos4J.getFollowApiMethods().getAccountReputations(ACCOUNT, 10);

        assertTrue(accountReputations.size() > 0);
        List<AccountName> accountNames = new ArrayList<>();
        if (useTestnet) {
            accountNames.add(ACCOUNT);
            accountNames.add(WITNESS_ACCOUNT);
            accountReputations = golos4J.getFollowApiMethods().getAccountReputations(accountNames);
            assertTrue(!accountReputations.isEmpty());
            assertNotNull(accountReputations.get(0));
        }
    }

    @Test
    public void testGetRebloggedBy() throws Exception {
        if (useTestnet) return;
        DiscussionQuery discussionQuery = DiscussionQuery.newBuilder().setLimit(1).setTruncateBody(0).build();
        Discussion discussion = golos4J.getDatabaseMethods().getDiscussionsBy(discussionQuery, DiscussionSortType.GET_DISCUSSIONS_BY_TRENDING).get(0);
        final List<AccountName> accountNames = golos4J.getFollowApiMethods().getRebloggedBy(discussion.getAuthor(),
                discussion.getPermlink());

        assertThat(accountNames.size(), greaterThan(0));
        assertNotNull(accountNames.get(0));
    }

    @Test
    public void testGetBlogAuthors() throws Exception {
        if (useTestnet) return;
        final List<PostsPerAuthorPair> blogAuthors = golos4J.getFollowApiMethods().getBlogAuthors(WITNESS_ACCOUNT);

        assertThat(blogAuthors.size(), greaterThan(0));
        assertThat(blogAuthors.get(1).getAccount(), equalTo(new AccountName("ami")));
    }

    @Test
    public void testGetAccounts() throws Exception {
        List<ExtendedAccount> accounts = golos4J.getDatabaseMethods().getAccounts(Collections.singletonList(WITNESS_ACCOUNT));
        assertTrue(!accounts.isEmpty());
    }

    @Test
    public void testGetStory() throws Exception {
        DiscussionWithComments story = golos4J.getDatabaseMethods().getStoryWithRepliesAndInvolvedAccounts(
                new AccountName("pravda"), new Permlink("obzor-estafety-ulybok-na-golose-nedelya-2"), -1);

        assertNotNull(story);
        assertThat("there not null replies", story.getDiscussions().size() > 0);
        assertThat("there not null accounts", story.getInvolvedAccounts().size() > 0);
    }


    @Test
    public void getFeedTest() throws Exception {
        DiscussionQuery query1 = DiscussionQuery.newBuilder().setLimit(20).setTruncateBody(0).build();
        List<DiscussionLight> discussions1 = golos4J.getDatabaseMethods().getDiscussionsLightBy(query1, DiscussionSortType.GET_DISCUSSIONS_BY_TRENDING);

        if (useTestnet) {
            DiscussionQuery query = new DiscussionQuery();
            query.setSelectAuthors(Collections.singletonList(new AccountName("qwerty")));
            query.setLimit(20);
            query.setTruncateBody(1025);
            List<DiscussionLight> discussions = golos4J.getDatabaseMethods().getDiscussionsLightBy(query, DiscussionSortType.GET_DISCUSSIONS_BY_BLOG);
            assertTrue(!discussions.isEmpty());
        } else {
            DiscussionQuery query = new DiscussionQuery();
            query.setSelectAuthors(Collections.singletonList(new AccountName("nick.kharchenko")));
            query.setLimit(20);
            query.setTruncateBody(1025);
            List<DiscussionLight> discussions = golos4J.getDatabaseMethods().getDiscussionsLightBy(query, DiscussionSortType.GET_DISCUSSIONS_BY_BLOG);
            assertTrue(!discussions.isEmpty());
        }

    }

    @Test
    public void testGetStoryWithoutBlog() throws Exception {
        DiscussionLight d = golos4J.getDatabaseMethods().getContentLight(new AccountName("sinte"),
                new Permlink("o-socialnykh-psikhopatakh-chast-3-o-tikhonyakh-mechtatelyakh-stesnitelnykh"));
        System.out.println(d);
    }

}
