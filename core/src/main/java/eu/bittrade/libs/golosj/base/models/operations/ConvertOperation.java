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
 * This class represents the Steem "convert_operation" object.
 * 
 * @author <a href="http://steemit.com/@dez1337">dez1337</a>
 */
public class ConvertOperation extends Operation {
    @JsonProperty("owner")
    private AccountName owner;
    // Original type is uint32_t so we have to use long here.
    @JsonProperty("requestid")
    private long requestId;
    @JsonProperty("amount")
    private Asset amount;

    /**
     * Create a new convert operation. This operation instructs the blockchain
     * to start a conversion between STEEM and SBD, The funds are deposited
     * after STEEMIT_CONVERSION_DELAY.
     * 
     * @param owner
     *            The account to covert the asset for (see
     *            {@link #setOwner(AccountName)}).
     * @param requestId
     *            The request id of this transaction (see
     *            {@link #setRequestId(long)}).
     * @param amount
     *            The amount to convert (see {@link #setAmount(Asset)}).
     * @throws InvalidParameterException
     *             If one of the arguments does not fulfill the requirements.
     */
    @JsonCreator
    public ConvertOperation(@JsonProperty("owner") AccountName owner, @JsonProperty("requestid") long requestId,
            @JsonProperty("amount") Asset amount) {
        super(false);

        this.setOwner(owner);
        this.setRequestId(requestId);
        this.setAmount(amount);
    }

    /**
     * Like {@link #ConvertOperation(AccountName, long, Asset)}, but
     * automatically sets the request id to 0.
     * 
     * @param owner
     *            The account to covert the asset for (see
     *            {@link #setOwner(AccountName)}).
     * @param amount
     *            The amount to convert (see {@link #setAmount(Asset)}).
     * @throws InvalidParameterException
     *             If one of the arguments does not fulfill the requirements.
     */
    public ConvertOperation(AccountName owner, Asset amount) {
        this(owner, 0, amount);
    }

    /**
     * Get the account which performed the conversion.
     * 
     * @return The account which performed the conversion.
     */
    public AccountName getOwner() {
        return owner;
    }

    /**
     * Set the account for which the operation should be performed.
     * <b>Notice:</b> The private active key of this account needs to be stored
     * in the key storage.
     * 
     * @param owner
     *            The account for which the operation should be performed.
     * @throws InvalidParameterException
     *             If the <code>owner</code> is null.
     */
    public void setOwner(AccountName owner) {
        if (owner == null) {
            throw new InvalidParameterException("The owner can't be null.");
        }

        this.owner = owner;
    }

    /**
     * Get the id of this conversion request.
     * 
     * @return The id of this conversion request.
     */
    public long getRequestId() {
        return requestId;
    }

    /**
     * Set the id of this conversion request. The id has to be unique.
     * 
     * @param requestId
     *            The id of this conversion request.
     */
    public void setRequestId(long requestId) {
        this.requestId = requestId;
    }

    /**
     * Get the SBD or STEEM amount that has been converted.
     * 
     * @return The SBD or STEEM amount that has been converted.
     */
    public Asset getAmount() {
        return amount;
    }

    /**
     * Set the amount of SBD or STEEM that should be converted.
     * 
     * @param amount
     *            The amount of SBD or STEEM that should be converted.
     * @throws InvalidParameterException
     *             If the provided <code>amount</code> is null, the symbol type
     *             is not SBD or the amount is less than 1.
     */
    public void setAmount(Asset amount) {
        if (amount == null) {
            throw new InvalidParameterException("The amount to convert can't be null.");
        } else if (!amount.getSymbol().equals(AssetSymbolType.GBG)) {
            // Only allow conversion from SBD to STEEM, allowing the opposite
            // can enable traders to abuse market fluxuations through converting
            // large quantities without moving the price.
            throw new InvalidParameterException("Can only convert SBD to STEEM.");
        } else if (amount.getAmount() <= 0) {
            throw new InvalidParameterException("Can only convert more than 0 SBD.");
        }

        this.amount = amount;
    }

    @Override
    public byte[] toByteArray() throws SteemInvalidTransactionException {
        try (ByteArrayOutputStream serializedConvertOperation = new ByteArrayOutputStream()) {
            serializedConvertOperation
                    .write(SteemJUtils.transformIntToVarIntByteArray(OperationType.CONVERT_OPERATION.ordinal()));
            serializedConvertOperation.write(this.getOwner().toByteArray());
            serializedConvertOperation.write(SteemJUtils.transformIntToByteArray((int) this.getRequestId()));
            serializedConvertOperation.write(this.getAmount().toByteArray());

            return serializedConvertOperation.toByteArray();
        } catch (IOException e) {
            throw new SteemInvalidTransactionException(
                    "A problem occured while transforming the operation into a byte array.", e);
        }
    }

    @Override
    public Map<SignatureObject, List<PrivateKeyType>> getRequiredAuthorities(
            Map<SignatureObject, List<PrivateKeyType>> requiredAuthoritiesBase) {
        return mergeRequiredAuthorities(requiredAuthoritiesBase, this.getOwner(), PrivateKeyType.ACTIVE);
    }

    @Override
    public void validate(ValidationType validationType) {
        // TODO Auto-generated method stub

    }

    @Override
    public String toString() {
        return "ConvertOperation{" +
                "owner=" + owner +
                ", requestId=" + requestId +
                ", amount=" + amount +
                '}';
    }
}
