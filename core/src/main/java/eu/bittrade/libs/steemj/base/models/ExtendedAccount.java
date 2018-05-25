package eu.bittrade.libs.steemj.base.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import com.sun.istack.internal.Nullable;
import org.apache.commons.lang3.tuple.Pair;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import eu.bittrade.libs.steemj.base.models.deserializer.BigIntegerAppliedOperationsDeserializer;

/**
 * This class represents a Steem "extended_account" object.
 *
 * @author <a href="http://steemit.com/@dez1337">dez1337</a>
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class ExtendedAccount extends Account {
    /**
     * Convert vesting_shares to vesting Steem.
     */
    @JsonProperty("vesting_balance")
    private Asset vestingBalance;
    // Original type is "share_type" which is a "safe<int64_t>".
    private long reputation;
    // The original tpye is map<uint64_t,applied_operation>
    /**
     * Transfer to/from vesting.
     */
    @JsonProperty("transfer_history")
    @JsonDeserialize(using = BigIntegerAppliedOperationsDeserializer.class)
    @Nullable
    private Map<BigInteger, AppliedOperation> transferHistory;
    // The original tpye is map<uint64_t,applied_operation>
    /**
     * Limit order / cancel / fill.
     */
    @JsonProperty("market_history")
    @JsonDeserialize(using = BigIntegerAppliedOperationsDeserializer.class)
    @Nullable
    private Map<BigInteger, AppliedOperation> marketHistory;
    // The original tpye is map<uint64_t,applied_operation>
    @JsonProperty("post_history")
    @JsonDeserialize(using = BigIntegerAppliedOperationsDeserializer.class)
    @Nullable
    private Map<BigInteger, AppliedOperation> postHistory;
    // The original tpye is map<uint64_t,applied_operation>
    @JsonProperty("vote_history")
    @Nullable
    @Deprecated
    @JsonDeserialize(using = BigIntegerAppliedOperationsDeserializer.class)
    private Map<BigInteger, AppliedOperation> voteHistory;
    // The original tpye is map<uint64_t,applied_operation>
    @JsonProperty("other_history")
    @JsonDeserialize(using = BigIntegerAppliedOperationsDeserializer.class)
    @Nullable
    private Map<BigInteger, AppliedOperation> otherHistory;
    // Original type is set<string>.
    @JsonProperty("witness_votes")
    @Nullable
    private List<AccountName> witnessVotes;
    // Original type is "vector<pair<string,uint32_t>>".
    @JsonProperty("tags_usage")
    @Nullable
    private List<Pair<String, Long>> tagsUsage;
    // Original type is "vector<pair<account_name_type,uint32_t>>".
    @JsonProperty("guest_bloggers")
    @Nullable
    private List<Pair<AccountName, Long>> guestBloggers;
    // Original type is "optional<map<uint32_t,extended_limit_order>>".
    @JsonProperty("open_orders")
    private Object[] openOrders;
    // Original type is "optional<vector<string>>".
    /**
     * Permlinks for this user.
     */
    @JsonProperty("comments")
    private Object[] comments;
    // Original type is "optional<vector<string>>".
    /**
     * Blog posts for this user.
     */
    @JsonProperty("blog")
    private Object[] blog;
    // Original type is "optional<vector<string>>".
    /**
     * Feed posts for this user.
     */
    @JsonProperty("feed")
    private Object[] feed;
    // Original type is "optional<vector<string>>".
    /**
     * Blog posts for this user.
     */
    @JsonProperty("recent_replies")
    private Object[] recentReplies;
    // Original type is "optional<vector<string>>".
    /**
     * Posts recommened for this user.
     */
    @JsonProperty("recommended")
    private Object[] recommended;
    /**
     * @deprecated This field is deprecated since HF 0.19.0 and may be null.
     */
    @JsonProperty("blog_category")
    @Nullable
    private Object[] blogCategory;

    /**
     * This object is only used to wrap the JSON response in a POJO, so
     * therefore this class should not be instantiated.
     */
    private ExtendedAccount() {
    }

    /**
     * @return the vestingBalance
     */
    public Asset getVestingBalance() {
        return vestingBalance;
    }

    /**
     * @return the reputation
     * Deprecated since HF_18
     */
    @Deprecated

    public long getReputation() {
        return reputation;
    }

    /**
     * @return the transferHistory
     */
    @Nullable
    @Deprecated
    public Map<BigInteger, AppliedOperation> getTransferHistory() {
        return transferHistory;
    }

    /**
     * @return the marketHistory
     */
    @Nullable
    @Deprecated
    public Map<BigInteger, AppliedOperation> getMarketHistory() {
        return marketHistory;
    }

    /**
     * @return the postHistory
     */
    @Nullable
    @Deprecated
    public Map<BigInteger, AppliedOperation> getPostHistory() {
        return postHistory;
    }

    /**
     * @return the voteHistory
     */
    @Nullable
    @Deprecated
    public Map<BigInteger, AppliedOperation> getVoteHistory() {
        return voteHistory;
    }

    /**
     * @return the otherHistory
     */
    @Nullable
    @Deprecated
    public Map<BigInteger, AppliedOperation> getOtherHistory() {
        return otherHistory;
    }

    /**
     * @return the witnessVotes
     */
    @Nullable
    @Deprecated
    public List<AccountName> getWitnessVotes() {
        return witnessVotes;
    }

    /**
     * @return the tagsUsage
     */
    @Nullable
    @Deprecated
    public List<Pair<String, Long>> getTagsUsage() {
        return tagsUsage;
    }

    /**
     * @return the guestBloggers
     */
    @Nullable
    @Deprecated
    public List<Pair<AccountName, Long>> getGuestBloggers() {
        return guestBloggers;
    }

    /**
     * @return the openOrders
     */
    public Object[] getOpenOrders() {
        return openOrders;
    }

    /**
     * @return the comments
     */
    public Object[] getComments() {
        return comments;
    }

    /**
     * @return the blog
     */
    public Object[] getBlog() {
        return blog;
    }

    /**
     * @return the feed
     */
    public Object[] getFeed() {
        return feed;
    }

    /**
     * @return the recentReplies
     */
    public Object[] getRecentReplies() {
        return recentReplies;
    }

    /**
     * @return the recommended
     */
    public Object[] getRecommended() {
        return recommended;
    }

    /**
     * @return the blogCategory
     */
    @Nullable
    @Deprecated
    public Object[] getBlogCategory() {
        return blogCategory;
    }

    @Override
    public String toString() {
        return super.toString() +  " ExtendedAccount{" +
                "vestingBalance=" + vestingBalance +
                ", reputation=" + reputation +
                ", transferHistory=" + transferHistory +
                ", marketHistory=" + marketHistory +
                ", postHistory=" + postHistory +
                ", voteHistory=" + voteHistory +
                ", otherHistory=" + otherHistory +
                ", witnessVotes=" + witnessVotes +
                ", tagsUsage=" + tagsUsage +
                ", guestBloggers=" + guestBloggers +
                ", openOrders=" + Arrays.toString(openOrders) +
                ", comments=" + Arrays.toString(comments) +
                ", blog=" + Arrays.toString(blog) +
                ", feed=" + Arrays.toString(feed) +
                ", recentReplies=" + Arrays.toString(recentReplies) +
                ", recommended=" + Arrays.toString(recommended) +
                ", blogCategory=" + Arrays.toString(blogCategory) +
                '}';
    }
}
