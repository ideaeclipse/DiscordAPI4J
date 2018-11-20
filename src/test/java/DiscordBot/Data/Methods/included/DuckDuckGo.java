package DiscordBot.Data.Methods.included;

import ideaeclipse.DiscordAPI.terminal.CustomTerminal;
import ideaeclipse.DuckDuckGoAPI4j.Search;

import java.io.IOException;

public class DuckDuckGo extends CustomTerminal {

    public String search(String parameters) {
        try {
            Search search = new Search(parameters);
            System.out.println(search.getLink());
            return search.getLink() + "\\n" + search.getDescription();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void done() {

    }
}
