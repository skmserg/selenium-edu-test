package ru.sskm;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertTrue;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.*;
import java.util.concurrent.TimeUnit;

public class TestChrome {

    private WebDriver driver;
    private WebDriverWait wait;

    @Before
    public void start(){
        driver = new ChromeDriver();
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        wait = new WebDriverWait(driver, 10);
    }

    @Test
    public void listClickTest() {
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

    @Test
    public void countriesTest(){
        driver.get("http://localhost/litecart/admin/"); //идем на страницу магазина
        driver.findElement(By.name("username")).sendKeys("admin"); //логинимся
        driver.findElement(By.name("password")).sendKeys("admin"); //   в форме
        driver.findElement(By.name("login")).click();                          //       авторизации
        driver.findElement(By.cssSelector("li#app-:nth-child(3)")).click();    //выбираем в боковой панели Countries

        int interruptCountry = 0;
        ArrayList<String> countriesList = new ArrayList<>(); //инициализируем лист для хранения имен стран

        WebElement tableCountries = driver.findElement(By.cssSelector(".dataTable")); //находим главный элемент таблицу
        List<WebElement> countriesElementList = tableCountries.findElements(By.cssSelector(".row")); //создаем лист строк из таблицы

        for(int i = 0; i < countriesElementList.size(); i++){ //циклом проходим по списку строк
            WebElement countryRow = countriesElementList.get(i);
            List<WebElement> countryCellsList = countryRow.findElements(By.tagName("td")); //создаем и заполняем список ячейками в которых хранятся различные данные к странам
            countriesList.add(countryCellsList.get(4).getAttribute("textContent")); //заполняем список имен стран их именами вытаскивая из 4 элемента

            if(Integer.parseInt(countryCellsList.get(5).getAttribute("textContent")) > 0){ //проверяем количество зон у каждой страны, если больше 0
                interruptCountry = Integer.parseInt(countryCellsList.get(2).getAttribute("textContent"));
                countryCellsList.get(4).findElement(By.tagName("a")).click(); //нажимаем на ссылку
                WebElement tableZones = driver.findElement(By.id("table-zones")); //по id находим элемент таблицу зон
                List<WebElement> zonesElementList = tableZones.findElements(By.tagName("tr")); //создаем список из строк таблицы зон
                List<String> zoneList = new ArrayList<>(); //инициалтзтруем список зон

                for(int j = 1; j < zonesElementList.size() - 1; j++){ //циклом проходим по списку зон
                    WebElement zoneRow = zonesElementList.get(j); //вытаскиваем строку таблицы, как WebElement
                    List<WebElement> zoneCellsList = zoneRow.findElements(By.tagName("td")); //создаем и заполняем список ячейками в которых содержаться различные данные
                    zoneList.add(zoneCellsList.get(2).getAttribute("textContent")); //получаем наименование территории
                }

                for(String zone : zoneList){ //проверяем сортировку в списке зон
                    compareTo(zone);
                }
                driver.findElement(By.cssSelector("span.button-set button[name='cancel']")).click();
            }
            System.out.println(interruptCountry);
        }
        for(String country : countriesList){ //проверяем сортировку в списке стран
            compareTo(country);
        }
    }

    @Test
    public void conformityTest(){
        driver.get("http://localhost/litecart/en/");
        driver.findElement(By.id("box-campaigns")).findElement(By.tagName("a")).click();

    }

    public boolean compareTo(String current){
        String previous ="";
        current = "";
        if(current.compareTo(previous) < 0)
            return false;
        else
            return true;
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
