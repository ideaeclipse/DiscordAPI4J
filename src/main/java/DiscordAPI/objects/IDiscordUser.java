package DiscordAPI.objects;

import DiscordAPI.objects.Interfaces.IUser;
import DiscordAPI.utils.Json;
import DiscordAPI.utils.RateLimitRecorder;

import static DiscordAPI.utils.DiscordUtils.DefaultLinks.*;

public interface IDiscordUser {
    Long getId();

    String getName();

    Integer getDiscriminator();

    static IUser getServerUniqueUser(final IDiscordUser user) {
        Json json = new Json(String.valueOf(rateLimitRecorder.queue(new RateLimitRecorder.QueueHandler.HttpEvent(RateLimitRecorder.QueueHandler.RequestTypes.get, GUILD + bot.getGuildId() + MEMBER + "/" + user.getId()))));
        User.ServerUniqueUserP s = Parser.convertToPayload(json, User.ServerUniqueUserP.class);
        return s.getServerUniqueUser();
    }
}
