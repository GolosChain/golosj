package eu.bittrade.libs.golosj.base.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import eu.bittrade.libs.golosj.base.models.operations.Operation;
import eu.bittrade.libs.golosj.communication.CommunicationHandler;
import eu.bittrade.libs.golosj.enums.OperationType;
import eu.bittrade.libs.golosj.enums.PrivateKeyType;
import eu.bittrade.libs.golosj.enums.ValidationType;
import eu.bittrade.libs.golosj.exceptions.SteemInvalidTransactionException;
import eu.bittrade.libs.golosj.interfaces.SignatureObject;
import eu.bittrade.libs.golosj.util.SteemJUtils;

import javax.annotation.Nonnull;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * Created by yuri yurivladdurain@gmail.com .
 */

public class AccountMetadataUpdateOperation extends Operation {
    @Nonnull
    @JsonProperty("account")
    private AccountName account;
    @Nonnull
    @JsonProperty("json_metadata")
    private String jsonMetadata;

    public AccountMetadataUpdateOperation() {
        super(true);
    }

    public AccountMetadataUpdateOperation(@Nonnull AccountName accountName, @Nonnull String jsonMetadata) {
        super(true);
        this.account = accountName;
        this.jsonMetadata = jsonMetadata;
    }

    public AccountMetadataUpdateOperation(@Nonnull AccountName accountName,@Nonnull GolosProfile profileData) {
        super(true);
        this.account = accountName;
        setProfileData(profileData);
    }

    public void setProfileData(@Nonnull GolosProfile profileData) {
        try {
            jsonMetadata = "{\"profile\":" + CommunicationHandler.getObjectMapper().writeValueAsString(profileData) + "}";
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }


    @Nonnull
    public AccountName getAccount() {
        return account;
    }

    public void setAccount(@Nonnull AccountName account) {
        this.account = account;
    }

    @Nonnull
    public String getJsonMetadata() {
        return jsonMetadata;
    }

    public void setJsonMetadata(@Nonnull String jsonMetadata) {
        this.jsonMetadata = jsonMetadata;
    }


    @Override
    public byte[] toByteArray() throws SteemInvalidTransactionException {
        try (ByteArrayOutputStream serializedAccountUpdateOperation = new ByteArrayOutputStream()) {
            serializedAccountUpdateOperation
                    .write(SteemJUtils.transformIntToVarIntByteArray(OperationType.ACCOUNT_UPDATE_OPERATION.ordinal()));

            serializedAccountUpdateOperation.write(this.getAccount().toByteArray());

            serializedAccountUpdateOperation
                    .write(SteemJUtils.transformStringToVarIntByteArray(this.getJsonMetadata()));

            return serializedAccountUpdateOperation.toByteArray();
        } catch (IOException e) {
            throw new SteemInvalidTransactionException(
                    "A problem occured while transforming the operation into a byte array.", e);
        }
    }

    @Override
    public void validate(ValidationType validationType) {

    }

    @Override
    public Map<SignatureObject, List<PrivateKeyType>> getRequiredAuthorities(Map<SignatureObject, List<PrivateKeyType>> requiredAuthoritiesBase) {
        return mergeRequiredAuthorities(requiredAuthoritiesBase, this.getAccount(), PrivateKeyType.POSTING);
    }

    @Override
    public String toString() {
        return "AccountMetadataUpdateOperation{" +
                "account=" + account +
                ", jsonMetadata='" + jsonMetadata + '\'' +
                '}';
    }
}
