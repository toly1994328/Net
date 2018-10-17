package com.toly1994.net.tcp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.toly1994.net.R;
import com.toly1994.net.app.Cons;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.Socket;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import top.toly.zutils.core.io.SpUtils;
import top.toly.zutils.core.shortUtils.ToastUtil;

public class TCPClientActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";


    @BindView(R.id.id_tv_ip)
    TextView mIdTvIp;
    @BindView(R.id.id_tcp)
    EditText mIdTcp;
    @BindView(R.id.id_btn_send)
    Button mIdBtnSend;
    @BindView(R.id.id_et_ip)
    EditText mIdEtIp;
    @BindView(R.id.id_btn_setip)
    Button mIdBtnSetip;
    private Socket mSocket;
    private PrintWriter mPwOut;
    String msg = null;
    private SpUtils<String> mSp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        mIdEtIp.setText(Cons.IP);
        mSp = new SpUtils<>(this);
    }



    @OnClick({R.id.id_btn_send, R.id.id_btn_setip})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.id_btn_setip:
                linkIP();
                //通过ip和端口连接服务端核心代码
                new Thread(() -> connServer(mIdEtIp.getText().toString(), 8080)).start();
                break;
            case R.id.id_btn_send:
                //将键盘输入内容发送给服务端
                mPwOut.println(mIdTcp.getText());
                break;

        }
    }

    /**
     * 通过IP连接服务端
     */
    private void linkIP() {
        mSp.put(Cons.SP_IP, mIdEtIp.getText().toString());
        String ip = mSp.get(Cons.SP_IP, Cons.IP);
        //通过ip和端口连接服务端核心代码
        new Thread(() -> connServer(ip, 8080)).start();
    }

    /**
     * 通过ip和端口连接服务端核心代码
     *
     * @param ip   ip地址
     * @param port 端口
     */
    private void connServer(String ip, int port) {
        try {
            //1.创建客户端Socket对象(ip,端口)
            mSocket = new Socket(ip, port);
            //2.通过socket对象获取字节输出流,并包装成PrintWriter----用于发送给服务端数据
            mPwOut = new PrintWriter(mSocket.getOutputStream(), true);
            //3.通过socket对象获取字节输出流
            InputStream is = mSocket.getInputStream();
            int len = 0;
            byte[] buf = new byte[1024];
            while ((len = is.read(buf)) != -1) {
                msg = new String(buf, 0, len).toUpperCase();
                runOnUiThread(() -> ToastUtil.showAtOnce(TCPClientActivity.this, msg));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mSocket != null) {
            try {
                mSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
