package com.toly1994.appserver;

import java.io.IOException;
import java.io.InputStream;

/**
 * 作者：张风捷特烈<br/>
 * 时间：2018/10/25 0025:11:22<br/>
 * 邮箱：1981462002@qq.com<br/>
 * 说明：
 */
public class StreamUtils {
    public static final String readLine(InputStream is) throws IOException {
        StringBuilder sb = new StringBuilder();
        int c1 = 0;
        int c2 = 0;
        while (c2 != -1 && !(c1 == '\r' && c2 == '\n')) {
            c1 = c2;
            c2 = is.read();
            sb.append((char) c2);
        }
        if (sb.length() == 0) {
            return null;
        }
        return sb.toString();
    }
}
