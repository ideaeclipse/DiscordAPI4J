package ideaeclipse.DiscordAPINEW.bot.objects.user;

import com.fasterxml.jackson.annotation.JsonFormat;
import ideaeclipse.DiscordAPINEW.bot.IPrivateBot;
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
public class DeleteDiscordUser extends Event {
    private final IPrivateBot bot;
    private final IDiscordUser user;

    /**
     * {@link Util#checkConstructor(Class, Json, IPrivateBot)} validates json string for components
     * calls {@link #parse(Json)} to get id and then remove the key value pair from users with that id
     *
     * @param json json string from {@link ideaeclipse.DiscordAPINEW.webSocket.Wss}
     */
    private DeleteDiscordUser(@JsonValidity("user") final Json json, final IPrivateBot bot) {
        this.bot = bot;
        this.user = IDiscordUser.parse(Util.check(this, "parse", new Json(String.valueOf(json.get("user")))));
    }

    /**
     * @param json parses 'id' from sub json 'user' and removes it from the users map
     */
    public IDiscordUser parse(@JsonValidity("id") Json json) {
        long id = Long.parseUnsignedLong(String.valueOf(json.get("id")));
        IDiscordUser user = this.bot.getUsers().get(id);
        this.bot.getUsers().remove(id);
        return user;
    }

    /**
     * @return object of deleted user
     */
    public IDiscordUser getUser() {
        return user;
    }
}
