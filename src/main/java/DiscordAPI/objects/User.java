package DiscordAPI.objects;

import DiscordAPI.IPrivateBot;
import DiscordAPI.objects.Interfaces.IGame;
import DiscordAPI.objects.Interfaces.IRole;
import DiscordAPI.objects.Interfaces.IUser;
import DiscordAPI.utils.Json;

import java.util.ArrayList;
import java.util.List;


class User implements IUser {
    private final String nick;
    private final String joined_at;
    private final List<IRole> roles;
    private final Boolean deaf;
    private final Boolean mute;
    private final String session_id;
    private final IDiscordUser user;
    private final IGame game;
    private final String status;

    private User(final String nick, final String joined_at, final List<IRole> roles, final Boolean deaf, final Boolean mute, final String session_id, final DiscordUser user, final String status, final IGame game) {
        this.nick = nick;
        this.joined_at = joined_at;
        this.roles = roles;
        this.deaf = deaf;
        this.mute = mute;
        this.session_id = session_id;
        this.user = user;
        this.game = game;
        this.status = status;
    }

    public String getNick() {
        return nick;
    }

    public String getJoined_at() {
        return joined_at;
    }

    public List<IRole> getRoles() {
        return roles;
    }

    public Boolean getDeaf() {
        return deaf;
    }

    public Boolean getMute() {
        return mute;
    }

    public String getSession_id() {
        return session_id;
    }

    public IDiscordUser getDiscordUser() {
        return user;
    }

    public String getStatus() {
        return status;
    }

    public IGame getGame() {
        return game;
    }

    @Override
    public String toString() {
        return "{User} Nick: " + nick + " Joined_At: " + joined_at + " Roles: " + roles + " Deaf: " + deaf + " Mute: " + mute + " Session: " + session_id + " DiscordUser: " + user + " Status: " + status + " Game: " + game;
    }

    static class ServerUniqueUserP {
        private final IPrivateBot bot;
        private final Json payload;
        private User serverUniqueUser;

        ServerUniqueUserP(final IPrivateBot bot, final Json payload) {
            this.bot = bot;
            this.payload = payload;
        }

        ServerUniqueUserP logic() {
            Payloads.DServerUniqueUser u = Parser.convertToPayload(payload, Payloads.DServerUniqueUser.class);
            List<IRole> roles = new ArrayList<>();
            if (u.roles != null) {
                for (Long s : u.roles) {
                    roles.add(new Role.RoleP(bot, s).logic().getRole());
                }
            }
            serverUniqueUser = new User(u.nick, u.joined_at, roles, u.deaf, u.mute, u.session_id, u.user, u.status, u.game);
            return this;
        }

        User getServerUniqueUser() {
            return serverUniqueUser;
        }
    }
}
