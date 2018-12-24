package ideaeclipse.DiscordAPINEW.bot.objects.channel.regularChannels;

import com.fasterxml.jackson.annotation.JsonFormat;
import ideaeclipse.DiscordAPINEW.bot.IPrivateBot;
import ideaeclipse.DiscordAPINEW.bot.objects.channel.IChannel;
import ideaeclipse.DiscordAPINEW.bot.objects.message.IMessage;
import ideaeclipse.DiscordAPINEW.bot.objects.message.MessageCreate;
import ideaeclipse.DiscordAPINEW.utils.Util;
import ideaeclipse.DiscordAPINEW.utils.annotations.JsonValidity;
import ideaeclipse.DiscordAPINEW.webSocket.RateLimitRecorder;
import ideaeclipse.JsonUtilities.Json;
import ideaeclipse.JsonUtilities.JsonArray;
import ideaeclipse.reflectionListener.Event;
import ideaeclipse.reflectionListener.EventManager;

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
 * Called when a payload with the identifier of {@link ideaeclipse.DiscordAPINEW.webSocket.DispatchType#CHANNEL_CREATE}
 *
 * @author Ideaeclipse
 * @see UpdateChannel
 * @see DeleteChannel
 * @see IChannel
 * @see ideaeclipse.DiscordAPINEW.webSocket.Wss#Wss(IPrivateBot, String)
 */
public class CreateChannel extends Event {
    private final IPrivateBot bot;
    private final IChannel channel;

    /**
     * Does a validity check {@link ideaeclipse.DiscordAPINEW.utils.Util#checkConstructor(Class, Json, IPrivateBot)}
     * and parses that data into a channel object
     *
     * @param json json object received from the websocket
     * @param bot  bot from util
     * @see ideaeclipse.DiscordAPINEW.utils.Util
     */
    private CreateChannel(@JsonValidity({"nsfw", "name", "id", "type"}) Json json, final IPrivateBot bot) {
        this.bot = bot;
        Map<Long, IMessage> messageHistory = new HashMap<>();
        long id = Long.parseUnsignedLong(String.valueOf(json.get("id")));
        for (Json json1 : new JsonArray(String.valueOf(Util.rateLimitRecorder.queue(new RateLimitRecorder.QueueHandler.HttpEvent(RateLimitRecorder.QueueHandler.RequestTypes.get, "channels/" + id + "/messages?limit=100"))))) {
            MessageCreate message = Util.checkConstructor(MessageCreate.class,json1,bot).getObject();
            messageHistory.put(message.getMessage().getId(),message.getMessage());
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
