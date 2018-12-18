package ideaeclipse.DiscordAPINEW.bot.objects.user;


import ideaeclipse.DiscordAPINEW.bot.objects.role.IRole;

import java.util.List;

public class DiscordUser implements IDiscordUser {
    private final String username;
    private final int discriminator;
    private final long id;
    private final List<IRole> roles;

    DiscordUser(final String username, final int discriminator, final long id, final List<IRole> roles) {
        this.username = username;
        this.discriminator = discriminator;
        this.id = id;
        this.roles = roles;
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
    public String toString() {
        return "{DiscordUser} Usernaame: " + this.username + " Discriminator: " + this.discriminator + " Id: " + this.id;
    }
}
