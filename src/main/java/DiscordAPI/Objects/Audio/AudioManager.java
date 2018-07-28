package DiscordAPI.Objects.Audio;

import DiscordAPI.DiscordBot;
import DiscordAPI.WebSocket.Voice.Ready.READY;
import DiscordAPI.WebSocket.Voice.Ready.ReadyObject;
import DiscordAPI.WebSocket.Voice.VoiceOpCodes;
import DiscordAPI.WebSocket.Voice.VoiceStateUpdate.VSUObject;
import com.neovisionaries.ws.client.WebSocket;
import org.json.simple.JSONObject;

import java.io.FileNotFoundException;

public class AudioManager {
    private DiscordBot bot;
    private WebSocket socket;
    private WebSocket voiceSocket;
    private Long guild_id;
    private VoiceAuth voiceAuth;
    private VoiceUDPSocket UDPsocket;

    public AudioManager(DiscordBot bot, WebSocket socket, Long guild_id) {
        this.bot = bot;
        this.socket = socket;
        this.guild_id = guild_id;
        this.voiceAuth = new VoiceAuth(bot);
    }

    public AudioManager initialize(Long id) {
        JSONObject object = new VSUObject(bot, guild_id).getVsu();
        object.put("channel_id", Long.parseUnsignedLong("471104815192211462"));
        JSONObject payload = new JSONObject();
        payload.put("op", 4);
        payload.put("d", object);
        socket.sendText(String.valueOf(payload));
        return this;
    }

    public void ready(JSONObject ready) {
        System.out.println(ready);
        ReadyObject object = new ReadyObject(ready, VoiceOpCodes.Protocol, String.valueOf(ready.get("ip")), Integer.parseInt(String.valueOf(ready.get("port"))));
        voiceSocket.sendText(String.valueOf(object.getPayload(object.logic(READY.values()))));
        this.UDPsocket = new VoiceUDPSocket(socket, String.valueOf(ready.get("ip")), Integer.parseInt(String.valueOf(ready.get("port"))), Integer.parseInt(String.valueOf(ready.get("ssrc"))));
    }

    public VoiceUDPSocket getUDPsocket() {
        return UDPsocket;
    }

    public VoiceAuth getVoiceAuth() {
        return voiceAuth;
    }

    public void setVoiceSocket(WebSocket voiceSocket) {
        this.voiceSocket = voiceSocket;
    }
}
