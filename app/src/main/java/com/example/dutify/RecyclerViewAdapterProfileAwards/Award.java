package com.example.dutify.RecyclerViewAdapterProfileAwards;

public class Award {
    private String name; // the title of the award
    private String description; //The description of the award
    private int price;
    private String pictureUrl;


    public Award(String name, String description, int price, String url) {
        this.description = description;
        this.name = name;
        this.price = price;
        this.pictureUrl = url;
    }


    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public Integer getPrice() {
        return price;
    }

    public String getPictureUrl() {
        return pictureUrl;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public void setPictureUrl(String pictureUrl) {
        this.pictureUrl = pictureUrl;
    }


}
