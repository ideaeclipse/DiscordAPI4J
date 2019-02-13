package ideaeclipse.DiscordAPI.bot.objects.reaction;

import com.fasterxml.jackson.annotation.JsonFormat;
import ideaeclipse.DiscordAPI.bot.IDiscordBot;
import ideaeclipse.DiscordAPI.bot.objects.channel.IChannel;
import ideaeclipse.DiscordAPI.utils.Util;
import ideaeclipse.DiscordAPI.utils.annotations.JsonValidity;
import ideaeclipse.JsonUtilities.Json;
import ideaeclipse.reflectionListener.parents.Event;

@JsonFormat
/**
 * This class is used to remove a reaction from a message in {@link IChannel#getMessageHistory()}
 *
 * Channel is parsed by {@link IDiscordBot#getChannels()}
 * Message is pared by {@link IChannel#getMessageHistory()}
 * {
 *   "emoji": {
 *     "name": "yeet",
 *     "animated": false,
 *     "id": "504817998087979009"
 *   },
 *   "user_id": "304408618986504195",
 *   "guild_id": "332064083283017728",
 *   "message_id": "533760092282028053",
 *   "channel_id": "531540409097256978"
 * }
 *
 * Called when a payload with the identifier {@link ideaeclipse.DiscordAPI.webSocket.DispatchType#MESSAGE_REACTION_REMOVE}
 * @author Ideaeclipse
 * @see Reaction
 * @see IReaction
 * @see ideaeclipse.DiscordAPI.bot.DiscordBot
 */
public final class RemoveReaction extends Event {
    private final IReaction reaction;

    /**
     * Uses {@link AddReaction} to parse the reaction data and get the reaction object
     * removes reaction from the map of reactions for the particular message
     *
     * @param json json payload
     * @param bot  bot
     */
    private RemoveReaction(@JsonValidity({"emoji", "channel_id", "message_id"}) final Json json, final IDiscordBot bot) {
        this.reaction = Util.checkConstructor(AddReaction.class, json, bot).getObject().getReaction();
        this.reaction.getChannel().getMessageHistory().get(this.reaction.getMessage().getId()).removeReaction(this.reaction.getCode());
    }

    /**
     * @return reaction instance
     */
    public IReaction getReaction() {
        return reaction;
    }
}
