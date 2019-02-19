package ideaeclipse.DiscordAPI.bot.objects.channel.directMessage;

import com.fasterxml.jackson.annotation.JsonFormat;
import ideaeclipse.DiscordAPI.bot.DiscordBot;
import ideaeclipse.DiscordAPI.bot.IDiscordBot;
import ideaeclipse.DiscordAPI.webSocket.Wss;
import ideaeclipse.DiscordAPI.bot.objects.channel.IChannel;
import ideaeclipse.DiscordAPI.bot.objects.user.IDiscordUser;
import ideaeclipse.DiscordAPI.utils.Util;
import ideaeclipse.DiscordAPI.utils.annotations.JsonValidity;
import ideaeclipse.JsonUtilities.Json;
import ideaeclipse.JsonUtilities.JsonArray;
import ideaeclipse.reflectionListener.parents.Event;

import java.util.LinkedList;
import java.util.List;

@JsonFormat
/**
 * Direct message
 *
 * recipients gets parsed for user id's and gets searched for in user's map
 * id, id of dm channel
 * type, integer value of channel {@link IChannel}
 *
 * {
 *   "last_message_id": "524800095367987210",
 *   "recipients": [
 *     {
 *       "id": "304408618986504195",
 *       "avatar": "a_d4a797f21eaa2c13a96a26bd83858af3",
 *       "username": "luminol",
 *       "discriminator": "6666"
 *     }
 *   ],
 *   "id": "477542016864092170",
 *   "type": 1
 * }
 *
 * Called when a payload with the identifier of {@link ideaeclipse.DiscordAPI.webSocket.DispatchType#CHANNEL_CREATE}
 *
 * @author Ideaeclipse
 * @see IChannel
 * @see DMChannel
 * @see Wss#Wss(IDiscordBot, String)
 */
public final class CreateDMChannel extends Event {
    private final DiscordBot bot;
    private final IChannel channel;

    /**
     * @param json json string from {@link Wss}
     */
    private CreateDMChannel(@JsonValidity({"recipients", "id", "type"}) final Json json, final DiscordBot bot) {
        this.bot = bot;
        List<IDiscordUser> recipients = new LinkedList<>();
        for (Object object : new JsonArray(String.valueOf(json.get("recipients")))) {
            Json json1 = new Json(String.valueOf(object));
            Util.check(this, "getId", json1).ifPresent(o -> recipients.add(IDiscordUser.parse(o)));
        }
        this.channel = new DMChannel(bot, Long.parseUnsignedLong(String.valueOf(json.get("id")))
                , Integer.parseInt(String.valueOf(json.get("type")))
                , recipients
        );
    }

    /**
     * fetches Id from recipients json object
     *
     * @param json recipients json object
     * @return Searched user from user map with the valid id key
     */
    public IDiscordUser getId(@JsonValidity("id") Json json) {
        return this.bot.getUsers().getByK1(Long.parseUnsignedLong(String.valueOf(json.get("id"))));
    }

    /**
     * @return return object from parsed data
     */
    public IChannel getChannel() {
        return channel;
    }
}
