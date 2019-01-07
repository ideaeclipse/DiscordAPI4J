package ideaeclipse.DiscordAPI.bot.objects.reaction;

import ideaeclipse.DiscordAPI.bot.IPrivateBot;
import ideaeclipse.DiscordAPI.utils.Util;
import ideaeclipse.DiscordAPI.utils.annotations.JsonValidity;
import ideaeclipse.JsonUtilities.Json;
import ideaeclipse.reflectionListener.Event;

public class RemoveReaction extends Event {
    private final IReaction reaction;

    public RemoveReaction(@JsonValidity({"emoji", "channel_id", "message_id"}) final Json json, final IPrivateBot bot) {
        this.reaction = Util.checkConstructor(AddReaction.class, json, bot).getObject().getReaction();
        this.reaction.getChannel().getMessageHistory().get(this.reaction.getMessage().getId()).removeReaction(this.reaction.getCode());
    }

    public IReaction getReaction() {
        return reaction;
    }
}
