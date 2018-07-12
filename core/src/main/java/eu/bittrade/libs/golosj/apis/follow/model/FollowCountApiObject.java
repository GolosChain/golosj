package eu.bittrade.libs.golosj.apis.follow.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import eu.bittrade.libs.golosj.base.models.AccountName;
import jdk.nashorn.internal.ir.annotations.Ignore;

/**
 * This class represents a Steem "follow_count_api_object" object.
 *
 * @author <a href="http://steemit.com/@dez1337">dez1337</a>
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class FollowCountApiObject {
    @JsonProperty("account")
    private AccountName account;
    // Original type is uint32_t.
    @JsonProperty("follower_count")
    private int followerCount;
    // Original type is uint32_t.
    @JsonProperty("following_count")
    private int followingCount;

    /**
     * @return The account which the {@link #getFollowerCount()
     * getFollowerCount()} and {@link #getFollowingCount()
     * getFollowingCount()} results belong to.
     */
    public AccountName getAccount() {
        return account;
    }

    /**
     * @return The number of accounts following the {@link #getAccount()
     * getAccount()}.
     */
    public int getFollowerCount() {
        return followerCount;
    }

    /**
     * @return The number of accounts the {@link #getAccount() getAccount()}
     * account is following.
     */
    public int getFollowingCount() {
        return followingCount;
    }

    @Override
    public String toString() {
        return "FollowCountApiObject{" +
                "account=" + account +
                ", followerCount=" + followerCount +
                ", followingCount=" + followingCount +
                '}';
    }
}
