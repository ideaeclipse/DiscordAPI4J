package ideaeclipse.DiscordAPINEW.utils.interfaces;

import ideaeclipse.JsonUtilities.Json;

/**
 * Interface that allows access to http commands
 *
 * @author Ideaeclipse
 * @see ideaeclipse.DiscordAPINEW.utils.Util.HttpRequests
 * @see ideaeclipse.DiscordAPINEW.utils.Util
 */
public interface IHttpRequests {
    /**
     * Get command
     * @param url connection url
     * @return return values if any normal a json string or jsonarray
     */
    Object get(final String url);

    /**
     * Post command
     * @param url connection url
     */
    void post(final String url);

    /**
     * Put command
     * @param url connection url
     */
    void put(final String url);

    /**
     * Delete command
     * @param url connection url
     */
    void delete(final String url);

    /**
     * Send a json string as a post connection
     * @param url connection url
     * @param object json object
     * @return return values if any
     */
    Object sendJson(final String url, final Json object);

    /**
     * Send a file as a post connection
     * @param url connection url
     * @param filename filename
     * @return return values if any
     */
    Object sendFile(final String url, final String filename);
}
