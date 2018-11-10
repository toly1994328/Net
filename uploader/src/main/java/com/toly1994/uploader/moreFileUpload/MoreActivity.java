package com.toly1994.uploader.moreFileUpload;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.toly1994.uploader.Cons;
import com.toly1994.uploader.R;

import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import top.toly.zutils.core.io.IOUtils;
import top.toly.zutils.core.shortUtils.L;
import top.toly.zutils.core.shortUtils.ToastUtil;
import top.toly.zutils.core.ui.common.BMUtils;
import top.toly.zutils.core.ui.common.ScrUtil;
import top.toly.zutils.core.zhttp.Poster;
import top.toly.zutils.core.zhttp.itf.Listener;
import top.toly.zutils.core.zhttp.itf.binaryImpl.FileBinary;
import top.toly.zutils.core.zhttp.itf.binaryImpl.InputStreamBinary;
import top.toly.zutils.core.zhttp.rep.Response;
import top.toly.zutils.core.zhttp.req.Request;
import top.toly.zutils.core.zhttp.req.RequestExecutor;
import top.toly.zutils.core.zhttp.req.RequestMethod;
import top.toly.zutils.core.zhttp.req.reqImpl.StringRequest;

/**
 * 作者：张风捷特烈<br/>
 * 时间：2018/10/16 0016:14:24<br/>
 * 邮箱：1981462002@qq.com<br/>
 * 说明：
 */
public class MoreActivity extends AppCompatActivity {
    private static final String TAG = "MoreActivity";
    @BindView(R.id.id_btn_get)
    Button mIdBtnGet;
    @BindView(R.id.id_btn_post)
    Button mIdBtnPost;
    @BindView(R.id.id_btn_post_str)
    Button mIdBtnPostStr;
    @BindView(R.id.id_btn_post_file)
    Button mIdBtnPostFile;
    @BindView(R.id.id_btn_upload_pic)
    Button mIdBtnUploadPic;
    @BindView(R.id.id_btn_download)
    Button mIdBtnDownload;
    @BindView(R.id.id_btn_download_pic)
    Button mIdBtnDownloadPic;
    @BindView(R.id.id_iv_show)
    ImageView mIdIvShow;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        mIdBtnPostStr.setText("异步通信封装");

    }

    @OnClick({R.id.id_btn_get, R.id.id_btn_post, R.id.id_btn_post_str, R.id.id_btn_post_file, R.id.id_btn_upload_pic, R.id.id_btn_download, R.id.id_btn_download_pic, R.id.id_iv_show})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.id_btn_get:
                new Thread(() -> executeGET()).start();


                break;
            case R.id.id_btn_post:
                new Thread(() -> executePOST()).start();
                break;
            case R.id.id_btn_post_str:

                File sdDir = Environment.getExternalStorageDirectory();
                File file = new File(sdDir, "DCIM/Camera/iv_500x400.png");
                File file2 = new File(sdDir, "JVM/对象的访问定位.png");
                File file3 = new File(sdDir, "JVM/共享变量可见性.png");
                File file4 = new File(sdDir, "JVM/标记整理.png");
                File file5 = new File(sdDir, "JVM/可达性分析.png");
                File file6 = new File(sdDir, "JVM/复制算法.png");
                File file7 = new File(sdDir, "JVM/虚拟机栈.png");


                Request<String> request = new StringRequest(Cons.BASE_URL + "upload", RequestMethod.POST);

                request.add("file", new FileBinary(file));
                request.add("file", new FileBinary(file2));
                request.add("file", new FileBinary(file3));
                request.add("file", new FileBinary(file4));
                request.add("file", new FileBinary(file5));
                request.add("file", new FileBinary(file6));
                FileBinary fileBinary = new FileBinary(file7);

                fileBinary.setOnProgressListener((progress) -> {
                    L.d("file7" + "===============" + progress + L.l());
                    mIdBtnPostStr.setText(progress+"%");
                });

                request.add("file", fileBinary);

                //将当前屏幕截图上传到服务器
                Bitmap bitmap = ScrUtil.snapShotWithStatusBar(this);
                InputStreamBinary isb = new InputStreamBinary(BMUtils.bitmap2InputStream(bitmap), "screen.png");

                //设置进度监听
                isb.setOnProgressListener((progress) -> {
                    L.d("screen" + "===============" + progress + L.l());
                    mIdBtnUploadPic.setText(progress+"%");

                });
                request.add("file", isb);

                RequestExecutor.INSTANCE.execute(request, new Listener<String>() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        String repBody = response.getRepBody();
                        L.d(repBody + L.l());
                    }

                    @Override
                    public void onFailed(Exception e) {

                    }
                });

                break;
            case R.id.id_btn_post_file:
                break;
            case R.id.id_btn_upload_pic:
                break;
            case R.id.id_btn_download:
                break;
            case R.id.id_btn_download_pic:
                break;
            case R.id.id_iv_show:
                break;
        }
    }

    private void executePOST() {
        OutputStream os = null;
        try {
            URL url = new URL(Cons.BASE_URL + "swords/postWithBody?name=hello");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");

            if (conn.getResponseCode() == 200) {
                InputStream is = conn.getInputStream();
                String read = IOUtils.read(is);
                Log.e(TAG, "executeGET: " + read);
                os = conn.getOutputStream();
                os.write("hello 客户端".getBytes());

                Poster.newInstance().post(() -> {
                    ToastUtil.showAtOnce(this, read);
                });
                os.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void executeGET() {
        try {
            URL url = new URL(Cons.BASE_URL + "swords/find/21");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            if (conn.getResponseCode() == 200) {
                Map<String, List<String>> headerFields = conn.getHeaderFields();
                for (String key : headerFields.keySet()) {
                    Log.e(TAG, key + " = " + headerFields.get(key));
                }
                InputStream is = conn.getInputStream();
                String read = IOUtils.read(is);
                Log.e(TAG, "executeGET: " + read);
                Poster.newInstance().post(() -> {
                    ToastUtil.showAtOnce(this, read);
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
