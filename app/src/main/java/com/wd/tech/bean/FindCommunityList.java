package com.wd.tech.bean;

import java.util.List;

/**
 * Description:<br>
 * Author:GJ<br>
 * Date:2019/2/19 11:28
 */
public class FindCommunityList {

    /**
     * comment : 0
     * content : 首发
     * file : http://172.17.8.100/images/tech/head_pic/2018-09-20/20180920081958.jpg
     * id : 18
     * nickName : 小白
     * power : 2
     * praise : 0
     * publishTime : 1538040675000
     * userId : 1012
     * whetherFollow : 2
     * whetherGreat : 2
     * whetherVip : 2
     * headPic : D:/image/2018-09-19/20180919083221.jpg
     * signature : 秋天不回来
     * {"content":"好","nickName":"旺旺","userId":59}
     */

    private int comment;
    private String content;
    private String file;
    private int id;
    private String nickName;
    private int power;
    private int praise;
    private long publishTime;
    private int userId;
    private int whetherFollow;
    private int whetherGreat;
    private int whetherVip;
    private String headPic;
    private String signature;
    private List<CommunityItem> communityCommentVoList;

    public int getComment() {
        return comment;
    }

    public void setComment(int comment) {
        this.comment = comment;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public int getPower() {
        return power;
    }

    public void setPower(int power) {
        this.power = power;
    }

    public int getPraise() {
        return praise;
    }

    public void setPraise(int praise) {
        this.praise = praise;
    }

    public long getPublishTime() {
        return publishTime;
    }

    public void setPublishTime(long publishTime) {
        this.publishTime = publishTime;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getWhetherFollow() {
        return whetherFollow;
    }

    public void setWhetherFollow(int whetherFollow) {
        this.whetherFollow = whetherFollow;
    }

    public int getWhetherGreat() {
        return whetherGreat;
    }

    public void setWhetherGreat(int whetherGreat) {
        this.whetherGreat = whetherGreat;
    }

    public int getWhetherVip() {
        return whetherVip;
    }

    public void setWhetherVip(int whetherVip) {
        this.whetherVip = whetherVip;
    }

    public String getHeadPic() {
        return headPic;
    }

    public void setHeadPic(String headPic) {
        this.headPic = headPic;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public List<CommunityItem> getCommunityCommentVoList() {
        return communityCommentVoList;
    }

    public void setCommunityCommentVoList(List<CommunityItem> communityCommentVoList) {
        this.communityCommentVoList = communityCommentVoList;
    }
}
