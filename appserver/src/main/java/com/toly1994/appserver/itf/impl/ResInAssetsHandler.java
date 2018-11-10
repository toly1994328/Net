package com.toly1994.appserver.itf.impl;

import android.content.Context;

import com.toly1994.appserver.bean.HttpContext;
import com.toly1994.appserver.itf.ResUriHandler;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;

import top.toly.zutils.core.io.IOUtils;

/**
 * 作者：张风捷特烈<br/>
 * 时间：2018/10/25 0025:13:01<br/>
 * 邮箱：1981462002@qq.com<br/>
 * 说明：
 */
public class ResInAssetsHandler implements ResUriHandler {

    private String prefix = "/static/";
    private Context mContext;

    public ResInAssetsHandler(Context context) {
        this.mContext = context;
    }

    @Override
    public boolean accept(String uri) {
        return uri.contains(prefix);
    }

    @Override
    public void handle(String uri, HttpContext httpContext) throws IOException {

        int startIndex = prefix.length();
        String assetPath = uri.substring(startIndex);
        InputStream is = mContext.getAssets().open(assetPath);

        byte[] bytes = IOUtils.read2bytes(is);
        is.close();


        OutputStream os = httpContext.getSocket().getOutputStream();
        PrintStream pw = new PrintStream(os, true);
        pw.println("HTTP/1.1 200 OK");
        pw.println("Content-Length: " + bytes.length);
        if (assetPath.endsWith(".html")) {
            pw.println("Content-Type:text/html");
        } else if (assetPath.endsWith(".js")) {
            pw.println("Content-Type:text/js");
        } else if (assetPath.endsWith(".css")) {
            pw.println("Content-Type:text/css");
        } else if (assetPath.endsWith(".jpg")) {
            pw.println("Content-Type:text/jpg");
        } else if (assetPath.endsWith(".png")) {
            pw.println("Content-Type:text/png");
        }
        pw.println();
        pw.write(bytes);


    }
}
