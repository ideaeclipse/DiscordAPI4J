package ideaeclipse.DiscordAPINEW.webSocket;

import com.neovisionaries.ws.client.WebSocket;
import com.neovisionaries.ws.client.WebSocketAdapter;
import com.neovisionaries.ws.client.WebSocketException;
import com.neovisionaries.ws.client.WebSocketFactory;
import ideaeclipse.AsyncUtility.Async;
import ideaeclipse.DiscordAPINEW.bot.IPrivateBot;
import ideaeclipse.DiscordAPINEW.bot.objects.channel.directMessage.CreateDMChannel;
import ideaeclipse.DiscordAPINEW.bot.objects.channel.regularChannels.CreateChannel;
import ideaeclipse.DiscordAPINEW.bot.objects.channel.regularChannels.DeleteChannel;
import ideaeclipse.DiscordAPINEW.bot.objects.channel.regularChannels.UpdateChannel;
import ideaeclipse.DiscordAPINEW.bot.objects.message.MessageCreate;
import ideaeclipse.DiscordAPINEW.bot.objects.presence.PresenceUpdate;
import ideaeclipse.DiscordAPINEW.bot.objects.reaction.AddReaction;
import ideaeclipse.DiscordAPINEW.bot.objects.reaction.IReaction;
import ideaeclipse.DiscordAPINEW.bot.objects.reaction.RemoveReaction;
import ideaeclipse.DiscordAPINEW.bot.objects.role.CreateRole;
import ideaeclipse.DiscordAPINEW.bot.objects.role.DeleteRole;
import ideaeclipse.DiscordAPINEW.bot.objects.role.UpdateRole;
import ideaeclipse.DiscordAPINEW.bot.objects.user.DeleteDiscordUser;
import ideaeclipse.DiscordAPINEW.bot.objects.user.CreateDiscordUser;
import ideaeclipse.DiscordAPINEW.bot.objects.user.UpdateDiscordUser;
import ideaeclipse.DiscordAPINEW.utils.CheckResponeType;
import ideaeclipse.DiscordAPINEW.utils.CheckResponse;
import ideaeclipse.DiscordAPINEW.utils.Util;
import ideaeclipse.JsonUtilities.Builder;
import ideaeclipse.JsonUtilities.Json;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static ideaeclipse.DiscordAPINEW.utils.Util.rateLimitRecorder;

/**
 *
 * TODO: Remove dependency on json file. make a static string
 * TODO: Update the way each dispatch gets handled
 */
public class Wss extends WebSocketFactory {
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
                                                bot.getChannels().put(channel.getChannel().getId(), channel.getChannel());
                                            } else {
                                                bot.getChannels().put(channelCheckResponse.getObject().getChannel().getId(), channelCheckResponse.getObject().getChannel());
                                            }
                                            break;
                                        case CHANNEL_UPDATE:
                                            CheckResponse<UpdateChannel> updateChannelCheckResponse = Util.checkConstructor(bot.getManager(), UpdateChannel.class, d, bot);
                                            if (updateChannelCheckResponse.getType().equals(CheckResponeType.EXECUTED))
                                                bot.getChannels().put(updateChannelCheckResponse.getObject().getChannel().getId(), updateChannelCheckResponse.getObject().getChannel());
                                            break;
                                        case CHANNEL_DELETE:
                                            Util.checkConstructor(bot.getManager(), DeleteChannel.class, d, bot);
                                            break;
                                        case GUILD_ROLE_CREATE:
                                            CheckResponse<CreateRole> createRoleCheckResponse = Util.checkConstructor(bot.getManager(), CreateRole.class, d, bot);
                                            if (createRoleCheckResponse.getType().equals(CheckResponeType.EXECUTED))
                                                bot.getRoles().put(createRoleCheckResponse.getObject().getRole().getId(), createRoleCheckResponse.getObject().getRole());
                                            break;
                                        case GUILD_ROLE_UPDATE:
                                            CheckResponse<UpdateRole> updateRoleCheckResponse = Util.checkConstructor(bot.getManager(), UpdateRole.class, d, bot);
                                            if (updateRoleCheckResponse.getType().equals(CheckResponeType.EXECUTED))
                                                bot.getRoles().put(updateRoleCheckResponse.getObject().getRole().getId(), updateRoleCheckResponse.getObject().getRole());
                                            break;
                                        case GUILD_ROLE_DELETE:
                                            Util.checkConstructor(bot.getManager(), DeleteRole.class, d, bot);
                                            break;
                                        case GUILD_MEMBER_ADD:
                                            CheckResponse<CreateDiscordUser> createDiscordUserCheckResponse = Util.checkConstructor(bot.getManager(), CreateDiscordUser.class, d, bot);
                                            if (createDiscordUserCheckResponse.getType().equals(CheckResponeType.EXECUTED))
                                                bot.getUsers().put(createDiscordUserCheckResponse.getObject().getUser().getId(), createDiscordUserCheckResponse.getObject().getUser());
                                            break;
                                        case GUILD_MEMBER_UPDATE:
                                            CheckResponse<UpdateDiscordUser> updateDiscordUserCheckResponse = Util.checkConstructor(bot.getManager(), UpdateDiscordUser.class, d, bot);
                                            if (updateDiscordUserCheckResponse.getType().equals(CheckResponeType.EXECUTED))
                                                bot.getUsers().put(updateDiscordUserCheckResponse.getObject().getUser().getId(), updateDiscordUserCheckResponse.getObject().getUser());
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
