package com.businesshaps.params;

public class Email {
	private String server;
	private boolean auth;
	private String port;
	private String username;
	private String password;
	public String getServer() {
		return server;
	}
	public void setServer(String server) {
		this.server = server;
	}
	public boolean isAuth() {
		return auth;
	}
	public void setAuth(boolean auth) {
		this.auth = auth;
	}
	public String getPort() {
		return port;
	}
	public void setPort(String port) {
		this.port = port;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	} 
}