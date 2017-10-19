package eu.bittrade.libs.steemj.base.models.operations;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.InvalidParameterException;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import eu.bittrade.libs.steemj.base.models.AccountName;
import eu.bittrade.libs.steemj.base.models.Asset;
import eu.bittrade.libs.steemj.base.models.TimePointSec;
import eu.bittrade.libs.steemj.enums.AssetSymbolType;
import eu.bittrade.libs.steemj.enums.OperationType;
import eu.bittrade.libs.steemj.enums.ValidationType;
import eu.bittrade.libs.steemj.exceptions.SteemInvalidTransactionException;
import eu.bittrade.libs.steemj.util.SteemJUtils;

/**
 * This class represents the Steem "escrow_transfer_operation" object.
 * 
 * @author <a href="http://steemit.com/@dez1337">dez1337</a>
 */
public class EscrowTransferOperation extends AbstractEscrowOperation {
    @JsonProperty("sbd_amount")
    private Asset sbdAmount;
    @JsonProperty("steem_amount")
    private Asset steemAmount;
    @JsonProperty("fee")
    private Asset fee;
    @JsonProperty("ratification_deadline")
    private TimePointSec ratificationDeadlineDate;
    @JsonProperty("escrow_expiration")
    private TimePointSec escrowExpirationDate;
    @JsonProperty("json_meta")
    private String jsonMeta;

    /**
     * Create a new escrow transfer operation.
     * 
     * The purpose of this operation is to enable someone to send money
     * contingently to another individual. The funds leave the {@link #getFrom()
     * from} account and go into a temporary balance where they are held until
     * {@link #getFrom() from} releases it to {@link #getTo() to} or
     * {@link #getTo() to} refunds it to {@link #getFrom() from} account.
     *
     * In the event of a dispute the {@link #getAgent() agent} can divide the
     * funds between the to/from account. Disputes can be raised any time before
     * or on the dispute deadline time, after the escrow has been approved by
     * all parties.
     *
     * This operation only creates a proposed escrow transfer. Both the
     * {@link #getAgent() agent} and {@link #getTo() to} must agree to the terms
     * of the arrangement by approving the escrow.
     *
     * The escrow agent is paid the fee on approval of all parties. It is up to
     * the escrow agent to determine the fee.
     *
     * Escrow transactions are uniquely identified by 'from' and 'escrow_id',
     * the 'escrow_id' is defined by the sender.
     * 
     * @param from
     *            The source account of the escrow operation (see
     *            {@link #setFrom(AccountName)}).
     * @param to
     *            The target account of the escrow operation (see
     *            {@link #setTo(AccountName)}).
     * @param agent
     *            The agent account of the escrow operation (see
     *            {@link #setAgent(AccountName)}).
     * @param escrowId
     *            Set the unique id of this escrow operation (see
     *            {@link #setEscrowId(long)}).
     * @param sbdAmount
     *            Set the SDB amount that should be transfered (see
     *            {@link #setSbdAmount(Asset)}).
     * @param steemAmount
     *            Set the STEEM amount that has been transfered (see
     *            {@link #setSteemAmount(Asset)}).
     * @param fee
     *            Set the fee that will be paid to the agent (see
     *            {@link #setFee(Asset)}).
     * @param ratificationDeadlineDate
     *            Define until when the escrow opperation needs to be approved
     *            (see {@link #setRatificationDeadlineDate(TimePointSec)}).
     * @param escrowExpirationDate
     *            Define when this escrow operation expires (see
     *            {@link #setEscrowExpirationDate(TimePointSec)}).
     * @param jsonMeta
     *            Additional meta data to set (see
     *            {@link #setJsonMeta(String)}).
     * @throws InvalidParameterException
     *             If one of the arguments does not fulfill the requirements.
     */
    @JsonCreator
    public EscrowTransferOperation(@JsonProperty("from") AccountName from, @JsonProperty("to") AccountName to,
            @JsonProperty("agent") AccountName agent, @JsonProperty("escrow_id") long escrowId,
            @JsonProperty("sbd_amount") Asset sbdAmount, @JsonProperty("steem_amount") Asset steemAmount,
            @JsonProperty("fee") Asset fee,
            @JsonProperty("ratification_deadline") TimePointSec ratificationDeadlineDate,
            @JsonProperty("escrow_expiration") TimePointSec escrowExpirationDate,
            @JsonProperty("json_meta") String jsonMeta) {
        super(false);

        if (from == null || to == null || agent == null || from.equals(agent) || to.equals(agent)) {
            throw new InvalidParameterException("The agent account must be a third party.");
        }

        this.from = from;
        this.to = to;
        this.agent = agent;

        if (sbdAmount == null || steemAmount == null || (sbdAmount.getAmount() + steemAmount.getAmount() <= 0)) {
            throw new InvalidParameterException("An escrow must transfer a non-zero amount.");
        }

        this.sbdAmount = sbdAmount;
        this.steemAmount = steemAmount;

        if (ratificationDeadlineDate == null || escrowExpirationDate == null
                || ratificationDeadlineDate.getDateTimeAsTimestamp() >= escrowExpirationDate.getDateTimeAsTimestamp()) {
            throw new InvalidParameterException("The ratification deadline must be before escrow expiration");
        }

        this.ratificationDeadlineDate = ratificationDeadlineDate;
        this.escrowExpirationDate = escrowExpirationDate;

        this.setEscrowId(escrowId);
        this.setFee(fee);
        this.setJsonMeta(jsonMeta);
    }

    /**
     * Like
     * {@link #EscrowTransferOperation(AccountName, AccountName, AccountName, long, Asset, Asset, Asset, TimePointSec, TimePointSec, String)},
     * but sets the <code>sbdAmount</code> and the <code>steemAmount</code> to
     * their default values (0).
     * 
     * @param from
     *            The source account of the escrow operation (see
     *            {@link #setFrom(AccountName)}).
     * @param to
     *            The target account of the escrow operation (see
     *            {@link #setTo(AccountName)}).
     * @param agent
     *            The agent account of the escrow operation (see
     *            {@link #setAgent(AccountName)}).
     * @param escrowId
     *            Set the unique id of this escrow operation (see
     *            {@link #setEscrowId(long)}).
     * @param fee
     *            Set the fee that will be paid to the agent (see
     *            {@link #setFee(Asset)}).
     * @param ratificationDeadlineDate
     *            Define until when the escrow opperation needs to be approved
     *            (see {@link #setRatificationDeadlineDate(TimePointSec)}).
     * @param escrowExpirationDate
     *            Define when this escrow operation expires (see
     *            {@link #setEscrowExpirationDate(TimePointSec)}).
     * @param jsonMeta
     *            Additional meta data to set (see
     *            {@link #setJsonMeta(String)}).
     * @throws InvalidParameterException
     *             If one of the arguments does not fulfill the requirements.
     */
    public EscrowTransferOperation(AccountName from, AccountName to, AccountName agent, long escrowId, Asset fee,
            TimePointSec ratificationDeadlineDate, TimePointSec escrowExpirationDate, String jsonMeta) {
        this(from, to, agent, escrowId, new Asset(0, AssetSymbolType.GBG), new Asset(0, AssetSymbolType.GOLOS), fee,
                ratificationDeadlineDate, escrowExpirationDate, jsonMeta);
    }

    /**
     * Like
     * {@link #EscrowTransferOperation(AccountName, AccountName, AccountName, Asset, TimePointSec, TimePointSec, String)},
     * but also sets the <code>escrowId</code> to its default value (30).
     * 
     * @param from
     *            The source account of the escrow operation (see
     *            {@link #setFrom(AccountName)}).
     * @param to
     *            The target account of the escrow operation (see
     *            {@link #setTo(AccountName)}).
     * @param agent
     *            The agent account of the escrow operation (see
     *            {@link #setAgent(AccountName)}).
     * @param fee
     *            Set the fee that will be paid to the agent (see
     *            {@link #setFee(Asset)}).
     * @param ratificationDeadlineDate
     *            Define until when the escrow opperation needs to be approved
     *            (see {@link #setRatificationDeadlineDate(TimePointSec)}).
     * @param escrowExpirationDate
     *            Define when this escrow operation expires (see
     *            {@link #setEscrowExpirationDate(TimePointSec)}).
     * @param jsonMeta
     *            Additional meta data to set (see
     *            {@link #setJsonMeta(String)}).
     * @throws InvalidParameterException
     *             If one of the arguments does not fulfill the requirements.
     */
    public EscrowTransferOperation(AccountName from, AccountName to, AccountName agent, Asset fee,
            TimePointSec ratificationDeadlineDate, TimePointSec escrowExpirationDate, String jsonMeta) {
        this(from, to, agent, 30, fee, ratificationDeadlineDate, escrowExpirationDate, jsonMeta);
    }

    /**
     * Set the account who wants to transfer the fund to the {@link #getTo() to}
     * account. <b>Notice:</b> The private active key of this account needs to
     * be stored in the key storage.
     * 
     * @param from
     *            The account who wants to transfer the fund.
     * @throws InvalidParameterException
     *             If the <code>from</code> is null or is equal to the
     *             <code>agent</code> or <code>to</code> account.
     */
    @Override
    public void setFrom(AccountName from) {
        if (from == null) {
            throw new InvalidParameterException("The from account can't be null.");
        } else if (from.equals(this.getAgent())) {
            throw new InvalidParameterException("The agent account must be a third party.");
        }

        this.from = from;
    }

    /**
     * Set the account who should receive the funds.
     * 
     * @param to
     *            The account who should receive the funds.
     * @throws InvalidParameterException
     *             If the <code>to</code> is null or is equal to the
     *             <code>agent</code> or <code>from</code> account.
     */
    @Override
    public void setTo(AccountName to) {
        if (to == null) {
            throw new InvalidParameterException("The to account can't be null.");
        } else if (to.equals(this.getAgent())) {
            throw new InvalidParameterException("The agent account must be a third party.");
        }

        this.to = to;
    }

    /**
     * Set the agent account.
     * 
     * @param agent
     *            The agent account.
     * @throws InvalidParameterException
     *             If the <code>agent</code> is null or is equal to the
     *             <code>from</code> or <code>to</code> account.
     */
    @Override
    public void setAgent(AccountName agent) {
        if (agent == null) {
            throw new InvalidParameterException("The agent can't be null.");
        } else if (agent.equals(this.getFrom()) || agent.equals(this.getTo())) {
            throw new InvalidParameterException("The agent account must be a third party.");
        }

        this.agent = agent;
    }

    /**
     * Get the SDB amount that has been transfered.
     * 
     * @return The SDB amount that has been transfered.
     */
    public Asset getSbdAmount() {
        return sbdAmount;
    }

    /**
     * Set the SDB amount that should be transfered.
     * 
     * @param sbdAmount
     *            The SDB amount that has been transfered.
     * @throws InvalidParameterException
     *             If the <code>sbdAmount</code> is null, has a negative amount,
     *             has a different symbol type than SBD or if the
     *             <code>sbdAmount</code> <b>and</b> {@link #getSteemAmount()}
     *             both have an amount of 0.
     */
    public void setSbdAmount(Asset sbdAmount) {
        if (sbdAmount == null) {
            throw new InvalidParameterException("The sbd amount can't be null.");
        } else if (sbdAmount.getAmount() < 0) {
            throw new InvalidParameterException("The sbd amount cannot be negative.");
        } else if (!sbdAmount.getSymbol().equals(AssetSymbolType.GBG)) {
            throw new InvalidParameterException("The sbd amount must contain SBD.");
        } else if (sbdAmount.getAmount() + this.getSteemAmount().getAmount() < 0) {
            throw new InvalidParameterException("An escrow must transfer a non-zero amount.");
        }

        this.sbdAmount = sbdAmount;
    }

    /**
     * Get the STEEM amount that has been transfered.
     * 
     * @return The STEEM amount that has been transfered.
     */
    public Asset getSteemAmount() {
        return steemAmount;
    }

    /**
     * Set the STEEM amount that has been transfered.
     * 
     * @param steemAmount
     *            The STEEM amount that has been transfered.
     * @throws InvalidParameterException
     *             If the <code>steemAmount</code> is null, has a negative
     *             amount, has a different symbol type than STEEM or if the
     *             <code>steemAmount</code> <b>and</b> {@link #getSbdAmount()}
     *             both have an amount of 0.
     */
    public void setSteemAmount(Asset steemAmount) {
        if (steemAmount == null) {
            throw new InvalidParameterException("The steem amount can't be null.");
        } else if (steemAmount.getAmount() < 0) {
            throw new InvalidParameterException("The steem amount cannot be negative.");
        } else if (!steemAmount.getSymbol().equals(AssetSymbolType.GBG)) {
            throw new InvalidParameterException("The steem amount must contain STEEM.");
        } else if (steemAmount.getAmount() + this.getSbdAmount().getAmount() < 0) {
            throw new InvalidParameterException("An escrow must transfer a non-zero amount.");
        }

        this.steemAmount = steemAmount;
    }

    /**
     * Get the fee that has been paid to the agent.
     * 
     * @return The fee that will be paid to the agent.
     */
    public Asset getFee() {
        return fee;
    }

    /**
     * Set the fee that will be paid to the agent.
     * 
     * @param fee
     *            The fee that will be paid to the agent.
     * @throws InvalidParameterException
     *             If the <code>fee</code> is null, has a negative amount or a
     *             different symbol than STEEM or SBD.
     */
    public void setFee(Asset fee) {
        if (fee == null) {
            throw new InvalidParameterException("The fee can't be null.");
        } else if (fee.getAmount() < 0) {
            throw new InvalidParameterException("The fee cannot be negative.");
        } else if (!fee.getSymbol().equals(AssetSymbolType.GOLOS) && !fee.getSymbol().equals(AssetSymbolType.GBG)) {
            throw new InvalidParameterException("The fee must be STEEM or SBD.");
        }

        this.fee = fee;
    }

    /**
     * Get the information until when the escrow opperation needs to be
     * approved.
     * 
     * @return The ratification deadline.
     */
    public TimePointSec getRatificationDeadlineDate() {
        return ratificationDeadlineDate;
    }

    /**
     * Define until when the escrow opperation needs to be approved.
     * 
     * @param ratificationDeadlineDate
     *            The ratification deadline. to set.
     * @throws InvalidParameterException
     *             If the <code>ratificationDeadlineDate</code> is null or if
     *             the <code>ratificationDeadlineDate</code> is <b>not</b>
     *             before the {@link #getEscrowExpirationDate()}.
     */
    public void setRatificationDeadlineDate(TimePointSec ratificationDeadlineDate) {
        if (ratificationDeadlineDate == null) {
            throw new InvalidParameterException("The ratification deadline date can't be null.");
        } else if (ratificationDeadlineDate.getDateTimeAsTimestamp() >= this.getEscrowExpirationDate()
                .getDateTimeAsTimestamp()) {
            throw new InvalidParameterException("The ratification deadline must be before escrow expiration.");
        }

        this.ratificationDeadlineDate = ratificationDeadlineDate;
    }

    /**
     * Get the expration date for this escrow operation.
     * 
     * @return The escrow expiration date.
     */
    public TimePointSec getEscrowExpirationDate() {
        return escrowExpirationDate;
    }

    /**
     * Define when this escrow operation expires.
     * 
     * @param escrowExpirationDate
     *            The escrow expiration date to set.
     */
    public void setEscrowExpirationDate(TimePointSec escrowExpirationDate) {
        if (escrowExpirationDate == null) {
            throw new InvalidParameterException("The escrow expiration date can't be null.");
        } else if (escrowExpirationDate.getDateTimeAsTimestamp() < this.getRatificationDeadlineDate()
                .getDateTimeAsTimestamp()) {
            throw new InvalidParameterException("The ratification deadline must be before escrow expiration.");
        }

        this.escrowExpirationDate = escrowExpirationDate;
    }

    /**
     * Get the json metadata that has been added to this operation.
     * 
     * @return The json metadata that has been added to this operation.
     */
    public String getJsonMeta() {
        return jsonMeta;
    }

    /**
     * Set the json metadata that has been added to this operation.
     * 
     * @param jsonMeta
     *            The json metadata that has been added to this operation.
     * @throws InvalidParameterException
     *             If the given <code>jsonMeta</code> is not valid.
     */
    public void setJsonMeta(String jsonMeta) {
        if (!jsonMeta.isEmpty() && !SteemJUtils.verifyJsonString(jsonMeta)) {
            throw new InvalidParameterException("The given String is no valid JSON");
        }

        this.jsonMeta = jsonMeta;
    }

    @Override
    public byte[] toByteArray() throws SteemInvalidTransactionException {
        try (ByteArrayOutputStream serializedEscrowTransferOperation = new ByteArrayOutputStream()) {
            serializedEscrowTransferOperation.write(
                    SteemJUtils.transformIntToVarIntByteArray(OperationType.ESCROW_TRANSFER_OPERATION.ordinal()));
            serializedEscrowTransferOperation.write(this.getFrom().toByteArray());
            serializedEscrowTransferOperation.write(this.getTo().toByteArray());
            serializedEscrowTransferOperation.write(this.getAgent().toByteArray());
            serializedEscrowTransferOperation.write(SteemJUtils.transformIntToByteArray(this.getEscrowId()));
            serializedEscrowTransferOperation.write(this.getSbdAmount().toByteArray());
            serializedEscrowTransferOperation.write(this.getSteemAmount().toByteArray());
            serializedEscrowTransferOperation.write(this.getFee().toByteArray());
            serializedEscrowTransferOperation.write(this.getRatificationDeadlineDate().toByteArray());
            serializedEscrowTransferOperation.write(this.getEscrowExpirationDate().toByteArray());
            serializedEscrowTransferOperation.write(SteemJUtils.transformStringToVarIntByteArray(this.getJsonMeta()));

            return serializedEscrowTransferOperation.toByteArray();
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
