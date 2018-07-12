package eu.bittrade.libs.golosj.base.models.operations;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.InvalidParameterException;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import eu.bittrade.libs.golosj.base.models.AccountName;
import eu.bittrade.libs.golosj.enums.OperationType;
import eu.bittrade.libs.golosj.enums.ValidationType;
import eu.bittrade.libs.golosj.exceptions.SteemInvalidTransactionException;
import eu.bittrade.libs.golosj.util.SteemJUtils;

/**
 * This class represents the Steem "escrow_approve_operation" object.
 * 
 * @author <a href="http://steemit.com/@dez1337">dez1337</a>
 */
public class EscrowApproveOperation extends AbstractEscrowOperation {
    @JsonProperty("who")
    private AccountName who;
    @JsonProperty("approve")
    private Boolean approve;

    /**
     * Create a new escrow approve operation.
     * 
     * The agent and to accounts must approve an escrow transaction for it to be
     * valid on the blockchain. Once a part approves the escrow, the cannot
     * revoke their approval. Subsequent escrow approve operations, regardless
     * of the approval, will be rejected.
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
     *            The <b>unique</b> id of the escrow operation (see
     *            {@link #setEscrowId(long)}).
     * @param who
     *            The account who approves the escrow operation (see
     *            {@link #setWho(AccountName)}).
     * @param approve
     *            Define if the {@link #getWho()} account approves the operation
     *            (see {@link #setApprove(Boolean)}).
     * @throws InvalidParameterException
     *             If one of the arguemnts does not fulfill the requirements.
     */
    @JsonCreator
    public EscrowApproveOperation(@JsonProperty("from") AccountName from, @JsonProperty("to") AccountName to,
            @JsonProperty("agent") AccountName agent, @JsonProperty("escrow_id") long escrowId,
            @JsonProperty("who") AccountName who, @JsonProperty("approve") Boolean approve) {
        super(false);

        this.setFrom(from);
        this.setTo(to);
        this.setAgent(agent);
        this.setEscrowId(escrowId);
        this.setWho(who);
        this.setApprove(approve);
    }

    /**
     * Like
     * {@link #EscrowApproveOperation(AccountName, AccountName, AccountName, long, AccountName, Boolean)},
     * will automatically approve the operation.
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
     *            The <b>unique</b> id of the escrow operation (see
     *            {@link #setEscrowId(long)}).
     * @param who
     *            The account who approves the escrow operation (see
     *            {@link #setWho(AccountName)}).
     * @throws InvalidParameterException
     *             If one of the arguemnts does not fulfill the requirements.
     */
    public EscrowApproveOperation(AccountName from, AccountName to, AccountName agent, long escrowId, AccountName who) {
        this(from, to, agent, escrowId, who, true);
    }

    /**
     * Like
     * {@link #EscrowApproveOperation(AccountName, AccountName, AccountName, long, AccountName, Boolean)},
     * but sets the <code>escrowId</code> to its default value (30) and will
     * automatically approve the operation.
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
     * @param who
     *            The account who approves the escrow operation (see
     *            {@link #setWho(AccountName)}).
     * @throws InvalidParameterException
     *             If one of the arguemnts does not fulfill the requirements.
     */
    public EscrowApproveOperation(AccountName from, AccountName to, AccountName agent, AccountName who) {
        this(from, to, agent, 30, who, true);
    }

    /**
     * Get the account which approved this operation.
     * 
     * @return The account which approved this operation.
     */
    @Override
    public AccountName getWho() {
        return who;
    }

    /**
     * Set the account who approves this operation. This can either be the
     * {@link #getTo() to} account or the {@link #getAgent() agent} account.
     * 
     * @param who
     *            The account which approved this operation.
     * @throws InvalidParameterException
     *             If the <code>who</code> is null.
     */
    public void setWho(AccountName who) {
        if (who == null) {
            throw new InvalidParameterException("The who account can't be null.");
        }

        this.who = who;
    }

    /**
     * Get the information if the {@link #getWho() who} account has approved the
     * operation or not.
     * 
     * @return True if the operation has been approved or false if not.
     */
    public Boolean getApprove() {
        return approve;
    }

    /**
     * Define if the {@link #getWho() who} account approves the operation or
     * not.
     * 
     * @param approve
     *            True if the operation has been approved or false if not.
     */
    public void setApprove(Boolean approve) {
        this.approve = approve;
    }

    /**
     * TODO: Validate all parameter of this Operation type.
     */
    public void validate() {
        // FC_ASSERT( who == from || who == to, "who must be from or to" );
    }

    @Override
    public byte[] toByteArray() throws SteemInvalidTransactionException {
        try (ByteArrayOutputStream serializedEscrowApproveOperation = new ByteArrayOutputStream()) {
            serializedEscrowApproveOperation
                    .write(SteemJUtils.transformIntToVarIntByteArray(OperationType.ESCROW_APPROVE_OPERATION.ordinal()));
            serializedEscrowApproveOperation.write(this.getFrom().toByteArray());
            serializedEscrowApproveOperation.write(this.getTo().toByteArray());
            serializedEscrowApproveOperation.write(this.getAgent().toByteArray());
            serializedEscrowApproveOperation.write(this.getWho().toByteArray());
            serializedEscrowApproveOperation.write(SteemJUtils.transformIntToByteArray(this.getEscrowId()));
            serializedEscrowApproveOperation.write(SteemJUtils.transformBooleanToByteArray(this.getApprove()));

            return serializedEscrowApproveOperation.toByteArray();
        } catch (IOException e) {
            throw new SteemInvalidTransactionException(
                    "A problem occured while transforming the operation into a byte array.", e);
        }
    }

    @Override
    public void validate(ValidationType validationType) {
        // TODO Auto-generated method stub

    }

    @Override
    public String toString() {
        return "EscrowApproveOperation{" +
                "who=" + who +
                ", approve=" + approve +
                '}';
    }
}
