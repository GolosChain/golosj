package eu.bittrade.libs.steemj.base.models.deserializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.TreeNode;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.node.IntNode;
import com.fasterxml.jackson.databind.node.TextNode;
import com.google.common.collect.HashBiMap;

import java.io.IOException;

import eu.bittrade.libs.steemj.util.ArrayMap;

/**
 * Created by yuri on 27.11.17.
 */

public class VotesDeseriaizer extends JsonDeserializer<ArrayMap<String, Integer>> {
    public static HashBiMap<IntNode, Integer> percentsCashe = HashBiMap.create(2000);
    static HashBiMap<TextNode, String> namesCashe = HashBiMap.create(2000);
    private static String voterField = "voter";
    private static String percentField = "percent";

    static {
        percentsCashe.put(new IntNode(10000), 10000);
        percentsCashe.put(new IntNode(100), 100);
        percentsCashe.put(new IntNode(9000), 9000);
        percentsCashe.put(new IntNode(8000), 8000);
        percentsCashe.put(new IntNode(7000), 7000);
        percentsCashe.put(new IntNode(6000), 6000);
        percentsCashe.put(new IntNode(5000), 5000);
        percentsCashe.put(new IntNode(4000), 4000);
        percentsCashe.put(new IntNode(3000), 3000);
        percentsCashe.put(new IntNode(2000), 2000);
        percentsCashe.put(new IntNode(1000), 1000);
        percentsCashe.put(new IntNode(0), 0);
    }

    @Override
    public ArrayMap<String, Integer> deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        ArrayMap<String, Integer> out = new ArrayMap<>(400);
        TreeNode node = p.getCodec().readTree(p);
        for (int i = 0; i < node.size(); i++) {
            TreeNode voter = node.get(i);
            try {
                IntNode percentNode = ((IntNode) voter.get(percentField));
                int percent = 0;
                if (percentsCashe.containsKey(percentNode))
                    percent = percentsCashe.get(percentNode);
                else {
                    percent = percentNode.numberValue().intValue();
                    percentsCashe.put(percentNode, percent);
                }
                TextNode nameNode = ((TextNode) voter.get(voterField));
                String name = "";
                if (namesCashe.containsKey(nameNode))
                    name = namesCashe.get(nameNode);
                else {
                    name = nameNode.asText();
                    namesCashe.put(nameNode, name);
                }
                out.put(name, percent);
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }

        }
        return out;
    }
}
