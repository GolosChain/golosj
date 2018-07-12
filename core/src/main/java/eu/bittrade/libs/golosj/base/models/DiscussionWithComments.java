package eu.bittrade.libs.golosj.base.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.ArrayList;
import java.util.List;

import eu.bittrade.libs.golosj.base.models.deserializer.StoryDeserializer;

/**
 * Created by yuri on 06.11.17.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonDeserialize(using = StoryDeserializer.class)
public class DiscussionWithComments {
    private String currentRoute = "";
    private List<Discussion> discussions = new ArrayList<>();
    private List<ExtendedAccount> involvedAccounts = new ArrayList<>();

    public DiscussionWithComments() {
    }

    public List<Discussion> getDiscussions() {
        return discussions;
    }

    public void setDiscussions(List<Discussion> discussions) {
        this.discussions = discussions;
    }

    public List<ExtendedAccount> getInvolvedAccounts() {
        return involvedAccounts;
    }

    public void setInvolvedAccounts(List<ExtendedAccount> involvedAccounts) {
        this.involvedAccounts = involvedAccounts;
    }

    public String getCurrentRoute() {
        return currentRoute;
    }

    public void setCurrentRoute(String currentRoute) {
        this.currentRoute = currentRoute;
    }

    @Override
    public String toString() {
        return "DiscussionWithComments{" +
                "currentRoute='" + currentRoute + '\'' +
                ", discussions=" + discussions +
                ", involvedAccounts=" + involvedAccounts +
                '}';
    }
}
