package ideaeclipse.DiscordAPINEW.bot.objects.presence.game;

import com.fasterxml.jackson.annotation.JsonFormat;

@JsonFormat
/**
 *
 *
 * Example json string for game.
 *
 * Name is the program
 * State is the action of the program
 * Details is the main info
 * additionalDetails is image text inside assets
 * type is the type, 0 is game, 2 is music
 *{
 *   "assets": {
 *     "large_image": "spotify:444e1a72c2893bfdb30516f523309d1a1eaf384f",
 *     "large_text": "I Want To Die In New Orleans"
 *   },
 *   "timestamps": {
 *     "start": 1545188757894,
 *     "end": 1545188899865
 *   },
 *   "name": "Spotify",
 *   "flags": 48,
 *   "session_id": "b656fcb82955a8bcb99927699004c28c",
 *   "created_at": 1545188769694,
 *   "details": "Phantom Menace",
 *   "state": "$uicideBoy$",
 *   "id": "spotify:1",
 *   "type": 2,
 *   "sync_id": "6oyeeA0sHwHM1pPpZaIsrD",
 *   "party": {
 *     "id": "spotify:304408618986504195"
 *   }
 * }
 * @see Game
 * @see LoadGame
 * @author ideaeclipse
 */
public interface IGame {
    /**
     * @return name of program
     */
    String getName();

    /**
     * @return what the program is doing
     */
    String getState();

    /**
     * @return details on the program
     */
    String getDetails();

    /**
     * @return image details
     */
    String getAdditionalDetails();

    /**
     * @return type of program
     */
    int getType();

    static IGame parse(Object o) {
        if (o instanceof IGame) {
            return (IGame) o;
        }
        return null;
    }
}
