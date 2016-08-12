package com.businesshaps.params;



public class Resource {
	private String name;
	private String filename;
	private boolean inRoot;
	
	public boolean isInRoot() {
		return inRoot;
	}
	public void setInRoot(boolean inRoot) {
		this.inRoot = inRoot;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getFilename() {
		return filename;
	}
	public void setFilename(String filename) {
		this.filename = filename;
	} 
	 
}