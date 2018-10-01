package DiscordAPI.objects;

import DiscordAPI.objects.Interfaces.IUser;
import DiscordAPI.utils.RateLimitRecorder;
import ideaeclipse.JsonUtilities.Json;
import ideaeclipse.JsonUtilities.Parser;

import static DiscordAPI.utils.DiscordUtils.DefaultLinks.*;

public interface IDiscordUser {
    Long getId();

    String getName();

    Integer getDiscriminator();

    static IUser getServerUniqueUser(final IDiscordUser user) {
        Json json = new Json(String.valueOf(rateLimitRecorder.queue(new RateLimitRecorder.QueueHandler.HttpEvent(RateLimitRecorder.QueueHandler.RequestTypes.get, GUILD + bot.getGuildId() + MEMBER + "/" + user.getId()))));
        User.ServerUniqueUserP s = ParserObjects.convertToPayload(json, User.ServerUniqueUserP.class);
        return s.getServerUniqueUser();
    }
}
