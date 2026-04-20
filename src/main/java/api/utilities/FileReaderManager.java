package api.utilities;

import java.util.HashMap;
import java.util.Map;

public final class FileReaderManager {
    private static final FileReaderManager fileReaderManager = new FileReaderManager();
    private static ConfigFileReader configFileReader;
    private static final Map<String, ConfigFileReader> customConfigReaders = new HashMap<>();

    private FileReaderManager() {
    }

    public static FileReaderManager getInstance() {
        return fileReaderManager;
    }

    public ConfigFileReader getConfigReader() {
        return (configFileReader == null) ? new ConfigFileReader() : configFileReader;
    }

    /**
     * Get a custom config reader for a specific config file path.
     * Useful for CI/CD environments to override config properties.
     *
     * @param configPath the path to the custom config file
     * @return ConfigFileReader instance for the given path
     */
    public static ConfigFileReader getCustomConfigReader(String configPath) {
        return customConfigReaders.computeIfAbsent(configPath, path -> new ConfigFileReader(path));
    }
}
