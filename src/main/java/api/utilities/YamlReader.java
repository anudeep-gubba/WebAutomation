package api.utilities;

import org.yaml.snakeyaml.Yaml;

import java.io.FileInputStream;
import java.util.Map;

public class YamlReader {

    public static Map<String,Object> read(String file) {

        try {

            Yaml yaml = new Yaml();

            FileInputStream fis = new FileInputStream(
                    System.getProperty("user.dir") + "/resources/" + file);

            return yaml.load(fis);

        } catch (Exception e) {
            throw new RuntimeException("Unable to read yaml " + file);
        }
    }
}