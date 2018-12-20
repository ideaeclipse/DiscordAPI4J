package ideaeclipse.DiscordAPINEW.bot.objects.user;


import ideaeclipse.DiscordAPINEW.bot.objects.role.IRole;
import ideaeclipse.DiscordAPINEW.utils.Util;
import ideaeclipse.DiscordAPINEW.webSocket.RateLimitRecorder;

import java.util.List;

import static ideaeclipse.DiscordAPINEW.utils.Util.rateLimitRecorder;

public class DiscordUser implements IDiscordUser {
    private final String nickname;
    private final String username;
    private final int discriminator;
    private final long id;
    private final List<IRole> roles;

    DiscordUser(final String nickname,final String username, final int discriminator, final long id, final List<IRole> roles) {
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
    public long getId() {
        return this.id;
    }

    @Override
    public List<IRole> getRoles() {
        return this.roles;
    }

    @Override
    public void addRole(final IRole role) {
        rateLimitRecorder.queue(new RateLimitRecorder.QueueHandler.HttpEvent(RateLimitRecorder.QueueHandler.RequestTypes.put, "guilds/" + Util.guildId + "/members/" + this.id + "/roles/" + role.getId()));
    }

    @Override
    public String toString() {
        return "{DiscordUser} NickName: " + this.nickname + " Username: " + this.username + " Discriminator: " + this.discriminator + " Id: " + this.id;
    }
}
