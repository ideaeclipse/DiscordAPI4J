package ideaeclipse.DiscordAPI.bot.objects.role;

/**
 * Object use to map role json data to a class
 *
 * @author Ideaeclipse
 * @see IRole
 * @see CreateRole
 * @see UpdateRole
 * @see DeleteRole
 */
public final class Role implements IRole {
    private final int colour;
    private final boolean managed;
    private final int permission;
    private final String name;
    private final boolean mentionable;
    private final int position;
    private final long id;
    private final boolean hoist;

    /**
     * @param colour      colour code of role
     * @param managed     managed status of role
     * @param permission  permission of role
     * @param name        name of role
     * @param mentionable mentionable status of the role
     * @param position    position of the role
     * @param id          unique identifier of the role
     * @param hoist       hoist status of the role
     */
    Role(final int colour, final boolean managed, final int permission, final String name, final boolean mentionable, final int position, final long id, final boolean hoist) {
        this.colour = colour;
        this.managed = managed;
        this.permission = permission;
        this.name = name;
        this.mentionable = mentionable;
        this.position = position;
        this.id = id;
        this.hoist = hoist;
    }

    @Override
    public int getColour() {
        return this.colour;
    }

    @Override
    public boolean isManaged() {
        return this.managed;
    }

    @Override
    public int getPermissionValue() {
        return this.permission;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public String getMention() {
        if (mentionable)
            return "<@" + this.id + ">";
        return this.name;
    }

    @Override
    public boolean isMentionable() {
        return this.mentionable;
    }

    @Override
    public int getPosition() {
        return this.position;
    }

    @Override
    public long getId() {
        return this.id;
    }

    @Override
    public boolean isHoist() {
        return this.hoist;
    }

    @Override
    public String toString() {
        return "{Role} name: " + this.name + " id: " + this.id;
    }
}
