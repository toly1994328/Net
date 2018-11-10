package com.toly1994.appserver;

import com.toly1994.appserver.bean.HttpContext;
import com.toly1994.appserver.bean.WebConfiguration;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import top.toly.zutils.core.io.IOUtils;
import top.toly.zutils.core.shortUtils.L;

/**
 * 作者：张风捷特烈<br/>
 * 时间：2018/10/25 0025:9:36<br/>
 * 邮箱：1981462002@qq.com<br/>
 * 说明：服务端
 */
public class AppHttpServer_Copy {
    /**
     * 网络的配置对象
     */
    private WebConfiguration mWebConfiguration;
    /**
     * Socket服务
     */
    private ServerSocket mServerSocket;
    /**
     * 服务是否可用
     */
    private boolean isEnable;
    /**
     * 线程池
     */
    private final ExecutorService mThreadPool;

    public AppHttpServer_Copy(WebConfiguration webConfiguration) {

        mWebConfiguration = webConfiguration;

        //线程执行完，不会立刻销毁
        mThreadPool = Executors.newCachedThreadPool();

    }

    /**
     * 开启Server(异步)
     */
    public void startAsync() {
        isEnable = true;
        new Thread(this::doProAsync).start();
    }

    private void doProAsync() {
        try {
            InetSocketAddress address = new InetSocketAddress(mWebConfiguration.getPort());
            mServerSocket = new ServerSocket();
            mServerSocket.bind(address);//手机上监听的端口


            while (isEnable) {
                //该方法阻塞----当远端连接后阻塞停止
                Socket socket = mServerSocket.accept();

                //2.通过accept方法获取Socket对象
                String ip = socket.getInetAddress().getHostAddress();
                L.d(ip + "....connected" + L.l());//日志：打印连接的客户端，

                mThreadPool.execute(() -> {
                    onAccept(socket);
                });
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 客户端连接后执行
     *
     * @param socket
     */
    private void onAccept(Socket socket) {
        OutputStream os = null;
        InputStream is = null;
        HttpContext httpContext = new HttpContext();
//        httpContext.setSocket(socket);
        try {
            os = socket.getOutputStream();
            os.write("hello Client".getBytes());

            //接收客户端的请求数据：(对浏览器而言就是请求头)
            is = socket.getInputStream();
            String line = null;
            //将字节流转为字符流
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            while ((line = br.readLine()) != null) {
                L.d(line + L.l());
//                line.split(": ")
            }
            String read = IOUtils.read(is);
            L.d(read + L.l());


        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (os != null) {
                    os.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 停止Server()
     */
    public void stopAsync() throws IOException {

        if (!isEnable) {
            return;
        }
        isEnable = false;
        mServerSocket.close();
    }
}
