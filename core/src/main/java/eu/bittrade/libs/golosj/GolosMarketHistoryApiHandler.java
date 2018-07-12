package eu.bittrade.libs.golosj;


import eu.bittrade.libs.golosj.apis.market.history.MarketHistoryApi;
import eu.bittrade.libs.golosj.apis.market.history.model.*;
import eu.bittrade.libs.golosj.base.models.TimePointSec;
import eu.bittrade.libs.golosj.communication.CommunicationHandler;
import eu.bittrade.libs.golosj.configuration.SteemJConfig;
import eu.bittrade.libs.golosj.exceptions.SteemCommunicationException;

import javax.annotation.Nonnull;
import java.util.List;

/**
 * Created by yuri yurivladdurain@gmail.com .
 */

@SuppressWarnings("unused")
class GolosMarketHistoryApiHandler implements MarketHistory {
    @Nonnull
    private final SteemJConfig config;
    @Nonnull
    private final CommunicationHandler communicationHandler;
    @Nonnull
    private SteemJ steemJ;

    GolosMarketHistoryApiHandler(@Nonnull SteemJConfig config,
                                 @Nonnull CommunicationHandler communicationHandler,
                                 @Nonnull SteemJ steemJ) {
        this.config = config;
        this.communicationHandler = communicationHandler;
        this.steemJ = steemJ;
    }

    @Override
    public MarketTicker getTicker(CommunicationHandler communicationHandler) throws SteemCommunicationException {
        return MarketHistoryApi.getTicker(communicationHandler);
    }


    @Override
    public MarketVolume getVolume(CommunicationHandler communicationHandler) throws SteemCommunicationException {
        return MarketHistoryApi.getVolume(communicationHandler);
    }

    @Override
    public OrderBook getOrderBook(CommunicationHandler communicationHandler, short limit)
            throws SteemCommunicationException {
        return MarketHistoryApi.getOrderBook(communicationHandler, limit);
    }

    @Override
    public List<MarketTrade> getTradeHistory(CommunicationHandler communicationHandler, TimePointSec start,
                                             TimePointSec end, short limit) throws SteemCommunicationException {
        return MarketHistoryApi.getTradeHistory(communicationHandler, start, end, limit);
    }

    @Override
    public List<MarketTrade> getRecentTrades(CommunicationHandler communicationHandler, short limit)
            throws SteemCommunicationException {
        return MarketHistoryApi.getRecentTrades(communicationHandler, limit);
    }

    @Override
    public List<Bucket> getMarketHistory(CommunicationHandler communicationHandler, long bucketSeconds,
                                         TimePointSec start, TimePointSec end) throws SteemCommunicationException {
        return MarketHistoryApi.getMarketHistory(communicationHandler, bucketSeconds, start, end);
    }

    @Override
    public List<Integer> getMarketHistoryBuckets(CommunicationHandler communicationHandler)
            throws SteemCommunicationException {
        return MarketHistoryApi.getMarketHistoryBuckets(communicationHandler);
    }
}
