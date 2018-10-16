package DiscordBot.Data.Methods.search;

import ideaeclipse.DuckDuckGoAPI4j.Search;

import java.io.IOException;

public class DuckDuckGo {
    public DuckDuckGo() {

    }

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
}
