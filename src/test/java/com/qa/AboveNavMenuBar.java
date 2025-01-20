package com.qa;

import com.qa.pages.SettingsMenuPage;
import io.appium.java_client.pagefactory.AndroidFindBy;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;
import io.appium.java_client.pagefactory.iOSXCUITFindBy;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;

public class AboveNavMenuBar extends BaseTest{
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
        PageFactory.initElements(new AppiumFieldDecorator(driver), this);
    }

    public SettingsMenuPage pressMenuButton(){
        System.out.println("Click on: menuButton");
        clickOnElement(menuButton);
        return new SettingsMenuPage();
    }
}
