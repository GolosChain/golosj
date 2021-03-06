package eu.bittrade.libs.golosj.base.models.operations;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.InvalidParameterException;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.fasterxml.jackson.annotation.JsonProperty;

import eu.bittrade.libs.golosj.base.models.AccountName;
import eu.bittrade.libs.golosj.base.models.Asset;
import eu.bittrade.libs.golosj.base.models.TimePointSec;
import eu.bittrade.libs.golosj.enums.OperationType;
import eu.bittrade.libs.golosj.enums.ValidationType;
import eu.bittrade.libs.golosj.exceptions.SteemInvalidTransactionException;
import eu.bittrade.libs.golosj.util.SteemJUtils;

/**
 * This class is the java implementation of the <a href=
 * "https://github.com/steemit/steem/blob/master/libraries/protocol/include/steemit/protocol/steem_operations.hpp">Steem
 * limit_order_create_operation</a>.
 * 
 * @author <a href="http://steemit.com/@dez1337">dez1337</a>
 */
public class LimitOrderCreateOperation extends AbstractLimitOrderOperation {
    @JsonProperty("amount_to_sell")
    private Asset amountToSell;
    @JsonProperty("min_to_receive")
    private Asset minToReceive;
    @JsonProperty("fill_or_kill")
    private Boolean fillOrKill;
    @JsonProperty("expiration")
    private TimePointSec expirationDate;

    /**
     * Create a new limit order operation.
     * 
     * @param owner
     *            The account to create the operation for (see
     *            {@link #setOwner(AccountName)}).
     * @param orderId
     *            The id of this order (see {@link #setOrderId(long)}).
     * @param amountToSell
     *            The amount to sell (see {@link #setAmountToSell(Asset)}).
     * @param minToReceive
     *            The minimal amount to receive for the offered asset (see
     *            {@link #setMinToReceive(Asset)}).
     * @param fillOrKill
     *            Define if this order is a fill or kill order (see
     *            {@link #setFillOrKill(Boolean)}).
     * @param expirationDate
     *            Define how long this order is valid (see
     *            {@link #setExpirationDate(TimePointSec)}).
     * @throws InvalidParameterException
     *             If one of the arguments does not fulfill the requirements.
     */
    public LimitOrderCreateOperation(@JsonProperty("owner") AccountName owner, @JsonProperty("orderid") long orderId,
            @JsonProperty("amount_to_sell") Asset amountToSell, @JsonProperty("min_to_receive") Asset minToReceive,
            @JsonProperty("fill_or_kill") Boolean fillOrKill, @JsonProperty("expiration") TimePointSec expirationDate) {
        super(false);

        this.setOwner(owner);
        this.setOrderId(orderId);
        this.setAmountToSell(amountToSell);
        this.setMinToReceive(minToReceive);
        this.setFillOrKill(fillOrKill);
        this.setExpirationDate(expirationDate);
    }

    /**
     * Like {@link #LimitOrderCreateOperation(AccountName, long, Asset, Asset)},
     * but this constructor applies default values for the
     * <code>fillOrKill</code> and the <code>expirationDate</code> parameters.
     * The <code>fillOrKill</code> parameter is set to false and the
     * <code>expirationDate</code> to the latest possible date, so that it will
     * never expire.
     * 
     * @param owner
     *            The account to create the operation for (see
     *            {@link #setOwner(AccountName)}).
     * @param orderId
     *            The id of this order (see {@link #setOrderId(long)}).
     * @param amountToSell
     *            The amount to sell (see {@link #setAmountToSell(Asset)}).
     * @param minToReceive
     *            The minimal amount to receive for the offered asset (see
     *            {@link #setMinToReceive(Asset)}).
     * @throws InvalidParameterException
     *             If one of the arguments does not fulfill the requirements.
     */
    public LimitOrderCreateOperation(AccountName owner, long orderId, Asset amountToSell, Asset minToReceive) {
        this(owner, orderId, amountToSell, minToReceive, false, new TimePointSec(Long.MAX_VALUE));
    }

    /**
     * Like {@link #LimitOrderCreateOperation(AccountName, long, Asset, Asset)},
     * but also sets the <code>orderId</code> to its default value (0).
     * 
     * @param owner
     *            The account to create the operation for (see
     *            {@link #setOwner(AccountName)}).
     * @param amountToSell
     *            The amount to sell (see {@link #setAmountToSell(Asset)}).
     * @param minToReceive
     *            The minimal amount to receive for the offered asset (see
     *            {@link #setMinToReceive(Asset)}).
     * @throws InvalidParameterException
     *             If one of the arguments does not fulfill the requirements.
     */
    public LimitOrderCreateOperation(AccountName owner, Asset amountToSell, Asset minToReceive) {
        this(owner, 0L, amountToSell, minToReceive, false, new TimePointSec(Long.MAX_VALUE));
    }

    /**
     * Get the account the order should be created for.
     * 
     * @return The account to create the order for.
     */
    public AccountName getOwner() {
        return owner;
    }

    /**
     * Set the account to create the order for. <b>Notice:</b> The private
     * active key of this account needs to be stored in the key storage.
     * 
     * @param owner
     *            The account to create the order for.
     * @throws InvalidParameterException
     *             If the <code>owner</code> is null.
     */
    public void setOwner(AccountName owner) {
        if (owner == null) {
            throw new InvalidParameterException("The provided owner can't be null.");
        }

        this.owner = owner;
    }

    /**
     * Get the id of this order.
     * 
     * @return The id of this order.
     */
    public int getOrderId() {
        return (int) orderId;
    }

    /**
     * Set the id of this order. The only limitation for this id is that it has
     * to be free, meaning that there is no other open order with this id.
     * 
     * @param orderId
     *            The id of this order.
     */
    public void setOrderId(long orderId) {
        this.orderId = orderId;
    }

    /**
     * Get the amount to sell within this order.
     * 
     * @return The amount to sell within this order.
     */
    public Asset getAmountToSell() {
        return amountToSell;
    }

    /**
     * Set the amount to sell within this order.
     * 
     * @param amountToSell
     *            The amount to sell within this order.
     */
    public void setAmountToSell(Asset amountToSell) {
        this.amountToSell = amountToSell;
    }

    /**
     * Get the amount that the owner has received.
     * 
     * @return The amount that the owner has received.
     */
    public Asset getMinToReceive() {
        return minToReceive;
    }

    /**
     * Set the amount that should be received for the asset that will be sold.
     * 
     * @param minToReceive
     *            The amount that should be received for the asset that will be
     *            sold.
     */
    public void setMinToReceive(Asset minToReceive) {
        this.minToReceive = minToReceive;
    }

    /**
     * Get the information if this order was a "fill or kill" order. A "fill or
     * kill" is an option that can be added to limit order. If set to
     * <code>true</code>, the order will be automatically removed, if the order
     * can't be fullfilled immediatly.
     * 
     * @return <code>true</code> if this order was a fill or kill order,
     *         otherwise <code>false</code>.
     */
    public Boolean getFillOrKill() {
        return fillOrKill;
    }

    /**
     * Define if this order was a "fill or kill" order. A "fill or kill" is an
     * option that can be added to limit order. If set to <code>true</code>, the
     * order will be automatically removed, if the order can't be fullfilled
     * immediatly.
     * 
     * @param fillOrKill
     *            <code>true</code> if this order is a fill or kill order,
     *            otherwise <code>false</code>.
     */
    public void setFillOrKill(Boolean fillOrKill) {
        if (fillOrKill == null) {
            this.fillOrKill = false;
        } else {
            this.fillOrKill = fillOrKill;
        }
    }

    /**
     * Get the expiration date for this order.
     * 
     * @return The expiration date of this order.
     */
    public TimePointSec getExpirationDate() {
        return expirationDate;
    }

    /**
     * Set the expiration date for this order.
     * 
     * @param expirationDate
     *            The expiration date to set.
     */
    public void setExpirationDate(TimePointSec expirationDate) {
        if (expirationDate == null) {
            this.expirationDate = new TimePointSec(Long.MAX_VALUE);
        } else {
            this.expirationDate = expirationDate;
        }
    }

    /**
     * TODO: Validate all parameter of this Operation type.
     */
    public void validate() {
        /*
         * FC_ASSERT( (is_asset_type(amount_to_sell, STEEM_SYMBOL) &&
         * is_asset_type(min_to_receive, SBD_SYMBOL)) ||
         * (is_asset_type(amount_to_sell, SBD_SYMBOL) &&
         * is_asset_type(min_to_receive, STEEM_SYMBOL)),
         * "Limit order must be for the STEEM:SBD market"); (amount_to_sell /
         * min_to_receive).validate();
         */
    }

    @Override
    public byte[] toByteArray() throws SteemInvalidTransactionException {
        try (ByteArrayOutputStream serializedLimitOrderCreateOperation = new ByteArrayOutputStream()) {
            serializedLimitOrderCreateOperation.write(
                    SteemJUtils.transformIntToVarIntByteArray(OperationType.LIMIT_ORDER_CREATE_OPERATION.ordinal()));
            serializedLimitOrderCreateOperation.write(this.getOwner().toByteArray());
            serializedLimitOrderCreateOperation.write(SteemJUtils.transformIntToByteArray(this.getOrderId()));
            serializedLimitOrderCreateOperation.write(this.getAmountToSell().toByteArray());
            serializedLimitOrderCreateOperation.write(this.getMinToReceive().toByteArray());
            serializedLimitOrderCreateOperation.write(SteemJUtils.transformBooleanToByteArray(this.getFillOrKill()));
            serializedLimitOrderCreateOperation.write(this.getExpirationDate().toByteArray());

            return serializedLimitOrderCreateOperation.toByteArray();
        } catch (IOException e) {
            throw new SteemInvalidTransactionException(
                    "A problem occured while transforming the operation into a byte array.", e);
        }
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @Override
    public void validate(ValidationType validationType) {
        // TODO Auto-generated method stub

    }
}
