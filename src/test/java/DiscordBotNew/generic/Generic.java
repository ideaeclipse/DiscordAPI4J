package DiscordBotNew.generic;

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
        builder.append("<N>Available Roles</N>").append("<V>");
        for (String string : bot.getRoles().getK2VMap().keySet()) {
            if (!cantAdd.contains(string))
                builder.append("`").append(string).append("`").append("\n");
        }
        builder.append("</V>");
        builder.append("<N>Examples</N>").append("<V>");
        builder.append("Role names are case sensitive").append("\n");
        builder.append("`!addRole $roleName`").append("\n");
        builder.append("`!addRole Computer-Science`").append("</V>");
        return String.valueOf(builder);
    }

    @Executable
    public String myRoles(final IMessage message) {
        StringBuilder builder = new StringBuilder();
        builder.append("<N>Your Roles</N>");
        builder.append("<V>You are a member of:\n");
        int total = 0;
        for (Map.Entry<Long, IRole> entry : message.getUser().getRoles().getK1VMap().entrySet()) {
            IRole role = entry.getValue();
            if (!cantAdd.contains(role.getName())) {
                builder.append('`').append(role.getMention()).append('`').append("\n");
                total++;
            }
        }
        if (total == 0)
            builder.append("You are not a member of any roles");
        builder.append("</V>");
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
