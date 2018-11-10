package com.toly1994.appserver.bean;

import java.net.Socket;
import java.util.HashMap;

/**
 * 作者：张风捷特烈<br/>
 * 时间：2018/10/25 0025:12:32<br/>
 * 邮箱：1981462002@qq.com<br/>
 * 说明：
 */
public class HttpContext {

    private Socket mSocket;
    private final HashMap<String, String> mReqHashMap;

    public HttpContext() {
        mReqHashMap = new HashMap<>();

    }

    public void setSocket(Socket socket) {

        mSocket = socket;
    }

    public Socket getSocket() {
        return mSocket;
    }

    public void addReqHeader(String k, String v) {
        mReqHashMap.put(k, v);
    }

    public HashMap<String, String> getReqHashMap() {
        return mReqHashMap;
    }

    public String getHeaderValue(String key) {
        return mReqHashMap.get(key);
    }
}
