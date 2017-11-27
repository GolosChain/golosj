package eu.bittrade.libs.steemj.base.models;

import java.util.List;

import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * This class is the java implementation of the Steem "signed_block" object.
 * 
 * @author <a href="http://steemit.com/@dez1337">dez1337</a>
 */
public class SignedBlock extends SignedBlockHeader {
    // TODO: Original type is vector<signed_transaction> - Do we implement the
    // signed_transaction object?.
    protected List<SignedTransaction> transactions;

    /**
     * @return the transactions
     */
    public List<SignedTransaction> getTransactions() {
        return transactions;
    }

    @Override
    public String toString() {
        return "SignedBlock{" +
                "transactions=" + transactions +
                '}';
    }
}
