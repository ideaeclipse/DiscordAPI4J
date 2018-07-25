import DiscordAPI.Bot.BotImpl;
import DiscordAPI.Objects.DChannel;
import DiscordAPI.Objects.DMessage;
import DiscordAPI.Objects.DUser;
import DiscordAPI.listener.Dispatcher.ListenerEvents.Message_Create;
import DiscordAPI.listener.Dispatcher.TListener;

public class Main {
    private Main() {
        BotImpl botImpl = new BotImpl("NDcxMTAzNTg5MTM4MzAwOTM4.DjkMvQ.JMaiAZ9_h6t-P7N248SscLFij1w", Long.parseUnsignedLong("300032525382713344")).login();
        botImpl.getDispatcher().addListener((TListener<Message_Create>) a -> {
            DMessage message = a.getMessage();
            if (message.getUser().getName().toLowerCase().equals("luminol")) {
                StringBuilder builder = new StringBuilder();
                builder.append("Message Content: ").append(message.getContent()).append("\n").append("Message author: ").append(message.getUser().getName()).append("\n").append("Channel Name: ").append(message.getChannel().getName()).append("\n").append("Guild id: ").append(botImpl.getGuildId());
                message.getChannel().sendMessage(String.valueOf(builder));
            }
        });
    }

    public static void main(String[] args) {
        new Main();
    }
}
