package api.utilities;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

public class ConfigFileReader {
    private Properties properties;
    private final String propertyFilePath = "//config.properties";

    public ConfigFileReader() {
        BufferedReader reader;
        String baseDir = System.getProperty("user.dir");
        try {
            reader = new BufferedReader(new FileReader(baseDir + propertyFilePath));
            properties = new Properties();
            try {
                properties.load(reader);
                reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException("Could not find config file: " + propertyFilePath);
        }
    }
    public String get(String propertyName) {
        String value = this.properties.getProperty(propertyName);
        if(value != null) {
            return value;
        } else {
            throw new RuntimeException("Given property '" + propertyName + "' not found in config file "+ propertyFilePath);
        }
    }

    public String get(String propertyName, String defaultValue) {
        String value = this.properties.getProperty(propertyName, defaultValue);
        return value;
    }

    public void set(String propertyName, String value) {
        this.properties.setProperty(propertyName, value);
    }
}
