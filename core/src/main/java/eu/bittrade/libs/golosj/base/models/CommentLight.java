package eu.bittrade.libs.golosj.base.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

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
    long authorRewards;
    // Original type is int32_t.
    @JsonProperty("net_votes")
    int netVotes;
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
    @JsonProperty("active_votes_count")
    @Nullable
    Long activeVotesCount;

    public CommentLight() {
    }


    public CommentLight(long id, String category, String parentAuthor, String parentPermlink, String author, String permlink, String title, String body, String jsonMetadata, TimePointSec created, short depth, int children, Asset totalPayoutValue, long authorRewards, int netVotes, String mode, int percentSteemDollars, Boolean allowReplies, Boolean allowVotes, Boolean allowCurationRewards, TimePointSec lastUpdate, Long activeVotesCount) {
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
        this.mode = mode;
        this.percentSteemDollars = percentSteemDollars;
        this.allowReplies = allowReplies;
        this.allowVotes = allowVotes;
        this.allowCurationRewards = allowCurationRewards;
        this.lastUpdate = lastUpdate;
        this.activeVotesCount = activeVotesCount;
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

        if (id != that.id) return false;
        if (depth != that.depth) return false;
        if (children != that.children) return false;
        if (authorRewards != that.authorRewards) return false;
        if (netVotes != that.netVotes) return false;
        if (percentSteemDollars != that.percentSteemDollars) return false;
        if (category != null ? !category.equals(that.category) : that.category != null) return false;
        if (parentAuthor != null ? !parentAuthor.equals(that.parentAuthor) : that.parentAuthor != null) return false;
        if (parentPermlink != null ? !parentPermlink.equals(that.parentPermlink) : that.parentPermlink != null)
            return false;
        if (author != null ? !author.equals(that.author) : that.author != null) return false;
        if (permlink != null ? !permlink.equals(that.permlink) : that.permlink != null) return false;
        if (title != null ? !title.equals(that.title) : that.title != null) return false;
        if (body != null ? !body.equals(that.body) : that.body != null) return false;
        if (jsonMetadata != null ? !jsonMetadata.equals(that.jsonMetadata) : that.jsonMetadata != null) return false;
        if (created != null ? !created.equals(that.created) : that.created != null) return false;
        if (totalPayoutValue != null ? !totalPayoutValue.equals(that.totalPayoutValue) : that.totalPayoutValue != null)
            return false;
        if (mode != null ? !mode.equals(that.mode) : that.mode != null) return false;
        if (allowReplies != null ? !allowReplies.equals(that.allowReplies) : that.allowReplies != null) return false;
        if (allowVotes != null ? !allowVotes.equals(that.allowVotes) : that.allowVotes != null) return false;
        if (allowCurationRewards != null ? !allowCurationRewards.equals(that.allowCurationRewards) : that.allowCurationRewards != null)
            return false;
        if (lastUpdate != null ? !lastUpdate.equals(that.lastUpdate) : that.lastUpdate != null) return false;
        return activeVotesCount != null ? activeVotesCount.equals(that.activeVotesCount) : that.activeVotesCount == null;
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
        result = 31 * result + (created != null ? created.hashCode() : 0);
        result = 31 * result + (int) depth;
        result = 31 * result + children;
        result = 31 * result + (totalPayoutValue != null ? totalPayoutValue.hashCode() : 0);
        result = 31 * result + (int) (authorRewards ^ (authorRewards >>> 32));
        result = 31 * result + netVotes;
        result = 31 * result + (mode != null ? mode.hashCode() : 0);
        result = 31 * result + percentSteemDollars;
        result = 31 * result + (allowReplies != null ? allowReplies.hashCode() : 0);
        result = 31 * result + (allowVotes != null ? allowVotes.hashCode() : 0);
        result = 31 * result + (allowCurationRewards != null ? allowCurationRewards.hashCode() : 0);
        result = 31 * result + (lastUpdate != null ? lastUpdate.hashCode() : 0);
        result = 31 * result + (activeVotesCount != null ? activeVotesCount.hashCode() : 0);
        return result;
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
                ", mode='" + mode + '\'' +
                ", percentSteemDollars=" + percentSteemDollars +
                ", allowReplies=" + allowReplies +
                ", allowVotes=" + allowVotes +
                ", allowCurationRewards=" + allowCurationRewards +
                ", lastUpdate=" + lastUpdate +
                '}';
    }
}
