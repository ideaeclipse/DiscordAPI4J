package ideaeclipse.DiscordAPI.customTerminal;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

class ParseInput {
    private String input;

    ParseInput(String string) {
        this.input = string;
    }

    List<String> getWords(List<String> sl) {
        if (input.contains("'") && sl.size() == 0) {
            sl = new LinkedList<>(Arrays.asList(input.split("'")));
            return getWords(sl);
        } else {
            if (input.contains("'")) {
                String last = sl.get(sl.size() - 1);
                sl = new LinkedList<>(Arrays.asList(sl.get(0).split(" ")));
                sl.add(last);
                return sl;
            } else {
                return new LinkedList<>(Arrays.asList(input.split(" ")));
            }
        }
    }
}
