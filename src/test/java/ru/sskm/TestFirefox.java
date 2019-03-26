package ru.sskm;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.*;
import java.util.*;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.openqa.selenium.support.ui.ExpectedConditions.numberOfWindowsToBe;

public class TestFirefox {

    private WebDriver driver;
    private WebDriverWait wait;

    @Before
    public void start(){
        FirefoxOptions options = new FirefoxOptions().setLegacy(false);
        driver = new FirefoxDriver(options);
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        wait = new WebDriverWait(driver, 10);
    }

    @Test
    public void listClickTest(){
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

        WebElement table = driver.findElement(By.cssSelector(".dataTable")); //находим главный элемент таблицу
        List<WebElement> countElementList = table.findElements(By.cssSelector(".row")); //создаем лист строк из таблицы

        for(int i = 0; i < countElementList.size(); i++){ //циклом проходим по списку строк

            WebElement tableCountries = driver.findElement(By.cssSelector(".dataTable")); //находим главный элемент таблицу
            List<WebElement> countriesElementList = tableCountries.findElements(By.cssSelector(".row")); //создаем лист строк из таблицы
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
        }
        for(String country : countriesList){ //проверяем сортировку в списке стран
            compareTo(country);
        }
    }

    @Test
    public void conformityTest() {
        driver.get("http://localhost/litecart/en/");


        Map<String, String> mapMainPage = new HashMap<>();
        Map<String, String> mapProductPage = new HashMap<>();
        String productName = "Product Name";
        String regularPrice = "Regular Price";
        String campaignPrice = "Campaign Price";
        String colorRegularPrice = "Color of Regular Price";
        String colorCampaignPrice = "Color of Campaign Price";
        String fontWeightPrice = "Font Weight Price";

        WebElement campaignsSection = driver.findElement(By.id("box-campaigns"));
        String productNameMainPage = campaignsSection.findElement(By.cssSelector(".name")).
                getAttribute("textContent");
        String regularPriceMainPage = campaignsSection.findElement(By.cssSelector(".regular-price")).
                getAttribute("textContent");
        String colorRegularPriceMainPage = campaignsSection.findElement(By.cssSelector(".regular-price")).
                getCssValue("color");
        String campaignPriceMainPage = campaignsSection.findElement(By.cssSelector(".campaign-price")).
                getAttribute("textContent");
        String colorCampaignPriceMainPage = campaignsSection.findElement(By.cssSelector(".campaign-price")).
                getCssValue("color");
        String fontWeightCampaignPriceMainPage = campaignsSection.findElement(By.cssSelector(".campaign-price")).
                getCssValue("font-weight");

        mapMainPage.put(productName, productNameMainPage);
        mapMainPage.put(regularPrice, regularPriceMainPage);
        mapMainPage.put(campaignPrice, campaignPriceMainPage);
//        mapMainPage.put(colorRegularPrice, colorRegularPriceMainPage);
//        mapMainPage.put(colorCampaignPrice, colorCampaignPriceMainPage);
//        mapMainPage.put(fontWeightPrice, fontWeightCampaignPriceMainPage);

        compareString(colorRegularPriceMainPage, colorCampaignPriceMainPage);

        campaignsSection.findElement(By.cssSelector("a:first-child")).click();
        driver.findElement(By.cssSelector("h1"));
        String productNameProductPage = driver.findElement(By.cssSelector("h1")).getAttribute("textContent");
        String regularPriceProductPage = driver.findElement(By.cssSelector(".regular-price")).
                getAttribute("textContent");
        String colorRegularPriceProductPage = driver.findElement(By.cssSelector(".regular-price")).
                getCssValue("color");
        String campaignPriceProductPage = driver.findElement(By.cssSelector(".campaign-price")).
                getAttribute("textContent");
        String colorCampaignPriceProductPage = driver.findElement(By.cssSelector(".campaign-price")).
                getCssValue("color");
        String fontWeightCampaignPriceProductPage = driver.findElement(By.cssSelector(".campaign-price")).
                getCssValue("font-weight");

        mapProductPage.put(productName, productNameProductPage);
        mapProductPage.put(regularPrice, regularPriceProductPage);
        mapProductPage.put(campaignPrice, campaignPriceProductPage);
//        mapProductPage.put(colorRegularPrice, colorRegularPriceProductPage);
//        mapProductPage.put(colorCampaignPrice, colorCampaignPriceProductPage);
//        mapProductPage.put(fontWeightPrice, fontWeightCampaignPriceProductPage);

//        mapMainPage.put(colorRegularPrice, colorRegularPriceMainPage);
//        mapMainPage.put(colorCampaignPrice, colorCampaignPriceMainPage);
//        mapMainPage.put(fontWeightPrice, fontWeightCampaignPriceMainPage);

        compareMap(mapMainPage, mapProductPage);

        assertFalse(compareColor(colorRegularPriceProductPage, colorCampaignPriceProductPage));
        assertFalse(compareColor(colorRegularPriceMainPage, colorCampaignPriceMainPage));


    }

    @Test
    public void geoZoneTest(){
        driver.get("http://localhost/litecart/admin/"); //идем на страницу магазина
        driver.findElement(By.name("username")).sendKeys("admin"); //логинимся
        driver.findElement(By.name("password")).sendKeys("admin"); //   в форме
        driver.findElement(By.name("login")).click();                          //       авторизации

        driver.findElement(By.cssSelector("li#app-:nth-child(6)")).click();    //выбираем в боковой панели Geo Zones
        int interruptGeoZone = 0;
        ArrayList<String> geoZoneList = new ArrayList<>(); //инициализируем лист для хранения имен стран

        WebElement table = driver.findElement(By.cssSelector(".dataTable")); //находим главный элемент таблицу
        List<WebElement> countElementList = table.findElements(By.cssSelector(".row")); //создаем лист строк из таблицы

        for(int i = interruptGeoZone; i < countElementList.size(); i++){
            WebElement tableGeoZones = driver.findElement(By.cssSelector(".dataTable")); //находим главный элемент таблицу
            List<WebElement> geoZonesElementList = tableGeoZones.findElements(By.cssSelector("tr.row")); //создаем лист строк из таблицы
            WebElement geoZoneRow = geoZonesElementList.get(i);
            List<WebElement> geoZoneCellsList = geoZoneRow.findElements(By.tagName("td")); //создаем и заполняем список ячейками в которых хранятся различные данные к странам
            interruptGeoZone = Integer.parseInt(geoZoneCellsList.get(1).getAttribute("textContent"));
            geoZoneCellsList.get(2).findElement(By.tagName("a")).click(); //нажимаем на ссылку

            WebElement tableZones = driver.findElement(By.id("table-zones")); //по id находим элемент таблицу зон
            List<WebElement> zonesElementList = tableZones.findElements(By.tagName("tr")); //создаем список из строк таблицы зон
            List<String> zoneList = new ArrayList<>(); //инициалтзтруем список зон

            for(int j = 1; j < zonesElementList.size() - 1; j++){ //циклом проходим по списку зон
                WebElement zoneRow = zonesElementList.get(j); //вытаскиваем строку таблицы, как WebElement
                List<WebElement> zoneCellsList = zoneRow.findElements(By.tagName("td")); //создаем и заполняем список ячейками в которых содержаться различные данные
                WebElement select = zoneCellsList.get(2).findElement(By.tagName("select")); //получаем наименование территории
                Select selectZone = new Select(select);

                WebElement selectedZone = selectZone.getFirstSelectedOption();
                String selectedZoneName = selectedZone.getAttribute("textContent");
                geoZoneList.add(selectedZoneName); //заполняем список имен стран их именами вытаскивая из 4 элемента
                System.out.println(selectedZoneName);
            }

            for(String zone : geoZoneList){ //проверяем сортировку в списке зон
                compareTo(zone);
            }
            driver.findElement(By.cssSelector("span.button-set button[name='cancel']")).click();
        }



    }

    @Test
    public void userRegistrationTest() {
        driver.get("http://localhost/litecart/en/"); //идем на страницу магазина

        driver.findElement(By.cssSelector("form table tr:last-child a")).click(); //вызов формы регистрации пользователя
        Map<String, String> formNameMap = new HashMap<>(); //инициализируем словарь

        String country = "United States"; //выбиравем Country

        String name = generateName(); //генерим имя (предполагаем, что домеен у всех будет один и генерим только первую чвсть email)
        String email = name + "@hornsandhooves.com"; //еа основе сгенерированного имени и домена получаем email

        formNameMap.put("tax_id", "11235");
        formNameMap.put("company", "Horns & hooves");
        formNameMap.put("firstname", "Ostap");
        formNameMap.put("lastname", "Bender");
        formNameMap.put("address1", "Malaya Arnautskaya");
        formNameMap.put("address2", "Brighton Beach");
        formNameMap.put("postcode", "11235");
        formNameMap.put("city", "Little Odessa");
        formNameMap.put("email", email);
        formNameMap.put("phone", "5551478");
        formNameMap.put("password", "123");
        formNameMap.put("confirmed_password", "123");

        Select selectCountry = new Select(driver.findElement(By.tagName("select")));
        selectCountry.selectByVisibleText(country);

        for (Map.Entry<String, String> entry : formNameMap.entrySet()) {
            driver.findElement(By.name(entry.getKey())).sendKeys(entry.getValue());
        }

        ((JavascriptExecutor) driver).executeScript("arguments[0].style.opacity=1;",
                driver.findElement(By.cssSelector("table tr:nth-child(5) td:nth-child(2)")));

        ((JavascriptExecutor) driver).executeScript("arguments[0].removeAttribute('disabled');",
                driver.findElement(By.cssSelector("select[name=zone_code]")));

        ((JavascriptExecutor) driver).executeScript("arguments[0].setAttribute('type', 'text');",
                driver.findElement(By.cssSelector("input[name=zone_code]")));

//        Select selectZoneCode = new Select(driver.findElement(By.cssSelector("select[name='zone_code']")));
//        selectZoneCode.selectByIndex(1);

        driver.findElement(By.cssSelector("input[name=zone_code]")).sendKeys("11235");

        driver.findElement(By.name("create_account")).click();

        driver.findElement(By.cssSelector("#box-account li:nth-child(4) a")).click();

        driver.get("http://localhost/litecart/en/"); //идем на страницу магазина
        driver.findElement(By.cssSelector("input[name=email]")).sendKeys(formNameMap.get("email"));
        driver.findElement(By.cssSelector("input[name=password]")).sendKeys(formNameMap.get("password"));
        driver.findElement(By.cssSelector("button[name=login]")).click();
        driver.findElement(By.cssSelector("#box-account li:nth-child(4) a")).click();

    }

    @Test
    public void addNewProductTest(){
        driver.get("http://localhost/litecart/admin/"); //идем на страницу магазина
        driver.findElement(By.name("username")).sendKeys("admin"); //логинимся
        driver.findElement(By.name("password")).sendKeys("admin"); //   в форме
        driver.findElement(By.name("login")).click();                          //       авторизации

        driver.findElement(By.cssSelector("li#app-:nth-child(2)")).click();    //выбираем в боковой панели Catalog
        driver.findElement(By.xpath("//a[contains(text(), 'Product')]")).click(); //нажимаем кнопку Add New Product

        List<WebElement> tabs = driver.findElements(By.cssSelector(".tabs li a")); // список вкладок

        //вкладка General
        driver.findElement(By.cssSelector("label input[value='1']")).click(); //выбираем Status Enabled
        driver.findElement(By.name("name[en]")).sendKeys("Bast Shoe"); //заполняем поле Name
        driver.findElement(By.name("code")).sendKeys("123456"); //заполняем поле Code
        driver.findElement(By.cssSelector("input[value='1-3']")).click(); //Выбираем Product Groups, Gender
        driver.findElement(By.name("quantity")).clear(); // очищаем поле Quantity
        driver.findElement(By.name("quantity")).sendKeys("13"); //заполняем поле Quantity
        Select selectQuantityUnit = new Select(driver.findElement(By.name("quantity_unit_id"))); //выбор Quantity Unit
        selectQuantityUnit.selectByValue("1");
        Select selectDeliveryStatus = new Select(driver.findElement(By.name("delivery_status_id"))); //выбор Delivery Status
        selectDeliveryStatus.selectByValue("1");
        Select selectSoldOutStatus = new Select(driver.findElement(By.name("sold_out_status_id"))); //выбор Sold Out Status
        selectSoldOutStatus.selectByValue("1");
        String imgFilePath = new File("src/resources/bast_shoe.jpg").getAbsolutePath();
        driver.findElement(By.name("new_images[]")).sendKeys(imgFilePath); //добавление картинки
        //вкладка Information
        tabs.get(1).click(); //выбираем вторую по счету вкладку Information
        new WebDriverWait(driver, 60).
                until(ExpectedConditions.presenceOfElementLocated(By.name("manufacturer_id")));
        Select selectManufacturer = new Select(driver.findElement(By.name("manufacturer_id"))); //выбор Manufacturer
        selectManufacturer.selectByVisibleText("ACME Corp."); //заполнение поля Manufacturer
        driver.findElement(By.name("keywords")).sendKeys("keywords"); //заполнение поля Keywords
        driver.findElement(By.name("short_description[en]")).
                sendKeys("Traditional footwear of the forest areas of Northern Europe"); //заполнение поля Short Description
        String textFilePath = new File("src/resources/description.txt").getAbsolutePath(); //путь к файлу с описанием
        //заполнение поле Description
        try{
            BufferedReader readerText = new BufferedReader(new InputStreamReader(new
                    FileInputStream(textFilePath)));
            String lineText;
            while((lineText = readerText.readLine()) != null){
                driver.findElement(By.cssSelector("div.trumbowyg-editor")).sendKeys(lineText);
            }
            readerText.close();
        }catch(FileNotFoundException ex){
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        driver.findElement(By.name("head_title[en]")).sendKeys("Bast Shoe");

        //вкладка Prices
        tabs.get(3).click(); //выбираем четвертую по счету вкладку Prices
        new WebDriverWait(driver, 60).
                until(ExpectedConditions.presenceOfElementLocated(By.name("purchase_price")));
        driver.findElement(By.name("purchase_price")).clear();
        driver.findElement(By.name("purchase_price")).sendKeys("99.90");
        Select selectPurchasePrice = new Select(driver.findElement(By.name("purchase_price_currency_code"))); //Purchase Price
        selectPurchasePrice.selectByVisibleText("Euros");
        driver.findElement(By.name("prices[USD]")).sendKeys("29.000");

        //сохранение
        driver.findElement(By.name("save")).click(); //нажатие на кнопку Save

        //проверка наличия
        new WebDriverWait(driver, 60).
                until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("h1")));
        assertTrue(isPresent(By.xpath("//a[.='Bast Shoe']")));
    }

    @Test
    public void buyingNewProductTest() {

        int nmbofProduct = 3; // количество товара

        for(int i = 1; i <= nmbofProduct; i++) {
            driver.get("http://localhost/litecart/en/"); //идем на страницу магазина
            driver.findElements(By.cssSelector("a.link .image")).get(0).click(); //выбираем первый товар из списка

            //проверка на наличие опции выбора размера
            if (driver.findElements(By.name("options[Size]")).size() > 0) {
                Select selectSize = new Select(driver.findElement(By.name("options[Size]")));
                selectSize.selectByValue("Small");
            }

            driver.findElement(By.name("add_cart_product")).click(); //нажимаем на добавить товар

            //ожидаем обновления карзины путем увеличения количества товаров в ней
            new WebDriverWait(driver, 30).until(ExpectedConditions.
                    textToBePresentInElementLocated(By.cssSelector("span.quantity"),
                            Integer.toString(i))); //quantityProducts + 1)));

        }
        //заходим в корзину
        driver.findElement(By.xpath("//a[contains(text(), 'Checkout')]")).click(); //нажатие на Checkout


        //находим элмент отвечающий в таблицке за количество товара в корзине
        List<WebElement> tableQuantityList =
                driver.findElements(By.cssSelector("table.dataTable tr"));

        for (int i = 1; i <= (tableQuantityList.size() - 5); i++) {
            tableQuantityList.get(i); //элемент таблицы

            //ожидаем кнопку удаления
            new WebDriverWait(driver, 5).
                    until(ExpectedConditions.presenceOfElementLocated(By.name("remove_cart_item")));

            driver.findElement(By.name("remove_cart_item")).click(); //нажимаем на удалить товар

            // ожидаем удаления элемента отвечающего за первый элемент списка
            new WebDriverWait(driver, 10).until(ExpectedConditions.
                    stalenessOf(tableQuantityList.get(i)));
        }

        new WebDriverWait(driver, 10).
                until(ExpectedConditions.presenceOfElementLocated(By.xpath("//a[contains(text(), 'Back')]")));
        driver.findElement(By.xpath("//a[contains(text(), 'Back')]")).click();
    }

    @Test
    public void openNewWindowTest(){
        driver.get("http://localhost/litecart/admin/"); //идем на страницу магазина
        driver.findElement(By.name("username")).sendKeys("admin"); //логинимся
        driver.findElement(By.name("password")).sendKeys("admin"); //   в форме
        driver.findElement(By.name("login")).click();                          //       авторизации
        driver.findElement(By.cssSelector("li#app-:nth-child(3)")).click();    //выбираем в боковой панели Countries
        String country = "Russian Federation"; //задаем страну
        driver.findElement(By.xpath("//a[contains(text(), '" + country +"')]")).click(); //заходим внутрь
        String mainWindow = driver.getWindowHandle(); //получаем заголовок текущего окна

        List<WebElement> exLinkList = driver.findElements(By.cssSelector(".fa-external-link")); //создаем лист внешних ссылок

        for(WebElement linkElement : exLinkList) { //циклом проходим по всем ссылкам
            linkElement.click(); //открывает новое окно
            Set<String> openWindows = driver.getWindowHandles(); //получаем список открыиых окон
            new WebDriverWait(driver, 30).until(numberOfWindowsToBe(openWindows.size())); //ожидаем открытия окон
            for (String window : openWindows) { //циклом проходим по вем открытым окнам
                if (!window.equals(mainWindow)) {
                    driver.switchTo().window(window); //переключаемся во вновь открытое окно
                    driver.getWindowHandle(); //проверяем какое окно текущее
                    driver.close();
                    openWindows = driver.getWindowHandles(); //получаем список открыиых окон
                    new WebDriverWait(driver, 30).until(numberOfWindowsToBe(openWindows.size()));
                    driver.switchTo().window(mainWindow);
                }
            }
        }
    }

    public static String generateName() {
        Random random = new Random();
        String chars = "abcdefghijklmnopqrstuvwxyz"; //испоьзуем только строчные буквы английскго алфавита
        int length = 8;
        char[] text = new char[length];
        for (int i = 0; i < length; i++)
        {
            text[i] = chars.charAt(random.nextInt(chars.length()));
        }
        return new String(text);
    }

    public boolean compareColor(String color1, String color2) {

        String[] colorRange = color1.
                replace("rgba", "").replace(" ", "").split(",");
        String[] colorRange1 = color2.
                replace("rgba", "").replace(" ", "").split(",");

        for (int i = 0; i < colorRange.length; i++)
            if (colorRange[i] == colorRange1[i]) {
                return true;
            }
        return false;
    }

    public boolean compareString(String str1, String str2){
        if(str1.equals(str2)){
            return true;
        }
        return false;
    }

    public boolean compareMap(Map<String, String> m, Map<String, String> n){
        if(m.size() != n.size())
            return false;
        for(String key: m.keySet())
            if(!m.get(key).equals((n.get(key))))
                return false;
        return true;
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
