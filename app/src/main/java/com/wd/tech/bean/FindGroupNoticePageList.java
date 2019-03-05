package com.wd.tech.bean;

/**
 * Created by ${LinJiangtao}
 * on 2019/2/25
 */
public class FindGroupNoticePageList {
    int noticeId;
    int status;
    int type;
    String nickName;
    String groupName;
    String remark;
    String headPic;
    long noticeTime;

    public int getNoticeId() {
        return noticeId;
    }

    public void setNoticeId(int noticeId) {
        this.noticeId = noticeId;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getHeadPic() {
        return headPic;
    }

    public void setHeadPic(String headPic) {
        this.headPic = headPic;
    }

    public long getNoticeTime() {
        return noticeTime;
    }

    public void setNoticeTime(long noticeTime) {
        this.noticeTime = noticeTime;
    }
}
