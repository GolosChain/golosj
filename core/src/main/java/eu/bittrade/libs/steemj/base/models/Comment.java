package eu.bittrade.libs.steemj.base.models;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigInteger;
import java.util.List;

import javax.annotation.Nullable;

/**
 * This class represents the Steem "comment_api_obj".
 *
 * @author <a href="http://steemit.com/@dez1337">dez1337</a>
 */
public class Comment {
    // Original type is comment_id_type.
    private long id;
    private String category;
    @JsonProperty("parent_author")
    private AccountName parentAuthor;
    @JsonProperty("parent_permlink")
    private Permlink parentPermlink;
    private AccountName author;
    private Permlink permlink;
    private String title;
    private String body;
    @JsonProperty("json_metadata")
    private String jsonMetadata;
    @JsonProperty("last_update")
    private TimePointSec lastUpdate;
    private TimePointSec created;
    private TimePointSec active;
    @JsonProperty("last_payout")
    private TimePointSec lastPayout;
    // Original type is uint8_t.
    private short depth;
    // Orignal type is uint32_t.
    private int children;
    @Deprecated
    @JsonProperty("children_rshares2")
    private String childrenRshares2;
    // Original type is "share_type" which is a "safe<int64_t>".
    @JsonProperty("net_rshares")
    private long netRshares;
    // Original type is "share_type" which is a "safe<int64_t>".
    @JsonProperty("abs_rshares")
    private long absRshares;
    // Original type is "share_type" which is a "safe<int64_t>".
    @JsonProperty("vote_rshares")
    private long voteRshares;
    // Original type is "share_type" which is a "safe<int64_t>".
    @JsonProperty("children_abs_rshares")
    private long childrenAbsRshares;
    @JsonProperty("cashout_time")
    private TimePointSec cashoutTime;
    @JsonProperty("max_cashout_time")
    private TimePointSec maxCashoutTime;
    // TODO: Original type is uint64_t, but long seems to be not enough here -->
    // "Can not deserialize value of type long from String
    // "16089511318360462253": not a valid Long value"
    @JsonProperty("total_vote_weight")
    private BigInteger totalVoteWeight;
    // Original type is uint64_t.
    @JsonProperty("reward_weight")
    private long rewardWeight;
    @JsonProperty("total_payout_value")
    private Asset totalPayoutValue;
    @JsonProperty("curator_payout_value")
    private Asset curatorPayoutValue;
    // Original type is "share_type" which is a "safe<int64_t>".
    @JsonProperty("author_rewards")
    private long authorRewards;
    // Original type is int32_t.
    @JsonProperty("net_votes")
    private int netVotes;
    // Original type is comment_id_type.
    @JsonProperty("root_comment")
    private long rootComment;
    @Deprecated
    private String mode;
    @JsonProperty("max_accepted_payout")
    private Asset maxAcceptedPayout;
    // Original type is uint16_t.
    @JsonProperty("percent_steem_dollars")
    private int percentSteemDollars;
    @JsonProperty("allow_replies")
    private Boolean allowReplies;
    @JsonProperty("allow_votes")
    private Boolean allowVotes;
    @JsonProperty("allow_curation_rewards")
    private Boolean allowCurationRewards;
    // TODO: Fix type
    // bip::vector< beneficiary_route_type, allocator< beneficiary_route_type >
    // > beneficiaries;
    @JsonProperty("beneficiaries")
    private List<Beneficiary> beneficiaries;

    @Nullable
    @JsonProperty("active_votes_count")
    private Long activeVotesCount;

    /**
     * This object is only used to wrap the JSON response in a POJO, so
     * therefore this class should not be instantiated.
     */
    protected Comment() {
    }

    public Comment(long id, String category, AccountName parentAuthor, Permlink parentPermlink, AccountName author, Permlink permlink, String title, String body, String jsonMetadata, TimePointSec lastUpdate, TimePointSec created, TimePointSec active, TimePointSec lastPayout, short depth, int children, String childrenRshares2, long netRshares, long absRshares, long voteRshares, long childrenAbsRshares, TimePointSec cashoutTime, TimePointSec maxCashoutTime, BigInteger totalVoteWeight, long rewardWeight, Asset totalPayoutValue, Asset curatorPayoutValue, long authorRewards, int netVotes, long rootComment, String mode, Asset maxAcceptedPayout, int percentSteemDollars, Boolean allowReplies, Boolean allowVotes, Boolean allowCurationRewards, List<Beneficiary> beneficiaries, Long activeVotesCount) {
        this.id = id;
        this.category = category;
        this.parentAuthor = parentAuthor;
        this.parentPermlink = parentPermlink;
        this.author = author;
        this.permlink = permlink;
        this.title = title;
        this.body = body;
        this.jsonMetadata = jsonMetadata;
        this.lastUpdate = lastUpdate;
        this.created = created;
        this.active = active;
        this.lastPayout = lastPayout;
        this.depth = depth;
        this.children = children;
        this.childrenRshares2 = childrenRshares2;
        this.netRshares = netRshares;
        this.absRshares = absRshares;
        this.voteRshares = voteRshares;
        this.childrenAbsRshares = childrenAbsRshares;
        this.cashoutTime = cashoutTime;
        this.maxCashoutTime = maxCashoutTime;
        this.totalVoteWeight = totalVoteWeight;
        this.rewardWeight = rewardWeight;
        this.totalPayoutValue = totalPayoutValue;
        this.curatorPayoutValue = curatorPayoutValue;
        this.authorRewards = authorRewards;
        this.netVotes = netVotes;
        this.rootComment = rootComment;
        this.mode = mode;
        this.maxAcceptedPayout = maxAcceptedPayout;
        this.percentSteemDollars = percentSteemDollars;
        this.allowReplies = allowReplies;
        this.allowVotes = allowVotes;
        this.allowCurationRewards = allowCurationRewards;
        this.beneficiaries = beneficiaries;
        this.activeVotesCount = activeVotesCount;
    }



    @Nullable
    public Long getActiveVotesCount() {
        return activeVotesCount;
    }

    public void setActiveVotesCount(Long activeVotesCount) {
        this.activeVotesCount = activeVotesCount;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setParentAuthor(AccountName parentAuthor) {
        this.parentAuthor = parentAuthor;
    }

    public void setParentPermlink(Permlink parentPermlink) {
        this.parentPermlink = parentPermlink;
    }

    public void setAuthor(AccountName author) {
        this.author = author;
    }

    public void setPermlink(Permlink permlink) {
        this.permlink = permlink;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public void setJsonMetadata(String jsonMetadata) {
        this.jsonMetadata = jsonMetadata;
    }

    public void setLastUpdate(TimePointSec lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    public void setCreated(TimePointSec created) {
        this.created = created;
    }

    public void setActive(TimePointSec active) {
        this.active = active;
    }

    public void setLastPayout(TimePointSec lastPayout) {
        this.lastPayout = lastPayout;
    }

    public void setDepth(short depth) {
        this.depth = depth;
    }

    public void setChildren(int children) {
        this.children = children;
    }

    public void setChildrenRshares2(String childrenRshares2) {
        this.childrenRshares2 = childrenRshares2;
    }

    public void setNetRshares(long netRshares) {
        this.netRshares = netRshares;
    }

    public void setAbsRshares(long absRshares) {
        this.absRshares = absRshares;
    }

    public void setVoteRshares(long voteRshares) {
        this.voteRshares = voteRshares;
    }

    public void setChildrenAbsRshares(long childrenAbsRshares) {
        this.childrenAbsRshares = childrenAbsRshares;
    }

    public void setCashoutTime(TimePointSec cashoutTime) {
        this.cashoutTime = cashoutTime;
    }

    public void setMaxCashoutTime(TimePointSec maxCashoutTime) {
        this.maxCashoutTime = maxCashoutTime;
    }

    public void setTotalVoteWeight(BigInteger totalVoteWeight) {
        this.totalVoteWeight = totalVoteWeight;
    }

    public void setRewardWeight(long rewardWeight) {
        this.rewardWeight = rewardWeight;
    }

    public void setTotalPayoutValue(Asset totalPayoutValue) {
        this.totalPayoutValue = totalPayoutValue;
    }

    public void setCuratorPayoutValue(Asset curatorPayoutValue) {
        this.curatorPayoutValue = curatorPayoutValue;
    }

    public void setAuthorRewards(long authorRewards) {
        this.authorRewards = authorRewards;
    }

    public void setNetVotes(int netVotes) {
        this.netVotes = netVotes;
    }

    public void setRootComment(long rootComment) {
        this.rootComment = rootComment;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public void setMaxAcceptedPayout(Asset maxAcceptedPayout) {
        this.maxAcceptedPayout = maxAcceptedPayout;
    }

    public void setPercentSteemDollars(int percentSteemDollars) {
        this.percentSteemDollars = percentSteemDollars;
    }

    public void setAllowReplies(Boolean allowReplies) {
        this.allowReplies = allowReplies;
    }

    public void setAllowVotes(Boolean allowVotes) {
        this.allowVotes = allowVotes;
    }

    public void setAllowCurationRewards(Boolean allowCurationRewards) {
        this.allowCurationRewards = allowCurationRewards;
    }

    public void setBeneficiaries(List<Beneficiary> beneficiaries) {
        this.beneficiaries = beneficiaries;
    }

    /**
     * @return the id
     */
    public long getId() {
        return id;
    }

    /**
     * @return the category
     *
     */
    public String getCategory() {
        return category;
    }

    /**
     * @return the parentAuthor
     */
    public AccountName getParentAuthor() {
        return parentAuthor;
    }

    /**
     * @return the parentPermlink
     */
    public Permlink getParentPermlink() {
        return parentPermlink;
    }

    /**
     * @return the author
     */
    public AccountName getAuthor() {
        return author;
    }

    /**
     * @return the permlink
     */
    public Permlink getPermlink() {
        return permlink;
    }

    /**
     * @return the title
     */
    public String getTitle() {
        return title;
    }

    /**
     * @return the body
     */
    public String getBody() {
        return body;
    }

    /**
     * @return the jsonMetadata
     */
    public String getJsonMetadata() {
        return jsonMetadata;
    }

    /**
     * @return the lastUpdate
     */
    public TimePointSec getLastUpdate() {
        return lastUpdate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Comment comment = (Comment) o;

        if (id != comment.id) return false;
        if (depth != comment.depth) return false;
        if (children != comment.children) return false;
        if (netRshares != comment.netRshares) return false;
        if (absRshares != comment.absRshares) return false;
        if (voteRshares != comment.voteRshares) return false;
        if (childrenAbsRshares != comment.childrenAbsRshares) return false;
        if (rewardWeight != comment.rewardWeight) return false;
        if (authorRewards != comment.authorRewards) return false;
        if (netVotes != comment.netVotes) return false;
        if (rootComment != comment.rootComment) return false;
        if (percentSteemDollars != comment.percentSteemDollars) return false;
        if (category != null ? !category.equals(comment.category) : comment.category != null)
            return false;
        if (parentAuthor != null ? !parentAuthor.equals(comment.parentAuthor) : comment.parentAuthor != null)
            return false;
        if (parentPermlink != null ? !parentPermlink.equals(comment.parentPermlink) : comment.parentPermlink != null)
            return false;
        if (author != null ? !author.equals(comment.author) : comment.author != null) return false;
        if (permlink != null ? !permlink.equals(comment.permlink) : comment.permlink != null)
            return false;
        if (title != null ? !title.equals(comment.title) : comment.title != null) return false;
        if (body != null ? !body.equals(comment.body) : comment.body != null) return false;
        if (jsonMetadata != null ? !jsonMetadata.equals(comment.jsonMetadata) : comment.jsonMetadata != null)
            return false;
        if (lastUpdate != null ? !lastUpdate.equals(comment.lastUpdate) : comment.lastUpdate != null)
            return false;
        if (created != null ? !created.equals(comment.created) : comment.created != null)
            return false;
        if (active != null ? !active.equals(comment.active) : comment.active != null) return false;
        if (lastPayout != null ? !lastPayout.equals(comment.lastPayout) : comment.lastPayout != null)
            return false;
        if (childrenRshares2 != null ? !childrenRshares2.equals(comment.childrenRshares2) : comment.childrenRshares2 != null)
            return false;
        if (cashoutTime != null ? !cashoutTime.equals(comment.cashoutTime) : comment.cashoutTime != null)
            return false;
        if (maxCashoutTime != null ? !maxCashoutTime.equals(comment.maxCashoutTime) : comment.maxCashoutTime != null)
            return false;
        if (totalVoteWeight != null ? !totalVoteWeight.equals(comment.totalVoteWeight) : comment.totalVoteWeight != null)
            return false;
        if (totalPayoutValue != null ? !totalPayoutValue.equals(comment.totalPayoutValue) : comment.totalPayoutValue != null)
            return false;
        if (curatorPayoutValue != null ? !curatorPayoutValue.equals(comment.curatorPayoutValue) : comment.curatorPayoutValue != null)
            return false;
        if (mode != null ? !mode.equals(comment.mode) : comment.mode != null) return false;
        if (maxAcceptedPayout != null ? !maxAcceptedPayout.equals(comment.maxAcceptedPayout) : comment.maxAcceptedPayout != null)
            return false;
        if (allowReplies != null ? !allowReplies.equals(comment.allowReplies) : comment.allowReplies != null)
            return false;
        if (allowVotes != null ? !allowVotes.equals(comment.allowVotes) : comment.allowVotes != null)
            return false;
        if (allowCurationRewards != null ? !allowCurationRewards.equals(comment.allowCurationRewards) : comment.allowCurationRewards != null)
            return false;
        if (beneficiaries != null ? !beneficiaries.equals(comment.beneficiaries) : comment.beneficiaries != null)
            return false;
        return activeVotesCount != null ? activeVotesCount.equals(comment.activeVotesCount) : comment.activeVotesCount == null;
    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + (category != null ? category.hashCode() : 0);
        result = 31 * result + (parentAuthor != null ? parentAuthor.hashCode() : 0);
        result = 31 * result + (parentPermlink != null ? parentPermlink.hashCode() : 0);
        result = 31 * result + (author != null ? author.hashCode() : 0);
        result = 31 * result + (permlink != null ? permlink.hashCode() : 0);
        result = 31 * result + (title != null ? title.hashCode() : 0);
        result = 31 * result + (body != null ? body.hashCode() : 0);
        result = 31 * result + (jsonMetadata != null ? jsonMetadata.hashCode() : 0);
        result = 31 * result + (lastUpdate != null ? lastUpdate.hashCode() : 0);
        result = 31 * result + (created != null ? created.hashCode() : 0);
        result = 31 * result + (active != null ? active.hashCode() : 0);
        result = 31 * result + (lastPayout != null ? lastPayout.hashCode() : 0);
        result = 31 * result + (int) depth;
        result = 31 * result + children;
        result = 31 * result + (childrenRshares2 != null ? childrenRshares2.hashCode() : 0);
        result = 31 * result + (int) (netRshares ^ (netRshares >>> 32));
        result = 31 * result + (int) (absRshares ^ (absRshares >>> 32));
        result = 31 * result + (int) (voteRshares ^ (voteRshares >>> 32));
        result = 31 * result + (int) (childrenAbsRshares ^ (childrenAbsRshares >>> 32));
        result = 31 * result + (cashoutTime != null ? cashoutTime.hashCode() : 0);
        result = 31 * result + (maxCashoutTime != null ? maxCashoutTime.hashCode() : 0);
        result = 31 * result + (totalVoteWeight != null ? totalVoteWeight.hashCode() : 0);
        result = 31 * result + (int) (rewardWeight ^ (rewardWeight >>> 32));
        result = 31 * result + (totalPayoutValue != null ? totalPayoutValue.hashCode() : 0);
        result = 31 * result + (curatorPayoutValue != null ? curatorPayoutValue.hashCode() : 0);
        result = 31 * result + (int) (authorRewards ^ (authorRewards >>> 32));
        result = 31 * result + netVotes;
        result = 31 * result + (int) (rootComment ^ (rootComment >>> 32));
        result = 31 * result + (mode != null ? mode.hashCode() : 0);
        result = 31 * result + (maxAcceptedPayout != null ? maxAcceptedPayout.hashCode() : 0);
        result = 31 * result + percentSteemDollars;
        result = 31 * result + (allowReplies != null ? allowReplies.hashCode() : 0);
        result = 31 * result + (allowVotes != null ? allowVotes.hashCode() : 0);
        result = 31 * result + (allowCurationRewards != null ? allowCurationRewards.hashCode() : 0);
        result = 31 * result + (beneficiaries != null ? beneficiaries.hashCode() : 0);
        result = 31 * result + (activeVotesCount != null ? activeVotesCount.hashCode() : 0);
        return result;
    }

    /**
     * @return the beneficiaries
     */
    @Override
    public String toString() {
        return "Comment{" +
                "id=" + id +
                ", category='" + category + '\'' +
                ", parentAuthor=" + parentAuthor +
                ", parentPermlink=" + parentPermlink +
                ", author=" + author +
                ", permlink=" + permlink +
                ", title='" + title + '\'' +
                ", jsonMetadata='" + jsonMetadata + '\'' +
                '}';
    }

    /**
     * @return the created
     */
    public TimePointSec getCreated() {
        return created;
    }

    /**
     * @return the active
     */
    public TimePointSec getActive() {
        return active;
    }

    /**
     * @return the lastPayout
     */
    public TimePointSec getLastPayout() {
        return lastPayout;
    }

    /**
     * @return the depth
     */
    public short getDepth() {
        return depth;
    }

    /**
     * @return the children
     */
    public int getChildren() {
        return children;
    }

    /**
     * @return the childrenRshares2
     */
    public String getChildrenRshares2() {
        return childrenRshares2;
    }

    /**
     * @return the netRshares
     */
    public long getNetRshares() {
        return netRshares;
    }

    /**
     * @return the absRshares
     */
    public long getAbsRshares() {
        return absRshares;
    }

    /**
     * @return the voteRshares
     */
    public long getVoteRshares() {
        return voteRshares;
    }

    /**
     * @return the childrenAbsRshares
     */
    public long getChildrenAbsRshares() {
        return childrenAbsRshares;
    }

    /**
     * @return the cashoutTime
     */
    public TimePointSec getCashoutTime() {
        return cashoutTime;
    }

    /**
     * @return the maxCashoutTime
     */
    public TimePointSec getMaxCashoutTime() {
        return maxCashoutTime;
    }

    /**
     * @return the totalVoteWeight
     */
    public BigInteger getTotalVoteWeight() {
        return totalVoteWeight;
    }

    /**
     * @return the rewardWeight
     */
    public long getRewardWeight() {
        return rewardWeight;
    }

    /**
     * @return the totalPayoutValue
     */
    public Asset getTotalPayoutValue() {
        return totalPayoutValue;
    }

    /**
     * @return the curatorPayoutValue
     */
    public Asset getCuratorPayoutValue() {
        return curatorPayoutValue;
    }

    /**
     * @return the authorRewards
     */
    public long getAuthorRewards() {
        return authorRewards;
    }

    /**
     * @return the netVotes
     */
    public int getNetVotes() {
        return netVotes;
    }

    /**
     * @return the rootComment
     */
    public long getRootComment() {
        return rootComment;
    }

    /**
     * @return the mode
     */
    public String getMode() {
        return mode;
    }

    /**
     * @return the maxAcceptedPayout
     */
    public Asset getMaxAcceptedPayout() {
        return maxAcceptedPayout;
    }

    /**
     * @return the percentSteemDollars
     */
    public int getPercentSteemDollars() {
        return percentSteemDollars;
    }

    /**
     * @return the allowReplies
     */
    public Boolean getAllowReplies() {
        return allowReplies;
    }

    /**
     * @return the allowVotes
     */
    public Boolean getAllowVotes() {
        return allowVotes;
    }

    /**
     * @return the allowCurationRewards
     */
    public Boolean getAllowCurationRewards() {
        return allowCurationRewards;
    }

    public List<Beneficiary> getBeneficiaries() {
        return beneficiaries;
    }

}
