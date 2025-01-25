package com.qa.pages;

import com.qa.BaseTest;
import com.qa.utils.TestUtils;
import io.appium.java_client.AppiumBy;
import io.appium.java_client.pagefactory.AndroidFindBy;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;
import io.appium.java_client.pagefactory.iOSXCUITFindBy;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;

public class LoginPage extends BaseTest {

    TestUtils testUtils = new TestUtils();

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
//        testUtils.log().info("Login with " + username);
        enterTextinTextField(usernameTextField, username, "Login with " + username);
        return this;
    }

    public LoginPage enterPassword(String password) {
//        testUtils.log().info("Password is " + password);
        enterTextinTextField(passwordTextField, password, "Password is " + password);
        return this;
    }

    public ProductsPage pressLoginButton() {
        clickOnElement(loginButton, "Press login button");
        return new ProductsPage();
    }

    public String getErrorText() {
        String errTXT = getTextOfElement(invalidUsernamePasswordErrorTxt, "Error text is: ");
        return errTXT;
    }

    public ProductsPage login(String username, String password) {
        enterUserName(username);
        enterPassword(password);
        return pressLoginButton();
    }

}


