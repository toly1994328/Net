package com.toly1994.net.activity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.toly1994.net.R;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 作者：张风捷特烈<br/>
 * 时间：2018/10/15 0015:9:03<br/>
 * 邮箱：1981462002@qq.com<br/>
 * 说明：
 */
public class ResFormNetActivity extends AppCompatActivity {
    private static final String TAG = "ResFormNetActivity";
    @BindView(R.id.btn_get)
    Button mBtnGet;
    @BindView(R.id.id_iv_img)
    ImageView mIdIvImg;
    @BindView(R.id.id_tv_json)
    TextView mIdTvJson;
    @BindView(R.id.id_et_url)
    EditText mIdEtUrl;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_get_pic);
        ButterKnife.bind(this);

        String path = "http://www.toly1994.com:8080/imgs/logo/BroadcastReceiver.jpg";
        mIdEtUrl.setText(path);
    }

    @OnClick(R.id.btn_get)
    public void onViewClicked() {
        new Thread(() -> {
            String result = getRes(mIdEtUrl.getText().toString());

            Bitmap img = decodeImg(mIdEtUrl.getText().toString());

            runOnUiThread(() -> {
//                mIdTvJson.setText(result);
                mIdIvImg.setImageBitmap(img);
            });
        }).start();
    }

    /**
     * 通过url返回一个Bitmap
     * @param path url
     * @return 图片
     */
    private Bitmap decodeImg(String path) {
        InputStream is = null;
        OutputStream os = null;
        try {
            //1.创建URL对象url
            URL url = new URL(path);
            //2.使用url连接并获取HttpURLConnection对象conn
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            //3.为conn对象设置请求信息
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(5000);
            //4.校验响应码,200代表请求成功
            if (conn.getResponseCode() == 200) {
                //5.通过conn获取服务器传来的输入流
                is = conn.getInputStream();
                //6.对流进行操作
                return BitmapFactory.decodeStream(is);
            }
        } catch (Exception e) {
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
        return null;
    }

    /**
     * 通过url访问服务器,并接收服务器响应数据
     *
     * @param path url
     */
    private String getRes(String path) {
        InputStream is = null;
        try {
            //1.创建URL对象url
            URL url = new URL(path);
            //2.使用url连接并获取HttpURLConnection对象conn
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            //3.为conn对象设置请求信息
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(5000);
            //4.校验响应码,200代表请求成功
            if (conn.getResponseCode() == 200) {
                //5.通过conn获取服务器传来的输入流
                is = conn.getInputStream();
                //6.对流进行操作
                StringBuilder sb = new StringBuilder();
                int len = 0;
                byte[] buf = new byte[1024];
                while ((len = is.read(buf)) != -1) {
                    sb.append(new String(buf, 0, len));
                }
                return sb.toString();
            }
        } catch (Exception e) {
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
        return "error";
    }
}
