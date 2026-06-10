// Jest runs with --runInBand on BrowserStack so the single Selenium session
// per pod is never contended. testEnvironment must be "node" — Jest drives a
// remote WebDriver, not the in-process DOM.
module.exports = {
  testEnvironment: "node",
  testMatch: ["**/tests/**/*.test.js", "**/?(*.)+(spec|test).js"],
  testTimeout: 120000,
};
