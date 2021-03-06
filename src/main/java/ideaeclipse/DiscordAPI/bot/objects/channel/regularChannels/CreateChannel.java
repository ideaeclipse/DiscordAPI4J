package ideaeclipse.DiscordAPI.bot.objects.channel.regularChannels;

import com.fasterxml.jackson.annotation.JsonFormat;
import ideaeclipse.DiscordAPI.bot.DiscordBot;
import ideaeclipse.DiscordAPI.bot.IDiscordBot;
import ideaeclipse.DiscordAPI.webSocket.Wss;
import ideaeclipse.DiscordAPI.bot.objects.channel.IChannel;
import ideaeclipse.DiscordAPI.bot.objects.message.IMessage;
import ideaeclipse.DiscordAPI.utils.annotations.JsonValidity;
import ideaeclipse.JsonUtilities.Json;
import ideaeclipse.reflectionListener.parents.Event;

import java.util.HashMap;
import java.util.Map;

@JsonFormat
/**
 * Converts a json string into an {@link IChannel} object
 * Json example:
 *
 * all keys are matched to exact values in {@link IChannel} and {@link Channel}
 *
 * {
 *   "permission_overwrites": [
 *
 *   ],
 *   "nsfw": false,
 *   "parent_id": null,
 *   "name": "Text Channels",
 *   "guild_id": "471104815192211458",
 *   "position": 0,
 *   "id": "471104815192211459",
 *   "type": 4
 * }
 *
 * Called when a payload with the identifier of {@link ideaeclipse.DiscordAPI.webSocket.DispatchType#CHANNEL_CREATE}
 *
 * @author Ideaeclipse
 * @see UpdateChannel
 * @see DeleteChannel
 * @see IChannel
 * @see Wss#Wss(IDiscordBot, String)
 */
public final class CreateChannel extends Event {
    private final DiscordBot bot;
    private final IChannel channel;

    /**
     * Does a validity check {@link ideaeclipse.DiscordAPI.utils.Util#checkConstructor(Class, Json, DiscordBot)}
     * and parses that data into a channel object
     *
     * @param json json object received from the websocket
     * @param bot  bot from util
     * @see ideaeclipse.DiscordAPI.utils.Util
     */
    private CreateChannel(@JsonValidity({"nsfw", "name", "id", "type"}) Json json, final DiscordBot bot) {
        this.bot = bot;
        Map<Long, IMessage> messageHistory = new HashMap<>();
        long id = Long.parseUnsignedLong(String.valueOf(json.get("id")));
        /*
        if (bot.queryMessages()) {
            String response = String.valueOf(bot.getRateLimitRecorder().queue(new HttpEvent(bot, RequestTypes.GET, "channels/" + id + "/messages?limit=100")));
            if (response.startsWith("[") && response.endsWith("]"))
                for (Object object : new JsonArray(response)) {
                    Json json1 = new Json(String.valueOf(object));
                    MessageCreate message = Util.checkConstructor(MessageCreate.class, json1, bot).getObject();
                    messageHistory.put(message.getMessage().getId(), message.getMessage());
                }
        }
        */
        this.channel = new Channel(bot, Boolean.parseBoolean(String.valueOf(json.get("nsfw")))
                , String.valueOf(json.get("name"))
                , id
                , Integer.parseInt(String.valueOf(json.get("type")))
                , messageHistory);
    }

    /**
     * @return created channel object from {@link #CreateChannel(Json, DiscordBot)} )}
     */
    public IChannel getChannel() {
        return channel;
    }
}
