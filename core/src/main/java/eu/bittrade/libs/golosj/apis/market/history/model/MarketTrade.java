package eu.bittrade.libs.golosj.apis.market.history.model;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.fasterxml.jackson.annotation.JsonProperty;

import eu.bittrade.libs.golosj.base.models.Asset;
import eu.bittrade.libs.golosj.base.models.TimePointSec;

/**
 * This class represents a Steem "market_trade" object of the
 * "market_history_plugin".
 * 
 * @author <a href="http://steemit.com/@dez1337">dez1337</a>
 */
public class MarketTrade {
    private TimePointSec date;
    @JsonProperty("current_pays")
    private Asset currentPays;
    @JsonProperty("open_pays")
    private Asset openPays;

    /**
     * @return the date
     */
    public TimePointSec getDate() {
        return date;
    }

    /**
     * @return the currentPays
     */
    public Asset getCurrentPays() {
        return currentPays;
    }

    /**
     * @return the openPays
     */
    public Asset getOpenPays() {
        return openPays;
    }

    @Override
    public String toString() {
        return "MarketTrade{" +
                "date=" + date +
                ", currentPays=" + currentPays +
                ", openPays=" + openPays +
                '}';
    }
}
