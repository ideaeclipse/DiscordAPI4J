package DiscordAPI.utils;

import java.util.*;

public class Json {
    private static final DiscordLogger logger = new DiscordLogger(String.valueOf(Json.class));
    private final Map<String, Object> map;

    public Json() {
        map = new HashMap<>();
    }

    public Json(final String object) {
        if (object.charAt(0) == '[') {
            logger.error("Wrong type use JsonArray");
        }
        ConvertToMap convert = new ConvertToMap(object);
        map = convert.getMap();
    }

    public Json(final Map<String, Object> map) {
        this.map = map;
    }

    public void put(String key, Object value) {
        map.put(key, value);
    }

    public Object get(String key) {
        Object o = map.get(key);
        if (o instanceof HashMap) {
            return convertToString((Map<String, Object>) o);
        } else {
            return o;
        }
    }

    public Map<String, Object> getMap() {
        return map;
    }

    public Set<String> keySet() {
        return map.keySet();
    }

    static class ConvertToMap {
        private Map<String, Object> map;

        ConvertToMap(final String object) {
            map = convert(object.replace("\n", "\\n"));
        }

        Map<String, Object> getMap() {
            return map;
        }

        private Map<String, Object> convert(String object) {
            Map<String, Object> map;
            object = object.replace("\"", "").trim();
            if (object.charAt(0) == '{') {
                object = object.substring(1, object.length());
            } else {
                return null;
            }
            if (object.charAt(object.length() - 1) == '}') {
                object = object.substring(0, object.length() - 1);
            } else {
                return null;
            }
            map = splitString(convertToList(object));
            return map;

        }

        static Iterator<String> convertToList(final String object) {
            Iterator<Integer> indexes = toIndexes(object.toCharArray());
            List<String> strings = new ArrayList<>();
            int current, previous = 0;
            if (indexes.hasNext()) {
                while (indexes.hasNext()) {
                    current = indexes.next();
                    if (previous == 0) {
                        strings.add(object.substring(previous, current));
                        previous = current + 1;
                        continue;
                    }
                    strings.add(object.substring(previous, current));
                    if (!indexes.hasNext()) {
                        strings.add(object.substring(current + 1, object.length()));
                    }
                    previous = current + 1;
                }
            } else {
                strings.add(object);
            }
            return strings.iterator();
        }

        static Iterator<Integer> toIndexes(final char[] chars) {
            List<Integer> index = new ArrayList<>();
            boolean run = true;
            boolean array = true;
            int counter = 0;
            for (int i = 0; i < chars.length; i++) {
                if (chars[i] == '{') {
                    if (counter == 0)
                        run = false;
                    counter++;
                } else if (chars[i] == '}') {
                    if (counter == 1)
                        run = true;
                    counter--;
                } else if (chars[i] == '[' && array) {
                    array = false;
                } else if (chars[i] == ']') {
                    array = true;
                } else if (chars[i] == ',' && run && array) {
                    index.add(i);
                }
            }
            return index.iterator();
        }


        private Map<String, Object> splitString(final Iterator<String> strings) {
            Map<String, Object> map = new HashMap<>();
            while (strings.hasNext()) {
                String s = strings.next();
                List<String> list = new ArrayList<>();
                int index = s.indexOf(":");
                list.add(s.substring(0, index).trim());
                list.add(s.substring(index + 1, s.length()).trim());
                if (list.get(1).startsWith("{")) {
                    Map<String, Object> m = new ConvertToMap(list.get(1)).getMap();
                    map.put(list.get(0), m);
                } else {
                    if (list.get(1).contains("null")) {
                        map.put(list.get(0), null);
                    } else if (list.get(1).contains("false")) {
                        map.put(list.get(0), false);
                    } else if (list.get(1).contains("true")) {
                        map.put(list.get(1), true);
                    } else {
                        map.put(list.get(0), list.get(1));
                    }
                }
            }
            return map;
        }
    }

    private String convertToString(Map<String, Object> map) {
        StringBuilder builder = new StringBuilder();
        builder.append("{");
        for (String s : map.keySet()) {
            builder.append("\"")
                    .append(s)
                    .append("\"")
                    .append(":")
                    .append((map.get(s) instanceof Integer ||
                            map.get(s) instanceof Long ||
                            map.get(s) instanceof ArrayList ||
                            map.get(s) instanceof Boolean ||
                            map.get(s) instanceof Json ||
                            map.get(s) instanceof Map ||
                            map.get(s) == null) ? "" : "\""
                    ).append((map.get(s) instanceof HashMap) ? convertToString((Map<String, Object>) map.get(s)) : map.get(s))
                    .append((map.get(s) instanceof Integer ||
                            map.get(s) instanceof Long ||
                            map.get(s) instanceof ArrayList ||
                            map.get(s) instanceof Boolean ||
                            map.get(s) instanceof Json ||
                            map.get(s) instanceof Map ||
                            map.get(s) == null) ? "" : "\"")
                    .append(",");
        }
        builder.deleteCharAt(builder.length() - 1);
        builder.append("}");
        return builder.toString();
    }

    @Override
    public String toString() {
        return convertToString(map);
    }
}
