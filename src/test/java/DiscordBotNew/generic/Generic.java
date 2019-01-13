package DiscordBotNew.generic;

import DiscordBotNew.main;
import ideaeclipse.DiscordAPI.bot.IDiscordBot;
import ideaeclipse.DiscordAPI.bot.objects.message.IMessage;
import ideaeclipse.DiscordAPI.bot.objects.role.IRole;
import ideaeclipse.customTerminal.Executable;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class Generic {
    private final IDiscordBot bot;
    private final List<String> cantAdd = Arrays.asList("Admin", "Moderator", "Bot", "@everyone");

    public Generic(final IDiscordBot bot) {
        this.bot = bot;
    }

    @Executable
    public String listRoles() {
        StringBuilder builder = new StringBuilder();
        builder.append("**Available Roles**").append("\n");
        for (String string : bot.getRoles().getK2VMap().keySet()) {
            if (!cantAdd.contains(string))
                builder.append("    ").append(string).append("\n");
        }
        builder.append("**Examples**:").append("\n");
        builder.append("    ").append("Role names are case sensitive").append("\n");
        builder.append("    ").append("!addRole $roleName").append("\n");
        builder.append("    ").append("!addRole computer-science").append("\n");
        return String.valueOf(builder);
    }

    @Executable
    public String myRoles(final IMessage message) {
        StringBuilder builder = new StringBuilder();
        builder.append("You are a member of [");
        for (Map.Entry<Long, IRole> entry : message.getUser().getRoles().getK1VMap().entrySet()) {
            IRole role = entry.getValue();
            if (!cantAdd.contains(role.getName()))
                builder.append(role.getMention()).append(", ");
        }
        if (builder.charAt(builder.length() - 1) != '[')
            builder.setLength(builder.length() - 2);
        builder.append("]");
        return String.valueOf(builder);
    }

    @Executable
    public String addRole(final IMessage message, final String roleName) {
        if (!cantAdd.contains(roleName)) {
            if (bot.getRoles().getByK2(roleName) != null) {
                IRole role = bot.getRoles().getByK2(roleName);
                if (message.getUser().getRoles().containsValue(role)) {
                    message.getUser().removeRole(role);
                    return message.getUser().getMention() + " left **" + roleName + "**";

                } else {
                    message.getUser().addRole(role);
                    return message.getUser().getMention() + " joined **" + roleName + "**";
                }
            } else
                return "That role doesn't exist you !listRoles for available roles";
        } else
            return "You cannot join that role";
    }
}
