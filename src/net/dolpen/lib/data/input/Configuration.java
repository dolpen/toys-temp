package net.dolpen.lib.data.input;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * java.util.Propertiesの薄いラッパー
 */
public class Configuration {

    Properties prop;

    public Configuration(File file) throws IOException {
        prop = new Properties();
        prop.load(new FileInputStream(file));
    }

    public Configuration(String fileName) throws IOException {

        this(new File(fileName));
    }

    public boolean contains(String key) {
        return prop.getProperty(key) != null;
    }

    public boolean containsAll(String... keys) {
        for (String key : keys) if (!contains(key)) return false;
        return true;
    }

    public String get(String key, String defaultValue) {
        return prop.getProperty(key, defaultValue);
    }

    public String get(String key) {
        return prop.getProperty(key);
    }

}
