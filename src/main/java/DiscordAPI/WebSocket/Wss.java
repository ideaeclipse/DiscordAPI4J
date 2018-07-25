package DiscordAPI.WebSocket;

import DiscordAPI.Bot.BotImpl;
import DiscordAPI.Objects.DMessage;
import DiscordAPI.WebSocket.JsonData.OpCodes;
import DiscordAPI.WebSocket.Utils.ConvertJSON;
import DiscordAPI.WebSocket.Utils.HeartBeat;
import DiscordAPI.WebSocket.Utils.Parsers.ChannelData;
import DiscordAPI.WebSocket.Utils.Parsers.UserData;
import DiscordAPI.listener.Dispatcher.ListenerEvents.Message_Create;
import DiscordAPI.listener.Dispatcher.ListenerEvents.Presence_Update;
import com.neovisionaries.ws.client.WebSocket;
import com.neovisionaries.ws.client.WebSocketAdapter;
import com.neovisionaries.ws.client.WebSocketException;
import com.neovisionaries.ws.client.WebSocketFactory;
import org.json.simple.JSONObject;

import java.io.*;
import java.util.Objects;
import java.util.Timer;

public class Wss {
    static final Timer timer = new Timer();

    public static WebSocket connect(final BotImpl botImpl) throws IOException, WebSocketException {
        return new WebSocketFactory()
                .setConnectionTimeout(5000)
                .createSocket(DefaultLinks.WEBSOCKET)
                .addListener(new WebSocketAdapter() {
                    public void onTextMessage(WebSocket webSocket1, String message) {
                        JSONObject payload = (JSONObject) Objects.requireNonNull(ConvertJSON.convertToJSONOBJECT(message));
                        OpCodes opCodes = OpCodes.values()[Integer.parseInt(String.valueOf(payload.get("op")))];
                        switch (opCodes) {
                            case Dispatch:
                                System.out.println("MESSAGE RECIEVED: " + message);
                                String currentEvent = String.valueOf(payload.get("t"));
                                if (currentEvent.equals("PRESENCE_UPDATE")) {
                                    JSONObject d = (JSONObject) ConvertJSON.convertToJSONOBJECT(String.valueOf(payload.get("d")));
                                    JSONObject user = (JSONObject) ConvertJSON.convertToJSONOBJECT(String.valueOf(d.get("user")));
                                    UserData pd = new UserData(String.valueOf(user.get("id")), botImpl).logic();
                                    botImpl.getDispatcher().notify(new Presence_Update(botImpl));
                                } else if (currentEvent.equals("MESSAGE_CREATE")) {
                                    JSONObject d = (JSONObject) ConvertJSON.convertToJSONOBJECT(String.valueOf(payload.get("d")));
                                    JSONObject user = (JSONObject) ConvertJSON.convertToJSONOBJECT(String.valueOf(d.get("author")));
                                    UserData pd = new UserData(String.valueOf(user.get("id")), botImpl).logic();
                                    ChannelData cd = new ChannelData((String) d.get("channel_id"), botImpl).logic();
                                    DMessage mes = new DMessage(pd.getUser(), cd.getChannel(), Long.parseLong(String.valueOf(d.get("guild_id"))), String.valueOf(d.get("content")));
                                    botImpl.getDispatcher().notify(new Message_Create(botImpl, mes));
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
                                JSONObject d = (JSONObject) ConvertJSON.convertToJSONOBJECT(String.valueOf(payload.get("d")));
                                timer.schedule(new HeartBeat(webSocket1, botImpl), 0, Long.parseLong(String.valueOf(d.get("heartbeat_interval"))));
                                webSocket1.sendText(String.valueOf(botImpl.getIdentity()));
                                break;
                            case HeartBeat_ACK:
                                System.out.println("pulse");
                                break;
                        }
                    }
                }).connect();
    }
}
