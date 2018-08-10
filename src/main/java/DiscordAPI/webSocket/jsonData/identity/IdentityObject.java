package DiscordAPI.webSocket.jsonData.identity;

import DiscordAPI.DiscordBot;
import DiscordAPI.webSocket.jsonData.IJSONObject;
import DiscordAPI.webSocket.utils.DiscordLogger;
import DiscordAPI.webSocket.utils.DiscordUtils;
import org.json.simple.JSONObject;

public class IdentityObject {
    private DiscordLogger logger = new DiscordLogger(String.valueOf(this.getClass()));
    private JSONObject identity;
    private IDENTITY[] values;
    private DiscordBot DiscordBot;

    public IdentityObject(DiscordBot DiscordBot) {
        logger.info("Creating bot's identity");
        this.identity = new JSONObject();
        this.values = IDENTITY.values();
        this.DiscordBot = DiscordBot;
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
