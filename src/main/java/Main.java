
import DiscordAPI.DiscordBot;
import DiscordAPI.Objects.DMessage;
import DiscordAPI.Objects.DRole;
import DiscordAPI.WebSocket.Utils.Search;
import DiscordAPI.listener.Dispatcher.ListenerEvents.Message_Create;
import DiscordAPI.listener.Dispatcher.ListenerEvents.Presence_Update;
import DiscordAPI.listener.Dispatcher.TListener;

import java.util.Objects;

public class Main {
    private Main(String token, Long guildId) {
        DiscordBot bot = new DiscordBot(token, guildId).login();
        bot.getDispatcher().addListener((TListener<Message_Create>) a -> {
            DMessage message = a.getMessage();
            if(!message.getUser().getName().toLowerCase().equals("testing")) {
                StringBuilder builder = new StringBuilder();
                builder.append("Message Content: ").append(message.getContent()).append("\n").append("Message author: ").append(message.getUser().getName()).append("\n").append("Channel Name: ").append(message.getChannel().getName()).append("\n").append("Guild id: ").append(bot.getGuildId());
                message.getChannel().sendMessage(String.valueOf(builder));
            }
            if(message.getContent().equals("audio")){
                bot.getAudioManager().initialize(Objects.requireNonNull(Search.CHANNEL(bot.getChannels(), "General")).getId());
            }

        });
        bot.getDispatcher().addListener((TListener<Presence_Update>) a -> {
        });
    }

    public static void main(String[] args) {
        new Main(args[0], Long.parseUnsignedLong(args[1]));
    }
}
