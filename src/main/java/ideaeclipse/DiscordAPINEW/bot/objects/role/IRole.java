package ideaeclipse.DiscordAPINEW.bot.objects.role;

public interface IRole {
    int getColour();

    boolean isManaged();

    int getPermissionValue();

    String getName();

    boolean isMentionable();

    int getPosition();

    long getId();

    boolean isHoist();
}
