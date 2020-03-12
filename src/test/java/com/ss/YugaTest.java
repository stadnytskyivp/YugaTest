package com.ss;

import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.Select;
import org.testng.Assert;
import org.testng.annotations.*;
import org.testng.asserts.SoftAssert;

import java.util.concurrent.TimeUnit;

public class YugaTest {

    private static final String myCity = "Львів";
    private static final String EXPECTED_EMPTY_PHONE_ERROR = "Невірний номер телефону";
    private static final String EXPECTED_EMPTY_FIELD_ERROR = "Поле необхідне для заповнення";
    private static final String file = System.getProperty("user.dir") + "\\src\\main\\resources\\chromedriver.exe";
    private static WebDriver driver;

    @BeforeClass
    void setup() {
        System.setProperty("webdriver.chrome.driver", file);
        // BROWSER == DRIVER
        driver = new ChromeDriver();
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        driver.manage().window().maximize();
        driver.get("https://yuga.com.ua/");
    }

    @Test(priority = 1)
    void searchTest() {

        // UA internalization
        driver.findElement(By.cssSelector(".flag.flag_ua a")).click();
        // Go to tours search
        driver.findElement(By.cssSelector("#menu-item-2530")).click();
        // choose city FROM
        Select fromCity = new Select(driver.findElement(By.cssSelector("select[ng-if='selectedCountry.name']")));
        fromCity.selectByVisibleText(myCity);
        // dropdown list "WHERE TO GO"
        driver.findElement(By.xpath("//span[@class='fd-input_btn_short']")).click();
        // scroll window down
        JavascriptExecutor jse = (JavascriptExecutor)driver;
        jse.executeScript("window.scrollBy(0,100)");
        // choose city "WHERE TO GO"
        driver.findElement(By.xpath("//div[@class='fd-location_b fd-location_city fd-desktop ss-container']//label[@title='Хургада']")).click();
        // USE FILTERS!
        driver.findElement(By.cssSelector(".fd-location a.filter-btn")).click();
        // click on SEARCH
        driver.findElement(By.xpath("//div[@class='fd-item fd-item__md']//a[@class='fd-find ng-binding ng-scope']")).click();
        // click on 'tour details' button
        driver.findElement(By.xpath("(//a[text()='Деталі туру'])[1]")).click();
        // click on 'leave request' button
        driver.findElement(By.xpath("//a[@class='fd-single_request notranslate ng-binding']")).click();
        // click on 'send request' button
        driver.findElement(By.xpath("//a[@class='fd-form_btn notranslate ng-binding']")).click();

        // find phone error message
        WebElement emptyNameErrMessage = driver.findElement(By.xpath("(//span[@class='fd-form_error_text ng-binding'])[1]"));
        String strNameError = emptyNameErrMessage.getText();
        // find phone error message
        WebElement emptyEmailErrMessage = driver.findElement(By.xpath("(//span[@class='fd-form_error_text ng-binding'])[2]"));
        String strEmailError = emptyEmailErrMessage.getText();
        // find phone error message
        WebElement emptyPhoneErrMessage = driver.findElement(By.xpath("(//span[@class='fd-form_error_text ng-binding'])[3]"));
        String strPhoneError = emptyPhoneErrMessage.getText();

        SoftAssert softAssert = new SoftAssert();
        softAssert.assertTrue(EXPECTED_EMPTY_FIELD_ERROR.contains(strNameError), "Name error message didn't match");
        softAssert.assertEquals(strEmailError, EXPECTED_EMPTY_FIELD_ERROR, "Email error message didn't match");
        softAssert.assertEquals(strPhoneError, EXPECTED_EMPTY_PHONE_ERROR, "Phone error message didn't match");
        softAssert.assertAll();
    }

    @AfterClass
    void tearDown() {
        driver.quit();
    }
}
