package DiscordAPI.objects;

import DiscordAPI.IPrivateBot;
import DiscordAPI.objects.Interfaces.IRole;
import DiscordAPI.utils.DiscordUtils;
import ideaeclipse.JsonUtilities.Json;
import ideaeclipse.JsonUtilities.Parser;

/**
 * This class is used for storing and parsing Role data
 *
 * @author Ideaeclipse
 * @see DiscordAPI.objects.Payloads.DRole
 */
class Role implements IRole {
    private final Long id;
    private final String name;
    private final Integer colourCode;
    private final Integer position;
    private final Long permission;

    /**
     * @param id         id of role
     * @param name       name of role
     * @param colourCode colourHash
     * @param position   position in hierarchy
     * @param permission permmission Long
     */
    private Role(final Long id, final String name, final Integer colourCode, final Integer position, final Long permission) {
        this.id = id;
        this.name = name;
        this.colourCode = colourCode;
        this.position = position;
        this.permission = permission;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Integer getColourCode() {
        return colourCode;
    }

    public Integer getPosition() {
        return position;
    }

    public Long getPermission() {
        return permission;
    }

    @Override
    public String toString() {
        return "{Role} Id: " + id + " Name: " + name + " Colour: " + colourCode + " Position: " + position + " Permission: " + permission;
    }

    /**
     * Parses Role data
     * logic() must be called
     *
     * @author Ideaeclipse
     */
    static class RoleP {
        private final IPrivateBot bot;
        private final Json object;
        private IRole role;
        private Long id;

        /**
         * @param object role Object
         */
        RoleP(final IPrivateBot bot, final Json object) {
            this.bot = bot;
            this.object = object;
        }

        RoleP(final IPrivateBot bot, final Long id) {
            this.bot = bot;
            this.object = null;
            this.id = id;
        }

        RoleP logic() {
            if (object != null) {
                Payloads.DRole r = ParserObjects.convertToPayload(object, Payloads.DRole.class);
                role = new Role(r.id, r.name, r.color, r.position, r.permissions);
            }else {
                role = DiscordUtils.Search.ROLES(bot.getRoles(), id);
            }
            return this;
        }

        IRole getRole() {
            return role;
        }
    }
}
