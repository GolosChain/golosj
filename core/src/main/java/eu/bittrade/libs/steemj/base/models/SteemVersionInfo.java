package eu.bittrade.libs.steemj.base.models;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * This class represents a Steem "steem_version_info" object.
 * 
 * @author <a href="http://steemit.com/@dez1337">dez1337</a>
 */
public class SteemVersionInfo {
    @JsonProperty("blockchain_version")
    private String blockchainVersion;
    @JsonProperty("steem_revision")
    private String steemRevision;
    @JsonProperty("fc_revision")
    private String fcRevision;

    /**
     * This object is only used to wrap the JSON response in a POJO, so
     * therefore this class should not be instantiated.
     */
    private SteemVersionInfo() {
    }

    /**
     * @return The blockchain version.
     */
    public String getBlockchainVersion() {
        return blockchainVersion;
    }

    /**
     * @return The latest commit id of this Steem version.
     */
    public String getSteemRevision() {
        return steemRevision;
    }

    /**
     * @return The latest commit id of the used fc version.
     */
    public String getFcRevision() {
        return fcRevision;
    }

    @Override
    public String toString() {
        return "SteemVersionInfo{" +
                "blockchainVersion='" + blockchainVersion + '\'' +
                ", steemRevision='" + steemRevision + '\'' +
                ", fcRevision='" + fcRevision + '\'' +
                '}';
    }
}
