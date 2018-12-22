package ideaeclipse.DiscordAPINEW.bot;

import com.neovisionaries.ws.client.WebSocketException;
import ideaeclipse.DiscordAPINEW.bot.objects.channel.IChannel;
import ideaeclipse.DiscordAPINEW.bot.objects.channel.regularChannels.CreateChannel;
import ideaeclipse.DiscordAPINEW.bot.objects.channel.regularChannels.DeleteChannel;
import ideaeclipse.DiscordAPINEW.bot.objects.message.MessageCreate;
import ideaeclipse.DiscordAPINEW.bot.objects.role.DeleteRole;
import ideaeclipse.DiscordAPINEW.bot.objects.role.IRole;
import ideaeclipse.DiscordAPINEW.bot.objects.role.CreateRole;
import ideaeclipse.DiscordAPINEW.bot.objects.role.UpdateRole;
import ideaeclipse.DiscordAPINEW.bot.objects.user.DeleteDiscordUser;
import ideaeclipse.DiscordAPINEW.bot.objects.user.IDiscordUser;
import ideaeclipse.DiscordAPINEW.bot.objects.user.CreateDiscordUser;
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
 * TODO: update util so it doesn't use {@link ideaeclipse.DiscordAPINEW.bot.objects.IDiscordAction} and passes the json in a constructor. also make {@link ideaeclipse.DiscordAPINEW.utils.CheckResponse} return the new instance of the object
 * TODO: Leaving a running copy on the server and wait for disconnection. Make sure the things get printed out before compilation
 * TODO: allow for deletions to pass the IRole object before deleting it from the list.
 * TODO: Allow for presence change for bot.
 * TODO: Updated event names. Make updated role/user/channel a seperate event. Make it so that each is in a more standard format, consider a generic interface
 * TODO: format loading.
 * TODO: IPublicBot
 * TODO: Voice channels
 * TODO: implement custom terminal
 * TODO: add custom logger
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
        this.manager.registerListener(new TestListener(this));
        System.out.println("Loading roles");
        for (Json json : new JsonArray((String) rateLimitRecorder.queue(new RateLimitRecorder.QueueHandler.HttpEvent(RateLimitRecorder.QueueHandler.RequestTypes.get, "guilds/" + serverId + "/roles")))) {
            CreateRole role = new CreateRole();
            Util.check(role, json);
            System.out.println(role.getRole());
            roles.put(role.getRole().getId(), role.getRole());
        }
        System.out.println("Roles loaded");
        System.out.println("Loading users");
        for (Json json : new JsonArray((String) rateLimitRecorder.queue(new RateLimitRecorder.QueueHandler.HttpEvent(RateLimitRecorder.QueueHandler.RequestTypes.get, "guilds/" + serverId + "/members" + "?limit=1000")))) {
            CreateDiscordUser user = new CreateDiscordUser(roles);
            Util.check(user, json);
            System.out.println(user.getUser());
            users.put(user.getUser().getId(), user.getUser());
        }
        System.out.println("Users loaded");
        System.out.println("Loading channels");
        for (Json json : new JsonArray((String) rateLimitRecorder.queue(new RateLimitRecorder.QueueHandler.HttpEvent(RateLimitRecorder.QueueHandler.RequestTypes.get, "guilds/" + serverId + "/channels")))) {
            CreateChannel channel = new CreateChannel();
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

    /**
     * Used to test that the events get called when they happen.
     */
    public static class TestListener implements Listener {
        private final IPrivateBot bot;

        public TestListener(final IPrivateBot bot) {
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
        public void onRoleCreate(CreateRole role){
            System.out.println("Role Create: " + role.getRole().getName());
        }
        @EventHandler
        public void onRoleUpdate(UpdateRole role) {
            System.out.println("Role Updated: " + role.getRole().getName());
        }

        @EventHandler
        public void deleteRole(DeleteRole role) {
            System.out.println("Role delete");
        }

        @EventHandler
        public void onChannelCreate(CreateChannel channel) {
            System.out.println("Channel loaded: " + channel.getChannel().getName());
        }

        @EventHandler
        public void deleteChannel(DeleteChannel channel) {
            System.out.println("Channel deleted");
        }

        @EventHandler
        public void onUserJoin(CreateDiscordUser user) {
            System.out.println("User added/updated: " + user.getUser());
        }

        @EventHandler
        public void deleteUser(DeleteDiscordUser user) {
            System.out.println("User deleted");
        }
    }
}
