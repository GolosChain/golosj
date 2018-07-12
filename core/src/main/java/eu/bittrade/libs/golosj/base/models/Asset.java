package eu.bittrade.libs.golosj.base.models;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import eu.bittrade.libs.golosj.base.models.deserializer.AssetDeserializer;
import eu.bittrade.libs.golosj.base.models.serializer.AssetSerializer;
import eu.bittrade.libs.golosj.configuration.SteemJConfig;
import eu.bittrade.libs.golosj.enums.AssetSymbolType;
import eu.bittrade.libs.golosj.exceptions.SteemInvalidTransactionException;
import eu.bittrade.libs.golosj.interfaces.ByteTransformable;
import eu.bittrade.libs.golosj.util.SteemJUtils;

/**
 * This class is the java implementation of the <a href=
 * "https://github.com/steemit/steem/blob/master/libraries/protocol/include/steemit/protocol/asset.hpp">Steem
 * assets object</a>.
 * 
 * @author <a href="http://steemit.com/@dez1337">dez1337</a>
 */
@JsonDeserialize(using = AssetDeserializer.class)
@JsonSerialize(using = AssetSerializer.class)
public class Asset implements ByteTransformable {
    // Original type is "share_type" which is a "safe<int64_t>".
    private long amount;
    // Type us uint64_t in the original code.
    private AssetSymbolType symbol;
    private byte precision;

    /**
     * Create an empty Asset object.
     */
    public Asset() {

    }

    /**
     * Create a new asset object by providing all required fields.
     * 
     * @param amount
     *            The amount.
     * @param symbol
     *            One type of
     *            {@link eu.bittrade.libs.golosj.enums.AssetSymbolType
     *            AssetSymbolType}.
     */
    public Asset(long amount, AssetSymbolType symbol) {
        this.setAmount(amount);
        this.setSymbol(symbol);
    }

    /**
     * Get the amount of this asset object.
     * 
     * @return The amount.
     */
    public Double getAmount() {
        return Double.valueOf(this.amount / Math.pow(10.0, this.getPrecision()));
    }

    /**
     * Get the precision of this asset object.
     * 
     * @return The precision.
     */
    public Integer getPrecision() {
        return (int) precision;
    }

    /**
     * Get the symbol for this asset object.
     * 
     * @return One type of {@link eu.bittrade.libs.golosj.enums.AssetSymbolType
     *         AssetSymbolType}.
     */
    public AssetSymbolType getSymbol() {
        return symbol;
    }

    /**
     * Set the amount of this asset.
     * 
     * @param amount
     *            The amount.
     */
    public void setAmount(long amount) {
        this.amount = amount;
    }

    /**
     * Set the symbol of this asset.
     * 
     * @param symbol
     *            One type of
     *            {@link eu.bittrade.libs.golosj.enums.AssetSymbolType
     *            AssetSymbolType}.
     */
    public void setSymbol(AssetSymbolType symbol) {
        if (symbol.equals(AssetSymbolType.VESTS) || symbol.equals(AssetSymbolType.GESTS)) {
            this.precision = 6;
        } else {
            this.precision = 3;
        }

        this.symbol = symbol;
    }

    @Override
    public byte[] toByteArray() throws SteemInvalidTransactionException {
        try (ByteArrayOutputStream serializedAsset = new ByteArrayOutputStream()) {
            serializedAsset.write(SteemJUtils.transformLongToByteArray(this.amount));
            serializedAsset.write(SteemJUtils.transformByteToLittleEndian(this.precision));

            serializedAsset
                    .write(this.symbol.name().toUpperCase().getBytes(SteemJConfig.getInstance().getEncodingCharset()));
            String filledAssetSymbol = this.symbol.name().toUpperCase();

            for (int i = filledAssetSymbol.length(); i < 7; i++) {
                serializedAsset.write(0x00);
            }

            return serializedAsset.toByteArray();
        } catch (IOException e) {
            throw new SteemInvalidTransactionException(
                    "A problem occured while transforming an asset into a byte array.", e);
        }
    }

    @Override
    public boolean equals(Object otherAsset) {
        if (this == otherAsset)
            return true;
        if (otherAsset == null || !(otherAsset instanceof Asset))
            return false;
        Asset other = (Asset) otherAsset;
        return (this.getAmount().equals(other.getAmount()) && this.getSymbol().equals(other.getSymbol())
                && this.getPrecision().equals(other.getPrecision()));
    }

    @Override
    public String toString() {
        return "Asset{" +
                "amount=" + amount +
                ", symbol=" + symbol +
                ", precision=" + precision +
                '}';
    }

    @Override
    public int hashCode() {
        int hashCode = 1;
        hashCode = 31 * hashCode + (this.getAmount() == null ? 0 : this.getAmount().hashCode());
        hashCode = 31 * hashCode + (this.getSymbol() == null ? 0 : this.getSymbol().hashCode());
        hashCode = 31 * hashCode + (this.getPrecision() == null ? 0 : this.getPrecision().hashCode());
        return hashCode;
    }
}
