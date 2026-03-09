package api.WebFramework;

import java.time.Duration;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import api.reporting.ExtentTestManager;
import api.utilities.FileReaderManager;

public class BrowserFunctions {

    private WebDriverWait wait;
    private WebDriver driver;

    public static final long waittime;

    static {
        waittime = Long.parseLong(
                FileReaderManager.getInstance()
                        .getConfigReader()
                        .get("timeout", "10"));
    }

    public BrowserFunctions() {

        this.driver =  DriverFactory.getDriver();
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(waittime));
    }

    public WebElement getElement(By loc, String eleName) {

        try {
            System.out.println("Element found: " + eleName);
            return wait.until(
                    ExpectedConditions.visibilityOfElementLocated(loc));

        } catch (Exception e) {

            ExtentTestManager.setFailMessageInReport(
                    "Failed to locate element " + eleName + " due to exception " + e);

            throw new RuntimeException("Element not found: " + eleName, e);
        }
    }

    public void click(By loc, String eleName) {

        try {

            WebElement ele = wait.until(
                    ExpectedConditions.elementToBeClickable(loc));

            ((JavascriptExecutor) driver)
                    .executeScript("arguments[0].scrollIntoView(true);", ele);

            ele.click();

            ExtentTestManager.setInfoMessageInReport(
                    "Clicked on element " + eleName);

        } catch (Exception e) {

            ExtentTestManager.setFailMessageInReport(
                    "Failed to click element " + eleName + " due to exception " + e);

            throw new RuntimeException(e);
        }
    }

    public void enterText(By loc, String text, String eleName) {

        try {

            WebElement ele = getElement(loc, eleName);

            ele.sendKeys(Keys.chord(Keys.CONTROL, "a", Keys.DELETE));
            ele.sendKeys(text);
//            captureStepScreenshot("After entering " + eleName);

            ExtentTestManager.setInfoMessageInReport(
                    "Entered text " + text + " in element " + eleName);

        } catch (Exception e) {

            ExtentTestManager.setFailMessageInReport(
                    "Failed to enter text in element " + eleName + " due to exception " + e);

            throw new RuntimeException(e);
        }
    }

    public static String takeScreenshot() {

        try {

            WebDriver driver;

            try {
                driver = DriverFactory.getDriver();
            } catch(Exception e) {
                return null;
            }

            return ((TakesScreenshot)driver)
                    .getScreenshotAs(OutputType.BASE64);

        } catch(Exception e) {

            System.out.println("Screenshot failed: " + e);
            return null;
        }
    }

    public static void captureStepScreenshot(String stepName) {

        try {

            String screenshot = takeScreenshot();

            if (screenshot != null) {

                ExtentTestManager.getNode()
                        .addScreenCaptureFromBase64String(
                                screenshot,
                                stepName);

            }

        } catch (Exception e) {

            System.out.println("Screenshot capture failed: " + e);
        }
    }
}