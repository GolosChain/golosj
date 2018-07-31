package eu.bittrade.libs.golos4j;

import eu.bittrade.libs.golosj.Golos4J;
import eu.bittrade.libs.golosj.base.models.*;
import eu.bittrade.libs.golosj.base.models.operations.AccountUpdateOperation;
import eu.bittrade.libs.golosj.base.models.operations.Operation;
import eu.bittrade.libs.golosj.base.models.operations.VoteOperation;
import eu.bittrade.libs.golosj.communication.CommunicationHandler;
import eu.bittrade.libs.golosj.enums.DiscussionSortType;
import eu.bittrade.libs.golosj.enums.PrivateKeyType;
import eu.bittrade.libs.golosj.exceptions.SteemCommunicationException;
import eu.bittrade.libs.golosj.exceptions.SteemInvalidTransactionException;
import eu.bittrade.libs.golosj.util.ImmutablePair;
import junit.framework.TestCase;
import org.junit.Before;
import org.junit.Test;
import org.junit.internal.runners.JUnit38ClassRunner;
import org.junit.runner.RunWith;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

/**
 * Created by yuri yurivladdurain@gmail.com .
 */
@RunWith(JUnit38ClassRunner.class)
public class SignedMethodsTest extends TestCase {
    private boolean useTestNet = false;
    private AccountName ACCOUNT = new AccountName("yuri-vlad-second");
    private String accountActiveKey = "5K7YbhJZqGnw3hYzsmH5HbDixWP5ByCBdnJxM5uoe9LuMX5rcZV";
    private AccountName TESTNET_ACCOUNT = new AccountName("qwerty");
    private String testnetAccPosting = "P5KbaLKyg7rWZNWHVNqewHqQwN7CamUfCpGqMm7872K7oieYwQsM";
    private Golos4J golos4J;


    @Before
    public void setUp() {
        if (useTestNet) golos4J = Golos4J.getTestnet();
        else golos4J = Golos4J.getInstance();
        if (useTestNet)
            golos4J.addAccountUsingMasterPassword(TESTNET_ACCOUNT, testnetAccPosting);
        else golos4J.addAccount(ACCOUNT, new ImmutablePair<>(PrivateKeyType.ACTIVE, accountActiveKey), true);
    }
    @Test
    public void testCreatePost() throws Exception{
        golos4J.getSimplifiedOperations().createPost("test title","test content", new String[]{"test"});
    }

    @Test
    public void testVote() {

        Discussion discussion =
                null;
        try {
            discussion = golos4J.getDatabaseMethods().getDiscussionsBy(DiscussionQuery.newBuilder().setLimit(1).setTruncateBody(0).setSelectedAuthor(
                    useTestNet ? TESTNET_ACCOUNT : ACCOUNT).build(),
                    DiscussionSortType.GET_DISCUSSIONS_BY_BLOG).get(0);
            golos4J.getSimplifiedOperations().vote(discussion.getAuthor(), discussion.getPermlink(), (short) new Random().nextInt(100));
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }

    }


    @Test
    public void testUploadImage() throws Exception {
        GolosIoFilePath filePath = golos4J.getGolosIoSpecificMethods().uploadFile(useTestNet ? TESTNET_ACCOUNT : ACCOUNT, new File(getClass().getClassLoader().getResource("ThemeColors.png").getFile()));
        assertNotNull(filePath);
        System.out.println(filePath);
        String value = filePath.getError() == null ? filePath.getUrlString() : filePath.getError();
        assertNotNull(value);
    }

    @Test

    public void testVerifyAuthority() throws Exception {
        if (useTestNet) return;
        golos4J.addAccount(new AccountName("yuri-vlad"), new ImmutablePair<>(PrivateKeyType.ACTIVE, "5KZPvXWyLVeJ3prqwvEgqFuWghwMLWGuYAgGCJutVdfwWJZXWHm"), true);
        VoteOperation voteOperation = new VoteOperation(
                new AccountName("yuri-vlad"),
                new AccountName("yuri-vlad-second"),
                new Permlink("sdsffs1211"),
                (short) (100 * 100));

        ArrayList<Operation> operations = new ArrayList<>();
        operations.add(voteOperation);

        GlobalProperties globalProperties = Golos4J.getInstance().getDatabaseMethods().getDynamicGlobalProperties();

        SignedTransaction signedTransaction = new SignedTransaction(globalProperties.getHeadBlockId(), operations,
                null);

        signedTransaction.sign();

        Golos4J.getInstance().getDatabaseMethods().verifyAuthority(signedTransaction);
    }

    @Test
    public void testAccUpdate() throws Exception {
        if (useTestNet) {
            GolosProfile golosProfile = new GolosProfile();
            String newName = UUID.randomUUID().toString();
            golosProfile.setAbout(newName);

            Operation aoo = new AccountMetadataUpdateOperation(TESTNET_ACCOUNT,golosProfile);

            List<Operation> operations = new ArrayList<>();
            operations.add(aoo);

            GlobalProperties globalProperties = Golos4J.getInstance().getDatabaseMethods().getDynamicGlobalProperties();

            SignedTransaction signedTransaction = new SignedTransaction(globalProperties.getHeadBlockId(), operations,
                    null);

            signedTransaction.sign();
            golos4J.getNetworkBroadcastMethods().broadcastTransaction(signedTransaction);
        } else {
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
            golos4J.getNetworkBroadcastMethods().broadcastTransaction(signedTransaction);
        }
    }
}
