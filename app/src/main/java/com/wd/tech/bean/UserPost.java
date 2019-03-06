package com.wd.tech.bean;

import java.util.List;

/**
 * Description:<br>
 * Author:GJ<br>
 * Date:2019/2/28 9:48
 */
public class UserPost {
    /**
     * communityUserPostVoList : [{"comment":0,"content":"呀呀呀","file":"http://mobile.bwstudent.com/images/tech/community_pic/2019-03-01/8467120190301150127.jpg","id":43,"praise":1,"whetherGreat":2},{"comment":0,"content":"嗯哼","file":"http://mobile.bwstudent.com/images/tech/community_pic/2019-03-01/8207120190301145713.jpg","id":42,"praise":0,"whetherGreat":2},{"comment":3,"content":"嗯嗯","file":"http://mobile.bwstudent.com/images/tech/community_pic/2019-03-01/8973620190301111817.jpg,http://mobile.bwstudent.com/images/tech/community_pic/2019-03-01/3190820190301111817.jpg","id":28,"praise":4,"whetherGreat":1},{"comment":0,"content":"咿呀","file":"http://mobile.bwstudent.com/images/tech/community_pic/2019-03-01/2647120190301103305.jpg","id":13,"praise":0,"whetherGreat":2},{"comment":0,"content":"哈哈哈","file":"http://mobile.bwstudent.com/images/tech/community_pic/2019-03-01/3697420190301103226.jpg","id":11,"praise":0,"whetherGreat":2}]
     * communityUserVo : {"headPic":"http://mobile.bwstudent.com/images/tech/default/tech.jpg","nickName":"咿呀","power":2,"userId":20,"whetherFollow":2,"whetherMyFriend":2}
     */

    private CommunityUserVoBean communityUserVo;
    private List<CommunityUserPostVoListBean> communityUserPostVoList;

    public CommunityUserVoBean getCommunityUserVo() {
        return communityUserVo;
    }

    public void setCommunityUserVo(CommunityUserVoBean communityUserVo) {
        this.communityUserVo = communityUserVo;
    }

    public List<CommunityUserPostVoListBean> getCommunityUserPostVoList() {
        return communityUserPostVoList;
    }

    public void setCommunityUserPostVoList(List<CommunityUserPostVoListBean> communityUserPostVoList) {
        this.communityUserPostVoList = communityUserPostVoList;
    }

    public static class CommunityUserVoBean {
        /**
         * headPic : http://mobile.bwstudent.com/images/tech/default/tech.jpg
         * nickName : 咿呀
         * power : 2
         * userId : 20
         * whetherFollow : 2
         * whetherMyFriend : 2
         */

        private String headPic;
        private String nickName;
        private int power;
        private int userId;
        private int whetherFollow;
        private int whetherMyFriend;

        public String getHeadPic() {
            return headPic;
        }

        public void setHeadPic(String headPic) {
            this.headPic = headPic;
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

        public int getWhetherMyFriend() {
            return whetherMyFriend;
        }

        public void setWhetherMyFriend(int whetherMyFriend) {
            this.whetherMyFriend = whetherMyFriend;
        }
    }

    public static class CommunityUserPostVoListBean {
        /**
         * comment : 0
         * content : 呀呀呀
         * file : http://mobile.bwstudent.com/images/tech/community_pic/2019-03-01/8467120190301150127.jpg
         * id : 43
         * praise : 1
         * whetherGreat : 2
         */

        private int comment;
        private String content;
        private String file;
        private int id;
        private int praise;
        private int whetherGreat;

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

        public int getPraise() {
            return praise;
        }

        public void setPraise(int praise) {
            this.praise = praise;
        }

        public int getWhetherGreat() {
            return whetherGreat;
        }

        public void setWhetherGreat(int whetherGreat) {
            this.whetherGreat = whetherGreat;
        }
    }

    /**
     * communityUserPostVoList : [{"comment":0,"content":"哈喽","file":"","id":418,"praise":1,"whetherGreat":2},{"comment":0,"content":"谁删的帖子","file":"","id":417,"praise":1,"whetherGreat":2}]
     * communityUserVo : {"headPic":"http://172.17.8.100/images/tech/default/tech.jpg","nickName":"Game player","userId":1021,"whetherFollow":2,"whetherMyFriend":2}
     *//*

    private CommunityUserVoBean communityUserVo;
    private List<CommunityUserPostVoListBean> communityUserPostVoList;

    public CommunityUserVoBean getCommunityUserVo() {
        return communityUserVo;
    }

    public void setCommunityUserVo(CommunityUserVoBean communityUserVo) {
        this.communityUserVo = communityUserVo;
    }

    public List<CommunityUserPostVoListBean> getCommunityUserPostVoList() {
        return communityUserPostVoList;
    }

    public void setCommunityUserPostVoList(List<CommunityUserPostVoListBean> communityUserPostVoList) {
        this.communityUserPostVoList = communityUserPostVoList;
    }

    public static class CommunityUserVoBean {
        *//**
         * headPic : http://172.17.8.100/images/tech/default/tech.jpg
         * nickName : Game player
         * userId : 1021
         * whetherFollow : 2
         * whetherMyFriend : 2
         *//*

        private String headPic;
        private String nickName;
        private int userId;
        private int whetherFollow;
        private int whetherMyFriend;

        public String getHeadPic() {
            return headPic;
        }

        public void setHeadPic(String headPic) {
            this.headPic = headPic;
        }

        public String getNickName() {
            return nickName;
        }

        public void setNickName(String nickName) {
            this.nickName = nickName;
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

        public int getWhetherMyFriend() {
            return whetherMyFriend;
        }

        public void setWhetherMyFriend(int whetherMyFriend) {
            this.whetherMyFriend = whetherMyFriend;
        }
    }

    public static class CommunityUserPostVoListBean {
        *//**
         * comment : 0
         * content : 哈喽
         * file :
         * id : 418 帖子id
         * praise : 1
         * whetherGreat : 2
         *//*

        private int comment;
        private String content;
        private String file;
        private int id;
        private int praise;
        private int whetherGreat;

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

        public int getPraise() {
            return praise;
        }

        public void setPraise(int praise) {
            this.praise = praise;
        }

        public int getWhetherGreat() {
            return whetherGreat;
        }

        public void setWhetherGreat(int whetherGreat) {
            this.whetherGreat = whetherGreat;
        }
    }*/


}
