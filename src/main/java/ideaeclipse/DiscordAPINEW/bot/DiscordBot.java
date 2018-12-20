package ideaeclipse.DiscordAPINEW.bot;

import com.neovisionaries.ws.client.WebSocketException;
import ideaeclipse.DiscordAPINEW.bot.objects.channel.IChannel;
import ideaeclipse.DiscordAPINEW.bot.objects.channel.regularChannels.DeleteChannel;
import ideaeclipse.DiscordAPINEW.bot.objects.channel.regularChannels.LoadChannel;
import ideaeclipse.DiscordAPINEW.bot.objects.message.MessageCreate;
import ideaeclipse.DiscordAPINEW.bot.objects.role.DeleteRole;
import ideaeclipse.DiscordAPINEW.bot.objects.role.IRole;
import ideaeclipse.DiscordAPINEW.bot.objects.role.LoadRole;
import ideaeclipse.DiscordAPINEW.bot.objects.user.DeleteDiscordUser;
import ideaeclipse.DiscordAPINEW.bot.objects.user.IDiscordUser;
import ideaeclipse.DiscordAPINEW.bot.objects.user.LoadUser;
import ideaeclipse.DiscordAPINEW.utils.Util;
import ideaeclipse.DiscordAPINEW.webSocket.RateLimitRecorder;
import ideaeclipse.DiscordAPINEW.webSocket.Wss;
import ideaeclipse.JsonUtilities.Json;
import ideaeclipse.JsonUtilities.JsonArray;
import ideaeclipse.reflectionListener.EventManager;
import ideaeclipse.reflectionListener.Listener;
import ideaeclipse.reflectionListener.annotations.EventHandler;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static ideaeclipse.DiscordAPINEW.utils.Util.rateLimitRecorder;

/**
 * TODO: add json examples for each type of dispatch event in their interface class.
 * TODO: implement custom terminal
 * TODO: add custom logger
 * TODO: format loading.
 * TODO: comments
 * TODO: make creating channel/role/user a listenable event
 * TODO: all for adding groups on users.
 */
public class DiscordBot implements IPrivateBot {
    private final EventManager manager;
    private final Map<Long, IRole> roles = new HashMap<>();
    private final Map<Long, IDiscordUser> users = new HashMap<>();
    private final Map<Long, IChannel> channels = new HashMap<>();

    public DiscordBot(final String token, final String serverId) {
        Util.requests = new Util.HttpRequests(token);
        Util.guildId = Long.parseUnsignedLong(String.valueOf(serverId));
        this.manager = new EventManager();
        this.manager.registerListener(new MemeListener(this));
        System.out.println("Loading roles");
        for (Json json : new JsonArray((String) rateLimitRecorder.queue(new RateLimitRecorder.QueueHandler.HttpEvent(RateLimitRecorder.QueueHandler.RequestTypes.get, "guilds/" + serverId + "/roles")))) {
            LoadRole role = new LoadRole();
            Util.check(role, json);
            System.out.println(role.getRole());
            roles.put(role.getRole().getId(), role.getRole());
        }
        System.out.println("Roles loaded");
        System.out.println("Loading users");
        for (Json json : new JsonArray((String) rateLimitRecorder.queue(new RateLimitRecorder.QueueHandler.HttpEvent(RateLimitRecorder.QueueHandler.RequestTypes.get, "guilds/" + serverId + "/members" + "?limit=1000")))) {
            LoadUser user = new LoadUser(roles);
            Util.check(user, json);
            System.out.println(user.getUser());
            users.put(user.getUser().getId(), user.getUser());
        }
        System.out.println("Users loaded");
        System.out.println("Loading channels");
        for (Json json : new JsonArray((String) rateLimitRecorder.queue(new RateLimitRecorder.QueueHandler.HttpEvent(RateLimitRecorder.QueueHandler.RequestTypes.get, "guilds/" + serverId + "/channels")))) {
            LoadChannel channel = new LoadChannel();
            Util.check(channel, json);
            System.out.println(channel.getChannel());
            channels.put(channel.getChannel().getId(), channel.getChannel());
        }
        System.out.println("Channels loaded");

        try {
            new Wss(this, token);
        } catch (IOException | WebSocketException e) {
            e.printStackTrace();
        }
    }

    @Override
    public EventManager getManager() {
        return this.manager;
    }

    @Override
    public Map<Long, IDiscordUser> getUsers() {
        return this.users;
    }

    @Override
    public Map<Long, IChannel> getChannels() {
        return this.channels;
    }

    @Override
    public Map<Long, IRole> getRoles() {
        return this.roles;
    }

    public static class MemeListener implements Listener {
        private final IPrivateBot bot;

        public MemeListener(final IPrivateBot bot) {
            this.bot = bot;
        }

        @EventHandler
        public void messageTest(MessageCreate create) {
            if (!create.getMessage().getUser().getUsername().equals("Testing")) {
                create.getMessage().getChannel().sendMessage("pong");
                // create.getMessage().getUser().addRole(bot.getRoles().get(Long.parseUnsignedLong("525059526345490442")));
                //create.getMessage().getChannel().uploadFile("C:\\Users\\myles\\Desktop\\memes.pdf");
            }
        }

        @EventHandler
        public void onRoleCreate(LoadRole role) {
            System.out.println("Role Loaded: " + role.getRole().getName());
        }

        @EventHandler
        public void deleteRole(DeleteRole role) {
            System.out.println("Role delete");
        }

        @EventHandler
        public void onChannelCreate(LoadChannel channel) {
            System.out.println("Channel loaded: " + channel.getChannel().getName());
        }

        @EventHandler
        public void deleteChannel(DeleteChannel channel) {
            System.out.println("Channel deleted");
        }
        @EventHandler
        public void onUserJoin(LoadUser user){
            System.out.println("User added/updated: " + user.getUser());
        }
        @EventHandler
        public void deleteUser(DeleteDiscordUser user){
            System.out.println("User deleted");
        }
    }
}
