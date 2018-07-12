package eu.bittrade.libs.golosj;

import eu.bittrade.libs.golosj.base.models.AccountName;
import eu.bittrade.libs.golosj.base.models.GolosIoFilePath;

import javax.annotation.Nonnull;
import java.io.File;
import java.io.IOException;

/**
 * Created by yuri yurivladdurain@gmail.com .
 * methods specific to golos.io webportal, not chain itself
 */

public interface GolosIoSpecificMethods {
    /**
     * upload file to golos.io internal server
     *
     * @param from         A
     *                     {@link eu.bittrade.libs.golosj.base.models.AccountName}
     *                     account, which from you post file. must have valid POSTING rpvate key in keystore, in order to work
     * @param fileToUplaod file.
     * @return path to uploaded file
     * @throws IOException various network issues can throw an exception;
     */
    @Nonnull
    GolosIoFilePath uploadFile(@Nonnull AccountName from, @Nonnull File fileToUplaod) throws IOException;
}
