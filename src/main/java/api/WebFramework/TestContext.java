package api.WebFramework;

import java.util.List;
import java.util.Map;

public class TestContext {

    private static ThreadLocal<List<Map<String,String>>> steps =
            new ThreadLocal<>();

    private static ThreadLocal<Map<String,String>> data =
            new ThreadLocal<>();

    public static void setSteps(List<Map<String,String>> stepList){
        steps.set(stepList);
    }

    public static void setData(Map<String,String> testData){
        data.set(testData);
    }

    public static List<Map<String,String>> getSteps(){
        return steps.get();
    }

    public static Map<String,String> getData(){
        return data.get();
    }
}