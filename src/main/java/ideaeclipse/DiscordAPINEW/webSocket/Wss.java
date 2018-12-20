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
import ideaeclipse.DiscordAPINEW.bot.objects.role.CreateRole;
import ideaeclipse.DiscordAPINEW.bot.objects.role.DeleteRole;
import ideaeclipse.DiscordAPINEW.bot.objects.role.UpdateRole;
import ideaeclipse.DiscordAPINEW.bot.objects.user.DeleteDiscordUser;
import ideaeclipse.DiscordAPINEW.bot.objects.user.LoadUser;
import ideaeclipse.DiscordAPINEW.bot.objects.user.UpdateDiscordUser;
import ideaeclipse.DiscordAPINEW.utils.CheckResponeType;
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
 * TODO: Remove dependency on json file. make a static string
 * TODO: Update the way each dispath gets handled
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
                                            Util.check(bot.getManager(), new MessageCreate(bot.getChannels(), bot.getUsers()), d);
                                            break;
                                        case PRESENCE_UPDATE:
                                            Util.check(bot.getManager(), new PresenceUpdate(bot.getUsers()), d);
                                            break;
                                        case CHANNEL_CREATE:
                                            CreateChannel channel = new CreateChannel();
                                            if (!Util.check(bot.getManager(), channel, d).getType().equals(CheckResponeType.EXECUTED))
                                                Util.check(bot.getManager(), new CreateDMChannel(bot.getUsers(), bot.getChannels()), d);
                                            else
                                                bot.getChannels().put(channel.getChannel().getId(), channel.getChannel());
                                            System.out.println(bot.getChannels());
                                            break;
                                        case CHANNEL_UPDATE:
                                            UpdateChannel Updatechannel = new UpdateChannel();
                                            if (Util.check(bot.getManager(), Updatechannel, d).getType().equals(CheckResponeType.EXECUTED))
                                                bot.getChannels().put(Updatechannel.getChannel().getId(), Updatechannel.getChannel());
                                            System.out.println(bot.getChannels());
                                            break;
                                        case CHANNEL_DELETE:
                                            Util.check(bot.getManager(), new DeleteChannel(bot.getChannels()), d);
                                            System.out.println(bot.getChannels());
                                            break;
                                        case GUILD_ROLE_CREATE:
                                            CreateRole role = new CreateRole();
                                            if (Util.check(bot.getManager(), role, new Json(String.valueOf(d.get("role")))).getType().equals(CheckResponeType.EXECUTED))
                                                bot.getRoles().put(role.getRole().getId(), role.getRole());
                                            System.out.println(bot.getRoles());
                                            break;
                                        case GUILD_ROLE_UPDATE:
                                            UpdateRole Updaterole = new UpdateRole();
                                            if (Util.check(bot.getManager(), Updaterole, d).getType().equals(CheckResponeType.EXECUTED))
                                                bot.getRoles().put(Updaterole.getRole().getId(), Updaterole.getRole());
                                            System.out.println(bot.getRoles());
                                            break;
                                        case GUILD_ROLE_DELETE:
                                            Util.check(bot.getManager(), new DeleteRole(bot.getRoles()), d);
                                            System.out.println(bot.getRoles());
                                            break;
                                        case GUILD_MEMBER_ADD:
                                            LoadUser user = new LoadUser(bot.getRoles());
                                            if (Util.check(bot.getManager(), user, d).getType().equals(CheckResponeType.EXECUTED))
                                                bot.getUsers().put(user.getUser().getId(), user.getUser());
                                            System.out.println(bot.getUsers());
                                            break;
                                        case GUILD_MEMBER_UPDATE:
                                            UpdateDiscordUser Updateuser = new UpdateDiscordUser(bot.getRoles());
                                            if (Util.check(bot.getManager(), Updateuser, d).getType().equals(CheckResponeType.EXECUTED))
                                                bot.getUsers().put(Updateuser.getUser().getId(), Updateuser.getUser());
                                            System.out.println(bot.getUsers());
                                            break;
                                        case GUILD_MEMBER_REMOVE:
                                            Util.check(bot.getManager(), new DeleteDiscordUser(bot.getUsers()), d);
                                            System.out.println(bot.getUsers());
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
                                System.out.println("Resume: " + d);
                                System.exit(-1);
                                break;
                            case Reconnect:
                                System.out.println("Reconnect: " + d);
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
}
