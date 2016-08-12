package com.businesshaps.schedule;
 
public class UserWorkSchedule {

    private int id;
    private String username;
    private int workScheduleId;
    public UserWorkSchedule() {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getWorkScheduleId() {
        return workScheduleId;
    }

    public void setWorkScheduleId(int workScheduleId) {
        this.workScheduleId = workScheduleId;
    }
}
