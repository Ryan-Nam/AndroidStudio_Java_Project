package com.example.a1_project.Events;

public class Event {
    private int id;
    private String event;
    private String dateTime;
    private String location;

    public Event() {
        super();
    }

    public Event(int id, String event, String dateTime, String location) {
        super();
        this.id = id;
        this.event = event;
        this.dateTime = dateTime;
        this.location = location;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}


