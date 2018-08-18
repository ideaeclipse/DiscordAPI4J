import DiscordAPI.IDiscordBot;
import DiscordAPI.listener.dispatcher.listenerEvents.Message_Create;
import DiscordAPI.listener.dispatcher.TListener;
import DiscordAPI.objects.DiscordBotBuilder;
import DiscordAPI.objects.Message;
import DiscordAPI.objects.Payloads;
import DiscordAPI.utils.DiscordUtils;


public class Main {
    private Main(String token, Long guildId) {
        IDiscordBot bot = new DiscordBotBuilder(token, guildId).login();
        bot.getDispatcher().addListener((TListener<Message_Create>) a -> {
            Message message = a.getMessage();
            if (message.getChannel().getType().equals(Payloads.ChannelTypes.textChannel)) {
                if (!message.getUser().getName().equals(bot.getUser().getName()) && message.getChannel().getName().toLowerCase().equals("bot")) {
                    StringBuilder builder = new StringBuilder();
                    builder.append("Test").append("\n").append("New Line");
                    bot.createDmChannel(DiscordUtils.Search.USER(bot.getUsers(), "luminol")).sendMessage(String.valueOf(builder));
                }
            } else if (message.getChannel().getType().equals(Payloads.ChannelTypes.dmChannel)) {
                if (!message.getUser().getName().equals(bot.getUser().getName())) {
                    bot.setStatus(Payloads.GameTypes.Listening, "Big Dicks");
                    message.getChannel().sendMessage("Dm received");
                }
            }

        });
    }

    public static void main(String[] args) {
        new Main(args[0], Long.parseUnsignedLong(args[1]));
    }
}
