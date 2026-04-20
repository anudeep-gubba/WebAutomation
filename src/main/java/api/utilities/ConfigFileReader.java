package api.utilities;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

public class ConfigFileReader {
    private Properties properties;
    private String propertyFilePath;

    /**
     * Default constructor - uses config.properties from user.dir
     */
    public ConfigFileReader() {
        this.propertyFilePath = "config.properties";
        loadProperties();
    }

    /**
     * Constructor with custom config file path - for CI/CD overrides
     * @param customPath path to custom config file
     */
    public ConfigFileReader(String customPath) {
        this.propertyFilePath = customPath;
        loadProperties();
    }

    private void loadProperties() {
        BufferedReader reader;
        String baseDir = System.getProperty("user.dir");

        try {
            Path customPath = Paths.get(propertyFilePath);

            // First try: absolute path or relative to current dir
            if (Files.exists(customPath)) {
                reader = new BufferedReader(new FileReader(propertyFilePath));
            }
            // Second try: from classpath
            else {
                InputStream is = getClass().getClassLoader().getResourceAsStream(propertyFilePath);
                if (is == null) {
                    // Third try: default location
                    reader = new BufferedReader(new FileReader(baseDir + "/" + propertyFilePath));
                } else {
                    reader = new BufferedReader(new InputStreamReader(is));
                }
            }

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
        // System property takes precedence (for CI/CD overrides)
        String systemValue = System.getProperty(propertyName);
        if (systemValue != null && !systemValue.isEmpty()) {
            return systemValue;
        }
        String value = this.properties.getProperty(propertyName);
        if(value != null) {
            return value;
        } else {
            throw new RuntimeException("Given property '" + propertyName + "' not found in config file "+ propertyFilePath);
        }
    }

    public String get(String propertyName, String defaultValue) {
        // System property takes precedence (for CI/CD overrides)
        String systemValue = System.getProperty(propertyName);
        if (systemValue != null && !systemValue.isEmpty()) {
            return systemValue;
        }
        String value = this.properties.getProperty(propertyName, defaultValue);
        return value;
    }

    public void set(String propertyName, String value) {
        this.properties.setProperty(propertyName, value);
    }
}
