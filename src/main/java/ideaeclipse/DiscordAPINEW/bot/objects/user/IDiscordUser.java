package ideaeclipse.DiscordAPINEW.bot.objects.user;

import ideaeclipse.DiscordAPINEW.bot.objects.role.IRole;

import java.util.List;

/**
 * Interface for {@link DiscordUser} to protect resources
 *
 * @author Ideacelipse
 * @see CreateDiscordUser
 * @see UpdateDiscordUser
 * @see DeleteDiscordUser
 * @see DiscordUser
 */
public interface IDiscordUser {
    /**
     * @return users nickname
     */
    String getNickName();

    /**
     * @return users username
     */
    String getUsername();

    /**
     * @return users discriminator
     */
    int getDiscriminator();

    /**
     * @return users unique identifier
     */
    long getId();

    /**
     * @return list of roles that the user is apart of
     */
    List<IRole> getRoles();

    /**
     * Adds a role to a user
     *
     * @param role role you wish to add to this user
     */
    void addRole(final IRole role);

    /**
     * Ensures that there is no cast exception when trying to cast a return object to a Discord User
     * if it is not a valid IDiscordUser return null
     *
     * @param o Theoretical IDiscordUser Object
     * @return casted object
     */
    static IDiscordUser parse(final Object o) {
        if (o instanceof IDiscordUser) {
            return (IDiscordUser) o;
        }
        return null;
    }
}
