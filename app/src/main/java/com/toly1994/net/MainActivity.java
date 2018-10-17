package com.toly1994.net;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";



    @BindView(R.id.id_tv_ip)
    TextView mIdTvIp;

    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            String obj = (String) msg.obj;
            mSb.append(obj + " ");
            mIdTvIp.setText(mSb.toString());

        }
    };
    private StringBuffer mSb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        mSb = new StringBuffer();

        new Thread() {
            @Override
            public void run() {
                try {
                    //1：创建udp socket，建立端点。
                    DatagramSocket ds = new DatagramSocket(8080);
                    while (true) {
                        //2：定义数据包。用于存储数据。
                        byte[] buf = new byte[1024];
                        DatagramPacket dp = new DatagramPacket(buf, buf.length);
                        //3:通过服务的receive方法将收到数据存入数据包中。
                        ds.receive(dp);//阻塞式方法。
                        //4:通过数据包的方法获取其中的数据。
                        String data = new String(dp.getData(), 0, dp.getLength());

                        Message msg = Message.obtain();
                        msg.obj = data;
                        mHandler.sendMessage(msg);
                    }
                    //5:关闭资源
                    //ds.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }
}
