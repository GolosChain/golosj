package eu.bittrade.libs.steemj.base.models.deserializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.TreeNode;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import eu.bittrade.libs.steemj.base.models.GolosIoFilePath;

import java.io.IOException;

/**
 * Created by yuri yurivladdurain@gmail.com .
 */

public class GolosIoFileDesrializer extends JsonDeserializer<GolosIoFilePath> {
    @Override
    public GolosIoFilePath deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
        TreeNode node = jsonParser.getCodec().readTree(jsonParser);
        String all = node.toString();
        all = all.replaceAll("[{}\"\\\\]", "").trim();
        String[] values = all.split(",");
        String url = null;
        String error = null;
        for (String valuePair : values) {
            if (valuePair.substring(0,3).equals("url")){url = valuePair.substring(4);}
            else if (valuePair.substring(0,5).equals("error")){error = valuePair.substring(6);}
        }
        return new GolosIoFilePath(url, error);
    }
}
