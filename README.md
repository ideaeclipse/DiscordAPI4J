# DiscordAPI4J
Custom discordapi interpreter  <br />

Example of Creating the bot
```java
import DiscordAPI.IDiscordBot;
import Message_Create;
import TListener;
import DiscordAPI.objects.DiscordBotBuilder;
import DiscordAPI.objects.Payloads;
import DiscordAPI.utils.DiscordUtils;

public class DiscordBot.Main {
    private DiscordBot.Main(String token, Long guildId) {
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
        new DiscordBot.Main(args[0], Long.parseUnsignedLong(args[1]));
    }
}

```
# Configuration
 * adminGroup=admin
**debug=true
**adminUser=luminol
**useTerminal=true
**genericDirectory=DiscordBot.Data.Generic
**adminFileDir=adminCommands
**commandsDirectory=DiscordBot.Data.Methods
**defaultFileDirectory=defaultCommands
