package ideaeclipse.DiscordAPI.bot.objects.user;


import ideaeclipse.DiscordAPI.bot.IDiscordBot;
import ideaeclipse.DiscordAPI.bot.objects.role.IRole;
import ideaeclipse.DiscordAPI.utils.MultiKeyMap;
import ideaeclipse.DiscordAPI.webSocket.rateLimit.HttpEvent;
import ideaeclipse.DiscordAPI.webSocket.rateLimit.RequestTypes;

/**
 * The class is created from parsing a user's json data
 *
 * @author Ideaeclipse
 * @see CreateDiscordUser
 * @see UpdateDiscordUser
 * @see DeleteDiscordUser
 * @see IDiscordUser
 */
public final class DiscordUser implements IDiscordUser {
    private final IDiscordBot bot;
    private final String nickname;
    private final String username;
    private final int discriminator;
    private final long id;
    private final MultiKeyMap<Long, String, IRole> roles;

    /**
     * @param nickname      users nickname
     * @param username      username
     * @param discriminator discriminator
     * @param id            unique identifier
     * @param roles         list of roles the user has
     */
    DiscordUser(final IDiscordBot bot, final String nickname, final String username, final int discriminator, final long id, final MultiKeyMap<Long, String, IRole> roles) {
        this.bot = bot;
        this.nickname = nickname;
        this.username = username;
        this.discriminator = discriminator;
        this.id = id;
        this.roles = roles;
    }

    @Override
    public String getNickName() {
        return this.nickname;
    }

    @Override
    public String getUsername() {
        return this.username;
    }

    @Override
    public int getDiscriminator() {
        return this.discriminator;
    }

    @Override
    public String getMention() {
        return "<@" + this.id + ">";
    }

    @Override
    public long getId() {
        return this.id;
    }

    @Override
    public MultiKeyMap<Long, String, IRole> getRoles() {
        return this.roles;
    }

    @Override
    public void addRole(final IRole role) {
        if (!this.getRoles().containsValue(role)) {
            this.bot.getRateLimitRecorder().queue(new HttpEvent(this.bot, RequestTypes.PUT, "guilds/" + this.bot.getGuildId() + "/members/" + this.id + "/roles/" + role.getId()));
        } else {
            System.err.println("Role not found");
        }
    }

    @Override
    public void removeRole(final IRole role) {
        if (this.getRoles().containsValue(role)) {
            this.bot.getRateLimitRecorder().queue(new HttpEvent(this.bot, RequestTypes.DELETE, "guilds/" + this.bot.getGuildId() + "/members/" + this.id + "/roles/" + role.getId()));
        }
    }

    @Override
    public String toString() {
        return "{DiscordUser} NickName: " + this.nickname + " Username: " + this.username + " Discriminator: " + this.discriminator + " Id: " + this.id;
    }
}
