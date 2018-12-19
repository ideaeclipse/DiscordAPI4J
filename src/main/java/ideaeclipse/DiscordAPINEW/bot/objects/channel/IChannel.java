package ideaeclipse.DiscordAPINEW.bot.objects.channel;

import com.fasterxml.jackson.annotation.JsonFormat;
import ideaeclipse.DiscordAPINEW.bot.objects.user.IDiscordUser;
import ideaeclipse.DiscordAPINEW.webSocket.RateLimitRecorder;
import ideaeclipse.JsonUtilities.Json;

import java.util.List;

import static ideaeclipse.DiscordAPINEW.utils.Util.rateLimitRecorder;

@JsonFormat
/**
 * regular channel
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
 * Direct message
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
 */
public abstract class IChannel {
    public void sendMessage(final String message) {
        if (this.getType() == 0 || this.getType() == 1) {
            Json object = new Json();
            object.put("content", message);
            object.put("file", "file");
            rateLimitRecorder.queue(new RateLimitRecorder.QueueHandler.HttpEvent(RateLimitRecorder.QueueHandler.RequestTypes.sendJson, "channels/" + this.getId() + "/messages", object));
        }
    }

    public void uploadFile(final String file) {
        if (this.getType() == 0 || this.getType() == 1) {
            Json object = new Json();
            object.put("file", "multipart/form-data");
            Json url = new Json();
            url.put("url", "attachment://" + file);
            Json image = new Json();
            image.put("image", url);
            object.put("embed", image);
            rateLimitRecorder.queue(new RateLimitRecorder.QueueHandler.HttpEvent(RateLimitRecorder.QueueHandler.RequestTypes.sendFile, "channels/" + this.getId() + "/messages", file));
        }
    }

    public abstract boolean isNsfw();

    public abstract String getName();

    public abstract long getId();

    public abstract int getType();

    public abstract List<IDiscordUser> getReciepient();
}
