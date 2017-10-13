package eu.bittrade.libs.steemj;

import eu.bittrade.libs.steemj.apis.follow.enums.FollowType;
import eu.bittrade.libs.steemj.apis.follow.model.*;
import eu.bittrade.libs.steemj.base.models.AccountName;
import eu.bittrade.libs.steemj.base.models.Permlink;
import eu.bittrade.libs.steemj.exceptions.SteemCommunicationException;

import java.util.List;

/**
 * Created by yuri yurivladdurain@gmail.com .
 */

@SuppressWarnings("unused")
public interface FollowApiMethods {

    /**
     * Get a list of account names which the <code>following</code> account is
     * followed by.
     *
     * @param following
     *            The account name for whose followers should be returned.
     * @param startFollower
     *            A filter to limit the number of results. If not empty, the
     *            method will only return account names after the
     *            <code>following</code> account has been followed by the
     *            <code>startFollower</code> account.
     * @param type
     *            The follow type.
     * @param limit
     *            The maximum number of results returned.
     * @return A list of account names that follow the <code>follower</code>
     *         account..
     * @throws SteemCommunicationException
     *             <ul>
     *             <li>If the server was not able to answer the request in the
     *             given time (see
     *             {@link eu.bittrade.libs.steemj.configuration.SteemJConfig#setResponseTimeout(long)
     *             setResponseTimeout}).</li>
     *             <li>If there is a connection problem.</li>
     *             <li>If the SteemJ is unable to transform the JSON response
     *             into a Java object.</li>
     *             <li>If the Server returned an error object.</li>
     *             </ul>
     */
    List<FollowApiObject> getFollowers(AccountName following, AccountName startFollower, FollowType type,
                                       short limit) throws SteemCommunicationException;

    /**
     * Get a list of account names which the <code>follower</code> account
     * follows.
     *
     * @param follower
     *            The account name for which the account names should be
     *            returned, that the <code>follower</code> is following.
     * @param startFollowing
     *            A filter to limit the number of results. If not empty, the
     *            method will only return account names after the
     *            <code>follower</code> account has followed the
     *            <code>startFollowing</code> account.
     * @param type
     *            The follow type.
     * @param limit
     *            The maximum number of results returned.
     * @return A list of account names the <code>follower</code> account is
     *         following.
     * @throws SteemCommunicationException
     *             <ul>
     *             <li>If the server was not able to answer the request in the
     *             given time (see
     *             {@link eu.bittrade.libs.steemj.configuration.SteemJConfig#setResponseTimeout(long)
     *             setResponseTimeout}).</li>
     *             <li>If there is a connection problem.</li>
     *             <li>If the SteemJ is unable to transform the JSON response
     *             into a Java object.</li>
     *             <li>If the Server returned an error object.</li>
     *             </ul>
     */
    List<FollowApiObject> getFollowing(AccountName follower, AccountName startFollowing, FollowType type,
                                              short limit)  throws SteemCommunicationException;

    /**
     * Get the amount of accounts following the given <code>account</code> and
     * the number of accounts this <code>account</code> follows. Both values are
     * wrapped in a FollowCountApiObject.
     *
     * @param account
     *            The account to get the number of followers / following
     *            accounts for.
     * @return The number of followers / following accounts
     * @throws SteemCommunicationException
     *             <ul>
     *             <li>If the server was not able to answer the request in the
     *             given time (see
     *             {@link eu.bittrade.libs.steemj.configuration.SteemJConfig#setResponseTimeout(long)
     *             setResponseTimeout}).</li>
     *             <li>If there is a connection problem.</li>
     *             <li>If the SteemJ is unable to transform the JSON response
     *             into a Java object.</li>
     *             <li>If the Server returned an error object.</li>
     *             </ul>
     */
    FollowCountApiObject getFollowCount(AccountName account) throws SteemCommunicationException ;

    /**
     * This method is like the {@link #getBlogEntries(AccountName, int, short)
     * getBlogEntries(AccountName, int, short)} method, but instead of returning
     * blog entries, the getFeedEntries method returns the feed of the given
     * account.
     *
     * @param account
     *            The account to get the feed entries for.
     * @param entryId
     *            The first feed entry id to return.
     * @param limit
     *            The number of results.
     * @return A list of feed entries from the given <code>author</code> based
     *         on the given conditions (<code>entryId</code> and
     *         <code>limit</code>).
     * @throws SteemCommunicationException
     *             <ul>
     *             <li>If the server was not able to answer the request in the
     *             given time (see
     *             {@link eu.bittrade.libs.steemj.configuration.SteemJConfig#setResponseTimeout(long)
     *             setResponseTimeout}).</li>
     *             <li>If there is a connection problem.</li>
     *             <li>If the SteemJ is unable to transform the JSON response
     *             into a Java object.</li>
     *             <li>If the Server returned an error object.</li>
     *             </ul>
     */
    List<FeedEntry> getFeedEntries(AccountName account, int entryId, short limit)
            throws SteemCommunicationException ;

    /**
     * This method is like the {@link #getBlog(AccountName, int, short)
     * getBlog(AccountName, int, short)} method, but instead of returning blog
     * entries, the getFeed method returns the feed of the given account.
     *
     * @param account
     *            The account to get the feed entries for.
     * @param entryId
     *            The first feed entry id to return.
     * @param limit
     *            The number of results.
     * @return A list of feed entries from the given <code>author</code> based
     *         on the given conditions (<code>entryId</code> and
     *         <code>limit</code>).
     * @throws SteemCommunicationException
     *             <ul>
     *             <li>If the server was not able to answer the request in the
     *             given time (see
     *             {@link eu.bittrade.libs.steemj.configuration.SteemJConfig#setResponseTimeout(long)
     *             setResponseTimeout}).</li>
     *             <li>If there is a connection problem.</li>
     *             <li>If the SteemJ is unable to transform the JSON response
     *             into a Java object.</li>
     *             <li>If the Server returned an error object.</li>
     *             </ul>
     */
    List<CommentFeedEntry> getFeed(AccountName account, int entryId, short limit)
            throws SteemCommunicationException ;

    /**
     * Get the blog entries of the given <code>author</code> based on the given
     * coniditions.
     *
     * Each blog entry of an <code>author</code> (resteemed or posted on his/her
     * own) has an <code>entryId</code>, while the <code>entryId</code> starts
     * with 0 for the first blog entry and is increment by 1 for each resteem or
     * post of the <code>author</code>.
     *
     * Steem allows to use the <code>entryId</code> as a search criteria: The
     * first entry of the returned list is the blog entry with the given
     * <code>entryId</code>. Beside that, the <code>limit</code> can be used to
     * limit the number of results.
     *
     * So if the method is called with <code>entryId</code> set to 5 and the
     * <code>limit</code> is set to 2, the returned list will contain 2 entries:
     * The first one is the blog entry with <code>entryId</code> of 5, the
     * second one has the <code>entryId</code> 4.
     *
     * If the <code>entryId</code> is set to 0, the first returned item will be
     * the latest blog entry of the given <code>author</code>.
     *
     * So if a user has 50 blog entries and this method is called with an
     * <code>entryId</code> set to 0 and a <code>limit</code> of 2, the returned
     * list will contain the blog entries with the <code>entryId</code>s 50 and
     * 49.
     *
     * @param account
     *            The account to get the blog entries for.
     * @param entryId
     *            The first blog entry id to return.
     * @param limit
     *            The number of results.
     * @return A list of blog entries from the given <code>author</code> based
     *         on the given conditions (<code>entryId</code> and
     *         <code>limit</code>).
     * @throws SteemCommunicationException
     *             <ul>
     *             <li>If the server was not able to answer the request in the
     *             given time (see
     *             {@link eu.bittrade.libs.steemj.configuration.SteemJConfig#setResponseTimeout(long)
     *             setResponseTimeout}).</li>
     *             <li>If there is a connection problem.</li>
     *             <li>If the SteemJ is unable to transform the JSON response
     *             into a Java object.</li>
     *             <li>If the Server returned an error object.</li>
     *             </ul>
     */
    List<BlogEntry> getBlogEntries(AccountName account, int entryId, short limit)
            throws SteemCommunicationException;

    /**
     * Like {@link #getBlogEntries(AccountName, int, short)
     * getBlogEntries(AccountName, int, short)}, but contains the whole content
     * of the blog entry.
     *
     * @param account
     *            The account to get the blog entries for.
     * @param entryId
     *            The first blog entry id to return.
     * @param limit
     *            The number of results.
     * @return A list of blog entries from the given <code>author</code> based
     *         on the given conditions (<code>entryId</code> and
     *         <code>limit</code>).
     * @throws SteemCommunicationException
     *             <ul>
     *             <li>If the server was not able to answer the request in the
     *             given time (see
     *             {@link eu.bittrade.libs.steemj.configuration.SteemJConfig#setResponseTimeout(long)
     *             setResponseTimeout}).</li>
     *             <li>If there is a connection problem.</li>
     *             <li>If the SteemJ is unable to transform the JSON response
     *             into a Java object.</li>
     *             <li>If the Server returned an error object.</li>
     *             </ul>
     */
    List<CommentBlogEntry> getBlog(AccountName account, int entryId, short limit)
            throws SteemCommunicationException;

    /**
     * Get the reputation for one or more accounts. This method will return the
     * reputation of the {@code limit} number of accounts that mostly match the
     * given {@code accountName}.
     *
     * <p>
     * <b>Example:</b>
     * </p>
     * <p>
     * <code>getAccountReputations(new AccountName("dez1337"), 0);</code>
     * </p>
     * <p>
     * This example will return the reputation of the account "dez1337".
     * </p>
     * <p>
     * <code>getAccountReputations(new AccountName("dez1337"), 1);</code>
     * </p>
     * <p>
     * This example will return the reputation of the account "dez1337" and
     * "dez243", because "dez243" is the most similar account name to "dez1337".
     * </p>
     *
     * @param accountName
     *            The first account name to get the reputation for.
     * @param limit
     *            The number of results.
     * @return A list of
     *         {@link eu.bittrade.libs.steemj.apis.follow.model.AccountReputation
     *         AccountReputation}.
     * @throws SteemCommunicationException
     *             <ul>
     *             <li>If the server was not able to answer the request in the
     *             given time (see
     *             {@link eu.bittrade.libs.steemj.configuration.SteemJConfig#setResponseTimeout(long)
     *             setResponseTimeout}).</li>
     *             <li>If there is a connection problem.</li>
     *             <li>If the SteemJ is unable to transform the JSON response
     *             into a Java object.</li>
     *             <li>If the Server returned an error object.</li>
     *             </ul>
     */
    List<AccountReputation> getAccountReputations(AccountName accountName, int limit)
            throws SteemCommunicationException ;

    /**
     * Gets list of accounts that have reblogged a particular post.
     *
     * @param author
     *            The author of the post to get the rebloggers for.
     * @param permlink
     *            The permlink of the post to get the rebloggers for.
     * @return A list of accounts that have reblogged a particular post.
     * @throws SteemCommunicationException
     *             <ul>
     *             <li>If the server was not able to answer the request in the
     *             given time (see
     *             {@link eu.bittrade.libs.steemj.configuration.SteemJConfig#setResponseTimeout(long)
     *             setResponseTimeout}).</li>
     *             <li>If there is a connection problem.</li>
     *             <li>If the SteemJ is unable to transform the JSON response
     *             into a Java object.</li>
     *             <li>If the Server returned an error object.</li>
     *             </ul>
     */
    List<AccountName> getRebloggedBy(AccountName author, Permlink permlink) throws SteemCommunicationException;

    /**
     * Use this method to find out how many posts of different authors have been
     * resteemed by the given <code>blogAccount</code>.
     *
     * @param blogAccount
     *            The account whose blog should be analyzed.
     * @return A list of pairs, while each pair contains the author name and the
     *         number of blog entries from this author published by the
     *         <code>blogAuthor</code>.
     * @throws SteemCommunicationException
     *             <ul>
     *             <li>If the server was not able to answer the request in the
     *             given time (see
     *             {@link eu.bittrade.libs.steemj.configuration.SteemJConfig#setResponseTimeout(long)
     *             setResponseTimeout}).</li>
     *             <li>If there is a connection problem.</li>
     *             <li>If the SteemJ is unable to transform the JSON response
     *             into a Java object.</li>
     *             <li>If the Server returned an error object.</li>
     *             </ul>
     */
    List<PostsPerAuthorPair> getBlogAuthors(AccountName blogAccount) throws SteemCommunicationException ;
}
