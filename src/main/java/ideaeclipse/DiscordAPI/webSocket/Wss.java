package ideaeclipse.DiscordAPI.webSocket;

import com.neovisionaries.ws.client.WebSocket;
import com.neovisionaries.ws.client.WebSocketAdapter;
import com.neovisionaries.ws.client.WebSocketException;
import com.neovisionaries.ws.client.WebSocketFactory;
import ideaeclipse.AsyncUtility.Async;
import ideaeclipse.DiscordAPI.IPrivateBot;
import ideaeclipse.DiscordAPI.objects.ParserObjects;
import ideaeclipse.DiscordAPI.objects.Payloads;
import ideaeclipse.DiscordAPI.utils.DiscordUtils;
import ideaeclipse.DiscordAPI.utils.RateLimitRecorder;
import ideaeclipse.DiscordAPI.utils.TextHeartBeat;
import ideaeclipse.JsonUtilities.Json;
import ideaeclipse.customLogger.CustomLogger;
import ideaeclipse.customLogger.Level;
import ideaeclipse.reflectionListener.Event;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Optional;

import static ideaeclipse.DiscordAPI.utils.DiscordUtils.DefaultLinks.rateLimitRecorder;

public class Wss extends WebSocketFactory {
    private final CustomLogger logger;
    private final Object lock = new Object();
    private Thread heartbeat;
    private Payloads.DWelcome w;
    private Long startTime;
    private Wss wss;
    private final WebSocket webSocket;

    public Wss(final IPrivateBot bot) throws IOException, WebSocketException {
        this.logger = new CustomLogger(this.getClass(),bot.getLoggerManager());
        if (bot.getProperties().getProperty("debug").equals("true")) {
            logger.setLevel(Level.DEBUG);
        }
        wss = this;
        webSocket = this
                .setConnectionTimeout(5000)
                .createSocket(DiscordUtils.DefaultLinks.WEBSOCKET)
                .addListener(new WebSocketAdapter() {
                    public void onTextMessage(WebSocket webSocket1, String message) {
                        Json payload = new Json(message);
                        Payloads.General g = ParserObjects.convertToPayload(payload, Payloads.General.class);
                        TextOpCodes opCodes = TextOpCodes.values()[g.op];
                        switch (opCodes) {
                            case Dispatch:
                                Async.queue(x -> {
                                    try {
                                        for (WebSocket_Events webSocket_events : WebSocket_Events.values()) {
                                            if (g.t.equals(webSocket_events.toString())) {
                                                Class<?> cl = webSocket_events.getaClass();
                                                Constructor constructor = cl.getConstructor(IPrivateBot.class, Json.class);
                                                Object t = constructor.newInstance(bot, new Json((String) payload.get("d")));
                                                bot.getDispatcher().callEvent((Event) t);
                                            }
                                        }
                                    } catch (NoSuchMethodException | InstantiationException | InvocationTargetException | IllegalAccessException e) {
                                        e.printStackTrace();
                                    }
                                    return Optional.empty();
                                }, g.t);
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
                                logger.error("Invalid_Session Restart if issue persists regen token");
                                System.exit(1);
                                break;
                            case Hello:
                                Thread.currentThread().setName("TextWss");
                                logger.info("Connected to webSocket");
                                logger.info("Received initial Message");
                                w = ParserObjects.convertToPayload(g.d, Payloads.DWelcome.class);
                                logger.info("Sending HeartBeast task every: " + w.heartbeat_interval + " milliseconds");
                                heartbeat = DiscordUtils.createDaemonThreadFactory("Heartbeat").newThread(new TextHeartBeat(wss, w.heartbeat_interval,bot.getLoggerManager()));
                                startTime = System.currentTimeMillis();
                                heartbeat.start();
                                sendText(bot.getIdentity());
                                synchronized (lock) {
                                    lock.notify();
                                }
                                break;
                            case HeartBeat_ACK:
                                logger.debug("Alive");
                                if (heartbeat.isAlive()) {
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

    public void sendText(final Json message) {
        rateLimitRecorder.queue(new RateLimitRecorder.QueueHandler.WebSocketEvent(webSocket, message));
    }

    public WebSocket getWebSocket() {
        return webSocket;
    }

    public Object getLock() {
        return lock;
    }
}
