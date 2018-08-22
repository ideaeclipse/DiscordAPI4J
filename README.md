# DiscordAPI4J
Custom discordapi interpreter  <br />

Example of Creating the bot
```java
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
                if (!message.getDiscordUser().getName().equals(bot.getDiscordUser().getName()) && message.getChannel().getName().toLowerCase().equals("bot")) {
                    StringBuilder builder = new StringBuilder();
                    builder.append("Message Content: ").append(message.getContent()).append("\n").append("Message author: ").append(message.getDiscordUse().getName()).append("\n").append("Channel Name: ").append(message.getChannel().getName()).append("\n").append("Guild id: ").append(bot.getGuildId());
                    bot.createDmChannel(DiscordUtils.Search.USER(bot.getUsers(), "luminol")).sendMessage(String.valueOf(builder));
                }
            } else if (message.getChannel().getType().equals(Payloads.ChannelTypes.dmChannel)) {
                if (!message.getDiscordUse().getName().equals(bot.getUser().getName())) {
                    bot.setStatus(Payloads.GameTypes.Playing,"test");
                    message.getChannel().sendMessage("Dm received");
                }
            }

        });
    }

    public static void main(String[] args) {
        new Main(args[0], Long.parseUnsignedLong(args[1]));
    }
}

```
# Documentation
 * Some list
