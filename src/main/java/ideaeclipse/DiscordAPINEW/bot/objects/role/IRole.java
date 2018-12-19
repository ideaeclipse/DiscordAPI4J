package ideaeclipse.DiscordAPINEW.bot.objects.role;

import com.fasterxml.jackson.annotation.JsonFormat;

@JsonFormat
/**
 * {
 *   "color": 0,
 *   "managed": true,
 *   "permissions": 262216,
 *   "name": "Testing",
 *   "mentionable": false,
 *   "position": 1,
 *   "id": "472196871884898304",
 *   "hoist": false
 * }
 */
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
