package ideaeclipse.DiscordAPI.webSocket;

import com.neovisionaries.ws.client.WebSocket;
import com.neovisionaries.ws.client.WebSocketAdapter;
import com.neovisionaries.ws.client.WebSocketException;
import com.neovisionaries.ws.client.WebSocketFactory;
import ideaeclipse.AsyncUtility.Async;
import ideaeclipse.DiscordAPI.bot.IDiscordBot;
import ideaeclipse.DiscordAPI.bot.objects.channel.IChannel;
import ideaeclipse.DiscordAPI.bot.objects.channel.directMessage.CreateDMChannel;
import ideaeclipse.DiscordAPI.bot.objects.channel.regularChannels.CreateChannel;
import ideaeclipse.DiscordAPI.bot.objects.channel.regularChannels.DeleteChannel;
import ideaeclipse.DiscordAPI.bot.objects.channel.regularChannels.UpdateChannel;
import ideaeclipse.DiscordAPI.bot.objects.message.MessageCreate;
import ideaeclipse.DiscordAPI.bot.objects.presence.PresenceUpdate;
import ideaeclipse.DiscordAPI.bot.objects.reaction.AddReaction;
import ideaeclipse.DiscordAPI.bot.objects.reaction.IReaction;
import ideaeclipse.DiscordAPI.bot.objects.reaction.RemoveReaction;
import ideaeclipse.DiscordAPI.bot.objects.role.CreateRole;
import ideaeclipse.DiscordAPI.bot.objects.role.DeleteRole;
import ideaeclipse.DiscordAPI.bot.objects.role.IRole;
import ideaeclipse.DiscordAPI.bot.objects.role.UpdateRole;
import ideaeclipse.DiscordAPI.bot.objects.user.DeleteDiscordUser;
import ideaeclipse.DiscordAPI.bot.objects.user.CreateDiscordUser;
import ideaeclipse.DiscordAPI.bot.objects.user.IDiscordUser;
import ideaeclipse.DiscordAPI.bot.objects.user.UpdateDiscordUser;
import ideaeclipse.DiscordAPI.utils.CheckResponeType;
import ideaeclipse.DiscordAPI.utils.CheckResponse;
import ideaeclipse.DiscordAPI.utils.Util;
import ideaeclipse.DiscordAPI.webSocket.rateLimit.WebSocketEvent;
import ideaeclipse.JsonUtilities.Builder;
import ideaeclipse.JsonUtilities.Json;
import ideaeclipse.customLogger.CustomLogger;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Used to handle all websocket data and execute accordingly
 *
 * @author Ideaeclipse
 */
public final class Wss extends WebSocketFactory {
    private static final String WEBSOCKET = "wss://gateway.discord.gg/?v=6&encoding=json";
    private final IDiscordBot bot;
    private final WebSocket socket;

    public Wss(final IDiscordBot bot, final String token) throws IOException, WebSocketException {
        this.bot = bot;
        socket = this.setConnectionTimeout(5000)
                .createSocket(WEBSOCKET)
                .addListener(new WebSocketAdapter() {
                    @Override
                    public void onTextMessage(WebSocket websocket, String text) {
                        Json message = new Json(text);
                        final CustomLogger logger = new CustomLogger(this.getClass(), bot.getLoggerManager());
                        final TextOpCodes op = TextOpCodes.values()[Integer.parseInt(String.valueOf(message.get("op")))];
                        final String s = String.valueOf(message.get("s"));
                        final String t = String.valueOf(message.get("t"));
                        final Json d = new Json(!String.valueOf(message.get("d")).equals("null") ? String.valueOf(message.get("d")) : "{}");
                        switch (op) {
                            case Dispatch:
                                List<DispatchType> filtered = Arrays.stream(DispatchType.values()).filter(o -> o.name().equals(t.toUpperCase())).collect(Collectors.toList());
                                if (!filtered.isEmpty()) {
                                    switch (filtered.get(0)) {
                                        case MESSAGE_CREATE:
                                            Util.checkConstructor(bot.getEventManager(), MessageCreate.class, d, bot);
                                            break;
                                        case PRESENCE_UPDATE:
                                            Util.checkConstructor(bot.getEventManager(), PresenceUpdate.class, d, bot);
                                            break;
                                        case CHANNEL_CREATE:
                                            CheckResponse<CreateChannel> channelCheckResponse = Util.checkConstructor(bot.getEventManager(), CreateChannel.class, d, bot);
                                            if (!channelCheckResponse.getType().equals(CheckResponeType.EXECUTED)) {
                                                CreateDMChannel channel = Util.checkConstructor(bot.getEventManager(), CreateDMChannel.class, d, bot).getObject();
                                                IChannel channel1 = channel.getChannel();
                                                bot.getChannels().put(channel1.getId(), channel1.getName(), channel1);
                                            } else {
                                                IChannel channel = channelCheckResponse.getObject().getChannel();
                                                bot.getChannels().put(channel.getId(), channel.getName(), channel);
                                            }
                                            break;
                                        case CHANNEL_UPDATE:
                                            CheckResponse<UpdateChannel> updateChannelCheckResponse = Util.checkConstructor(bot.getEventManager(), UpdateChannel.class, d, bot);
                                            if (updateChannelCheckResponse.getType().equals(CheckResponeType.EXECUTED)) {
                                                IChannel channel = updateChannelCheckResponse.getObject().getChannel();
                                                bot.getChannels().removeByK1(channel.getId());
                                                bot.getChannels().put(channel.getId(), channel.getName(), channel);
                                            }
                                            break;
                                        case CHANNEL_DELETE:
                                            Util.checkConstructor(bot.getEventManager(), DeleteChannel.class, d, bot);
                                            break;
                                        case GUILD_ROLE_CREATE:
                                            CheckResponse<CreateRole> createRoleCheckResponse = Util.checkConstructor(bot.getEventManager(), CreateRole.class, d, bot);
                                            if (createRoleCheckResponse.getType().equals(CheckResponeType.EXECUTED)) {
                                                IRole role = createRoleCheckResponse.getObject().getRole();
                                                bot.getRoles().put(role.getId(), role.getName(), role);
                                            }
                                            break;
                                        case GUILD_ROLE_UPDATE:
                                            CheckResponse<UpdateRole> updateRoleCheckResponse = Util.checkConstructor(bot.getEventManager(), UpdateRole.class, d, bot);
                                            if (updateRoleCheckResponse.getType().equals(CheckResponeType.EXECUTED)) {
                                                IRole role = updateRoleCheckResponse.getObject().getRole();
                                                bot.getRoles().removeByK1(role.getId());
                                                bot.getRoles().put(role.getId(), role.getName(), role);
                                                for (Map.Entry<Long, IDiscordUser> entry : bot.getUsers().getK1VMap().entrySet()) {
                                                    IDiscordUser user = entry.getValue();
                                                    if (user.getRoles().containsKey1(role.getId())) {
                                                        user.getRoles().removeByK1(role.getId());
                                                        user.getRoles().put(role.getId(), role.getName(), role);
                                                    }
                                                }
                                            }
                                            break;
                                        case GUILD_ROLE_DELETE:
                                            Util.checkConstructor(bot.getEventManager(), DeleteRole.class, d, bot);
                                            break;
                                        case GUILD_MEMBER_ADD:
                                            CheckResponse<CreateDiscordUser> createDiscordUserCheckResponse = Util.checkConstructor(bot.getEventManager(), CreateDiscordUser.class, d, bot);
                                            if (createDiscordUserCheckResponse.getType().equals(CheckResponeType.EXECUTED)) {
                                                IDiscordUser user = createDiscordUserCheckResponse.getObject().getUser();
                                                bot.getUsers().put(user.getId(), user.getUsername(), user);
                                            }
                                            break;
                                        case GUILD_MEMBER_UPDATE:
                                            CheckResponse<UpdateDiscordUser> updateDiscordUserCheckResponse = Util.checkConstructor(bot.getEventManager(), UpdateDiscordUser.class, d, bot);
                                            if (updateDiscordUserCheckResponse.getType().equals(CheckResponeType.EXECUTED)) {
                                                IDiscordUser user = updateDiscordUserCheckResponse.getObject().getUser();
                                                bot.getUsers().put(user.getId(), user.getUsername(), user);
                                            }
                                            break;
                                        case GUILD_MEMBER_REMOVE:
                                            Util.checkConstructor(bot.getEventManager(), DeleteDiscordUser.class, d, bot);
                                            break;
                                        case MESSAGE_REACTION_ADD:
                                            AddReaction addReaction = Util.checkConstructor(bot.getEventManager(), AddReaction.class, d, bot).getObject();
                                            IReaction reaction = addReaction.getReaction();
                                            reaction.getChannel().getMessageHistory().get(reaction.getMessage().getId()).addReaction(reaction.getCode());
                                            break;
                                        case MESSAGE_REACTION_REMOVE:
                                            Util.checkConstructor(bot.getEventManager(), RemoveReaction.class, d, bot);
                                            break;
                                    }
                                }
                                break;
                            case Heartbeat:
                                break;
                            case Identify:
                                break;
                            case Status_Update:
                                break;
                            case Voice_State_Update:
                                break;
                            case Voice_Server_Ping:
                                break;
                            case Resume:
                                logger.error("Resume: " + message);
                                System.exit(-1);
                                break;
                            case Reconnect:
                                logger.error("Reconnect: " + message);
                                System.exit(-1);
                                break;
                            case Request_Guild_Members:
                                break;
                            case Invalid_Session:
                                break;
                            case Hello:
                                final long hbi = Long.parseUnsignedLong(String.valueOf(d.get("heartbeat_interval")));
                                String payload = "{\"op\":2,\"d\":{\"shards\":[0,1],\"compress\":true,\"presence\":{\"game\":{\"name\":\"!help for commands\",\"type\":0},\"afk\":false,\"status\":\"online\"},\"large_threshold\":250,\"properties\":{\"$device\":\"D4J\",\"$os\":\"Windows 10\",\"$browser\":\"D4J\"},\"token\":\"?token\"}}";
                                Json welcomePayload = new Json(payload.replace("?token", token));
                                sendText(welcomePayload);
                                logger.info("Starting heartbeat with an interval of: " + hbi);
                                Async.addJob(o -> {
                                    CustomLogger logger1 = new CustomLogger(this.getClass(), bot.getLoggerManager());
                                    logger1.debug("Sending HeartBeat Payload");
                                    Json object = Builder.buildPayload(ideaeclipse.DiscordAPI.webSocket.TextOpCodes.Heartbeat.ordinal(), 251);
                                    sendText(object);
                                    return Optional.empty();
                                }, hbi);
                                logger.info("Hello event finished, bot online");
                                break;
                            case HeartBeat_ACK:
                                logger.debug("HeartBeat_ACK received");
                                break;
                        }
                    }
                }).connect();
    }

    /**
     * Send text over the websocket
     * @param json json to send
     */
    private void sendText(final Json json) {
        this.bot.getRateLimitRecorder().queue(new WebSocketEvent(socket, json));
    }

    /**
     * @return websocket instance
     */
    public WebSocket getSocket() {
        return socket;
    }
}
