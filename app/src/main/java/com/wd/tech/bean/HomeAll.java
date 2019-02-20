package com.wd.tech.bean;

public class HomeAll {


    /**
     * collection : 7
     * id : 54
     * releaseTime : 1539586683000
     * share : 1
     * source : 中国企业家杂志©
     * summary : 谁说滴滴之后再无网约车？新的搅局者又来了。
     * thumbnail : https://img.huxiucdn.com/article/cover/201810/13/190901169923.jpg?imageView2/1/w/710/h/400/|imageMogr2/strip/interlace/1/quality/85/format/jpg
     * title : 有摩拜的前车之鉴，为何哈啰仍要入局网约车？
     * whetherAdvertising : 2
     * whetherCollection : 2
     * whetherPay : 2
     */

    private int collection;
    private int id;
    private long releaseTime;
    private int share;
    private String source;
    private String summary;
    private String thumbnail;
    private String title;
    private int whetherAdvertising;
    private int whetherCollection;
    private int whetherPay;
    private ResultBean infoAdvertisingVo;

    public HomeAll(ResultBean infoAdvertisingVo) {
        this.infoAdvertisingVo = infoAdvertisingVo;
    }

    public ResultBean getInfoAdvertisingVo() {
        return infoAdvertisingVo;
    }

    public void setInfoAdvertisingVo(ResultBean infoAdvertisingVo) {
        this.infoAdvertisingVo = infoAdvertisingVo;
    }

    public int getCollection() {
        return collection;
    }

    public void setCollection(int collection) {
        this.collection = collection;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public long getReleaseTime() {
        return releaseTime;
    }

    public void setReleaseTime(long releaseTime) {
        this.releaseTime = releaseTime;
    }

    public int getShare() {
        return share;
    }

    public void setShare(int share) {
        this.share = share;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
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

    public int getWhetherAdvertising() {
        return whetherAdvertising;
    }

    public void setWhetherAdvertising(int whetherAdvertising) {
        this.whetherAdvertising = whetherAdvertising;
    }

    public int getWhetherCollection() {
        return whetherCollection;
    }

    public void setWhetherCollection(int whetherCollection) {
        this.whetherCollection = whetherCollection;
    }

    public int getWhetherPay() {
        return whetherPay;
    }

    public void setWhetherPay(int whetherPay) {
        this.whetherPay = whetherPay;
    }

    public static class ResultBean {
        /**
         * title : 国庆遇见十月一
         * id : 4
         * content : 出游吧
         * url : url
         */

        String pic;
        private String title;
        private int id;
        private String content;
        private String url;

        public String getPic() {
            return pic;
        }

        public void setPic(String pic) {
            this.pic = pic;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }
    }

}
