package com.businesshaps.params;

import com.businesshaps.am.businessobjects.AppGroup;


public class SystemProperties {
	private Mapping[] mappings;
	private Resource[] resources;
	private Sync[] syncs;
	private AppGroup[] groups;
	
	private String bypassExtensions;
	private String resourceDirectory;
	private String layoutExtensions;
	private String bypassDirectories;
	private String imageStorageHome;
	private Email email;
	private String jdbc;
	private boolean serverMode;
	private String domain;
	private String initializeScript;
	private boolean httpsOnly;
	private String httpsServer;
	
	public boolean isHttpsOnly() {
		return httpsOnly;
	}

	public void setHttpsOnly(boolean httpsOnly) {
		this.httpsOnly = httpsOnly;
	}

	public String getHttpsServer() {
		return httpsServer;
	}

	public void setHttpsServer(String httpsServer) {
		this.httpsServer = httpsServer;
	}

	private API[] apis;

	public API[] getApis() {
		return apis;
	}

	public void setApis(API[] apis) {
		this.apis = apis;
	}
	


	public AppGroup[] getGroups() {
		return groups;
	}

	public void setAppGroups(AppGroup[] groups) {
		this.groups = groups;
	}

	public String getInitializeScript() {
		return initializeScript;
	}

	public void setInitializeScript(String initializeScript) {
		this.initializeScript = initializeScript;
	}

	public boolean isServerMode() {
		return serverMode;
	}

	public void setServerMode(boolean serverMode) {
		this.serverMode = serverMode;
	}

	public String getDomain() {
		return domain;
	}

	public void setDomain(String domain) {
		this.domain = domain;
	}

	public String getJdbc() {
		return jdbc;
	}

	public void setJdbc(String jdbc) {
		this.jdbc = jdbc;
	}

	public Email getEmail() {
		return email;
	}

	public void setEmail(Email email) {
		this.email = email;
	}

	public String getImageStorageHome() {
		return imageStorageHome;
	}

	public void setImageStorageHome(String imageStorageHome) {
		this.imageStorageHome = imageStorageHome;
	}

	public Sync[] getSyncs() { 
		return syncs;
	}

	public void setSyncs(Sync[] syncs) {
		this.syncs = syncs;
	}


	public String getBypassDirectories() {
		return bypassDirectories;
	}

	public void setBypassDirectories(String bypassDirectories) {
		this.bypassDirectories = bypassDirectories;
	}

	public String getLayoutExtensions() {
		return layoutExtensions;
	}

	public void setLayoutExtensions(String layoutExtensions) {
		this.layoutExtensions = layoutExtensions;
	}

	public String getResourceDirectory() {
		return resourceDirectory;
	} 

	public void setResourceDirectory(String resourceDirectory) {
		this.resourceDirectory = resourceDirectory;
	}

	public String getBypassExtensions() {
		return bypassExtensions;
	}

	public void setBypassExtensions(String bypassExtensions) {
		this.bypassExtensions = bypassExtensions;
	}

	public Resource[] getResources() {
		return resources;
	}

	public void setResources(Resource[] resources) {
		this.resources = resources;
	}

	public Mapping[] getMappings() {
		return mappings;
	}

	public void setMappings(Mapping[] mappings) {
		this.mappings = mappings;
	}
	
}
  
