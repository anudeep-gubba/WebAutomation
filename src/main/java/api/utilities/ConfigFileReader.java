package api.utilities;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Properties;

public class ConfigFileReader {
    private Properties properties;
    private final String propertyFilePath = "config.properties";

    public ConfigFileReader() {
        String baseDir = System.getProperty("user.dir");

        String[] possiblePaths = {
            baseDir + "/target/classes/" + propertyFilePath,
            baseDir + "/src/main/resources/" + propertyFilePath,
            baseDir + "/" + propertyFilePath,
            propertyFilePath
        };

        BufferedReader reader = null;
        String loadedPath = null;

        for (String path : possiblePaths) {
            try {
                FileReader fr = new FileReader(path);
                reader = new BufferedReader(fr);
                loadedPath = path;
                break;
            } catch (FileNotFoundException e) {
                // Try next path
            }
        }

        // Try classpath as last resort
        if (reader == null) {
            InputStream is = getClass().getClassLoader().getResourceAsStream(propertyFilePath);
            if (is != null) {
                reader = new BufferedReader(new InputStreamReader(is));
                loadedPath = "classpath:" + propertyFilePath;
            }
        }

        if (reader == null) {
            throw new RuntimeException("Could not find config file: " + propertyFilePath);
        }

        properties = new Properties();
        try {
            properties.load(reader);
            reader.close();
            System.out.println("Config loaded from: " + loadedPath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String get(String propertyName) {
        String value = this.properties.getProperty(propertyName);
        if(value != null) {
            return value;
        } else {
            throw new RuntimeException("Given property '" + propertyName + "' not found in config file");
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
