package com.wd.tech.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;

import java.io.Serializable;
import org.greenrobot.greendao.annotation.Generated;
@Entity
public class Conversation{

    /**
     * headPic : http://172.17.8.100/images/tech/head_pic/2018-10-08/20181008085110.jpg
     * nickName : 周考
     * pwd : R+0jdN3P4MXHPMFVe9cX5MbX5ulIXHJkfigPLKEeTBY5lUgxJWUNg0js1oGtbsKiLFL4ScqdmUbtHXIfrgQnWrwTNjf09OJLycbeJ+ka4+CV7I1eEqG8DtZPnQoCyxjoYMjO4soDl6EX9YgqaZp3DlUH4pXrYHYz58YyFkSeJEk=
     * userId : 1078
     * userName : kx30FD16619998889
     */

    private String headPic;
    private String nickName;
    private String pwd;
    @Id(autoincrement = true)
    private long userId;
    private String userName;

    @Generated(hash = 895508773)
    public Conversation(String headPic, String nickName, String pwd, long userId, String userName) {
        this.headPic = headPic;
        this.nickName = nickName;
        this.pwd = pwd;
        this.userId = userId;
        this.userName = userName;
    }

    @Generated(hash = 1893991898)
    public Conversation() {
    }

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

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
