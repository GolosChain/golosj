package eu.bittrade.libs.steemj.enums;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * An enumeration for all existing APIs.
 *
 * @author <a href="http://steemit.com/@dez1337">dez1337</a>
 */
public enum SteemApis {
    LOGIN_API(1),
    NETWORK_NODE_API(null),
    NETWORK_BROADCAST_API(3),
    ACCOUNT_BY_KEY_API(2),
    ACCOUNT_STATISTICS_API(null),
    AUTH_UTIL_API(null),
    BLOCK_INFO_API(null),
    BLOCKCHAIN_STATISTICS_API(null),
    DATABASE_API(0),
    DEBUG_NODE_API(null),
    FOLLOW_API(5),
    MARKET_HISTORY_API(6),
    RAW_BLOCK_API(null);

    @Nullable
    private Integer apiId;

    @Nullable
    SteemApis(Integer apiId) {
        this.apiId = apiId;
    }

    @Nullable
    public Integer getApiId() {
        return apiId;
    }

    public void setApiId(@Nonnull Integer apiId) {
        this.apiId = apiId;
    }
}
