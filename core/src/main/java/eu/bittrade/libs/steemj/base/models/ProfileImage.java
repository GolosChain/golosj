package eu.bittrade.libs.steemj.base.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import eu.bittrade.libs.steemj.base.models.deserializer.ProfileImageDeserializer;

/**
 * Created by yuri on 03.11.17.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonDeserialize(using = ProfileImageDeserializer.class)
public class ProfileImage {
    private String profilePath;

    public String getProfilePath() {
        return profilePath;
    }

    public ProfileImage(String profilePath) {
        this.profilePath = profilePath;
    }

    @Override
    public String toString() {
        return "ProfileImage{" +
                "profilePath='" + profilePath + '\'' +
                '}';
    }
}
