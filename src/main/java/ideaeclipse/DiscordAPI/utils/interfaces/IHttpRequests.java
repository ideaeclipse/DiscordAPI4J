package ideaeclipse.DiscordAPI.utils.interfaces;

import ideaeclipse.JsonUtilities.Json;

import java.io.IOException;

/**
 * Interface that allows access to http commands
 *
 * @author Ideaeclipse
 * @see ideaeclipse.DiscordAPI.utils.Util.HttpRequests
 * @see ideaeclipse.DiscordAPI.utils.Util
 */
public interface IHttpRequests {
    /**
     * Get command
     * @param url connection url
     * @return return values if any normal a json string or jsonarray
     */
    Object get(final String url) throws IOException;

    /**
     * Post command
     * @param url connection url
     */
    void post(final String url) throws IOException;

    /**
     * Put command
     * @param url connection url
     */
    void put(final String url) throws IOException;

    /**
     * Delete command
     * @param url connection url
     */
    void delete(final String url) throws IOException;

    /**
     * Send a json string as a post connection
     * @param url connection url
     * @param object json object
     * @return return values if any
     */
    Object sendJson(final String url, final Json object) throws IOException;

    /**
     * Send a file as a post connection
     * @param url connection url
     * @param filename filename
     * @return return values if any
     */
    Object sendFile(final String url, final String filename) throws IOException;
}
