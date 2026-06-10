const { Builder, By, until } = require("selenium-webdriver");

// On BrowserStack Load Testing, the Selenium pod exposes a hub at
// http://localhost:4444/wd/hub. A plain `forBrowser("chrome").build()`
// would try to spawn a local browser the pod does not have.
const HUB_URL = "http://localhost:4444/wd/hub";
const WAIT_MS = 10000;

const waitForClickable = async (driver, locator) => {
  const el = await driver.wait(until.elementLocated(locator), WAIT_MS);
  await driver.wait(until.elementIsVisible(el), WAIT_MS);
  await driver.wait(until.elementIsEnabled(el), WAIT_MS);
  return el;
};

const click = async (driver, locator) => {
  const el = await waitForClickable(driver, locator);
  await el.click();
};

const type = async (driver, locator, text) => {
  const el = await waitForClickable(driver, locator);
  await el.sendKeys(text);
};

describe("BStackDemo test checkout flow", () => {
  let driver;

  beforeAll(async () => {
    driver = await new Builder()
      .usingServer(HUB_URL)
      .forBrowser("chrome")
      .build();
    // Implicit wait so findElement polls instead of failing the moment a
    // React modal/transition hasn't painted yet.
    await driver.manage().setTimeouts({ implicit: WAIT_MS });
  });

  afterAll(async () => {
    if (driver) await driver.quit();
  });

  test("should complete checkout flow successfully", async () => {
    // visit the site
    await driver.get("https://bstackdemo.com/");

    // sign in
    await click(driver, By.id("signin"));
    await click(driver, By.css("#username svg"));
    await click(driver, By.id("react-select-2-option-0-0"));
    await click(driver, By.css("#password svg"));
    await click(driver, By.id("react-select-3-option-0-0"));
    await click(driver, By.id("login-btn"));

    // click first item
    await click(driver, By.css("#\\31  > .shelf-item__buy-btn"));

    // cart overlay opens
    await driver.wait(
      until.elementLocated(By.css(".float-cart.float-cart--open")),
      WAIT_MS,
    );

    // close cart (best-effort)
    try {
      await click(driver, By.css(".float-cart__close-btn"));
    } catch (e) {
      console.log("[WARN] Could not close cart overlay:", e.message);
    }

    // click second item
    await click(driver, By.css("#\\32  > .shelf-item__buy-btn"));

    // proceed to checkout
    await click(driver, By.css(".buy-btn"));

    // add address details
    await type(driver, By.id("firstNameInput"), "first");
    await type(driver, By.id("lastNameInput"), "last");
    await type(driver, By.id("addressLine1Input"), "address");
    await type(driver, By.id("provinceInput"), "province");
    await type(driver, By.id("postCodeInput"), "pincode");

    // checkout
    await click(driver, By.id("checkout-shipping-continue"));

    // continue shopping confirmation
    await click(
      driver,
      By.xpath("//*[@id='checkout-app']/div/div/div/div/a/button"),
    );

    // navigate to orders
    await click(driver, By.xpath("//*[text()='Orders']"));

    expect(true).toBe(true);
  });
});
