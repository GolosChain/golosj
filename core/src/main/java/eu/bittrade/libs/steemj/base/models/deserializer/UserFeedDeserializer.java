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

import eu.bittrade.libs.steemj.base.models.Discussion;
import eu.bittrade.libs.steemj.base.models.UserFeed;
import eu.bittrade.libs.steemj.communication.CommunicationHandler;

/**
 * Created by yuri on 14.11.17.
 */

public class UserFeedDeserializer extends JsonDeserializer<UserFeed> {
    @Override
    public UserFeed deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        TreeNode node = p.getCodec().readTree(p);
        TreeNode content = node.get("content");
        if (content == null) {
            return new UserFeed(new ArrayList<Discussion>());
        }
        List<Discussion> discussions = new ArrayList<>();
        Iterator<String> iter = content.fieldNames();
        while (iter.hasNext()) {
            String s = iter.next();
            discussions.add(CommunicationHandler.getObjectMapper().treeToValue(content.get(s), Discussion.class));
        }
        return new UserFeed(discussions);
    }
}
