package DiscordAPI.objects;

import DiscordAPI.IDiscordBot;
import DiscordAPI.utils.Json;

import java.util.ArrayList;
import java.util.List;


public class User {
    private final String nick;
    private final String joined_at;
    private final List<Role> roles;
    private final Boolean deaf;
    private final Boolean mute;
    private final String session_id;
    private final DiscordUser user;
    private final Game game;
    private final String status;

    private User(final String nick, final String joined_at, final List<Role> roles, final Boolean deaf, final Boolean mute, final String session_id, final DiscordUser user, final String status, final Game game) {
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

    public List<Role> getRoles() {
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

    public DiscordUser getDiscordUser() {
        return user;
    }

    public String getStatus() {
        return status;
    }

    public Game getGame() {
        return game;
    }

    @Override
    public String toString() {
        return "{User} Nick: " + nick + " Joined_At: " + joined_at + " Roles: " + roles + " Deaf: " + deaf + " Mute: " + mute + " Session: " + session_id + " DiscordUser: " + user + " Status: " + status + " Game: " + game;
    }

    public static class ServerUniqueUserP {
        private final IDiscordBot bot;
        private final Json payload;
        private User serverUniqueUser;

        ServerUniqueUserP(final IDiscordBot bot, final Json payload) {
            this.bot = bot;
            this.payload = payload;
        }

        ServerUniqueUserP logic() {
            Payloads.DServerUniqueUser u = Parser.convertToPayload(payload, Payloads.DServerUniqueUser.class);
            List<Role> roles = new ArrayList<>();
            if (u.roles != null) {
                for (Long s : u.roles) {
                    roles.add(new Role.RoleP(bot, s).logic().getRole());
                }
            }
            serverUniqueUser = new User(u.nick, u.joined_at, roles, u.deaf, u.mute, u.session_id, u.user, u.status, u.game);
            return this;
        }

        public User getServerUniqueUser() {
            return serverUniqueUser;
        }
    }
}
