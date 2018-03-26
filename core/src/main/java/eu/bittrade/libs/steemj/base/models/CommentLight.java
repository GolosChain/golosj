package eu.bittrade.libs.steemj.base.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by yuri on 27.11.17.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class CommentLight {
    // Original type is comment_id_type.
    public long id;
    public String category;
    @JsonProperty("parent_author")
    public String parentAuthor;
    @JsonProperty("parent_permlink")
    public String parentPermlink;
    public String author;
    public String permlink;
    public String title;
    public String body;
    @JsonProperty("json_metadata")
    public String jsonMetadata;
    public TimePointSec created;
    // Original type is uint8_t.
    public short depth;
    // Orignal type is uint32_t.
    public int children;
    public Asset totalPayoutValue;
    @JsonProperty("author_rewards")
    public long authorRewards;
    // Original type is int32_t.
    @JsonProperty("net_votes")
    public int netVotes;
    @Deprecated
    public String mode;
    @JsonProperty("percent_steem_dollars")
    int percentSteemDollars;
    @JsonProperty("allow_replies")
    public Boolean allowReplies;
    @JsonProperty("allow_votes")
    public Boolean allowVotes;
    @JsonProperty("allow_curation_rewards")
    public Boolean allowCurationRewards;
    @JsonProperty("last_update")
    public TimePointSec lastUpdate;

}
