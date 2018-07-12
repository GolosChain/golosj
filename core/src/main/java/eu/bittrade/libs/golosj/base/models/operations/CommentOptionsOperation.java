package eu.bittrade.libs.golosj.base.models.operations;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import eu.bittrade.libs.golosj.base.models.AccountName;
import eu.bittrade.libs.golosj.base.models.Asset;
import eu.bittrade.libs.golosj.base.models.CommentOptionsExtension;
import eu.bittrade.libs.golosj.base.models.Permlink;
import eu.bittrade.libs.golosj.enums.AssetSymbolType;
import eu.bittrade.libs.golosj.enums.OperationType;
import eu.bittrade.libs.golosj.enums.PrivateKeyType;
import eu.bittrade.libs.golosj.enums.ValidationType;
import eu.bittrade.libs.golosj.exceptions.SteemInvalidTransactionException;
import eu.bittrade.libs.golosj.interfaces.SignatureObject;
import eu.bittrade.libs.golosj.util.SteemJUtils;

/**
 * This class represents the Steem "comment_options_operation" object.
 * 
 * @author <a href="http://steemit.com/@dez1337">dez1337</a>
 */
public class CommentOptionsOperation extends Operation {
    @JsonProperty("author")
    private AccountName author;
    @JsonProperty("permlink")
    private Permlink permlink;
    @JsonProperty("max_accepted_payout")
    private Asset maxAcceptedPayout;
    @JsonProperty("percent_steem_dollars")
    private Short percentSteemDollars;
    @JsonProperty("allow_votes")
    private Boolean allowVotes;
    @JsonProperty("allow_curation_rewards")
    private Boolean allowCurationRewards;
    // Original type is "comment_options_extensions_type" which is a list of
    // "comment_options_extension".
    @JsonProperty("extensions")
    private List<CommentOptionsExtension> extensions;

    /**
     * Create a new comment options operation. Use this operation to define
     * additional options for an already existing post or a comment.
     * 
     * Authors of posts may not want all of the benefits that come from creating
     * a post. This operation allows authors to update properties associated
     * with their post.
     *
     * The <code>maxAcceptedPayout</code> may be decreased, but never increased.
     * The <code>percentSteemDollars</code> may be decreased, but never
     * increased
     * 
     * @param author
     *            The author of the post this operation should apply to (see
     *            {@link #setAuthor(AccountName)}).
     * @param permlink
     *            The permlink of the post this operation should apply to (see
     *            {@link #setPermlink(Permlink)}).
     * @param maxAcceptedPayout
     *            The maximal excepted payout (see
     *            {@link #setMaxAcceptedPayout(Asset)}).
     * @param percentSteemDollars
     *            The percent of Steem dollars the reward should be paid in (see
     *            {@link #setPercentSteemDollars(Short)}).
     * @param allowVotes
     *            Define if votes are allowed (see
     *            {@link #setAllowVotes(Boolean)}).
     * @param allowCurationRewards
     *            Define if a curation reward should be paid (see
     *            {@link #setAllowCurationRewards(Boolean)}).
     * @param extensions
     *            Additional extensions to set (see
     *            {@link #setExtensions(List)}.
     * @throws InvalidParameterException
     *             If one of the parameters does not fulfill the requirements.
     */
    @JsonCreator
    public CommentOptionsOperation(@JsonProperty("author") AccountName author,
            @JsonProperty("permlink") Permlink permlink, @JsonProperty("max_accepted_payout") Asset maxAcceptedPayout,
            @JsonProperty("percent_steem_dollars") Short percentSteemDollars,
            @JsonProperty("allow_votes") Boolean allowVotes,
            @JsonProperty("allow_curation_rewards") Boolean allowCurationRewards,
            @JsonProperty("extensions") List<CommentOptionsExtension> extensions) {
        super(false);

        this.setAuthor(author);
        this.setPermlink(permlink);
        this.setMaxAcceptedPayout(maxAcceptedPayout);
        this.setPercentSteemDollars(percentSteemDollars);
        this.setAllowVotes(allowVotes);
        this.setAllowCurationRewards(allowCurationRewards);
        this.setExtensions(extensions);

    }

    /**
     * Like
     * {@link #CommentOptionsOperation(AccountName, Permlink, Asset, Short, Boolean, Boolean, List)},
     * but sets the maximum payout to the highest possible value, allows votes
     * and curation rewards.
     * 
     * @param author
     *            The author of the post this operation should apply to (see
     *            {@link #setAuthor(AccountName)}).
     * @param permlink
     *            The permlink of the post this operation should apply to (see
     *            {@link #setPermlink(Permlink)}).
     * @param percentSteemDollars
     *            The percent of Steem dollars the reward should be paid in (see
     *            {@link #setPercentSteemDollars(Short)}).
     * @param extensions
     *            Additional extensions to set (see
     *            {@link #setExtensions(List)}.
     * @throws InvalidParameterException
     *             If one of the parameters does not fulfill the requirements.
     */
    public CommentOptionsOperation(AccountName author, Permlink permlink, Short percentSteemDollars,
            List<CommentOptionsExtension> extensions) {
        this(author, permlink, new Asset(1000000000, AssetSymbolType.GOLOS), percentSteemDollars, true, true, extensions);
    }

    /**
     * Like
     * {@link #CommentOptionsOperation(AccountName, Permlink, Short, List)}, but
     * does not require extensions.
     * 
     * @param author
     *            The author of the post this operation should apply to (see
     *            {@link #setAuthor(AccountName)}).
     * @param permlink
     *            The permlink of the post this operation should apply to (see
     *            {@link #setPermlink(Permlink)}).
     * @param percentSteemDollars
     *            The percent of Steem dollars the reward should be paid in (see
     *            {@link #setPercentSteemDollars(Short)}).
     * @throws InvalidParameterException
     *             If one of the parameters does not fulfill the requirements.
     */
    public CommentOptionsOperation(AccountName author, Permlink permlink, Short percentSteemDollars) {
        this(author, permlink, percentSteemDollars, new ArrayList<CommentOptionsExtension>());
    }

    /**
     * Like
     * {@link #CommentOptionsOperation(AccountName, Permlink, Asset, Short, Boolean, Boolean, List)},
     * but sets the maximum payout to the highest possible value, allows votes,
     * allows curation rewards and sets the <code>percentSteemDollars</code> to
     * 100.0%.
     * 
     * @param author
     *            The author of the post this operation should apply to (see
     *            {@link #setAuthor(AccountName)}).
     * @param permlink
     *            The permlink of the post this operation should apply to (see
     *            {@link #setPermlink(Permlink)}).
     * @param extensions
     *            Additional extensions to set (see
     *            {@link #setExtensions(List)}.
     * @throws InvalidParameterException
     *             If one of the parameters does not fulfill the requirements.
     */
    public CommentOptionsOperation(AccountName author, Permlink permlink, List<CommentOptionsExtension> extensions) {
        this(author, permlink, (short) 10000, extensions);
    }

    /**
     * Like {@link #CommentOptionsOperation(AccountName, Permlink, List)}, but
     * does not require extensions.
     * 
     * @param author
     *            The author of the post this operation should apply to (see
     *            {@link #setAuthor(AccountName)}).
     * @param permlink
     *            The permlink of the post this operation should apply to (see
     *            {@link #setPermlink(Permlink)}).
     * @throws InvalidParameterException
     *             If one of the parameters does not fulfill the requirements.
     */
    public CommentOptionsOperation(AccountName author, Permlink permlink) {
        this(author, permlink, new ArrayList<CommentOptionsExtension>());
    }

    /**
     * Get the author of the comment.
     * 
     * @return The account name of the author.
     */
    public AccountName getAuthor() {
        return author;
    }

    /**
     * Set the author of the comment. <b>Notice:</b> The private posting key of
     * this account needs to be stored in the key storage.
     * 
     * @param author
     *            The account name of the author.
     * @throws InvalidParameterException
     *             If the <code>author</code> is null.
     */
    public void setAuthor(AccountName author) {
        if (author == null) {
            throw new InvalidParameterException("The author can't be null.");
        }

        this.author = author;
    }

    /**
     * Get the permanent link of this comment.
     * 
     * @return The permanent link.
     */
    public Permlink getPermlink() {
        return permlink;
    }

    /**
     * Set the permanent link of this comment.
     * 
     * @param permlink
     *            The permanent link.
     * @throws InvalidParameterException
     *             If the <code>permlink</code> is null.
     */
    public void setPermlink(Permlink permlink) {
        if (permlink == null) {
            throw new InvalidParameterException("The permanent link can't be null.");
        }

        this.permlink = permlink;
    }

    /**
     * Get the SBD value of the maximum payout this post will receive.
     * 
     * @return The SBD value of the maximum payout this post will receive.
     */
    public Asset getMaxAcceptedPayout() {
        return maxAcceptedPayout;
    }

    /**
     * Set the SBD value of the maximum payout this post will receive.
     * 
     * @param maxAcceptedPayout
     *            The SBD value of the maximum payout this post will receive.
     * @throws InvalidParameterException
     *             If the <code>maxAcceptedPayout</code> is null, has a
     *             different symbol than SBD or the amount is less than 0.
     */
    public void setMaxAcceptedPayout(Asset maxAcceptedPayout) {
        if (maxAcceptedPayout == null) {
            throw new InvalidParameterException("The maximal accepted payout can't be null.");
        } else if (!maxAcceptedPayout.getSymbol().equals(AssetSymbolType.GBG)) {
            throw new InvalidParameterException("The maximal accepted payout must be in SBD.");
        } else if (maxAcceptedPayout.getAmount() < 0) {
            throw new InvalidParameterException("Cannot accept less than 0 payout.");
        }

        this.maxAcceptedPayout = maxAcceptedPayout;
    }

    /**
     * Get the information if votes have been allowed on this post or comment.
     * 
     * @return True if votes are allowed or false if not.
     */
    public Boolean getAllowVotes() {
        return allowVotes;
    }

    /**
     * Define if votes have been allowed on this post or comment.
     * 
     * @param allowVotes
     *            The information if votes have been allowed on this post or
     *            comment.
     */
    public void setAllowVotes(Boolean allowVotes) {
        this.allowVotes = allowVotes;
    }

    /**
     * Get the information if voters are allowed to receive curation rewards.
     * 
     * @return True if votes curation rewards are paid or false if not.
     */
    public Boolean getAllowCurationRewards() {
        return allowCurationRewards;
    }

    /**
     * Define if voters are allowed to receive curation rewards.
     * 
     * @param allowCurationRewards
     *            True if votes curation rewards are paid or false if not.
     */
    public void setAllowCurationRewards(Boolean allowCurationRewards) {
        this.allowCurationRewards = allowCurationRewards;
    }

    /**
     * Get the percent of Steem Dollars to keep, unkept amounts will be received
     * as Steem Power. The default value is 10000 which is equal to 100%.
     * 
     * @return The percent of Steem Dollars.
     */
    public int getPercentSteemDollars() {
        return (int) percentSteemDollars;
    }

    /**
     * @param percentSteemDollars
     *            The percent of Steem Dollars.
     * @throws InvalidParameterException
     *             If the <code>percentSteemDollars</code> is higher than 10000
     *             which is equal to 100.00%.
     */
    public void setPercentSteemDollars(Short percentSteemDollars) {
        if (percentSteemDollars > 10000) {
            throw new InvalidParameterException(
                    "The percent of steem dollars can't be higher than 10000 which is equivalent to 100%.");
        }

        this.percentSteemDollars = percentSteemDollars;
    }

    /**
     * Get the list of configured extensions.
     * 
     * @return All extensions.
     */
    public List<CommentOptionsExtension> getExtensions() {
        if (extensions == null || extensions.isEmpty()) {
            // Create a new ArrayList to avoid a NullPointerException.
            extensions = new ArrayList<>();
        }
        return extensions;
    }

    /**
     * Extensions are currently not supported and will be ignored.
     * 
     * @param extensions
     *            Define a list of extensions.
     */
    public void setExtensions(List<CommentOptionsExtension> extensions) {
        this.extensions = extensions;
    }

    @Override
    public byte[] toByteArray() throws SteemInvalidTransactionException {
        try (ByteArrayOutputStream serializedCommentOptionsOperation = new ByteArrayOutputStream()) {
            serializedCommentOptionsOperation.write(
                    SteemJUtils.transformIntToVarIntByteArray(OperationType.COMMENT_OPTIONS_OPERATION.ordinal()));
            serializedCommentOptionsOperation.write(this.getAuthor().toByteArray());
            serializedCommentOptionsOperation.write(this.getPermlink().toByteArray());
            serializedCommentOptionsOperation.write(this.getMaxAcceptedPayout().toByteArray());
            serializedCommentOptionsOperation
                    .write(SteemJUtils.transformShortToByteArray(this.getPercentSteemDollars()));
            serializedCommentOptionsOperation.write(SteemJUtils.transformBooleanToByteArray(this.getAllowVotes()));
            serializedCommentOptionsOperation
                    .write(SteemJUtils.transformBooleanToByteArray(this.getAllowCurationRewards()));

            serializedCommentOptionsOperation
                    .write(SteemJUtils.transformLongToVarIntByteArray(this.getExtensions().size()));

            for (CommentOptionsExtension commentOptionsExtension : this.getExtensions()) {
                serializedCommentOptionsOperation.write(commentOptionsExtension.toByteArray());
            }

            return serializedCommentOptionsOperation.toByteArray();
        } catch (IOException e) {
            throw new SteemInvalidTransactionException(
                    "A problem occured while transforming the operation into a byte array.", e);
        }
    }

    @Override
    public Map<SignatureObject, List<PrivateKeyType>> getRequiredAuthorities(
            Map<SignatureObject, List<PrivateKeyType>> requiredAuthoritiesBase) {
        return mergeRequiredAuthorities(requiredAuthoritiesBase, this.getAuthor(), PrivateKeyType.POSTING);
    }

    @Override
    public void validate(ValidationType validationType) {
        // TODO Auto-generated method stub

    }

    @Override
    public String toString() {
        return "CommentOptionsOperation{" +
                "author=" + author +
                ", permlink=" + permlink +
                ", maxAcceptedPayout=" + maxAcceptedPayout +
                ", percentSteemDollars=" + percentSteemDollars +
                ", allowVotes=" + allowVotes +
                ", allowCurationRewards=" + allowCurationRewards +
                ", extensions=" + extensions +
                '}';
    }
}
