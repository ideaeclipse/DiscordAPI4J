package ideaeclipse.DiscordAPI.bot.objects.serverCommands;

import ideaeclipse.DiscordAPI.bot.IDiscordBot;
import ideaeclipse.DiscordAPI.bot.DiscordBot;
import ideaeclipse.DiscordAPI.bot.objects.presence.UserStatus;
import ideaeclipse.DiscordAPI.bot.objects.presence.game.GameType;
import ideaeclipse.customTerminal.CommandsClass;
import ideaeclipse.customTerminal.Executable;

/**
 * ConsoleCommands that can only be executed from the console.
 *
 * @author Ideaeclipse
 * @see DiscordBot
 */
public class ConsoleCommands implements CommandsClass {
    private final IDiscordBot bot;

    public ConsoleCommands(final IDiscordBot bot) {
        this.bot = bot;
    }

    /**
     * Sets the presence of bot
     * @param message message to set, will say Playing 'message'
     * @return Presence Updated, if error it will come up in the console as an error
     */
    @Executable
    public String setPresence(final String message) {
        this.bot.setStatus(GameType.playing, message, UserStatus.online);
        return "Presence Updated";
    }
    @Executable
    public void shutdown(){
        System.exit(0);
    }
}
