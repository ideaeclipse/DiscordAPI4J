package ideaeclipse.DiscordAPI.objects;

import ideaeclipse.DiscordAPI.IPrivateBot;
import ideaeclipse.DiscordAPI.objects.Interfaces.IUser;
import ideaeclipse.DiscordAPI.utils.DiscordUtils;
import ideaeclipse.JsonUtilities.Json;
import ideaeclipse.JsonUtilities.Parser;

import java.util.Objects;

import static ideaeclipse.DiscordAPI.utils.DiscordUtils.DefaultLinks.*;
import static ideaeclipse.DiscordAPI.utils.RateLimitRecorder.QueueHandler.*;

/**
 * This class is used for storing and parsing DiscordUser data
 *
 * @author Ideaeclipse
 * @see ideaeclipse.DiscordAPI.objects.Payloads.DUser
 */
class DiscordUser implements IDiscordUser {
    private final IPrivateBot bot;
    private final Long id;
    private final String name;
    private final Integer discriminator;
    private IUser user;

    /**
     * @param id            DiscordUser Id
     * @param Name          DiscordUser Name
     * @param discriminator DiscordUser discriminator
     */
    private DiscordUser(final Long id, final String Name, final Integer discriminator, final IPrivateBot bot) {
        this.bot = bot;
        this.id = id;
        this.name = Name;
        this.discriminator = discriminator;
    }

    public String getName() {
        return name;
    }

    public Integer getDiscriminator() {
        return discriminator;
    }


    public Long getId() {
        return id;
    }


    @Override
    public String toString() {
        return "{DiscordUser} Id: " + id + " Name: " + name + " Discriminator: " + discriminator;
    }

    /**
     * Parses user data
     *
     * @author Ideaeclipse
     */
    static class UserP {
        private final Long id;
        private final IPrivateBot bot;
        private IDiscordUser user;
        private Json object;

        /**
         * @param id         user Id
         * @param DiscordBot bot
         */
        UserP(final Long id, final IPrivateBot DiscordBot) {
            this.id = id;
            this.bot = DiscordBot;
        }

        /**
         * @param object
         * @param bot
         */
        UserP(final Json object, final IPrivateBot bot) {
            this.object = object;
            this.bot = bot;
            this.id = null;
        }

        UserP logicId() {
            String s = String.valueOf(rateLimitRecorder.queue(new HttpEvent(RequestTypes.get, GUILD + bot.getGuildId() + MEMBER + "/" + id)));
            if (!s.equals("null")) {
                object = new Json(s);
                Payloads.DUser u = ParserObjects.convertToPayload(new Json((String) object.get("user")), Payloads.DUser.class);
                user = new DiscordUser(u.id, u.username, u.discriminator, bot);
            }
            return this;
        }

        UserP logic() {
            if (id == null) {
                IUser temp = null;
                if (bot.getUsers() != null) {
                    temp = DiscordUtils.Search.USER(bot.getUsers(), Long.parseUnsignedLong(String.valueOf(object.get("id"))));
                }
                if (temp == null) {
                    Payloads.DUser u = ParserObjects.convertToPayload(object, Payloads.DUser.class);
                    user = new DiscordUser(u.id, u.username, u.discriminator, bot);
                } else {
                    user = temp.getDiscordUser();
                }
            }
            return this;
        }

        IDiscordUser getUser() {
            return this.user;
        }
    }
}
