package com.qa.pages;

import com.qa.BaseTest;
import io.appium.java_client.pagefactory.AndroidFindBy;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;
import io.appium.java_client.pagefactory.iOSXCUITFindBy;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;

public class SettingsMenuPage extends BaseTest {
    @AndroidFindBy(accessibility = "test-LOGOUT")
    @iOSXCUITFindBy(xpath = "//android.view.ViewGroup[@content-desc=\"test-Menu\"]/android.view.ViewGroup/android.widget.ImageView")
    private WebElement logoutButton;


    public SettingsMenuPage() {
        PageFactory.initElements(new AppiumFieldDecorator(driver), this);
    }

    public LoginPage pressLogoutButton() {
        System.out.println("Click on: LogOut");
        clickOnElement(logoutButton);
        return new LoginPage();
    }
}
