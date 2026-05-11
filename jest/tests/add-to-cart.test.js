const { Builder, By } = require("selenium-webdriver");

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
    const productElement = await driver.findElement(By.css("#\\33  > p"));
    const productToAdd = await productElement.getText();

    // click on add to cart
    await driver.findElement(By.css("#\\33  > .shelf-item__buy-btn")).click();

    // get name of item in cart
    const cartItemElement = await driver.findElement(
      By.css(
        "#__next > div > div > div.float-cart.float-cart--open > " +
          "div.float-cart__content > div.float-cart__shelf-container > " +
          "div > div.shelf-item__details > p.title",
      ),
    );
    const productInCart = await cartItemElement.getText();

    // check if product in cart is same as one added
    expect(productInCart).toBe(productToAdd);
  });
});
