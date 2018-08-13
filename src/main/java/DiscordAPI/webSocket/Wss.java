package DiscordAPI.webSocket;

import DiscordAPI.listener.listenerTypes.ListenerEvent;
import DiscordAPI.objects.DiscordBot;
import DiscordAPI.objects.Parser;
import DiscordAPI.webSocket.jsonData.OpCodes;
import DiscordAPI.utils.DiscordLogger;
import DiscordAPI.utils.DiscordUtils;
import DiscordAPI.utils.HeartBeat;
import DiscordAPI.objects.Payloads;
import com.neovisionaries.ws.client.WebSocket;
import com.neovisionaries.ws.client.WebSocketAdapter;
import com.neovisionaries.ws.client.WebSocketException;
import com.neovisionaries.ws.client.WebSocketFactory;
import org.json.simple.JSONObject;

import java.io.*;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Objects;


public class Wss {
    private static DiscordLogger logger = new DiscordLogger(String.valueOf(Wss.class));
    private static Thread heartbeat;
    private static Payloads.DWelcome w;
    private static Long startTime;

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
                                        Object t = constructor.newInstance(bot, payload.get("d"));
                                        bot.getDispatcher().notify((ListenerEvent) t);
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
                                logger.info("Connected to webSocket");
                                logger.info("Received initial Message");
                                JSONObject d = (JSONObject) DiscordUtils.convertToJSONOBJECT(String.valueOf(payload.get("d")));
                                w = Parser.convertToJSON(d, Payloads.DWelcome.class);
                                logger.info("Sending HeartBeast task every: " + w.heartbeat_interval + " milliseconds");
                                heartbeat = DiscordUtils.createDaemonThreadFactory("Heartbeat").newThread(new HeartBeat(webSocket1, w.heartbeat_interval));
                                startTime = System.currentTimeMillis();
                                heartbeat.start();
                                webSocket1.sendText(String.valueOf(bot.getIdentity()));
                                break;
                            case HeartBeat_ACK:
                                if (heartbeat.isAlive()) {
                                    if ((System.currentTimeMillis() - startTime > (w.heartbeat_interval + 1000)) && heartbeat.isAlive()) {
                                        heartbeat.interrupt();
                                        logger.error("Heartbeat return took to long");
                                        break;
                                    }
                                    startTime = System.currentTimeMillis();

                                }
                                break;
                        }
                    }
                }).connect();
    }
}
