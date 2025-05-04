package com.ak.homework.tests;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class IframeTabHandlingTest {

    private static final Logger logger = LoggerFactory.getLogger(IframeTabHandlingTest.class);
    private WebDriver driver;
    private WebDriverWait wait;

    @BeforeEach
    public void setUp() {
        // Set up ChromeDriver using WebDriverManager
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        logger.info("WebDriver initialized and browser maximized");
    }

    @Test
    public void testIframeAndTabHandling() {
        // Navigate to the test page
        logger.info("Navigating to http://demo.guru99.com/test/guru99home");
        driver.get("http://demo.guru99.com/test/guru99home");

        // Find iframe
        logger.info("Waiting for iframe to be present");
        WebElement iframe = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("a077aa5e")));
        logger.info("Iframe found");

        // Switch to iframe and click on image
        logger.info("Switching to iframe and clicking image link");
        driver.switchTo().frame(iframe);
        WebElement imageLink = wait.until(ExpectedConditions.elementToBeClickable(By.tagName("a")));
        imageLink.click();
        logger.info("Image link clicked");
        driver.switchTo().defaultContent();

        // Handle new tab
        logger.info("Handling new tab");
        String originalWindow = driver.getWindowHandle();
        Set<String> allWindows = driver.getWindowHandles();
        for (String handle : allWindows) {
            if (!handle.equals(originalWindow)) {
                driver.switchTo().window(handle);
                driver.close();
                logger.info("New tab closed");
            }
        }
        driver.switchTo().window(originalWindow);
        logger.info("Switched back to original window");

        // Fill email field
        logger.info("Filling email field");
        WebElement emailInput = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("philadelphia-field-email")));
        emailInput.sendKeys("test@example.com");
        logger.info("Email field filled with: test@example.com");

        // Click submit button
        logger.info("Clicking submit button");
        WebElement submitButton = wait.until(ExpectedConditions.elementToBeClickable(By.id("philadelphia-field-submit")));
        submitButton.click();
        logger.info("Submit button clicked");

        // Handle alert
        logger.info("Waiting for alert and verifying text");
        try {
            Alert alert = wait.until(ExpectedConditions.alertIsPresent());
            String alertText = alert.getText();
            assertTrue(alertText.contains("Successfully"), "There is no Successfully word in alert text");
            logger.info("Alert text verified: {}", alertText);
            alert.accept();
            logger.info("Alert accepted");
        } catch (NoAlertPresentException e) {
            logger.error("No alert present after submit");
            throw new AssertionError("No alert after submit");
        }

        // Navigate to Selenium dropdown and select Tooltip
        logger.info("Clicking Selenium dropdown");
        WebElement seleniumDropdown = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//a[contains(text(),'Selenium')]")));
        seleniumDropdown.click();
        logger.info("Selenium dropdown clicked");

        logger.info("Selecting Tooltip option");
        WebElement tooltipOption = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//a[contains(text(),'Tooltip')]")));
        tooltipOption.click();
        logger.info("Tooltip option selected");

        // Verify Download now button
        logger.info("Verifying Download now button");
        WebElement downloadNow = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//a[contains(text(),'Download now')]")));
        assertTrue(downloadNow.isDisplayed(), "There is no Download button");
        logger.info("Download now button is displayed");
    }

    @AfterEach
    public void tearDown() {
        // Close the browser if driver is not null
        if (driver != null) {
            logger.info("Closing WebDriver");
            driver.quit();
        }
    }
}