package DiscordAPI.WebSocket.JsonData.Identity;

import DiscordAPI.Bot.BotImpl;
import DiscordAPI.WebSocket.JsonData.IJSONObject;
import org.json.simple.JSONObject;

public class IdentityObject {
    private JSONObject identity;
    private IDENTITY[] values;
    private BotImpl botImpl;

    public IdentityObject(BotImpl botImpl) {
        this.identity = new JSONObject();
        this.values = IDENTITY.values();
        this.botImpl = botImpl;
        identity = logic(values);
    }

    private JSONObject logic(IJSONObject[] values) {
        JSONObject object = new JSONObject();
        for (IJSONObject d : values) {
            if (d == IDENTITY.token) {
                object.put(d, botImpl.getToken());
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
