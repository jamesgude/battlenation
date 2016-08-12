package com.businesshaps.schedule;

import java.util.Date;

public class WorkScheduleEntry {
	private int id;
	private String type;
	private int start;
	private int timeslots;
	private String rowKey;
	private String overlayType;
	private Date date;
	private int workScheduleId;
	private String email;
	private String dateKey;
	private String organizationKey;
	
	public String getOrganizationKey() {
		return organizationKey;
	}
	public void setOrganizationKey(String organizationKey) {
		this.organizationKey = organizationKey;
	}
	public String getDateKey() {
		return dateKey;
	}
	public void setDateKey(String dateKey) {
		this.dateKey = dateKey;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public int getWorkScheduleId() {
		return workScheduleId;
	}
	public void setWorkScheduleId(int workScheduleId) {
		this.workScheduleId = workScheduleId;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public int getStart() {
		return start;
	}
	public void setStart(int start) {
		this.start = start;
	}
	public int getTimeslots() {
		return timeslots;
	}
	public void setTimeslots(int timeslots) {
		this.timeslots = timeslots;
	}
	public String getRowKey() {
		return rowKey;
	}
	public void setRowKey(String rowKey) {
		this.rowKey = rowKey;
	}
	public String getOverlayType() {
		return overlayType;
	}
	public void setOverlayType(String overlayType) {
		this.overlayType = overlayType;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	
	
}
