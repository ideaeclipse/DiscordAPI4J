package ideaeclipse.DiscordAPINEW.bot.objects.user;

import com.fasterxml.jackson.annotation.JsonFormat;
import ideaeclipse.DiscordAPINEW.bot.IPrivateBot;
import ideaeclipse.DiscordAPINEW.bot.objects.IDiscordAction;
import ideaeclipse.DiscordAPINEW.utils.Util;
import ideaeclipse.DiscordAPINEW.utils.annotations.JsonValidity;
import ideaeclipse.JsonUtilities.Json;
import ideaeclipse.reflectionListener.Event;
import ideaeclipse.reflectionListener.EventManager;

import java.util.Map;

@JsonFormat
/**
 * This object is called when a guild member remove payload is sent to {@link ideaeclipse.DiscordAPINEW.webSocket.Wss}
 * It will remove the user pased on id from the users map
 *
 * Json Example:
 *
 * Only search for 'id' under the sub json 'user'
 *
 * {
 *   "guild_id": "471104815192211458",
 *   "user": {
 *     "id": "525130696008663041",
 *     "avatar": null,
 *     "username": "bigMemes2k18",
 *     "discriminator": "4013"
 *   }
 * }
 *
 * Called when a payload with the identifier of {@link ideaeclipse.DiscordAPINEW.webSocket.DispatchType#GUILD_MEMBER_REMOVE}
 *
 * @author Ideaeclipse
 * @see CreateDiscordUser
 * @see UpdateDiscordUser
 * @see IDiscordUser
 * @see ideaeclipse.DiscordAPINEW.webSocket.Wss#Wss(IPrivateBot, String)
 */
public class DeleteDiscordUser extends Event implements IDiscordAction {
    private final Map<Long, IDiscordUser> users;

    /**
     * @param users users map
     */
    public DeleteDiscordUser(final Map<Long, IDiscordUser> users) {
        this.users = users;
    }

    /**
     * {@link Util#check(EventManager, Event, Json)} validates json string for components
     * calls {@link #parse(Json)} to get id and then remove the key value pair from users with that id
     *
     * @param json json string from {@link ideaeclipse.DiscordAPINEW.webSocket.Wss}
     */
    @Override
    public void initialize(@JsonValidity( "user") Json json) {
        Util.check(this, "parse", new Json(String.valueOf(json.get("user"))));
    }

    /**
     * @param json parses 'id' from sub json 'user' and removes it from the users map
     */
    public void parse(@JsonValidity( "id") Json json) {
        users.remove(Long.parseUnsignedLong(String.valueOf(json.get("id"))));
    }
}
