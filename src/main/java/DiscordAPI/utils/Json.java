package DiscordAPI.utils;

import org.eclipse.jetty.websocket.api.SuspendToken;

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

    public static List<String> asList(String message) {
        List<String> strings = new ArrayList<>();
        List<Integer> integers = new ArrayList<>();
        message = message.substring(1, message.length() - 1);
        if (message.contains(",")) {
            int index = message.indexOf(',');
            while (index >= 0) {
                integers.add(index);
                index = message.indexOf(',', index + 1);
            }
            for (int i = 0; i < integers.size(); i++) {
                if (i == integers.size() - 1 && i == 0) {
                    strings.add(message.substring(0, integers.get(i)).trim());
                    strings.add(message.substring(integers.get(i) + 1, message.length()).trim());
                } else if (i == integers.size() - 1) {
                    strings.add(message.substring(integers.get(i - 1), integers.get(i)).trim());
                    strings.add(message.substring(integers.get(i) + 1, message.length()).trim());
                } else if (i == 0) {
                    strings.add(message.substring(0, integers.get(i)).trim());
                } else {
                    strings.add(message.substring(integers.get(i - 1), integers.get(i)).trim());
                }
            }
        } else {
            strings.add(message);
        }
        return strings;
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
                try {
                    System.out.println(object);
                    throw new InvalidString();
                } catch (InvalidString invalidString) {
                    invalidString.printStackTrace();
                }
                return null;
            }
            if (object.charAt(object.length() - 1) == '}') {
                object = object.substring(0, object.length() - 1);
            } else {
                try {
                    throw new InvalidString();
                } catch (InvalidString invalidString) {
                    invalidString.printStackTrace();
                }
                return null;
            }
            map = splitString(convertToList(object));
            return map;

        }

        static List<String> convertToList(final String object) {
            //System.out.println("Formated String: " + object);
            List<Integer> indexs = test(object.toCharArray());
            List<String> strings = new ArrayList<>();
            if (indexs.size() > 0) {
                for (int i = 0; i < indexs.size(); i++) {
                    if (i == 0) {
                        strings.add(object.substring(0, indexs.get(i)).trim());
                    } else if (i == indexs.size() - 1) {
                        strings.add(object.substring(indexs.get(i) + 1, object.length()).trim());
                        strings.add(object.substring(indexs.get(i - 1) + 1, indexs.get(i)).trim());
                    } else {
                        strings.add(object.substring(indexs.get(i - 1) + 1, indexs.get(i)).trim());
                    }
                }
            } else {
                strings.add(object);
            }
            //System.out.println("LIST: " + strings);
            return strings;
        }

        static List<Integer> test(final char[] chars) {
            List<Integer> index = new ArrayList<>();
            boolean run = true;
            boolean array = true;
            int counter = 0;
            for (int i = 0; i < chars.length; i++) {
                if (chars[i] == '{') {
                    //System.out.println("{ " + counter);
                    if (counter == 0)
                        run = false;
                    counter++;
                } else if (chars[i] == '}') {
                    //System.out.println("} " + counter);
                    if (counter == 1)
                        run = true;
                    counter--;
                } else if (chars[i] == '[' && array) {
                    array = false;
                } else if (chars[i] == ']') {
                    array = true;
                } else if (chars[i] == ',' && run && array) {
                    //System.out.println("split");
                    index.add(i);
                }
            }
            //System.out.println(index);
            return index;
        }


        private Map<String, Object> splitString(List<String> strings) {
            //System.out.println("!!!!!!!! " + strings);
            Map<String, Object> map = new HashMap<>();
            for (String s : strings) {
                //System.out.println(s);
                List<String> list = new ArrayList<>();
                int index = s.indexOf(":");
                //System.out.println(index);
                list.add(s.substring(0, index).trim());
                list.add(s.substring(index + 1, s.length()).trim());
                //System.out.println(list);
                //System.out.println(list.get(1));
                if (list.get(1).startsWith("{")) {
                    //System.out.println(s + " " + list.get(1));
                    Map<String, Object> m = new ConvertToMap(list.get(1)).getMap();
                    //System.out.println("Test " + m);
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

    private static class InvalidString extends Exception {

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
                            map.get(s) instanceof ArrayList ||
                            map.get(s) instanceof Boolean ||
                            map.get(s) instanceof Json ||
                            map.get(s) instanceof Map ||
                            map.get(s) == null) ? "" : "\""
                    ).append((map.get(s) instanceof HashMap) ? convertToString((Map<String, Object>) map.get(s)) : map.get(s))
                    .append((map.get(s) instanceof Integer ||
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
