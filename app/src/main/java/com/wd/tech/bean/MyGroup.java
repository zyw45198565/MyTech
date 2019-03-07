package com.wd.tech.bean;

/**
 * Description:<br>
 * Author:GJ<br>
 * Date:2019/3/6 15:25
 */
public class MyGroup {
    private int groupId;
    private String groupName;
    private String groupImage;
    private String hxGroupId;
    private int blackFlag;
    private int role;

    public int getGroupId() {
        return groupId;
    }

    public void setGroupId(int groupId) {
        this.groupId = groupId;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getGroupImage() {
        return groupImage;
    }

    public void setGroupImage(String groupImage) {
        this.groupImage = groupImage;
    }

    public String getHxGroupId() {
        return hxGroupId;
    }

    public void setHxGroupId(String hxGroupId) {
        this.hxGroupId = hxGroupId;
    }

    public int getBlackFlag() {
        return blackFlag;
    }

    public void setBlackFlag(int blackFlag) {
        this.blackFlag = blackFlag;
    }

    public int getRole() {
        return role;
    }

    public void setRole(int role) {
        this.role = role;
    }
}
