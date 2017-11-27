package eu.bittrade.libs.steemj.base.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.ArrayList;
import java.util.List;

import eu.bittrade.libs.steemj.base.models.deserializer.UserFeedDeserializer;
import eu.bittrade.libs.steemj.base.models.deserializer.UserFeedLightDeserializer;

/**
 * Created by yuri on 27.11.17.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonDeserialize(using = UserFeedLightDeserializer.class)
public class UserFeedLight {
    public List<DiscussionLight> discussions = new ArrayList<>();

    public UserFeedLight(List<DiscussionLight> discussions) {
        this.discussions = discussions;
    }

    public UserFeedLight() {
    }
}

