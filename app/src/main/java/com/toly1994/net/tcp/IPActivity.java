package com.toly1994.net.tcp;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.toly1994.net.R;

import java.util.Enumeration;
import java.util.Properties;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import top.toly.zutils.core.http.IpUtils;

public class IPActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    @BindView(R.id.id_tv_ip)
    TextView mIdTvIp;

    StringBuffer sb = new StringBuffer();

    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            Properties prop = (Properties) msg.obj;

            Enumeration<?> names = prop.propertyNames();
            while (names.hasMoreElements()) {
                String s = (String) names.nextElement();
                sb.append((s + "=" + prop.getProperty(s)) + "\n");
            }

            Log.e(TAG, "handleMessage: " + sb.toString());
            mIdTvIp.setText(sb.toString());

        }
    };
    @BindView(R.id.id_btn_client)
    Button mIdBtnClient;
    @BindView(R.id.id_btn_server)
    Button mIdBtnServer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);
        ButterKnife.bind(this);


        new Thread() {
            @Override
            public void run() {
                Log.e(TAG, "onCreate: " + IpUtils.getIPAddress(IPActivity.this));
                Message obtain = Message.obtain();
                obtain.obj = IpUtils.getIPAddress(IPActivity.this);
                mHandler.sendMessage(obtain);

            }
        }.start();

    }

    @OnClick({R.id.id_btn_client, R.id.id_btn_server})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.id_btn_client:
                startActivity(new Intent(this,TCPClientActivity.class));
                break;
            case R.id.id_btn_server:
                startActivity(new Intent(this,TCPServerActivity.class));
                break;
        }
    }
}
