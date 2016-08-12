package com.businesshaps.oi;

public class DataObject {
	private int id;
	private int parentId = 0;
	private String name;
	private String displayName;
	private String uri;
	private int defaultItemsPerPage;
	
	public int getDefaultItemsPerPage() {
		return defaultItemsPerPage;
	}
	public void setDefaultItemsPerPage(int defaultItemsPerPage) {
		this.defaultItemsPerPage = defaultItemsPerPage;
	}
	public String getUri() {
		return uri;
	}
	public void setUri(String uri) {
		this.uri = uri;
	}
	public String getDisplayName() {
		return displayName;
	}
	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getParentId() {
		return parentId;
	}
	public void setParentId(int parentId) {
		this.parentId = parentId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public DataObject() {
		
	}
}
