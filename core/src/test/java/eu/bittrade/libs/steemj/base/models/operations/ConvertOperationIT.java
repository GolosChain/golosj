package eu.bittrade.libs.steemj.base.models.operations;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.instanceOf;

import java.util.ArrayList;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import eu.bittrade.libs.steemj.IntegrationTest;
import eu.bittrade.libs.steemj.base.models.AccountName;
import eu.bittrade.libs.steemj.base.models.Asset;
import eu.bittrade.libs.steemj.base.models.BaseTransactionalIntegrationTest;
import eu.bittrade.libs.steemj.base.models.SignedBlockWithInfo;
import eu.bittrade.libs.steemj.enums.AssetSymbolType;
import eu.bittrade.libs.steemj.exceptions.SteemCommunicationException;

/**
 * Verify the functionality of the "convert operation" under the use of real api
 * calls.
 * 
 * @author <a href="http://steemit.com/@dez1337">dez1337</a>
 */
public class ConvertOperationIT extends BaseTransactionalIntegrationTest {
    private static final long BLOCK_NUMBER_CONTAINING_OPERATION = 5764515;
    private static final int TRANSACTION_INDEX = 1;
    private static final int OPERATION_INDEX = 0;
    private static final String EXPECTED_OWNER = "mindhunter";
    private static final Asset EXPECTED_AMOUNT = new Asset();
    private static final String EXPECTED_TRANSACTION_HEX = "f68585abf4dceec8045701080764657a31333337390500"
            + "000100000000000000035342440000000000011b39df7757e8d202e850d45ac9f7de49cce804ed0cb3ace0cbe87"
            + "f34e9be7ee33f4f50c4212e551983a29f6f4827b96432a253400ecef29e468c1b31e33c559f2d";

    /**
     * <b>Attention:</b> This test class requires a valid active key of the used
     * "owner". If no active key is provided or the active key is not valid an
     * Exception will be thrown. The active key is passed as a -D parameter
     * during test execution.
     * 
     * @throws Exception
     *             If something went wrong.
     */
    @BeforeClass()
    public static void prepareTestClass() throws Exception {
        setupIntegrationTestEnvironmentForTransactionalTests();

        AccountName owner = new AccountName("dez1337");
        long requestId = 1337L;

        Asset amount = new Asset();
        amount.setAmount(1L);
        amount.setSymbol(AssetSymbolType.SBD);

        ConvertOperation convertOperation = new ConvertOperation(owner, requestId, amount);

        ArrayList<Operation> operations = new ArrayList<>();
        operations.add(convertOperation);

        signedTransaction.setOperations(operations);

        sign();

        // Set expected objects.
        EXPECTED_AMOUNT.setAmount(24);
        EXPECTED_AMOUNT.setSymbol(AssetSymbolType.SBD);
    }

    @Category({ IntegrationTest.class })
    @Test
    public void testOperationParsing() throws SteemCommunicationException {
        SignedBlockWithInfo blockContainingConvertOperation = steemJ.getBlock(BLOCK_NUMBER_CONTAINING_OPERATION);

        Operation convertOperation = blockContainingConvertOperation.getTransactions().get(TRANSACTION_INDEX)
                .getOperations().get(OPERATION_INDEX);

        assertThat(convertOperation, instanceOf(ConvertOperation.class));
        assertThat(((ConvertOperation) convertOperation).getOwner().getName(), equalTo(EXPECTED_OWNER));
        assertThat(((ConvertOperation) convertOperation).getAmount(), equalTo(EXPECTED_AMOUNT));
    }

    @Category({ IntegrationTest.class })
    @Test
    public void verifyTransaction() throws Exception {
        assertThat(steemJ.verifyAuthority(signedTransaction), equalTo(true));
    }

    @Category({ IntegrationTest.class })
    @Test
    public void getTransactionHex() throws Exception {
        assertThat(steemJ.getTransactionHex(signedTransaction), equalTo(EXPECTED_TRANSACTION_HEX));
    }
}
