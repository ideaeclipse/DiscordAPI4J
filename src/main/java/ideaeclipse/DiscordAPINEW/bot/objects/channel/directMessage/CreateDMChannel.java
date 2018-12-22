package ideaeclipse.DiscordAPINEW.bot.objects.channel.directMessage;

import com.fasterxml.jackson.annotation.JsonFormat;
import ideaeclipse.DiscordAPINEW.bot.IPrivateBot;
import ideaeclipse.DiscordAPINEW.bot.objects.IDiscordAction;
import ideaeclipse.DiscordAPINEW.bot.objects.channel.IChannel;
import ideaeclipse.DiscordAPINEW.bot.objects.user.IDiscordUser;
import ideaeclipse.DiscordAPINEW.utils.Util;
import ideaeclipse.DiscordAPINEW.utils.annotations.JsonValidity;
import ideaeclipse.JsonUtilities.Json;
import ideaeclipse.JsonUtilities.JsonArray;
import ideaeclipse.reflectionListener.Event;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

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
 * Called when a payload with the identifier of {@link ideaeclipse.DiscordAPINEW.webSocket.DispatchType#CHANNEL_CREATE}
 *
 * @author Ideaeclipse
 * @see IChannel
 * @see DMChannel
 * @see ideaeclipse.DiscordAPINEW.webSocket.Wss#Wss(IPrivateBot, String)
 */
public class CreateDMChannel extends Event implements IDiscordAction {
    private final Map<Long, IDiscordUser> users;
    private final Map<Long, IChannel> channels;

    /**
     * @param users    map of users of server
     * @param channels map of channels of server
     * @see ideaeclipse.DiscordAPINEW.bot.IPrivateBot
     */
    public CreateDMChannel(final Map<Long, IDiscordUser> users, final Map<Long, IChannel> channels) {
        this.users = users;
        this.channels = channels;
    }

    /**
     * @param json json string from {@link ideaeclipse.DiscordAPINEW.webSocket.Wss}
     */
    @Override
    public void initialize(@JsonValidity( {"recipients", "id", "type"}) Json json) {
        List<IDiscordUser> recipients = new LinkedList<>();
        for (Json json1 : new JsonArray(String.valueOf(json.get("recipients")))) {
            Util.check(this, "getId", json1).ifPresent(o -> recipients.add(IDiscordUser.parse(o)));
        }
        this.channels.put(Long.parseUnsignedLong(String.valueOf(json.get("id")))
                , new DMChannel(Long.parseUnsignedLong(String.valueOf(json.get("id")))
                        , Integer.parseInt(String.valueOf(json.get("type")))
                        , recipients
                ));
    }

    /**
     * fetches Id from recipients json object
     *
     * @param json recipients json object
     * @return Searched user from user map with the valid id key
     */
    public IDiscordUser getId(@JsonValidity( "id") Json json) {
        return this.users.get(Long.parseUnsignedLong(String.valueOf(json.get("id"))));
    }
}
