package eu.bittrade.libs.golosj.base.models.operations;

import java.security.InvalidParameterException;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonProperty;

import eu.bittrade.libs.golosj.base.models.AccountName;
import eu.bittrade.libs.golosj.base.models.Asset;
import eu.bittrade.libs.golosj.enums.PrivateKeyType;
import eu.bittrade.libs.golosj.interfaces.SignatureObject;

/**
 * This abstract class contains fields that exist in all Steem
 * "trasnfer_operation" objects.
 * 
 * @author <a href="http://steemit.com/@dez1337">dez1337</a>
 */
abstract class AbstractTransferOperation extends Operation {
    @JsonProperty("from")
    protected AccountName from;
    @JsonProperty("to")
    protected AccountName to;
    @JsonProperty("amount")
    protected Asset amount;

    /**
     * Create a new Operation object by providing the operation type.
     * 
     * @param virtual
     *            Define if the operation instance is a virtual
     *            (<code>true</code>) or a market operation
     *            (<code>false</code>).
     */
    protected AbstractTransferOperation(boolean virtual) {
        super(virtual);
    }

    /**
     * Get the account name of the user who transfered the <code>amount</code>.
     * 
     * @return The user which transfered the <code>amount</code>.
     */
    public AccountName getFrom() {
        return from;
    }

    /**
     * Set the account name of the user who transfers the <code>amount</code>.
     * <b>Notice:</b> The private active key of this account needs to be stored
     * in the key storage.
     * 
     * @param from
     *            The account name of the user who will transfer the
     *            <code>amount</code>.
     * @throws InvalidParameterException
     *             If the <code>from</code> account is null.
     */
    public void setFrom(AccountName from) {
        if (from == null) {
            throw new InvalidParameterException("The from account can't be null.");
        }

        this.from = from;
    }

    /**
     * Get the account name of the user who received the <code>amount</code>.
     * 
     * @return The account name of the user who received the
     *         <code>amount</code>.
     */
    public AccountName getTo() {
        return to;
    }

    /**
     * Set the account name of the user who will received the
     * <code>amount</code>.
     * 
     * @param to
     *            The account name of the user who will received the
     *            <code>amount</code>.
     * @throws InvalidParameterException
     *             If the <code>to</code> account is null.
     */
    public void setTo(AccountName to) {
        if (to == null) {
            throw new InvalidParameterException("The to account can't be null.");
        }

        this.to = to;
    }

    /**
     * Get the <code>amount</code> of that has been send.
     * 
     * @return The <code>amount</code> of that has been send.
     */
    public Asset getAmount() {
        return amount;
    }

    /**
     * Set the <code>amount</code> of that will be send.
     * 
     * @param amount
     *            The <code>amount</code> of that will be send.
     * @throws InvalidParameterException
     *             If the <code>amount</code> is null.
     */
    public void setAmount(Asset amount) {
        if (amount == null) {
            throw new InvalidParameterException("The amount can't be null.");
        }

        this.amount = amount;
    }

    @Override
    public Map<SignatureObject, List<PrivateKeyType>> getRequiredAuthorities(
            Map<SignatureObject, List<PrivateKeyType>> requiredAuthoritiesBase) {
        return mergeRequiredAuthorities(requiredAuthoritiesBase, this.getFrom(), PrivateKeyType.ACTIVE);
    }
}
