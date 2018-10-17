package com.toly1994.net.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.toly1994.net.R;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import top.toly.zutils.core.shortUtils.StrUtil;
import top.toly.zutils.core.shortUtils.ToastUtil;

/**
 * 作者：张风捷特烈<br/>
 * 时间：2018/10/15 0015:15:47<br/>
 * 邮箱：1981462002@qq.com<br/>
 * 说明：
 */
public class ChatActivity extends AppCompatActivity implements Runnable {

    @BindView(R.id.id_et_msg_content)
    EditText mIdEtMsgContent;
    @BindView(R.id.id_btn_send)
    Button mIdBtnSend;
    @BindView(R.id.id_tv_msg)
    TextView mIdTvMsg;
    @BindView(R.id.id_btn_name)
    Button mIdBtnName;
    @BindView(R.id.id_btn_ip)
    Button mIdBtnIp;
    @BindView(R.id.id_et_name)
    EditText mIdEtName;
    @BindView(R.id.id_et_ip)
    EditText mIdEtIp;

    private String username, ip, msg, msgFromServer;

    public static final int PORT = 8080;

    Socket mSocket;
    Thread mThread;
    DataInputStream dis;
    DataOutputStream dos;
    boolean flag = false;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_chat);
        ButterKnife.bind(this);

        mIdEtName.setText("toly");
        mIdEtIp.setText("192.168.10.107");
    }

    @Override
    public void run() {
        while (true) {
            try {
                msgFromServer = dis.readUTF() + "\n";
                runOnUiThread(()->{
                    mIdTvMsg.append(msgFromServer);
                });

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @OnClick({R.id.id_btn_send, R.id.id_btn_name, R.id.id_btn_ip})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.id_btn_send:
                send();
                break;
            case R.id.id_btn_name:
                new Thread(() -> {
                    enter();
                }).start();
                break;
            case R.id.id_btn_ip:
                leave();
                break;
        }
    }

    private void leave() {
        if (flag) {
            try {
                dos.writeUTF("!!" + username + "下线了,大家欢送");
                dos.close();
                dis.close();
                mSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            flag = false;
            ToastUtil.showAtOnce(this, "成功退出");
        }
    }

    /**
     * 发送消息
     */
    private void send() {
        if (!flag) {
            ToastUtil.showAtOnce(this, "请登陆");
            return;
        }

        msg = mIdEtMsgContent.getText().toString();

        if (msg != null) {
            String now = new SimpleDateFormat("hh:mm:ss").format(new Date());

            try {
                dos.writeUTF("~~" + username + " " + now + "说: " + msg);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


    }

    /**
     * 进入聊天服务器
     */
    private void enter() {
        if (flag) {
            ToastUtil.showAtOnce(this, "已登陆");
            return;
        }

        username = mIdEtName.getText().toString();
        ip = mIdEtIp.getText().toString();

        if (!StrUtil.isEmpty(username) && !StrUtil.isEmpty(ip)) {
            try {
                mSocket = new Socket(ip, PORT);
                dis = new DataInputStream(mSocket.getInputStream());
                dos = new DataOutputStream(mSocket.getOutputStream());
                String now = new SimpleDateFormat("hh:mm:ss").format(new Date());
                //向服务端写数据
                dos.writeUTF(username + " " + now + " 上线");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        mThread = new Thread(this);
        mThread.start();
        flag = true;
    }
}
