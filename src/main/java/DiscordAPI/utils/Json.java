package DiscordAPI.utils;

import com.koloboke.collect.impl.hash.Hash;
import org.omg.PortableInterceptor.SYSTEM_EXCEPTION;

import java.util.*;

public class Json {
    private final Map<String, Object> map;
    private String output;
    List<Boolean> run;
    List<Integer> indexs;
    int index;
    String object;

    public Json(final String object) {
        this.object = object;
        System.out.println(object);
        map = convertToMap(object);
    }

    public void put(String key, Object value) {
        map.put(key, value);
    }

    public Object get(String key) {
        return map.get(key);
    }

    private Map<String, Object> convertToMap(String object) {
        index = 0;
        run = new ArrayList<>();
        indexs = new ArrayList<>();
        final Map<String, Object> map;
        object = object.replace("\"", "");

        if (object.charAt(0) == '{') {
            object = object.substring(1, object.length());
        } else {
            System.out.println("Invalid String");
        }
        if (object.charAt(object.length() - 1) == '}') {
            object = object.substring(0, object.length() - 1);
        } else {
            System.out.println("Invalid String");
        }
        this.object = object;
        //System.out.println("**" + object);
        map = splitString(convertToList(object));
        System.out.println(map);
        return map;
    }

    private List<String> convertToList(String object) {
        index = object.indexOf(',');
        while (index >= 0) {
            if (run.size() > 0) {
                for (int i = 0; i < run.size(); i++) {
                    run.set(i, test(run.get(i)));
                }
            } else {
                run.add(test(true));
            }
            index = object.indexOf(",", index + 1);
        }
        List<String> strings = new ArrayList<>();
        for (int i = 0; i < indexs.size(); i++) {
            if (i == 0) {
                strings.add(object.substring(0, indexs.get(i)));
            } else if (i == indexs.size() - 1) {
                strings.add(object.substring(indexs.get(i) + 1, object.length()));
                strings.add(object.substring(indexs.get(i - 1) + 1, indexs.get(i)));
            } else {
                strings.add(object.substring(indexs.get(i - 1) + 1, indexs.get(i)));
            }
        }
        return strings;
    }

    private Boolean test(boolean run) {
        System.out.println(object.charAt(index + 3) + " " + object.charAt(index - 1));
        if ((object.charAt(index + 3) != '{') && run) {
            System.out.println("No");
            indexs.add(index);
        } else if ((object.charAt(index - 1) == '}')) {
            System.out.println("End");
            run = Boolean.parseBoolean(null);
            indexs.add(index);
        } else if (run) {
            System.out.println("Start");
            run = false;
            indexs.add(index);
        }
        return run;
    }

    private Map<String, Object> splitString(List<String> strings) {
        System.out.println("*" + strings);
        Map<String, Object> map = new HashMap<>();
        for (String s : strings) {
            List<String> list = new ArrayList<>();
            int index = s.indexOf(":");
            list.add(s.substring(0, index));
            list.add(s.substring(index + 1, s.length()));
            System.out.println(list);
            if (list.get(1).startsWith("{")) {
                map.put(list.get(0), convertToMap(list.get(1)));
            } else {
                map.put(list.get(0), list.get(1));
            }
        }
        return map;
    }

    private String convertToString(Map<String, Object> map) {
        StringBuilder builder = new StringBuilder();
        builder.append("{");
        for (String s : map.keySet()) {
            builder.append("\"").append(s).append("\"").append(":").append("\"").append((map.get(s) instanceof HashMap) ? convertToString((Map<String, Object>) map.get(s)) : map.get(s)).append("\"").append(",");
        }
        builder.deleteCharAt(builder.length() - 1);
        builder.append("}");
        return builder.toString();
    }

    @Override
    public String toString() {
        System.out.println(convertToString(map));
        return output;
    }
}
