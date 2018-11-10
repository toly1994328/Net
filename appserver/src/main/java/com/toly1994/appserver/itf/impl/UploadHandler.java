package com.toly1994.appserver.itf.impl;

import com.toly1994.appserver.bean.HttpContext;
import com.toly1994.appserver.itf.ResUriHandler;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;

import top.toly.zutils.core.io.PathUtils;

/**
 * 作者：张风捷特烈<br/>
 * 时间：2018/10/25 0025:13:01<br/>
 * 邮箱：1981462002@qq.com<br/>
 * 说明：HTTP/1.1 200 OK
 */
public class UploadHandler implements ResUriHandler {

    private String prefix = "/upload_app/";

    @Override
    public boolean accept(String uri) {
        return uri.startsWith(prefix);
    }

    @Override
    public void handle(String uri, HttpContext httpContext) throws IOException {

        Long length = Long.valueOf(httpContext.getHeaderValue("content-length"));

        String path = PathUtils.getSDPath() + "/upLoad.jpg";
        FileOutputStream fos = new FileOutputStream(path);
        InputStream is = httpContext.getSocket().getInputStream();

        byte[] buf = new byte[1024 * 10];
        int len = 0;

        while (true) {
            if (len == -1) {
                break;
            }
            len = is.read(buf);
            fos.write(buf, 0, len);
        }

        OutputStream os = httpContext.getSocket().getOutputStream();
        PrintStream pw = new PrintStream(os, true);
        pw.println("HTTP/1.1 200 OK");
        pw.println();

        onImgLoaded(path);
    }

    public void onImgLoaded(String path) {

    }
}
