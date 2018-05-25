package eu.bittrade.libs.steemj.apis.follow.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import eu.bittrade.libs.steemj.base.models.AccountName;
import eu.bittrade.libs.steemj.base.models.Comment;
import eu.bittrade.libs.steemj.base.models.TimePointSec;

/**
 * This class represents a Steem "comment_blog_entry" object.
 *
 * @author <a href="http://steemit.com/@dez1337">dez1337</a>
 */
public class CommentBlogEntry {
    private Comment comment;
    private AccountName blog;
    @JsonProperty("reblog_on")
    private TimePointSec reblogOn;
    // Original type is uint32_t.
    @JsonProperty("entry_id")
    private int entryId;

    /**
     * Get the whole content of the blog entry.
     *
     * @return The whole content of the blog entry.
     */
    public Comment getComment() {
        return comment;
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
     * owner.
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
        return "CommentBlogEntry{" +
                "comment=" + comment +
                ", blog=" + blog +
                ", reblogOn=" + reblogOn +
                ", entryId=" + entryId +
                '}';
    }
}
