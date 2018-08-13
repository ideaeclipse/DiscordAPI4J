package DiscordAPI.objects;

import DiscordAPI.utils.DiscordUtils;
import org.json.simple.JSONObject;

public class Role {
    private final Long id;
    private final String name;
    private final Integer colourCode;
    private final Integer position;
    private final Long permission;

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

    static class RoleP {
        private final JSONObject object;
        private Role role;

        RoleP(final JSONObject object) {
            this.object = object;
        }

        RoleP logic() {
            Payloads.DRole r = Parser.convertToJSON(object, Payloads.DRole.class);
            role = new Role(r.id, r.name, r.color, r.position, r.permissions);
            return this;
        }

        Role getRole() {
            return role;
        }
    }
}
