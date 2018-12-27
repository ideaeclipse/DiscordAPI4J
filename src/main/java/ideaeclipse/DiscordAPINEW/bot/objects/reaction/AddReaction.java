package ideaeclipse.DiscordAPINEW.bot.objects.reaction;

import emoji4j.EmojiUtils;
import ideaeclipse.DiscordAPINEW.bot.IPrivateBot;
import ideaeclipse.DiscordAPINEW.bot.objects.channel.IChannel;
import ideaeclipse.DiscordAPINEW.utils.Util;
import ideaeclipse.DiscordAPINEW.utils.annotations.JsonValidity;
import ideaeclipse.JsonUtilities.Json;
import ideaeclipse.reflectionListener.Event;

public class AddReaction extends Event {
    private final IReaction reaction;

    public AddReaction(@JsonValidity({"emoji", "channel_id", "message_id"}) final Json json, final IPrivateBot bot) {
        String s = String.valueOf(Util.check(this, "getEmojiName", new Json(String.valueOf(json.get("emoji")))).getObject());
        IChannel channel = bot.getChannels().getByK1(Long.parseUnsignedLong(String.valueOf(json.get("channel_id"))));
        this.reaction = new Reaction(EmojiUtils.shortCodify(s), s
                , channel
                , channel.getMessageHistory().get(Long.parseUnsignedLong(String.valueOf(json.get("message_id")))));
        bot.getReactions().putIfAbsent(this.reaction.getCode(), this.reaction);
    }

    private String getEmojiName(@JsonValidity("name") final Json json) {
        return String.valueOf(json.get("name"));
    }

    public IReaction getReaction() {
        return reaction;
    }
}
