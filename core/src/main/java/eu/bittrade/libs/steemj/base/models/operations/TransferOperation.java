package eu.bittrade.libs.steemj.base.models.operations;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.InvalidParameterException;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import eu.bittrade.libs.steemj.base.models.AccountName;
import eu.bittrade.libs.steemj.base.models.Asset;
import eu.bittrade.libs.steemj.enums.AssetSymbolType;
import eu.bittrade.libs.steemj.enums.OperationType;
import eu.bittrade.libs.steemj.enums.ValidationType;
import eu.bittrade.libs.steemj.exceptions.SteemInvalidTransactionException;
import eu.bittrade.libs.steemj.util.SteemJUtils;

/**
 * This class represents the Steem "transfer_operation" object.
 * 
 * @author <a href="http://steemit.com/@dez1337">dez1337</a>
 */
public class TransferOperation extends AbstractTransferOperation {
    @JsonProperty("memo")
    private String memo;

    /**
     * Create a new transfer operation. Use this operation to transfer an asset
     * from one account to another.
     * 
     * @param from
     *            The account to transfer the vestings from (see
     *            {@link #setFrom(AccountName)}).
     * @param to
     *            The account that will receive the transfered vestings (see
     *            {@link #setTo(AccountName)}).
     * @param amount
     *            The amount of vests to transfer (see
     *            {@link #setAmount(Asset)}).
     * @param memo
     *            An additional message added to the operation (see
     *            {@link #setMemo(String)}).
     * @throws InvalidParameterException
     *             If one of the arguments does not fulfill the requirements.
     */
    @JsonCreator
    public TransferOperation(@JsonProperty("from") AccountName from, @JsonProperty("to") AccountName to,
            @JsonProperty("amount") Asset amount, @JsonProperty("memo") String memo) {
        super(false);

        this.setFrom(from);
        this.setTo(to);
        this.setAmount(amount);
        this.setMemo(memo);
    }

    @Override
    public String toString() {
        return "TransferOperation{" +
                "from=" + from +
                ", to=" + to +
                ", amount=" + amount +
                ", memo='" + memo + '\'' +
                '}';
    }

    /**
     * Set the <code>amount</code> of that will be send.
     * 
     * @param amount
     *            The <code>amount</code> of that will be send.
     * @throws InvalidParameterException
     *             If the <code>amount</code> is null, of symbol type VESTS or
     *             less than 1.
     */
    @Override
    public void setAmount(Asset amount) {
        if (amount == null) {
            throw new InvalidParameterException("The amount can't be null.");
        } else if (amount.getSymbol().equals(AssetSymbolType.VESTS)) {
            throw new InvalidParameterException("Transfering Steem Power (VESTS) is not allowed.");
        } else if (amount.getAmount() <= 0) {
            throw new InvalidParameterException("Must transfer a nonzero amount.");
        }

        this.amount = amount;
    }

    /**
     * Get the message added to this operation.
     * 
     * @return The message added to this operation.
     */
    public String getMemo() {
        return memo;
    }

    /**
     * Add an additional message to this operation.
     * 
     * @param memo
     *            The message added to this operation.
     * @throws InvalidParameterException
     *             If the <code>memo</code> has more than 2048 characters.
     */
    public void setMemo(String memo) {
        if (memo.length() > 2048) {
            throw new InvalidParameterException("The memo is too long. Only 2048 characters are allowed.");
        }
        this.memo = memo;
    }

    @Override
    public byte[] toByteArray() throws SteemInvalidTransactionException {
        try (ByteArrayOutputStream serializedTransferOperation = new ByteArrayOutputStream()) {
            serializedTransferOperation
                    .write(SteemJUtils.transformIntToVarIntByteArray(OperationType.TRANSFER_OPERATION.ordinal()));
            serializedTransferOperation.write(this.getFrom().toByteArray());
            serializedTransferOperation.write(this.getTo().toByteArray());
            serializedTransferOperation.write(this.getAmount().toByteArray());
            serializedTransferOperation.write(SteemJUtils.transformStringToVarIntByteArray(this.getMemo()));

            return serializedTransferOperation.toByteArray();
        } catch (IOException e) {
            throw new SteemInvalidTransactionException(
                    "A problem occured while transforming the operation into a byte array.", e);
        }
    }

    @Override
    public void validate(ValidationType validationType) {
        // TODO Auto-generated method stub

    }
}
