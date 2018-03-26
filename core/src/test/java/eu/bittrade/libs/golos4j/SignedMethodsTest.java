package eu.bittrade.libs.golos4j;

import junit.framework.TestCase;


import org.junit.Before;
import org.junit.Test;
import org.junit.internal.runners.JUnit38ClassRunner;
import org.junit.runner.RunWith;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Nonnull;

import eu.bittrade.libs.steemj.Golos4J;
import eu.bittrade.libs.steemj.base.models.AccountName;
import eu.bittrade.libs.steemj.base.models.AppliedOperation;
import eu.bittrade.libs.steemj.base.models.GlobalProperties;
import eu.bittrade.libs.steemj.base.models.GolosIoFilePath;
import eu.bittrade.libs.steemj.base.models.GolosProfile;
import eu.bittrade.libs.steemj.base.models.PublicKey;
import eu.bittrade.libs.steemj.base.models.SignedTransaction;
import eu.bittrade.libs.steemj.base.models.operations.AccountUpdateOperation;
import eu.bittrade.libs.steemj.base.models.operations.Operation;
import eu.bittrade.libs.steemj.base.models.operations.VoteOperation;
import eu.bittrade.libs.steemj.communication.CommunicationHandler;
import eu.bittrade.libs.steemj.enums.PrivateKeyType;
import eu.bittrade.libs.steemj.util.AuthUtils;
import eu.bittrade.libs.steemj.util.ImmutablePair;

/**
 * Created by yuri yurivladdurain@gmail.com .
 */
@RunWith(JUnit38ClassRunner.class)
public class  SignedMethodsTest extends TestCase {
    @Nonnull
    Golos4J golos4J = Golos4J.getInstance();

    @Before
    public void setUp() {
        //  golos4J.addAccount(new AccountName("yuri-vlad-second"), new ImmutablePair<>(PrivateKeyType.POSTING, "5JeKh4taphREBdqfKzfapu6ar3gCNPPKgG5QbUzEwmuasSAQFs3"), true);
        golos4J.addAccount(new AccountName("yuri-vlad"), new ImmutablePair<>(PrivateKeyType.ACTIVE, "5KGbRrb7AcZruv2ECPzDDVYBhe2451oRgZ9RjZbwx7QhRSU6onK"), true);
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
        System.out.println(keysNew);
    }

    @Test
    public void testUploadImage() throws Exception {
        String login2 = "yuri-vlad";
        golos4J.addAccountUsingMasterPassword(new AccountName(login2), "234sfdgkh1ezedsiU234wewe235ym8jhlq1unA0tlkJKfdhyn");
        GolosIoFilePath filePath = golos4J.getGolosIoSpecificMethods().uploadFile(new AccountName(login2), new File(getClass().getClassLoader().getResource("ThemeColors.png").getFile()));
        assertNotNull(filePath);

        String value = filePath.getError() == null ? filePath.getUrlString() : filePath.getError();
        assertNotNull(value);
    }


    @Test
    public void testAccUpdate() throws Exception {
        golos4J.addAccount(new AccountName("yuri-vlad"), new ImmutablePair<>(PrivateKeyType.ACTIVE, "5KGbRrb7AcZruv2ECPzDDVYBhe2451oRgZ9RjZbwx7QhRSU6onK"), true);

        GolosProfile gp = new GolosProfile();
        gp.setShownName("Yuri");
        String s = "{\"profile\":" + CommunicationHandler.getObjectMapper().writeValueAsString(gp) + "}";
        AccountUpdateOperation aoo = new AccountUpdateOperation(new AccountName("yuri-vlad"), null,
                null,
                null,
                new PublicKey("GLS69FZuT6LjUXmcQboMaJZQAFMF4RnbhBtQC3wNkeJGMSW3YXEQD"),
                s);

        List<Operation> operations = new ArrayList<>();
        operations.add(aoo);

        GlobalProperties globalProperties = Golos4J.getInstance().getDatabaseMethods().getDynamicGlobalProperties();

        SignedTransaction signedTransaction = new SignedTransaction(globalProperties.getHeadBlockId(), operations,
                null);

        signedTransaction.sign();

        Golos4J.getInstance().getNetworkBroadcastMethods().broadcastTransaction(signedTransaction);
    }
}
