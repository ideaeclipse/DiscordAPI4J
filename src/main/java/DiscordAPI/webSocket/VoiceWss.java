package DiscordAPI.webSocket;

import DiscordAPI.IDiscordBot;
import com.neovisionaries.ws.client.WebSocketFactory;

import java.util.concurrent.Callable;

public class VoiceWss extends WebSocketFactory implements Callable<Boolean> {
    public VoiceWss(final IDiscordBot bot) {

    }

    @Override
    public Boolean call() throws Exception {
        return null;
    }
}
