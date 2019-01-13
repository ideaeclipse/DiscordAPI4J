package ideaeclipse.DiscordAPI.bot.objects.presence;

import com.fasterxml.jackson.annotation.JsonFormat;
import ideaeclipse.DiscordAPI.bot.IDiscordBot;
import ideaeclipse.DiscordAPI.bot.objects.presence.game.IGame;
import ideaeclipse.DiscordAPI.bot.objects.presence.game.LoadGame;
import ideaeclipse.DiscordAPI.bot.objects.user.IDiscordUser;
import ideaeclipse.DiscordAPI.utils.Util;
import ideaeclipse.DiscordAPI.utils.annotations.JsonValidity;
import ideaeclipse.JsonUtilities.Json;
import ideaeclipse.reflectionListener.Event;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@JsonFormat
/**
 * This class parses a presence update payload into a presence class and potentially a game class
 *
 * Example json string
 *
 * Game is parsed by {@link ideaeclipse.DiscordAPI.bot.objects.presence.game.LoadGame}
 * user is parsed for the id and grabbed from {@link IDiscordBot#getUsers()}
 * status is the users status
 * {
 *   "game": {
 *     "assets": {
 *       "large_image": "403245045018525696",
 *       "large_text": "Talon"
 *     },
 *     "timestamps": {
 *       "start": 1545183893000
 *     },
 *     "name": "League of Legends",
 *     "created_at": 1545183893606,
 *     "state": "In Game",
 *     "id": "123f759f91ca980f",
 *     "type": 0,
 *     "application_id": "401518684763586560"
 *   },
 *   "client_status": {
 *     "desktop": "online"
 *   },
 *   "activities": [
 *     {
 *       "assets": {
 *         "large_image": "403245045018525696",
 *         "large_text": "Talon"
 *       },
 *       "timestamps": {
 *         "start": 1545183893000
 *       },
 *       "name": "League of Legends",
 *       "created_at": 1545183893606,
 *       "state": "In Game",
 *       "id": "123f759f91ca980f",
 *       "type": 0,
 *       "application_id": "401518684763586560"
 *     }
 *   ],
 *   "roles": [
 *
 *   ],
 *   "guild_id": "471104815192211458",
 *   "user": {
 *     "id": "226469650903334934"
 *   },
 *   "status": "online"
 * }
 *
 * Called when a payload with the identifier of {@link ideaeclipse.DiscordAPI.webSocket.DispatchType#PRESENCE_UPDATE}
 *
 * @author Ideaeclipse
 * @see IPresence
 * @see IGame
 * @see Presence
 * @see LoadGame
 * @see UserStatus
 * @see ideaeclipse.DiscordAPI.webSocket.Wss#Wss(IDiscordBot, String)
 */
public final class PresenceUpdate extends Event {
    private final IDiscordBot bot;
    private final IPresence presence;

    /**
     * {@link Util#checkConstructor(Class, Json, IDiscordBot)} validates json object has right keys
     * Filters {@link UserStatus} to get a enum value instead of a string
     *
     * @param json json from {@link ideaeclipse.DiscordAPI.webSocket.Wss}
     * @see UserStatus
     */
    private PresenceUpdate(@JsonValidity({"game", "status", "user"}) final Json json, final IDiscordBot bot) {
        this.bot = bot;
        List<UserStatus> filtered = Arrays.stream(UserStatus.values()).filter(o -> o.name().toLowerCase().equals(String.valueOf(json.get("status")).toLowerCase())).collect(Collectors.toList());
        LoadGame game = !String.valueOf(json.get("game")).equals("null") ? Util.checkConstructor(LoadGame.class, new Json(String.valueOf(json.get("game"))), bot).getObject() : null;
        this.presence = new Presence(!filtered.isEmpty() ? filtered.get(0) : UserStatus.invisible
                , IDiscordUser.parse(Util.check(this, "loadUsers", new Json(String.valueOf(json.get("user")))).getObject())
                , game != null ? game.getGame() : null);
    }

    /**
     * Searches through sub json user to find the id and then user the map to get a user object
     * Id will always be a long.
     *
     * @param json sub json user
     * @return user object from users using the found id
     */
    public IDiscordUser loadUsers(@JsonValidity({"id"}) Json json) {
        return this.bot.getUsers().getByK1(Long.parseUnsignedLong(String.valueOf(json.get("id"))));
    }

    /**
     * @return created presence object
     */
    public IPresence getPresence() {
        return presence;
    }
}
