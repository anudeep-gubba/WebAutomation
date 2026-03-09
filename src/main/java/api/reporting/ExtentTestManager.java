package api.reporting;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.Reporter;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;

public class ExtentTestManager {

	static Map<Integer, ExtentTest> extentTestMap = new HashMap<>();
	static Map<Integer, ExtentTest> extentNodeMap = new HashMap<>();

	static ExtentReports extent;

	static {
		try {
			extent = ExtentManager.getReporter();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public static synchronized ExtentTest getTest() {
		return extentTestMap.get((int) Thread.currentThread().getId());
	}

	public static synchronized ExtentTest getNode() {
		return extentNodeMap.get((int) Thread.currentThread().getId());
	}

	public static synchronized ExtentTest startTest(String testName) {

		ExtentTest test = extent.createTest(testName);

		extentTestMap.put((int) Thread.currentThread().getId(), test);

		return test;
	}

	public static synchronized ExtentTest startNode(String nodeName) {

		ExtentTest parent = getTest();

		if (parent == null) {
			return null;
		}

		ExtentTest node = parent.createNode(nodeName);

		extentNodeMap.put((int) Thread.currentThread().getId(), node);

		return node;
	}

	public static synchronized void setPassMessageInReport(String message) {

		if (getNode() != null) {
			getNode().pass(message);
		}

		Assert.assertTrue(true);
	}

	public static synchronized void setInfoMessageInReport(String message) {

		if (getNode() != null) {
			getNode().info(message);
		}
	}

	public static synchronized void setSkipMessageInReport(String message) {

		if (getNode() != null) {
			getNode().skip(message);
		}
	}

	public static synchronized void setFailMessageInReport(String message) {

		if (getNode() != null) {
			getNode().fail(message);
		}

		Assert.fail(message);

		Reporter.getCurrentTestResult().setStatus(ITestResult.FAILURE);
	}
}