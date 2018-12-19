package ideaeclipse.DiscordAPINEW.webSocket;

import com.neovisionaries.ws.client.WebSocket;
import com.neovisionaries.ws.client.WebSocketAdapter;
import com.neovisionaries.ws.client.WebSocketException;
import com.neovisionaries.ws.client.WebSocketFactory;
import ideaeclipse.AsyncUtility.Async;
import ideaeclipse.DiscordAPINEW.bot.IPrivateBot;
import ideaeclipse.DiscordAPINEW.bot.objects.channel.directMessage.LoadDMChannel;
import ideaeclipse.DiscordAPINEW.bot.objects.channel.regularChannels.LoadChannel;
import ideaeclipse.DiscordAPINEW.bot.objects.message.MessageCreate;
import ideaeclipse.DiscordAPINEW.bot.objects.presence.PresenceUpdate;
import ideaeclipse.DiscordAPINEW.utils.Util;
import ideaeclipse.JsonUtilities.Builder;
import ideaeclipse.JsonUtilities.Json;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Optional;

import static ideaeclipse.DiscordAPINEW.utils.Util.rateLimitRecorder;

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
                        final TextOpCodes op = TextOpCodes.values()[(int) message.get("op")];
                        final String s = String.valueOf(message.get("s"));
                        final String t = String.valueOf(message.get("t"));
                        final Json d = new Json(String.valueOf(message.get("d")));
                        switch (op) {
                            case Dispatch:
                                //System.out.println("OP: " + op + " S: " + s + " T: " + t + " D: " + d);
                                if (t.toLowerCase().equals("message_create")) {
                                    Util.check(bot.getManager(), new MessageCreate(bot.getChannels(), bot.getUsers()), d);
                                }
                                if (t.toLowerCase().equals("presence_update")) {
                                    Util.check(bot.getManager(), new PresenceUpdate(bot.getUsers()), d);
                                }
                                if (t.toLowerCase().equals("channel_create")) {
                                    if (Boolean.parseBoolean(String.valueOf(Util.check(bot.getManager(), new LoadChannel(), d).orElse(false))))
                                        Util.check(bot.getManager(), new LoadDMChannel(bot.getUsers(), bot.getChannels()), d);

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
                                break;
                            case Reconnect:
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
