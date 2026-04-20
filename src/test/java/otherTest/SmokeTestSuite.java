package otherTest;

import org.testng.annotations.Test;
import api.WebFramework.Runner;

/**
 * TestNG wrapper class for running smoke tests via Maven surefire.
 * This class is used by CI/CD pipelines.
 */
public class SmokeTestSuite {

    @Test
    public void runSmokeTests() {
        System.setProperty("tags", "smoke");
        Runner.run();
    }
}
