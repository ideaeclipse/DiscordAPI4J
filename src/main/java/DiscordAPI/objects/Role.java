package DiscordAPI.objects;

import org.json.simple.JSONObject;

/**
 * This class is used for storing and parsing Role data
 *
 * @author Ideaeclipse
 * @see DiscordAPI.objects.Payloads.DRole
 */
public class Role {
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

    /**
     * Parses Role data
     * logic() must be called
     *
     * @author Ideaeclipse
     */
    static class RoleP {
        private final JSONObject object;
        private Role role;

        /**
         * @param object role Object
         */
        RoleP(final JSONObject object) {
            this.object = object;
        }

        RoleP logic() {
            Payloads.DRole r = Parser.convertToPayload(object, Payloads.DRole.class);
            role = new Role(r.id, r.name, r.color, r.position, r.permissions);
            return this;
        }

        Role getRole() {
            return role;
        }
    }
}
