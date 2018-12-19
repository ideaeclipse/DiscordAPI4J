package ideaeclipse.DiscordAPINEW.bot.objects.user;

import com.fasterxml.jackson.annotation.JsonFormat;
import ideaeclipse.DiscordAPINEW.bot.objects.role.IRole;

import java.util.List;

@JsonFormat
/**
 *{
 *   "nick": null,
 *   "joined_at": "2018-07-24T04:10:43.386000+00:00",
 *   "roles": [
 *     "472235368863760384"
 *   ],
 *   "deaf": false,
 *   "mute": false,
 *   "user": {
 *     "id": "178616639049170945",
 *     "avatar": "8c1ea850cfc7458bda25093918f0bb9c",
 *     "username": "Minghao",
 *     "discriminator": "8396"
 *   }
 * }
 */
public interface IDiscordUser {
    String getUsername();

    int getDiscriminator();

    long getId();

    List<IRole> getRoles();

    static IDiscordUser parse(final Object o){
        if(o instanceof IDiscordUser){
            return (IDiscordUser) o;
        }
        return null;
    }
}
