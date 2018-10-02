package ideaeclipse.DiscordAPI.webSocket;

import ideaeclipse.DiscordAPI.IDiscordBot;
import ideaeclipse.DiscordAPI.IPrivateBot;
import ideaeclipse.DiscordAPI.objects.*;
import ideaeclipse.DiscordAPI.utils.*;
import com.neovisionaries.ws.client.WebSocket;
import com.neovisionaries.ws.client.WebSocketAdapter;
import com.neovisionaries.ws.client.WebSocketException;
import com.neovisionaries.ws.client.WebSocketFactory;
import ideaeclipse.JsonUtilities.Builder;
import ideaeclipse.JsonUtilities.Json;
import ideaeclipse.JsonUtilities.Parser;

import java.io.IOException;
import java.util.concurrent.Callable;

import static ideaeclipse.DiscordAPI.utils.DiscordUtils.DefaultLinks.rateLimitRecorder;
@Deprecated
public class VoiceWss extends WebSocketFactory {
    private final DiscordLogger logger = new DiscordLogger(String.valueOf(this.getClass()));
    private final VoiceWss wss;
    private final IPrivateBot bot;
    private final VServerUpdate initialServerUpdate;
    private final VStateUpdate initialStateUpdate;
    private final WebSocket webSocket;

    public VoiceWss(final IPrivateBot bot, final VServerUpdate vServerUpdate, final VStateUpdate vStateUpdate, final String endpoint) throws IOException, WebSocketException {
        wss = this;
        this.bot = bot;
        this.initialServerUpdate = vServerUpdate;
        this.initialStateUpdate = vStateUpdate;
        if (bot.getProperties().getProperty("debug").equals("true")) {
            logger.setLevel(DiscordLogger.Level.TRACE);
        }
        webSocket = this
                .setConnectionTimeout(5000)
                .createSocket("ws://" + endpoint + "?v=3")
                .addListener(new WebSocketAdapter() {
                    @Override
                    public void onTextMessage(WebSocket websocket, String message) throws Exception {
                        Json payload = new Json(message);
                        Payloads.General g = ParserObjects.convertToPayload(payload, Payloads.General.class);
                        VoiceOpCodes opCodes = VoiceOpCodes.values()[g.op];
                        switch (opCodes) {
                            case Ready:
                                logger.debug(String.valueOf(payload));
                                break;
                            case HeartBeat_Ack:
                                System.out.println(payload);

                                break;
                            case Initial:
                                Thread.currentThread().setName("VoiceWss");
                                logger.debug("Initial");
                                Payloads.DWelcome welcome = ParserObjects.convertToPayload(g.d, Payloads.DWelcome.class);
                                Thread heartBeat = DiscordUtils.createDaemonThreadFactory("VoiceHeartBeat").newThread(new VoiceHeartBeat(wss, (int) (welcome.heartbeat_interval * .75)));
                                logger.info("Starting Voice HeartBeat");
                                BuilderObjects.VoiceIdentify v = new BuilderObjects.VoiceIdentify();
                                v.server_id = bot.getGuildId();
                                v.user_id = initialStateUpdate.getUser_id();
                                v.token = initialServerUpdate.getToken();
                                v.session_id = initialStateUpdate.getSession_id();
                                sendText(Builder.buildPayload(VoiceOpCodes.zero.ordinal(), Builder.buildData(v)));
                                heartBeat.start();
                                break;
                        }
                    }
                }).connect();
    }

    public void sendText(final Json message) {
        rateLimitRecorder.queue(new RateLimitRecorder.QueueHandler.WebSocketEvent(webSocket, message));
    }
}
