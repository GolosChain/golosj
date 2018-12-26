package eu.bittrade.libs.golosj.apis.follow.model;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import eu.bittrade.libs.golosj.base.models.AccountName;
import eu.bittrade.libs.golosj.base.models.Permlink;
import eu.bittrade.libs.golosj.base.models.TimePointSec;

/**
 * This class represents a Steem "blog_entry" object.
 * 
 * @author <a href="http://steemit.com/@dez1337">dez1337</a>
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class BlogEntry {
    private AccountName author;
    private Permlink permlink;
    private AccountName blog;
    @JsonProperty("reblog_on")
    private TimePointSec reblogOn;
    // Original type is uint32_t.
    @JsonProperty("entry_id")
    private int entryId;

    /**
     * @return The author of the post.
     */
    public AccountName getAuthor() {
        return author;
    }

    /**
     * @return The permlink of the post.
     */
    public Permlink getPermlink() {
        return permlink;
    }

    /**
     * Get the account name of the blog owner.
     * 
     * @return The account name of the blog owner.
     */
    public AccountName getBlog() {
        return blog;
    }

    /**
     * In case this blog entry is not written by the blog owner, but was
     * resteemed by the blog owner, this field contains the date when the blog
     * owner has resteemed this post. If the entry has not been resteemed, the
     * timestamp is set to 0 which results in <code>1970-01-01T00:00:00</code>
     * as the date.
     * 
     * @return The date when the blog entry has been restemmed by the blog
     *         owner.
     */
    public TimePointSec getReblogOn() {
        return reblogOn;
    }

    /**
     * Each blog entry has an id. The first posted or resteemed post of a blog
     * owner has the id 0. This id is incremented for each new post or resteem.
     * 
     * @return The id of the blog entry.
     */
    public int getEntryId() {
        return entryId;
    }

    @Override
    public String toString() {
        return "BlogEntry{" +
                "author=" + author +
                ", permlink=" + permlink +
                ", blog=" + blog +
                ", reblogOn=" + reblogOn +
                ", entryId=" + entryId +
                '}';
    }
}
