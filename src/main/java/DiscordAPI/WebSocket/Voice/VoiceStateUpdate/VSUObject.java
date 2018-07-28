package DiscordAPI.WebSocket.Voice.VoiceStateUpdate;

import DiscordAPI.DiscordBot;
import DiscordAPI.WebSocket.JsonData.IJSONObject;
import DiscordAPI.WebSocket.Utils.DiscordLogger;
import org.json.simple.JSONObject;

public class VSUObject {
    private DiscordLogger logger = new DiscordLogger(String.valueOf(this.getClass()));
    private DiscordBot bot;
    private JSONObject vsu;
    private Long guild_id;
    public VSUObject(DiscordBot bot,long guild_id){
        logger.info("Creating Voice State Update");
        this.bot = bot;
        this.guild_id = guild_id;
        vsu = logic(VSU.values());
    }

    private JSONObject logic(IJSONObject[] values) {
        JSONObject object = new JSONObject();
        for (IJSONObject d : values) {
            if (d == VSU.guild_id) {
                object.put(d, guild_id);
            } else if (!d.getaClass().isEnum()) {
                object.put(d, d.getDefaultValue());
            }
        }
        return object;
    }

    public JSONObject getVsu() {
        return vsu;
    }
}
