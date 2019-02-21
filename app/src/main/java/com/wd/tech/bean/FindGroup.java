package com.wd.tech.bean;

/**
 * date:2019/2/20 17:23
 * author:陈国星(陈国星)
 * function:
 */
public class FindGroup {

    /**
     * currentCount : 2
     * description : 天下第一
     * groupId : 10000
     * groupImage : D:/image/2018-09-19/20180919083221.jpg
     * groupName : 天下第一
     * hxGroupId : 1
     * maxCount : 10
     * ownerUid : 1010
     */

    private int currentCount;
    private String description;
    private int groupId;
    private String groupImage;
    private String groupName;
    private String hxGroupId;
    private int maxCount;
    private int ownerUid;

    public int getCurrentCount() {
        return currentCount;
    }

    public void setCurrentCount(int currentCount) {
        this.currentCount = currentCount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public int getMaxCount() {
        return maxCount;
    }

    public void setMaxCount(int maxCount) {
        this.maxCount = maxCount;
    }

    public int getOwnerUid() {
        return ownerUid;
    }

    public void setOwnerUid(int ownerUid) {
        this.ownerUid = ownerUid;
    }
}
