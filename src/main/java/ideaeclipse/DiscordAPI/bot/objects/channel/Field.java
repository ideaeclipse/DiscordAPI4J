package ideaeclipse.DiscordAPI.bot.objects.channel;

import java.util.LinkedList;
import java.util.List;

/**
 * Used to parse response data
 *
 * <N>Field Title</N>
 * <V>Field Value</V>
 *
 * @author Ideaeclipse
 * @see IChannel#sendEmbed(List)
 */
public class Field {
    private final String name;
    private final String value;

    /**
     * create field
     *
     * @param name  field name
     * @param value field value
     */
    private Field(final String name, final String value) {
        this.name = name;
        this.value = value;
    }

    /**
     * @return field name
     */
    public String getName() {
        return name;
    }

    /**
     * @return field value
     */
    public String getValue() {
        return value;
    }

    /**
     * @param string string to parse data from
     * @return list of field objects
     * @see #getAll(String, String)
     */
    public static List<Field> parser(final String string) {
        List<Integer> startingN = getAll(string, "<N>");
        List<Integer> finishingN = getAll(string, "</N>");
        List<Integer> startingV = getAll(string, "<V>");
        List<Integer> finishingV = getAll(string, "</V>");
        List<Field> fields = new LinkedList<>();
        if (((startingN.size() == finishingN.size()) && (startingV.size() == finishingV.size())) && (startingN.size() == startingV.size())) {
            for (int i = 0; i < startingN.size(); i++) {
                fields.add(new Field(string.substring(startingN.get(i) + 3, finishingN.get(i)).trim(), string.substring(startingV.get(i) + 3, finishingV.get(i)).trim()));
            }
        }
        return fields;
    }

    /**
     * gets all the indexes of whatever string find from string
     *
     * @param string searchable string
     * @param find   string to find
     * @return list of indexes
     */
    private static List<Integer> getAll(final String string, final String find) {
        List<Integer> indexes = new LinkedList<>();
        int index = string.indexOf(find);
        while (index >= 0) {
            indexes.add(index);
            index = string.indexOf(find, index + 1);
        }
        return indexes;
    }

    @Override
    public String toString() {
        return "{Field} name: " + this.name + " value: " + this.value.replace("\n", "<br>");
    }
}
