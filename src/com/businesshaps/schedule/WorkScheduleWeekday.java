package com.businesshaps.schedule;
 
public class WorkScheduleWeekday {

    private int id;
    private int workScheduleId;
    private int weekday;
    private String timeslots;
    private String color;

    public WorkScheduleWeekday() {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getWeekday() {
        return weekday;
    }

    public void setWeekday(int weekday) {
        this.weekday = weekday;
    }

    public String getTimeslots() {
        return timeslots;
    }

    public void setTimeslots(String timeslots) {
        this.timeslots = timeslots;
    }

    public int getWorkScheduleId() {
        return workScheduleId;
    }

    public void setWorkScheduleId(int workScheduleId) {
        this.workScheduleId = workScheduleId;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }
}
