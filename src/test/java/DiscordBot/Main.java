package DiscordBot;

import DiscordAPI.IDiscordBot;
import DiscordAPI.objects.DiscordBotBuilder;


public class Main {
    public static IDiscordBot bot;

    private Main(String token, Long guildId) {
        bot = new DiscordBotBuilder(token, guildId).login();
        /*
        bot.getDispatcher().addListener((IListener<Message_Create>) a -> {
            IMessage message = a.getMessage();
            if (message.getChannel().getType().equals(Payloads.ChannelTypes.textChannel)) {
                if (!message.getUser().getName().equals(bot.getBotUser().getName()) && message.getChannel().getName().toLowerCase().equals("bot")) {
                    StringBuilder builder = new StringBuilder();
                    builder.append("Test").append("\n").append("New Line");
                    bot.createDmChannel(DiscordUtils.Search.USER(bot.getUsers(), "luminol")).sendMessage(String.valueOf(builder));
                }
            } else if (message.getChannel().getType().equals(Payloads.ChannelTypes.dmChannel)) {
                if (!message.getUser().getName().equals(bot.getBotUser().getName())) {
                    bot.setStatus(Payloads.GameTypes.Listening, "Some music");
                    message.getChannel().sendMessage("Dm received");
                }
            }

        });
        */
    }

    public static void main(String[] args) {
        new Main(args[0], Long.parseUnsignedLong(args[1]));
    }
}
