package edu.apiforbot.restapi.domain;

public class Item {

    private String shopName;
    private String itemName;
    private String itemOldPrice;
    private String itemNewPrice;
    private String discountEndDate;

    public Item() {
    }

    public Item(String shopName, String itemName, String itemOldPrice, String itemNewPrice, String discountEndDate) {
        this.shopName = shopName;
        this.itemName = itemName;
        this.itemOldPrice = itemOldPrice;
        this.itemNewPrice = itemNewPrice;
        this.discountEndDate = discountEndDate;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getItemOldPrice() {
        return itemOldPrice;
    }

    public void setItemOldPrice(String itemOldPrice) {
        this.itemOldPrice = itemOldPrice;
    }

    public String getItemNewPrice() {
        return itemNewPrice;
    }

    public void setItemNewPrice(String itemNewPrice) {
        this.itemNewPrice = itemNewPrice;
    }

    public String getDiscountEndDate() {
        return discountEndDate;
    }

    public void setDiscountEndDate(String discountEndDate) {
        this.discountEndDate = discountEndDate;
    }

    @Override
    public String toString() {
        return "Item{" +
                "shopName='" + shopName + '\'' +
                ", itemName='" + itemName + '\'' +
                ", itemOldPrice='" + itemOldPrice + '\'' +
                ", itemNewPrice='" + itemNewPrice + '\'' +
                ", discountEndDate='" + discountEndDate + '\'' +
                '}';
    }
}
