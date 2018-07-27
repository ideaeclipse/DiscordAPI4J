package DiscordAPI.WebSocket;

import DiscordAPI.DiscordBot;
import DiscordAPI.Objects.Audio.AudioManager;
import DiscordAPI.WebSocket.JsonData.OpCodes;
import DiscordAPI.WebSocket.Utils.ConvertJSON;
import DiscordAPI.WebSocket.Utils.DiscordLogger;
import DiscordAPI.WebSocket.Utils.HeartBeat;
import com.neovisionaries.ws.client.WebSocket;
import com.neovisionaries.ws.client.WebSocketAdapter;
import com.neovisionaries.ws.client.WebSocketException;
import com.neovisionaries.ws.client.WebSocketFactory;
import org.json.simple.JSONObject;

import java.io.IOException;
import java.util.Timer;

public class VoiceWss {
    private static DiscordLogger logger = new DiscordLogger(String.valueOf(VoiceWss.class));
    static final Timer timer = new Timer();

    public static WebSocket connect(final DiscordBot bot, AudioManager audioManager) throws IOException, WebSocketException {
        return new WebSocketFactory()
                .setConnectionTimeout(5000)
                .createSocket("ws://" + audioManager.getVoiceAuth().getEndpoint())
                .addListener(new WebSocketAdapter() {
                    public void onTextMessage(WebSocket webSocket1, String message) {
                        System.out.println(message);
                        JSONObject payload = new JSONObject();
                        JSONObject object = new JSONObject();
                        object.put("server_id",audioManager.getVoiceAuth().getGuild_id());
                        object.put("user_id",bot.getId());
                        object.put("session_id",audioManager.getVoiceAuth().getSession());
                        object.put("token",audioManager.getVoiceAuth().getToken());
                        payload.put("op",0);
                        payload.put("d",object);
                        System.out.println(payload);
                        webSocket1.sendText(String.valueOf(payload));
                    }
                }).connect();
    }
}
