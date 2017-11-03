package eu.bittrade.libs.steemj.enums;

/**
 * An enumeration for all existing API methods.
 * 
 * @author <a href="http://steemit.com/@dez1337">dez1337</a>
 */
public enum RequestMethods {
    // database_api
    SET_BLOCK_APPLIED_CALLBACK, GET_OPS_IN_BLOCK, GET_ACCOUNT_COUNT, GET_REWARD_FUND, GET_ACCOUNTS, GET_LIQUIDITY_QUEUE, GET_ACCOUNT_HISTORY, GET_WITNESS_BY_ACCOUNT, GET_DISCUSSIONS_BY_AUTHOR_BEFORE_DATE, GET_ACCOUNT_VOTES, GET_ACTIVE_VOTES, GET_WITNESS_COUNT, GET_WITNESSES, GET_WITNESSES_BY_VOTE, LOOKUP_ACCOUNTS, LOOKUP_WITNESS_ACCOUNTS, GET_WITNESS_SCHEDULE, GET_CHAIN_PROPERTIES, GET_CONTENT, GET_CONTENT_REPLIES, GET_CURRENT_MEDIAN_HISTORY_PRICE, GET_MINER_QUEUE, GET_CONFIG, GET_TRENDING_TAGS, GET_HARDFORK_VERSION, GET_DYNAMIC_GLOBAL_PROPERTIES, GET_DISCUSSIONS_BY_TRENDING, GET_DISCUSSIONS_BY_TRENDING30, GET_DISCUSSIONS_BY_CREATED, GET_DISCUSSIONS_BY_ACTIVE, GET_DISCUSSIONS_BY_CASHOUT, GET_DISCUSSIONS_BY_VOTES, GET_DISCUSSIONS_BY_CHILDREN, GET_DISCUSSIONS_BY_HOT, GET_DISCUSSIONS_BY_FEED, GET_DISCUSSIONS_BY_BLOG, GET_DISCUSSIONS_BY_COMMENTS, GET_DISCUSSIONS_BY_PROMOTED, GET_BLOCK_HEADER, GET_BLOCK, GET_CONVERSION_REQUESTS, GET_FEED_HISTORY, GET_NEXT_SCHEDULED_HARDFORK, GET_OPEN_ORDERS, GET_ORDER_BOOK, GET_ACTIVE_WITNESSES, GET_KEY_REFERENCES, GET_REPLIES_BY_LAST_UPDATE, GET_POTENTIAL_SIGNATURES, GET_TRANSACTION_HEX, VERIFY_AUTHORITY,
    GET_STATE,
    // network_node_api (Not implemented as I am unable to test those methods)
    // GET_INFO, ADD_NODE, GET_CONNECTED_PEERS, GET_POTENTIAL_PEERS,
    // GET_ADVANCED_NODE_PARAMETERS, SET_ADVANCED_NODE_PARAMETERS,
    // login_api
    LOGIN, GET_API_BY_NAME, GET_VERSION,
    // network_broadcast_api
    BROADCAST_TRANSACTION, BROADCAST_TRANSACTION_SYNCHRONOUS,
    // follow_api
    GET_FOLLOWERS, GET_FOLLOWING, GET_FOLLOW_COUNT, GET_FEED_ENTRIES, GET_FEED, GET_BLOG_ENTRIES, GET_BLOG, GET_ACCOUNT_REPUTATIONS, GET_REBLOGGED_BY, GET_BLOG_AUTHORS,
    // market_history_api
    // GET_ORDER_BOOK (doubled with database_api)
    GET_TICKER, GET_VOLUME, GET_TRADE_HISTORY, GET_RECENT_TRADES, GET_MARKET_HISTORY, GET_MARKET_HISTORY_BUCKETS
}
