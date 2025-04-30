package com.simplinote.simplinote.model;

import java.time.LocalDate;
import java.time.LocalTime;
import java.io.Serializable;

public class Events implements Serializable {
    private LocalDate date;
    private LocalTime time;
    private String title;
    private String description;
    private boolean isCompleted;

    public Events(LocalDate date, String title, String description) {
        this.date = date;
        this.title = title;
        this.description = description;
        this.time = LocalTime.now();
        this.isCompleted = false;
    }

    // Getters and setters
    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }

    public LocalTime getTime() { return time; }
    public void setTime(LocalTime time) { this.time = time; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public boolean isCompleted() { return isCompleted; }
    public void setCompleted(boolean completed) { isCompleted = completed; }
}