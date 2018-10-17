package com.toly1994.net.bean;

import java.util.Date;

/**
 * 作者：张风捷特烈<br/>
 * 时间：2018/10/15 0015:14:06<br/>
 * 邮箱：1981462002@qq.com<br/>
 * 说明：
 */
public class Client {
    private String ip;
    private Date linkedTime;
    private Date unLinkedTime;

    public Client(String ip, Date linkedTime, Date unLinkedTime) {
        this.ip = ip;
        this.linkedTime = linkedTime;
        this.unLinkedTime = unLinkedTime;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public Date getLinkedTime() {
        return linkedTime;
    }

    public void setLinkedTime(Date linkedTime) {
        this.linkedTime = linkedTime;
    }

    public Date getUnLinkedTime() {
        return unLinkedTime;
    }

    public void setUnLinkedTime(Date unLinkedTime) {
        this.unLinkedTime = unLinkedTime;
    }

    @Override
    public String toString() {
        return "Client{" +
                "ip='" + ip + '\'' +
                ", linkedTime=" + linkedTime +
                ", unLinkedTime=" + unLinkedTime +
                '}';
    }
}
