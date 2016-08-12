/**
 * <p>Title: AppGroup</p>
 * <p>Description:</p>
 * @author bgude
 * @version 1.0
 */

package com.businesshaps.am.businessobjects;

import java.util.Date;

public class AppGroup {
    private int id = 0;
    private String groupAlphaId;
    private String groupTypeAlphaId;
    private String groupName;
    private String groupDesc;
    private String website;
    private String tags;
    private String layout;
    private boolean virtual;
    private boolean deleted;
    private Date dateCreated;
    private boolean active;
    private String parentGroupAlphaId;
    private boolean restricted;
    private boolean allowAnonAccess;
    private String icon;
    private String owner;
    private boolean restrictLayout;
    private boolean minimumLayout;
    private String domain;
    
    public boolean isDeleted() {
		return deleted;
	}

	public void setDeleted(boolean deleted) {
		this.deleted = deleted;
	}

	public String getLayout() {
        return layout;
    }

    public void setLayout(String layout) {
        this.layout = layout;
    }

    public boolean isVirtual() {
        return virtual;
    }

    public void setVirtual(boolean virtual) {
        this.virtual = virtual;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }


    public AppGroup() {

    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public Date getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
    }

    public String getGroupAlphaId() {
        return groupAlphaId;
    }

    public void setGroupAlphaId(String groupAlphaId) {
        this.groupAlphaId = groupAlphaId;
    }

    public String getGroupDesc() {
        return groupDesc;
    }

    public void setGroupDesc(String groupDesc) {
        this.groupDesc = groupDesc;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getGroupTypeAlphaId() {
        return groupTypeAlphaId;
    }

    public void setGroupTypeAlphaId(String groupTypeAlphaId) {
        this.groupTypeAlphaId = groupTypeAlphaId;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getParentGroupAlphaId() {
        return parentGroupAlphaId;
    }

    public void setParentGroupAlphaId(String parentGroupAlphaId) {
        this.parentGroupAlphaId = parentGroupAlphaId;
    }


    public boolean isRestricted() {
        return restricted;
    }

    public void setRestricted(boolean restricted) {
        this.restricted = restricted;
    }


    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }


    public boolean isAllowAnonAccess() {
        return allowAnonAccess;
    }

    public void setAllowAnonAccess(boolean allowAnonAccess) {
        this.allowAnonAccess = allowAnonAccess;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public boolean isRestrictLayout() {
        return restrictLayout;
    }

    public void setRestrictLayout(boolean restrictLayout) {
        this.restrictLayout = restrictLayout;
    }

    public boolean isMinimumLayout() {
        return minimumLayout;
    }

    public void setMinimumLayout(boolean minimumLayout) {
        this.minimumLayout = minimumLayout;
    }
}
