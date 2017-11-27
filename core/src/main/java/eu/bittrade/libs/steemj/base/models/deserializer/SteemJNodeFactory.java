package eu.bittrade.libs.steemj.base.models.deserializer;

import com.fasterxml.jackson.databind.node.IntNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.NullNode;
import com.fasterxml.jackson.databind.node.NumericNode;
import com.fasterxml.jackson.databind.node.ValueNode;
import com.google.common.collect.BiMap;

/**
 * Created by yuri on 27.11.17.
 */

public class SteemJNodeFactory extends JsonNodeFactory {
    static BiMap<Integer, IntNode> percentNodeCashe = VotesDeseriaizer.percentsCashe.inverse();

    @Override
    public NumericNode numberNode(int v) {
        if (percentNodeCashe.containsKey(v)) return percentNodeCashe.get(v);
        else {
            IntNode node = IntNode.valueOf(v);
            VotesDeseriaizer.percentsCashe.put(node, v);
            return node;
        }
    }

    @Override
    public ValueNode numberNode(Integer value) {
        if (value == null) return NullNode.getInstance();
        if (percentNodeCashe.containsKey(value)) return percentNodeCashe.get(value);
        else {
            IntNode node = IntNode.valueOf(value);
            VotesDeseriaizer.percentsCashe.put(node, value);
            return node;
        }
    }
}
