package eu.bittrade.libs.steemj;


import eu.bittrade.libs.steemj.apis.follow.enums.FollowType;
import eu.bittrade.libs.steemj.apis.follow.model.*;
import eu.bittrade.libs.steemj.base.models.AccountName;
import eu.bittrade.libs.steemj.base.models.Permlink;
import eu.bittrade.libs.steemj.communication.CommunicationHandler;
import eu.bittrade.libs.steemj.communication.dto.RequestWrapperDTO;
import eu.bittrade.libs.steemj.configuration.SteemJConfig;
import eu.bittrade.libs.steemj.enums.RequestMethods;
import eu.bittrade.libs.steemj.enums.SteemApis;
import eu.bittrade.libs.steemj.exceptions.SteemCommunicationException;

import javax.annotation.Nonnull;
import java.util.ArrayList;
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
        if (Golos4J.getInstance().getCurrentHardforkVersion() == Golos4J.HardForkVersion.HF_16)
            return steemJ.getFollowers(following, startFollower, type, limit);
        else {
            RequestWrapperDTO requestObject = new RequestWrapperDTO();
            requestObject.setApiMethod(RequestMethods.GET_FOLLOWERS);
            requestObject.setSteemApi(SteemApis.FOLLOW);

            Object[] parameters = {following.getName(), startFollower.getName(), type.toString().toLowerCase(), limit};
            requestObject.setAdditionalParameters(parameters);

            return communicationHandler.performRequest(requestObject, FollowApiObject.class);
        }
    }

    @Override
    public List<FollowApiObject> getFollowing(AccountName following, AccountName startFollower, FollowType type,
                                              Short limit) throws SteemCommunicationException {
        Golos4J.HardForkVersion version = Golos4J.getInstance().getCurrentHardforkVersion();
        RequestWrapperDTO requestObject = new RequestWrapperDTO();
        requestObject.setApiMethod(RequestMethods.GET_FOLLOWING);
        if (version == Golos4J.HardForkVersion.HF_16) {
            requestObject.setSteemApi(SteemApis.FOLLOW_API);
        } else {
            requestObject.setSteemApi(SteemApis.FOLLOW);
        }

        ArrayList<Object> parametersList = new ArrayList();
        if (following != null) parametersList.add(following.getName());
        if (startFollower != null) parametersList.add(startFollower.getName());
        if (type != null) parametersList.add(type.toString().toLowerCase());
        if (limit != null) parametersList.add(limit);
        Object[] parameters = parametersList.toArray();
        requestObject.setAdditionalParameters(parameters);

        return communicationHandler.performRequest(requestObject, FollowApiObject.class);
    }

    @Override
    public FollowCountApiObject getFollowCount(AccountName account) throws SteemCommunicationException {
        if (Golos4J.getInstance().getCurrentHardforkVersion() == Golos4J.HardForkVersion.HF_16)
            return steemJ.getFollowCount(account);
        else {
            RequestWrapperDTO requestObject = new RequestWrapperDTO();
            requestObject.setApiMethod(RequestMethods.GET_FOLLOW_COUNT);
            requestObject.setSteemApi(SteemApis.FOLLOW);

            Object[] parameters = {account.getName()};
            requestObject.setAdditionalParameters(parameters);

            return communicationHandler.performRequest(requestObject, FollowCountApiObject.class).get(0);
        }

    }

    @Override
    public List<FeedEntry> getFeedEntries(AccountName account, int entryId, short limit)
            throws SteemCommunicationException {
        if (Golos4J.getInstance().getCurrentHardforkVersion() == Golos4J.HardForkVersion.HF_16)
            return steemJ.getFeedEntries(account, entryId, limit);
        else {
            RequestWrapperDTO requestObject = new RequestWrapperDTO();
            requestObject.setApiMethod(RequestMethods.GET_FEED_ENTRIES);
            requestObject.setSteemApi(SteemApis.FOLLOW);

            Object[] parameters = {account.getName(), entryId, limit};
            requestObject.setAdditionalParameters(parameters);

            return communicationHandler.performRequest(requestObject, FeedEntry.class);
        }
    }

    @Override
    public List<CommentFeedEntry> getFeed(AccountName account, int entryId, short limit)
            throws SteemCommunicationException {
        if (Golos4J.getInstance().getCurrentHardforkVersion() == Golos4J.HardForkVersion.HF_16)
            return steemJ.getFeed(account, entryId, limit);
        else {
            RequestWrapperDTO requestObject = new RequestWrapperDTO();
            requestObject.setApiMethod(RequestMethods.GET_FEED);
            requestObject.setSteemApi(SteemApis.FOLLOW);

            Object[] parameters = {account.getName(), entryId, limit};
            requestObject.setAdditionalParameters(parameters);

            return communicationHandler.performRequest(requestObject, CommentFeedEntry.class);
        }

    }

    @Override
    public List<BlogEntry> getBlogEntries(AccountName account, int entryId, short limit)
            throws SteemCommunicationException {
        if (Golos4J.getInstance().getCurrentHardforkVersion() == Golos4J.HardForkVersion.HF_16)
            return steemJ.getBlogEntries(account, entryId, limit);
        else {
            RequestWrapperDTO requestObject = new RequestWrapperDTO();
            requestObject.setApiMethod(RequestMethods.GET_BLOG_ENTRIES);
            requestObject.setSteemApi(SteemApis.FOLLOW);

            Object[] parameters = {account.getName(), entryId, limit};
            requestObject.setAdditionalParameters(parameters);

            return communicationHandler.performRequest(requestObject, BlogEntry.class);
        }
    }

    @Override
    public List<CommentBlogEntry> getBlog(AccountName account, int entryId, short limit)
            throws SteemCommunicationException {
        if (Golos4J.getInstance().getCurrentHardforkVersion() == Golos4J.HardForkVersion.HF_16)
            return steemJ.getBlog(account, entryId, limit);
        else {
            RequestWrapperDTO requestObject = new RequestWrapperDTO();
            requestObject.setApiMethod(RequestMethods.GET_BLOG);
            requestObject.setSteemApi(SteemApis.FOLLOW);

            Object[] parameters = {account.getName(), entryId, limit};
            requestObject.setAdditionalParameters(parameters);

            return communicationHandler.performRequest(requestObject, CommentBlogEntry.class);
        }

    }

    @Override
    public List<AccountReputation> getAccountReputations(AccountName accountName, int limit)
            throws SteemCommunicationException {
        if (Golos4J.getInstance().getCurrentHardforkVersion() == Golos4J.HardForkVersion.HF_16)
            return steemJ.getAccountReputations(accountName, limit);
        else {
            RequestWrapperDTO requestObject = new RequestWrapperDTO();
            requestObject.setApiMethod(RequestMethods.GET_ACCOUNT_REPUTATIONS);
            requestObject.setSteemApi(SteemApis.FOLLOW);

            Object[] parameters = {accountName.getName(), limit};
            requestObject.setAdditionalParameters(parameters);

            return communicationHandler.performRequest(requestObject, AccountReputation.class);
        }
    }

    @Override
    public List<AccountName> getRebloggedBy(AccountName author, Permlink permlink) throws SteemCommunicationException {
        if (Golos4J.getInstance().getCurrentHardforkVersion() == Golos4J.HardForkVersion.HF_16)
            return steemJ.getRebloggedBy(author, permlink);
        else {
            RequestWrapperDTO requestObject = new RequestWrapperDTO();
            requestObject.setApiMethod(RequestMethods.GET_REBLOGGED_BY);
            requestObject.setSteemApi(SteemApis.FOLLOW);

            Object[] parameters = {author.getName(), permlink.getLink()};
            requestObject.setAdditionalParameters(parameters);

            return communicationHandler.performRequest(requestObject, AccountName.class);
        }
    }

    @Override
    public List<PostsPerAuthorPair> getBlogAuthors(AccountName blogAccount) throws SteemCommunicationException {
        if (Golos4J.getInstance().getCurrentHardforkVersion() == Golos4J.HardForkVersion.HF_16)
            return steemJ.getBlogAuthors(blogAccount);
        else {
            RequestWrapperDTO requestObject = new RequestWrapperDTO();
            requestObject.setApiMethod(RequestMethods.GET_BLOG_AUTHORS);
            requestObject.setSteemApi(SteemApis.FOLLOW);

            Object[] parameters = {blogAccount.getName()};
            requestObject.setAdditionalParameters(parameters);

            return communicationHandler.performRequest(requestObject, PostsPerAuthorPair.class);
        }
    }
}
