package com.wd.tech.bean;

import java.io.Serializable;
import java.util.List;

/**
 * date:2019/2/20 16:24
 * author:陈国星(陈国星)
 * function:
 */
public class FindUser implements Serializable {

    /**
     * email : 999@163.com
     * headPic : http://172.17.8.100/images/tech/head_pic/2018-09-30/20180930093918.jpg
     * integral : 0
     * nickName : 江山
     * phone : 16619998889
     * sex : 1
     * signature : 打江山
     * userId : 1078
     * whetherVip : 2
     * whetherFaceId : 1
     */

    private String email;
    private String headPic;
    private int integral;
    private String nickName;
    private String phone;
    private int sex;
    private String signature;
    private int userId;
    private int whetherVip;
    private int whetherFaceId;
    private Long vipExpireDate;
    private List<MyGroup> myGroupList;

    private String userName;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getHeadPic() {
        return headPic;
    }

    public void setHeadPic(String headPic) {
        this.headPic = headPic;
    }

    public int getIntegral() {
        return integral;
    }

    public void setIntegral(int integral) {
        this.integral = integral;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getWhetherVip() {
        return whetherVip;
    }

    public void setWhetherVip(int whetherVip) {
        this.whetherVip = whetherVip;
    }

    public int getWhetherFaceId() {
        return whetherFaceId;
    }

    public void setWhetherFaceId(int whetherFaceId) {
        this.whetherFaceId = whetherFaceId;
    }

    public Long getVipExpireDate() {
        return vipExpireDate;
    }

    public void setVipExpireDate(Long vipExpireDate) {
        this.vipExpireDate = vipExpireDate;
    }

    public List<MyGroup> getMyGroupList() {
        return myGroupList;
    }

    public void setMyGroupList(List<MyGroup> myGroupList) {
        this.myGroupList = myGroupList;
    }
}
