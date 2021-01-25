package com.example.dutify.RecyclerViewAdapterDashDescMyTasks;

public class Task {
   private int taskId;
    private  String taskTitle;
    private  String tasksDescription;
    private  int id_progress_status;
    private  String enDate;
    private int taskPrice;


    public Task(int taskId,String taskTitle, String tasksDescription, int id_progress_status, String enDate, int taskPrice) {
        this.taskId = taskId;
        this.taskTitle = taskTitle;
        this.tasksDescription = tasksDescription;
        this.id_progress_status = id_progress_status;
        this.enDate= enDate;
        this.taskPrice = taskPrice;
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

    public String getEnDate() {
        return enDate;
    }

    public void setEnDate(String enDate) {
        this.enDate = enDate;
    }

    public int getTaskPrice() {
        return taskPrice;
    }

    public void setTaskPrice(int taskPrice) {
        this.taskPrice = taskPrice;
    }
}
