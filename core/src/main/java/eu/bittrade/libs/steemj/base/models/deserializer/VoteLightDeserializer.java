package eu.bittrade.libs.steemj.base.models.deserializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.TreeNode;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.node.IntNode;
import com.fasterxml.jackson.databind.node.LongNode;
import com.fasterxml.jackson.databind.node.TextNode;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import eu.bittrade.libs.steemj.base.models.VoteLight;

import static eu.bittrade.libs.steemj.base.models.deserializer.VotesDeseriaizer.namesCashe;
import static eu.bittrade.libs.steemj.base.models.deserializer.VotesDeseriaizer.percentsCashe;

/**
 * Created by yuri on 22.01.18.
 */

public class VoteLightDeserializer extends JsonDeserializer<List<VoteLight>> {
    @Override
    public List<VoteLight> deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {

        TreeNode node = p.getCodec().readTree(p);
        List<VoteLight> out = new ArrayList<>(node.size());
        for (int i = 0; i < node.size(); i++) {
            TreeNode voter = node.get(i);
            try {
                int percent;
                IntNode percentNode = ((IntNode) voter.get("percent"));
                if (percentsCashe.containsKey(percentNode))
                    percent = percentsCashe.get(percentNode);
                else {
                    percent = percentNode.numberValue().intValue();
                    percentsCashe.put(percentNode, percent);
                }

                TextNode nameNode = ((TextNode) voter.get("voter"));
                String name;
                if (namesCashe.containsKey(nameNode))
                    name = namesCashe.get(nameNode);
                else {
                    name = nameNode.asText();
                    namesCashe.put(nameNode, name);
                }
                TreeNode rsharesNode = voter.get("rshares");
                long rshares;
                if (rsharesNode instanceof IntNode) {
                    rshares = ((IntNode) rsharesNode).longValue();
                } else if (rsharesNode instanceof LongNode) {
                    rshares = ((LongNode) rsharesNode).longValue();
                } else {
                    rshares = Long.parseLong(((TextNode) rsharesNode).textValue());
                }

                out.add(new VoteLight(name, rshares, percent));
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }

        }
        return out;
    }
}
