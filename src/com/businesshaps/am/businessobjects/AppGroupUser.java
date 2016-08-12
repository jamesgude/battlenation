/**
 * <p>Title: AppGroupUser</p>
 * <p>Description:</p>
 * @author bgude
 * @version 1.0
 */

package com.businesshaps.am.businessobjects;

import java.util.Date;

public class AppGroupUser {
    private int id = 0;
    private String groupAlphaId;
    private int userId;
    private boolean groupAccess = false;
    private int rank;
    private Date dateCreated;
    private String email;
    private boolean disabled;
    private Date lastInvited;
    private Date lastAccessed;
    private boolean deleted;

    public boolean isDeleted() {
		return deleted;
	}

	public void setDeleted(boolean deleted) {
		this.deleted = deleted;
	}

	public AppGroupUser() {

    }

    public Date getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
    }

    public void setDateCreated(String dateCreated) {
        this.dateCreated = new Date();
    }

    public String getGroupAlphaId() {
        return groupAlphaId;
    }

    public void setGroupAlphaId(String groupAlphaId) {
        this.groupAlphaId = groupAlphaId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;  
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public boolean isGroupAccess() {
        return groupAccess;
    }

    public void setGroupAccess(boolean groupAccess) {
        this.groupAccess = groupAccess;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isDisabled() {
        return disabled;
    }

    public void setDisabled(boolean disabled) {
        this.disabled = disabled;
    }

    public Date getLastInvited() {
        return lastInvited;
    }

    public void setLastInvited(Date lastInvited) {
        this.lastInvited = lastInvited;
    }

    public Date getLastAccessed() {
        return lastAccessed;
    }

    public void setLastAccessed(Date lastAccessed) {
        this.lastAccessed = lastAccessed;
    }

}
