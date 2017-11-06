package eu.bittrade.libs.steemj.base.models;

/**
 * Created by yuri on 06.11.17.
 */

public class Route {
    private String blogName;
    private AccountName authorName;
    private Permlink permlink;

    public Route(String blogName, AccountName authorName, Permlink permlink) {
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

    public String constructRoute(){
        return blogName + "/@" + authorName.getName() + "/" + permlink.getLink();
    }
}
