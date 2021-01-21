package com.example.dutify.RecyleViewAdapterProjectsCard;

public class Project {
    private String title;
    private String category;
    private String pictureUrl1;
    private String pictureUrl2;
    private String pictureUrl3;

    public Project(String title, String category) {
        this.category = category;
        this.title = title;
        /*this.pictureUrl1 = pictureUrl1;
        this.pictureUrl2 = pictureUrl2;
        this.pictureUrl3 = pictureUrl3;  String pictureUrl1, String pictureUrl2, String pictureUrl3*/
    }

    public String getTitle() {
        return title;
    }

    public String getCategory() {
        return category;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getPictureUrl1() {
        return pictureUrl1;
    }

    public void setPictureUrl1(String pictureUrl1) {
        this.title = pictureUrl1;
    }

    public String getPictureUrl2() {
        return pictureUrl2;
    }

    public void setPictureUrl2(String pictureUrl2) {
        this.pictureUrl2 = pictureUrl2;
    }

    public String getPictureUrl3() {
        return pictureUrl3;
    }

    public void setPictureUrl3(String pictureUrl3) {
        this.pictureUrl3 = pictureUrl3;
    }

}
