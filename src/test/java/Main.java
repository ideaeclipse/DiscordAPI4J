
import DiscordAPI.DiscordBot;
import DiscordAPI.objects.DMessage;
import DiscordAPI.listener.dispatcher.listenerEvents.Message_Create;
import DiscordAPI.listener.dispatcher.TListener;

public class Main {
    private Main(String token, Long guildId) {
        DiscordBot bot = new DiscordBot(token, guildId).login();
        bot.getDispatcher().addListener((TListener<Message_Create>) a -> {
            DMessage message = a.getMessage();
            if (!message.getUser().getName().toLowerCase().equals("testing") && message.getChannel().getName().toLowerCase().equals("bot")) {
                StringBuilder builder = new StringBuilder();
                builder.append("Message Content: ").append(message.getContent()).append("\n").append("Message author: ").append(message.getUser().getName()).append("\n").append("Channel Name: ").append(message.getChannel().getName()).append("\n").append("Guild id: ").append(bot.getGuildId());
                message.getChannel().sendMessage(String.valueOf(builder));
            }

        });
    }

    public static void main(String[] args) {
        new Main(args[0], Long.parseUnsignedLong(args[1]));
    }
}
