package ideaeclipse.DiscordAPI.bot.objects.role;

/**
 * Interface for {@link Role} to protect accessible data
 *
 * @author Ideaeclipse
 * @see Role
 * @see CreateRole
 * @see UpdateRole
 * @see DeleteRole
 */
public interface IRole {
    /**
     * @return integer colour code for the role
     */
    int getColour();

    /**
     * @return boolean on whether the role is managed or not
     */
    boolean isManaged();

    /**
     * TODO: see if permissions are parsable
     *
     * @return int permission value
     */
    int getPermissionValue();

    /**
     * @return name of role
     */
    String getName();

    /**
     * @return mention string
     */
    String getMention();

    /**
     * @return if the role can be "@'ed"
     */
    boolean isMentionable();

    /**
     * @return position on the dash
     */
    int getPosition();

    /**
     * @return unique identifier for the role
     */
    long getId();

    /**
     * @return boolean, is the role's users shown in a separate category in the user dash
     */
    boolean isHoist();
}
