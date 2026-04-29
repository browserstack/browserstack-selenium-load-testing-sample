package com.example;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.net.URL;
import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class StepDefs {

    private WebDriver driver;
    private String addedProductName;
    private static final String HUB_URL = "http://localhost:4444/wd/hub";

    @Before
    public void setUp() throws Exception {
        ChromeOptions chromeOptions = new ChromeOptions();
        chromeOptions.addArguments(
                "--headless",
                "--no-first-run",
                "--no-default-browser-check",
                "--disable-extensions",
                "--disable-default-apps",
                "--disable-gpu",
                "--disable-dev-shm-usage",
                "--disable-software-rasterizer",
                "--no-sandbox",
                "--disable-background-timer-throttling",
                "--disable-backgrounding-occluded-windows",
                "--disable-renderer-backgrounding",
                "--disable-features=TranslateUI",
                "--disable-ipc-flooding-protection",
                "--disable-web-security",
                "--disable-features=VizDisplayCompositor",
                "--disable-logging",
                "--silent"
        );

        driver = new RemoteWebDriver(new URL(HUB_URL), chromeOptions);
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        driver.manage().window().maximize();
    }

    @After
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    @Given("I open the BrowserStack demo store")
    public void iOpenTheBrowserStackDemoStore() {
        driver.get("https://bstackdemo.com/");
    }

    @And("I sign in to BrowserStack demo")
    public void iSignInToBrowserStackDemo() throws InterruptedException {
        driver.findElement(By.id("signin")).click();
        driver.findElement(By.cssSelector("#username svg")).click();
        driver.findElement(By.id("react-select-2-option-0-0")).click();
        driver.findElement(By.cssSelector("#password svg")).click();
        driver.findElement(By.id("react-select-3-option-0-0")).click();
        driver.findElement(By.id("login-btn")).click();
        Thread.sleep(500);
    }

    @When("I add the product at index {string} to my cart")
    public void iAddTheProductAtIndexToMyCart(String index) {
        WebElement productNameElem = driver.findElement(By.cssSelector("#\\3" + index + " > p"));
        addedProductName = productNameElem.getText();
        WebElement addToCartBtn = driver.findElement(By.cssSelector("#\\3" + index + " > .shelf-item__buy-btn"));
        addToCartBtn.click();
    }

    @Then("the same product should appear in my cart")
    public void theSameProductShouldAppearInMyCart() {
        By cartProductTitle = By.cssSelector(
                "#__next > div > div > div.float-cart.float-cart--open > div.float-cart__content > div.float-cart__shelf-container > div > div.shelf-item__details > p.title"
        );
        // The cart panel slides in async after the buy click; wait for its text to populate
        // before asserting, otherwise getText() can return an empty string.
        new WebDriverWait(driver, Duration.ofSeconds(10))
                .until(d -> !d.findElement(cartProductTitle).getText().isEmpty());
        assertEquals(addedProductName, driver.findElement(cartProductTitle).getText());
    }

    @And("I close the cart panel")
    public void iCloseTheCartPanel() {
        driver.findElement(By.cssSelector("div.float-cart__close-btn")).click();
    }

    @And("I proceed to checkout")
    public void iProceedToCheckout() {
        driver.findElement(By.cssSelector(".buy-btn")).click();
    }

    @And("I fill in the shipping address")
    public void iFillInTheShippingAddress() {
        driver.findElement(By.id("firstNameInput")).sendKeys("first");
        driver.findElement(By.id("lastNameInput")).sendKeys("last");
        driver.findElement(By.id("addressLine1Input")).sendKeys("address");
        driver.findElement(By.id("provinceInput")).sendKeys("province");
        driver.findElement(By.id("postCodeInput")).sendKeys("pincode");
        driver.findElement(By.id("checkout-shipping-continue")).click();
    }

    @Then("I should see the confirmation message {string}")
    public void iShouldSeeTheConfirmationMessage(String expected) {
        String message = driver.findElement(By.id("confirmation-message")).getText();
        assertEquals(expected, message);
    }
}
