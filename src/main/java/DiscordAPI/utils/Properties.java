package DiscordAPI.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Properties {
    private static final DiscordLogger LOGGER = new DiscordLogger(Properties.class.getName());
    private static final String configName = "src/config.properties";
    private File file;
    private java.util.Properties prop;
    private String[] properties = {"adminUser", "adminGroup", "commandsDirectory", "genericDirectory", "adminFileDir", "defaultFileDirectory","useTerminal"};

    public Properties() {
        file = new File(configName);
    }

    public void start() throws IOException {
        prop = new java.util.Properties();
        if (!file.exists()) {
            LOGGER.info("Config file DOES NOT exist therefore creating file");
            if (!configure()) {
                for (String s : properties) {
                    prop.put(s, "");
                }
            }
            prop.store(new FileOutputStream(configName), null);
            if (!file.createNewFile()) {
                LOGGER.warn("Configuration file could not be created. Try running program with administrator access");
            } else {
                LOGGER.debug("Configuration Loaded");
            }
        } else {
            LOGGER.info("Config file exist");
            prop.load(new FileInputStream(configName));
            List<String> unsetConfig = new ArrayList<>();
            for (String s : properties) {
                if (prop.getProperty(s) == null) {
                    unsetConfig.add(s);
                }
            }
            if (unsetConfig.size() > 0)
                unset(unsetConfig);
            prop.store(new FileOutputStream(configName), null);
        }
    }

    private boolean configure() {
        Scanner sc = new Scanner(System.in);
        System.out.println("This is your first time running the bot do you want to configure over command line or configure in the properties file? (Y/N)");
        if (answer(sc.nextLine())) {
            for (String s : properties) {
                System.out.println("Enter property for: " + s);
                prop.put(s, sc.nextLine());
            }
            sc.close();
            return true;
        }
        sc.close();
        return false;
    }

    private void unset(List<String> unset) {
        Scanner sc = new Scanner(System.in);
        System.out.println("You're config.properties file is out of date would you like to update it through the console? (Y/N)");
        if (answer(sc.nextLine())) {
            for (String s : unset) {
                System.out.println("Enter property for: " + s);
                prop.put(s, sc.nextLine());
            }
        } else {
            for (String s : unset) {
                prop.put(s, sc.nextLine());
            }
        }
    }

    private boolean answer(String s) {
        return s.toLowerCase().equals("y");
    }

    public String getProperty(String p) {
        return prop.getProperty(p);
    }
}
