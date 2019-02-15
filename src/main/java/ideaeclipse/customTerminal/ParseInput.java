package ideaeclipse.customTerminal;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * This class takes a string and parses into a list
 * <p>
 * you can also put single quotes around something and it will count as a single word until you end quote
 * i.e
 * I am a developer => [I, am, a, developer]
 * I 'am a developer' => [I, am a developer]
 *
 * @author Ideaeclipse
 */
class ParseInput {
    private final String input;

    /**
     * @param string string to parse
     */
    ParseInput(final String string) {
        this.input = string;
    }

    /**
     * Recursive algorithm to parse string
     *
     * @param sl {@link LinkedList} new list
     * @return list of words
     */
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
