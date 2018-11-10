package com.toly1994.appserver.itf;

import com.toly1994.appserver.bean.HttpContext;

import java.io.IOException;

/**
 * 作者：张风捷特烈<br/>
 * 时间：2018/10/25 0025:12:54<br/>
 * 邮箱：1981462002@qq.com<br/>
 * 说明：
 */
public interface ResUriHandler {
    boolean accept(String uri);

    void handle(String uri, HttpContext httpContext) throws IOException;
}
