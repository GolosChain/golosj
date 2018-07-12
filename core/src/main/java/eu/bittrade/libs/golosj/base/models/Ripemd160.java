package eu.bittrade.libs.golosj.base.models;

import java.io.Serializable;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import org.bitcoinj.core.Utils;

import eu.bittrade.libs.golosj.exceptions.SteemInvalidTransactionException;
import eu.bittrade.libs.golosj.interfaces.ByteTransformable;

/**
 * This class is a wrapper for ripemd160 hashes.
 * 
 * @author <a href="http://steemit.com/@dez1337">dez1337</a>
 */
public abstract class Ripemd160 implements ByteTransformable, Serializable {
    /** Generated serial uid. */
    private static final long serialVersionUID = 7984783145088522082L;
    /**
     * Contains the ripemd160.
     */
    private byte[] hashValue;

    /**
     * Create a new wrapper for the given ripemd160 hash.
     * 
     * @param hashValue
     *            The hash to wrap.
     */
    public Ripemd160(String hashValue) {
        this.setHashValue(hashValue);
    }

    /**
     * Convert the first four bytes of the hash into a number.
     * 
     * @return The number.
     */
    public int getNumberFromHash() {
        byte[] fourBytesByte = new byte[4];
        System.arraycopy(hashValue, 0, fourBytesByte, 0, 4);
        return ByteBuffer.wrap(fourBytesByte).order(ByteOrder.BIG_ENDIAN).getInt();
    }

    /**
     * Get the wrapped hash value in its long representation.
     * 
     * @return The wrapped hash value in its long representation.
     */
    public long getHashValue() {
        return Utils.readUint32(hashValue, 4);
    }

    /**
     * Set the hash value by providing its decoded byte representation.
     * 
     * @param hashValue
     *            The hash to wrap.
     */
    public void setHashValue(byte[] hashValue) {
        this.hashValue = hashValue;
    }

    /**
     * Set the hash value by providing its encoded String representation.
     * 
     * @param hashValue
     *            The hash to wrap.
     */
    public void setHashValue(String hashValue) {
        this.hashValue = Utils.HEX.decode(hashValue);
    }

    @Override
    public byte[] toByteArray() throws SteemInvalidTransactionException {
        return this.hashValue;
    }

    @Override
    public String toString() {
        return Utils.HEX.encode(this.hashValue);
    }
}
