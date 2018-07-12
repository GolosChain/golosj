package eu.bittrade.libs.golosj.base.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.annotations.VisibleForTesting;

import org.bitcoinj.core.ECKey;
import org.bitcoinj.core.ECKey.ECDSASignature;
import org.bitcoinj.core.Sha256Hash;
import org.bitcoinj.core.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.Nullable;

import eu.bittrade.libs.golosj.base.models.operations.Operation;
import eu.bittrade.libs.golosj.configuration.SteemJConfig;
import eu.bittrade.libs.golosj.enums.PrivateKeyType;
import eu.bittrade.libs.golosj.exceptions.SteemInvalidTransactionException;
import eu.bittrade.libs.golosj.interfaces.ByteTransformable;
import eu.bittrade.libs.golosj.interfaces.SignatureObject;
import eu.bittrade.libs.golosj.util.ImmutablePair;
import eu.bittrade.libs.golosj.util.SteemJUtils;

/**
 * This class represents a Steem "signed_transaction" object.
 *
 * @author <a href="http://Steemit.com/@dez1337">dez1337</a>
 */
public class SignedTransaction extends Transaction implements ByteTransformable, Serializable {
    private static final long serialVersionUID = 4821422578657270330L;
    private static final Logger LOGGER = LoggerFactory.getLogger(SignedTransaction.class);

    protected transient List<String> signatures;

    /**
     * This constructor is only used to create the POJO from a JSON response.
     */
    @JsonCreator
    private SignedTransaction(@JsonProperty("ref_block_num") int refBlockNum,
                              @JsonProperty("ref_block_prefix") long refBlockPrefix,
                              @JsonProperty("expiration") TimePointSec expirationDate,
                              @JsonProperty("operations") List<Operation> operations,
                              @JsonProperty("extensions") List<FutureExtensions> extensions,
                              @JsonProperty("signatures") List<String> signatures) {
        super(refBlockNum, refBlockPrefix, expirationDate, operations, extensions);
        this.signatures = signatures;
    }

    /**
     * Create a new signed transaction object.
     *
     * @param refBlockNum    The reference block number (see {@link #setRefBlockNum(int)}).
     * @param refBlockPrefix The reference block index (see
     *                       {@link #setRefBlockPrefix(long)}).
     * @param expirationDate Define until when the transaction has to be processed (see
     *                       {@link #setExpirationDate(TimePointSec)}).
     * @param operations     A list of operations to process within this Transaction (see
     *                       {@link #setOperations(List)}).
     * @param extensions     Extensions are currently not supported and will be ignored
     *                       (see {@link #setExtensions(List)}).
     */
    public SignedTransaction(int refBlockNum, long refBlockPrefix, TimePointSec expirationDate,
                             List<Operation> operations, List<FutureExtensions> extensions) {
        super(refBlockNum, refBlockPrefix, expirationDate, operations, extensions);
        this.signatures = new ArrayList<>();
    }

    /**
     * Like {@link #SignedTransaction(int, long, TimePointSec, List, List)}, but
     * allows you to provide a
     * {@link eu.bittrade.libs.golosj.base.models.BlockId} object as the
     * reference block and will also set the <code>expirationDate</code> to the
     * latest possible time.
     *
     * @param blockId    The block reference (see {@link #setRefBlockNum(int)} and
     *                   {@link #setRefBlockPrefix(long)}).
     * @param operations A list of operations to process within this Transaction (see
     *                   {@link #setOperations(List)}).
     * @param extensions Extensions are currently not supported and will be ignored
     *                   (see {@link #setExtensions(List)}).
     */
    public SignedTransaction(BlockId blockId, List<Operation> operations, List<FutureExtensions> extensions) {
        super(blockId, operations, extensions);
        this.signatures = new ArrayList<>();
    }

    /**
     * <b>This method is only used by JUnit-Tests</b>
     * <p>
     * Create a new signed transaction object.
     */
    @VisibleForTesting
    protected SignedTransaction() {
        super();
        this.signatures = new ArrayList<>();
    }

    /**
     * Get the signatures for this transaction.
     *
     * @return An array of currently appended signatures.
     */
    @JsonProperty("signatures")
    public List<String> getSignatures() {
        return this.signatures;
    }

    /**
     * Verify that the signature is canonical.
     * <p>
     * Original implementation can be found <a href=
     * "https://github.com/kenCode-de/graphenej/blob/master/graphenej/src/main/java/de/bitsharesmunich/graphenej/Transaction.java"
     * >here</a>.
     *
     * @param signature A single signature in its byte representation.
     * @return True if the signature is canonical or false if not.
     */
    private boolean isCanonical(byte[] signature) {
        return ((signature[0] & 0x80) != 0) || (signature[0] == 0) || ((signature[1] & 0x80) != 0)
                || ((signature[32] & 0x80) != 0) || (signature[32] == 0) || ((signature[33] & 0x80) != 0);
    }

    /**
     * Like {@link #sign(String) sign(String)}, but uses the default Steem chain
     * id.
     *
     * @param skipValidation Define if the validation should be skipped or not.
     * @throws SteemInvalidTransactionException If the transaction can not be signed.
     */
    @VisibleForTesting
    protected void sign(boolean skipValidation) throws SteemInvalidTransactionException {
        sign(SteemJConfig.getInstance().getChainId(), skipValidation);
    }

    /**
     * Like {@link #sign(String) sign(String)}, but uses the default Steem chain
     * id.
     *
     * @throws SteemInvalidTransactionException If the transaction can not be signed.
     */
    public void sign() throws SteemInvalidTransactionException {
        sign(SteemJConfig.getInstance().getChainId(), false);
    }

    /**
     * Use this method if you want to specify a different chainId than the
     * default one for STEEM. Otherwise use the {@link #sign() sign()} method.
     *
     * @param chainId The chain id that should be used during signing.
     * @throws SteemInvalidTransactionException If the transaction can not be signed.
     */
    public void sign(String chainId) throws SteemInvalidTransactionException {
        sign(chainId, false);
    }

    /**
     * Use this method if you want to specify a different chainId than the
     * default one for STEEM. Otherwise use the {@link #sign() sign()} method.
     *
     * @param chainId        The chain id that should be used during signing.
     * @param skipValidation Define if the validation should be skipped or not.
     * @throws SteemInvalidTransactionException If the transaction can not be signed.
     */
    protected void sign(String chainId, boolean skipValidation) throws SteemInvalidTransactionException {

        if (!skipValidation) {
            this.validate();
        }

        for (ECKey requiredPrivateKey : getRequiredSignatureKeys()) {
            boolean isCanonical = false;

            Sha256Hash messageAsHash;
            while (!isCanonical) {
                try {
                    messageAsHash = Sha256Hash.wrap(Sha256Hash.hash(this.toByteArray(chainId)));
                } catch (SteemInvalidTransactionException e) {
                    throw new SteemInvalidTransactionException(
                            "The required encoding is not supported by your platform.", e);
                }

                ECDSASignature signature = requiredPrivateKey.sign(messageAsHash);

                /*
                 * Use 0 as the key type until the signature is canonical as the
                 * key type does not effect the canonical test. The correct key
                 * type will be tested later on. This technique improves the
                 * performance (thanks to ray66rus).
                 */
                if (isCanonical(createSignedTransaction(0, signature, requiredPrivateKey))) {
                    this.getExpirationDate().setDateTime(this.getExpirationDate().getDateTimeAsTimestamp() + 2);
                } else {
                    isCanonical = true;

                    int keyType = SteemJUtils.getKeyType(signature, messageAsHash, requiredPrivateKey);
                    this.signatures.add(Utils.HEX
                            .encode(SteemJUtils.createSignedTransaction(keyType, signature, requiredPrivateKey)));
                }
            }
        }
    }

    /**
     * @return The list of private keys required to sign this transaction.
     * @throws SteemInvalidTransactionException If the required private key is not present in the
     *                                          {@link eu.bittrade.libs.golosj.configuration.PrivateKeyStorage}.
     */
    protected List<ECKey> getRequiredSignatureKeys() throws SteemInvalidTransactionException {
        List<ECKey> requiredSignatures = new ArrayList<>();
        Map<SignatureObject, List<PrivateKeyType>> requiredAuthorities = getRequiredAuthorities();

        for (Entry<SignatureObject, List<PrivateKeyType>> requiredAuthority : requiredAuthorities.entrySet()) {
            if (requiredAuthority.getKey() instanceof AccountName) {
                for (PrivateKeyType requiredKeyType : requiredAuthority.getValue()) {
                    /*
                     * TODO: Minimize the required signatures. If the
                     * transaction requires the owner and the active key the
                     * Steem Node only allows the owner key signature. Therefore
                     * the following code replaces 'lower' keys if a higher key
                     * is required too.
                     */
                    requiredSignatures = getRequiredSignatureKeyForAccount(requiredSignatures,
                            (AccountName) requiredAuthority.getKey(), requiredKeyType);
                }
            } else if (requiredAuthority.getKey() instanceof Authority) {
                // TODO: Support authorities.
            } else {
                LOGGER.warn("Unknown SigningObject type {}", requiredAuthority.getKey());
            }
        }

        return requiredSignatures;
    }

    /**
     * Fetch the requested private key for the given <code>accountName</code>
     * from the {@link eu.bittrade.libs.golosj.configuration.PrivateKeyStorage}
     * and merge it into the <code>requiredSignatures</code> list.
     *
     * @param requiredSignatures A list of already fetched keys. This list is used to make sure
     *                           that a key is not added twice.
     * @param accountName        The account name to fetch the key for.
     * @param privateKeyType     The key type to fetch.
     * @return The <code>requiredSignatures</code> including the
     * <code>privateKeyType</code> for <code>accountName</code>.
     * @throws SteemInvalidTransactionException If the required private key is not present in the
     *                                          {@link eu.bittrade.libs.golosj.configuration.PrivateKeyStorage}.
     */
    private List<ECKey> getRequiredSignatureKeyForAccount(List<ECKey> requiredSignatures, AccountName accountName,
                                                          PrivateKeyType privateKeyType) throws SteemInvalidTransactionException {
        ECKey privateKey;
        Map<PrivateKeyType, ECKey> keys = new HashMap<>();
        List<ImmutablePair<PrivateKeyType, ECKey>> userKeys = SteemJConfig.getInstance().getPrivateKeyStorage().getPrivateKeysPerAccounts().get(accountName);
        if (userKeys == null)throw new SteemInvalidTransactionException(
                "Could not find private " + privateKeyType + " key for the user " + accountName.getName() + ".");

        for (ImmutablePair<PrivateKeyType, ECKey> pair : userKeys) {
            keys.put(pair.getLeft(), pair.getRight());
        }
        if (keys.containsKey(privateKeyType)) privateKey = keys.get(privateKeyType);
        else {
            if (privateKeyType == PrivateKeyType.POSTING && keys.containsKey(PrivateKeyType.ACTIVE))
                privateKey = keys.get(PrivateKeyType.ACTIVE);
            else throw new SteemInvalidTransactionException(
                    "Could not find private " + privateKeyType + " key for the user " + accountName.getName() + ".");
        }
       /* try {
            privateKey = SteemJConfig.getInstance().getPrivateKeyStorage().getKeyForAccount(privateKeyType,
                    accountName);
        } catch (InvalidParameterException ipe) {
            throw new SteemInvalidTransactionException(
                    "Could not find private " + privateKeyType + " key for the user " + accountName.getName() + ".");
        }*/

        if (!requiredSignatures.contains(privateKey)) {
            requiredSignatures.add(privateKey);
        }

        return requiredSignatures;
    }

    /**
     * This method creates a byte array based on a transaction object under the
     * use of a guide written by <a href="https://Steemit.com/Steem/@xeroc/">
     * Xeroc</a>. This method should only be used internally.
     * <p>
     * If a chainId is provided it will be added in front of the byte array.
     *
     * @return The serialized transaction object.
     * @throws SteemInvalidTransactionException If the transaction can not be signed.
     */
    @Override
    public byte[] toByteArray() throws SteemInvalidTransactionException {
        return toByteArray(SteemJConfig.getInstance().getChainId());
    }

    /**
     * Like {@link #toByteArray() toByteArray()}, but allows to define a Steem
     * chain id.
     *
     * @param chainId The HEX representation of the chain Id you want to use for
     * this transaction.
     * @return The serialized transaction object.
     * @throws SteemInvalidTransactionException If the transaction can not be signed.
     */
    @Nullable
    private byte[] chainIdByte;
    @Nullable
    private byte[] refBlockByte;
    @Nullable
    private byte[] refBlockPrefixByte;
    @Nullable
    private byte[] operationSizeByte;

    private HashMap<Operation, byte[]> operationHashMap = new HashMap<>(operations.size());

    @Nullable
    private byte[] extensionSizeByte;

    private HashMap<FutureExtensions, byte[]> extensionsHashMap = new HashMap<>(operations.size());


    protected byte[] toByteArray(String chainId) throws SteemInvalidTransactionException {
        try (ByteArrayOutputStream serializedTransaction = new ByteArrayOutputStream()) {
            if (chainId != null && !chainId.isEmpty()) {
                if (chainIdByte == null) chainIdByte = Utils.HEX.decode(chainId);
                serializedTransaction.write(chainIdByte);
            }
            if (refBlockByte == null)
                refBlockByte = SteemJUtils.transformShortToByteArray(this.getRefBlockNum());
            serializedTransaction.write(refBlockByte);

            if (refBlockPrefixByte == null)
                refBlockPrefixByte = SteemJUtils.transformIntToByteArray((int) this.getRefBlockPrefix());
            serializedTransaction.write(refBlockPrefixByte);


            serializedTransaction.write(this.getExpirationDate().toByteArray());

            if (operationSizeByte == null)
                operationSizeByte = SteemJUtils.transformLongToVarIntByteArray(this.getOperations().size());

            serializedTransaction.write(operationSizeByte);
            for (Operation operation : this.getOperations()) {
                if (!operationHashMap.containsKey(operation))
                    operationHashMap.put(operation, operation.toByteArray());

                serializedTransaction.write(operationHashMap.get(operation));
            }

            if (extensionSizeByte == null)
                extensionSizeByte = SteemJUtils.transformIntToVarIntByteArray(this.getExtensions().size());

            serializedTransaction.write(extensionSizeByte);
            for (FutureExtensions futureExtensions : this.getExtensions()) {
                if (!extensionsHashMap.containsKey(futureExtensions))
                    extensionsHashMap.put(futureExtensions, futureExtensions.toByteArray());
                serializedTransaction.write(extensionsHashMap.get(futureExtensions));
            }

            return serializedTransaction.toByteArray();
        } catch (IOException e) {
            throw new SteemInvalidTransactionException(
                    "A problem occured while transforming the transaction into a byte array.", e);
        }
    }


    private byte[] createSignedTransaction(int keyType, ECKey.ECDSASignature signature, ECKey requiredPrivateKey) {
        int headerByte = getHeaderByte(keyType, requiredPrivateKey);


        signedTransaction[0] = (byte) headerByte;
        System.arraycopy(Utils.bigIntegerToBytes(signature.r, 32), 0, signedTransaction, 1, 32);
        System.arraycopy(Utils.bigIntegerToBytes(signature.s, 32), 0, signedTransaction, 33, 32);

        return signedTransaction;
    }

    private byte[] signedTransaction = new byte[65];

    private HashMap<Integer, Integer> headerBytes = new HashMap<>();

    private int getHeaderByte(int keyType, ECKey requiredPrivateKey) {
        if (!headerBytes.containsKey(keyType)) {
            headerBytes.put(keyType, keyType + 27 + (requiredPrivateKey.isCompressed() ? 4 : 0));
        }
        return headerBytes.get(keyType);
    }

    @Override
    public String toString() {
        return "SignedTransaction{" +
                "signatures=" + signatures +
                '}';
    }
}
