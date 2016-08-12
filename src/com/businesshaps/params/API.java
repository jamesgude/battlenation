package com.businesshaps.params;



public class API {
	private String name;
	private String parent;
	private String displayName;
	private int defaultItemsPerPage;
	private API[] children;
	
	public int getDefaultItemsPerPage() {
		return defaultItemsPerPage;
	}
	public void setDefaultItemsPerPage(int defaultItemsPerPage) {
		this.defaultItemsPerPage = defaultItemsPerPage;
	}
	public API[] getChildren() {
		return children;
	}
	public void setChildren(API[] children) {
		this.children = children;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getParent() {
		return parent;
	}
	public void setParent(String parent) {
		this.parent = parent;
	}
	public String getDisplayName() {
		return displayName;
	}
	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}
	 
}