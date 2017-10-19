package eu.bittrade.libs.steemj.base.models.operations;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.InvalidParameterException;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import eu.bittrade.libs.steemj.base.models.AccountName;
import eu.bittrade.libs.steemj.base.models.Asset;
import eu.bittrade.libs.steemj.base.models.Price;
import eu.bittrade.libs.steemj.base.models.TimePointSec;
import eu.bittrade.libs.steemj.enums.AssetSymbolType;
import eu.bittrade.libs.steemj.enums.OperationType;
import eu.bittrade.libs.steemj.enums.ValidationType;
import eu.bittrade.libs.steemj.exceptions.SteemInvalidTransactionException;
import eu.bittrade.libs.steemj.util.SteemJUtils;

/**
 * This class is the java implementation of the <a href=
 * "https://github.com/steemit/steem/blob/master/libraries/protocol/include/steemit/protocol/steem_operations.hpp">Steem
 * limit_order_create2_operation</a>.
 * 
 * @author <a href="http://steemit.com/@dez1337">dez1337</a>
 */
public class LimitOrderCreate2Operation extends AbstractLimitOrderOperation {
    @JsonProperty("amount_to_sell")
    private Asset amountToSell;
    @JsonProperty("fill_or_kill")
    private Boolean fillOrKill;
    @JsonProperty("exchange_rate")
    private Price exchangeRate;
    @JsonProperty("expiration")
    private TimePointSec expirationDate;

    /**
     * Create a new limit order create operation. This operation is identical to
     * the
     * {@link eu.bittrade.libs.steemj.base.models.operations.LimitOrderCreateOperation
     * limit order create operation} except that it serializes the price rather
     * than calculating it from other fields.
     * 
     * @param owner
     *            The owner of the order that should be created (see
     *            {@link #setOwner(AccountName)}).
     * @param orderId
     *            The order id for this order (see {@link #setOrderId(long)}).
     * @param amountToSell
     *            The amount to sell (see {@link #setAmountToSell(Asset)}).
     * @param fillOrKill
     *            Define if this order is a "fillOrKill" order (see
     *            {@link #setFillOrKill(Boolean)}).
     * @param exchangeRate
     *            The exchange rate to set (see
     *            {@link #setExchangeRate(Price)}).
     * @param expirationDate
     *            The expiration date to set (see
     *            {@link #setExpirationDate(TimePointSec)}).
     * @throws InvalidParameterException
     *             If one of the arguments does not fulfill the requirements.
     */
    @JsonCreator
    public LimitOrderCreate2Operation(@JsonProperty("owner") AccountName owner, @JsonProperty("orderid") long orderId,
            @JsonProperty("amount_to_sell") Asset amountToSell, @JsonProperty("fill_or_kill") Boolean fillOrKill,
            @JsonProperty("exchange_rate") Price exchangeRate,
            @JsonProperty("expiration") TimePointSec expirationDate) {
        super(false);

        this.setOwner(owner);
        this.setOrderId(orderId);
        this.setAmountToSell(amountToSell);
        this.setFillOrKill(fillOrKill);
        this.setExchangeRate(exchangeRate);
        this.setExpirationDate(expirationDate);
    }

    /**
     * Like
     * {@link #LimitOrderCreate2Operation(AccountName, long, Asset, Boolean, Price, TimePointSec)},
     * but this constructor applies default values for the
     * <code>fillOrKill</code> and the <code>expirationDate</code> parameters.
     * The <code>fillOrKill</code> parameter is set to false and the
     * <code>expirationDate</code> to the latest possible date, so that it will
     * never expire.
     * 
     * @param owner
     *            The owner of the order that should be created (see
     *            {@link #setOwner(AccountName)}).
     * @param orderId
     *            The order id for this order (see {@link #setOrderId(long)}).
     * @param amountToSell
     *            The amount to sell (see {@link #setAmountToSell(Asset)}).
     * @param exchangeRate
     *            The exchange rate to set (see
     *            {@link #setExchangeRate(Price)}).
     * @throws InvalidParameterException
     *             If one of the arguments does not fulfill the requirements.
     */
    public LimitOrderCreate2Operation(AccountName owner, long orderId, Asset amountToSell, Price exchangeRate) {
        this(owner, orderId, amountToSell, false, exchangeRate, new TimePointSec(Long.MAX_VALUE));
    }

    /**
     * Like
     * {@link #LimitOrderCreate2Operation(AccountName, long, Asset, Price)}, but
     * also sets the <code>orderId</code> to its default value (0).
     * 
     * @param owner
     *            The owner of the order that should be created (see
     *            {@link #setOwner(AccountName)}).
     * @param amountToSell
     *            The amount to sell (see {@link #setAmountToSell(Asset)}).
     * @param exchangeRate
     *            The exchange rate to set (see
     *            {@link #setExchangeRate(Price)}).
     * @throws InvalidParameterException
     *             If one of the arguments does not fulfill the requirements.
     */
    public LimitOrderCreate2Operation(AccountName owner, Asset amountToSell, Price exchangeRate) {
        this(owner, 0, amountToSell, false, exchangeRate, new TimePointSec(Long.MAX_VALUE));
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
        if (amountToSell == null) {
            throw new InvalidParameterException("The amount to sell can't be null.");
        } else if (this.getExchangeRate() != null
                && (!amountToSell.getSymbol().equals(this.getExchangeRate().getBase().getSymbol()))) {
            throw new InvalidParameterException("The sell asset must be the base of the price.");
        } else if (this.getExchangeRate() != null && this.getExchangeRate().multiply(amountToSell).getAmount() <= 0) {
            throw new InvalidParameterException("The Amount to sell cannot round to 0 when traded.");
        } else if (this.getExchangeRate() != null && ((amountToSell.getSymbol().equals(AssetSymbolType.GOLOS)
                && this.getExchangeRate().getQuote().getSymbol().equals(AssetSymbolType.GBG))
                || (amountToSell.getSymbol().equals(AssetSymbolType.GBG)
                        && this.getExchangeRate().getQuote().getSymbol().equals(AssetSymbolType.GOLOS)))) {
            throw new InvalidParameterException("Limit order must be for the STEEM:SBD market.");
        }

        this.amountToSell = amountToSell;
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
     * Get the exchange rate (price.base / price.quote) used for this order.
     * 
     * @return The exchange rate used for this order.
     */
    public Price getExchangeRate() {
        return exchangeRate;
    }

    /**
     * Set the exchange rate (price.base / price.quote) used for this order.
     * 
     * @param exchangeRate
     *            The exchange rate used for this order.
     */
    public void setExchangeRate(Price exchangeRate) {
        this.exchangeRate = exchangeRate;
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

    @Override
    public byte[] toByteArray() throws SteemInvalidTransactionException {
        try (ByteArrayOutputStream serializedLimitOrderCreate2Operation = new ByteArrayOutputStream()) {
            serializedLimitOrderCreate2Operation.write(
                    SteemJUtils.transformIntToVarIntByteArray(OperationType.LIMIT_ORDER_CREATE2_OPERATION.ordinal()));
            serializedLimitOrderCreate2Operation.write(this.getOwner().toByteArray());
            serializedLimitOrderCreate2Operation.write(SteemJUtils.transformIntToByteArray(this.getOrderId()));
            serializedLimitOrderCreate2Operation.write(this.getAmountToSell().toByteArray());
            serializedLimitOrderCreate2Operation.write(this.getExchangeRate().toByteArray());
            serializedLimitOrderCreate2Operation.write(SteemJUtils.transformBooleanToByteArray(this.getFillOrKill()));
            serializedLimitOrderCreate2Operation.write(this.getExpirationDate().toByteArray());

            return serializedLimitOrderCreate2Operation.toByteArray();
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
