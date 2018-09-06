package DiscordAPI.listener.discordApiListener.listenerEvents;

import DiscordAPI.IPrivateBot;
import DiscordAPI.listener.discordApiListener.ApiEvent;
import DiscordAPI.objects.Parser;
import DiscordAPI.utils.Json;

public class Voice_State_Update extends ApiEvent {
    /**
     * @param b DiscordBot {@link DiscordAPI.objects.DiscordBot}
     */
    public Voice_State_Update(final IPrivateBot b, final Json payload) {
        super(b);
        Parser.VoiceStateUpdate voiceStateUpdate = new Parser.VoiceStateUpdate(b, payload);
    }
}
