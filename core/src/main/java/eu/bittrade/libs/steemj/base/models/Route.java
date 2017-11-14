package eu.bittrade.libs.steemj.base.models;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Created by yuri on 06.11.17.
 */

public class Route {
    @Nullable
    private String blogName;
    private AccountName authorName;
    @Nullable
    private Permlink permlink;

    public Route(@Nullable String blogName, AccountName authorName, @Nullable Permlink permlink) {
        this.blogName = blogName;
        this.authorName = authorName;
        this.permlink = permlink;
    }

    public String getBlogName() {
        return blogName;
    }

    public AccountName getAuthorName() {
        return authorName;
    }

    public Permlink getPermlink() {
        return permlink;
    }

    @Nonnull
    public String constructDiscussionRoute() {
        if (blogName == null || permlink == null)
            throw new IllegalStateException("blogname and permlink must be not null for discussion route");
        return blogName.replace("-", "--") + "/@" + authorName.getName() + "/" + permlink.getLink();
    }

    @Nonnull
    public String constructBlogRoute() {
        return "@" + authorName.getName() + "/feed";
    }
}
