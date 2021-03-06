package eu.bittrade.libs.golosj.base.models.operations.virtual;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.fasterxml.jackson.annotation.JsonProperty;

import eu.bittrade.libs.golosj.base.models.AccountName;
import eu.bittrade.libs.golosj.base.models.Asset;
import eu.bittrade.libs.golosj.base.models.operations.Operation;
import eu.bittrade.libs.golosj.enums.PrivateKeyType;
import eu.bittrade.libs.golosj.enums.ValidationType;
import eu.bittrade.libs.golosj.exceptions.SteemInvalidTransactionException;
import eu.bittrade.libs.golosj.interfaces.SignatureObject;

/**
 * This class represents a Steem "fill_order_operation" object.
 * 
 * This operation type occurs if a order has been closed completely or if a part
 * of the order has been closed.
 * 
 * @author <a href="http://steemit.com/@dez1337">dez1337</a>
 */
public class FillOrderOperation extends Operation {
    @JsonProperty("current_owner")
    private AccountName currentOwner;
    @JsonProperty("current_orderid")
    // Original type is uint32_t here so we have to use long.
    private int currentOrderId;
    @JsonProperty("current_pays")
    private Asset currentPays;
    @JsonProperty("open_owner")
    private AccountName openOwner;
    @JsonProperty("open_orderid")
    // Original type is uint32_t here so we have to use long.
    private long openOrderId;
    @JsonProperty("open_pays")
    private Asset openPays;

    /**
     * This operation is a virtual one and can only be created by the blockchain
     * itself. Due to that, this constructor is private.
     */
    private FillOrderOperation() {
        super(true);
    }

    /**
     * @return The current owner.
     */
    public AccountName getCurrentOwner() {
        return currentOwner;
    }

    /**
     * @return The current order id.
     */
    public int getCurrentOrderId() {
        return currentOrderId;
    }

    /**
     * @return The current pays.
     */
    public Asset getCurrentPays() {
        return currentPays;
    }

    /**
     * @return The open owner.
     */
    public AccountName getOpenOwner() {
        return openOwner;
    }

    /**
     * @return The open order id.
     */
    public long getOpenOrderId() {
        return openOrderId;
    }

    /**
     * @return The open pays.
     */
    public Asset getOpenPays() {
        return openPays;
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
