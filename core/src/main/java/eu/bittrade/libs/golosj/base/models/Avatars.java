package eu.bittrade.libs.golosj.base.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.HashMap;

import eu.bittrade.libs.golosj.base.models.deserializer.AvatarsDeserializer;

/**
 * Created by yuri on 27.11.17.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonDeserialize(using = AvatarsDeserializer.class)
public class Avatars {
    public HashMap<String, String> accountAvatars;

    public Avatars(HashMap<String, String> accountAvatars) {
        this.accountAvatars = accountAvatars;
    }
}
