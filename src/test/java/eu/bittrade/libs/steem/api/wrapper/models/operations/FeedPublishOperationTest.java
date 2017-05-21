package eu.bittrade.libs.steem.api.wrapper.models.operations;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import org.bitcoinj.core.Sha256Hash;
import org.bitcoinj.core.Utils;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import eu.bittrade.libs.steem.api.wrapper.BaseTest;
import eu.bittrade.libs.steem.api.wrapper.IntegrationTest;
import eu.bittrade.libs.steem.api.wrapper.enums.AssetSymbolType;
import eu.bittrade.libs.steem.api.wrapper.exceptions.SteemInvalidTransactionException;
import eu.bittrade.libs.steem.api.wrapper.models.AccountName;
import eu.bittrade.libs.steem.api.wrapper.models.Asset;
import eu.bittrade.libs.steem.api.wrapper.models.Price;
import eu.bittrade.libs.steem.api.wrapper.models.Transaction;

/**
 * Test a Steem "feed public operation" and verify the results against the api.
 * 
 * @author <a href="http://steemit.com/@dez1337">dez1337</a>
 */
public class FeedPublishOperationTest extends BaseTest {
    final String EXPECTED_BYTE_REPRESENTATION = "070764657a3133333773000000000000000353424400000000"
            + "640000000000000003535445454d0000";
    final String EXPECTED_TRANSACTION_HASH = "9d822ed47ef4f4be6dd337618228a8d225f0a12959769463af1fccc4df70aeb3";
    final String EXPECTED_TRANSACTION_SERIALIZATION = "00000000000000000000000000000000000000000000"
            + "00000000000000000000f68585abf4dce7c8045701070764657a31333337730000000000000003534244"
            + "00000000640000000000000003535445454d000000";

    private static FeedPublishOperation feedPublishOperation;
    private static Transaction feedPublishOperationOperationTransaction;

    @BeforeClass
    public static void setup() throws Exception {
        feedPublishOperation = new FeedPublishOperation();
        feedPublishOperation.setPublisher(new AccountName("dez1337"));

        Asset base = new Asset();
        base.setAmount(115);
        base.setSymbol(AssetSymbolType.SBD);
        Asset quote = new Asset();
        quote.setAmount(100);
        quote.setSymbol(AssetSymbolType.STEEM);

        Price exchangeRate = new Price();
        exchangeRate.setBase(base);
        exchangeRate.setQuote(quote);

        feedPublishOperation.setExchangeRate(exchangeRate);

        ArrayList<Operation> operations = new ArrayList<>();
        operations.add(feedPublishOperation);

        feedPublishOperationOperationTransaction = new Transaction();
        feedPublishOperationOperationTransaction.setExpirationDate(EXPIRATION_DATE);
        feedPublishOperationOperationTransaction.setRefBlockNum(REF_BLOCK_NUM);
        feedPublishOperationOperationTransaction.setRefBlockPrefix(REF_BLOCK_PREFIX);
        // TODO: Add extensions when supported.
        // transaction.setExtensions(extensions);
        feedPublishOperationOperationTransaction.setOperations(operations);
    }

    @Test
    public void testFeedPublishOperationToByteArray()
            throws UnsupportedEncodingException, SteemInvalidTransactionException {
        assertThat("Expect that the operation has the given byte representation.",
                Utils.HEX.encode(feedPublishOperation.toByteArray()), equalTo(EXPECTED_BYTE_REPRESENTATION));
    }

    @Test
    public void testFeedPublishOperationTransactionHex()
            throws UnsupportedEncodingException, SteemInvalidTransactionException {
        feedPublishOperationOperationTransaction.sign();

        assertThat("The serialized transaction should look like expected.",
                Utils.HEX.encode(feedPublishOperationOperationTransaction.toByteArray()),
                equalTo(EXPECTED_TRANSACTION_SERIALIZATION));
        assertThat("Expect that the serialized transaction results in the given hex.",
                Utils.HEX.encode(Sha256Hash
                        .wrap(Sha256Hash.hash(feedPublishOperationOperationTransaction.toByteArray())).getBytes()),
                equalTo(EXPECTED_TRANSACTION_HASH));
    }

    @Category({ IntegrationTest.class })
    @Test
    public void verifyTransaction() {

    }

    @Category({ IntegrationTest.class })
    @Test
    public void getTransactionHex() {

    }
}
