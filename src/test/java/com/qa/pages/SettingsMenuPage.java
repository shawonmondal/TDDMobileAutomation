package com.qa.pages;

import com.qa.BaseTest;
import com.qa.utils.TestUtils;
import io.appium.java_client.pagefactory.AndroidFindBy;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;
import io.appium.java_client.pagefactory.iOSXCUITFindBy;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;

public class SettingsMenuPage extends BaseTest {
    TestUtils testUtils = new TestUtils();
    @AndroidFindBy(accessibility = "test-LOGOUT")
    @iOSXCUITFindBy(xpath = "//android.view.ViewGroup[@content-desc=\"test-Menu\"]/android.view.ViewGroup/android.widget.ImageView")
    private WebElement logoutButton;


    public SettingsMenuPage() {
        PageFactory.initElements(new AppiumFieldDecorator(getDriver()), this);
    }

    public LoginPage pressLogoutButton() {
        clickOnElement(logoutButton, "Click on: Logout Button");
        return new LoginPage();
    }
}
