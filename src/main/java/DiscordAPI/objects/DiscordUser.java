package DiscordAPI.objects;

import DiscordAPI.IDiscordBot;
import DiscordAPI.utils.DiscordUtils;
import DiscordAPI.utils.Json;

import java.util.Objects;

import static DiscordAPI.utils.DiscordUtils.DefaultLinks.GUILD;
import static DiscordAPI.utils.DiscordUtils.DefaultLinks.MEMBER;
import static DiscordAPI.utils.DiscordUtils.DefaultLinks.rateLimitRecorder;
import static DiscordAPI.utils.RateLimitRecorder.QueueHandler.*;

/**
 * This class is used for storing and parsing DiscordUser data
 *
 * @author Ideaeclipse
 * @see DiscordAPI.objects.Payloads.DUser
 */
public class DiscordUser {
    private final Long id;
    private final String name;
    private final Integer discriminator;

    /**
     * @param id            DiscordUser Id
     * @param Name          DiscordUser Name
     * @param discriminator DiscordUser discriminator
     */
    private DiscordUser(final Long id, final String Name, final Integer discriminator) {
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
        private final IDiscordBot bot;
        private DiscordUser user;
        private Json object;

        /**
         * @param id         user Id
         * @param DiscordBot bot
         */
        UserP(final Long id, final IDiscordBot DiscordBot) {
            this.id = id;
            this.bot = DiscordBot;
        }

        /**
         * @param object
         * @param bot
         */
        UserP(Json object, IDiscordBot bot) {
            this.object = object;
            this.bot = bot;
            this.id = null;
        }

        UserP logicId() {
            object = new Json((String) rateLimitRecorder.queue(new HttpEvent(RequestTypes.get, GUILD + bot.getGuildId() + MEMBER + "/" + id)));
            Payloads.DUser u = Parser.convertToPayload(new Json((String) object.get("user")), Payloads.DUser.class);
            user = new DiscordUser(u.id, u.username, u.discriminator);
            return this;
        }

        UserP logic() {
            if (id == null) {
                User temp = DiscordUtils.Search.USER(Objects.requireNonNull(bot.getUsers()), Long.parseUnsignedLong((String) object.get("id")));
                if (temp == null) {
                    Payloads.DUser u = Parser.convertToPayload(object, Payloads.DUser.class);
                    user = new DiscordUser(u.id, u.username, u.discriminator);
                } else {
                    user = temp.getDiscordUser();
                }
            }
            return this;
        }

        DiscordUser getUser() {
            return this.user;
        }
    }
}
