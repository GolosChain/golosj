package eu.bittrade.libs.golosj.base.models.deserializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.core.TreeNode;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.TextNode;

import java.io.IOException;

import eu.bittrade.libs.golosj.base.models.ProfileImage;
import eu.bittrade.libs.golosj.communication.CommunicationHandler;

/**
 * Created by yuri on 03.11.17.
 */

public class ProfileImageDeserializer extends JsonDeserializer<ProfileImage> {
    @Override
    public ProfileImage deserialize(JsonParser jsonParser, DeserializationContext deserializationContext)
            throws IOException {
        ObjectCodec codec = jsonParser.getCodec();
        TreeNode rootNode = codec.readTree(jsonParser);
        TreeNode metadata = rootNode.get("json_metadata");
        if (metadata == null)
            return null;
        String profileText = ((TextNode) metadata).textValue();
        JsonNode node = CommunicationHandler.getObjectMapper().readTree(profileText);

        if (node == null || !(node instanceof ObjectNode))
            return null;
        TreeNode profile = node.get("profile");
        if (profile == null) return null;
        TreeNode image = profile.get("profile_image");
        if (image == null)
            return null;
        return new ProfileImage(image.toString().replace("\"", ""));
    }
}

