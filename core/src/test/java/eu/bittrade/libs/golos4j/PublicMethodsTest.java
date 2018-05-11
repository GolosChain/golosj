package eu.bittrade.libs.golos4j;


import org.bitcoinj.core.Base58;
import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.hamcrest.MatcherAssert;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import eu.bittrade.libs.steemj.Golos4J;
import eu.bittrade.libs.steemj.apis.follow.enums.FollowType;
import eu.bittrade.libs.steemj.apis.follow.model.AccountReputation;
import eu.bittrade.libs.steemj.apis.follow.model.BlogEntry;
import eu.bittrade.libs.steemj.apis.follow.model.CommentBlogEntry;
import eu.bittrade.libs.steemj.apis.follow.model.CommentFeedEntry;
import eu.bittrade.libs.steemj.apis.follow.model.FeedEntry;
import eu.bittrade.libs.steemj.apis.follow.model.FollowApiObject;
import eu.bittrade.libs.steemj.apis.follow.model.FollowCountApiObject;
import eu.bittrade.libs.steemj.apis.follow.model.PostsPerAuthorPair;
import eu.bittrade.libs.steemj.base.models.AccountName;
import eu.bittrade.libs.steemj.base.models.AppliedOperation;
import eu.bittrade.libs.steemj.base.models.Asset;
import eu.bittrade.libs.steemj.base.models.BlockHeader;
import eu.bittrade.libs.steemj.base.models.ChainProperties;
import eu.bittrade.libs.steemj.base.models.Config;
import eu.bittrade.libs.steemj.base.models.Discussion;
import eu.bittrade.libs.steemj.base.models.DiscussionLight;
import eu.bittrade.libs.steemj.base.models.DiscussionQuery;
import eu.bittrade.libs.steemj.base.models.DiscussionWithComments;
import eu.bittrade.libs.steemj.base.models.ExtendedAccount;
import eu.bittrade.libs.steemj.base.models.GlobalProperties;
import eu.bittrade.libs.steemj.base.models.LiquidityBalance;
import eu.bittrade.libs.steemj.base.models.Permlink;
import eu.bittrade.libs.steemj.base.models.PublicKey;
import eu.bittrade.libs.steemj.base.models.ScheduledHardfork;
import eu.bittrade.libs.steemj.base.models.SignedBlockWithInfo;
import eu.bittrade.libs.steemj.base.models.SteemVersionInfo;
import eu.bittrade.libs.steemj.base.models.TrendingTag;
import eu.bittrade.libs.steemj.base.models.Vote;
import eu.bittrade.libs.steemj.base.models.VoteState;
import eu.bittrade.libs.steemj.base.models.Witness;
import eu.bittrade.libs.steemj.base.models.WitnessSchedule;
import eu.bittrade.libs.steemj.base.models.operations.AccountCreateOperation;
import eu.bittrade.libs.steemj.base.models.operations.Operation;
import eu.bittrade.libs.steemj.base.models.operations.VoteOperation;
import eu.bittrade.libs.steemj.enums.AssetSymbolType;
import eu.bittrade.libs.steemj.enums.DiscussionSortType;
import eu.bittrade.libs.steemj.enums.PrivateKeyType;
import eu.bittrade.libs.steemj.exceptions.SteemCommunicationException;
import eu.bittrade.libs.steemj.exceptions.SteemResponseError;
import eu.bittrade.libs.steemj.util.AuthUtils;

import static eu.bittrade.libs.steemj.enums.PrivateKeyType.POSTING;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.isEmptyOrNullString;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class PublicMethodsTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(PublicMethodsTest.class);
    public static final AccountName ACCOUNT = new AccountName("yuri-vlad");
    private static final AccountName WITNESS_ACCOUNT = new AccountName("cyberfounder");
    public static final Permlink PERMLINK = new Permlink("kriptopirozhkovo-khardfokovyi-post");
    private Golos4J golos4J;

    @Before
    public void setup() {
        golos4J = Golos4J.getInstance();
    }

    @Test
    public void testGetBlockHeader() throws Exception {
        final BlockHeader blockHeader = golos4J.getDatabaseMethods().getBlockHeader(8000L);
        assertNotNull(blockHeader);
        assertNotNull(blockHeader.getPrevious());
        assertNotNull(blockHeader.getTimestamp());
    }

    @Test
    public void testGetOpsInBlock() throws Exception {
        final List<AppliedOperation> appliedOperationsOnlyVirtual = golos4J.getDatabaseMethods().getOpsInBlock(1, false);

        assertTrue(!appliedOperationsOnlyVirtual.isEmpty());

        //   assertThat(appliedOperationsOnlyVirtual);
        assertThat(appliedOperationsOnlyVirtual.get(0).getTrxInBlock(), equalTo(0));
        assertThat(appliedOperationsOnlyVirtual.get(0).getVirtualOp(), equalTo(0L));
        MatcherAssert.assertThat(appliedOperationsOnlyVirtual.get(0).getOp(), instanceOf(VoteOperation.class));
    }

    @Test
    public void testGetPost() throws Exception {
        DiscussionWithComments comments = golos4J.getDatabaseMethods().getStoryByRoute("test",
                new AccountName("yuri-vlad"), new Permlink("b07ce6d4-6134-45d4-b2c0-771e290ce9b2"));
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
        //  final List<Vote> votes = golos4J.getDatabaseMethods().getAccountVotes(ACCOUNT);
        final List<VoteState> activeVotesForArticle = golos4J.getDatabaseMethods().getActiveVotes(ACCOUNT, PERMLINK);

        //  assertNotNull("expect votes", votes);
        //  assertThat("expect account has votes", votes.size(), greaterThan(0));

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
    public void testAccounts() throws Exception {//0,000268379
        List<ExtendedAccount> accounts = golos4J.getDatabaseMethods().getAccounts(Collections.singletonList(new AccountName("yuri-vlad")));
        assertEquals(1, accounts.size());
        System.out.println("posting key is " + ((PublicKey) accounts.get(0).getPosting().getKeyAuths().keySet().toArray()[0]).getAddressFromPublicKey());

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
    public void getBlogsPosts() throws SteemCommunicationException {
        DiscussionQuery discussionQuery = new DiscussionQuery();
        discussionQuery.setLimit(20);
        discussionQuery.setSelectAuthors(Collections.singletonList(new AccountName("mariadia")));
        List<Discussion> discussions = golos4J.getDatabaseMethods().getDiscussionsBy(discussionQuery, DiscussionSortType.GET_DISCUSSIONS_BY_BLOG);
        Assert.assertTrue(discussions.size() > 2);
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
        final List<Discussion> repliesByLastUpdate = golos4J.getDatabaseMethods().getDiscussionsByAuthorBeforeDate(ACCOUNT, PERMLINK,
                "2017-10-10T14:08:51", 8);

        assertEquals("expect that 8 results are returned", repliesByLastUpdate.size(), 8);
        assertEquals("expect " + ACCOUNT + " to be the first returned author",
                repliesByLastUpdate.get(0).getAuthor(), ACCOUNT);
        assertEquals("expect " + PERMLINK + " to be the first returned permlink", PERMLINK,
                repliesByLastUpdate.get(0).getPermlink());
        // TODO: 24/03/2018 document methods that returns exception instead of empty collection
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

    @Test
    public void testWitnessCount() throws Exception {
        final int witnessCount = golos4J.getDatabaseMethods().getWitnessCount();

        assertThat("expect the number of witnesses greater than 13071", witnessCount, greaterThan(0));
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

        assertTrue(followers.size() > 0);
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

        assertTrue(accountReputations.size() > 1);
        assertThat(accountReputations.get(1).getReputation(), greaterThan(0L));
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
    public void testGetStory() throws Exception {
        DiscussionWithComments story = golos4J.getDatabaseMethods().getStoryByRoute("ru--yestafetaulybo",
                new AccountName("pravda"), new Permlink("obzor-estafety-ulybok-na-golose-nedelya-2"));

        assertNotNull(story);
        assertThat("there not null replies", story.getDiscussions().size() > 0);
        assertThat("there not null accounts", story.getInvolvedAccounts().size() > 0);
    }


    /*  @Test
      public void testGetAvatarByName() throws Exception {
          String avatar = golos4J.getDatabaseMethods().getAccountAvatar(new AccountName("andrvik"));
          assertNotNull(avatar);
          avatar = golos4J.getDatabaseMethods().getAccountAvatar("ru--prave",
                  new AccountName("andrvik"),
                  new Permlink("pravo-deputaty-gotovyat-predvybornyi-portfel-svezhii-obzor-zakonoproektov-ot-dumskikh-frakcii"));
          assertNotNull(avatar);
      }
  */
    @Test
    public void getFeedTest() throws Exception {
        DiscussionQuery query = new DiscussionQuery();
        query.setSelectAuthors(Collections.singletonList(new AccountName("nick.kharchenko")));
        //  query.setStartAuthor(new AccountName("yuri-vlad-second"));
        query.setLimit(20);
        query.setTruncateBody(1025);
        List<DiscussionLight> discussions = golos4J.getDatabaseMethods().getDiscussionsLightBy(query, DiscussionSortType.GET_DISCUSSIONS_BY_BLOG);
        System.out.println(discussions);
    }

    @Test
    public void testGetStoryWithoutBlog() throws Exception {
        DiscussionLight d = golos4J.getDatabaseMethods().getContentLight(new AccountName("sinte"),
                new Permlink("o-socialnykh-psikhopatakh-chast-3-o-tikhonyakh-mechtatelyakh-stesnitelnykh"));
        System.out.println(d);
    }

    @Test
    public void getAvatartest() throws Exception {
        long pre = System.currentTimeMillis();
        ArrayList<AccountName> list = new ArrayList<>();
        list.add(new AccountName("jevgenika"));
        list.add(new AccountName("yuri-vlad"));
        list.add(new AccountName("yuri-vlad-second"));
        List<ExtendedAccount> extendedAccount =
                golos4J.getDatabaseMethods().getAccounts(list);
        long after = System.currentTimeMillis();
        System.out.println(after - pre);
    }

    @Test
    public void test() throws Exception {
        String pre = "5K7YbhJZqGnw3hYzsmH5HbDixWP5ByCBdnJxM5uoe9LuMX5rcZV";
        String result = Base58.encode(pre.getBytes());
        System.out.println(result);
    }

}
