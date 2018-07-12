package eu.bittrade.libs.golosj.base.models.operations;

import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonProperty;

import eu.bittrade.libs.golosj.base.models.AccountName;
import eu.bittrade.libs.golosj.enums.PrivateKeyType;
import eu.bittrade.libs.golosj.interfaces.SignatureObject;

/**
 * This abstract class contains fields that exist in all Steem
 * "limit_order_operation" objects.
 * 
 * @author <a href="http://steemit.com/@dez1337">dez1337</a>
 */
public abstract class AbstractLimitOrderOperation extends Operation {
    @JsonProperty("owner")
    protected AccountName owner;
    // Type is uint32 in the original code, but has to be long here as Java does
    // not support unsigned numbers accurate.
    @JsonProperty("orderid")
    protected long orderId;

    /**
     * Create a new Operation object by providing the operation type.
     * 
     * @param virtual
     *            Define if the operation instance is a virtual
     *            (<code>true</code>) or a market operation
     *            (<code>false</code>).
     */
    protected AbstractLimitOrderOperation(boolean virtual) {
        super(virtual);
    }

    /**
     * Get the owner of this order.
     * 
     * @return The owner of the order.
     */
    public abstract AccountName getOwner();

    /**
     * Set the owner for this order.
     * 
     * @param owner
     *            The owner for this order.
     */
    public abstract void setOwner(AccountName owner);

    /**
     * Get the id of this order.
     * 
     * @return The id of this order.
     */
    public abstract int getOrderId();

    /**
     * Set the id of this order. The only limitation for this id is that it has
     * to be free, meaning that there is no other open order with this id.
     * 
     * @param orderId
     *            The id of this order.
     */
    public abstract void setOrderId(long orderId);

    @Override
    public Map<SignatureObject, List<PrivateKeyType>> getRequiredAuthorities(
            Map<SignatureObject, List<PrivateKeyType>> requiredAuthoritiesBase) {
        return mergeRequiredAuthorities(requiredAuthoritiesBase, this.getOwner(), PrivateKeyType.ACTIVE);
    }
}
