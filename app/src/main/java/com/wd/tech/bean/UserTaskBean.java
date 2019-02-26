package com.wd.tech.bean;

/**
 * @author Tech
 * @date 2019/2/25 9:53
 * QQ:45198565
 * 佛曰：永无BUG 盘他！
 */
public class UserTaskBean {

    /**
     * status : 2
     * taskId : 1001
     * taskIntegral : 10
     * taskName : 每日签到
     * taskType : 1
     */

    private int status;
    private int taskId;
    private int taskIntegral;
    private String taskName;
    private int taskType;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getTaskId() {
        return taskId;
    }

    public void setTaskId(int taskId) {
        this.taskId = taskId;
    }

    public int getTaskIntegral() {
        return taskIntegral;
    }

    public void setTaskIntegral(int taskIntegral) {
        this.taskIntegral = taskIntegral;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public int getTaskType() {
        return taskType;
    }

    public void setTaskType(int taskType) {
        this.taskType = taskType;
    }
}
