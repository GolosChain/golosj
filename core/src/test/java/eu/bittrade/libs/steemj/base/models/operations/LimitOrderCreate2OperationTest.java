package eu.bittrade.libs.steemj.base.models.operations;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import org.bitcoinj.core.Sha256Hash;
import org.bitcoinj.core.Utils;
import org.junit.BeforeClass;
import org.junit.Test;

import eu.bittrade.libs.steemj.base.models.AccountName;
import eu.bittrade.libs.steemj.base.models.Asset;
import eu.bittrade.libs.steemj.base.models.BaseTransactionalUnitTest;
import eu.bittrade.libs.steemj.base.models.Price;
import eu.bittrade.libs.steemj.base.models.TimePointSec;
import eu.bittrade.libs.steemj.enums.AssetSymbolType;
import eu.bittrade.libs.steemj.exceptions.SteemInvalidTransactionException;

/**
 * Test a Steem "limit order create 2 operation" and verify the results against
 * the api.
 * 
 * @author <a href="http://steemit.com/@dez1337">dez1337</a>
 */
public class LimitOrderCreate2OperationTest extends BaseTransactionalUnitTest {
    final String EXPECTED_BYTE_REPRESENTATION = "150764657a31333337bf85070001000000000000000353424400000000010000"
            + "000000000003534244000000000a0000000000000003535445454d000000e7c80457";
    final String EXPECTED_TRANSACTION_HASH = "e9bcd1316cd72a43190a8c10c7aab8fbc87a0287bb8ca0e9b449f50c6330a4de";
    final String EXPECTED_TRANSACTION_SERIALIZATION = "000000000000000000000000000000000000000000000000000000000"
            + "0000000f68585abf4dce8c8045701150764657a31333337bf850700010000000000000003534244000000000100000000"
            + "00000003534244000000000a0000000000000003535445454d000000e7c8045700";

    private static LimitOrderCreate2Operation limitOrderCreate2Operation;

    /**
     * Prepare the environment for this specific test.
     * 
     * @throws Exception
     *             If something went wrong.
     */
    @BeforeClass()
    public static void prepareTestClass() throws Exception {
        setupUnitTestEnvironmentForTransactionalTests();

        Asset base = new Asset();
        base.setAmount(1L);
        base.setSymbol(AssetSymbolType.SBD);

        Asset quote = new Asset();
        quote.setAmount(10L);
        quote.setSymbol(AssetSymbolType.STEEM);

        Price exchangeRate = new Price(base, quote);

        Asset amountToSell = new Asset();
        amountToSell.setAmount(1L);
        amountToSell.setSymbol(AssetSymbolType.SBD);

        TimePointSec expirationDate = new TimePointSec(EXPIRATION_DATE);
        boolean fillOrKill = false;
        long orderId = 492991L;
        AccountName owner = new AccountName("dez1337");

        limitOrderCreate2Operation = new LimitOrderCreate2Operation(owner, orderId, amountToSell, fillOrKill,
                exchangeRate, expirationDate);

        ArrayList<Operation> operations = new ArrayList<>();
        operations.add(limitOrderCreate2Operation);

        signedTransaction.setOperations(operations);
    }

    @Override
    @Test
    public void testOperationToByteArray() throws UnsupportedEncodingException, SteemInvalidTransactionException {
        assertThat("Expect that the operation has the given byte representation.",
                Utils.HEX.encode(limitOrderCreate2Operation.toByteArray()), equalTo(EXPECTED_BYTE_REPRESENTATION));
    }

    @Override
    @Test
    public void testTransactionWithOperationToHex()
            throws UnsupportedEncodingException, SteemInvalidTransactionException {
        sign();

        assertThat("The serialized transaction should look like expected.",
                Utils.HEX.encode(signedTransaction.toByteArray()), equalTo(EXPECTED_TRANSACTION_SERIALIZATION));
        assertThat("Expect that the serialized transaction results in the given hex.",
                Utils.HEX.encode(Sha256Hash.wrap(Sha256Hash.hash(signedTransaction.toByteArray())).getBytes()),
                equalTo(EXPECTED_TRANSACTION_HASH));
    }
}
