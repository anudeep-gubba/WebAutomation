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
		DateFormat df = new SimpleDateFormat("HH-mm-ss_dd-MMM-yy");
		return df.format(date);
	}

	public synchronized static ExtentReports getReporter() throws IOException {

		if (extent == null) {

			String workingDir = System.getProperty("user.dir");

			String reportPath;

			if (FileReaderManager.getInstance()
					.getConfigReader()
					.get("overrideReport")
					.equalsIgnoreCase("yes")) {

				reportPath = workingDir + File.separator
						+ "ExtentReports"
						+ File.separator
						+ "ExtentReport_" + getTimeStamp() + ".html";

			} else {

				reportPath = workingDir + File.separator
						+ "ExtentReports"
						+ File.separator
						+ "ExtentReport.html";
			}

			ExtentSparkReporter reporter = new ExtentSparkReporter(reportPath);

			File xmlConfig = new File(workingDir + File.separator + "extent-config.xml");

			if (xmlConfig.exists()) {
				reporter.loadXMLConfig(xmlConfig);
			}

			extent = new ExtentReports();
			extent.attachReporter(reporter);
		}

		return extent;
	}
}