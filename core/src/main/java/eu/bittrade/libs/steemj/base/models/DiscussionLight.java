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
    public String url;
    @JsonProperty("root_title")
    public String rootTitle;
    @JsonProperty("pending_payout_value")
    public Asset pendingPayoutValue;
    @JsonProperty("total_pending_payout_value")
    public Asset totalPendingPayoutValue;
    // Original type is vector<vote_state>.
    @JsonProperty("active_votes")
    @JsonDeserialize(using = VoteLightDeserializer.class)
    public List<VoteLight> votes;
    @JsonProperty("author_reputation")
    public long authorReputation;
    @JsonProperty("body_length")
    public Long bodyLength;
    @JsonProperty("reblogged_by")
    public List<String> rebloggedBy;
    @JsonProperty("first_reblogged_by")
    @Nullable
    public String firstRebloggedBy;
    @JsonProperty("first_reblogged_on")
    @Nullable
    public Date firstRebloggedOn;
    @JsonProperty("vote_rshares")
    public long voteRshares;
}
