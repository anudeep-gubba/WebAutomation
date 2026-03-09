package api.reporting;

import api.WebFramework.DriverFactory;
import api.WebFramework.BrowserFunctions;

import org.testng.*;

public class TestStatusListener implements ITestListener,
		ISuiteListener,
		IInvokedMethodListener {

	private boolean hasFailures = false;

	private String getTestMethodName(ITestResult result) {
		return result.getMethod()
				.getConstructorOrMethod()
				.getName();
	}

	// -----------------------------
	// Suite Start
	// -----------------------------
	@Override
	public void onStart(ISuite suite) {
		System.out.println("Starting Test Suite: " + suite.getName());
	}

	// -----------------------------
	// Suite Finish
	// -----------------------------
	@Override
	public void onFinish(ISuite suite) {

		try {
			ExtentManager.getReporter().flush();
		} catch (Exception e) {
			e.printStackTrace();
		}

		System.out.println("Test Suite Finished");
	}

	// -----------------------------
	// Test Class Start
	// -----------------------------
	@Override
	public void onStart(ITestContext context) {

		String name = context.getName();

		System.out.println("Initializing TestClass - " + name);

		ExtentTestManager.startTest(name);
		ExtentTestManager.startNode(name);
	}

	// -----------------------------
	// Test Start
	// -----------------------------
	@Override
	public void onTestStart(ITestResult result) {

		String name = getTestMethodName(result);

		System.out.println("Initializing test method " + name);
	}

	// -----------------------------
	// Test Success
	// -----------------------------
	@Override
	public void onTestSuccess(ITestResult result) {

		System.out.println("Test Passed: " + getTestMethodName(result));

		ExtentTestManager.setPassMessageInReport("Test Passed");
	}

	// -----------------------------
	// Test Failure
	// -----------------------------
	@Override
	public void onTestFailure(ITestResult result) {

		System.out.println("Test Failed: " + getTestMethodName(result));

		try {

			String screenshot = BrowserFunctions.takeScreenshot();

			if (screenshot != null) {

				ExtentTestManager.getNode()
						.addScreenCaptureFromBase64String(
								screenshot,
								"Failure Screenshot");
			}

		} catch (Exception e) {
			System.out.println("Screenshot capture failed: " + e);
		}
	}

	// -----------------------------
	// Test Skipped
	// -----------------------------
	@Override
	public void onTestSkipped(ITestResult result) {

		System.out.println("Test Skipped: " + getTestMethodName(result));

		ExtentTestManager.setSkipMessageInReport(
				"Test skipped: " + getTestMethodName(result));
	}

	// -----------------------------
	// Before Invocation
	// -----------------------------
	@Override
	public void beforeInvocation(IInvokedMethod method, ITestResult testResult) {

		if (method.isTestMethod()) {

			System.out.println(
					"Initializing driver for thread: "
							+ Thread.currentThread().getId());

			DriverFactory df = new DriverFactory();
			df.initDriver();
		}
	}

	// -----------------------------
	// After Invocation
	// -----------------------------
	@Override
	public void afterInvocation(IInvokedMethod method, ITestResult testResult) {

		if (method.isTestMethod()) {

			new DriverFactory().closeDriver();
		}
	}
}