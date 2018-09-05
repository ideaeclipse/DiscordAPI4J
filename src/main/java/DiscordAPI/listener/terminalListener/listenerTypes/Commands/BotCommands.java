package DiscordAPI.listener.terminalListener.listenerTypes.Commands;

import DiscordAPI.Terminal.Terminal;
import DiscordAPI.listener.terminalListener.listenerTypes.TerminalEvent;
import DiscordAPI.listener.terminalListener.listenerTypes.ListenerFeatures;

import java.util.List;
import java.util.Map;

public class BotCommands extends TerminalEvent implements ListenerFeatures {
    private Map map;
    private List list;
    private Class<?> d, a;
    private Terminal t;

    private BotCommands(Terminal t) {
        super(t);
    }

    public BotCommands(Terminal t, List l, Map m, Class<?> def, Class<?> admin) {
        this(t);
        this.t = t;
        this.list = l;
        this.map = m;
        this.d = def;
        this.a = admin;
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
        string = ClassInfo.genericCommands(string, t, d, a);
        return String.valueOf(string);
    }
}
