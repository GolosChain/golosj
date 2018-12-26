package eu.bittrade.libs.golosj.base.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;

import javax.annotation.Nullable;

/**
 * Created by yuri on 27.11.17.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class CommentLight {
    // Original type is comment_id_type.
    long id;


    String category;
    @JsonProperty("parent_author")
    String parentAuthor;
    @JsonProperty("parent_permlink")
    String parentPermlink;
    String author;
    String permlink;
    String title;
    String body;
    @JsonProperty("json_metadata")
    String jsonMetadata;
    TimePointSec created;
    // Original type is uint8_t.
    short depth;
    // Orignal type is uint32_t.
    int children;
    Asset totalPayoutValue;
    @JsonProperty("author_rewards")
    long authorRewards;//curator_payout_value
    // Original type is int32_t.
    @JsonProperty("net_votes")
    int netVotes;

    @JsonProperty("curator_payout_value")
    Asset curatorPayoutValue;

    @Deprecated
    String mode;
    @JsonProperty("percent_steem_dollars")
    int percentSteemDollars;
    @JsonProperty("allow_replies")
    Boolean allowReplies;
    @JsonProperty("allow_votes")
    Boolean allowVotes;
    @JsonProperty("allow_curation_rewards")
    Boolean allowCurationRewards;
    @JsonProperty("last_update")
    TimePointSec lastUpdate;
    @JsonProperty("net_rshares")
    long netRshares;
    @JsonProperty("active_votes_count")
    @Nullable
    Long activeVotesCount;

    public CommentLight() {
    }

    public CommentLight(long id, String category, String parentAuthor, String parentPermlink, String author, String permlink, String title, String body, String jsonMetadata, TimePointSec created, short depth, int children, Asset totalPayoutValue, long authorRewards, int netVotes, Asset curatorPayoutValue, String mode, int percentSteemDollars, Boolean allowReplies, Boolean allowVotes, Boolean allowCurationRewards, TimePointSec lastUpdate, long netRshares, @Nullable Long activeVotesCount) {
        this.id = id;
        this.category = category;
        this.parentAuthor = parentAuthor;
        this.parentPermlink = parentPermlink;
        this.author = author;
        this.permlink = permlink;
        this.title = title;
        this.body = body;
        this.jsonMetadata = jsonMetadata;
        this.created = created;
        this.depth = depth;
        this.children = children;
        this.totalPayoutValue = totalPayoutValue;
        this.authorRewards = authorRewards;
        this.netVotes = netVotes;
        this.curatorPayoutValue = curatorPayoutValue;
        this.mode = mode;
        this.percentSteemDollars = percentSteemDollars;
        this.allowReplies = allowReplies;
        this.allowVotes = allowVotes;
        this.allowCurationRewards = allowCurationRewards;
        this.lastUpdate = lastUpdate;
        this.netRshares = netRshares;
        this.activeVotesCount = activeVotesCount;
    }

    public Asset getCuratorPayoutValue() {
        return curatorPayoutValue;
    }

    public void setCuratorPayoutValue(Asset curatorPayoutValue) {
        this.curatorPayoutValue = curatorPayoutValue;
    }

    @Nullable
    public Long getActiveVotesCount() {
        return activeVotesCount;
    }

    public void setActiveVotesCount(@Nullable Long activeVotesCount) {
        this.activeVotesCount = activeVotesCount;
    }

    public void setActiveVotesCount(long activeVotesCount) {
        this.activeVotesCount = activeVotesCount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CommentLight that = (CommentLight) o;
        return id == that.id &&
                depth == that.depth &&
                children == that.children &&
                authorRewards == that.authorRewards &&
                netVotes == that.netVotes &&
                percentSteemDollars == that.percentSteemDollars &&
                netRshares == that.netRshares &&
                Objects.equals(category, that.category) &&
                Objects.equals(parentAuthor, that.parentAuthor) &&
                Objects.equals(parentPermlink, that.parentPermlink) &&
                Objects.equals(author, that.author) &&
                Objects.equals(permlink, that.permlink) &&
                Objects.equals(title, that.title) &&
                Objects.equals(body, that.body) &&
                Objects.equals(jsonMetadata, that.jsonMetadata) &&
                Objects.equals(created, that.created) &&
                Objects.equals(totalPayoutValue, that.totalPayoutValue) &&
                Objects.equals(curatorPayoutValue, that.curatorPayoutValue) &&
                Objects.equals(mode, that.mode) &&
                Objects.equals(allowReplies, that.allowReplies) &&
                Objects.equals(allowVotes, that.allowVotes) &&
                Objects.equals(allowCurationRewards, that.allowCurationRewards) &&
                Objects.equals(lastUpdate, that.lastUpdate) &&
                Objects.equals(activeVotesCount, that.activeVotesCount);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, category, parentAuthor, parentPermlink, author, permlink, title, body, jsonMetadata, created, depth, children, totalPayoutValue, authorRewards, netVotes, curatorPayoutValue, mode, percentSteemDollars, allowReplies, allowVotes, allowCurationRewards, lastUpdate, netRshares, activeVotesCount);
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getParentAuthor() {
        return parentAuthor;
    }

    public void setParentAuthor(String parentAuthor) {
        this.parentAuthor = parentAuthor;
    }

    public String getParentPermlink() {
        return parentPermlink;
    }

    public void setParentPermlink(String parentPermlink) {
        this.parentPermlink = parentPermlink;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getPermlink() {
        return permlink;
    }

    public void setPermlink(String permlink) {
        this.permlink = permlink;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getJsonMetadata() {
        return jsonMetadata;
    }

    public void setJsonMetadata(String jsonMetadata) {
        this.jsonMetadata = jsonMetadata;
    }

    public TimePointSec getCreated() {
        return created;
    }

    public void setCreated(TimePointSec created) {
        this.created = created;
    }

    public short getDepth() {
        return depth;
    }

    public void setDepth(short depth) {
        this.depth = depth;
    }

    public int getChildren() {
        return children;
    }

    public void setChildren(int children) {
        this.children = children;
    }

    public Asset getTotalPayoutValue() {
        return totalPayoutValue;
    }

    public void setTotalPayoutValue(Asset totalPayoutValue) {
        this.totalPayoutValue = totalPayoutValue;
    }

    public long getAuthorRewards() {
        return authorRewards;
    }

    public void setAuthorRewards(long authorRewards) {
        this.authorRewards = authorRewards;
    }

    public int getNetVotes() {
        return netVotes;
    }

    public void setNetVotes(int netVotes) {
        this.netVotes = netVotes;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public int getPercentSteemDollars() {
        return percentSteemDollars;
    }

    public void setPercentSteemDollars(int percentSteemDollars) {
        this.percentSteemDollars = percentSteemDollars;
    }

    public Boolean getAllowReplies() {
        return allowReplies;
    }

    public void setAllowReplies(Boolean allowReplies) {
        this.allowReplies = allowReplies;
    }

    public Boolean getAllowVotes() {
        return allowVotes;
    }

    public void setAllowVotes(Boolean allowVotes) {
        this.allowVotes = allowVotes;
    }

    public Boolean getAllowCurationRewards() {
        return allowCurationRewards;
    }

    public void setAllowCurationRewards(Boolean allowCurationRewards) {
        this.allowCurationRewards = allowCurationRewards;
    }

    public TimePointSec getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(TimePointSec lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    public long getNetRshares() {
        return netRshares;
    }

    public void setNetRshares(long netRshares) {
        this.netRshares = netRshares;
    }

    @Override
    public String toString() {
        return "CommentLight{" +
                "id=" + id +
                ", category='" + category + '\'' +
                ", parentAuthor='" + parentAuthor + '\'' +
                ", parentPermlink='" + parentPermlink + '\'' +
                ", author='" + author + '\'' +
                ", permlink='" + permlink + '\'' +
                ", title='" + title + '\'' +
                ", body='" + body + '\'' +
                ", jsonMetadata='" + jsonMetadata + '\'' +
                ", created=" + created +
                ", depth=" + depth +
                ", children=" + children +
                ", totalPayoutValue=" + totalPayoutValue +
                ", authorRewards=" + authorRewards +
                ", netVotes=" + netVotes +
                ", curatorPayoutValue=" + curatorPayoutValue +
                ", mode='" + mode + '\'' +
                ", percentSteemDollars=" + percentSteemDollars +
                ", allowReplies=" + allowReplies +
                ", allowVotes=" + allowVotes +
                ", allowCurationRewards=" + allowCurationRewards +
                ", lastUpdate=" + lastUpdate +
                ", netRshares=" + netRshares +
                ", activeVotesCount=" + activeVotesCount +
                '}';
    }
}
