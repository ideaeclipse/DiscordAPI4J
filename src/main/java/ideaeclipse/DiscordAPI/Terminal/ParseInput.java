package ideaeclipse.DiscordAPI.terminal;

import java.util.ArrayList;
import java.util.Arrays;

class ParseInput {
    private String input;

    ParseInput(String string) {
        this.input = string;
    }

    ArrayList<String> getWords(ArrayList<String> sl) {
        if (input.contains("'") && sl.size() == 0) {
            sl = new ArrayList<>(Arrays.asList(input.split("'")));
            return getWords(sl);
        } else {
            if (input.contains("'")) {
                String last = sl.get(sl.size() - 1);
                sl = new ArrayList<>(Arrays.asList(sl.get(0).split(" ")));
                sl.add(last);
                return sl;
            } else {
                return new ArrayList<>(Arrays.asList(input.split(" ")));
            }
        }
    }
}
