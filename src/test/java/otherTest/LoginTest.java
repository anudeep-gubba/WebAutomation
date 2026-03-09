package otherTest;

import java.util.Map;

import pages.LoginPage;
import api.reporting.ExtentTestManager;

public class LoginTest {

    public void testLogin(Map<String,String> data) throws InterruptedException {

        System.out.println("Executing LoginTest.testLogin");

        System.out.println("Email: " + data.get("email"));
        System.out.println("Password: " + data.get("password"));

        LoginPage loginPage = new LoginPage();

        loginPage.fillEmail(data.get("email"));
        loginPage.fillPassword(data.get("password"));
        loginPage.clickLogin();
    }
}