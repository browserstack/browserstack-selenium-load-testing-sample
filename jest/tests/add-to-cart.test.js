const { Builder, By, until } = require("selenium-webdriver");

// On BrowserStack Load Testing, the Selenium pod exposes a hub at
// http://localhost:4444/wd/hub. A plain `forBrowser("chrome").build()`
// would try to spawn a local browser the pod does not have.
const HUB_URL = "http://localhost:4444/wd/hub";

describe("BStackDemo test add to cart", () => {
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

  test("should add product to cart successfully", async () => {
    // visit the site
    await driver.get("https://bstackdemo.com/");

    // get name of product we want to add to cart
    const productNameElem = await driver.wait(
      until.elementLocated(By.xpath("//*[@id='3']/p")),
      10000,
    );
    await driver.wait(until.elementIsVisible(productNameElem), 10000);
    const productToAdd = (await productNameElem.getText()).trim();

    // click on add to cart
    const addToCartBtn = await driver.findElement(
      By.css("#\\33  > .shelf-item__buy-btn"),
    );
    await addToCartBtn.click();

    // get name of item in cart
    const productInCartElem = await driver.wait(
      until.elementLocated(
        By.css(
          ".float-cart.float-cart--open .float-cart__shelf-container .shelf-item__details p.title",
        ),
      ),
      10000,
    );
    await driver.wait(until.elementIsVisible(productInCartElem), 10000);
    const productInCart = (await productInCartElem.getText()).trim();

    // check if product in cart is same as one added
    expect(productInCart).toBe(productToAdd);
  });
});
