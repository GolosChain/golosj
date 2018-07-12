package eu.bittrade.libs.golosj.apis.market.history.model;

import java.util.List;

import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * This class represents a Steem "order_book" object of the
 * "market_history_plugin".
 * 
 * @author <a href="http://steemit.com/@dez1337">dez1337</a>
 */
public class OrderBook {
    private List<Order> bids;
    private List<Order> asks;

    /**
     * @return the bids
     */
    public List<Order> getBids() {
        return bids;
    }

    /**
     * @return the asks
     */
    public List<Order> getAsks() {
        return asks;
    }

    @Override
    public String toString() {
        return "OrderBook{" +
                "bids=" + bids +
                ", asks=" + asks +
                '}';
    }
}
