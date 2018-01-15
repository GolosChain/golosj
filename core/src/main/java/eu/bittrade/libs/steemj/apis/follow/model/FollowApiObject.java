package eu.bittrade.libs.steemj.apis.follow.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

import org.apache.commons.lang3.builder.ToStringBuilder;

import eu.bittrade.libs.steemj.apis.follow.enums.FollowType;
import eu.bittrade.libs.steemj.base.models.AccountName;

/**
 * This class represents a Steem "follow_api_object" object.
 * 
 * @author <a href="http://steemit.com/@dez1337">dez1337</a>
 */
public class FollowApiObject {
    @JsonProperty("follower")
    private AccountName follower;
    @JsonProperty("following")
    private AccountName following;
    @JsonProperty("what")
    private List<FollowType> what;

    /**
     * @return The account which is following {@link #getFollowing()}.
     */
    public AccountName getFollower() {
        return follower;
    }

    /**
     * @return The account which is followed by {@link #getFollower()}.
     */
    public AccountName getFollowing() {
        return following;
    }

    /**
     * @return The variant of the follow (see
     *         {@link eu.bittrade.libs.steemj.apis.follow.enums.FollowType
     *         FollowType}.
     */
    public List<FollowType> getWhat() {
        return what;
    }

    @Override
    public String toString() {
        return "FollowApiObject{" +
                "follower=" + follower +
                ", following=" + following +
                ", what=" + what +
                '}';
    }
}
