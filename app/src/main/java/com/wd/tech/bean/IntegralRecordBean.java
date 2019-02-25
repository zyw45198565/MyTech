package com.wd.tech.bean;

/**
 * @author Tech
 * @date 2019/2/25 8:57
 * QQ:45198565
 * 佛曰：永无BUG 盘他！
 */
public class IntegralRecordBean {

    /**
     * amount : 50
     * createTime : 1551054132000
     * direction : 1
     * type : 8
     */

    private int amount;
    private long createTime;
    private int direction;
    private int type;

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public int getDirection() {
        return direction;
    }

    public void setDirection(int direction) {
        this.direction = direction;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
