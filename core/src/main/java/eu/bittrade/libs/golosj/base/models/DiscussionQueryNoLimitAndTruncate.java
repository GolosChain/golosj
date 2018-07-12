package eu.bittrade.libs.golosj.base.models;

/**
 * Created by yuri on 08.12.17.
 */

public class DiscussionQueryNoLimitAndTruncate extends DiscussionQuery {
    public DiscussionQueryNoLimitAndTruncate() {
        this.setLimit(null);
        this.setTruncateBody(null);
    }
}
