package clssapp.todoapp;

import java.util.Date;

/**
 * Created by Carlos on 15/11/2017.
 */

public class TaskItem {
    private int id;
    private String task;
    private String date;
    private String time;
    private String description;

    public TaskItem(){

    }

    public TaskItem(int id, String task, String date, String time, String description){
        this.id=id;
        this.task=task;
        this.date=date;
        this.time=time;
        this.description=description;

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTask() {
        return task;
    }

    public void setTask(String task) {
        this.task = task;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
