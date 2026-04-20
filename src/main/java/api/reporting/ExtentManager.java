package api.reporting;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;

import api.utilities.FileReaderManager;

public class ExtentManager {

	private static ExtentReports extent;

	private static String getTimeStamp() {
		Date date = new Date();
		DateFormat df = new SimpleDateFormat("yyyyMMdd_HHmmss");
		return df.format(date);
	}

	public synchronized static ExtentReports getReporter() throws IOException {

		if (extent == null) {

			String workingDir = System.getProperty("user.dir");

			// Create ExtentReports directory if it doesn't exist
			String reportsDir = workingDir + File.separator + "ExtentReports";
			new File(reportsDir).mkdirs();

			String reportPath;
			String overrideReport = FileReaderManager.getInstance()
					.getConfigReader()
					.get("overrideReport", "false");

			// Support both "true" and "yes" values
			if (overrideReport.equalsIgnoreCase("true")
					|| overrideReport.equalsIgnoreCase("yes")) {

				reportPath = reportsDir + File.separator
						+ "ExtentReport_" + getTimeStamp() + ".html";

			} else {

				reportPath = reportsDir + File.separator + "ExtentReport.html";
			}

			ExtentSparkReporter reporter = new ExtentSparkReporter(reportPath);

			// Try multiple paths for extent-config.xml
			File xmlConfig = findConfigFile(workingDir, "extent-config.xml");

			if (xmlConfig != null && xmlConfig.exists()) {
				reporter.loadXMLConfig(xmlConfig);
				System.out.println("Loaded Extent Report config: " + xmlConfig.getAbsolutePath());
			} else {
				System.out.println("Extent Report config not found, using defaults");
			}

			extent = new ExtentReports();
			extent.attachReporter(reporter);

			System.out.println("Extent Report initialized: " + reportPath);
		}

		return extent;
	}

	/**
	 * Find config file in various locations
	 */
	private static File findConfigFile(String workingDir, String fileName) {
		String[] possiblePaths = {
			workingDir + File.separator + fileName,
			workingDir + File.separator + "src" + File.separator + fileName,
			workingDir + File.separator + "resources" + File.separator + fileName,
			fileName
		};

		for (String path : possiblePaths) {
			File file = new File(path);
			if (file.exists()) {
				return file;
			}
		}
		return null;
	}

	/**
	 * Get the ExtentReports directory path
	 */
	public static String getReportDirectory() {
		return System.getProperty("user.dir") + File.separator + "ExtentReports";
	}
}