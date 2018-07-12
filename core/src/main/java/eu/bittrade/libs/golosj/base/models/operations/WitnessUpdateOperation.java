package eu.bittrade.libs.golosj.base.models.operations;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URL;
import java.security.InvalidParameterException;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import eu.bittrade.libs.golosj.base.models.AccountName;
import eu.bittrade.libs.golosj.base.models.Asset;
import eu.bittrade.libs.golosj.base.models.ChainProperties;
import eu.bittrade.libs.golosj.base.models.PublicKey;
import eu.bittrade.libs.golosj.enums.AssetSymbolType;
import eu.bittrade.libs.golosj.enums.OperationType;
import eu.bittrade.libs.golosj.enums.PrivateKeyType;
import eu.bittrade.libs.golosj.enums.ValidationType;
import eu.bittrade.libs.golosj.exceptions.SteemInvalidTransactionException;
import eu.bittrade.libs.golosj.interfaces.SignatureObject;
import eu.bittrade.libs.golosj.util.SteemJUtils;

/**
 * This class represents the Steem "witness_update_operation" object.
 * 
 * @author <a href="http://steemit.com/@dez1337">dez1337</a>
 */
public class WitnessUpdateOperation extends Operation {
    @JsonProperty("owner")
    private AccountName owner;
    @JsonProperty("url")
    private URL url;
    @JsonProperty("block_signing_key")
    private PublicKey blockSigningKey;
    @JsonProperty("props")
    private ChainProperties properties;
    @JsonProperty("fee")
    private Asset fee;

    /**
     * Create a new witness update operation.
     * 
     * Users who wish to become a witness must pay a {@link #getFee() fee}
     * acceptable to the current witnesses to apply for the position and allow
     * voting to begin.
     *
     * If the {@link #getOwner() owner} isn't a witness they will become a
     * witness. Witnesses are charged a fee equal to 1 weeks worth of witness
     * pay which in turn is derived from the current share supply. The fee is
     * only applied if the owner is not already a witness.
     *
     * If the {@link #getBlockSigningKey() blockSigningKey} is null then the
     * witness is removed from contention. The network will pick the top 21
     * witnesses for producing blocks.
     * 
     * @param owner
     *            The Witness account name to set (see
     *            {@link #setOwner(AccountName)}).
     * @param url
     *            The URL to a statement post (see {@link #setUrl(URL)}).
     * @param blockSigningKey
     *            The public part of the key used to sign a block (see
     *            {@link #setBlockSigningKey(PublicKey)}).
     * @param properties
     *            The chain properties the witness is voting for (see
     *            {@link #setProperties(ChainProperties)}).
     * @param fee
     *            The fee to pay for this update (see {@link #setFee(Asset)}).
     */
    @JsonCreator
    public WitnessUpdateOperation(@JsonProperty("owner") AccountName owner, @JsonProperty("url") URL url,
            @JsonProperty("block_signing_key") PublicKey blockSigningKey,
            @JsonProperty("props") ChainProperties properties, @JsonProperty("fee") Asset fee) {
        super(false);

        this.setOwner(owner);
        this.setUrl(url);
        this.setBlockSigningKey(blockSigningKey);
        this.setProperties(properties);
        this.setFee(fee);
    }

    /**
     * Like
     * {@link #WitnessUpdateOperation(AccountName, URL, PublicKey, ChainProperties, Asset)},
     * but creates a new ChainProperties object with default values (Account
     * creation fee = 0.001 STEEM, maximum block size = minimum block size * 2,
     * SBD interest rate = 10%).
     * 
     * @param owner
     *            The Witness account name to set (see
     *            {@link #setOwner(AccountName)}).
     * @param url
     *            The URL to a statement post (see {@link #setUrl(URL)}).
     * @param blockSigningKey
     *            The public part of the key used to sign a block (see
     *            {@link #setBlockSigningKey(PublicKey)}).
     * @param fee
     *            The fee to pay for this update (see {@link #setFee(Asset)}).
     */
    public WitnessUpdateOperation(AccountName owner, URL url, PublicKey blockSigningKey, Asset fee) {
        super(false);

        this.setOwner(owner);
        this.setUrl(url);
        this.setBlockSigningKey(blockSigningKey);
        this.setFee(fee);

        Asset accountCreationFee = new Asset(1, AssetSymbolType.GOLOS);
        long maximumBlockSize = 131072;
        int sdbInterestRate = 1000;
        ChainProperties chainProperties = new ChainProperties(accountCreationFee, maximumBlockSize, sdbInterestRate);

        this.setProperties(chainProperties);
    }

    /**
     * Get the account name of the account for that the witness update operation
     * has been executed.
     * 
     * @return The name of the account that this operation has been executed
     *         for.
     */
    public AccountName getOwner() {
        return owner;
    }

    /**
     * Set the account name of the account for that the witness update operation
     * should be executed. <b>Notice:</b> The private active key of this account
     * needs to be stored in the key storage.
     * 
     * @param owner
     *            The name of the account that this operation should be executed
     *            for.
     * @throws InvalidParameterException
     *             If the owner is null.
     */
    public void setOwner(AccountName owner) {
        if (owner == null) {
            throw new InvalidParameterException("The owner can't be null.");
        }

        this.owner = owner;
    }

    /**
     * Get the URL that has been added to this witness update operation. In most
     * of the cases this URL is a link to a comment that describes the update.
     * 
     * @return The URL that has been added to this witness update operation.
     */
    public URL getUrl() {
        return url;
    }

    /**
     * Set the URL that should be added to this witness update operation. In
     * most of the cases this URL is a link to a comment that describes the
     * update.
     * 
     * @param url
     *            The URL that should be added to this witness update operation.
     * @throws InvalidParameterException
     *             If the url is null or empty.
     */
    public void setUrl(URL url) {
        if (url == null || url.toString().isEmpty()) {
            throw new InvalidParameterException("You need to provide a URL.");
        }

        this.url = url;
    }

    /**
     * Get the public key of the key pair that will be used to sign blocks.
     * 
     * @return The public key of the key pair that will be used to sign blocks.
     */
    public PublicKey getBlockSigningKey() {
        return blockSigningKey;
    }

    /**
     * Set the public key of the key pair that will be used to sign blocks.
     * 
     * @param blockSigningKey
     *            The public key of the key pair that will be used to sign
     *            blocks.
     * @throws InvalidParameterException
     *             If the blockSigningKey is null.
     */
    public void setBlockSigningKey(PublicKey blockSigningKey) {
        if (blockSigningKey == null) {
            throw new InvalidParameterException("You need to provide a block signing key.");
        }
        this.blockSigningKey = blockSigningKey;
    }

    /**
     * Get the chain properties that this witness is using.
     * 
     * @return The chain properties used by this witness.
     */
    public ChainProperties getProperties() {
        return properties;
    }

    /**
     * Set the chain properties that should be used.
     * 
     * @param properties
     *            The chain properties used by this witness.
     * @throws InvalidParameterException
     *             If the properties are null.
     */
    public void setProperties(ChainProperties properties) {
        if (properties == null) {
            throw new InvalidParameterException("You need to provide the blockchain properties.");
        }
        this.properties = properties;
    }

    /**
     * Get the fee that has been paid for this witness update.
     * 
     * @return The fee that has been paid for this witness update.
     */
    public Asset getFee() {
        return fee;
    }

    /**
     * Set the fee that should be paid for this witness update. The
     * <code>fee</code> paid to register a new witness, should be 10x current
     * block production pay.
     * 
     * @param fee
     *            The fee that should be paid for this witness update.
     * @throws InvalidParameterException
     *             If the provided asset object is null, the amount is 0 or the
     *             asset symbol is not STEEM.
     */
    public void setFee(Asset fee) {
        if (fee == null || fee.getAmount() < 0 || fee.getSymbol() != AssetSymbolType.GOLOS) {
            throw new InvalidParameterException("The fee needs to be a positive amount of GOLOS.");
        }
        this.fee = fee;
    }

    @Override
    public byte[] toByteArray() throws SteemInvalidTransactionException {
        try (ByteArrayOutputStream serializedWitnessUpdateOperation = new ByteArrayOutputStream()) {
            serializedWitnessUpdateOperation
                    .write(SteemJUtils.transformIntToVarIntByteArray(OperationType.WITNESS_UPDATE_OPERATION.ordinal()));
            serializedWitnessUpdateOperation.write(this.getOwner().toByteArray());
            serializedWitnessUpdateOperation
                    .write(SteemJUtils.transformStringToVarIntByteArray(this.getUrl().toString()));
            serializedWitnessUpdateOperation.write(this.getBlockSigningKey().toByteArray());
            serializedWitnessUpdateOperation.write(this.getProperties().toByteArray());
            serializedWitnessUpdateOperation.write(this.getFee().toByteArray());

            return serializedWitnessUpdateOperation.toByteArray();
        } catch (IOException e) {
            throw new SteemInvalidTransactionException(
                    "A problem occured while transforming the operation into a byte array.", e);
        }
    }

    @Override
    public String toString() {
        return "WitnessUpdateOperation{" +
                "owner=" + owner +
                ", url=" + url +
                ", blockSigningKey=" + blockSigningKey +
                ", properties=" + properties +
                ", fee=" + fee +
                '}';
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
}
