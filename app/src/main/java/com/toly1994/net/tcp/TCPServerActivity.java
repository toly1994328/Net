package com.toly1994.net.tcp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.toly1994.net.R;

import java.io.IOException;
import java.net.ServerSocket;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class TCPServerActivity extends AppCompatActivity {
    private static final String TAG = "TCPServerActivity";
    @BindView(R.id.id_btn_start_server)
    Button mIdBtnStartServer;
    @BindView(R.id.id_tv_link)
    TextView mIdTvLink;
    @BindView(R.id.id_btn_close_server)
    Button mIdBtnCloseServer;
    private JoinRunnable mJoinRunnable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_server);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.id_btn_start_server, R.id.id_btn_close_server})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.id_btn_start_server://开启服务
                new Thread(() -> {
                    try {
                        //1.开启服务器
                        ServerSocket serverSocket = new ServerSocket(8080);
                        runOnUiThread(() -> mIdTvLink.setText("服务已开启"));
                        mJoinRunnable = new JoinRunnable(serverSocket);
                        //2.将连接的客户端显示在服务端界面
                        mJoinRunnable.setOnConnListener(ip -> runOnUiThread(() -> {
                            mIdTvLink.append("\n" + ip + "已连接");
                        }));
                        //3.为该客户端开启服务线程
                        new Thread(mJoinRunnable).start();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }).start();
                break;
            case R.id.id_btn_close_server://关闭服务
                if (mJoinRunnable != null) {
                    mIdTvLink.setText("服务未开启");
                    mJoinRunnable.close();
                }
                break;
        }
    }

}
