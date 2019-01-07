package ideaeclipse.DiscordAPI.bot.objects.user;

import com.fasterxml.jackson.annotation.JsonFormat;
import ideaeclipse.DiscordAPI.bot.IPrivateBot;
import ideaeclipse.DiscordAPI.utils.Util;
import ideaeclipse.DiscordAPI.utils.annotations.JsonValidity;
import ideaeclipse.JsonUtilities.Json;
import ideaeclipse.reflectionListener.Event;

@JsonFormat
/**
 * This class is called when a guild member update payload is sent to {@link ideaeclipse.DiscordAPI.webSocket.Wss}
 * It returns an a {@link IDiscordUser} object that replaces its original object
 *
 * Json Example
 *
 * {
 *   "nick": "Mayo",
 *   "roles": [
 *     "472235368863760384",
 *     "525059526345490442"
 *   ],
 *   "guild_id": "471104815192211458",
 *   "user": {
 *     "id": "304408618986504195",
 *     "avatar": "a_d4a797f21eaa2c13a96a26bd83858af3",
 *     "username": "luminol",
 *     "discriminator": "6666"
 *   }
 * }
 *
 * Called when a payload with the identifier of {@link ideaeclipse.DiscordAPI.webSocket.DispatchType#GUILD_MEMBER_UPDATE}
 *
 * @author Ideaeclipse
 * @see CreateDiscordUser
 * @see DeleteDiscordUser
 * @see IDiscordUser
 * @see ideaeclipse.DiscordAPI.webSocket.Wss#Wss(IPrivateBot, String)
 */
public class UpdateDiscordUser extends Event {
    private final IPrivateBot bot;
    private final IDiscordUser user;

    /**
     * {@link Util#checkConstructor(Class, Json, IPrivateBot)} validates that the json object has proper components
     * Uses {@link CreateDiscordUser} to parse the json string
     *
     * @param json json from {@link ideaeclipse.DiscordAPI.webSocket.Wss}
     */
    private UpdateDiscordUser(@JsonValidity({"nick", "user", "roles"}) final Json json, final IPrivateBot bot) {
        this.bot = bot;
        CreateDiscordUser user = Util.checkConstructor(CreateDiscordUser.class, json, bot).getObject();
        this.user = user.getUser();
    }

    /**
     * @return user object that was parsed from the json string
     */
    public IDiscordUser getUser() {
        return user;
    }
}
