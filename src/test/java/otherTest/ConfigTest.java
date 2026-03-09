package otherTest;

import api.utilities.ConfigFileReader;

public class ConfigTest {
    public static void main(String[] args) {
        ConfigFileReader config = new ConfigFileReader();
        System.out.println(config.get("test_script_file"));
        System.out.println(config.get("test_script_file","test"));
        System.out.println(config.get("DBUrl","defaultValue"));
        System.out.println(config.get("random","defaultValue"));
        config.set("DBUrl", "anudeep");
        System.out.println(config.get("DBUrl"));
    }
}
