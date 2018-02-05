package eu.bittrade.libs.steemj;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.MultipartBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import org.apache.commons.lang3.ArrayUtils;

import org.bitcoinj.core.ECKey;
import org.bitcoinj.core.Sha256Hash;
import org.bitcoinj.core.Utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.annotation.Nonnull;

import eu.bittrade.libs.steemj.base.models.AccountName;
import eu.bittrade.libs.steemj.base.models.GolosIoFilePath;
import eu.bittrade.libs.steemj.configuration.SteemJConfig;
import eu.bittrade.libs.steemj.enums.PrivateKeyType;
import eu.bittrade.libs.steemj.util.ImmutablePair;
import eu.bittrade.libs.steemj.util.SteemJUtils;

/**
 * Created by yuri yurivladdurain@gmail.com .
 */

class GolosIoSpecificMethodsHandler implements GolosIoSpecificMethods {
    @Nonnull
    private OkHttpClient okHttpClient;
    @Nonnull
    private final SteemJConfig config;
    @Nonnull
    private final ObjectMapper mapper;

    public GolosIoSpecificMethodsHandler(@Nonnull SteemJConfig config, @Nonnull ObjectMapper mapper) {
        this.config = config;
        this.mapper = mapper;
        okHttpClient = new OkHttpClient();
        okHttpClient.setReadTimeout(50, TimeUnit.SECONDS);
        okHttpClient.setWriteTimeout(50, TimeUnit.SECONDS);
    }

    @Nonnull
    @Override
    public GolosIoFilePath uploadFile(@Nonnull AccountName from, @Nonnull File fileToUplaod) throws IOException {
        String loginPathPart = from.getName();
        List<ImmutablePair<PrivateKeyType, ECKey>> keys = config.getPrivateKeyStorage().getPrivateKeysPerAccounts().get(from);
        ECKey key = null;
        for (ImmutablePair<PrivateKeyType, ECKey> pair : keys) {
            if (pair.getLeft() != PrivateKeyType.MEMO) {
                key = pair.getRight();
                break;
            }
        }
        byte[] fileBytes = new byte[(int) fileToUplaod.length()];
        FileInputStream fis = new FileInputStream(fileToUplaod);
        fis.read(fileBytes);
        fis.close();
        byte[] signingBytes = ArrayUtils.addAll("ImageSigningChallenge".getBytes(Charset.forName("utf-8")), fileBytes);

        Sha256Hash messageAsHash = Sha256Hash.wrap(Sha256Hash.hash(signingBytes));
        ECKey.ECDSASignature signature = key.sign(messageAsHash);
        String signatureString = Utils.HEX.encode(SteemJUtils.createSignedTransaction(5, signature, key));

        RequestBody requestBody = new MultipartBuilder()
                .type(MultipartBuilder.FORM)
                .addPart(
                        RequestBody.create(MediaType.parse("file/*"), fileBytes))
                .build();
        Request request = new Request.Builder()
                .url(String.format("https://images.golos.io/%s/%s", loginPathPart, signatureString))
                .post(requestBody)
                .build();

        Response response = okHttpClient.newCall(request).execute();
        String responseString = response.body().string();
        return mapper.convertValue(responseString, GolosIoFilePath.class);
    }
}
