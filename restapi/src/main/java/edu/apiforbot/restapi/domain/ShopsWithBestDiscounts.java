package edu.apiforbot.restapi.domain;

public class ShopsWithBestDiscounts {

    private String shopName;
    private String maxDiscount;

    public ShopsWithBestDiscounts() {
    }

    public ShopsWithBestDiscounts(String shopName, String maxDiscount) {
        this.shopName = shopName;
        this.maxDiscount = maxDiscount;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public String getMaxDiscount() {
        return maxDiscount;
    }

    public void setMaxDiscount(String maxDiscount) {
        this.maxDiscount = maxDiscount;
    }

    @Override
    public String toString() {
        return "ShopsWithBestDiscounts{" +
                "shopName='" + shopName + '\'' +
                ", maxDiscount='" + maxDiscount + '\'' +
                '}';
    }
}
