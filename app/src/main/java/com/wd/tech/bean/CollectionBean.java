package com.wd.tech.bean;

/**
 * @author Tech
 * @date 2019/2/24 19:50
 * QQ:45198565
 * 佛曰：永无BUG 盘他！
 */
public class CollectionBean {

    /**
     * createTime : 1538104445000
     * id : 3
     * infoId : 1
     * thumbnail : https://img.huxiucdn.com/article/cover/201808/28/103850448205.jpg?imageView2/1/w/710/h/400/|imageMogr2/strip/interlace/1/quality/85/format/jpg
     * title : 关于滴滴顺风车事件的几点思考
     */

    private long createTime;
    private int id;
    private int infoId;
    private String thumbnail;
    private String title;
    boolean ischeck=false;
    boolean ischeck2=false;

    public boolean isIscheck2() {
        return ischeck2;
    }

    public void setIscheck2(boolean ischeck2) {
        this.ischeck2 = ischeck2;
    }

    public boolean isIscheck() {
        return ischeck;
    }

    public void setIscheck(boolean ischeck) {
        this.ischeck = ischeck;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getInfoId() {
        return infoId;
    }

    public void setInfoId(int infoId) {
        this.infoId = infoId;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
