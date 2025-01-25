package com.qa;

import com.qa.pages.SettingsMenuPage;
import com.qa.utils.TestUtils;
import io.appium.java_client.pagefactory.AndroidFindBy;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;
import io.appium.java_client.pagefactory.iOSXCUITFindBy;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;

public class AboveNavMenuBar extends BaseTest{
    TestUtils testUtils = new TestUtils();
    @AndroidFindBy(xpath = "//android.view.ViewGroup[@content-desc=\"test-Menu\"]/android.view.ViewGroup/android.widget.ImageView")
    @iOSXCUITFindBy(xpath ="//android.view.ViewGroup[@content-desc=\"test-Menu\"]/android.view.ViewGroup/android.widget.ImageView")
    private WebElement menuButton;

    @AndroidFindBy(xpath = "//android.view.ViewGroup[@content-desc=\"test-Menu\"]/following-sibling::android.widget.ImageView")
    @iOSXCUITFindBy(xpath = "//android.view.ViewGroup[@content-desc=\"test-Menu\"]/following-sibling::android.widget.ImageView")
    private WebElement swagLabsHeaderTitle;

    @AndroidFindBy(xpath = "//android.view.ViewGroup[@content-desc=\"test-Cart\"]/android.view.ViewGroup")
    @iOSXCUITFindBy(xpath = "//android.view.ViewGroup[@content-desc=\"test-Cart\"]/android.view.ViewGroup")
    private WebElement cartButton;

    public AboveNavMenuBar() {
        PageFactory.initElements(new AppiumFieldDecorator(getDriver()), this);
    }

    public SettingsMenuPage pressMenuButton(){
        clickOnElement(menuButton, "Click on: menuButton");
        return new SettingsMenuPage();
    }
}
