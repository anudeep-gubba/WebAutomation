package otherTest;

import org.testng.annotations.Test;
import api.WebFramework.Runner;

/**
 * TestNG wrapper class for running tests via Maven surefire.
 * This class is used by CI/CD pipelines.
 *
 * System properties that can be passed:
 * - tags: smoke,sanity,regression (comma-separated)
 * - tests: TC001,TC002 (specific test IDs)
 * - browser: chrome,firefox,edge
 * - threads: number of parallel threads
 * - env: qa,staging,prod
 * - parallel: true,false
 * - overrideReport: true,false
 */
public class SmokeTestSuite {

    @Test
    public void runTests() {
        // System properties are passed via Maven -D flags
        // Runner.run() reads them from System.getProperty()

        String tags = System.getProperty("tags", "smoke");
        String tests = System.getProperty("tests", "");
        String browser = System.getProperty("browser", "chrome");
        String threads = System.getProperty("threads", "5");
        String env = System.getProperty("env", "qa");
        String parallel = System.getProperty("parallel", "true");
        String overrideReport = System.getProperty("overrideReport", "true");

        System.out.println("========================================");
        System.out.println("  Running Test Suite via Maven Surefire");
        System.out.println("========================================");
        System.out.println("  Tags: " + tags);
        System.out.println("  Tests: " + tests);
        System.out.println("  Browser: " + browser);
        System.out.println("  Threads: " + threads);
        System.out.println("  Environment: " + env);
        System.out.println("  Parallel: " + parallel);
        System.out.println("  Override Report: " + overrideReport);
        System.out.println("========================================");

        Runner.run();
    }
}
