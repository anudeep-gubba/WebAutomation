package api.reporting;

import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;

public class Retry implements IRetryAnalyzer {

    private int count = 0;
    private static final int maxTry = 0;

    @Override
    public boolean retry(ITestResult result) {

        if (!result.isSuccess() && count < maxTry) {
            count++;
            result.setStatus(ITestResult.FAILURE);
            return true;
        }

        return false;
    }
}