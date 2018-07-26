package DiscordAPI.WebSocket;

import DiscordAPI.DiscordBot;
import DiscordAPI.Objects.DMessage;
import DiscordAPI.Objects.DStatus;
import DiscordAPI.WebSocket.JsonData.OpCodes;
import DiscordAPI.WebSocket.Utils.ConvertJSON;
import DiscordAPI.WebSocket.Utils.DiscordLogger;
import DiscordAPI.WebSocket.Utils.HeartBeat;
import DiscordAPI.WebSocket.Utils.Parsers.ChannelData;
import DiscordAPI.WebSocket.Utils.Parsers.GameData;
import DiscordAPI.WebSocket.Utils.Parsers.UserData;
import DiscordAPI.listener.Dispatcher.ListenerEvents.Message_Create;
import DiscordAPI.listener.Dispatcher.ListenerEvents.Presence_Update;
import DiscordAPI.listener.Dispatcher.TListener;
import com.neovisionaries.ws.client.WebSocket;
import com.neovisionaries.ws.client.WebSocketAdapter;
import com.neovisionaries.ws.client.WebSocketException;
import com.neovisionaries.ws.client.WebSocketFactory;
import org.json.simple.JSONObject;

import java.io.*;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Objects;
import java.util.Timer;

public class Wss {
    private static DiscordLogger logger = new DiscordLogger(String.valueOf(Wss.class));
    static final Timer timer = new Timer();

    public static WebSocket connect(final DiscordBot bot) throws IOException, WebSocketException {
        return new WebSocketFactory()
                .setConnectionTimeout(5000)
                .createSocket(DefaultLinks.WEBSOCKET)
                .addListener(new WebSocketAdapter() {
                    public void onTextMessage(WebSocket webSocket1, String message) throws IllegalAccessException, InstantiationException, NoSuchMethodException, InvocationTargetException {
                        JSONObject payload = (JSONObject) Objects.requireNonNull(ConvertJSON.convertToJSONOBJECT(message));
                        OpCodes opCodes = OpCodes.values()[Integer.parseInt(String.valueOf(payload.get("op")))];
                        switch (opCodes) {
                            case Dispatch:
                                String currentEvent = String.valueOf(payload.get("t"));
                                for (WebSocket_Events webSocket_events : WebSocket_Events.values()) {
                                    if (currentEvent.equals(webSocket_events.toString())) {
                                        Class<?> cl = webSocket_events.getaClass();
                                        Constructor constructor = cl.getConstructor(DiscordBot.class, JSONObject.class);
                                        Object t = constructor.newInstance(bot, payload);
                                        bot.getDispatcher().notify(t);
                                    }
                                }
                                /*

                                } else if (currentEvent.equals("MESSAGE_CREATE")) {

                                } else if (currentEvent.equals("CHANNEL_CREATE") || currentEvent.equals("CHANNEL_UPDATE") || currentEvent.equals("CHANNEL_DELETE")) {
                                    JSONObject d = (JSONObject) ConvertJSON.convertToJSONOBJECT(String.valueOf(payload.get("d")));
                                    ChannelData cd = new ChannelData(d).logic();
                                    System.out.println(cd.getChannel().getName());
                                    DiscordBot.updateChannels();
                                }
                                 */
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
                                logger.info("Connected to WebSocket");
                                logger.info("Received initial Message");
                                JSONObject d = (JSONObject) ConvertJSON.convertToJSONOBJECT(String.valueOf(payload.get("d")));
                                logger.info("Sending HeartBeast task every: " + Long.parseLong(String.valueOf(d.get("heartbeat_interval"))) + " milliseconds");
                                timer.schedule(new HeartBeat(webSocket1, bot), 0, Long.parseLong(String.valueOf(d.get("heartbeat_interval"))));
                                logger.info("Bot's Identity is Sent");
                                webSocket1.sendText(String.valueOf(bot.getIdentity()));
                                break;
                            case HeartBeat_ACK:
                                logger.info("HeartBeat returned");
                                break;
                        }
                    }
                }).connect();
    }
}
