package com.ak.homework.tests;

import com.ak.homework.util.Credential;
import com.ak.homework.util.CredentialReader;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;

public class PurchaseTest {

    private static final Logger logger = LoggerFactory.getLogger(PurchaseTest.class);
    private WebDriver driver;
    private WebDriverWait wait;

    @BeforeEach
    public void setUp() {
        // Set up ChromeDriver using WebDriverManager
        WebDriverManager.chromedriver().setup();
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--incognito");
        driver = new ChromeDriver(options);
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        logger.info("WebDriver initialized with incognito mode");
    }

    @AfterEach
    public void tearDown() {
        // Close the browser if driver is not null
        if (driver != null) {
            logger.info("Closing WebDriver");
            driver.quit();
        }
    }

    @Test
    public void testLogin() {
        logger.info("Starting testLogin");
        Credential cred = CredentialReader.readCredential();
        logger.info("Navigating to https://www.saucedemo.com");
        driver.get("https://www.saucedemo.com");

        logger.info("Logging in with username: {}", cred.getUsername());
        WebElement usernameInput = driver.findElement(By.id("user-name"));
        WebElement passwordInput = driver.findElement(By.id("password"));
        WebElement loginButton = driver.findElement(By.id("login-button"));

        usernameInput.sendKeys(cred.getUsername());
        passwordInput.sendKeys(cred.getPassword());
        loginButton.click();
        logger.info("Login completed");

        logger.info("Adding items to cart");
        WebElement backpackButton = wait.until(ExpectedConditions.elementToBeClickable(By.id("add-to-cart-sauce-labs-backpack")));
        WebElement jacketButton = wait.until(ExpectedConditions.elementToBeClickable(By.id("add-to-cart-sauce-labs-fleece-jacket")));

        backpackButton.click();
        jacketButton.click();
        logger.info("Items added to cart");

        logger.info("Verifying cart item count");
        WebElement cartBadge = driver.findElement(By.className("shopping_cart_badge"));
        String itemCount = cartBadge.getText();
        Assertions.assertEquals("2", itemCount, "Expected 2 items in the cart, but found: " + itemCount);
        logger.info("Cart contains 2 items");

        logger.info("Navigating to cart");
        WebElement cartIcon = driver.findElement(By.className("shopping_cart_link"));
        cartIcon.click();
        logger.info("Navigated to cart");

        logger.info("Proceeding to checkout");
        WebElement checkoutButton = driver.findElement(By.id("checkout"));
        checkoutButton.click();
        logger.info("Navigated to checkout");

        logger.info("Filling checkout form");
        driver.findElement(By.id("first-name")).sendKeys("Test");
        driver.findElement(By.id("last-name")).sendKeys("User");
        driver.findElement(By.id("postal-code")).sendKeys("54355");
        driver.findElement(By.id("continue")).click();
        logger.info("Checkout form submitted");

        logger.info("Completing purchase");
        driver.findElement(By.id("finish")).click();
        WebElement thankYou = driver.findElement(By.className("complete-header"));
        String thankYouText = thankYou.getText();
        Assertions.assertEquals("Thank you for your order!", thankYouText, "Thank you message not displayed correctly");
        logger.info("Purchase completed successfully: {}", thankYouText);
    }

    @Test
    public void testAccessDeniedAndFooterValidation() {
        logger.info("Starting testAccessDeniedAndFooterValidation");
        logger.info("Navigating to https://www.saucedemo.com/inventory.html");
        driver.get("https://www.saucedemo.com/inventory.html");

        logger.info("Verifying access denied error message");
        WebElement errorMsg = driver.findElement(By.cssSelector("[data-test='error']"));
        String errorText = errorMsg.getText();
        Assertions.assertTrue(errorText.contains("You can only access '/inventory.html' when you are logged in"),
                "Expected error message not displayed: " + errorText);
        logger.info("Access denied error message verified: {}", errorText);

        logger.info("Logging in with username: standard_user");
        WebElement username = driver.findElement(By.id("user-name"));
        WebElement password = driver.findElement(By.id("password"));
        WebElement loginButton = driver.findElement(By.id("login-button"));

        username.sendKeys("standard_user");
        password.sendKeys("secret_sauce");
        loginButton.click();
        logger.info("Login completed");

        logger.info("Waiting for inventory page to load");
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("inventory_list")));
        logger.info("Inventory page loaded");

        logger.info("Scrolling to footer and verifying content");
        WebElement footer = driver.findElement(By.className("footer_copy"));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", footer);
        String footerText = footer.getText();
        Assertions.assertTrue(footerText.contains("2025") && footerText.contains("Terms of Service"),
                "Footer does not contain expected text: " + footerText);
        logger.info("Footer content verified: {}", footerText);
    }
}