package ideaeclipse.DiscordAPINEW.bot.objects.user;

import com.fasterxml.jackson.annotation.JsonFormat;
import ideaeclipse.DiscordAPINEW.bot.objects.role.IRole;

import java.util.List;

@JsonFormat
/**
 *{
 *   "nick": "Mayo",
 *   "joined_at": "2018-07-24T00:02:20.696000+00:00",
 *   "roles": [
 *     "472235368863760384"
 *   ],
 *   "deaf": false,
 *   "mute": false,
 *   "user": {
 *     "id": "304408618986504195",
 *     "avatar": "a_d4a797f21eaa2c13a96a26bd83858af3",
 *     "username": "luminol",
 *     "discriminator": "6666"
 *   }
 * }
 */
public interface IDiscordUser {
    String getNickName();

    String getUsername();

    int getDiscriminator();

    long getId();

    List<IRole> getRoles();

    void addRole(final IRole role);

    static IDiscordUser parse(final Object o){
        if(o instanceof IDiscordUser){
            return (IDiscordUser) o;
        }
        return null;
    }
}
