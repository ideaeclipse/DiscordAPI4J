package ideaeclipse.DiscordAPI.listener.discordApiListener;

import ideaeclipse.DiscordAPI.IPrivateBot;
import ideaeclipse.DiscordAPI.objects.Interfaces.IReaction;
import ideaeclipse.DiscordAPI.objects.ParserObjects;
import ideaeclipse.JsonUtilities.Json;
import ideaeclipse.reflectionListener.Event;

public class Reaction_Add extends Event {
    private final IReaction reaction;

    public Reaction_Add(final IPrivateBot b, final Json json) {
        this.reaction = new ParserObjects.ReactionAdd(b, json).getReaction();
    }

    public IReaction getReaction() {
        return reaction;
    }
}
