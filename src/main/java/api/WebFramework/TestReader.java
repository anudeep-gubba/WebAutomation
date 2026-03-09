package api.WebFramework;

import java.util.*;

import org.testng.TestNG;
import org.testng.xml.*;

public class TestReader {

    private XmlSuite suite;

    public void createSuite(String suiteName, boolean parallel, int threads) {

        suite = new XmlSuite();
        suite.setName(suiteName);

        if (parallel) {
            suite.setParallel(XmlSuite.ParallelMode.TESTS);
            suite.setThreadCount(threads);
        }

        List<String> listeners = new ArrayList<>();

        listeners.add("api.reporting.TestStatusListener");
        listeners.add("api.reporting.AnnotationTransformer");

        suite.setListeners(listeners);
    }

    public void createTest(String testName,
                           String className,
                           Map<String,String> params) {

        System.out.println("Initializing TestClass - " + testName);

        XmlTest xmlTest = new XmlTest(suite);
        xmlTest.setName(testName);
        xmlTest.setParameters(params);

        XmlClass xmlClass = new XmlClass(className);

        List<XmlInclude> methods = new ArrayList<>();
        methods.add(new XmlInclude("execute"));

        xmlClass.setIncludedMethods(methods);

        List<XmlClass> classes = new ArrayList<>();
        classes.add(xmlClass);

        xmlTest.setXmlClasses(classes);
    }

    public void run() {

        TestNG testng = new TestNG();
        testng.setXmlSuites(Collections.singletonList(suite));
        testng.run();
    }
}