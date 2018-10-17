package ideaeclipse.DiscordAPI.terminal.listener.Commands;

import ideaeclipse.DiscordAPI.terminal.Terminal;
import ideaeclipse.DiscordAPI.listener.TerminalEvent;

import java.util.List;
import java.util.Map;

/**
 * This function is called when a user types cm help, the {@link #getReturn()} function returns all custom commands that are possible
 *
 * @author ideaeclipse
 */
public class BotCommands extends TerminalEvent {
    private Class<?> defaultClass, adminClass;
    private Terminal t;

    public BotCommands(final Terminal terminal, final Class<?> defaultClass, final Class<?> admin) {
        super(terminal);
        this.t = terminal;
        this.defaultClass = defaultClass;
        this.adminClass = admin;
    }

    public String getReturn() {
        StringBuilder string = new StringBuilder();
        string.append("***Custom Commands***").append("\n");
        for (String s : t.getCommandList().getCommandMap().keySet()) {
            string.append("Parent Commands: ").append(s).append("\n");
            for(Class<?> c: t.getCommandList().getCommandMap().get(s)){
                string.append("    -> Sub Commands: ").append(c.getSimpleName()).append("\n");
            }
        }
        string.append("To get help for a specific sub command and to see its options type help ${parent command} ${sub command}").append("\n");
        string = ClassInfo.genericCommands(string, t, defaultClass, adminClass);
        return String.valueOf(string);
    }
}
