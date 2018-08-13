package DiscordAPI.webSocket.jsonData.identity;

import DiscordAPI.webSocket.jsonData.IJSONObject;
import DiscordAPI.utils.DiscordLogger;
import DiscordAPI.utils.DiscordUtils;
import org.json.simple.JSONObject;

public class IdentityObject {
    private final DiscordLogger logger = new DiscordLogger(String.valueOf(this.getClass()));
    private volatile JSONObject identity;
    private final IDENTITY[] values;

    public IdentityObject() {
        logger.info("Creating bot's identity");
        this.identity = new JSONObject();
        this.values = IDENTITY.values();
        identity = logic(values);
    }

    private JSONObject logic(IJSONObject[] values) {
        JSONObject object = new JSONObject();
        for (IJSONObject d : values) {
            if (d == IDENTITY.token) {
                object.put(d, DiscordUtils.DefaultLinks.token);
            } else if (!d.getaClass().isEnum()) {
                object.put(d, d.getDefaultValue());
            } else if (d.getaClass().isEnum()) {
                object.put(d, logic((IJSONObject[]) d.getaClass().getEnumConstants()));
            }
        }
        return object;
    }

    public JSONObject getIdentity() {
        return identity;
    }
}
