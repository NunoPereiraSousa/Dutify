package com.example.dutify.RecyclerViewAdapterDashListTaskCard;

public class Task {

    private int taskId;
    private String taskTitle;
    private String tasksDescription;
    private int id_progress_status;
    private int taskPrice;

    private int projectRelatedId;
    private String projectTitle;
    private int projectProgressStatus;
    private boolean isPersonalTask;

    public Task(int taskId, String taskTitle, String tasksDescription, int id_progress_status, int taskPrice, int projectRelatedId, String projectTitle, int projectProgressStatus, boolean isPersonalTask) {
        this.taskId = taskId;
        this.taskTitle = taskTitle;
        this.tasksDescription = tasksDescription;
        this.id_progress_status = id_progress_status;
        this.taskPrice = taskPrice;

        this.projectRelatedId = projectRelatedId;
        this.projectTitle = projectTitle;
        this.projectProgressStatus = projectProgressStatus;
        this.isPersonalTask = isPersonalTask;
    }

    public String getTaskTitle() {
        return taskTitle;
    }

    public void setTaskTitle(String taskTitle) {
        this.taskTitle = taskTitle;
    }

    public int getTaskId() {
        return taskId;
    }

    public void setTaskId(int taskId) {
        this.taskId = taskId;
    }

    public String getTasksDescription() {
        return tasksDescription;
    }

    public void setTasksDescription(String tasksDescription) {
        this.tasksDescription = tasksDescription;
    }

    public int getId_progress_status() {
        return id_progress_status;
    }

    public void setId_progress_status(int id_progress_status) {
        this.id_progress_status = id_progress_status;
    }


    public int getTaskPrice() {
        return taskPrice;
    }

    public void setTaskPrice(int taskPrice) {
        this.taskPrice = taskPrice;
    }

    public int getProjectRelatedId() {
        return projectRelatedId;
    }

    public void setProjectRelatedId(int projectRelatedId) {
        this.projectRelatedId = projectRelatedId;
    }

    public String getProjectTitle() {
        return projectTitle;
    }

    public void setProjectTitle(String projectTitle) {
        this.projectTitle = projectTitle;
    }

    public int getProjectProgressStatus() {
        return projectProgressStatus;
    }

    public void setProjectProgressStatus(int projectProgressStatus) {
        this.projectProgressStatus = projectProgressStatus;
    }

    public boolean getIsPersonalTask() {
        return isPersonalTask;
    }

    public void setPersonalTask(boolean personalTask) {
        isPersonalTask = personalTask;
    }
}
