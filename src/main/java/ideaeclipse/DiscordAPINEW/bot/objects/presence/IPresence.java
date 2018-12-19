package ideaeclipse.DiscordAPINEW.bot.objects.presence;

import com.fasterxml.jackson.annotation.JsonFormat;
import ideaeclipse.DiscordAPINEW.bot.IPrivateBot;
import ideaeclipse.DiscordAPINEW.bot.objects.presence.game.IGame;
import ideaeclipse.DiscordAPINEW.bot.objects.user.IDiscordUser;

@JsonFormat
/**
 * Example json string
 *
 * Game is parsed by {@link ideaeclipse.DiscordAPINEW.bot.objects.presence.game.LoadGame}
 * user is parsed for the id and grabbed from {@link IPrivateBot#getUsers()}
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
 */
public interface IPresence {
    /**
     * @return user's status
     */
    StatusTypes getStatus();

    /**
     * @return user object
     */
    IDiscordUser getUser();

    /**
     * @return game object if one.
     */
    IGame getGame();
}
