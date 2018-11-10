package com.toly1994.appserver;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.toly1994.appserver.bean.WebConfiguration;
import com.toly1994.appserver.itf.impl.ResInAssetsHandler;
import com.toly1994.appserver.itf.impl.UploadHandler;

import java.io.IOException;

import top.toly.zutils.core.http.IpUtils;
import top.toly.zutils.core.shortUtils.L;

public class MainActivity extends AppCompatActivity {

    private AppHttpServer mServer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        WebConfiguration cfg = new WebConfiguration(8080, 100);
        mServer = new AppHttpServer(cfg);
        mServer.registerResHandler(new ResInAssetsHandler(this));
        mServer.registerResHandler(new UploadHandler(){
            @Override
            public void onImgLoaded(String path) {
                L.d(path + L.l());
            }
        });

        mServer.startAsync();

        new Thread() {
            @Override
            public void run() {
                L.d(IpUtils.getIPAddress(MainActivity.this) + L.l());
            }
        }.start();

    }

    @Override//192.168.43.60  error/error404.html
    protected void onDestroy() {
        super.onDestroy();
        try {
            mServer.stopAsync();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
