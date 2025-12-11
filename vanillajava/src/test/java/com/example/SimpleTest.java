package com.example;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.net.URL;
import java.time.Duration;

public class SimpleTest {

  private static final String HUB_URL = "http://localhost:4444/wd/hub";

  public static void main(String[] args) {
    SimpleTest test = new SimpleTest();
    
    try {
      System.out.println("Starting Add to Cart test...");
      test.testAddToCartBStackDemo();
      System.out.println("Add to Cart test completed successfully!");
      
      System.out.println("\nStarting Checkout Flow test...");
      test.testCheckoutFlowBStackDemo();
      System.out.println("Checkout Flow test completed successfully!");
      
      System.out.println("\nAll tests passed!");
    } catch (Exception e) {
      System.err.println("Test failed: " + e.getMessage());
      e.printStackTrace();
      System.exit(1);
    }
  }

  private WebDriver createDriver() throws Exception {
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
      WebDriver driver = new RemoteWebDriver(new URL(HUB_URL), chromeOptions);

      driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
      driver.manage().window().maximize();
      System.out.println("Successfully connected to Selenium Grid!");
      return driver;
    } catch (Exception e) {
      System.err.println("Error during setup: " + e.getMessage());
      e.printStackTrace();
      throw e;
    }
  }

  public void testAddToCartBStackDemo() throws Exception {
    WebDriver driver = createDriver();
    
    try {
      driver.get("https://bstackdemo.com/");

      WebElement productNameElem = driver.findElement(By.cssSelector("#\\33  > p"));

      String productToAdd = productNameElem.getText();

      WebElement addToCartBtn = driver.findElement(By.cssSelector("#\\33 > .shelf-item__buy-btn"));
      addToCartBtn.click();
      
      try { Thread.sleep(500); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }

      WebElement productInCartElem = driver.findElement(By.cssSelector("#__next > div > div > div.float-cart.float-cart--open > div.float-cart__content > div.float-cart__shelf-container > div > div.shelf-item__details > p.title"));
      String productInCart = productInCartElem.getText();

      if (!productToAdd.equals(productInCart)) {
        throw new AssertionError("Expected: " + productToAdd + ", but got: " + productInCart);
      }
      System.out.println("Test passed: Add to cart works!");
    } finally {
      if (driver != null) {
        driver.quit();
        System.out.println("Browser closed");
      }
    }
  }

  public void testCheckoutFlowBStackDemo() throws Exception {
    WebDriver driver = createDriver();
    try {
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
      
      if (!"Your Order has been successfully placed.".equals(checkoutMessage)) {
        throw new AssertionError("Expected: 'Your Order has been successfully placed.', but got: " + checkoutMessage);
      }
      System.out.println("Test passed: Checkout flow works!");
    } finally {
      if (driver != null) {
        driver.quit();
        System.out.println("Browser closed");
      }
    }
  }
}

