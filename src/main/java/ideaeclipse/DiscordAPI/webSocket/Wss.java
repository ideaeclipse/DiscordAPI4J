package ideaeclipse.DiscordAPI.webSocket;

import com.neovisionaries.ws.client.WebSocket;
import com.neovisionaries.ws.client.WebSocketAdapter;
import com.neovisionaries.ws.client.WebSocketException;
import com.neovisionaries.ws.client.WebSocketFactory;
import ideaeclipse.AsyncUtility.Async;
import ideaeclipse.DiscordAPI.bot.IPrivateBot;
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
import ideaeclipse.JsonUtilities.Builder;
import ideaeclipse.JsonUtilities.Json;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static ideaeclipse.DiscordAPI.utils.Util.rateLimitRecorder;

/**
 * TODO: Remove dependency on json file. make a static string
 * TODO: Update the way each dispatch gets handled
 */
public final class Wss extends WebSocketFactory {
    private final String WEBSOCKET = "wss://gateway.discord.gg/?v=6&encoding=json";
    private final WebSocket socket;

    public Wss(final IPrivateBot bot, final String token) throws IOException, WebSocketException {
        socket = this.setConnectionTimeout(5000)
                .createSocket(WEBSOCKET)
                .addListener(new WebSocketAdapter() {
                    @Override
                    public void onTextMessage(WebSocket websocket, String text) {
                        Json message = new Json(text);
                        final TextOpCodes op = TextOpCodes.values()[Integer.parseInt(String.valueOf(message.get("op")))];
                        final String s = String.valueOf(message.get("s"));
                        final String t = String.valueOf(message.get("t"));
                        final Json d = new Json(!String.valueOf(message.get("d")).equals("null") ? String.valueOf(message.get("d")) : "{}");
                        switch (op) {
                            case Dispatch:
                                // System.out.println("OP: " + op + " S: " + s + " T: " + t + " D: " + d);
                                List<DispatchType> filtered = Arrays.stream(DispatchType.values()).filter(o -> o.name().equals(t.toUpperCase())).collect(Collectors.toList());
                                if (!filtered.isEmpty()) {
                                    switch (filtered.get(0)) {
                                        case MESSAGE_CREATE:
                                            Util.checkConstructor(bot.getManager(), MessageCreate.class, d, bot);
                                            break;
                                        case PRESENCE_UPDATE:
                                            Util.checkConstructor(bot.getManager(), PresenceUpdate.class, d, bot);
                                            break;
                                        case CHANNEL_CREATE:
                                            CheckResponse<CreateChannel> channelCheckResponse = Util.checkConstructor(bot.getManager(), CreateChannel.class, d, bot);
                                            if (!channelCheckResponse.getType().equals(CheckResponeType.EXECUTED)) {
                                                CreateDMChannel channel = Util.checkConstructor(bot.getManager(), CreateDMChannel.class, d, bot).getObject();
                                                IChannel channel1 = channel.getChannel();
                                                bot.getChannels().put(channel1.getId(), channel1.getName(), channel1);
                                            } else {
                                                IChannel channel = channelCheckResponse.getObject().getChannel();
                                                bot.getChannels().put(channel.getId(), channel.getName(), channel);
                                            }
                                            break;
                                        case CHANNEL_UPDATE:
                                            CheckResponse<UpdateChannel> updateChannelCheckResponse = Util.checkConstructor(bot.getManager(), UpdateChannel.class, d, bot);
                                            if (updateChannelCheckResponse.getType().equals(CheckResponeType.EXECUTED)) {
                                                IChannel channel = updateChannelCheckResponse.getObject().getChannel();
                                                bot.getChannels().removeByK1(channel.getId());
                                                bot.getChannels().put(channel.getId(), channel.getName(), channel);
                                            }
                                            break;
                                        case CHANNEL_DELETE:
                                            Util.checkConstructor(bot.getManager(), DeleteChannel.class, d, bot);
                                            break;
                                        case GUILD_ROLE_CREATE:
                                            CheckResponse<CreateRole> createRoleCheckResponse = Util.checkConstructor(bot.getManager(), CreateRole.class, d, bot);
                                            if (createRoleCheckResponse.getType().equals(CheckResponeType.EXECUTED)) {
                                                IRole role = createRoleCheckResponse.getObject().getRole();
                                                bot.getRoles().put(role.getId(), role.getName(), role);
                                            }
                                            break;
                                        case GUILD_ROLE_UPDATE:
                                            CheckResponse<UpdateRole> updateRoleCheckResponse = Util.checkConstructor(bot.getManager(), UpdateRole.class, d, bot);
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
                                            Util.checkConstructor(bot.getManager(), DeleteRole.class, d, bot);
                                            break;
                                        case GUILD_MEMBER_ADD:
                                            CheckResponse<CreateDiscordUser> createDiscordUserCheckResponse = Util.checkConstructor(bot.getManager(), CreateDiscordUser.class, d, bot);
                                            if (createDiscordUserCheckResponse.getType().equals(CheckResponeType.EXECUTED)) {
                                                IDiscordUser user = createDiscordUserCheckResponse.getObject().getUser();
                                                bot.getUsers().put(user.getId(), user.getUsername(), user);
                                            }
                                            break;
                                        case GUILD_MEMBER_UPDATE:
                                            CheckResponse<UpdateDiscordUser> updateDiscordUserCheckResponse = Util.checkConstructor(bot.getManager(), UpdateDiscordUser.class, d, bot);
                                            if (updateDiscordUserCheckResponse.getType().equals(CheckResponeType.EXECUTED)) {
                                                IDiscordUser user = updateDiscordUserCheckResponse.getObject().getUser();
                                                bot.getUsers().put(user.getId(), user.getUsername(), user);
                                            }
                                            break;
                                        case GUILD_MEMBER_REMOVE:
                                            Util.checkConstructor(bot.getManager(), DeleteDiscordUser.class, d, bot);
                                            break;
                                        case MESSAGE_REACTION_ADD:
                                            AddReaction addReaction = Util.checkConstructor(bot.getManager(), AddReaction.class, d, bot).getObject();
                                            IReaction reaction = addReaction.getReaction();
                                            reaction.getChannel().getMessageHistory().get(reaction.getMessage().getId()).addReaction(reaction.getCode());
                                            break;
                                        case MESSAGE_REACTION_REMOVE:
                                            Util.checkConstructor(bot.getManager(), RemoveReaction.class, d, bot);
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
                                System.out.println("Resume: " + message);
                                System.exit(-1);
                                break;
                            case Reconnect:
                                System.out.println("Reconnect: " + message);
                                System.exit(-1);
                                break;
                            case Request_Guild_Members:
                                break;
                            case Invalid_Session:
                                break;
                            case Hello:
                                final long hbi = Long.parseUnsignedLong(String.valueOf(d.get("heartbeat_interval")));
                                File file = new File("WelcomePayload.json");
                                Json welcomePayload = null;
                                try {
                                    BufferedReader reader = new BufferedReader(new FileReader(file));
                                    StringBuilder builder = new StringBuilder();
                                    String newLine;
                                    while ((newLine = reader.readLine()) != null) {
                                        builder.append(newLine);
                                    }
                                    welcomePayload = new Json(builder.toString().replace("?token", token));
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                if (welcomePayload != null)
                                    sendText(welcomePayload);
                                else {
                                    System.out.println("couldn't read from welcomePayload json. Exiting");
                                    System.exit(-1);
                                }
                                System.out.println("Starting heartbeat: " + hbi);
                                Async.addJob(o -> {
                                    Json object = Builder.buildPayload(ideaeclipse.DiscordAPI.webSocket.TextOpCodes.Heartbeat.ordinal(), 251);
                                    sendText(object);
                                    return Optional.empty();
                                }, hbi);
                                break;
                            case HeartBeat_ACK:
                                System.out.println("Alive");
                                break;
                        }
                    }
                }).connect();
    }

    private void sendText(final Json json) {
        rateLimitRecorder.queue(new RateLimitRecorder.QueueHandler.WebSocketEvent(socket, json));
    }

    public WebSocket getSocket() {
        return socket;
    }
}
