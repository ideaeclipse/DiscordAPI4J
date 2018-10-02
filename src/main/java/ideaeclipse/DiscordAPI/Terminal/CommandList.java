package ideaeclipse.DiscordAPI.Terminal;

import ideaeclipse.DiscordAPI.IDiscordBot;
import ideaeclipse.DiscordAPI.utils.DiscordLogger;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Modifier;
import java.net.URL;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.zip.ZipFile;


/**
 * This file loads all custom 'commands' in the specified directory in config.properties (commandsDirectory)
 *
 * @author ideaeclipse
 */
public class CommandList {

    private static final DiscordLogger LOGGER = new DiscordLogger(CommandList.class.getName());
    private HashMap<String, List> commands;
    private HashMap<String, List> commandMethods;
    private Class<?> defaultCommands, adminCommands;

    /**
     * @param bot discordBot
     * @throws IOException            if you don't have permission to view files (run with sudo or admin)
     * @throws ClassNotFoundException if class can't be found (this never gets thrown)
     */
    CommandList(final IDiscordBot bot) throws IOException, ClassNotFoundException {
        commands = new HashMap<>();
        commandMethods = new HashMap<>();
        defaultCommands = getClass(bot.getProperties().getProperty("genericDirectory") + "." + bot.getProperties().getProperty("defaultFileDirectory"));
        adminCommands = getClass(bot.getProperties().getProperty("genericDirectory") + "." + bot.getProperties().getProperty("adminFileDir"));
        Map map = getClasses(bot.getProperties().getProperty("commandsDirectory"));
        Map convertedMap = convertMap((List) map.get("names"), (List) map.get("methods"), (List) map.get("classes"));
        addCommands((List) convertedMap.get("commands"), (List) convertedMap.get("methods"));
        addCommandMethods((List) convertedMap.get("commandMethods"), (List) convertedMap.get("methods"));
    }

    /**
     * @param names   names of files
     * @param Methods full file path
     * @param classes class
     * @return full map of all data
     */
    private Map convertMap(final List names, final List Methods, final List classes) {
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

    /**
     * @param names   keys
     * @param Methods values
     */
    private void addCommands(final List names, final List Methods) {
        for (int i = 0; i < Methods.size(); i++) {
            commands.put(String.valueOf(Methods.get(i)), (List) names.get(i));
        }
        LOGGER.debug("commands added");
    }

    /**
     * @param classes keys
     * @param Methods values
     */
    private void addCommandMethods(final List classes, final List Methods) {
        for (int i = 0; i < Methods.size(); i++) {
            commandMethods.put(String.valueOf(Methods.get(i)), (List) classes.get(i));
        }
        LOGGER.debug("commandMethods added");
    }

    public HashMap<String, List> getCommands() {
        return commands;
    }

    HashMap<String, List> getCommandMethods() {
        return commandMethods;
    }

    Class<?> getDefaultCommands() {
        return defaultCommands;
    }

    Class<?> getAdminCommands() {
        return adminCommands;
    }

    /**
     * @param packageName genericDirectory
     * @return map of all data
     * @throws ClassNotFoundException if class isn't found
     * @throws IOException            if you don't have permission
     */
    private static Map getClasses(final String packageName)
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

    /**
     * This is different if the program is being run inside a jar container or not. view source
     *
     * @param directory directory name
     * @param packageName package name
     * @return map of data
     * @throws IOException
     * @throws ClassNotFoundException
     */
    private static Map findClasses(final File directory, String packageName) throws IOException, ClassNotFoundException {
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
                            names.add(f.getName().substring(0, f.getName().length() - 6));
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
                        names.add(file.getName().substring(0, file.getName().length() - 6));
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

    private Class<?> getClass(final String s) {
        try {
            return Class.forName(s);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }
}