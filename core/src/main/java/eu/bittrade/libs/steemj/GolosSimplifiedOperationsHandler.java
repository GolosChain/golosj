package eu.bittrade.libs.steemj;

import eu.bittrade.libs.steemj.base.models.AccountName;
import eu.bittrade.libs.steemj.base.models.Permlink;
import eu.bittrade.libs.steemj.base.models.operations.CommentOperation;
import eu.bittrade.libs.steemj.communication.CommunicationHandler;
import eu.bittrade.libs.steemj.configuration.SteemJConfig;
import eu.bittrade.libs.steemj.exceptions.SteemCommunicationException;
import eu.bittrade.libs.steemj.exceptions.SteemInvalidTransactionException;

import javax.annotation.Nonnull;

/**
 * Created by yuri yurivladdurain@gmail.com .
 */

class GolosSimplifiedOperationsHandler implements SimplifiedOperations {
    @Nonnull
    private final SteemJConfig config;
    @Nonnull
    private final CommunicationHandler communicationHandler;
    @Nonnull
    private SteemJ steemJ;

    public GolosSimplifiedOperationsHandler(@Nonnull SteemJConfig config,
                                            @Nonnull CommunicationHandler communicationHandler,
                                            @Nonnull SteemJ steemJ) {
        this.config = config;
        this.communicationHandler = communicationHandler;
        this.steemJ = steemJ;
    }

    @Override
    public void vote(AccountName postOrCommentAuthor, Permlink postOrCommentPermlink, short percentage)
            throws SteemCommunicationException, SteemInvalidTransactionException {
        steemJ.vote(postOrCommentAuthor, postOrCommentPermlink, percentage);
    }

    @Override
    public void vote(AccountName voter, AccountName postOrCommentAuthor, Permlink postOrCommentPermlink,
                     short percentage) throws SteemCommunicationException, SteemInvalidTransactionException {
        steemJ.vote(voter, postOrCommentAuthor, postOrCommentPermlink, percentage);
    }

    @Override
    public void cancelVote(AccountName postOrCommentAuthor, Permlink postOrCommentPermlink)
            throws SteemCommunicationException, SteemInvalidTransactionException {
        steemJ.cancelVote(postOrCommentAuthor, postOrCommentPermlink);
    }

    @Override
    public void cancelVote(AccountName voter, AccountName postOrCommentAuthor, Permlink postOrCommentPermlink)
            throws SteemCommunicationException, SteemInvalidTransactionException {
        steemJ.cancelVote(voter, postOrCommentAuthor, postOrCommentPermlink);
    }

    @Override
    public void follow(AccountName accountToFollow)
            throws SteemCommunicationException, SteemInvalidTransactionException {
        steemJ.follow(accountToFollow);
    }

    @Override
    public void follow(AccountName accountThatFollows, AccountName accountToFollow)
            throws SteemCommunicationException, SteemInvalidTransactionException {
        steemJ.follow(accountThatFollows, accountToFollow);
    }

    @Override
    public void unfollow(AccountName accountToUnfollow)
            throws SteemCommunicationException, SteemInvalidTransactionException {
        steemJ.unfollow(accountToUnfollow);
    }

    @Override
    public void unfollow(AccountName accountThatUnfollows, AccountName accountToUnfollow)
            throws SteemCommunicationException, SteemInvalidTransactionException {
        steemJ.unfollow(accountThatUnfollows, accountToUnfollow);
    }

    @Override
    public CommentOperation createPost(String title,
                                       String content,
                                       String[] tags)
            throws SteemCommunicationException, SteemInvalidTransactionException {
        return steemJ.createPost(title, content, tags);
    }

    @Override
    public CommentOperation createPost(AccountName authorThatPublishsThePost,
                                       String title,
                                       String content,
                                       String[] tags) throws SteemCommunicationException, SteemInvalidTransactionException {
        return steemJ.createPost(authorThatPublishsThePost, title, content, tags);
    }

    @Override
    public CommentOperation createComment(AccountName authorOfThePostOrCommentToReplyTo,
                                          Permlink permlinkOfThePostOrCommentToReplyTo,
                                          String content,
                                          String[] tags)
            throws SteemCommunicationException, SteemInvalidTransactionException {
        return steemJ.createComment(authorOfThePostOrCommentToReplyTo, permlinkOfThePostOrCommentToReplyTo, content, tags);
    }

    @Override
    public CommentOperation createComment(AccountName authorOfThePostOrCommentToReplyTo,
                                          Permlink permlinkOfThePostOrCommentToReplyTo,
                                          AccountName authorThatPublishsTheComment,
                                          String content,
                                          String[] tags) throws SteemCommunicationException,
            SteemInvalidTransactionException {
        return steemJ.createComment(authorOfThePostOrCommentToReplyTo, permlinkOfThePostOrCommentToReplyTo, authorThatPublishsTheComment, content, tags);
    }

    @Override
    public CommentOperation updatePost(Permlink permlinkOfThePostToUpdate, String title, String content, String[] tags)
            throws SteemCommunicationException, SteemInvalidTransactionException {
        return steemJ.updatePost(permlinkOfThePostToUpdate, title, content, tags);
    }

    @Override
    public CommentOperation updatePost(AccountName authorOfThePostToUpdate, Permlink permlinkOfThePostToUpdate,
                                       String title, String content, String[] tags) throws SteemCommunicationException, SteemInvalidTransactionException {
        return steemJ.updatePost(authorOfThePostToUpdate, permlinkOfThePostToUpdate, title, content, tags);
    }

    @Override
    public CommentOperation updateComment(AccountName parentAuthor, Permlink parentPermlink,
                                          Permlink originalPermlinkOfYourComment, String content, String[] tags)
            throws SteemCommunicationException, SteemInvalidTransactionException {
        return steemJ.updateComment(parentAuthor, parentPermlink, originalPermlinkOfYourComment, content, tags);
    }

    @Override
    public CommentOperation updateComment(AccountName parentAuthor, Permlink parentPermlink,
                                          Permlink originalPermlinkOfTheCommentToUpdate, AccountName originalAuthorOfTheCommentToUpdate,
                                          String content, String[] tags) throws SteemCommunicationException, SteemInvalidTransactionException {
        return steemJ.updateComment(parentAuthor, parentPermlink, originalPermlinkOfTheCommentToUpdate, originalAuthorOfTheCommentToUpdate, content, tags);
    }

    @Override
    public void deletePostOrComment(Permlink postOrCommentPermlink)
            throws SteemCommunicationException, SteemInvalidTransactionException {
        steemJ.deletePostOrComment(postOrCommentPermlink);
    }

    @Override
    public void deletePostOrComment(AccountName postOrCommentAuthor, Permlink postOrCommentPermlink)
            throws SteemCommunicationException, SteemInvalidTransactionException {
        steemJ.deletePostOrComment(postOrCommentAuthor, postOrCommentPermlink);
    }
}
