package pages;

import org.openqa.selenium.By;

import api.WebFramework.BrowserFunctions;
import api.web.actions.WaitForElement;

public class LoginPage extends BrowserFunctions {

    private By txtEmail = By.name("username");
    private By txtPassword = By.name("password");
    private By btnLogin = By.name("signin");

    public void fillEmail(String userName) {
        System.out.println("fillEmail method called");
        this.enterText(txtEmail, userName, "Email");
    }

    public void fillPassword(String password) {
        this.enterText(txtPassword, password, "Password");
    }

    public void clickLogin() {
        this.click(btnLogin, "Login button");
    }

}

