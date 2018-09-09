package DiscordAPI.listener.terminalListener.listenerTypes.Commands;

import DiscordAPI.Terminal.Terminal;
import DiscordAPI.listener.terminalListener.listenerTypes.TerminalEvent;
import DiscordAPI.listener.terminalListener.listenerTypes.ListenerFeatures;

import java.util.List;
import java.util.Map;

/**
 * This function is called when a user types cm help, the {@link #getReturn()} function returns all custom commands that are possible
 *
 * @author ideaeclipse
 */
public class BotCommands extends TerminalEvent implements ListenerFeatures {
    private Map map;
    private List list;
    private Class<?> defaultClass, adminClass;
    private Terminal t;

    private BotCommands(final Terminal t) {
        super(t);
    }

    public BotCommands(final Terminal terminal, final List list, final Map map, final Class<?> defaultClass, final Class<?> admin) {
        this(terminal);
        this.t = terminal;
        this.list = list;
        this.map = map;
        this.defaultClass = defaultClass;
        this.adminClass = admin;
    }

    @Override
    public String getReturn() {
        StringBuilder string = new StringBuilder();
        string.append("***Custom Commands***").append("\n");
        for (int i = 0; i < map.size(); i++) {
            string.append("Parent Commands: ").append(list.get(i)).append("\n");
            string.append("    -> Sub Commands: ").append(map.get(list.get(i))).append("\n");
        }
        string.append("To get help for a specific sub command and to see its options type help ${parent command} ${sub command}").append("\n");
        string = ClassInfo.genericCommands(string, t, defaultClass, adminClass);
        return String.valueOf(string);
    }
}
