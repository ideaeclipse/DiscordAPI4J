package DiscordAPI.listener.dispatcher.listenerEvents;

import DiscordAPI.IDiscordBot;
import DiscordAPI.listener.listenerTypes.ListenerEvent;
import DiscordAPI.objects.Parser;
import DiscordAPI.utils.Json;

import java.util.concurrent.Callable;

public class Voice_Server_Update extends ListenerEvent {
    /**
     * @param b DiscordBot {@link DiscordAPI.objects.DiscordBot}
     */
    public Voice_Server_Update(final IDiscordBot b, final Json payload) {
        super(b);
        Parser.VoiceServerUpdate voiceServerUpdate = new Parser.VoiceServerUpdate(b, payload);
    }
}
