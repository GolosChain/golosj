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
import eu.bittrade.libs.golosj.enums.OperationType;
import eu.bittrade.libs.golosj.enums.PrivateKeyType;
import eu.bittrade.libs.golosj.enums.ValidationType;
import eu.bittrade.libs.golosj.exceptions.SteemInvalidTransactionException;
import eu.bittrade.libs.golosj.interfaces.SignatureObject;
import eu.bittrade.libs.golosj.util.SteemJUtils;

/**
 * This class represents the Steem "set_reset_account_operation" object.
 * 
 * @author <a href="http://steemit.com/@dez1337">dez1337</a>
 */
public class SetResetAccountOperation extends Operation {
    @JsonProperty("account")
    private AccountName account;
    @JsonProperty("current_reset_account")
    private AccountName currentResetAccount;
    @JsonProperty("reset_account")
    private AccountName resetAccount;

    /**
     * Create a new set reset account operation.
     * 
     * This operation allows the {@link #getAccount() account} owner to control
     * which account has the power to execute the 'reset_account_operation'
     * after 60 days.
     * 
     * @param account
     *            The account to set the reset account for (see
     *            {@link #setAccount(AccountName)}).
     * @param currentResetAccount
     *            The current reset account (see
     *            {@link #setResetAccount(AccountName)}).
     * @param resetAccount
     *            The new reset account to set (see
     *            {@link #setResetAccount(AccountName)}).
     * @throws InvalidParameterException
     *             If a parameter does not fulfill the requirements.
     */
    @JsonCreator
    public SetResetAccountOperation(@JsonProperty("account") AccountName account,
            @JsonProperty("current_reset_account") AccountName currentResetAccount,
            @JsonProperty("reset_account") AccountName resetAccount) {
        super(false);

        this.setAccount(account);
        this.setCurrentResetAccount(currentResetAccount);
        this.setResetAccount(resetAccount);
    }

    @Override
    public String toString() {
        return "SetResetAccountOperation{" +
                "account=" + account +
                ", currentResetAccount=" + currentResetAccount +
                ", resetAccount=" + resetAccount +
                '}';
    }

    /**
     * Get the account which the "set reset account operation" has been executed
     * for.
     * 
     * @return The account which the "set reset account operation" has been
     *         executed for.
     */
    public AccountName getAccount() {
        return account;
    }

    /**
     * Define for which account this "set reset account operation" is for.
     * <b>Notice:</b> The private owner key of this account needs to be stored
     * in the key storage.
     * 
     * @param account
     *            The account which the "set reset account operation" has been
     *            executed for.
     * @throws InvalidParameterException
     *             If no account has been provided.
     */
    public void setAccount(AccountName account) {
        if (account == null) {
            throw new InvalidParameterException("The account can't be null.");
        }

        this.account = account;
    }

    /**
     * Get the current reset account of the {@link #getAccount() account}. For
     * newly created accounts this is <i> new AccountName("null") </i> in most
     * cases.
     * 
     * @return The current reset account for the {@link #getAccount() account}.
     */
    public AccountName getCurrentResetAccount() {
        return currentResetAccount;
    }

    /**
     * Set the current reset account of the {@link #getAccount() account}. For
     * accounts created by Steemit.com the current reset account is
     * <code>new AccountName("")</code>.
     * 
     * @param currentResetAccount
     *            The current reset account for the {@link #getAccount()
     *            account}. I
     */
    public void setCurrentResetAccount(AccountName currentResetAccount) {
        if (currentResetAccount.equals(this.getResetAccount())) {
            throw new InvalidParameterException(
                    "The current reset account can't be set to the same account as the new reset account.");
        }
        this.currentResetAccount = currentResetAccount;
    }

    /**
     * Get the new reset account which has been set with this operation.
     * 
     * @return The new reset account which has been set with this operation.
     */
    public AccountName getResetAccount() {
        return resetAccount;
    }

    /**
     * Set the new reset account for the {@link #getAccount() account}.
     * 
     * @param resetAccount
     *            The new reset account which has been set with this operation.
     * @throws InvalidParameterException
     *             If no reset account has been provided or if the
     *             <code>resetAccount</code> is set to the same value than the
     *             {@link #getCurrentResetAccount()}.
     */
    public void setResetAccount(AccountName resetAccount) {
        if (resetAccount == null) {
            throw new InvalidParameterException("The reset account can't be null.");
        } else if (resetAccount.equals(this.getCurrentResetAccount())) {
            throw new InvalidParameterException(
                    "The new reset account can't be the same as the current reset account.");
        }

        this.resetAccount = resetAccount;
    }

    @Override
    public byte[] toByteArray() throws SteemInvalidTransactionException {
        try (ByteArrayOutputStream serializedSetResetAccountOperation = new ByteArrayOutputStream()) {
            serializedSetResetAccountOperation.write(
                    SteemJUtils.transformIntToVarIntByteArray(OperationType.SET_RESET_ACCOUNT_OPERATION.ordinal()));
            serializedSetResetAccountOperation.write(this.getAccount().toByteArray());
            if (this.getCurrentResetAccount() != null) {
                serializedSetResetAccountOperation.write(this.getCurrentResetAccount().toByteArray());
            }
            serializedSetResetAccountOperation.write(this.getResetAccount().toByteArray());

            return serializedSetResetAccountOperation.toByteArray();
        } catch (IOException e) {
            throw new SteemInvalidTransactionException(
                    "A problem occured while transforming the operation into a byte array.", e);
        }
    }

    @Override
    public Map<SignatureObject, List<PrivateKeyType>> getRequiredAuthorities(
            Map<SignatureObject, List<PrivateKeyType>> requiredAuthoritiesBase) {
        return mergeRequiredAuthorities(requiredAuthoritiesBase, this.getAccount(), PrivateKeyType.OWNER);
    }

    @Override
    public void validate(ValidationType validationType) {
        // TODO Auto-generated method stub

    }
}
