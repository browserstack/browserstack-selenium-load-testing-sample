package com.example;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.net.URL;
import java.time.Duration;

public class SimpleTest {
    
    private WebDriver driver;
    private static final String HUB_URL = "http://localhost:4444/wd/hub";
    
    @BeforeMethod
    public void setUp() throws Exception {
        try {
            System.out.println("Setting up Chrome options...");
            
            // Try a simpler approach first - just basic capabilities
            DesiredCapabilities caps = new DesiredCapabilities();
            caps.setBrowserName("chrome");
            
            // Add Chrome options as a map
            ChromeOptions chromeOptions = new ChromeOptions();
            // Use timestamp for unique user data directory
            String uniqueUserDataDir = "/tmp/chrome-user-data-" + System.currentTimeMillis();
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
            
            System.out.println("Chrome options configured. User data dir: " + uniqueUserDataDir);
            
            // Merge the chrome options with capabilities
            
            System.out.println("Connecting to Selenium Grid at: " + HUB_URL);
            driver = new RemoteWebDriver(new URL(HUB_URL), chromeOptions);
            // driver = new ChromeDriver(); // For local testing without Grid
            
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
    public void testAddToCartBStackDemo() {
        // Visit BStackDemo
        driver.get("https://bstackdemo.com/");

    // Get name of product to add to cart using provided XPath //*[@id="3"]/p
    WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    WebElement productNameElem = wait.until(
        ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id='3']/p"))
    );
    String productToAdd = productNameElem.getText().trim();

        // Click on add to cart
        WebElement addToCartBtn = driver.findElement(By.cssSelector("#\\33 > .shelf-item__buy-btn"));
        addToCartBtn.click();

    // Wait for cart to open and get the product name inside the cart
    WebElement productInCartElem = wait.until(
        ExpectedConditions.visibilityOfElementLocated(
            By.cssSelector(".float-cart.float-cart--open .float-cart__shelf-container .shelf-item__details p.title")
        )
    );
    String productInCart = productInCartElem.getText().trim();

        // Check if product in cart is same as one added
    Assert.assertEquals(productInCart, productToAdd, "Product in cart should match product selected.");
        System.out.println("Test passed: Add to cart works!");
    }

    @Test
    public void testCheckoutFlowBStackDemo() {
        // Visit BStackDemo
        driver.get("https://bstackdemo.com/");

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        // Sign in
        driver.findElement(By.id("signin")).click();
        driver.findElement(By.cssSelector("#username svg")).click();
        driver.findElement(By.id("react-select-2-option-0-0")).click();
        driver.findElement(By.cssSelector("#password svg")).click();
        driver.findElement(By.id("react-select-3-option-0-0")).click();
        driver.findElement(By.id("login-btn")).click();

        // Wait for login
        try { Thread.sleep(500); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }

        // Click on first item (wait until clickable to avoid interactability issues)
        wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("#\\31 > .shelf-item__buy-btn"))).click();

        // Wait for cart overlay to open
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".float-cart.float-cart--open")));

        // Attempt to close cart safely (selector simplified to class only; element may not be a div)
        try {
            WebElement closeBtn = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector(".float-cart__close-btn")));
            closeBtn.click();
        } catch (Exception e) {
            System.out.println("[WARN] Could not close cart overlay: " + e.getMessage());
        }

        // Click on second item
        wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("#\\32 > .shelf-item__buy-btn"))).click();

        // Proceed to checkout (Buy button)
        wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector(".buy-btn"))).click();

        // Add address details
        driver.findElement(By.id("firstNameInput")).sendKeys("first");
        driver.findElement(By.id("lastNameInput")).sendKeys("last");
        driver.findElement(By.id("addressLine1Input")).sendKeys("address");
        driver.findElement(By.id("provinceInput")).sendKeys("province");
        driver.findElement(By.id("postCodeInput")).sendKeys("pincode");

    // Checkout
    wait.until(ExpectedConditions.elementToBeClickable(By.id("checkout-shipping-continue"))).click();
    // Updated: use provided XPath for the Continue Shopping button
    WebElement continueShopping = wait.until(
        ExpectedConditions.elementToBeClickable(By.xpath("//*[@id='checkout-app']/div/div/div/div/a/button"))
    );
    continueShopping.click();
        driver.findElement(By.xpath("//*[text()='Orders']")).click();

        System.out.println("Test passed: Checkout flow works!");
    }
    
    @AfterMethod
    public void tearDown() {
        if (driver != null) {
            driver.quit();
            System.out.println("Browser closed");
        }
    }
}
src/test/java/com/example/SimpleTest.java