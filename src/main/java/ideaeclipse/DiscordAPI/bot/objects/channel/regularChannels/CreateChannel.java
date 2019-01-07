package ideaeclipse.DiscordAPI.bot.objects.channel.regularChannels;

import com.fasterxml.jackson.annotation.JsonFormat;
import ideaeclipse.DiscordAPI.bot.IPrivateBot;
import ideaeclipse.DiscordAPI.bot.objects.channel.IChannel;
import ideaeclipse.DiscordAPI.bot.objects.message.IMessage;
import ideaeclipse.DiscordAPI.bot.objects.message.MessageCreate;
import ideaeclipse.DiscordAPI.utils.Util;
import ideaeclipse.DiscordAPI.utils.annotations.JsonValidity;
import ideaeclipse.DiscordAPI.webSocket.RateLimitRecorder;
import ideaeclipse.JsonUtilities.Json;
import ideaeclipse.JsonUtilities.JsonArray;
import ideaeclipse.reflectionListener.Event;

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
 * @see ideaeclipse.DiscordAPI.webSocket.Wss#Wss(IPrivateBot, String)
 */
public class CreateChannel extends Event {
    private final IPrivateBot bot;
    private final IChannel channel;

    /**
     * Does a validity check {@link ideaeclipse.DiscordAPI.utils.Util#checkConstructor(Class, Json, IPrivateBot)}
     * and parses that data into a channel object
     *
     * @param json json object received from the websocket
     * @param bot  bot from util
     * @see ideaeclipse.DiscordAPI.utils.Util
     */
    private CreateChannel(@JsonValidity({"nsfw", "name", "id", "type"}) Json json, final IPrivateBot bot) {
        this.bot = bot;
        Map<Long, IMessage> messageHistory = new HashMap<>();
        long id = Long.parseUnsignedLong(String.valueOf(json.get("id")));
        if (Util.QueryMessages) {
            String response = String.valueOf(Util.rateLimitRecorder.queue(new RateLimitRecorder.QueueHandler.HttpEvent(RateLimitRecorder.QueueHandler.RequestTypes.get, "channels/" + id + "/messages?limit=100")));
            if (response.startsWith("[") && response.endsWith("]"))
                for (Json json1 : new JsonArray(response)) {
                    MessageCreate message = Util.checkConstructor(MessageCreate.class, json1, bot).getObject();
                    messageHistory.put(message.getMessage().getId(), message.getMessage());
                }
        }
        this.channel = new Channel(Boolean.parseBoolean(String.valueOf(json.get("nsfw")))
                , String.valueOf(json.get("name"))
                , id
                , Integer.parseInt(String.valueOf(json.get("type")))
                , messageHistory);
    }

    /**
     * @return created channel object from {@link #CreateChannel(Json, IPrivateBot)} )}
     */
    public IChannel getChannel() {
        return channel;
    }
}
