package eu.bittrade.libs.golosj.base.models.operations.virtual;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.builder.ToStringBuilder;

import eu.bittrade.libs.golosj.base.models.AccountName;
import eu.bittrade.libs.golosj.base.models.Asset;
import eu.bittrade.libs.golosj.base.models.Permlink;
import eu.bittrade.libs.golosj.base.models.operations.Operation;
import eu.bittrade.libs.golosj.enums.PrivateKeyType;
import eu.bittrade.libs.golosj.enums.ValidationType;
import eu.bittrade.libs.golosj.exceptions.SteemInvalidTransactionException;
import eu.bittrade.libs.golosj.interfaces.SignatureObject;

/**
 * This class represents the Steem "comment_reward_operation" object.
 * 
 * This operation type occurs if the payout period is over and the author of
 * comment finally gets his reward.
 * 
 * @author <a href="http://steemit.com/@dez1337">dez1337</a>
 */
public class CommentRewardOperation extends Operation {
    private AccountName author;
    private Permlink permlink;
    private Asset payout;

    /**
     * This operation is a virtual one and can only be created by the blockchain
     * itself. Due to that, this constructor is private.
     */
    private CommentRewardOperation() {
        super(true);
    }

    /**
     * Get the author of the comment.
     * 
     * @return The author of the comment.
     */
    public AccountName getAuthor() {
        return author;
    }

    /**
     * Get the permanent link to the comment.
     * 
     * @return The permanent link.
     */
    public Permlink getPermlink() {
        return permlink;
    }

    /**
     * Get the amount and the currency that the author of the comment receives.
     * 
     * @return The payout.
     */
    public Asset getPayout() {
        return payout;
    }

    @Override
    public byte[] toByteArray() throws SteemInvalidTransactionException {
        // The byte representation is not needed for virtual operations as we
        // can't broadcast them.
        return new byte[0];
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @Override
    public Map<SignatureObject, List<PrivateKeyType>> getRequiredAuthorities(
            Map<SignatureObject, List<PrivateKeyType>> requiredAuthoritiesBase) {
        // A virtual operation can't be created by the user, therefore it also
        // does not require any authority.
        return null;
    }

    @Override
    public void validate(ValidationType validationType) {
        // There is no need to validate virtual operations.
    }
}
