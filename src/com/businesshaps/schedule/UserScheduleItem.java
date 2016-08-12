package com.businesshaps.schedule;
 
public class UserScheduleItem {
	private int id;
	private String username;
	private int startTimeslot;
	private int timeslots;
	private int weekday;
	private String type;
	private String title;
	private String description;
	private String overlayType;
	
	public String getOverlayType() {
		return overlayType;
	}
	public void setOverlayType(String overlayType) {
		this.overlayType = overlayType;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
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
	public int getStartTimeslot() {
		return startTimeslot;
	}
	public void setStartTimeslot(int startTimeslot) {
		this.startTimeslot = startTimeslot;
	}
	public int getTimeslots() {
		return timeslots;
	}
	public void setTimeslots(int timeslots) {
		this.timeslots = timeslots;
	}
	public int getWeekday() {
		return weekday;
	}
	public void setWeekday(int weekday) {
		this.weekday = weekday;
	}
	
}
