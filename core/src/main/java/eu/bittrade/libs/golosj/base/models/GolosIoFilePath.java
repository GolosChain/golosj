package eu.bittrade.libs.golosj.base.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import eu.bittrade.libs.golosj.base.models.deserializer.GolosIoFileDesrializer;

import javax.annotation.Nullable;

/**
 * Created by yuri yurivladdurain@gmail.com .
 * class represents golos.io response on file upload
 */
@JsonDeserialize(using = GolosIoFileDesrializer.class)
public class GolosIoFilePath {
    @JsonProperty("url")
    @Nullable
    private String urlString;
    @Nullable
    private String error;

    @Nullable
    public String getUrlString() {
        return urlString;
    }

    public GolosIoFilePath( @Nullable String urlString, @Nullable String error) {
        this.urlString = urlString;
        this.error = error;
    }

    public GolosIoFilePath() {
    }

    @Nullable
    public String getError() {
        return error;
    }

    public void setUrlString(@Nullable String urlString) {
        this.urlString = urlString;
    }

    public void setError(@Nullable String error) {
        this.error = error;
    }

    @Override
    public String toString() {
        return "GolosIoFile{" +
                "urlString='" + urlString + '\'' +
                ", error='" + error + '\'' +
                '}';
    }
}
