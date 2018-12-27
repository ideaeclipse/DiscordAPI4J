package ideaeclipse.DiscordAPINEW.bot;

import com.neovisionaries.ws.client.WebSocket;
import com.neovisionaries.ws.client.WebSocketException;
import ideaeclipse.DiscordAPINEW.bot.objects.channel.IChannel;
import ideaeclipse.DiscordAPINEW.bot.objects.channel.directMessage.CreateDMChannel;
import ideaeclipse.DiscordAPINEW.bot.objects.channel.regularChannels.CreateChannel;
import ideaeclipse.DiscordAPINEW.bot.objects.channel.regularChannels.DeleteChannel;
import ideaeclipse.DiscordAPINEW.bot.objects.channel.regularChannels.UpdateChannel;
import ideaeclipse.DiscordAPINEW.bot.objects.message.IMessage;
import ideaeclipse.DiscordAPINEW.bot.objects.message.MessageCreate;
import ideaeclipse.DiscordAPINEW.bot.objects.presence.PresenceUpdate;
import ideaeclipse.DiscordAPINEW.bot.objects.presence.UserStatus;
import ideaeclipse.DiscordAPINEW.bot.objects.presence.game.GameType;
import ideaeclipse.DiscordAPINEW.bot.objects.reaction.AddReaction;
import ideaeclipse.DiscordAPINEW.bot.objects.reaction.IReaction;
import ideaeclipse.DiscordAPINEW.bot.objects.reaction.RemoveReaction;
import ideaeclipse.DiscordAPINEW.bot.objects.role.CreateRole;
import ideaeclipse.DiscordAPINEW.bot.objects.role.DeleteRole;
import ideaeclipse.DiscordAPINEW.bot.objects.role.IRole;
import ideaeclipse.DiscordAPINEW.bot.objects.role.UpdateRole;
import ideaeclipse.DiscordAPINEW.bot.objects.user.CreateDiscordUser;
import ideaeclipse.DiscordAPINEW.bot.objects.user.DeleteDiscordUser;
import ideaeclipse.DiscordAPINEW.bot.objects.user.IDiscordUser;
import ideaeclipse.DiscordAPINEW.bot.objects.user.UpdateDiscordUser;
import ideaeclipse.DiscordAPINEW.utils.MultiKeyMap;
import ideaeclipse.DiscordAPINEW.utils.Util;
import ideaeclipse.DiscordAPINEW.webSocket.RateLimitRecorder;
import ideaeclipse.DiscordAPINEW.webSocket.TextOpCodes;
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
 * TODO: Multi Key maps
 * TODO: Leaving a running copy on the server and wait for disconnection. Make sure the things get printed out before compilation
 * TODO: Allow for querying of dm channels
 * TODO: reactions
 * TODO: format loading.
 * TODO: IPublicBot
 * TODO: Voice channels
 * TODO: implement custom terminal
 * TODO: add custom logger
 */
public class DiscordBot implements IPrivateBot {
    private final EventManager manager;
    private final MultiKeyMap<Long, String, IRole> roles = new MultiKeyMap<>();
    private final MultiKeyMap<Long, String, IDiscordUser> users = new MultiKeyMap<>();
    private final MultiKeyMap<Long, String, IChannel> channels = new MultiKeyMap<>();
    private final Map<String, IReaction> reactions = new HashMap<>();
    private WebSocket socket;

    public DiscordBot(final String token, final String serverId) {
        Util.requests = new Util.HttpRequests(token);
        Util.guildId = Long.parseUnsignedLong(String.valueOf(serverId));
        this.manager = new EventManager();
        this.manager.registerListener(new TestListener(this));
        System.out.println("Loading roles");
        for (Json json : new JsonArray((String) rateLimitRecorder.queue(new RateLimitRecorder.QueueHandler.HttpEvent(RateLimitRecorder.QueueHandler.RequestTypes.get, "guilds/" + serverId + "/roles")))) {
            CreateRole role = Util.checkConstructor(CreateRole.class, json, this).getObject();
            System.out.println(role.getRole());
            roles.put(role.getRole().getId(), role.getRole().getName(), role.getRole());
        }
        System.out.println("Roles loaded");
        System.out.println("Loading users");
        for (Json json : new JsonArray((String) rateLimitRecorder.queue(new RateLimitRecorder.QueueHandler.HttpEvent(RateLimitRecorder.QueueHandler.RequestTypes.get, "guilds/" + serverId + "/members" + "?limit=1000")))) {
            CreateDiscordUser user = Util.checkConstructor(CreateDiscordUser.class, json, this).getObject();
            System.out.println(user.getUser());
            users.put(user.getUser().getId(), user.getUser().getUsername(), user.getUser());
        }
        System.out.println("Users loaded");
        System.out.println("Loading channels");
        for (Json json : new JsonArray((String) rateLimitRecorder.queue(new RateLimitRecorder.QueueHandler.HttpEvent(RateLimitRecorder.QueueHandler.RequestTypes.get, "guilds/" + serverId + "/channels")))) {
            CreateChannel channel = Util.checkConstructor(CreateChannel.class, json, this).getObject();
            System.out.println(channel.getChannel());
            channels.put(channel.getChannel().getId(), channel.getChannel().getName(), channel.getChannel());
        }
        System.out.println("Channels loaded");

        try {
            this.socket = new Wss(this, token).getSocket();
        } catch (IOException | WebSocketException e) {
            e.printStackTrace();
        }
    }

    @Override
    public EventManager getManager() {
        return this.manager;
    }

    @Override
    public MultiKeyMap<Long, String, IDiscordUser> getUsers() {
        return this.users;
    }

    @Override
    public MultiKeyMap<Long, String, IChannel> getChannels() {
        return this.channels;
    }

    @Override
    public MultiKeyMap<Long, String, IRole> getRoles() {
        return this.roles;
    }

    @Override
    public Map<String, IReaction> getReactions() {
        return this.reactions;
    }

    @Override
    public void setStatus(final GameType type, final String message, final UserStatus status) {
        String payload = "{\"op\":3,\"d\":{\"since\":0,\"game\":{\"name\":\"?name\",\"type\":?type},\"afk\":false,\"status\":\"?status\"}}";
        payload = payload.replace("?name", message).replace("?type", String.valueOf(type.ordinal())).replace("?status", status.name());
        System.out.println(new Json(payload) + " " + TextOpCodes.Status_Update.ordinal());
        rateLimitRecorder.queue(new RateLimitRecorder.QueueHandler.WebSocketEvent(this.socket, new Json(payload)));
    }

    @Override
    public IChannel createDmChannel(final IDiscordUser user) {
        String info = "{\"recipient_id\":?id}";
        info = info.replace("?id", String.valueOf(user.getId()));
        CreateDMChannel channel = Util.checkConstructor(CreateDMChannel.class, new Json(String.valueOf(rateLimitRecorder.queue(new RateLimitRecorder.QueueHandler.HttpEvent(RateLimitRecorder.QueueHandler.RequestTypes.sendJson, "/users/@me/channels", new Json(info))))), this).getObject();
        this.getChannels().put(channel.getChannel().getId(), channel.getChannel().getName(), channel.getChannel());
        return channel.getChannel();
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
            System.out.println(create.getMessage());
            if (!create.getMessage().getUser().getUsername().equals("Testing")) {
                IMessage message = create.getMessage();
                //create.getMessage().getChannel().sendMessage("pong");
                //this.bot.createDmChannel(create.getMessage().getUser()).sendMessage("Why you messaging in my channel");
                //this.bot.setStatus(GameType.playing, "with numbers", UserStatus.online);
                // create.getMessage().getUser().addRole(bot.getRoles().get(Long.parseUnsignedLong("525059526345490442")));
                //create.getMessage().getChannel().uploadFile("C:\\Users\\myles\\Desktop\\memes.pdf");
            }
        }

        @EventHandler
        public void presence(PresenceUpdate update) {
            System.out.println(update.getPresence());
        }

        @EventHandler
        public void onRoleCreate(CreateRole role) {
            System.out.println("Role added: " + role.getRole().getName());
        }

        @EventHandler
        public void onRoleUpdate(UpdateRole role) {
            System.out.println("Role Updated: " + role.getRole().getName());
        }

        @EventHandler
        public void deleteRole(DeleteRole role) {
            System.out.println("Role delete: " + role.getRole().getName());
        }

        @EventHandler
        public void onChannelCreate(CreateChannel channel) {
            System.out.println("Channel added: " + channel.getChannel().getName());
        }

        @EventHandler
        public void onChannelUpdate(UpdateChannel channel) {
            System.out.println("Channel updated: " + channel.getChannel().getName());
        }

        @EventHandler
        public void deleteChannel(DeleteChannel channel) {
            System.out.println("Channel deleted: " + channel.getChannel().getName());
        }

        @EventHandler
        public void onUserJoin(CreateDiscordUser user) {
            System.out.println("User added: " + user.getUser());
        }

        @EventHandler
        public void onUserUpdate(UpdateDiscordUser user) {
            System.out.println("User updated: " + user.getUser());
        }

        @EventHandler
        public void deleteUser(DeleteDiscordUser user) {
            System.out.println("User deleted: " + user.getUser().getUsername());
        }

        @EventHandler
        public void addReaction(AddReaction add) {
            System.out.println("Reaction added: " + add.getReaction());
        }

        @EventHandler
        public void removeReaction(RemoveReaction remove) {
            System.out.println("Reaction removed: " + remove.getReaction());
        }
    }
}
