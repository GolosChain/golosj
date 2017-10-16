package eu.bittrade.libs.golos4j;

import eu.bittrade.libs.steemj.Golos4J;
import eu.bittrade.libs.steemj.base.models.AccountName;
import eu.bittrade.libs.steemj.base.models.AppliedOperation;
import eu.bittrade.libs.steemj.base.models.operations.VoteOperation;
import eu.bittrade.libs.steemj.enums.PrivateKeyType;
import junit.framework.TestCase;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.junit.Before;
import org.junit.Test;
import org.junit.internal.runners.JUnit38ClassRunner;
import org.junit.runner.RunWith;

import javax.annotation.Nonnull;
import java.util.Map;

/**
 * Created by yuri yurivladdurain@gmail.com .
 */
@RunWith(JUnit38ClassRunner.class)
public class PrivateMethodsTest extends TestCase {
    @Nonnull
    Golos4J golos4J;

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
    public void qwerf1() {

    }
}
