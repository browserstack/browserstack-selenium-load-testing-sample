package com.example;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.net.URL;
import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SimpleTest {

    private WebDriver driver;
    private static final String HUB_URL = "http://localhost:4444/wd/hub";

    @BeforeEach
    void setUp() throws Exception {
        try {
            System.out.println("Setting up Chrome options...");
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

            System.out.println("Connecting to Selenium Grid at: " + HUB_URL);
            driver = new RemoteWebDriver(new URL(HUB_URL), chromeOptions);
            driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
            driver.manage().window().maximize();
            System.out.println("Successfully connected to Selenium Grid!");
        } catch (Exception e) {
            System.err.println("Error during setup: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    @Test
    void testAddToCartBStackDemo() {
        driver.get("https://bstackdemo.com/");

        WebElement productNameElem = driver.findElement(By.cssSelector("#\\33  > p"));
        String productToAdd = productNameElem.getText();

        WebElement addToCartBtn = driver.findElement(By.cssSelector("#\\33 > .shelf-item__buy-btn"));
        addToCartBtn.click();

        WebElement productInCartElem = driver.findElement(By.cssSelector("#__next > div > div > div.float-cart.float-cart--open > div.float-cart__content > div.float-cart__shelf-container > div > div.shelf-item__details > p.title"));
        String productInCart = productInCartElem.getText();

        assertEquals(productToAdd, productInCart);
        System.out.println("Test passed: Add to cart works!");
    }

    @Test
    void testCheckoutFlowBStackDemo() {
        driver.get("https://bstackdemo.com/");

        driver.findElement(By.id("signin")).click();
        driver.findElement(By.cssSelector("#username svg")).click();
        driver.findElement(By.id("react-select-2-option-0-0")).click();
        driver.findElement(By.cssSelector("#password svg")).click();
        driver.findElement(By.id("react-select-3-option-0-0")).click();
        driver.findElement(By.id("login-btn")).click();

        try { Thread.sleep(500); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }

        driver.findElement(By.cssSelector("#\\31 > .shelf-item__buy-btn")).click();
        driver.findElement(By.cssSelector("div.float-cart__close-btn")).click();
        driver.findElement(By.cssSelector("#\\32 > .shelf-item__buy-btn")).click();
        driver.findElement(By.cssSelector(".buy-btn")).click();

        driver.findElement(By.id("firstNameInput")).sendKeys("first");
        driver.findElement(By.id("lastNameInput")).sendKeys("last");
        driver.findElement(By.id("addressLine1Input")).sendKeys("address");
        driver.findElement(By.id("provinceInput")).sendKeys("province");
        driver.findElement(By.id("postCodeInput")).sendKeys("pincode");

        driver.findElement(By.id("checkout-shipping-continue")).click();
        String checkoutMessage = driver.findElement(By.id("confirmation-message")).getText();
        assertEquals("Your Order has been successfully placed.", checkoutMessage);
        System.out.println("Test passed: Checkout flow works!");
    }

    @AfterEach
    void tearDown() {
        if (driver != null) {
            driver.quit();
            System.out.println("Browser closed");
        }
    }
}
