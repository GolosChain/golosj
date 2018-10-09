package eu.bittrade.libs.golosj;

import com.fasterxml.jackson.core.JsonProcessingException;

import java.util.ArrayList;
import java.util.Collections;

import javax.annotation.Nonnull;

import eu.bittrade.libs.golosj.apis.follow.models.operations.ReblogOperation;
import eu.bittrade.libs.golosj.base.models.AccountName;
import eu.bittrade.libs.golosj.base.models.Asset;
import eu.bittrade.libs.golosj.base.models.Authority;
import eu.bittrade.libs.golosj.base.models.GlobalProperties;
import eu.bittrade.libs.golosj.base.models.Permlink;
import eu.bittrade.libs.golosj.base.models.PublicKey;
import eu.bittrade.libs.golosj.base.models.SignedTransaction;
import eu.bittrade.libs.golosj.base.models.operations.AccountCreateOperation;
import eu.bittrade.libs.golosj.base.models.operations.CommentOperation;
import eu.bittrade.libs.golosj.base.models.operations.CustomJsonOperation;
import eu.bittrade.libs.golosj.base.models.operations.Operation;
import eu.bittrade.libs.golosj.communication.CommunicationHandler;
import eu.bittrade.libs.golosj.configuration.SteemJConfig;
import eu.bittrade.libs.golosj.enums.PrivateKeyType;
import eu.bittrade.libs.golosj.exceptions.SteemCommunicationException;
import eu.bittrade.libs.golosj.exceptions.SteemInvalidTransactionException;
import eu.bittrade.libs.golosj.util.ImmutablePair;

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


    public void createAccount(@Nonnull AccountName creator, @Nonnull String creatorActiveKey, @Nonnull Asset fee,
                              @Nonnull AccountName newAccountName, @Nonnull PublicKey ownerKey,
                              @Nonnull PublicKey activeKey, @Nonnull PublicKey postingKey,
                              @Nonnull PublicKey memoKey, @Nonnull String jsonMetadata)
            throws SteemCommunicationException, SteemInvalidTransactionException {

        AccountCreateOperation accountCreateOperation = new AccountCreateOperation(creator, fee, newAccountName,
                new Authority(ownerKey), new Authority(activeKey), new Authority(postingKey), memoKey, jsonMetadata);

        GlobalProperties globalProperties = Golos4J.getInstance().getDatabaseMethods().getDynamicGlobalProperties();
        ArrayList<Operation> operations = new ArrayList<>();

        operations.add(accountCreateOperation);

        SignedTransaction signedTransaction = new SignedTransaction(globalProperties.getHeadBlockId(), operations,
                null);

        Golos4J.getInstance().addAccount(creator, new ImmutablePair<>(PrivateKeyType.ACTIVE, creatorActiveKey), false);

        signedTransaction.sign();

        Golos4J.getInstance().getNetworkBroadcastMethods().broadcastTransaction(signedTransaction);
    }


    public void reblog(@Nonnull AccountName authorOfTheAuthor, @Nonnull Permlink permlink) throws SteemCommunicationException,
            SteemInvalidTransactionException, JsonProcessingException {
        ReblogOperation reblogOperation = new ReblogOperation(SteemJConfig.getInstance().getDefaultAccount(), authorOfTheAuthor, permlink);
        GlobalProperties globalProperties = Golos4J.getInstance().getDatabaseMethods().getDynamicGlobalProperties();
        CustomJsonOperation customJsonOperation = new CustomJsonOperation(Collections.<AccountName>emptyList(),
                Collections.singletonList(SteemJConfig.getInstance().getDefaultAccount()),
                "follow",reblogOperation.toJson());
        ArrayList<Operation> operations = new ArrayList<>();
        operations.add(customJsonOperation);
        SignedTransaction signedTransaction = new SignedTransaction(globalProperties.getHeadBlockId(), operations,
                null);
        signedTransaction.sign();
        Golos4J.getInstance().getNetworkBroadcastMethods().broadcastTransaction(signedTransaction);
    }
}
