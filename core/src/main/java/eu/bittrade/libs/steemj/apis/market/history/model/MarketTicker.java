package eu.bittrade.libs.steemj.apis.market.history.model;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.fasterxml.jackson.annotation.JsonProperty;

import eu.bittrade.libs.steemj.base.models.Asset;

/**
 * This class represents a Steem "market_ticker" object of the
 * "market_history_plugin".
 * 
 * @author <a href="http://steemit.com/@dez1337">dez1337</a>
 */
public class MarketTicker {
    private double latest;
    @JsonProperty("lowest_ask")
    private double lowestAsk;
    @JsonProperty("highest_bid")
    private double highestBid;
    @JsonProperty("percent_change")
    private double percentChange;
    @JsonProperty("steem_volume")
    private Asset steemVolume;
    @JsonProperty("sbd_volume")
    private Asset sbdVolume;

    /**
     * @return the latest
     */
    public double getLatest() {
        return latest;
    }

    /**
     * @return the lowestAsk
     */
    public double getLowestAsk() {
        return lowestAsk;
    }

    /**
     * @return the highestBid
     */
    public double getHighestBid() {
        return highestBid;
    }

    /**
     * @return the percentChange
     */
    public double getPercentChange() {
        return percentChange;
    }

    /**
     * @return the steemVolume
     */
    public Asset getSteemVolume() {
        return steemVolume;
    }

    /**
     * @return the sbdVolume
     */
    public Asset getSbdVolume() {
        return sbdVolume;
    }

    @Override
    public String toString() {
        return "MarketTicker{" +
                "latest=" + latest +
                ", lowestAsk=" + lowestAsk +
                ", highestBid=" + highestBid +
                ", percentChange=" + percentChange +
                ", steemVolume=" + steemVolume +
                ", sbdVolume=" + sbdVolume +
                '}';
    }
}
