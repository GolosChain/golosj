package eu.bittrade.libs.steemj;


import eu.bittrade.libs.steemj.apis.follow.enums.FollowType;
import eu.bittrade.libs.steemj.apis.follow.model.*;
import eu.bittrade.libs.steemj.base.models.AccountName;
import eu.bittrade.libs.steemj.base.models.Permlink;
import eu.bittrade.libs.steemj.communication.CommunicationHandler;
import eu.bittrade.libs.steemj.configuration.SteemJConfig;
import eu.bittrade.libs.steemj.exceptions.SteemCommunicationException;

import javax.annotation.Nonnull;
import java.util.List;

/**
 * Created by yuri yurivladdurain@gmail.com .
 */

@SuppressWarnings("unused")
class GolosFollowApiMethodsHandler implements FollowApiMethods {
    @Nonnull
    private final SteemJConfig config;
    @Nonnull
    private final CommunicationHandler communicationHandler;
    @Nonnull
    private SteemJ steemJ;

    GolosFollowApiMethodsHandler(@Nonnull SteemJConfig config,
                                 @Nonnull CommunicationHandler communicationHandler,
                                 @Nonnull SteemJ steemJ) {
        this.config = config;
        this.communicationHandler = communicationHandler;
        this.steemJ = steemJ;
    }
    @Override
    public List<FollowApiObject> getFollowers(AccountName following, AccountName startFollower, FollowType type,
                                              short limit) throws SteemCommunicationException {
        return steemJ.getFollowers(following, startFollower, type, limit);
    }
    @Override
    public List<FollowApiObject> getFollowing(AccountName following, AccountName startFollower, FollowType type,
                                              short limit) throws SteemCommunicationException {
        return steemJ.getFollowing(following, startFollower, type, limit);
    }

    @Override
    public FollowCountApiObject getFollowCount(AccountName account) throws SteemCommunicationException {
        return steemJ.getFollowCount(account);
    }

    @Override
    public List<FeedEntry> getFeedEntries(AccountName account, int entryId, short limit)
            throws SteemCommunicationException {
        return steemJ.getFeedEntries(account, entryId, limit);
    }
    @Override
    public List<CommentFeedEntry> getFeed(AccountName account, int entryId, short limit)
            throws SteemCommunicationException {
        return steemJ.getFeed(account, entryId, limit);
    }

    @Override
    public List<BlogEntry> getBlogEntries(AccountName account, int entryId, short limit)
            throws SteemCommunicationException {
        return steemJ.getBlogEntries(account, entryId, limit);

    }
    @Override
    public List<CommentBlogEntry> getBlog(AccountName account, int entryId, short limit)
            throws SteemCommunicationException {
        return steemJ.getBlog(account, entryId, limit);
    }
    @Override
    public List<AccountReputation> getAccountReputations(AccountName accountName, int limit)
            throws SteemCommunicationException {
        return steemJ.getAccountReputations(accountName, limit);
    }

    @Override
    public List<AccountName> getRebloggedBy(AccountName author, Permlink permlink) throws SteemCommunicationException {
        return steemJ.getRebloggedBy(author, permlink);
    }

    @Override
    public List<PostsPerAuthorPair> getBlogAuthors(AccountName blogAccount) throws SteemCommunicationException {
        return steemJ.getBlogAuthors(blogAccount);
    }
}
