package com.businesshaps.params;

public class Mapping {
	private String path;
	private String defaultFile;
	private boolean api;
	private String[] combine;
	private boolean combineAsFile;
	private boolean javascriptCompress;
	private String contentType;
	
	public String getContentType() {
		return contentType;
	}
	public void setContentType(String contentType) {
		this.contentType = contentType;
	}
	public boolean isJavascriptCompress() {
		return javascriptCompress;
	}
	public void setJavascriptCompress(boolean javascriptCompress) {
		this.javascriptCompress = javascriptCompress;
	}
	public boolean isCombineAsFile() {
		return combineAsFile;
	}
	public void setCombineAsFile(boolean combineAsFile) {
		this.combineAsFile = combineAsFile;
	}
	public String[] getCombine() {
		return combine;
	}
	public void setCombine(String[] combine) {
		this.combine = combine;
	}
	public boolean isApi() {
		return api;
	}
	public void setApi(boolean api) {
		this.api = api;
	}
	public String getDefaultFile() {
		return defaultFile;
	}
	public void setDefaultFile(String defaultFile) {
		this.defaultFile = defaultFile;
	}
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
} 