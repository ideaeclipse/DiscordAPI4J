package DiscordAPI.webSocket;

import DiscordAPI.IDiscordBot;
import DiscordAPI.listener.listenerTypes.ListenerEvent;
import DiscordAPI.objects.Parser;
import DiscordAPI.utils.RateLimitRecorder;
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

import static DiscordAPI.utils.DiscordUtils.DefaultLinks.rateLimitRecorder;

public class Wss extends WebSocketFactory {
    private final DiscordLogger logger = new DiscordLogger(String.valueOf(Wss.class));
    private Thread heartbeat;
    private Payloads.DWelcome w;
    private Long startTime;
    private Wss wss;
    private final WebSocket webSocket;

    public Wss(final IDiscordBot bot) throws IOException, WebSocketException {
        wss = this;
        webSocket = this
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
                                        Constructor constructor = cl.getConstructor(IDiscordBot.class, JSONObject.class);
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
                                Thread.currentThread().setName("TextWss");
                                logger.info("Connected to webSocket");
                                logger.info("Received initial Message");
                                JSONObject d = (JSONObject) DiscordUtils.convertToJSONOBJECT(String.valueOf(payload.get("d")));
                                w = Parser.convertToJSON(d, Payloads.DWelcome.class);
                                logger.info("Sending HeartBeast task every: " + w.heartbeat_interval + " milliseconds");
                                heartbeat = DiscordUtils.createDaemonThreadFactory("Heartbeat").newThread(new HeartBeat(wss, w.heartbeat_interval));
                                startTime = System.currentTimeMillis();
                                heartbeat.start();
                                sendText(bot.getIdentity());
                                break;
                            case HeartBeat_ACK:
                                if (heartbeat.isAlive()) {
                                    System.out.println(System.currentTimeMillis() - startTime);
                                    if ((System.currentTimeMillis() - startTime > (w.heartbeat_interval + 5000)) && heartbeat.isAlive()) {
                                        heartbeat.interrupt();
                                        logger.error("Heartbeat return took to long");
                                    }
                                    startTime = System.currentTimeMillis();
                                }
                                break;
                        }
                    }
                }).connect();
    }

    public void sendText(final JSONObject message) {
        rateLimitRecorder.queue(new RateLimitRecorder.QueueHandler.WebSocketEvent(webSocket,message));
    }

    public WebSocket getWebSocket() {
        return webSocket;
    }
}