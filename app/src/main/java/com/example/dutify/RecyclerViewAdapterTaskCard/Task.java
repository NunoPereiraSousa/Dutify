package com.example.dutify.RecyclerViewAdapterTaskCard;

public class Task {
    private String title;
    private String description;
    private String startDate;
    private String endDate;
    private int id_progress_status;

    public Task(String title, String description, String startDate, String endDate, int id_progress_status) {
        this.title = title;
        this.description = description;
        this.startDate = startDate;
        this.endDate = endDate;
        this.id_progress_status = id_progress_status;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String startDate) {
        this.endDate = endDate;
    }

    public int getProgressStatus() {
        return id_progress_status;
    }

    public void setProgressStatus(int id_progress_status) {
        this.id_progress_status = id_progress_status;
    }
}
