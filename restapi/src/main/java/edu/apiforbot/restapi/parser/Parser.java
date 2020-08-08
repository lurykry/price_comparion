package edu.apiforbot.restapi.parser;


import edu.apiforbot.restapi.domain.Item;
import edu.apiforbot.restapi.domain.ShopsWithBestDiscounts;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import static org.openqa.selenium.support.ui.ExpectedConditions.presenceOfElementLocated;

@Component
public class Parser {

    private static final String SHOP_URL = "https://edadeal.ru/";
    private static final String DRIVER_NAME = "webdriver.chrome.driver";
    private static final String PATH_TO_DRIVER = "/src/main/resources/chromedriver.exe";

    private static final String RETAILERS_WITH_GREATEST_DISCOUNTS_SELECTOR =
            "#view > div.p-index > div.p-index__retailers-block > div > div.p-index__retailers";
    private static final String ITEMS_WITH_GREATEST_DISCOUNTS_SELECTOR =
            "#view > div.p-index > div:nth-child(3) > div > div";

    private static final String RETAILERS_WITH_GREATEST_DISCOUNTS_ID = "my_id_for_retailer_div";
    private static final String ITEMS_WITH_GREATEST_DISCOUNTS_ID = "my_id_for_item_div";

    private static final String USER_REQUEST_ITEM_SELECTOR = "#view > div.p-offers.p-offers_mode_default.p-offers_sticky_false" +
            ".p-offers_has-marketing-markers_false > div > div.b-swipe.p-offers__main-content.b-swipe_disabled_false" +
            ".b-swipe_swipe-can_null > div > div > div > section:nth-child(2)";
    private static final String USER_REQUEST_ITEM_ID = "my_id_for_target_item_div";


    public List<ShopsWithBestDiscounts> getShopsWithBestDiscounts(String cityName){
        WebDriver driver = setDriver();
        WebElement createdDiv = getRequiredHtml(driver, cityName, null ,RETAILERS_WITH_GREATEST_DISCOUNTS_SELECTOR, RETAILERS_WITH_GREATEST_DISCOUNTS_ID);
        Document bodyFragment = Jsoup.parse(createdDiv.getAttribute("textContent"));

        List<String> retailerNames = findItemAttrByClass("b-retailer__logo-title", bodyFragment);
        List<String> retailerDiscounts = findItemAttrByClass("b-retailer__discount", bodyFragment);

        List<ShopsWithBestDiscounts> shops = new ArrayList<>();
        for(int i = 0; i < retailerNames.size(); i++){
            shops.add(new ShopsWithBestDiscounts(retailerNames.get(i),
                    retailerDiscounts.get(i)));
        }

        driver.quit();
        return shops;
    }


    public List<Item> getBestDiscounts(String cityName, String itemName){

        WebDriver driver = setDriver();
        WebElement createdDiv;
        if(itemName != null){
            createdDiv = getRequiredHtml(driver,cityName, itemName, USER_REQUEST_ITEM_SELECTOR,USER_REQUEST_ITEM_ID);
        }else{
            createdDiv = getRequiredHtml(driver,cityName, null ,ITEMS_WITH_GREATEST_DISCOUNTS_SELECTOR,ITEMS_WITH_GREATEST_DISCOUNTS_ID);
        }

        Document bodyFragment = Jsoup.parse(createdDiv.getAttribute("textContent"));
        List<String> endDates = findItemAttrByClass("b-offer__dates", bodyFragment);
        List<String> newPrices = findItemAttrByClass("b-offer__price-new", bodyFragment);
        List<String> oldPrices = findItemAttrByClass("b-offer__price-old", bodyFragment);
        List<String> itemDesc = findItemAttrByClass("b-offer__description", bodyFragment);
        List<String> shopNames = findItemAttrByClass("b-offer__retailer-icon", bodyFragment);

        int[] arr = {endDates.size(), newPrices.size(),oldPrices.size(),itemDesc.size(),shopNames.size()};
        int smallest = Integer.MAX_VALUE;
        for (int i : arr) {
            if (i < smallest)
                smallest = i;
        }

        //TODO: check for exceptions
        List<Item> items = new ArrayList<>();
        for(int i = 0; i < smallest; i++){
            items.add(new Item(shopNames.get(i), itemDesc.get(i),
                    oldPrices.get(i), newPrices.get(i), endDates.get(i)));
        }

        driver.quit();
        return items;
    }



    private List<String> findItemAttrByClass(String className, Document bodyFragment){
        if(className.equals("b-offer__retailer-icon")){
            Elements itemAttrs = bodyFragment.getElementsByClass(className);
            Elements images = itemAttrs.select("img");
            List<String> itemsAttrString = new LinkedList<>();
            for (Element image : images) {
                String title = image.attr("title");
                itemsAttrString.add(title);
            }
            return itemsAttrString;
        }
        Elements itemAttrs = bodyFragment.getElementsByClass(className);
        List<String> itemsAttrString = new LinkedList<>();
        for (Element itemAttr : itemAttrs) {
            itemsAttrString.add(itemAttr.text());
        }
        return itemsAttrString;
    }


    private String buildInjection(String selector, String id, boolean isTooLong){
        String jsInjection;
        if(isTooLong){
            jsInjection = "var data = document.querySelector('?').innerHTML.substring(0,10000);" +
                    "var div = document.createElement('div');" +
                    "div.textContent = data;" +
                    "div.id = '?';" +
                    "document.body.appendChild(div);";
        }else{
            jsInjection = "var data = document.querySelector('?').innerHTML;" +
                    "var div = document.createElement('div');" +
                    "div.textContent = data;" +
                    "div.id = '?';" +
                    "document.body.appendChild(div);";
        }


        return jsInjection.replaceFirst("\\?", selector)
                                            .replaceFirst("\\?", id);
    }

    private WebElement getRequiredHtml(WebDriver driver, String city, String itemName, String selector, String id){
        if(itemName != null){
            driver.get(SHOP_URL + city + "/offers?q=" + itemName);
        }else
            driver.get(SHOP_URL + city);
        WebDriverWait wait = new WebDriverWait(driver, 10L);
        WebElement firstResult = wait.until(presenceOfElementLocated(By.cssSelector(selector)));
//        new WebDriverWait(driver, 20L).until(
//                webDriver -> ((JavascriptExecutor) webDriver).executeScript("return document.readyState").equals("complete"));

        if(itemName != null){
            ((JavascriptExecutor) driver)
                    .executeScript(buildInjection(selector,
                            id, true));
        }else {
            ((JavascriptExecutor) driver)
                    .executeScript(buildInjection(selector,
                            id, false));
        }


        return driver.findElement(By.id(id));
    }

    public WebDriver setDriver(){
        System.setProperty(DRIVER_NAME,System.getProperty("user.dir").replaceAll("\\\\","/") + PATH_TO_DRIVER);
        ChromeOptions options = new ChromeOptions();
        options.setHeadless(true);
        return new ChromeDriver(options);
    }


}
