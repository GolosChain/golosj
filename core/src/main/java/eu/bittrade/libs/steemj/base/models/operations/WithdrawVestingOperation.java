package eu.bittrade.libs.steemj.base.models.operations;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.InvalidParameterException;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import eu.bittrade.libs.steemj.base.models.AccountName;
import eu.bittrade.libs.steemj.base.models.Asset;
import eu.bittrade.libs.steemj.enums.AssetSymbolType;
import eu.bittrade.libs.steemj.enums.OperationType;
import eu.bittrade.libs.steemj.enums.PrivateKeyType;
import eu.bittrade.libs.steemj.enums.ValidationType;
import eu.bittrade.libs.steemj.exceptions.SteemInvalidTransactionException;
import eu.bittrade.libs.steemj.interfaces.SignatureObject;
import eu.bittrade.libs.steemj.util.SteemJUtils;

/**
 * This class represents the Steem "withdraw_vesting_operation" object.
 * 
 * @author <a href="http://steemit.com/@dez1337">dez1337</a>
 */
public class WithdrawVestingOperation extends Operation {
    @JsonProperty("account")
    private AccountName account;
    @JsonProperty("vesting_shares")
    private Asset vestingShares;

    /**
     * Create a new withdraw vesting operation.
     * 
     * At any given point in time an account can be withdrawing from their
     * vesting shares. A user may change the number of shares they wish to cash
     * out at any time between 0 and their total vesting stake.
     *
     * After applying this operation, {@link #getVestingShares() vestingShares}
     * will be withdrawn at a rate of {@link #getVestingShares()
     * vestingShares}/104 per week for two years starting one week after this
     * operation is included in the blockchain.
     *
     * This operation is not valid if the user has no vesting shares.
     * 
     * @param account
     *            Set the account that wants to start powering down (see
     *            {@link #setAccount(AccountName)}).
     * @param vestingShares
     *            Set the amount of VESTS to power down (see
     *            {@link #setVestingShares(Asset)}).
     */
    @JsonCreator
    public WithdrawVestingOperation(@JsonProperty("account") AccountName account,
            @JsonProperty("vesting_shares") Asset vestingShares) {
        super(false);

        this.setAccount(account);
        this.setVestingShares(vestingShares);
    }

    @Override
    public String toString() {
        return "WithdrawVestingOperation{" +
                "account=" + account +
                ", vestingShares=" + vestingShares +
                '}';
    }

    /**
     * Get the account name of the account that the withdraw vesting operation
     * has been executed for.
     * 
     * @return The account name for which the withdraw vesting operation has
     *         been executed for.
     */
    public AccountName getAccount() {
        return account;
    }

    /**
     * Set the account name of the account that the withdraw vesting operation
     * should be executed for. <b>Notice:</b> The private active key of this
     * account needs to be stored in the key storage.
     * 
     * @param account
     *            The account name for which the withdraw vesting operation
     *            should be executed for.
     * @throws InvalidParameterException
     *             If no account name has been provided.
     */
    public void setAccount(AccountName account) {
        if (account == null) {
            throw new InvalidParameterException("An account name needs to be provided.");
        }
        this.account = account;
    }

    /**
     * Get the amount that has been requested for withdrawing.
     * 
     * @return the vestingShares The amount that has been requested for
     *         withdrawing.
     */
    public Asset getVestingShares() {
        return vestingShares;
    }

    /**
     * Set the amount that should be requested for withdrawing.
     * 
     * @param vestingShares
     *            The amount that should be requested for withdrawing.
     * @throws InvalidParameterException
     *             If the asset type is not VESTS.
     */
    public void setVestingShares(Asset vestingShares) {
        if (vestingShares == null || !vestingShares.getSymbol().equals(AssetSymbolType.VESTS)) {
            throw new InvalidParameterException("The provided asset needs to have the symbol type VESTS.");
        }
        this.vestingShares = vestingShares;
    }

    @Override
    public byte[] toByteArray() throws SteemInvalidTransactionException {
        try (ByteArrayOutputStream serializedWithdrawVestingOperation = new ByteArrayOutputStream()) {
            serializedWithdrawVestingOperation.write(
                    SteemJUtils.transformIntToVarIntByteArray(OperationType.WITHDRAW_VESTING_OPERATION.ordinal()));
            serializedWithdrawVestingOperation.write(this.getAccount().toByteArray());
            serializedWithdrawVestingOperation.write(this.getVestingShares().toByteArray());

            return serializedWithdrawVestingOperation.toByteArray();
        } catch (IOException e) {
            throw new SteemInvalidTransactionException(
                    "A problem occured while transforming the operation into a byte array.", e);
        }
    }

    @Override
    public Map<SignatureObject, List<PrivateKeyType>> getRequiredAuthorities(
            Map<SignatureObject, List<PrivateKeyType>> requiredAuthoritiesBase) {
        return mergeRequiredAuthorities(requiredAuthoritiesBase, this.getAccount(), PrivateKeyType.ACTIVE);
    }

    @Override
    public void validate(ValidationType validationType) {
        // TODO Auto-generated method stub

    }
}
