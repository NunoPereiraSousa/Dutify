package com.example.dutify.RecyleViewAdapterProjectsCard;

public class Project {
    private int id;
    private String title;
    private String category;
    private String pictureUrl1;
    private String pictureUrl2;
    private String pictureUrl3;
    private String teamName;
    private String color;
    private int totalTask;
    private int totalTasksTodo;
    private String daysLeft;

    public Project(String title, String category, String pictureUrl1, String pictureUrl2, String pictureUrl3, int id, String teamName, String color, int totalTask, int totalTasksTodo, String daysLeft) {
        this.category = category;
        this.title = title;
        this.pictureUrl1 = pictureUrl1;
        this.pictureUrl2 = pictureUrl2;
        this.pictureUrl3 = pictureUrl3;
        this.id = id;
        this.teamName = teamName;
        this.color = color;
        this.totalTask = totalTask;
        this.totalTasksTodo = totalTasksTodo;
        this.daysLeft = daysLeft;
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
        this.pictureUrl1 = pictureUrl1;
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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;

    }

    public String getColor() {
        return color;
    }

    public void setColor() {
        this.color = color;
    }

    public int getTotalTask() {
        return totalTask;
    }

    public void setTotalTask() {
        this.totalTask = this.totalTask;
    }


    public int getTotalTasksTodo() {
        return totalTasksTodo;
    }

    public void setTotalTasksTodo(int totalTasksTodo) {
        this.totalTasksTodo = totalTasksTodo;
    }

    public String getDaysLeft() {
        return daysLeft;
    }

    public void setDaysLeft(String daysLeft) {
        this.daysLeft = daysLeft;
    }
}
