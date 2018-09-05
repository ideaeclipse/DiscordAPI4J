package DiscordAPI.Terminal.Commands;

import DiscordAPI.IDiscordBot;
import DiscordAPI.IPrivateBot;
import DiscordAPI.utils.DiscordLogger;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Modifier;
import java.net.URL;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.zip.ZipFile;

public class CommandList {
    private static final DiscordLogger LOGGER = new DiscordLogger(CommandList.class.getName());
    private HashMap<String, List> commands;
    private HashMap<String, List> commandMethods;
    private Class<?> defaultCommands, adminCommands;

    public CommandList(final IDiscordBot bot) throws IOException, ClassNotFoundException {
        commands = new HashMap<>();
        commandMethods = new HashMap<>();
        defaultCommands = getClass(bot.getProperties().getProperty("genericDirectory") + "." + bot.getProperties().getProperty("defaultFileDirectory"));
        adminCommands = getClass(bot.getProperties().getProperty("genericDirectory") + "." + bot.getProperties().getProperty("adminFileDir"));
        Map map = getClasses(bot.getProperties().getProperty("commandsDirectory"));
        Map convertedMap = test((List) map.get("names"), (List) map.get("methods"), (List) map.get("classes"));
        addCommands((List) convertedMap.get("commands"), (List) convertedMap.get("methods"));
        addCommandMethods((List) convertedMap.get("commandMethods"), (List) convertedMap.get("methods"));
    }

    private Map test(List names, List Methods, List classes) {
        List<List> commands = new ArrayList<>();
        List<List> commandMethods = new ArrayList<>();
        Set set = new HashSet(Methods);
        List uniqueList = new ArrayList(set);
        for (int i = 0; i < uniqueList.size(); i++) {
            commands.add(new ArrayList<>());
            commandMethods.add(new ArrayList<>());
            for (int j = 0; j < Methods.size(); j++) {
                if (uniqueList.get(i).equals(Methods.get(j))) {
                    commands.get(i).add(names.get(j));
                    commandMethods.get(i).add(classes.get(j));
                }
            }
        }
        Map m = new HashMap();
        m.put("methods", uniqueList);
        m.put("commands", commands);
        m.put("commandMethods", commandMethods);
        return m;

    }

    private void addCommands(List names, List Methods) {
        for (int i = 0; i < Methods.size(); i++) {
            commands.put(String.valueOf(Methods.get(i)), (List) names.get(i));
        }
        LOGGER.debug("commands added");
    }

    private void addCommandMethods(List classes, List Methods) {
        for (int i = 0; i < Methods.size(); i++) {
            commandMethods.put(String.valueOf(Methods.get(i)), (List) classes.get(i));
        }
        LOGGER.debug("commandMethods added");
    }

    public HashMap<String, List> getCommands() {
        return commands;
    }

    public HashMap<String, List> getCommandMethods() {
        return commandMethods;
    }

    public Class<?> getDefaultCommands() {
        return defaultCommands;
    }

    public Class<?> getAdminCommands() {
        return adminCommands;
    }

    private static Map getClasses(String packageName)
            throws ClassNotFoundException, IOException {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        assert classLoader != null;
        String path = packageName.replace('.', '/');
        Enumeration<URL> resources = classLoader.getResources(path);
        List<File> dirs = new ArrayList<>();
        Map<String, Collection> map = new HashMap<>();
        while (resources.hasMoreElements()) {
            URL resource = resources.nextElement();
            dirs.add(new File(resource.getFile()));
        }
        ArrayList classes = new ArrayList();
        ArrayList names = new ArrayList();
        ArrayList<String> methods = new ArrayList<>();
        for (Object directory : dirs) {
            Map m = findClasses((File) directory, packageName);
            classes.addAll((Collection) m.get("classes"));
            names.addAll((Collection) m.get("names"));
        }
        for (Object aClass : classes) {
            String test = aClass.toString();
            Class<?> c = Class.forName(test);
            if (c.getModifiers() == Modifier.PUBLIC) {
                test = test.replace(packageName + ".", "");
                if (test.indexOf('.') != -1) {
                    methods.add(test.substring(0, test.indexOf('.')).trim());
                } else {
                    methods.add(":");
                }
            }
        }
        map.put("methods", methods);
        map.put("classes", classes);
        map.put("names", names);
        return map;
    }

    private static Map findClasses(File directory, String packageName) throws IOException, ClassNotFoundException {
        List classes = new ArrayList();
        List names = new ArrayList();
        Map<String, List> map = new HashMap<>();
        //For Jar Files
        if (!directory.exists()) {
            String file = directory.toString().substring(0, (directory.toString().length() - packageName.length()) - 2).replaceAll("file:", "");
            ZipFile zip = new ZipFile(file);
            packageName = packageName.replace('.', '/');
            ArrayList<File> files = new ArrayList<>();
            if (zip.getEntry("META-INF/MANIFEST.MF") != null) {
                JarFile jf = new JarFile(file);
                Enumeration<JarEntry> entries = jf.entries();
                while (entries.hasMoreElements()) {
                    JarEntry je = entries.nextElement();
                    if (je.toString().startsWith(packageName)) {
                        String s = je.toString().substring(packageName.length(), je.toString().length());
                        files.add(new File(s));
                    }
                }
                packageName = packageName.replace('/', '.');
                String s;
                for (File f : files) {
                    if (f.toString().endsWith(".class")) {
                        s = packageName + f.toString();
                        s = s.replace('/', '.');
                        s = s.substring(0, s.length() - 6);
                        Class<?> c = Class.forName(s);
                        if (c.getModifiers() == Modifier.PUBLIC) {
                            classes.add(s);
                            names.add(f.getName().substring(0, f.getName().length() - 6).toLowerCase());
                        }
                    }
                }
                map.put("names", names);
                map.put("classes", classes);
                return map;
            }
        }
        //If file isn't a jar file
        File[] files = directory.listFiles();
        assert files != null;
        for (File file : files) {
            if (file.isDirectory()) {
                assert !file.getName().contains(".");
                Map m = findClasses(file, packageName + "." + file.getName());
                classes.addAll((Collection) m.get("classes"));
                names.addAll((Collection) m.get("names"));
            } else if (file.getName().endsWith(".class")) {
                try {
                    Class<?> c = Class.forName(packageName + '.' + file.getName().substring(0, file.getName().length() - 6));
                    if (c.getModifiers() == Modifier.PUBLIC) {
                        classes.add(packageName + '.' + file.getName().substring(0, file.getName().length() - 6));
                        names.add(file.getName().substring(0, file.getName().length() - 6).toLowerCase());
                    }
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
        map.put("names", names);
        map.put("classes", classes);
        return map;
    }

    private Class<?> getClass(String s) {
        try {
            return Class.forName(s);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }
}