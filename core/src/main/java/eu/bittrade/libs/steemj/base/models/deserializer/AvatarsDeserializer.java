package eu.bittrade.libs.steemj.base.models.deserializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.core.TreeNode;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.TextNode;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;

import eu.bittrade.libs.steemj.base.models.Avatars;
import eu.bittrade.libs.steemj.communication.CommunicationHandler;

/**
 * Created by yuri on 27.11.17.
 */

public class AvatarsDeserializer extends JsonDeserializer<Avatars> {
    @Override
    public Avatars deserialize(JsonParser jsonParser, DeserializationContext ctxt) throws IOException {
        HashMap<String, String> out = new HashMap<>();
        ObjectCodec codec = jsonParser.getCodec();
        TreeNode rootNode = codec.readTree(jsonParser);
        TreeNode accs = rootNode.get("accounts");
        Iterator<String> iter = accs.fieldNames();
        while (iter.hasNext()) {
            String s = iter.next();
            TreeNode firstAccount = accs.get(s);
            TreeNode metadata = firstAccount.get("json_metadata");
            if (metadata == null || metadata.toString().isEmpty())
                return null;
            String profileText = ((TextNode) metadata).textValue();
            ObjectNode node = (ObjectNode) CommunicationHandler.getObjectMapper().readTree(profileText);
            if (node == null) {
                out.put(s, null);
            }else  {
                TreeNode profile = node.get("profile");
                if (profile == null) out.put(s, null);
                else {
                    TreeNode image = profile.get("profile_image");
                    if (image != null)
                        out.put(s, image.toString().replace("\"", ""));
                    else out.put(s, null);
                }
            }

        }
        return new Avatars(out);
    }
}
