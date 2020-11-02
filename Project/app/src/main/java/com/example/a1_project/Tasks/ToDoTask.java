package com.example.a1_project.Tasks;

public class ToDoTask {
    private int id;
    private String todo;
    private String location;
    // 1=yes 0=no
    private int completed;

    public ToDoTask()    {
        super();
    }

    public ToDoTask(int id, String todo, String location, int completed)    {
        super();
        this.id = id;
        this.todo = todo;
        this.location = location;
        this.completed = completed;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTodo() {
        return todo;
    }

    public void setTodo(String todo) {
        this.todo = todo;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public int getCompleted() {
        return completed;
    }

    public void setCompleted(int completed) {
        this.completed = completed;
    }
}