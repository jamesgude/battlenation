/**
 * <p>Title: AppOrganization</p>
 * <p>Description:</p>
 * @author Bradley Gude
 * @version 1.0
 */

package com.businesshaps.am.businessobjects;

public class AppOrganization {
    private int id;
    private String name;
    private String organizationKey;
    
	public String getOrganizationKey() {
		return organizationKey;
	}
	public void setOrganizationKey(String organizationKey) {
		this.organizationKey = organizationKey;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
    
}
