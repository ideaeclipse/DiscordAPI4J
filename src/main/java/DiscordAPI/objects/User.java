package DiscordAPI.objects;

import DiscordAPI.IDiscordBot;
import DiscordAPI.utils.DiscordUtils;
import DiscordAPI.utils.Json;
import org.json.simple.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static DiscordAPI.utils.DiscordUtils.DefaultLinks.GUILD;
import static DiscordAPI.utils.DiscordUtils.DefaultLinks.MEMBER;
import static DiscordAPI.utils.DiscordUtils.DefaultLinks.rateLimitRecorder;
import static DiscordAPI.utils.RateLimitRecorder.QueueHandler.*;

/**
 * This class is used for storing and parsing User data
 *
 * @author Ideaeclipse
 * @see DiscordAPI.objects.Payloads.DUser
 */
public class User {
    private final Long id;
    private final String name;
    private final Integer discriminator;
    private final List<Role> roles;

    /**
     * @param id            User Id
     * @param Name          User Name
     * @param discriminator User discriminator
     * @param roles         {@link Role}
     */
    private User(final Long id, final String Name, final Integer discriminator, final List<Role> roles) {
        this.id = id;
        this.name = Name;
        this.discriminator = discriminator;
        this.roles = roles;
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

    public List<Role> getRoles() {
        return roles;
    }

    @Override
    public String toString() {
        return "{User} Id: " + id + " Name: " + name + " Discriminator: " + discriminator + " Roles: " + roles;
    }

    /**
     * Parses user data
     *
     * @author Ideaeclipse
     */
    static class UserP {
        private final Long id;
        private final IDiscordBot bot;
        private User user;
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

        UserP logic() {
            if (object == null) {
                object = new Json((String) rateLimitRecorder.queue(new HttpEvent(RequestTypes.get, GUILD + bot.getGuildId() + MEMBER + "/" + id)));
            }
            List<Role> roles = new ArrayList<>();
            for (String s : Json.asList((String) object.get("roles"))) {
                Role role = DiscordUtils.Search.ROLES(bot.getRoles(), Long.parseUnsignedLong(s));
                if (role != null) {
                    roles.add(role);
                }
            }
            Payloads.DUser u = null;
            if (object.get("user") != null) {
                u = Parser.convertToPayload(new Json((String) object.get("user")), Payloads.DUser.class);
            }
            assert u != null;
            user = new User(u.id, u.username, u.discriminator, roles);
            return this;
        }

        User getUser() {
            return this.user;
        }
    }
}
