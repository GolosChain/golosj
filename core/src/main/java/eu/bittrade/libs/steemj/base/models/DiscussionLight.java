package eu.bittrade.libs.steemj.base.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.Date;
import java.util.List;

import javax.annotation.Nullable;

import eu.bittrade.libs.steemj.base.models.deserializer.VoteLightDeserializer;
import eu.bittrade.libs.steemj.base.models.deserializer.VotesDeseriaizer;
import eu.bittrade.libs.steemj.util.ArrayMap;

/**
 * Created by yuri on 27.11.17.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class DiscussionLight extends CommentLight {
    String url;
    @JsonProperty("root_title")
    String rootTitle;
    @JsonProperty("pending_payout_value")
    Asset pendingPayoutValue;
    @JsonProperty("total_pending_payout_value")
    Asset totalPendingPayoutValue;
    @JsonProperty("active_votes")
    @JsonDeserialize(using = VoteLightDeserializer.class)
    List<VoteLight> votes;
    @JsonProperty("author_reputation")
    long authorReputation;
    @JsonProperty("body_length")
    Long bodyLength;
    @JsonProperty("reblogged_by")
    List<String> rebloggedBy;
    @JsonProperty("first_reblogged_by")
    @Nullable
    String firstRebloggedBy;
    @JsonProperty("first_reblogged_on")
    @Nullable
    Date firstRebloggedOn;
    @JsonProperty("vote_rshares")
    public long voteRshares;

    public DiscussionLight() {
    }

    public DiscussionLight(long id, String category, String parentAuthor, String parentPermlink, String author, String permlink, String title, String body, String jsonMetadata, TimePointSec created, short depth, int children, Asset totalPayoutValue, long authorRewards, int netVotes, String mode, int percentSteemDollars, Boolean allowReplies, Boolean allowVotes, Boolean allowCurationRewards, TimePointSec lastUpdate, Long activeVotesCount, String url, String rootTitle, Asset pendingPayoutValue, Asset totalPendingPayoutValue, List<VoteLight> votes, long authorReputation, Long bodyLength, List<String> rebloggedBy, String firstRebloggedBy, Date firstRebloggedOn, long voteRshares) {
        super(id, category, parentAuthor, parentPermlink, author, permlink, title, body, jsonMetadata, created, depth, children, totalPayoutValue, authorRewards, netVotes, mode, percentSteemDollars, allowReplies, allowVotes, allowCurationRewards, lastUpdate, activeVotesCount);
        this.url = url;
        this.rootTitle = rootTitle;
        this.pendingPayoutValue = pendingPayoutValue;
        this.totalPendingPayoutValue = totalPendingPayoutValue;
        this.votes = votes;
        this.authorReputation = authorReputation;
        this.bodyLength = bodyLength;
        this.rebloggedBy = rebloggedBy;
        this.firstRebloggedBy = firstRebloggedBy;
        this.firstRebloggedOn = firstRebloggedOn;
        this.voteRshares = voteRshares;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getRootTitle() {
        return rootTitle;
    }

    public void setRootTitle(String rootTitle) {
        this.rootTitle = rootTitle;
    }

    public Asset getPendingPayoutValue() {
        return pendingPayoutValue;
    }

    public void setPendingPayoutValue(Asset pendingPayoutValue) {
        this.pendingPayoutValue = pendingPayoutValue;
    }

    public Asset getTotalPendingPayoutValue() {
        return totalPendingPayoutValue;
    }

    public void setTotalPendingPayoutValue(Asset totalPendingPayoutValue) {
        this.totalPendingPayoutValue = totalPendingPayoutValue;
    }

    public List<VoteLight> getVotes() {
        return votes;
    }

    public void setVotes(List<VoteLight> votes) {
        this.votes = votes;
    }

    public long getAuthorReputation() {
        return authorReputation;
    }

    public void setAuthorReputation(long authorReputation) {
        this.authorReputation = authorReputation;
    }

    public Long getBodyLength() {
        return bodyLength;
    }

    public void setBodyLength(Long bodyLength) {
        this.bodyLength = bodyLength;
    }

    public List<String> getRebloggedBy() {
        return rebloggedBy;
    }

    public void setRebloggedBy(List<String> rebloggedBy) {
        this.rebloggedBy = rebloggedBy;
    }

    @Nullable
    public String getFirstRebloggedBy() {
        return firstRebloggedBy;
    }

    public void setFirstRebloggedBy(@Nullable String firstRebloggedBy) {
        this.firstRebloggedBy = firstRebloggedBy;
    }

    @Nullable
    public Date getFirstRebloggedOn() {
        return firstRebloggedOn;
    }

    public void setFirstRebloggedOn(@Nullable Date firstRebloggedOn) {
        this.firstRebloggedOn = firstRebloggedOn;
    }

    public long getVoteRshares() {
        return voteRshares;
    }

    public void setVoteRshares(long voteRshares) {
        this.voteRshares = voteRshares;
    }



    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        DiscussionLight that = (DiscussionLight) o;

        if (authorReputation != that.authorReputation) return false;
        if (voteRshares != that.voteRshares) return false;
        if (url != null ? !url.equals(that.url) : that.url != null) return false;
        if (rootTitle != null ? !rootTitle.equals(that.rootTitle) : that.rootTitle != null) return false;
        if (pendingPayoutValue != null ? !pendingPayoutValue.equals(that.pendingPayoutValue) : that.pendingPayoutValue != null)
            return false;
        if (totalPendingPayoutValue != null ? !totalPendingPayoutValue.equals(that.totalPendingPayoutValue) : that.totalPendingPayoutValue != null)
            return false;
        if (votes != null ? !votes.equals(that.votes) : that.votes != null) return false;
        if (bodyLength != null ? !bodyLength.equals(that.bodyLength) : that.bodyLength != null) return false;
        if (rebloggedBy != null ? !rebloggedBy.equals(that.rebloggedBy) : that.rebloggedBy != null) return false;
        if (firstRebloggedBy != null ? !firstRebloggedBy.equals(that.firstRebloggedBy) : that.firstRebloggedBy != null)
            return false;
        return firstRebloggedOn != null ? firstRebloggedOn.equals(that.firstRebloggedOn) : that.firstRebloggedOn == null;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (url != null ? url.hashCode() : 0);
        result = 31 * result + (rootTitle != null ? rootTitle.hashCode() : 0);
        result = 31 * result + (pendingPayoutValue != null ? pendingPayoutValue.hashCode() : 0);
        result = 31 * result + (totalPendingPayoutValue != null ? totalPendingPayoutValue.hashCode() : 0);
        result = 31 * result + (votes != null ? votes.hashCode() : 0);
        result = 31 * result + (int) (authorReputation ^ (authorReputation >>> 32));
        result = 31 * result + (bodyLength != null ? bodyLength.hashCode() : 0);
        result = 31 * result + (rebloggedBy != null ? rebloggedBy.hashCode() : 0);
        result = 31 * result + (firstRebloggedBy != null ? firstRebloggedBy.hashCode() : 0);
        result = 31 * result + (firstRebloggedOn != null ? firstRebloggedOn.hashCode() : 0);
        result = 31 * result + (int) (voteRshares ^ (voteRshares >>> 32));
        return result;
    }

    @Override
    public String toString() {
        return "DiscussionLight{" +
                "id=" + id +
                ", category='" + category + '\'' +
                ", parentAuthor='" + parentAuthor + '\'' +
                ", parentPermlink='" + parentPermlink + '\'' +
                ", author='" + author + '\'' +
                ", permlink='" + permlink + '\'' +
                ", title='" + title + '\'' +
                ", url='" + url + '\'' +
                ", depth=" + depth +
                ", rootTitle='" + rootTitle + '\'' +
                ", children=" + children +
                ", bodyLength=" + bodyLength +
                '}';
    }
}
