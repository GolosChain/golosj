package eu.bittrade.libs.steemj.base.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.annotation.Nullable;
import java.util.Date;
import java.util.List;

/**
 * This class represents the Steem "discussion" object.
 *
 * @author <a href="http://steemit.com/@dez1337">dez1337</a>
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Discussion extends Comment {
    private String url;
    @JsonProperty("root_title")
    private String rootTitle;
    @JsonProperty("pending_payout_value")
    private Asset pendingPayoutValue;
    @JsonProperty("total_pending_payout_value")
    private Asset totalPendingPayoutValue;
    // Original type is vector<vote_state>.
    @JsonProperty("active_votes")
    private List<VoteState> activeVotes;
    private List<String> replies;
    // Original type is "share_type" which is a "safe<int64_t>".
    @JsonProperty("author_reputation")
    private long authorReputation;
    private Asset promoted;
    // Original type is uint32_t
    @JsonProperty("body_length")
    private String bodyLength;
    @JsonProperty("reblogged_by")
    private List<AccountName> rebloggedBy;
    @JsonProperty("first_reblogged_by")
    @Nullable
    private AccountName firstRebloggedBy;
    @JsonProperty("first_reblogged_on")
    @Nullable
    private Date firstRebloggedOn;

    /**
     * This object is only used to wrap the JSON response in a POJO, so
     * therefore this class should not be instantiated.
     */
    private Discussion() {
    }

    /**
     * @return the url
     */
    public String getUrl() {
        return url;
    }

    /**
     * @return the rootTitle
     */
    public String getRootTitle() {
        return rootTitle;
    }

    /**
     * @return the pendingPayoutValue
     */
    public Asset getPendingPayoutValue() {
        return pendingPayoutValue;
    }

    /**
     * @return the totalPendingPayoutValue
     */
    public Asset getTotalPendingPayoutValue() {
        return totalPendingPayoutValue;
    }

    /**
     * @return the activeVotes
     */
    public List<VoteState> getActiveVotes() {
        return activeVotes;
    }

    /**
     * @return the replies
     */
    public List<String> getReplies() {
        return replies;
    }

    /**
     * @return the authorReputation
     */
    public long getAuthorReputation() {
        return authorReputation;
    }

    /**
     * @return the promoted
     */
    public Asset getPromoted() {
        return promoted;
    }

    /**
     * @return the bodyLength
     */
    public String getBodyLength() {
        return bodyLength;
    }

    /**
     * @return the rebloggedBy
     */
    public List<AccountName> getRebloggedBy() {
        return rebloggedBy;
    }

    /**
     * @return the firstRebloggedBy
     */
    public AccountName getFirstRebloggedBy() {
        return firstRebloggedBy;
    }

    /**
     * @return the firstRebloggedOn
     */
    public Date getFirstRebloggedOn() {
        return firstRebloggedOn;
    }

    @Override
    public String toString() {
        return "Discussion{" +
                "url='" + url + '\'' +
                ", rootTitle='" + rootTitle + '\'' +
                ", pendingPayoutValue=" + pendingPayoutValue +
                ", totalPendingPayoutValue=" + totalPendingPayoutValue +
                ", activeVotes=" + activeVotes +
                ", replies=" + replies +
                ", authorReputation=" + authorReputation +
                ", promoted=" + promoted +
                ", bodyLength='" + bodyLength + '\'' +
                ", rebloggedBy=" + rebloggedBy +
                ", firstRebloggedBy=" + firstRebloggedBy +
                ", firstRebloggedOn=" + firstRebloggedOn +
                '}';
    }
}
