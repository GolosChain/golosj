package eu.bittrade.libs.steemj.base.models.deserializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.TreeNode;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import eu.bittrade.libs.steemj.base.models.Account;
import eu.bittrade.libs.steemj.base.models.Discussion;
import eu.bittrade.libs.steemj.base.models.ExtendedAccount;
import eu.bittrade.libs.steemj.base.models.Story;
import eu.bittrade.libs.steemj.communication.CommunicationHandler;

/**
 * Created by yuri on 06.11.17.
 */

public class StoryDeserializer extends JsonDeserializer<Story> {
    @Override
    public Story deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        Story story = new Story();
        TreeNode node = p.getCodec().readTree(p);
        TreeNode content = node.get("content");
        TreeNode currentRoute = node.get("current_route");
        story.setCurrentRoute(currentRoute.toString());
        if (content == null) return story;
        TreeNode accounts = node.get("accounts");
        List<Discussion> discussions = new ArrayList<>();
        if (content.size() == 0) return story;
        Iterator<String> iter = content.fieldNames();
        while (iter.hasNext()) {
            String s = iter.next();
            discussions.add(CommunicationHandler.getObjectMapper().treeToValue(content.get(s), Discussion.class));
        }
        List<ExtendedAccount> accountsList = new ArrayList<>();
        if (accounts != null && accounts.size() > 0) {
            iter = accounts.fieldNames();
            while (iter.hasNext()) {
                String s = iter.next();
                accountsList.add(CommunicationHandler.getObjectMapper().treeToValue(accounts.get(s), ExtendedAccount.class));
            }
        }
        story.setDiscussions(discussions);
        story.setInvolvedAccounts(accountsList);
        return story;
    }
}
