package com.toly1994.appserver;

import com.toly1994.appserver.bean.HttpContext;
import com.toly1994.appserver.bean.WebConfiguration;
import com.toly1994.appserver.itf.ResUriHandler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 作者：张风捷特烈<br/>
 * 时间：2018/10/25 0025:9:36<br/>
 * 邮箱：1981462002@qq.com<br/>
 * 说明：服务端
 */
public class AppHttpServer {
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

    private Set<ResUriHandler> mResUriHandlers;


    public AppHttpServer(WebConfiguration webConfiguration) {

        mWebConfiguration = webConfiguration;

        //线程执行完，不会立刻销毁
        mThreadPool = Executors.newCachedThreadPool();
        mResUriHandlers = new HashSet<>();

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
        InputStream is = null;
        HttpContext httpContext = new HttpContext();
        httpContext.setSocket(socket);
        try {
            //接收客户端的请求数据：(对浏览器而言就是请求头)
            is = socket.getInputStream();
            String line = null;
            //将字节流转为字符流
            BufferedReader br = new BufferedReader(new InputStreamReader(is));

//            String[] firstLineSplit = br.readLine().split(" ");
            String prefix = "/upload_app/";
//            if (firstLineSplit.length > 1) {
//                prefix = firstLineSplit[1];
//            }

            while (true) {
                line = br.readLine();
                if ("".equals(line)) {
                    break;
                }

                String[] split = line.split(": ");
                if (split.length > 1) {
                    httpContext.addReqHeader(split[0], split[1]);
                }

            }

            for (ResUriHandler resUriHandler : mResUriHandlers) {
                if (!resUriHandler.accept(prefix)) {
                    continue;
                }

                resUriHandler.handle(prefix, httpContext);

            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (is != null) {
                    is.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    public void registerResHandler(ResUriHandler resUriHandler) {
        mResUriHandlers.add(resUriHandler);
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
