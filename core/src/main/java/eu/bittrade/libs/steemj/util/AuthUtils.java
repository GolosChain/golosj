package eu.bittrade.libs.steemj.util;

import org.apache.commons.lang3.ArrayUtils;
import org.bitcoinj.core.AddressFormatException;
import org.bitcoinj.core.Base58;
import org.bitcoinj.core.ECKey;
import org.spongycastle.crypto.digests.GeneralDigest;
import org.spongycastle.crypto.digests.RIPEMD160Digest;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nonnull;

import eu.bittrade.libs.steemj.enums.PrivateKeyType;
import eu.bittrade.libs.steemj.enums.SteemitAddressPrefix;

/**
 * Created by yuri
 */

@SuppressWarnings("unused")
public class AuthUtils {
    private static final int HASH_BYTES_LENGTH = 4;

    private AuthUtils() {
    }

    public static Map<PrivateKeyType, String> generatePublicWiFs(@Nonnull String login,
                                                                 @Nonnull String password,
                                                                 @Nonnull PrivateKeyType[] roles) {
        Map<PrivateKeyType, String> out = new HashMap<>();
        MessageDigest messageDigest256 = null;
        try {
            messageDigest256 = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        for (PrivateKeyType role : roles) {
            final String seed = login + role.toString().toLowerCase() + password;
            final String brainKey = StringUtils.join(seed.trim().split("[\\t\\n\\v\\f\\r]+"), " ");
            byte[] hashSha256 = null;
            try {
                hashSha256 = messageDigest256.digest(brainKey.getBytes("UTF-8"));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            final BigInteger bigInteger = new BigInteger(1, hashSha256);

            final byte[] pubBuff = ECKey.fromPrivate(bigInteger).getPubKey();
            byte[] addy = ArrayUtils.addAll(pubBuff, generateChecksumRipeMd160(pubBuff));
            out.put(role, SteemitAddressPrefix.GLS.toString() + Base58.encode(addy));
        }
        return out;
    }

    public static Map<PrivateKeyType, String> generatePrivateWiFs(@Nonnull String login,
                                                                  @Nonnull String password,
                                                                  @Nonnull PrivateKeyType[] roles) {
        Map<PrivateKeyType, String> out = new HashMap<>();
        MessageDigest messageDigest256 = null;
        try {
            messageDigest256 = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        for (PrivateKeyType role : roles) {
            final String seed = login + role.toString().toLowerCase() + password;
            final String brainKey = StringUtils.join(seed.trim().split("/[\\t\\n\\v\\f\\r ]+/"), " ");
            byte[] hashSha256 = null;
            try {
                hashSha256 = messageDigest256.digest(brainKey.getBytes("UTF-8"));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            final byte[] privateKey = ArrayUtils.addAll(new byte[]{(byte) 0x80}/*version is 128, for check*/, hashSha256);
            final byte[] privateWiF = ArrayUtils.addAll(privateKey, generateChecksumSha256(privateKey));
            out.put(role, Base58.encode(privateWiF));
        }
        return out;
    }
/*

    public static GolosPublicWiF fromPrivate(@Nonnull GolosPrivateWif privateWif) throws GolosTransformationException {

        try {
            checkPrivateWiF(privateWif.getPrivateWiF());
        } catch (IllegalArgumentException e) {
            throw new GolosTransformationException(e.getMessage(), e);
        }

        final byte[] privateWifBytesWithHashAndVesrionNumber = Base58.decode(privateWif.getPrivateWiF());
        byte[] privateKeyBytes = ArrayUtils.subarray(privateWifBytesWithHashAndVesrionNumber, 1, privateWifBytesWithHashAndVesrionNumber.length - HASH_BYTES_LENGTH);

        ECKey ecKey = ECKey.fromPrivate(privateKeyBytes);
        byte[] addy = ArrayUtils.addAll(ecKey.getPubKey(), generateChecksumRipeMd160(ecKey.getPubKey()));
        return new GolosPublicWiF(GolosAddressPrefix.GLS.toString() + Base58.encode(addy));
    }
*/

    public static byte[] generateChecksumSha256(@Nonnull byte[] in) {
        MessageDigest messageDigest256 = null;
        try {
            messageDigest256 = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        byte[] checksum = messageDigest256.digest(in);
        checksum = messageDigest256.digest(checksum);
        return ArrayUtils.subarray(checksum, 0, HASH_BYTES_LENGTH);
    }

    public static byte[] generateChecksumRipeMd160(@Nonnull byte[] in) {
        final GeneralDigest digest = new RIPEMD160Digest();
        digest.update(in, 0, in.length);
        byte[] checkSum = new byte[digest.getDigestSize()];
        digest.doFinal(checkSum, 0);

        return ArrayUtils.subarray(checkSum, 0, HASH_BYTES_LENGTH);
    }

    public static void checkPrivateWiF(@Nonnull String privateWiFKey) throws IllegalArgumentException {
        if (privateWiFKey.length() != 51)
            throw new IllegalArgumentException("key must be 51 chars length");

        final byte[] privateWifBytesAll = Base58.decode(privateWiFKey);
        if (privateWifBytesAll[0] != (byte) 0x80)
            throw new IllegalArgumentException("wrong version, must be 0x80");
        byte[] privateKeyBytes = ArrayUtils.subarray(privateWifBytesAll, 0, privateWifBytesAll.length - HASH_BYTES_LENGTH);
        final byte[] checkSum = ArrayUtils.subarray(privateWifBytesAll, privateWifBytesAll.length - HASH_BYTES_LENGTH, privateWifBytesAll.length);
        byte[] newCheckSum = generateChecksumSha256(privateKeyBytes);
        if (!Arrays.equals(checkSum, newCheckSum))
            throw new IllegalArgumentException("Invalid WIF key (checksum miss-match)");

        privateKeyBytes = ArrayUtils.subarray(privateKeyBytes, 1, privateKeyBytes.length);
        if (privateKeyBytes.length != 32)
            throw new IllegalArgumentException("key must be 32 bytes length");
    }

    public static void checkPublicWiF(@Nonnull String publicWifKey) throws IllegalArgumentException {
        if ((publicWifKey.length() - SteemitAddressPrefix.GLS.toString().length() != 50))
            throw new IllegalArgumentException("key without prefix must be 40 chars length");
        if (!publicWifKey.substring(0, SteemitAddressPrefix.GLS.toString().length()).equals(SteemitAddressPrefix.GLS.toString())) {
            throw new IllegalArgumentException("key without prefix must be " + SteemitAddressPrefix.GLS);
        }
        publicWifKey = publicWifKey.substring(SteemitAddressPrefix.GLS.toString().length());
        final byte[] publicWifBytesWithHash = Base58.decode(publicWifKey);
        byte[] publicKeyBytes = ArrayUtils.subarray(publicWifBytesWithHash, 0, publicWifBytesWithHash.length - HASH_BYTES_LENGTH);
        final byte[] checkSum = ArrayUtils.subarray(publicWifBytesWithHash, publicWifBytesWithHash.length - HASH_BYTES_LENGTH, publicWifBytesWithHash.length);
        byte[] newCheckSum = generateChecksumRipeMd160(publicKeyBytes);
        if (!Arrays.equals(checkSum, newCheckSum))
            throw new IllegalArgumentException("Invalid WIF key (checksum miss-match)");
        if (publicKeyBytes.length != 33)
            throw new IllegalArgumentException("key must be 33 bytes length");
    }

    public static boolean isWiFsValid(@Nonnull String privateWiF, @Nonnull String publicWiF) {
        try {
            checkPrivateWiF(privateWiF);
            checkPublicWiF(publicWiF);
            byte[] privKeyBytesAll = Base58.decode(privateWiF);
            byte[] cleaned = ArrayUtils.subarray(privKeyBytesAll, 1, privKeyBytesAll.length - 4);
            byte[] publicKey = ECKey.fromPrivate(cleaned).getPubKey();

            byte[] pubPotential = Base58.decode(publicWiF.substring(3));
            byte[] potentialCleaned = ArrayUtils.subarray(pubPotential, 0, pubPotential.length - 4);
            return Arrays.equals(publicKey, potentialCleaned);
        } catch (AddressFormatException e) {
            e.printStackTrace();
            return false;
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            return false;
        }
    }
}
