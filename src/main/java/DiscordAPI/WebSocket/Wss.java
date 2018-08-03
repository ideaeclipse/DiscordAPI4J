package DiscordAPI.WebSocket;

import DiscordAPI.DiscordBot;
import DiscordAPI.WebSocket.JsonData.OpCodes;
import DiscordAPI.WebSocket.Utils.DiscordLogger;
import DiscordAPI.WebSocket.Utils.DiscordUtils;
import DiscordAPI.WebSocket.Utils.HeartBeat;
import DiscordAPI.WebSocket.Utils.Parsers.Payloads;
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
                .createSocket(DiscordUtils.DefaultLinks.WEBSOCKET)
                .addListener(new WebSocketAdapter() {
                    public void onTextMessage(WebSocket webSocket1, String message) throws IllegalAccessException, InstantiationException, NoSuchMethodException, InvocationTargetException {
                        JSONObject payload = (JSONObject) Objects.requireNonNull(DiscordUtils.convertToJSONOBJECT(message));
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
                                JSONObject d = (JSONObject) DiscordUtils.convertToJSONOBJECT(String.valueOf(payload.get("d")));
                                Payloads.Welcome w = DiscordUtils.Parser.convertToJSON(d,Payloads.Welcome.class);
                                logger.info("Sending HeartBeast task every: " + w.heartbeat_interval + " milliseconds");
                                timer.schedule(new HeartBeat(webSocket1, bot), 0, Long.parseLong(String.valueOf(d.get("heartbeat_interval"))));
                                webSocket1.sendText(String.valueOf(bot.getIdentity()));
                                break;
                            case HeartBeat_ACK:
                                //logger.info("HeartBeat returned");
                                break;
                        }
                    }
                }).connect();
    }
}
