package api.WebFramework;

import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class DynamicTest {

    @Test
    @Parameters({"steps", "data"})
    public void execute(String stepsStr, String dataStr) {

        System.out.println("DynamicTest.execute() started");
        System.out.println("Steps raw: " + stepsStr);
        System.out.println("Data raw: " + dataStr);

        try {

            // Extract class name
            String className = stepsStr.substring(
                    stepsStr.indexOf("class=") + 6,
                    stepsStr.indexOf(","));

            // Extract method name
            String methodName = stepsStr.substring(
                    stepsStr.indexOf("method=") + 7,
                    stepsStr.indexOf("}"));

            System.out.println("Executing " + className + "." + methodName);

            // Convert data string → map
            Map<String, String> data = parseData(dataStr);

            // Load class
            Class<?> clazz = Class.forName(className);

            Object instance = clazz.getDeclaredConstructor().newInstance();

            // Find method
            Method method = clazz.getMethod(methodName, Map.class);
            System.out.println("Driver instance: " + DriverFactory.getDriver());
            System.out.println("Invoking step method...");

            // Execute method
            method.invoke(instance, data);

        } catch (Exception e) {

            e.printStackTrace();
            throw new RuntimeException("Dynamic step execution failed", e);
        }
    }

    private Map<String, String> parseData(String dataString) {

        Map<String, String> data = new HashMap<>();

        if (dataString == null || dataString.isEmpty()) {
            return data;
        }

        dataString = dataString.replace("{", "").replace("}", "");

        String[] pairs = dataString.split(",");

        for (String pair : pairs) {

            String[] kv = pair.split("=", 2);  // limit split to 2

            String key = kv[0].trim();

            String value = "";
            if (kv.length > 1) {
                value = kv[1].trim();
            }

            data.put(key, value);
        }

        return data;
    }
}