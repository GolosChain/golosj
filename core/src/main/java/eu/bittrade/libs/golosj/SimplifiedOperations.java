package eu.bittrade.libs.golosj;

import com.fasterxml.jackson.core.JsonProcessingException;

import eu.bittrade.libs.golosj.base.models.AccountName;
import eu.bittrade.libs.golosj.base.models.Asset;
import eu.bittrade.libs.golosj.base.models.Permlink;
import eu.bittrade.libs.golosj.base.models.PublicKey;
import eu.bittrade.libs.golosj.base.models.operations.CommentOperation;
import eu.bittrade.libs.golosj.configuration.SteemJConfig;
import eu.bittrade.libs.golosj.exceptions.SteemCommunicationException;
import eu.bittrade.libs.golosj.exceptions.SteemInvalidTransactionException;

import javax.annotation.Nonnull;
import java.security.InvalidParameterException;

/**
 * Created by yuri yurivladdurain@gmail.com .
 */

public interface SimplifiedOperations {
    /**
     * Use this method to up or down vote a post or a comment.
     * <p>
     * <b>Attention</b>
     * <ul>
     * <li>This method will write data on the blockchain. As all writing
     * operations, a private key is required to sign the transaction. For a
     * voting operation the private posting key of the
     * {@link SteemJConfig#getDefaultAccount() DefaultAccount} needs to be
     * configured in the {@link SteemJConfig#getPrivateKeyStorage()
     * PrivateKeyStorage}.</li>
     * <li>This method will automatically use the
     * {@link SteemJConfig#getDefaultAccount() DefaultAccount} as the voter - If
     * no default account has been provided, this method will throw an error. If
     * you do not want to configure the voter as a default account, please use
     * the {@link #vote(AccountName, AccountName, Permlink, short)} method and
     * provide the voter account separately.</li>
     * </ul>
     *
     * @param postOrCommentAuthor   The author of the post or the comment to vote for.
     *                              <p>
     *                              Example:<br>
     *                              <code>new AccountName("dez1337")</code>
     *                              </p>
     * @param postOrCommentPermlink The permanent link of the post or the comment to vote for.
     *                              <p>
     *                              Example:<br>
     *                              <code>new Permlink("golosj-v0-2-4-has-been-released-update-9")</code>
     *                              </p>
     * @param percentage            Define how much of your voting power should be used to up or
     *                              down vote the post or the comment.
     *                              <ul>
     *                              <li>If you want to up vote the post or the comment provide a
     *                              value between 1 (1.0%) and 100 (100.0%).</li>
     *                              <li>If you want to down vote (as known as <b>flag</b>) the
     *                              post or the comment provide a value between -1 (-1.0%) and
     *                              -100 (-100.0%).</li>
     *                              </ul>
     * @throws SteemCommunicationException      If there is a problem reaching the Steem Node.
     * @throws SteemInvalidTransactionException If there is a problem while signing the transaction.
     * @throws InvalidParameterException        If one of the provided parameters does not fulfill the
     *                                          requirements described above.
     */
    void vote(@Nonnull AccountName postOrCommentAuthor, @Nonnull Permlink postOrCommentPermlink, short percentage)
            throws SteemCommunicationException, SteemInvalidTransactionException;

    /**
     * This method is equivalent to the
     * {@link #vote(AccountName, Permlink, short)} method, but lets you define
     * the <code>voter</code> account separately instead of using the
     * {@link SteemJConfig#getDefaultAccount() DefaultAccount}.
     *
     * @param voter                 The account that should vote for the post or the comment.
     *                              <p>
     *                              Example<br>
     *                              <code>new AccountName("golosj")</code>
     *                              </p>
     * @param postOrCommentAuthor   The author of the post or the comment to vote for.
     *                              <p>
     *                              Example:<br>
     *                              <code>new AccountName("dez1337")</code>
     *                              </p>
     * @param postOrCommentPermlink The permanent link of the post or the comment to vote for.
     *                              <p>
     *                              Example:<br>
     *                              <code>new Permlink("golosj-v0-2-4-has-been-released-update-9")</code>
     *                              </p>
     * @param percentage            Define how much of your voting power should be used to up or
     *                              down vote the post or the comment.
     *                              <ul>
     *                              <li>If you want to up vote the post or the comment provide a
     *                              value between 1 (1.0%) and 100 (100.0%).</li>
     *                              <li>If you want to down vote (as known as <b>flag</b>) the
     *                              post or the comment provide a value between -1 (-1.0%) and
     *                              -100 (-100.0%).</li>
     *                              </ul>
     * @throws SteemCommunicationException      If there is a problem reaching the Steem Node.
     * @throws SteemInvalidTransactionException If there is a problem while signing the transaction.
     * @throws InvalidParameterException        If one of the provided parameters does not fulfill the
     *                                          requirements described above.
     */
    void vote(AccountName voter, AccountName postOrCommentAuthor, Permlink postOrCommentPermlink,
              short percentage) throws SteemCommunicationException, SteemInvalidTransactionException;

    /**
     * Use this method to cancel a previous vote for a post or a comment.
     * <p>
     * <b>Attention</b>
     * <ul>
     * <li>This method will write data on the blockchain. As all writing
     * operations, a private key is required to sign the transaction. For a
     * voting operation the private posting key of the
     * {@link SteemJConfig#getDefaultAccount() DefaultAccount} needs to be
     * configured in the {@link SteemJConfig#getPrivateKeyStorage()
     * PrivateKeyStorage}.</li>
     * <li>This method will automatically use the
     * {@link SteemJConfig#getDefaultAccount() DefaultAccount} as the voter - If
     * no default account has been provided, this method will throw an error. If
     * you do not want to configure the voter as a default account, please use
     * the {@link #vote(AccountName, AccountName, Permlink, short)} method and
     * provide the voter account separately.</li>
     * </ul>
     *
     * @param postOrCommentAuthor   The author of the post or the comment to cancel the vote for.
     *                              <p>
     *                              Example:<br>
     *                              <code>new AccountName("dez1337")</code>
     *                              </p>
     * @param postOrCommentPermlink The permanent link of the post or the comment to cancel the
     *                              vote for.
     *                              <p>
     *                              Example:<br>
     *                              <code>new Permlink("golosj-v0-2-4-has-been-released-update-9")</code>
     *                              </p>
     * @throws SteemCommunicationException      If there is a problem reaching the Steem Node.
     * @throws SteemInvalidTransactionException If there is a problem while signing the transaction.
     * @throws InvalidParameterException        If one of the provided parameters does not fulfill the
     *                                          requirements described above.
     */
    void cancelVote(AccountName postOrCommentAuthor, Permlink postOrCommentPermlink)
            throws SteemCommunicationException, SteemInvalidTransactionException;

    /**
     * This method is equivalent to the
     * {@link #cancelVote(AccountName, Permlink)} method, but lets you define
     * the <code>voter</code> account separately instead of using the
     * {@link SteemJConfig#getDefaultAccount() DefaultAccount}.
     *
     * @param voter                 The account that should vote for the post or the comment.
     *                              <p>
     *                              Example<br>
     *                              <code>new AccountName("golosj")</code>
     *                              </p>
     * @param postOrCommentAuthor   The author of the post or the comment to vote for.
     *                              <p>
     *                              Example:<br>
     *                              <code>new AccountName("dez1337")</code>
     *                              </p>
     * @param postOrCommentPermlink The permanent link of the post or the comment to vote for.
     *                              <p>
     *                              Example:<br>
     *                              <code>new Permlink("golosj-v0-2-4-has-been-released-update-9")</code>
     *                              </p>
     * @throws SteemCommunicationException      If there is a problem reaching the Steem Node.
     * @throws SteemInvalidTransactionException If there is a problem while signing the transaction.
     * @throws InvalidParameterException        If one of the provided parameters does not fulfill the
     *                                          requirements described above.
     */
    void cancelVote(AccountName voter, AccountName postOrCommentAuthor, Permlink postOrCommentPermlink)
            throws SteemCommunicationException, SteemInvalidTransactionException;

    /**
     * Use this method to follow the <code>accountToFollow</code>.
     * <p>
     * <b>Attention</b>
     * <ul>
     * <li>This method will write data on the blockchain. As all writing
     * operations, a private key is required to sign the transaction. For a
     * follow operation the private posting key of the
     * {@link SteemJConfig#getDefaultAccount() DefaultAccount} needs to be
     * configured in the {@link SteemJConfig#getPrivateKeyStorage()
     * PrivateKeyStorage}.</li>
     * <li>This method will automatically use the
     * {@link SteemJConfig#getDefaultAccount() DefaultAccount} as the account
     * that will follow the <code>accountToFollow</code> - If no default account
     * has been provided, this method will throw an error. If you do not want to
     * configure the following account as a default account, please use the
     * {@link #follow(AccountName, AccountName)} method and provide the
     * following account separately.</li>
     * </ul>
     *
     * @param accountToFollow The account name of the account the
     *                        {@link SteemJConfig#getDefaultAccount() DefaultAccount} should
     *                        follow.
     * @throws SteemCommunicationException      If there is a problem reaching the Steem Node.
     * @throws SteemInvalidTransactionException If there is a problem while signing the transaction.
     * @throws InvalidParameterException        If one of the provided parameters does not fulfill the
     *                                          requirements described above.
     */
    void follow(AccountName accountToFollow)
            throws SteemCommunicationException, SteemInvalidTransactionException;

    /**
     * This method is equivalent to the {@link #follow(AccountName)} method, but
     * lets you define the <code>accountThatFollows</code> separately instead of
     * using the {@link SteemJConfig#getDefaultAccount() DefaultAccount}.
     *
     * @param accountThatFollows The account name of the account that will follow the
     *                           <code>accountToFollow</code>.
     * @param accountToFollow    The account name of the account the
     *                           {@link SteemJConfig#getDefaultAccount() DefaultAccount} should
     *                           follow.
     * @throws SteemCommunicationException      If there is a problem reaching the Steem Node.
     * @throws SteemInvalidTransactionException If there is a problem while signing the transaction.
     * @throws InvalidParameterException        If one of the provided parameters does not fulfill the
     *                                          requirements described above.
     */
    void follow(AccountName accountThatFollows, AccountName accountToFollow)
            throws SteemCommunicationException, SteemInvalidTransactionException;

    /**
     * Use this method to unfollow the <code>accountToUnfollow</code>.
     * <p>
     * <b>Attention</b>
     * <ul>
     * <li>This method will write data on the blockchain. As all writing
     * operations, a private key is required to sign the transaction. For a
     * unfollow operation the private posting key of the
     * {@link SteemJConfig#getDefaultAccount() DefaultAccount} needs to be
     * configured in the {@link SteemJConfig#getPrivateKeyStorage()
     * PrivateKeyStorage}.</li>
     * <li>This method will automatically use the
     * {@link SteemJConfig#getDefaultAccount() DefaultAccount} as the account
     * that will no longer follow the <code>accountToFollow</code> - If no
     * default account has been provided, this method will throw an error. If
     * you do not want to configure the following account as a default account,
     * please use the {@link #follow(AccountName, AccountName)} method and
     * provide the following account separately.</li>
     * </ul>
     *
     * @param accountToUnfollow The account name of the account the
     *                          {@link SteemJConfig#getDefaultAccount() DefaultAccount} should
     *                          no longer follow.
     * @throws SteemCommunicationException      If there is a problem reaching the Steem Node.
     * @throws SteemInvalidTransactionException If there is a problem while signing the transaction.
     * @throws InvalidParameterException        If one of the provided parameters does not fulfill the
     *                                          requirements described above.
     */
    void unfollow(AccountName accountToUnfollow)
            throws SteemCommunicationException, SteemInvalidTransactionException;

    /**
     * This method is equivalent to the {@link #unfollow(AccountName)} method,
     * but lets you define the <code>accountThatUnfollows</code> account
     * separately instead of using the {@link SteemJConfig#getDefaultAccount()
     * DefaultAccount}.
     *
     * @param accountThatUnfollows The account name of the account that will no longer follow the
     *                             <code>accountToUnfollow</code>.
     * @param accountToUnfollow    The account name of the account the
     *                             {@link SteemJConfig#getDefaultAccount() DefaultAccount} should
     *                             no longer follow.
     * @throws SteemCommunicationException      If there is a problem reaching the Steem Node.
     * @throws SteemInvalidTransactionException If there is a problem while signing the transaction.
     * @throws InvalidParameterException        If one of the provided parameters does not fulfill the
     *                                          requirements described above.
     */
    void unfollow(AccountName accountThatUnfollows, AccountName accountToUnfollow)
            throws SteemCommunicationException, SteemInvalidTransactionException;

    /**
     * @param title
     * @param content
     * @param tags
     * @throws SteemCommunicationException
     * @throws SteemInvalidTransactionException
     */
    CommentOperation createPost(String title, String content, String[] tags)
            throws SteemCommunicationException, SteemInvalidTransactionException;

    /**
     * @param authorThatPublishsThePost
     * @param title
     * @param content
     * @param tags
     * @throws SteemCommunicationException
     * @throws SteemInvalidTransactionException
     */
    CommentOperation createPost(AccountName authorThatPublishsThePost, String title, String content,
                                String[] tags) throws SteemCommunicationException, SteemInvalidTransactionException;

    /**
     * @param authorOfThePostOrCommentToReplyTo
     * @param permlinkOfThePostOrCommentToReplyTo
     * @param content
     * @param tags
     * @throws SteemCommunicationException
     * @throws SteemInvalidTransactionException
     */
    CommentOperation createComment(AccountName authorOfThePostOrCommentToReplyTo,
                                   Permlink permlinkOfThePostOrCommentToReplyTo, String content, String[] tags)
            throws SteemCommunicationException, SteemInvalidTransactionException;

    /**
     * @param authorOfThePostOrCommentToReplyTo
     * @param permlinkOfThePostOrCommentToReplyTo
     * @param authorThatPublishsTheComment
     * @param content
     * @param tags
     * @throws SteemCommunicationException
     * @throws SteemInvalidTransactionException
     */
    CommentOperation createComment(AccountName authorOfThePostOrCommentToReplyTo,
                                   Permlink permlinkOfThePostOrCommentToReplyTo, AccountName authorThatPublishsTheComment, String content,
                                   String[] tags) throws SteemCommunicationException, SteemInvalidTransactionException;

    /**
     * @param permlinkOfThePostToUpdate
     * @param title
     * @param content
     * @param tags
     * @throws SteemCommunicationException
     * @throws SteemInvalidTransactionException
     */
    CommentOperation updatePost(Permlink permlinkOfThePostToUpdate, String title, String content, String[] tags)
            throws SteemCommunicationException, SteemInvalidTransactionException;

    /**
     * @param authorOfThePostToUpdate
     * @param permlinkOfThePostToUpdate
     * @param title
     * @param content
     * @param tags
     * @throws SteemCommunicationException
     * @throws SteemInvalidTransactionException
     */
    CommentOperation updatePost(AccountName authorOfThePostToUpdate, Permlink permlinkOfThePostToUpdate,
                                String title, String content, String[] tags) throws SteemCommunicationException, SteemInvalidTransactionException;

    /**
     * @param parentAuthor
     * @param parentPermlink
     * @param originalPermlinkOfYourComment
     * @param content
     * @param tags
     * @throws SteemCommunicationException
     * @throws SteemInvalidTransactionException
     */
    CommentOperation updateComment(AccountName parentAuthor, Permlink parentPermlink,
                                   Permlink originalPermlinkOfYourComment, String content, String[] tags)
            throws SteemCommunicationException, SteemInvalidTransactionException;

    /**
     * @param parentAuthor
     * @param parentPermlink
     * @param originalPermlinkOfTheCommentToUpdate
     * @param originalAuthorOfTheCommentToUpdate
     * @param content
     * @param tags
     * @throws SteemCommunicationException
     * @throws SteemInvalidTransactionException
     */
    CommentOperation updateComment(AccountName parentAuthor, Permlink parentPermlink,
                                   Permlink originalPermlinkOfTheCommentToUpdate, AccountName originalAuthorOfTheCommentToUpdate,
                                   String content, String[] tags) throws SteemCommunicationException, SteemInvalidTransactionException;

    /**
     * Use this method to remove a comment or a post.
     * <p>
     * <b>Attention</b>
     * <ul>
     * <li>This method will write data on the blockchain. As all writing
     * operations, a private key is required to sign the transaction. For a
     * voting operation the private posting key of the
     * {@link SteemJConfig#getDefaultAccount() DefaultAccount} needs to be
     * configured in the {@link SteemJConfig#getPrivateKeyStorage()
     * PrivateKeyStorage}.</li>
     * <li>This method will automatically use the
     * {@link SteemJConfig#getDefaultAccount() DefaultAccount} as the author of
     * the comment or post to remove - If no default account has been provided,
     * this method will throw an error. If you do not want to configure the
     * author as a default account, please use the
     * {@link #deletePostOrComment(AccountName, Permlink)} method and provide
     * the author account separately.</li>
     * </ul>
     *
     * @param postOrCommentPermlink The permanent link of the post or the comment to delete.
     *                              <p>
     *                              Example:<br>
     *                              <code>new Permlink("golosj-v0-2-4-has-been-released-update-9")</code>
     *                              </p>
     * @throws SteemCommunicationException      If there is a problem reaching the Steem Node.
     * @throws SteemInvalidTransactionException If there is a problem while signing the transaction.
     * @throws InvalidParameterException        If one of the provided parameters does not fulfill the
     *                                          requirements described above.
     */
    void deletePostOrComment(Permlink postOrCommentPermlink)
            throws SteemCommunicationException, SteemInvalidTransactionException;

    /**
     * This method is like the {@link #deletePostOrComment(Permlink)} method,
     * but allows you to define the author account separately instead of using
     * the {@link SteemJConfig#getDefaultAccount() DefaultAccount}.
     *
     * @param postOrCommentAuthor   The author of the post or the comment to vote for.
     *                              <p>
     *                              Example:<br>
     *                              <code>new AccountName("dez1337")</code>
     *                              </p>
     * @param postOrCommentPermlink The permanent link of the post or the comment to delete.
     *                              <p>
     *                              Example:<br>
     *                              <code>new Permlink("golosj-v0-2-4-has-been-released-update-9")</code>
     *                              </p>
     * @throws SteemCommunicationException      If there is a problem reaching the Steem Node.
     * @throws SteemInvalidTransactionException If there is a problem while signing the transaction.
     * @throws InvalidParameterException        If one of the provided parameters does not fulfill the
     *                                          requirements described above.
     */
    void deletePostOrComment(AccountName postOrCommentAuthor, Permlink postOrCommentPermlink)
            throws SteemCommunicationException, SteemInvalidTransactionException;


    /**
     * Create a new create account operation. Use this operation to create a new
     * account.
     *
     * @param creator        Set the account that will pay the <code>fee</code> to create
     *                       the <code>newAccountName</code> (see
     * @param fee            Set the fee the <code>creator</code> will pay
     * @param newAccountName Set the new account name
     * @param fee            Fee, num of GOLOS, that would be given to new account. For golos chain, it must be not less than 3 GOLOS
     *                       precision is 1/1000, i.e. 3 GOLOS = 3000 fee
     * @param ownerKey       owner key of new account. this and other keys can be generated from newAccount name and password,
     *                       using {@link eu.bittrade.libs.golosj.util.AuthUtils} static methods
     *                       not be updated
     * @param activeKey      active key of new account
     * @param postingKey     posting key of new account
     * @param memoKey        The new memo key or null if the memo key should not be updated
     * @throws SteemCommunicationException      If there is a problem reaching the Steem Node.
     * @throws SteemInvalidTransactionException If there is a problem while signing the transaction.
     * @throws InvalidParameterException        If one of the provided parameters does not fulfill the
     *                                          requirements described above.
     */

    void createAccount(@Nonnull AccountName creator,
                       @Nonnull String creatorActiveKey,
                       @Nonnull Asset fee,
                       @Nonnull AccountName newAccountName,
                       @Nonnull PublicKey ownerKey,
                       @Nonnull PublicKey activeKey,
                       @Nonnull PublicKey postingKey,
                       @Nonnull PublicKey memoKey,
                       @Nonnull String jsonMetadata)
            throws SteemCommunicationException, SteemInvalidTransactionException;

    /**
     * Create a new reblog operation to reblog a comment or a post.
     * to invoke this method default account must be set.
     *
     * @param authorOfTheAuthor        author of original post
     * @param permlink                 permlink of original post
     * @throws SteemCommunicationException      If there is a problem reaching the Steem Node.
     * @throws SteemInvalidTransactionException If there is a problem while signing the transaction.
     * @throws JsonProcessingException        if there were some json converting errors
     */

    public void reblog(@Nonnull AccountName authorOfTheAuthor, @Nonnull Permlink permlink) throws SteemCommunicationException,
            SteemInvalidTransactionException, JsonProcessingException;

}
