package eu.bittrade.libs.golosj.base.models.operations;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.InvalidParameterException;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import eu.bittrade.libs.golosj.base.models.AccountName;
import eu.bittrade.libs.golosj.base.models.Asset;
import eu.bittrade.libs.golosj.enums.AssetSymbolType;
import eu.bittrade.libs.golosj.enums.OperationType;
import eu.bittrade.libs.golosj.enums.PrivateKeyType;
import eu.bittrade.libs.golosj.enums.ValidationType;
import eu.bittrade.libs.golosj.exceptions.SteemInvalidTransactionException;
import eu.bittrade.libs.golosj.interfaces.SignatureObject;
import eu.bittrade.libs.golosj.util.SteemJUtils;

/**
 * This class represents the Steem "delegate_vesting_shares_operation" object.
 * 
 * @author <a href="http://steemit.com/@dez1337">dez1337</a>
 */
public class DelegateVestingSharesOperation extends Operation {
    @JsonProperty("delegator")
    private AccountName delegator;
    @JsonProperty("delegatee")
    private AccountName delegatee;
    @JsonProperty("vesting_shares")
    private Asset vestingShares;

    /**
     * Create a new delegate vesting shares operation.
     * 
     * Delegate vesting shares from one account to the other. The vesting shares
     * are still owned by the original account, but content voting rights and
     * bandwidth allocation are transferred to the receiving account. This sets
     * the delegation to `vesting_shares`, increasing it or decreasing it as
     * needed. (i.e. a delegation of 0 removes the delegation)
     *
     * When a delegation is removed the shares are placed in limbo for a week to
     * prevent a satoshi of VESTS from voting on the same content twice.
     * 
     * @param delegator
     *            The account that delegates the <code>vestingShares</code> (see
     *            {@link #setDelegator(AccountName)}).
     * @param delegatee
     *            The account to send the <code>vestingShares</code> to (see
     *            {@link #setDelegatee(AccountName)}).
     * @param vestingShares
     *            The amount to deletage (see {@link #setVestingShares(Asset)}).
     * @throws InvalidParameterException
     *             If one of the arguments does not fulfill the requirements.
     */
    @JsonCreator
    public DelegateVestingSharesOperation(@JsonProperty("delegator") AccountName delegator,
            @JsonProperty("delegatee") AccountName delegatee, @JsonProperty("vesting_shares") Asset vestingShares) {
        super(false);

        this.setDelegator(delegator);
        this.setDelegatee(delegatee);
        this.setVestingShares(vestingShares);
    }

    /**
     * Get the account name of the account who delegated the vesting shares.
     * 
     * @return The account name of the delegator.
     */
    public AccountName getDelegator() {
        return delegator;
    }

    /**
     * Set the account name of the account who delegated the vesting shares.
     * <b>Notice:</b> The private active key of this account needs to be stored
     * in the key storage.
     * 
     * @param delegator
     *            The account name of the delegator.
     * @throws InvalidParameterException
     *             If the <code>delegator</code> account is null or equal to the
     *             {@link #getDelegatee()} account.
     */
    public void setDelegator(AccountName delegator) {
        if (delegator == null) {
            throw new InvalidParameterException("The delegatee account can't be null.");
        } else if (this.getDelegatee() != null && this.getDelegatee().equals(delegator)) {
            throw new InvalidParameterException("The delegatee account can't be equal to the delegator account.");
        }

        this.delegator = delegator;
    }

    /**
     * Get the account name of the account which received the vesting shares.
     * 
     * @return The account name of the delegatee.
     */
    public AccountName getDelegatee() {
        return delegatee;
    }

    /**
     * Set the account name of the account which received the vesting shares.
     * 
     * @param delegatee
     *            The account name of the delegatee.
     * @throws InvalidParameterException
     *             If the <code>delegatee</code> account is null or equal to the
     *             {@link #getDelegator()} account.
     */
    public void setDelegatee(AccountName delegatee) {
        if (delegatee == null) {
            throw new InvalidParameterException("The delegatee account can't be null.");
        } else if (this.getDelegator() != null && this.getDelegator().equals(delegatee)) {
            throw new InvalidParameterException("The delegatee account can't be equal to the delegator account.");
        }

        this.delegatee = delegatee;
    }

    /**
     * Get the amount of vesting shares delegated.
     * 
     * @return The amount of vesting shares delegated.
     */
    public Asset getVestingShares() {
        return vestingShares;
    }

    /**
     * Set the amount of vesting shares delegated.
     * 
     * @param vestingShares
     *            The amount of vesting shares delegated.
     * @throws InvalidParameterException
     *             If the provided <code>vestingShares</code> is null, the asset
     *             symbol is not VESTS or the amount is negative.
     */
    public void setVestingShares(Asset vestingShares) {
        if (vestingShares == null) {
            throw new InvalidParameterException("The vesting shares to delegate can't be null.");
        } else if (!vestingShares.getSymbol().equals(AssetSymbolType.VESTS)) {
            throw new InvalidParameterException("Can only delegate VESTS.");
        } else if (vestingShares.getAmount() <= 0) {
            throw new InvalidParameterException("Can't delegate a negative amount of VESTS.");
        }

        this.vestingShares = vestingShares;
    }

    @Override
    public byte[] toByteArray() throws SteemInvalidTransactionException {
        try (ByteArrayOutputStream serializedDelegateVestingSharesOperation = new ByteArrayOutputStream()) {
            serializedDelegateVestingSharesOperation.write(SteemJUtils
                    .transformIntToVarIntByteArray(OperationType.DELEGATE_VESTING_SHARES_OPERATION.ordinal()));
            serializedDelegateVestingSharesOperation.write(this.getDelegator().toByteArray());
            serializedDelegateVestingSharesOperation.write(this.getDelegatee().toByteArray());
            serializedDelegateVestingSharesOperation.write(this.getVestingShares().toByteArray());

            return serializedDelegateVestingSharesOperation.toByteArray();
        } catch (IOException e) {
            throw new SteemInvalidTransactionException(
                    "A problem occured while transforming the operation into a byte array.", e);
        }
    }

    @Override
    public Map<SignatureObject, List<PrivateKeyType>> getRequiredAuthorities(
            Map<SignatureObject, List<PrivateKeyType>> requiredAuthoritiesBase) {
        return mergeRequiredAuthorities(requiredAuthoritiesBase, this.getDelegator(), PrivateKeyType.ACTIVE);
    }

    @Override
    public void validate(ValidationType validationType) {
        // TODO Auto-generated method stub

    }

    @Override
    public String toString() {
        return "DelegateVestingSharesOperation{" +
                "delegator=" + delegator +
                ", delegatee=" + delegatee +
                ", vestingShares=" + vestingShares +
                '}';
    }
}
