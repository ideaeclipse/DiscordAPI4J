package ideaeclipse.DiscordAPI.terminal;

import ideaeclipse.DiscordAPI.IDiscordBot;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;


/**
 * This class queries all classes inside the directory specified in the config.properties, "genericDirectory" and "commandsDirectory"
 *
 * @author ideaeclipse
 */
public class CommandList {
    private Map<String, List<Class<?>>> commandMap;
    private Map<String, List<Class<?>>> genericCommandMap;

    public CommandList(IDiscordBot bot) {
        try {
            commandMap = getClasses(bot.getProperties().getProperty("commandsDirectory"));
            genericCommandMap = getClasses(bot.getProperties().getProperty("genericDirectory"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * @param directory path to class files
     * @return a map of subdirectory,list of functions
     * @throws IOException if class isn't found
     */
    private Map<String, List<Class<?>>> getClasses(String directory) throws IOException {
        Map<String, List<Class<?>>> map = new HashMap<>();
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        Enumeration<URL> urlEnumeration = classLoader.getResources(directory.replace('.', '/'));
        while (urlEnumeration.hasMoreElements()) {
            String baseDir = urlEnumeration.nextElement().getPath();
            File file;
            if (System.getProperty("os.name").toLowerCase().contains("windows")) {
                file = new File(baseDir.substring(1));
            } else {
                file = new File(baseDir);
            }
            if (file.exists()) {
                URLClassLoader newClassLoader = new URLClassLoader(new URL[]{
                        new File(file.getPath().substring(0, (file.getPath().length() - directory.length()))).toURI().toURL()
                });
                for (File file1 : Objects.requireNonNull(file.listFiles())) {
                    String subDirectory = file1.getPath().substring(file.getPath().length() + 1);
                    List<Class<?>> classes = new LinkedList<>();
                    if (file1.listFiles() != null) {
                        for (File file2 : Objects.requireNonNull(file1.listFiles())) {
                            try {
                                classes.add(newClassLoader.loadClass(directory + "." + subDirectory + "." + file2.getName().replace(".class", "")));
                            } catch (ClassNotFoundException e) {
                                e.printStackTrace();
                            }
                        }
                    } else {
                        try {
                            classes.add(newClassLoader.loadClass(directory + "." + file1.getName().replace(".class", "")));
                        } catch (ClassNotFoundException e) {
                            e.printStackTrace();
                        }
                    }
                    map.put(subDirectory.replace(".class", ""), classes);
                }
            } else {
                String dir = file.getPath().substring(0, (file.getPath().length() - directory.length()));
                dir = dir.replace("file:", "");
                dir = dir.substring(0, dir.indexOf("!"));
                JarInputStream jarfile = new JarInputStream(new FileInputStream(dir));
                JarEntry entry;
                directory = directory.replace('.', '/');
                URLClassLoader newClassLoader = new URLClassLoader(new URL[]{
                        new URL("jar:file:" + dir + "!/")
                });
                while ((entry = jarfile.getNextJarEntry()) != null) {
                    if (entry.getName().startsWith(directory) && entry.getName().endsWith(".class")) {
                        try {
                            String temp = entry.getName().substring(directory.length() + 1);
                            String subCommand = temp.substring(0, temp.indexOf('/'));
                            Class<?> aClass = newClassLoader.loadClass(entry.getName().replace('/', '.').replace(".class", ""));
                            if (map.get(subCommand) != null)
                                map.get(subCommand).add(aClass);
                            else {
                                List<Class<?>> classes = new LinkedList<>();
                                classes.add(aClass);
                                map.put(subCommand, classes);
                            }
                        } catch (ClassNotFoundException e) {
                            e.printStackTrace();
                        }

                    }
                }
            }
        }
        return map;
    }

    /**
     * @return returns the map of commands
     */
    public Map<String, List<Class<?>>> getCommandMap() {
        return commandMap;
    }

    /**
     * @return returns the map of generic commands
     */
    public Map<String, List<Class<?>>> getGenericCommandMap() {
        return genericCommandMap;
    }
}
