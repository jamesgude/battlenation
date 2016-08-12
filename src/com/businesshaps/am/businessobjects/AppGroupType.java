/**
 * <p>Title: AppGroupType</p>
 * <p>Description:</p>
 * @author bgude
 * @version 1.0
 */

package com.businesshaps.am.businessobjects;

public class AppGroupType {
    private int id = 0;
    private String groupTypeAlphaId;
    private String groupTypeName;
    private String groupTypeDesc;
    private boolean deleted;

    public boolean isDeleted() {
		return deleted;
	}

	public void setDeleted(boolean deleted) {
		this.deleted = deleted;
	}

	public AppGroupType() {

    }

    public String getGroupTypeAlphaId() {
        return groupTypeAlphaId;
    }

    public void setGroupTypeAlphaId(String groupTypeAlphaId) {
        this.groupTypeAlphaId = groupTypeAlphaId;
    }

    public String getGroupTypeDesc() {
        return groupTypeDesc;
    }

    public void setGroupTypeDesc(String groupTypeDesc) {
        this.groupTypeDesc = groupTypeDesc;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getGroupTypeName() {
        return groupTypeName;
    }

    public void setGroupTypeName(String groupTypeName) {
        this.groupTypeName = groupTypeName;
    }
}
