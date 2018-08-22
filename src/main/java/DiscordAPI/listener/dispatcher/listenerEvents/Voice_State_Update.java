package DiscordAPI.listener.dispatcher.listenerEvents;

import DiscordAPI.IDiscordBot;
import DiscordAPI.listener.listenerTypes.ListenerEvent;
import DiscordAPI.objects.Parser;
import DiscordAPI.utils.Json;

public class Voice_State_Update extends ListenerEvent {
    /**
     * @param b DiscordBot {@link DiscordAPI.objects.DiscordBot}
     */
    public Voice_State_Update(final IDiscordBot b, final Json payload) {
        super(b);
        Parser.VoiceStateUpdate voiceStateUpdate = new Parser.VoiceStateUpdate(b, payload);
    }
}
