package com.qa.pages;

import com.qa.AboveNavMenuBar;
import com.qa.utils.TestUtils;
import io.appium.java_client.pagefactory.AndroidFindBy;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;
import io.appium.java_client.pagefactory.iOSXCUITFindBy;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;

public class ProductsDetailsPage extends AboveNavMenuBar {
    TestUtils testUtils = new TestUtils();
    @AndroidFindBy(xpath = "//android.widget.TextView[@text=\"BACK TO PRODUCTS\"]")
    @iOSXCUITFindBy(xpath = "//android.widget.TextView[@text=\"BACK TO PRODUCTS\"]")
    private WebElement backToProductButton;

    @AndroidFindBy(xpath = "//android.view.ViewGroup[@content-desc=\"test-Image Container\"]/android.widget.ImageView")
    @iOSXCUITFindBy(xpath = "//android.view.ViewGroup[@content-desc=\"test-Image Container\"]/android.widget.ImageView")
    private WebElement productBackpackImage;


    @AndroidFindBy(xpath = "//android.widget.TextView[@text=\"Sauce Labs Backpack\"]")
    @iOSXCUITFindBy(xpath = "//android.widget.TextView[@text=\"Sauce Labs Backpack\"]")
    private WebElement productBackpackTitle;


    @AndroidFindBy(xpath = "//android.widget.TextView[@text=\"Sauce Labs Backpack\"]/following-sibling::android.widget.TextView")
    @iOSXCUITFindBy(xpath = "//android.widget.TextView[@text=\"Sauce Labs Backpack\"]/following-sibling::android.widget.TextView")
    private WebElement productBackpackDescribtion;

    @AndroidFindBy(accessibility = "test-Price")
    @iOSXCUITFindBy(accessibility = "test-Price")
    private WebElement productBackpackPrice;


    public ProductsDetailsPage() {
        PageFactory.initElements(new AppiumFieldDecorator(getDriver()), this);
    }

    public void landedOnProductDetailsPage(){
        verifyElementPresent((By) backToProductButton);
        verifyElementPresent((By) productBackpackImage);
        verifyElementPresent((By) productBackpackTitle);
        verifyElementPresent((By) productBackpackDescribtion);
    }

    public String getProductTitleText() {
        String productTitle = getTextOfElement(productBackpackTitle, "Product title is: ");
        return productTitle;
    }

    public String getProductDetailsText() {
        String productDetailstext = getTextOfElement(productBackpackDescribtion, "Product description is: ");
//        testUtils.log().info("Product description is: " + productDetailstext);
        return productDetailstext;
    }

    public String scrollToPriceAndValidate(){
        return getTextOfElement(scrollToElement(),"Text of element is: ");
    }

    public ProductsPage pressBackToProductsButton() {
        clickOnElement(backToProductButton,"Navigat back to ProductsPage");
        return new ProductsPage();
    }




}
