const { Builder, By } = require("selenium-webdriver");

const HUB_URL = "http://localhost:4444/wd/hub";

describe("BStackDemo test checkout flow", () => {
  let driver;

  beforeAll(async () => {
    driver = await new Builder()
      .usingServer(HUB_URL)
      .forBrowser("chrome")
      .build();
  });

  afterAll(async () => {
    if (driver) await driver.quit();
  });

  test("should complete checkout flow successfully", async () => {
    // visit the site
    await driver.get("https://bstackdemo.com/");

    // sign in
    await driver.findElement(By.id("signin")).click();
    await driver.findElement(By.css("#username svg")).click();
    await driver.findElement(By.id("react-select-2-option-0-0")).click();
    await driver.findElement(By.css("#password svg")).click();
    await driver.findElement(By.id("react-select-3-option-0-0")).click();

    await driver.findElement(By.id("login-btn")).click();
    await driver.sleep(500);

    // click on buy item
    await driver.findElement(By.css("#\\31  > .shelf-item__buy-btn")).click();
    await driver.findElement(By.css(".float-cart__close-btn")).click();
    await driver.findElement(By.css("#\\32  > .shelf-item__buy-btn")).click();
    await driver.findElement(By.css(".buy-btn")).click();

    // add address details
    await driver.findElement(By.id("firstNameInput")).sendKeys("first");
    await driver.findElement(By.id("lastNameInput")).sendKeys("last");
    await driver.findElement(By.id("addressLine1Input")).sendKeys("address");
    await driver.findElement(By.id("provinceInput")).sendKeys("province");
    await driver.findElement(By.id("postCodeInput")).sendKeys("pincode");

    // checkout
    await driver.findElement(By.id("checkout-shipping-continue")).click();
    await driver
      .findElement(By.xpath("//*[contains(text(),'Continue')]"))
      .click();
    await driver
      .findElement(By.xpath("//*[contains(text(),'Orders')]"))
      .click();
  });
});
