package com.toly1994.net.tcp;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * 作者：张风捷特烈<br/>
 * 时间：2018/10/14 0014:8:01<br/>
 * 邮箱：1981462002@qq.com<br/>
 * 说明：每次客户端加入时创建的线程
 */
public class JoinRunnable implements Runnable {

    private ServerSocket mServerSocket;//ServerSocket对象
    private Socket mSocket;//Socket对象

    public JoinRunnable(ServerSocket serverSocket) {
        mServerSocket = serverSocket;
    }

    @Override
    public void run() {
        InputStream is = null;
        OutputStream os = null;
        try {
            //1.通过服务端ServerSocket获取Socket对象
            mSocket = mServerSocket.accept();
            //2.获取连接的客户端ip,并设置连接时的回调监听
            String ip = mSocket.getInetAddress().getHostAddress();
            if (mOnConnListener != null) {
                mOnConnListener.conn(ip);
            }
            //3.通过Socket获取输入I流，读取客户端的信息
            is = mSocket.getInputStream();
            //4.通过Socket获取输出O流，向客户端发送信息
            os = mSocket.getOutputStream();
            //5.IO操作
            int len = 0;
            byte[] buf = new byte[1024];
            while ((len = is.read(buf)) != -1) {
                String msg = new String(buf, 0, len).toUpperCase();
                //将读出来的信息进行操作后返回给客户端
                os.write(("服务端发来信息:" + msg).getBytes());
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (is != null) {
                    is.close();
                }
                if (os != null) {
                    os.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 关闭客户端连接
     */
    public void close() {
        try {
            mServerSocket.close();
            mSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //客户端连接时的监听
    public interface OnConnListener {
        void conn(String ip);
    }

    private OnConnListener mOnConnListener;

    public void setOnConnListener(OnConnListener onConnListener) {
        mOnConnListener = onConnListener;
    }
}
