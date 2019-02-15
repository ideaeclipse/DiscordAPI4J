package ideaeclipse.DiscordAPI.webSocket;

import com.neovisionaries.ws.client.*;
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
    private WebSocket socket;
    private static final String WEBSOCKET = "wss://gateway.discord.gg/?v=6&encoding=json";
    private final IDiscordBot bot;
    private static int reconnectionCount = 0;
    private static boolean gotACK;
    private static boolean redemption;

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
                                            Util.checkConstructor(bot.getListenerManager(), MessageCreate.class, d, bot);
                                            break;
                                        case PRESENCE_UPDATE:
                                            Util.checkConstructor(bot.getListenerManager(), PresenceUpdate.class, d, bot);
                                            break;
                                        case CHANNEL_CREATE:
                                            CheckResponse<CreateChannel> channelCheckResponse = Util.checkConstructor(bot.getListenerManager(), CreateChannel.class, d, bot);
                                            if (!channelCheckResponse.getType().equals(CheckResponeType.EXECUTED)) {
                                                CreateDMChannel channel = Util.checkConstructor(bot.getListenerManager(), CreateDMChannel.class, d, bot).getObject();
                                                IChannel channel1 = channel.getChannel();
                                                bot.getChannels().put(channel1.getId(), channel1.getName(), channel1);

                                            } else {
                                                IChannel channel = channelCheckResponse.getObject().getChannel();
                                                bot.getChannels().put(channel.getId(), channel.getName(), channel);
                                            }
                                            break;
                                        case CHANNEL_UPDATE:
                                            CheckResponse<UpdateChannel> updateChannelCheckResponse = Util.checkConstructor(bot.getListenerManager(), UpdateChannel.class, d, bot);
                                            if (updateChannelCheckResponse.getType().equals(CheckResponeType.EXECUTED)) {
                                                IChannel channel = updateChannelCheckResponse.getObject().getChannel();
                                                bot.getChannels().removeByK1(channel.getId());
                                                bot.getChannels().put(channel.getId(), channel.getName(), channel);
                                            }
                                            break;
                                        case CHANNEL_DELETE:
                                            Util.checkConstructor(bot.getListenerManager(), DeleteChannel.class, d, bot);
                                            break;
                                        case GUILD_ROLE_CREATE:
                                            CheckResponse<CreateRole> createRoleCheckResponse = Util.checkConstructor(bot.getListenerManager(), CreateRole.class, d, bot);
                                            if (createRoleCheckResponse.getType().equals(CheckResponeType.EXECUTED)) {
                                                IRole role = createRoleCheckResponse.getObject().getRole();
                                                bot.getRoles().put(role.getId(), role.getName(), role);
                                            }
                                            break;
                                        case GUILD_ROLE_UPDATE:
                                            CheckResponse<UpdateRole> updateRoleCheckResponse = Util.checkConstructor(bot.getListenerManager(), UpdateRole.class, d, bot);
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
                                            Util.checkConstructor(bot.getListenerManager(), DeleteRole.class, d, bot);
                                            break;
                                        case GUILD_MEMBER_ADD:
                                            CheckResponse<CreateDiscordUser> createDiscordUserCheckResponse = Util.checkConstructor(bot.getListenerManager(), CreateDiscordUser.class, d, bot);
                                            if (createDiscordUserCheckResponse.getType().equals(CheckResponeType.EXECUTED)) {
                                                IDiscordUser user = createDiscordUserCheckResponse.getObject().getUser();
                                                bot.getUsers().put(user.getId(), user.getUsername(), user);
                                            }
                                            break;
                                        case GUILD_MEMBER_UPDATE:
                                            CheckResponse<UpdateDiscordUser> updateDiscordUserCheckResponse = Util.checkConstructor(bot.getListenerManager(), UpdateDiscordUser.class, d, bot);
                                            if (updateDiscordUserCheckResponse.getType().equals(CheckResponeType.EXECUTED)) {
                                                IDiscordUser user = updateDiscordUserCheckResponse.getObject().getUser();
                                                bot.getUsers().put(user.getId(), user.getUsername(), user);
                                            }
                                            break;
                                        case GUILD_MEMBER_REMOVE:
                                            Util.checkConstructor(bot.getListenerManager(), DeleteDiscordUser.class, d, bot);
                                            break;
                                        case MESSAGE_REACTION_ADD:
                                            AddReaction addReaction = Util.checkConstructor(bot.getListenerManager(), AddReaction.class, d, bot).getObject();
                                            IReaction reaction = addReaction.getReaction();
                                            reaction.getChannel().getMessageHistory().get(reaction.getMessage().getId()).addReaction(reaction.getCode());
                                            break;
                                        case MESSAGE_REACTION_REMOVE:
                                            Util.checkConstructor(bot.getListenerManager(), RemoveReaction.class, d, bot);
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
                                break;
                            case Reconnect:
                                logger.error("Reconnect: " + message);
                                break;
                            case Request_Guild_Members:
                                break;
                            case Invalid_Session:
                                break;
                            case Hello:
                                final long hbi = Long.parseUnsignedLong(String.valueOf(d.get("heartbeat_interval")));
                                String payload = "{\"op\":2,\"d\":{\"shards\":[0,1],\"compress\":true,\"presence\":{\"game\":{\"name\":\"!help for commands\",\"type\":0},\"afk\":false,\"status\":\"online\"},\"large_threshold\":250,\"properties\":{\"$device\":\"D4J\",\"$os\":\"Windows 10\",\"$browser\":\"D4J\"},\"token\":\"?token\"}}";
                                Json welcomePayload = new Json(payload.replace("?token", token));
                                queueText(welcomePayload);
                                logger.info("Starting heartbeat with an interval of: " + hbi);
                                /*
                                 * gotAck starts at true, once a heatbeat is sent, set to false
                                 * When a ACK is received set to true and set redemption to false
                                 * If gotACK is false and redemption is false, wait one more ack to attempt reconnect
                                 * set redemption to true, if gotAck is false again, reconnect
                                 */
                                Async.addJob(o -> {
                                    if (gotACK) {
                                        gotACK = false;
                                        CustomLogger logger1 = new CustomLogger(this.getClass(), bot.getLoggerManager());
                                        logger1.debug("Sending HeartBeat Payload");
                                        Json object = Builder.buildPayload(TextOpCodes.Heartbeat.ordinal(), 251);
                                        queueText(object);
                                    } else {
                                        if (!redemption) {
                                            redemption = true;
                                            logger.error("Didn't get ACK in time waiting one more time");
                                        } else {
                                            redemption = false;
                                            gotACK = true;
                                            reconnectionCount++;
                                            if (reconnectionCount < 5) {
                                                logger.error("Didn't get ACK in time");
                                                logger.error("Restarting connection");
                                                try {
                                                    socket = socket.disconnect();
                                                    socket = socket.recreate().connect();
                                                } catch (WebSocketException | IOException e) {
                                                    e.printStackTrace();
                                                }
                                            } else {
                                                logger.error("Tried to reconnect 5 times, could not connect, check your internet connection or discordapi status");
                                                bot.getLoggerManager().dump();
                                                System.exit(-1);
                                            }
                                        }
                                    }
                                    return Optional.empty();
                                }, hbi);
                                logger.info("Hello event finished, bot online");
                                break;
                            case HeartBeat_ACK:
                                logger.debug("HeartBeat_ACK received");
                                gotACK = true;
                                redemption = false;
                                break;
                        }
                    }

                    @Override
                    public void onDisconnected(WebSocket websocket, WebSocketFrame serverCloseFrame, WebSocketFrame clientCloseFrame, boolean closedByServer) {
                        final CustomLogger logger = new CustomLogger(this.getClass(), bot.getLoggerManager());
                        logger.warn("Disconnected, closed by server: " + closedByServer);
                    }

                    @Override
                    public void onConnected(WebSocket websocket, Map<String, List<String>> headers) {
                        final CustomLogger logger = new CustomLogger(this.getClass(), bot.getLoggerManager());
                        logger.warn("Connected");
                        gotACK = true;
                        redemption = false;
                    }
                }).connect();
    }

    /**
     * Send text over the websocket
     *
     * @param json json to send
     */
    private void queueText(final Json json) {
        this.bot.getRateLimitRecorder().queue(new WebSocketEvent(socket, json));
    }

    /**
     * @return websocket instance
     */
    public WebSocket getSocket() {
        return socket;
    }
}
