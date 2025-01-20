package com.qa.pages;

import com.qa.AboveNavMenuBar;
import io.appium.java_client.pagefactory.AndroidFindBy;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;
import io.appium.java_client.pagefactory.iOSXCUITFindBy;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;

public class ProductsPage extends AboveNavMenuBar {
    @AndroidFindBy(xpath = "//android.widget.TextView[@text=\"PRODUCTS\"]")
    @iOSXCUITFindBy(xpath = "//XCUIElementTypeOther[@name=\"test-Toggle\"]/parent::*[1]/preceding-sibling::*[1]")
    private WebElement productPageTitle;

    @AndroidFindBy(xpath = "//*[@content-desc=\"test-Item title\" and @text=\"Sauce Labs Backpack\"]/preceding-sibling::android.widget.ImageView")
    @iOSXCUITFindBy(xpath = "//android.view.ViewGroup[@content-desc=\"test-Menu\"]/following-sibling::android.widget.ImageView")
    private WebElement productBackpackImage;

    @AndroidFindBy(xpath = "//*[@content-desc=\"test-Item title\" and @text=\"Sauce Labs Backpack\"]")
    @iOSXCUITFindBy(xpath = "//*[@content-desc=\"test-Item title\" and @text=\"Sauce Labs Backpack\"]")
    private WebElement productBackpackTitleLink;

    @AndroidFindBy(xpath = "(//*[@content-desc=\"test-Price\"])[1]")
    @iOSXCUITFindBy(xpath = "(//*[@content-desc=\"test-Price\"])[1]")
    private WebElement productBackpackPrice;

    @AndroidFindBy(xpath = "(//android.widget.TextView[@text=\"ADD TO CART\"])[1]")
    @iOSXCUITFindBy(xpath = "(//android.widget.TextView[@text=\"ADD TO CART\"])[1]")
    private WebElement productBackpackAddToCart;

    public ProductsPage() {
        PageFactory.initElements(new AppiumFieldDecorator(driver), this);
    }

    public String getProductHeaderTitle() {
//        return getAttributOfElement(productPageTitle, "text");
        String productHeaderTitle = getTextOfElement(productPageTitle);
        System.out.println("Product Page header is: " + productHeaderTitle);
        return productHeaderTitle;
    }

    public String getProductTitle() {
//        return getAttributOfElement(productBackpackTitleLink, "text");
        String productTitle = getTextOfElement(productBackpackTitleLink);
        System.out.println("Product Title is: " + productTitle);
        return productTitle;
    }

    public String getProductPrice() {
//        return getAttributOfElement(productBackpackPrice, "text");
        String productPrice = getTextOfElement(productBackpackPrice);
        System.out.println("Product Price is: " + productPrice);
        return productPrice;
    }

    public ProductsDetailsPage pressProductTitleLink() {
        System.out.println("Click on BackPack Title Link");
        clickOnElement(productBackpackTitleLink);
        return new ProductsDetailsPage();
    }

}
