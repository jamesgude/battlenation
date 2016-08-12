package com.businesshaps.am.businessobjects;

public class AppUserOrganization {
	private int id;
	private String email;
	private int userId;
	private String organizationKey;
	private int workScheduleId;
	private int locationId;
	private String jobTitle;
	private String jobDescription;
	private String payRate;
	private String displayName;
	
	
	public String getDisplayName() {
		return displayName;
	}
	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}
	public String getJobTitle() {
		return jobTitle;
	}
	public void setJobTitle(String jobTitle) {
		this.jobTitle = jobTitle;
	}
	public String getJobDescription() {
		return jobDescription;
	}
	public void setJobDescription(String jobDescription) {
		this.jobDescription = jobDescription;
	}
	public String getPayRate() {
		return payRate;
	}
	public void setPayRate(String payRate) {
		this.payRate = payRate;
	}
	public int getLocationId() {
		return locationId;
	}
	public void setLocationId(int locationId) {
		this.locationId = locationId;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	public String getOrganizationKey() {
		return organizationKey;
	}
	public void setOrganizationKey(String organizationKey) {
		this.organizationKey = organizationKey;
	}
	public int getWorkScheduleId() {
		return workScheduleId;
	}
	public void setWorkScheduleId(int workScheduleId) {
		this.workScheduleId = workScheduleId;
	}
	
	
}
