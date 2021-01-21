package com.example.dutify.RecyclerViewAdapterAward;

public class CatalogAward {
    private String name;
    private int price;
    private String picture;

    public CatalogAward(String name, int price, String picture) {
        this.name = name;
        this.price = price;
        this.picture = picture;
    }

    public String getName() {
        return name;
    }

    public Integer getPrice() {
        return price;
    }

    public String getPicture() {
        return picture;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }
}
