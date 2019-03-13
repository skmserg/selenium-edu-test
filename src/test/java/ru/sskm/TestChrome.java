package ru.sskm;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertTrue;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.concurrent.TimeUnit;

import static org.openqa.selenium.support.ui.ExpectedConditions.presenceOfElementLocated;


public class TestChrome {

    private WebDriver driver;
    private WebDriverWait wait;

    @Before
    public void start(){
        driver = new ChromeDriver();
        driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
        wait = new WebDriverWait(driver, 10);
    }

    @Test
    public void testChrome() {
        driver.get("http://localhost/litecart/admin/");
        driver.findElement(By.name("username")).sendKeys("admin");
        driver.findElement(By.name("password")).sendKeys("admin");
        driver.findElement(By.name("login")).click();

        List<WebElement> listLiApp = driver.findElements(By.cssSelector("li#app-"));
        for (int i = 1; i < listLiApp.size() + 1; i++) {
            driver.findElement(By.cssSelector("li#app-:nth-child(" + i + ")")).click();
            assertTrue(isPresent(By.cssSelector("h1")));

            List<WebElement> listLiDocs = driver.findElements(By.cssSelector("ul.docs li"));
            for (int j = 1; j < listLiDocs.size() + 1; j++) {
                driver.findElement(By.cssSelector("ul.docs li:nth-child(" + j + ")")).click();
                assertTrue(isPresent(By.cssSelector("h1")));
            }
        }
    }

    @Test
    public void stickerTest(){
        driver.get("http://localhost/litecart/en/rubber-ducks-c-1/");
        List<WebElement> duckList = driver.findElements(By.cssSelector(".product"));
        for(int i = 1; i < duckList.size(); i++){
            assertTrue(isPresent(By.cssSelector(".sticker")));
        }
    }

    public boolean isPresent(By locator){
        try{
            driver.findElement(locator);
            return true;
        }catch (NoSuchElementException ex){
            return false;
        }
    }

    @After
    public void stop(){
        driver.quit();
        driver = null;
    }



}
