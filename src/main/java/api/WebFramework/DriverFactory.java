package api.WebFramework;

import api.utilities.FileReaderManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.edge.EdgeDriver;

public class DriverFactory {

    private static final ThreadLocal<WebDriver> tdriver = new ThreadLocal<>();

    public void initDriver() {

        String browserName = FileReaderManager.getInstance()
                .getConfigReader()
                .get("browserName");

        WebDriver driver;

        switch (browserName.toLowerCase()) {

            case "chrome":
                driver = new ChromeDriver();
                break;

            case "firefox":
                driver = new FirefoxDriver();
                break;

            case "edge":
                driver = new EdgeDriver();
                break;

            default:
                throw new RuntimeException("Invalid Browser: " + browserName);
        }

        tdriver.set(driver);

        getDriver().manage().window().maximize();

        String url = FileReaderManager.getInstance()
                .getConfigReader()
                .get("appUrl");

        getDriver().get(url);
    }

    public static WebDriver getDriver() {

        WebDriver driver = tdriver.get();

        if(driver == null){
            throw new RuntimeException(
                    "Driver not initialized for thread: "
                            + Thread.currentThread().getId());
        }

        return driver;
    }

    public void closeDriver() {

        WebDriver driver = tdriver.get();

        if (driver != null) {
            driver.quit();
            tdriver.remove();
        }
    }
}