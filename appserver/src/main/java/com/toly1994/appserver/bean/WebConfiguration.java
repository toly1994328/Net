package com.toly1994.appserver.bean;

/**
 * 作者：张风捷特烈<br/>
 * 时间：2018/10/25 0025:10:27<br/>
 * 邮箱：1981462002@qq.com<br/>
 * 说明：
 */
public class WebConfiguration {
    /**
     * 端口
     */
    private int port;
    /**
     * 最大并发数
     */
    private int maxParallels;

    public WebConfiguration() {
    }

    public WebConfiguration(int port, int maxParallels) {
        this.port = port;
        this.maxParallels = maxParallels;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public int getMaxParallels() {
        return maxParallels;
    }

    public void setMaxParallels(int maxParallels) {
        this.maxParallels = maxParallels;
    }
}
