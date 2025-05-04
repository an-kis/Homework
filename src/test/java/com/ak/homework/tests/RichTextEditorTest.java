package com.ak.homework.tests;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;

public class RichTextEditorTest {

    private static final Logger logger = LoggerFactory.getLogger(RichTextEditorTest.class);
    private WebDriver driver;
    private WebDriverWait wait;

    @BeforeEach
    public void setUp() {
        // Set up ChromeDriver using WebDriverManager
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        logger.info("WebDriver initialized and browser maximized");
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
    public void testFormattedTyping() {
        // Navigate to the online HTML editor
        logger.info("Navigating to https://onlinehtmleditor.dev/");
        driver.get("https://onlinehtmleditor.dev/");

        // Wait for the editable area to be clickable and click it
        logger.info("Waiting for editable area to be clickable");
        WebElement editableArea = wait.until(ExpectedConditions.elementToBeClickable(
                By.cssSelector("div[contenteditable='true']")));
        editableArea.click();
        logger.info("Editable area clicked");

        // 1. Enable bold and type "Automation "
        logger.info("Enabling bold and typing 'Automation '");
        editableArea.sendKeys(Keys.chord(Keys.CONTROL, "b"));
        editableArea.sendKeys("Automation ");
        editableArea.sendKeys(Keys.chord(Keys.CONTROL, "b")); // Disable bold

        // 2. Enable underline and type "Test"
        logger.info("Enabling underline and typing 'Test'");
        editableArea.sendKeys(Keys.chord(Keys.CONTROL, "u"));
        editableArea.sendKeys("Test");
        editableArea.sendKeys(Keys.chord(Keys.CONTROL, "u")); // Disable underline

        // 3. Type " Example" without formatting
        logger.info("Typing ' Example' without formatting");
        editableArea.sendKeys(" Example");

        // Wait for the formatted text to appear in innerHTML
        logger.info("Waiting for formatted text to appear in innerHTML");
        wait.until(ExpectedConditions.attributeContains(editableArea, "innerHTML", "<strong>Automation "));

        // Verify the formatted content
        logger.info("Verifying formatted content");
        String content = editableArea.getAttribute("innerHTML");
        Assertions.assertTrue(content.contains("<strong>Automation "), "Bold formatting missing");
        Assertions.assertTrue(content.contains("<u>Test</u>"), "Underline formatting missing");
        Assertions.assertTrue(content.contains("Example"), "Plain text missing");
        logger.info("Text formatting verified successfully: {}", content);
    }
}