package api.WebFramework;

import java.util.*;

import api.utilities.FileReaderManager;
import api.utilities.YamlReader;

public class Runner {

    public static void run() {

        TestReader reader = new TestReader();

        String testsFile = FileReaderManager.getInstance()
                .getConfigReader().get("testsFile");

        String testDataFile = FileReaderManager.getInstance()
                .getConfigReader().get("testDataFile");

        boolean parallel = Boolean.parseBoolean(
                FileReaderManager.getInstance()
                        .getConfigReader()
                        .get("parallel"));

        int threads = Integer.parseInt(
                FileReaderManager.getInstance()
                        .getConfigReader()
                        .get("threads"));

        String suiteName = FileReaderManager.getInstance()
                .getConfigReader()
                .get("suiteName");

        String runTests = FileReaderManager.getInstance()
                .getConfigReader()
                .get("runTests", "")
                .trim();

        String runTags = FileReaderManager.getInstance()
                .getConfigReader()
                .get("runTags", "")
                .trim();

        /* ----------------------------------------
           Convert runTests to Set
        ---------------------------------------- */

        Set<String> testsToRun = new HashSet<>();

        if (!runTests.isEmpty()) {

            Arrays.stream(runTests.split(","))
                    .map(String::trim)
                    .forEach(testsToRun::add);
        }

        /* ----------------------------------------
           Convert runTags to Set
        ---------------------------------------- */

        Set<String> tagsToRun = new HashSet<>();

        if (!runTags.isEmpty()) {

            Arrays.stream(runTags.split(","))
                    .map(String::trim)
                    .forEach(tagsToRun::add);
        }

        Map<String, Object> tests = YamlReader.read(testsFile);
        Map<String, Object> testData = YamlReader.read(testDataFile);

        reader.createSuite(suiteName, parallel, threads);

        for (String testId : tests.keySet()) {

            Map<String, Object> test =
                    (Map<String, Object>) tests.get(testId);

            /* ----------------------------------------
               FILTER : RUN SPECIFIC TESTS
            ---------------------------------------- */

            if (!testsToRun.isEmpty()) {

                if (!testsToRun.contains(testId)) {
                    continue;
                }
            }

            /* ----------------------------------------
               FILTER : RUN TAGS
            ---------------------------------------- */

            else if (!tagsToRun.isEmpty()) {

                List<String> tags =
                        (List<String>) test.get("tags");

                if (tags == null) {
                    continue;
                }

                boolean match = tags.stream()
                        .anyMatch(tagsToRun::contains);

                if (!match) {
                    continue;
                }
            }

            System.out.println("Loading test: " + testId);

            String dataSet = (String) test.get("testData");

            List<Map<String, String>> steps =
                    (List<Map<String, String>>) test.get("steps");

            List<Map<String, String>> iterations =
                    (List<Map<String, String>>) testData.get(dataSet);

            if (iterations == null) {

                throw new RuntimeException(
                        "Dataset not found in testdata.yaml: " + dataSet);
            }

            for (Map<String, String> iteration : iterations) {

                String iterationNum =
                        iteration.getOrDefault("iteration", "1");

                System.out.println("Creating test iteration: " + iterationNum);

                Map<String, String> params = new HashMap<>();

                params.put("steps", steps.toString());
                params.put("data", iteration.toString());

                reader.createTest(
                        testId + "_ITER_" + iterationNum,
                        "api.WebFramework.DynamicTest",
                        params
                );
            }
        }

        reader.run();
    }
}