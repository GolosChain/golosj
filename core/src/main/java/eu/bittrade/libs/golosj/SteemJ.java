package eu.bittrade.libs.golosj;

import eu.bittrade.libs.golosj.base.models.*;
import eu.bittrade.libs.golosj.base.models.operations.*;
import eu.bittrade.libs.golosj.communication.BlockAppliedCallback;
import eu.bittrade.libs.golosj.communication.CallbackHub;
import eu.bittrade.libs.golosj.communication.CommunicationHandler;
import eu.bittrade.libs.golosj.communication.dto.RequestWrapperDTO;
import eu.bittrade.libs.golosj.configuration.SteemJConfig;
import eu.bittrade.libs.golosj.enums.AssetSymbolType;
import eu.bittrade.libs.golosj.enums.PrivateKeyType;
import eu.bittrade.libs.golosj.enums.RequestMethods;
import eu.bittrade.libs.golosj.enums.SteemApis;
import eu.bittrade.libs.golosj.exceptions.SteemCommunicationException;
import eu.bittrade.libs.golosj.exceptions.SteemInvalidTransactionException;
import eu.bittrade.libs.golosj.util.CondenserUtils;
import eu.bittrade.libs.golosj.util.SteemJUtils;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.bitcoinj.core.ECKey;
import org.bitcoinj.core.Sha256Hash;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static gcardone.junidecode.Junidecode.unidecode;


/**
 * This class is a wrapper for the Steem web socket API and provides all
 * features known from the Steem CLI Wallet.
 *
 * @author <a href="http://steemit.com/@dez1337">dez1337</a>
 * @deprecated use Golos4J instead
 */
@Deprecated
class SteemJ {
    private static final Logger LOGGER = LoggerFactory.getLogger(SteemJ.class);

    private CommunicationHandler communicationHandler;


    public CommunicationHandler getCommunicationHandler() {
        return communicationHandler;
    }

    SteemJ(boolean unused) {
        this.communicationHandler = new CommunicationHandler();
    }

    // #########################################################################
    // ## NETWORK BROADCAST API ################################################
    // #########################################################################

    /**
     * Broadcast a transaction on the Steem blockchain.
     *
     * @param transaction A transaction object that has been signed.
     * @throws SteemCommunicationException <ul>
     *                                     <li>If the server was not able to answer the request in the
     *                                     given time (see
     *                                     {@link eu.bittrade.libs.golosj.configuration.SteemJConfig#setResponseTimeout(long)
     *                                     setResponseTimeout}).</li>
     *                                     <li>If there is a connection problem.</li>
     *                                     <li>If the SteemJ is unable to transform the JSON response
     *                                     into a Java object.</li>
     *                                     <li>If the Server returned an error object.</li>
     *                                     </ul>
     */
    public void broadcastTransaction(SignedTransaction transaction) throws SteemCommunicationException {
        RequestWrapperDTO requestObject = new RequestWrapperDTO();
        requestObject.setApiMethod(RequestMethods.BROADCAST_TRANSACTION);
        requestObject.setSteemApi(SteemApis.NETWORK_BROADCAST_API);

        // TODO: transaction.sign();
        Object[] parameters = {transaction};
        requestObject.setAdditionalParameters(parameters);

        communicationHandler.performRequest(requestObject, Object.class);
    }

    // TODO implement this!
    public Boolean broadcastTransactionSynchronous(SignedTransaction transaction) throws SteemCommunicationException {
        RequestWrapperDTO requestObject = new RequestWrapperDTO();
        requestObject.setApiMethod(RequestMethods.BROADCAST_TRANSACTION_SYNCHRONOUS);
        requestObject.setSteemApi(SteemApis.NETWORK_BROADCAST_API);
        Object[] parameters = {transaction};
        requestObject.setAdditionalParameters(parameters);

        return null;
    }

    // #########################################################################
    // ## DATABASE API #########################################################
    // #########################################################################


    /**
     * @param accountNames A list of accounts you want to request the details for.
     * @return A List of accounts found for the given account names.
     * @throws SteemCommunicationException <ul>
     *                                     <li>If the server was not able to answer the request in the
     *                                     given time (see
     *                                     {@link eu.bittrade.libs.golosj.configuration.SteemJConfig#setResponseTimeout(long)
     *                                     setResponseTimeout}).</li>
     *                                     <li>If there is a connection problem.</li>
     *                                     <li>If the SteemJ is unable to transform the JSON response
     *                                     into a Java object.</li>
     *                                     <li>If the Server returned an error object.</li>
     *                                     </ul>
     */
    public List<ExtendedAccount> getAccounts(List<AccountName> accountNames) throws SteemCommunicationException {
        RequestWrapperDTO requestObject = new RequestWrapperDTO();
        requestObject.setSteemApi(SteemApis.DATABASE_API);
        requestObject.setApiMethod(RequestMethods.GET_ACCOUNTS);

        // The API expects an array of arrays here.
        String[] innerParameters = new String[accountNames.size()];
        for (int i = 0; i < accountNames.size(); i++) {
            innerParameters[i] = accountNames.get(i).getName();
        }

        String[][] parameters = {innerParameters};

        requestObject.setAdditionalParameters(parameters);
        return communicationHandler.performRequest(requestObject, ExtendedAccount.class);
    }


    /**
     * Get the global properties.
     *
     * @return The dynamic global properties.
     * @throws SteemCommunicationException <ul>
     *                                     <li>If the server was not able to answer the request in the
     *                                     given time (see
     *                                     {@link eu.bittrade.libs.golosj.configuration.SteemJConfig#setResponseTimeout(long)
     *                                     setResponseTimeout}).</li>
     *                                     <li>If there is a connection problem.</li>
     *                                     <li>If the SteemJ is unable to transform the JSON response
     *                                     into a Java object.</li>
     *                                     <li>If the Server returned an error object.</li>
     *                                     </ul>
     */
    public GlobalProperties getDynamicGlobalProperties() throws SteemCommunicationException {
        RequestWrapperDTO requestObject = new RequestWrapperDTO();
        requestObject.setApiMethod(RequestMethods.GET_DYNAMIC_GLOBAL_PROPERTIES);
        requestObject.setSteemApi(SteemApis.DATABASE_API);
        String[] parameters = {};
        requestObject.setAdditionalParameters(parameters);

        return communicationHandler.performRequest(requestObject, GlobalProperties.class).get(0);
    }


    /**
     * Get the hardfork version the node you are connected to is using.
     *
     * @return The hardfork version that the connected node is running on.
     * @throws SteemCommunicationException <ul>
     *                                     <li>If the server was not able to answer the request in the
     *                                     given time (see
     *                                     {@link eu.bittrade.libs.golosj.configuration.SteemJConfig#setResponseTimeout(long)
     *                                     setResponseTimeout}).</li>
     *                                     <li>If there is a connection problem.</li>
     *                                     <li>If the SteemJ is unable to transform the JSON response
     *                                     into a Java object.</li>
     *                                     <li>If the Server returned an error object.</li>
     *                                     </ul>
     */
    public String getHardforkVersion() throws SteemCommunicationException {
        RequestWrapperDTO requestObject = new RequestWrapperDTO();
        requestObject.setApiMethod(RequestMethods.GET_HARDFORK_VERSION);
        requestObject.setSteemApi(SteemApis.DATABASE_API);
        String[] parameters = {};
        requestObject.setAdditionalParameters(parameters);

        return communicationHandler.performRequest(requestObject, String.class).get(0);
    }

    /**
     * Search for users under the use of their public key(s).
     *
     * @param publicKeys An array containing one or more public keys.
     * @return A list of arrays containing the matching account names.
     * @throws SteemCommunicationException <ul>
     *                                     <li>If the server was not able to answer the request in the
     *                                     given time (see
     *                                     {@link eu.bittrade.libs.golosj.configuration.SteemJConfig#setResponseTimeout(long)
     *                                     setResponseTimeout}).</li>
     *                                     <li>If there is a connection problem.</li>
     *                                     <li>If the SteemJ is unable to transform the JSON response
     *                                     into a Java object.</li>
     *                                     <li>If the Server returned an error object.</li>
     *                                     </ul>
     */
    public List<String[]> getKeyReferences(String[] publicKeys) throws SteemCommunicationException {
        RequestWrapperDTO requestObject = new RequestWrapperDTO();
        requestObject.setApiMethod(RequestMethods.GET_KEY_REFERENCES);
        requestObject.setSteemApi(SteemApis.ACCOUNT_BY_KEY_API);
        Object[] parameters = {publicKeys};
        requestObject.setAdditionalParameters(parameters);

        return communicationHandler.performRequest(requestObject, String[].class);
    }


    // TODO implement this!
    public List<String[]> getPotentialSignatures() throws SteemCommunicationException {
        RequestWrapperDTO requestObject = new RequestWrapperDTO();
        requestObject.setApiMethod(RequestMethods.GET_POTENTIAL_SIGNATURES);
        requestObject.setSteemApi(SteemApis.DATABASE_API);
        Object[] parameters = {};
        requestObject.setAdditionalParameters(parameters);
        LOGGER.info("output: {}", communicationHandler.performRequest(requestObject, Object[].class));
        return null;
    }


    /**
     * Use the Steem API to verify the required authorities for this
     * transaction.
     *
     * @param signedTransaction A {@link SignedTransaction} transaction which has been signed.
     * @return <code>true</code> if the given transaction has been signed
     * correctly, otherwise an Exception will be thrown.
     * @throws SteemCommunicationException <ul>
     *                                     <li>If the server was not able to answer the request in the
     *                                     given time (see
     *                                     {@link eu.bittrade.libs.golosj.configuration.SteemJConfig#setResponseTimeout(long)
     *                                     setResponseTimeout}).</li>
     *                                     <li>If there is a connection problem.</li>
     *                                     <li>If the SteemJ is unable to transform the JSON response
     *                                     into a Java object.</li>
     *                                     <li>If the Server returned an error object.</li>
     *                                     </ul>
     */
    public Boolean verifyAuthority(SignedTransaction signedTransaction) throws SteemCommunicationException {
        RequestWrapperDTO requestObject = new RequestWrapperDTO();
        requestObject.setApiMethod(RequestMethods.VERIFY_AUTHORITY);
        requestObject.setSteemApi(SteemApis.DATABASE_API);

        Object[] parameters = {signedTransaction};
        requestObject.setAdditionalParameters(parameters);
        // The method does not simply return false, it throws an error
        // describing the problem.
        return communicationHandler.performRequest(requestObject, Boolean.class).get(0);
    }

    /**
     * Use this method to register a callback method that is called whenever a
     * new block has been applied.
     * <p>
     * <p>
     * <b>Notice:</b>
     * <p>
     * That there can only be one active Callback. If you call this method
     * multiple times with different callback methods, only the last one will be
     * called.
     * <p>
     * Beside that there is currently no way to cancel a subscription. Once
     * you've registered a callback it will be called until the connection has
     * been closed.
     * </p>
     *
     * @param blockAppliedCallback A class implementing the
     *                             {@link eu.bittrade.libs.golosj.communication.BlockAppliedCallback
     *                             BlockAppliedCallback}.
     * @throws SteemCommunicationException <ul>
     *                                     <li>If the server was not able to answer the request in the
     *                                     given time (see
     *                                     {@link eu.bittrade.libs.golosj.configuration.SteemJConfig#setResponseTimeout(long)
     *                                     setResponseTimeout}).</li>
     *                                     <li>If there is a connection problem.</li>
     *                                     <li>If the SteemJ is unable to transform the JSON response
     *                                     into a Java object.</li>
     *                                     <li>If the Server returned an error object.</li>
     *                                     </ul>
     */
    public void setBlockAppliedCallback(BlockAppliedCallback blockAppliedCallback) throws SteemCommunicationException {
        // Register the given callback at the callback hub.
        CallbackHub.getInstance().addCallback(blockAppliedCallback);

        // Register the callback at the steem node.
        RequestWrapperDTO requestObject = new RequestWrapperDTO();
        requestObject.setApiMethod(RequestMethods.SET_BLOCK_APPLIED_CALLBACK);
        requestObject.setSteemApi(SteemApis.DATABASE_API);

        Object[] parameters = {blockAppliedCallback.getUuid()};
        requestObject.setAdditionalParameters(parameters);

        communicationHandler.performRequest(requestObject, Object.class);
    }


    // #########################################################################
    // ## UTILITY METHODS ######################################################
    // #########################################################################

    /**
     * Get the private and public key of a given type for the given
     * <code>account</code>
     *
     * @param account       The account name to generate the passwords for.
     * @param role          The key type that should be generated.
     * @param steemPassword The password of the <code>account</code> valid for the Steem
     *                      blockchain.
     * @return The requested key pair.
     */
    public static ImmutablePair<PublicKey, String> getPrivateKeyFromPassword(AccountName account, PrivateKeyType role,
                                                                             String steemPassword) {
        String seed = account.getName() + role.name().toLowerCase() + steemPassword;
        ECKey keyPair = ECKey.fromPrivate(Sha256Hash.hash(seed.getBytes(), 0, seed.length()));

        return new ImmutablePair<>(new PublicKey(keyPair), SteemJUtils.privateKeyToWIF(keyPair));
    }

    // #########################################################################
    // ## SIMPLIFIED OPERATIONS ################################################
    // #########################################################################

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
    public void vote(AccountName postOrCommentAuthor, Permlink postOrCommentPermlink, short percentage)
            throws SteemCommunicationException, SteemInvalidTransactionException {
        if (SteemJConfig.getInstance().getDefaultAccount().isEmpty()) {
            throw new InvalidParameterException(
                    "Using the upVote method without providing an account requires to have a default account configured.");
        }

        this.vote(SteemJConfig.getInstance().getDefaultAccount(), postOrCommentAuthor, postOrCommentPermlink,
                percentage);
    }

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
    public void vote(AccountName voter, AccountName postOrCommentAuthor, Permlink postOrCommentPermlink,
                     short percentage) throws SteemCommunicationException, SteemInvalidTransactionException {
        if (percentage < -100 || percentage > 100 || percentage == 0) {
            throw new InvalidParameterException(
                    "Please provide a percentage between -100 and 100 which is also not 0.");
        }

        VoteOperation voteOperation = new VoteOperation(voter, postOrCommentAuthor, postOrCommentPermlink,
                (short) (percentage * 100));

        ArrayList<Operation> operations = new ArrayList<>();
        operations.add(voteOperation);

        GlobalProperties globalProperties = this.getDynamicGlobalProperties();

        SignedTransaction signedTransaction = new SignedTransaction(globalProperties.getHeadBlockId(), operations,
                null);

        signedTransaction.sign();

        this.broadcastTransaction(signedTransaction);
    }

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
    public void cancelVote(AccountName postOrCommentAuthor, Permlink postOrCommentPermlink)
            throws SteemCommunicationException, SteemInvalidTransactionException {
        if (SteemJConfig.getInstance().getDefaultAccount().isEmpty()) {
            throw new InvalidParameterException(
                    "Using the cancelVote method without providing an account requires to have a default account configured.");
        }
        cancelVote(SteemJConfig.getInstance().getDefaultAccount(), postOrCommentAuthor, postOrCommentPermlink);
    }

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
    public void cancelVote(AccountName voter, AccountName postOrCommentAuthor, Permlink postOrCommentPermlink)
            throws SteemCommunicationException, SteemInvalidTransactionException {
        VoteOperation voteOperation = new VoteOperation(voter, postOrCommentAuthor, postOrCommentPermlink, (short) 0);

        ArrayList<Operation> operations = new ArrayList<>();
        operations.add(voteOperation);

        GlobalProperties globalProperties = this.getDynamicGlobalProperties();

        SignedTransaction signedTransaction = new SignedTransaction(globalProperties.getHeadBlockId(), operations,
                null);

        signedTransaction.sign();

        this.broadcastTransaction(signedTransaction);
    }

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
    public void follow(AccountName accountToFollow)
            throws SteemCommunicationException, SteemInvalidTransactionException {
        if (SteemJConfig.getInstance().getDefaultAccount().isEmpty()) {
            throw new InvalidParameterException(
                    "Using the follow method without providing an account requires to have a default account configured.");
        }

        follow(SteemJConfig.getInstance().getDefaultAccount(), accountToFollow);
    }

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
    public void follow(AccountName accountThatFollows, AccountName accountToFollow)
            throws SteemCommunicationException, SteemInvalidTransactionException {
        ArrayList<AccountName> requiredPostingAuths = new ArrayList<>();
        requiredPostingAuths.add(accountThatFollows);

        String id = "follow";
        String json = "[\"follow\",{\"follower\":\"" + accountThatFollows.getName() + "\",\"following\":\""
                + accountToFollow.getName() + "\",\"what\":[\"blog\"]}]";

        CustomJsonOperation customJsonOperation = new CustomJsonOperation(null, requiredPostingAuths, id, json);

        ArrayList<Operation> operations = new ArrayList<>();
        operations.add(customJsonOperation);

        GlobalProperties globalProperties = this.getDynamicGlobalProperties();

        SignedTransaction signedTransaction = new SignedTransaction(globalProperties.getHeadBlockId(), operations,
                null);

        signedTransaction.sign();

        this.broadcastTransaction(signedTransaction);
    }

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
    public void unfollow(AccountName accountToUnfollow)
            throws SteemCommunicationException, SteemInvalidTransactionException {
        if (SteemJConfig.getInstance().getDefaultAccount().isEmpty()) {
            throw new InvalidParameterException(
                    "Using the unfollow method without providing an account requires to have a default account configured.");
        }

        unfollow(SteemJConfig.getInstance().getDefaultAccount(), accountToUnfollow);
    }

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
    public void unfollow(AccountName accountThatUnfollows, AccountName accountToUnfollow)
            throws SteemCommunicationException, SteemInvalidTransactionException {
        ArrayList<AccountName> requiredPostingAuths = new ArrayList<>();
        requiredPostingAuths.add(accountThatUnfollows);

        String id = "follow";
        String json = "[\"follow\",{\"follower\":\"" + accountThatUnfollows.getName() + "\",\"following\":\""
                + accountToUnfollow.getName() + "\",\"what\":[\"\"]}]";

        CustomJsonOperation customJsonOperation = new CustomJsonOperation(null, requiredPostingAuths, id, json);

        ArrayList<Operation> operations = new ArrayList<>();
        operations.add(customJsonOperation);

        GlobalProperties globalProperties = this.getDynamicGlobalProperties();

        SignedTransaction signedTransaction = new SignedTransaction(globalProperties.getHeadBlockId(), operations,
                null);

        signedTransaction.sign();

        this.broadcastTransaction(signedTransaction);
    }

    /**
     * @param title
     * @param content
     * @param tags
     * @throws SteemCommunicationException
     * @throws SteemInvalidTransactionException
     */
    public CommentOperation createPost(String title, String content, String[] tags)
            throws SteemCommunicationException, SteemInvalidTransactionException {
        if (SteemJConfig.getInstance().getDefaultAccount().isEmpty()) {
            throw new InvalidParameterException(
                    "Using the unfollow method without providing an account requires to have a default account configured.");
        }

        return createPost(SteemJConfig.getInstance().getDefaultAccount(), title, content, tags);
    }

    /**
     * @param authorThatPublishsThePost
     * @param title
     * @param content
     * @param tags
     * @throws SteemCommunicationException
     * @throws SteemInvalidTransactionException
     */
    public CommentOperation createPost(AccountName authorThatPublishsThePost, String title, String content,
                                       String[] tags) throws SteemCommunicationException, SteemInvalidTransactionException {
        if (tags == null || tags.length < 1 || tags.length > 5) {
            throw new InvalidParameterException("You need to provide at least one tag, but not more than five.");
        }
        ArrayList<Operation> operations = new ArrayList<>();

        // Generate the permanent link from the title by replacing all unallowed
        // characters.
        Permlink permlink = new Permlink(unidecode(title.toLowerCase()).replaceAll("[^a-z0-9-]+", "") + UUID.randomUUID().toString().substring(0, 8));
        // On new posts the parentPermlink is the main tag.
        Permlink parentPermlink = new Permlink(tags[0]);
        // One new posts the parentAuthor is empty.
        AccountName parentAuthor = new AccountName("");

        String jsonMetadata = CondenserUtils.generateSteemitMetadata(content, tags, "golos4J/0.0.3", "markdown");

        CommentOperation commentOperation = new CommentOperation(parentAuthor, parentPermlink,
                authorThatPublishsThePost, permlink, title, content, jsonMetadata);

        operations.add(commentOperation);

        boolean allowVotes = true;
        boolean allowCurationRewards = true;
        short percentSteemDollars = (short) 10000;
        Asset maxAcceptedPayout = new Asset(1000000000, AssetSymbolType.GBG);

        BeneficiaryRouteType beneficiaryRouteType = new BeneficiaryRouteType(SteemJConfig.getSteemJAccount(),
                SteemJConfig.getInstance().getSteemJWeight());


        ArrayList<BeneficiaryRouteType> beneficiaryRouteTypes = new ArrayList<>();
        beneficiaryRouteTypes.add(beneficiaryRouteType);

        CommentPayoutBeneficiaries commentPayoutBeneficiaries = new CommentPayoutBeneficiaries();
        commentPayoutBeneficiaries.setBeneficiaries(beneficiaryRouteTypes);

        ArrayList<CommentOptionsExtension> commentOptionsExtensions = new ArrayList<>();
        commentOptionsExtensions.add(commentPayoutBeneficiaries);

        CommentOptionsOperation commentOptionsOperation = new CommentOptionsOperation(authorThatPublishsThePost,
                permlink, maxAcceptedPayout, percentSteemDollars, allowVotes, allowCurationRewards,
                commentOptionsExtensions);

        operations.add(commentOptionsOperation);


        GlobalProperties globalProperties = this.getDynamicGlobalProperties();

        SignedTransaction signedTransaction = new SignedTransaction(globalProperties.getHeadBlockId(), operations,
                null);

        signedTransaction.sign();

        this.broadcastTransaction(signedTransaction);

        return commentOperation;
    }

    /**
     * @param authorOfThePostOrCommentToReplyTo
     * @param permlinkOfThePostOrCommentToReplyTo
     * @param content
     * @param tags
     * @throws SteemCommunicationException
     * @throws SteemInvalidTransactionException
     */
    public CommentOperation createComment(AccountName authorOfThePostOrCommentToReplyTo,
                                          Permlink permlinkOfThePostOrCommentToReplyTo, String content, String[] tags)
            throws SteemCommunicationException, SteemInvalidTransactionException {
        if (SteemJConfig.getInstance().getDefaultAccount().isEmpty()) {
            throw new InvalidParameterException(
                    "Using the unfollow method without providing an account requires to have a default account configured.");
        }

        return createComment(authorOfThePostOrCommentToReplyTo, permlinkOfThePostOrCommentToReplyTo,
                SteemJConfig.getInstance().getDefaultAccount(), content, tags);
    }

    /**
     * @param authorOfThePostOrCommentToReplyTo
     * @param permlinkOfThePostOrCommentToReplyTo
     * @param authorThatPublishsTheComment
     * @param content
     * @param tags
     * @throws SteemCommunicationException
     * @throws SteemInvalidTransactionException
     */
    public CommentOperation createComment(AccountName authorOfThePostOrCommentToReplyTo,
                                          Permlink permlinkOfThePostOrCommentToReplyTo, AccountName authorThatPublishsTheComment, String content,
                                          String[] tags) throws SteemCommunicationException, SteemInvalidTransactionException {
        if (tags == null || tags.length < 1 || tags.length > 5) {
            throw new InvalidParameterException("You need to provide at least one tag, but not more than five.");
        }
        ArrayList<Operation> operations = new ArrayList<>();

        // Generate the permanent link by adding the current timestamp and a
        // UUID.
        String permlinkString = ("re-" + authorOfThePostOrCommentToReplyTo.getName() + "-"
                + permlinkOfThePostOrCommentToReplyTo.getLink() + "-" + System.currentTimeMillis() + "t"
                + UUID.randomUUID().toString().substring(8) + "uid").toLowerCase();
        if (!permlinkString.matches("^[a-z0-9\\-]{0,256}")) {
            permlinkString = "re-" + UUID.randomUUID().toString();
        }
        Permlink permlink = new Permlink(permlinkString);

        String jsonMetadata = CondenserUtils.generateSteemitMetadata(content, tags, "golos4J/0.0.3", "markdown");

        CommentOperation commentOperation = new CommentOperation(authorOfThePostOrCommentToReplyTo,
                permlinkOfThePostOrCommentToReplyTo, authorThatPublishsTheComment, permlink, "", content, jsonMetadata);

        operations.add(commentOperation);

        boolean allowVotes = true;
        boolean allowCurationRewards = true;
        short percentSteemDollars = (short) 10000;
        Asset maxAcceptedPayout = new Asset(1000000000, AssetSymbolType.GBG);

        BeneficiaryRouteType beneficiaryRouteType = new BeneficiaryRouteType(SteemJConfig.getSteemJAccount(),
                SteemJConfig.getInstance().getSteemJWeight());

        ArrayList<BeneficiaryRouteType> beneficiaryRouteTypes = new ArrayList<>();
        beneficiaryRouteTypes.add(beneficiaryRouteType);

        CommentPayoutBeneficiaries commentPayoutBeneficiaries = new CommentPayoutBeneficiaries();
        commentPayoutBeneficiaries.setBeneficiaries(beneficiaryRouteTypes);

        ArrayList<CommentOptionsExtension> commentOptionsExtensions = new ArrayList<>();
        commentOptionsExtensions.add(commentPayoutBeneficiaries);

        CommentOptionsOperation commentOptionsOperation = new CommentOptionsOperation(authorThatPublishsTheComment,
                permlink, maxAcceptedPayout, percentSteemDollars, allowVotes, allowCurationRewards,
                commentOptionsExtensions);

        operations.add(commentOptionsOperation);

        GlobalProperties globalProperties = this.getDynamicGlobalProperties();

        SignedTransaction signedTransaction = new SignedTransaction(globalProperties.getHeadBlockId(), operations,
                null);

        signedTransaction.sign();

        this.broadcastTransaction(signedTransaction);

        return commentOperation;
    }

    /**
     * @param permlinkOfThePostToUpdate
     * @param title
     * @param content
     * @param tags
     * @throws SteemCommunicationException
     * @throws SteemInvalidTransactionException
     */
    public CommentOperation updatePost(Permlink permlinkOfThePostToUpdate, String title, String content, String[] tags)
            throws SteemCommunicationException, SteemInvalidTransactionException {
        if (SteemJConfig.getInstance().getDefaultAccount().isEmpty()) {
            throw new InvalidParameterException(
                    "Using the unfollow method without providing an account requires to have a default account configured.");
        }

        return updatePost(SteemJConfig.getInstance().getDefaultAccount(), permlinkOfThePostToUpdate, title, content,
                tags);
    }

    /**
     * @param authorOfThePostToUpdate
     * @param permlinkOfThePostToUpdate
     * @param title
     * @param content
     * @param tags
     * @throws SteemCommunicationException
     * @throws SteemInvalidTransactionException
     */
    public CommentOperation updatePost(AccountName authorOfThePostToUpdate, Permlink permlinkOfThePostToUpdate,
                                       String title, String content, String[] tags)
            throws SteemCommunicationException, SteemInvalidTransactionException {
        if (tags == null || tags.length < 1 || tags.length > 5) {
            throw new InvalidParameterException("You need to provide at least one tag, but not more than five.");
        }

        ArrayList<Operation> operations = new ArrayList<>();
        AccountName parentAuthor = new AccountName("");
        Permlink parentPermlink = new Permlink(tags[0]);

        String jsonMetadata = CondenserUtils.generateSteemitMetadata(content, tags, "golos4J/0.0.3", "markdown");

        CommentOperation commentOperation = new CommentOperation(parentAuthor, parentPermlink, authorOfThePostToUpdate,
                permlinkOfThePostToUpdate, title, content, jsonMetadata);

        operations.add(commentOperation);

        GlobalProperties globalProperties = this.getDynamicGlobalProperties();

        SignedTransaction signedTransaction = new SignedTransaction(globalProperties.getHeadBlockId(), operations,
                null);

        signedTransaction.sign();

        this.broadcastTransaction(signedTransaction);

        return commentOperation;
    }

    /**
     * @param parentAuthor
     * @param parentPermlink
     * @param originalPermlinkOfYourComment
     * @param content
     * @param tags
     * @throws SteemCommunicationException
     * @throws SteemInvalidTransactionException
     */
    public CommentOperation updateComment(AccountName parentAuthor, Permlink parentPermlink,
                                          Permlink originalPermlinkOfYourComment, String content, String[] tags)
            throws SteemCommunicationException, SteemInvalidTransactionException {
        if (SteemJConfig.getInstance().getDefaultAccount().isEmpty()) {
            throw new InvalidParameterException(
                    "Using the unfollow method without providing an account requires to have a default account configured.");
        }

        return updateComment(parentAuthor, parentPermlink, originalPermlinkOfYourComment,
                SteemJConfig.getInstance().getDefaultAccount(), content, tags);
    }

    /**
     * @param parentAuthor
     * @param parentPermlink
     * @param originalPermlinkOfTheCommentToUpdate
     * @param originalAuthorOfTheCommentToUpdate
     * @param content
     * @param tags
     * @return The {@link CommentOperation} which has been created within this
     * method. The returned Operation allows you to access the generated
     * values.
     * @throws SteemCommunicationException
     * @throws SteemInvalidTransactionException
     */
    public CommentOperation updateComment(AccountName parentAuthor, Permlink parentPermlink,
                                          Permlink originalPermlinkOfTheCommentToUpdate, AccountName originalAuthorOfTheCommentToUpdate,
                                          String content, String[] tags) throws SteemCommunicationException, SteemInvalidTransactionException {
        if (tags == null || tags.length < 1 || tags.length > 5) {
            throw new InvalidParameterException("You need to provide at least one tag, but not more than five.");
        }
        ArrayList<Operation> operations = new ArrayList<>();

        String jsonMetadata = CondenserUtils.generateSteemitMetadata(content, tags, "golos4J/0.0.3", "markdown");

        CommentOperation commentOperation = new CommentOperation(parentAuthor, parentPermlink,
                originalAuthorOfTheCommentToUpdate, originalPermlinkOfTheCommentToUpdate, "", content, jsonMetadata);

        operations.add(commentOperation);
        GlobalProperties globalProperties = this.getDynamicGlobalProperties();

        SignedTransaction signedTransaction = new SignedTransaction(globalProperties.getHeadBlockId(), operations,
                null);

        signedTransaction.sign();

        this.broadcastTransaction(signedTransaction);

        return commentOperation;
    }

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
    public void deletePostOrComment(Permlink postOrCommentPermlink)
            throws SteemCommunicationException, SteemInvalidTransactionException {
        if (SteemJConfig.getInstance().getDefaultAccount().isEmpty()) {
            throw new InvalidParameterException(
                    "Using the upVote method without providing an account requires to have a default account configured.");
        }

        deletePostOrComment(SteemJConfig.getInstance().getDefaultAccount(), postOrCommentPermlink);
    }

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
    public void deletePostOrComment(AccountName postOrCommentAuthor, Permlink postOrCommentPermlink)
            throws SteemCommunicationException, SteemInvalidTransactionException {
        DeleteCommentOperation deleteCommentOperation = new DeleteCommentOperation(postOrCommentAuthor,
                postOrCommentPermlink);

        ArrayList<Operation> operations = new ArrayList<>();
        operations.add(deleteCommentOperation);

        GlobalProperties globalProperties = this.getDynamicGlobalProperties();

        SignedTransaction signedTransaction = new SignedTransaction(globalProperties.getHeadBlockId(), operations,
                null);

        signedTransaction.sign();

        this.broadcastTransaction(signedTransaction);
    }
}
