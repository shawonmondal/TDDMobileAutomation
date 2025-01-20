package com.qa.pages;

import com.qa.BaseTest;
import io.appium.java_client.AppiumBy;
import io.appium.java_client.pagefactory.AndroidFindBy;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;
import io.appium.java_client.pagefactory.iOSXCUITFindBy;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;

public class LoginPage extends BaseTest {
    @AndroidFindBy(accessibility = "test-Username")
    @iOSXCUITFindBy(id = "test-Username")
    private WebElement usernameTextField;

    @AndroidFindBy(accessibility = "test-Password")
    @iOSXCUITFindBy(id = "test-Password")
    private WebElement passwordTextField;

    @AndroidFindBy(accessibility = "test-LOGIN")
    @iOSXCUITFindBy(id = "test-LOGIN")
    private WebElement loginButton;

    @AndroidFindBy(xpath = "//android.widget.TextView[@text=\"Username and password do not match any user in this service.\"]")
    @iOSXCUITFindBy(xpath = "//XCUIElementTypeOther[@name=\"test-Error message\"]/child::XCUIElementTypeStaticText")
    private WebElement invalidUsernamePasswordErrorTxt;

    //    @AndroidFindBy(xpath = "//android.widget.TextView[@text=\"PRODUCTS\"]") private WebElement productPageTitle;
    //    WebElement productPageTitle = driver.findElement(AppiumBy.xpath("//android.widget.TextView[@text=\"PRODUCTS\"]"));
    public LoginPage() {
        PageFactory.initElements(new AppiumFieldDecorator(getDriver()), this);
    }

    public LoginPage enterUserName(String username) {
        System.out.println("Login with " + username);
        enterTextinTextField(usernameTextField, username);
        return this;
    }

    public LoginPage enterPassword(String password) {
        System.out.println("Password is " + password);
        enterTextinTextField(passwordTextField, password);
        return this;
    }

    public ProductsPage pressLoginButton() {
        System.out.println("Press login button");
        clickOnElement(loginButton);
        return new ProductsPage();
    }

    public String getErrorText() {
//        return getAttributOfElement(invalidUsernamePasswordErrorTxt, "text");
        String errTXT = getTextOfElement(invalidUsernamePasswordErrorTxt);
        System.out.println("Error text is: " + errTXT);
        return errTXT;
    }

    public ProductsPage login(String username, String password) {
        enterUserName(username);
        enterPassword(password);
        return pressLoginButton();
    }

}


