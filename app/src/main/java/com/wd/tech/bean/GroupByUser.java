package com.wd.tech.bean;

public class GroupByUser {

    /**
     * groupId : 10000
     * groupImage : D:/image/2018-09-19/20180919083221.jpg
     * groupName : 天下第一
     * hxGroupId : 1
     */

    private int groupId;
    private String groupImage;
    private String groupName;
    private String hxGroupId;
    private String role;

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public int getGroupId() {
        return groupId;
    }

    public void setGroupId(int groupId) {
        this.groupId = groupId;
    }

    public String getGroupImage() {
        return groupImage;
    }

    public void setGroupImage(String groupImage) {
        this.groupImage = groupImage;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getHxGroupId() {
        return hxGroupId;
    }

    public void setHxGroupId(String hxGroupId) {
        this.hxGroupId = hxGroupId;
    }
}
