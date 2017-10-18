package eu.bittrade.libs.golos4j;

import eu.bittrade.libs.steemj.Golos4J;
import eu.bittrade.libs.steemj.base.models.AccountName;
import eu.bittrade.libs.steemj.base.models.AppliedOperation;
import eu.bittrade.libs.steemj.base.models.GolosIoFilePath;
import eu.bittrade.libs.steemj.base.models.operations.VoteOperation;
import eu.bittrade.libs.steemj.enums.PrivateKeyType;
import eu.bittrade.libs.steemj.util.AuthUtils;
import junit.framework.TestCase;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.junit.Before;
import org.junit.Test;
import org.junit.internal.runners.JUnit38ClassRunner;
import org.junit.runner.RunWith;

import javax.annotation.Nonnull;
import java.io.File;
import java.util.Map;

/**
 * Created by yuri yurivladdurain@gmail.com .
 */
@RunWith(JUnit38ClassRunner.class)
public class SignedMethodsTest extends TestCase {
    @Nonnull
    Golos4J golos4J = Golos4J.getInstance();

    @Before
    public void setUp() {
        golos4J.addAccount(new AccountName("yuri-vlad"), new ImmutablePair<>(PrivateKeyType.POSTING, "5KZPvXWyLVeJ3prqwvEgqFuWghwMLWGuYAgGCJutVdfwWJZXWHm"), true);
    }

    @Test
    public void testVote() throws Exception {
        //    golos4J.getSimplifiedOperations().vote(PublicMethodsTest.ACCOUNT, PublicMethodsTest.PERMLINK, (short) 50);
        Map<Integer, AppliedOperation> operationMap = golos4J.getDatabaseMethods().getAccountHistory(new AccountName("yuri-vlad"), 1000, 5);
        AppliedOperation operation = operationMap.get(operationMap.size() - 1);
        assertTrue(operation.getOp() instanceof VoteOperation);
    }

    @Test
    public void testCreateAccount() throws Exception {
        String login2 = "yuri-vlad-second";
        String newPassword = "234sfdgkh1ezedsiU234wewe235ym8jhlq1unA0tlkJKfdhyn";
        Map<PrivateKeyType, String> keysNew = AuthUtils.generatePrivateWiFs(login2, newPassword, PrivateKeyType.values());
    }

    @Test
    public void testUploadImage() throws Exception {
        String login2 = "yuri-vlad-second";
        golos4J.addAccountUsingMasterPassword(new AccountName(login2), "234sfdgkh1ezedsiU234wewe235ym8jhlq1unA0tlkJKfdhyn");
        GolosIoFilePath filePath = golos4J.getGolosIoSpecificMethods().uploadFile(new AccountName(login2), new File(getClass().getClassLoader().getResource("ThemeColors.png").getFile()));
        assertNotNull(filePath);
        String value = filePath.getError() == null ? filePath.getUrlString() : filePath.getError();
        assertNotNull(value);
    }
}
