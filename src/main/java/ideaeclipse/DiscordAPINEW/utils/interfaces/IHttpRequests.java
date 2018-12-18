package ideaeclipse.DiscordAPINEW.utils.interfaces;

import ideaeclipse.JsonUtilities.Json;

public interface IHttpRequests {
    Object get(final String url);

    void post(final String url);

    Object sendJson(final String url, final Json object);

    Object sendFile(final String url,final String filename);
}
