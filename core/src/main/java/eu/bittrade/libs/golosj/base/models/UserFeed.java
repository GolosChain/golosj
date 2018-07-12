package eu.bittrade.libs.golosj.base.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.ArrayList;
import java.util.List;

import eu.bittrade.libs.golosj.base.models.deserializer.UserFeedDeserializer;

/**
 * Created by yuri on 14.11.17.
 */
@JsonIgnoreProperties (ignoreUnknown = true)
@JsonDeserialize(using = UserFeedDeserializer.class)
public class UserFeed {
    private List<Discussion> discussions = new ArrayList<>();

    public UserFeed(List<Discussion> discussions) {
        this.discussions = discussions;
    }

    public List<Discussion> getDiscussions() {
        return discussions;
    }

    public UserFeed() {
    }

    public void setDiscussions(List<Discussion> discussions) {
        this.discussions = discussions;
    }
}

