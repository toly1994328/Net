package com.toly1994.uploader.okhttp;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.toly1994.uploader.Cons;
import com.toly1994.uploader.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import top.toly.zutils.core.shortUtils.ToastUtil;

public class MainActivity extends AppCompatActivity {

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

    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        startActivity(new Intent(this, PmsActivity.class));

    }

    @OnClick({R.id.id_btn_get, R.id.id_btn_post, R.id.id_btn_post_str, R.id.id_btn_post_file, R.id.id_btn_upload_pic, R.id.id_btn_download, R.id.id_btn_download_pic, R.id.id_iv_show})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.id_btn_get:
                doGet();
                break;
            case R.id.id_btn_post:
                doPost();
                break;
            case R.id.id_btn_post_str:
                doPostStr();
                break;
            case R.id.id_btn_post_file:
                doPostFile();
                break;
            case R.id.id_btn_upload_pic:
                doUpload();
                break;
            case R.id.id_btn_download:
                doDownload();
                break;
            case R.id.id_btn_download_pic:
                showImg();
                break;
            case R.id.id_iv_show:
                break;
        }
    }

    private void showImg() {
        //1.获取OkHttpClient对象
        OkHttpClient okHttpClient = new OkHttpClient();
        //2.获取Request对象
        Request request = new Request.Builder().get().url(Cons.BASE_URL + "imgs/test.jpg").build();
        //3.将Request封装为Call对象
        Call call = okHttpClient.newCall(request);
        //4.执行Call
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e(TAG, "onFailure: " + e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                InputStream is = response.body().byteStream();
                Bitmap bitmap = BitmapFactory.decodeStream(is);
                runOnUiThread(() -> {
                    mIdIvShow.setImageBitmap(bitmap);
                });
            }
        });
    }

    private void doDownload() {
        //1.获取OkHttpClient对象
        OkHttpClient okHttpClient = new OkHttpClient();
        //2.获取Request对象
        Request request = new Request.Builder().get().url(Cons.BASE_URL + "imgs/test.jpg").build();
        //3.将Request封装为Call对象
        Call call = okHttpClient.newCall(request);
        //4.执行Call
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e(TAG, "onFailure: " + e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                long total = response.body().contentLength();
                long sum = 0L;

                File file = new File(Environment.getExternalStorageDirectory(), "download.jpg");
                InputStream is = response.body().byteStream();
                FileOutputStream fos = new FileOutputStream(file);
                byte[] buf = new byte[102];
                int len = 0;
                while ((len = is.read(buf)) != -1) {
                    fos.write(buf, 0, len);
                    sum += len;
                    if (sum == total) {
                        mIdBtnDownload.setText("OK");
                        break;
                    }
                    mIdBtnDownload.setText(sum + "/" + total);
                }
                fos.close();
                is.close();
            }
        });
    }

    /**
     * 模拟表单上传文件：通过MultipartBody
     */
    private void doUpload() {
        File file = new File(Environment.getExternalStorageDirectory(), "DCIM/Camera/iv_500x400.png");
        RequestBody fileBody = RequestBody.create(MediaType.parse("application/octet-stream"), file);
        //1.获取OkHttpClient对象
        OkHttpClient okHttpClient = new OkHttpClient();
        //2.获取Request对象
        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("file", "test.jpg", fileBody)
                .build();

        CountingRequestBody countingRequestBody = new CountingRequestBody(requestBody, (byteWritten, contentLength) -> {
            Log.e(TAG, "doUpload: " + byteWritten + "/" + contentLength);
            if (byteWritten == contentLength) {
                mIdBtnUploadPic.setText("UpLoad OK");
            } else {
                mIdBtnUploadPic.setText(byteWritten + "/" + contentLength);
            }
        });

        Request request = new Request.Builder()
                .url(Cons.BASE_URL + "upload")
                .post(countingRequestBody).build();
        //3.将Request封装为Call对象
        Call call = okHttpClient.newCall(request);
        //4.执行Call
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e(TAG, "onFailure: " + e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result = response.body().string();
                Log.e(TAG, "onResponse: " + result);

                runOnUiThread(() -> ToastUtil.showAtOnce(MainActivity.this, result));
            }
        });
    }


    private void doPostFile() {
        File file = new File(Environment.getExternalStorageDirectory(), "DCIM/Camera/iv_500x400.png");
        //1.获取OkHttpClient对象
        OkHttpClient okHttpClient = new OkHttpClient();



        //2.构造Request--任意二进制流：application/octet-stream
        Request request = new Request.Builder()
                .url(Cons.BASE_URL + "PostFile")
                .post(RequestBody.create(MediaType.parse("application/octet-stream"), file)).build();
        //3.将Request封装为Call对象
        Call call = okHttpClient.newCall(request);
        //4.执行Call
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e(TAG, "onFailure: " + e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result = response.body().string();
                Log.e(TAG, "onResponse: " + result);

                runOnUiThread(() -> ToastUtil.showAtOnce(MainActivity.this, result));

            }
        });
    }

    private void doPostStr() {

        //1.获取OkHttpClient对象
        OkHttpClient okHttpClient = new OkHttpClient();


        okHttpClient.newBuilder().cookieJar(new CookieJar() {
            private final HashMap<HttpUrl, List<Cookie>> cookieStore = new HashMap<>();

            @Override
            public void saveFromResponse(HttpUrl url, List<Cookie> cookies) {
                cookieStore.put(url, cookies);
            }

            @Override
            public List<Cookie> loadForRequest(HttpUrl url) {
                List<Cookie> cookies = cookieStore.get(url);
                return cookies != null ? cookies : new ArrayList<>();
            }
        });


        //2.构造RequestBody
        RequestBody body = RequestBody.create(MediaType.parse("text/plain;chaset=utf-8"), "轻轻的我走了，\n" +
                "正如我轻轻的来；\n" +
                "我轻轻的招手，\n" +
                "作别西天的云彩。\n" +
                "\n" +
                "那河畔的金柳，\n" +
                "是夕阳中的新娘；\n" +
                "波光里的艳影，\n" +
                "在我的心头荡漾。\n" +
                "\n" +
                "软泥上的青荇，\n" +
                "油油的在水底招摇；\n" +
                "在康河的柔波里，\n" +
                "我甘心做一条水草！\n" +
                "\n" +
                "那榆荫下的一潭，\n" +
                "不是清泉，是天上虹；\n" +
                "揉碎在浮藻间，\n" +
                "沉淀着彩虹似的梦。\n" +
                "\n" +
                "寻梦？撑一支长篙，\n" +
                "向青草更青处漫溯；\n" +
                "满载一船星辉，\n" +
                "在星辉斑斓里放歌。\n" +
                "\n" +
                "但我不能放歌，\n" +
                "悄悄是别离的笙箫；\n" +
                "夏虫也为我沉默，\n" +
                "沉默是今晚的康桥！\n" +
                "\n" +
                "悄悄的我走了，\n" +
                "正如我悄悄的来；\n" +
                "我挥一挥衣袖，\n" +
                "不带走一片云彩。");
        Request request = new Request.Builder().url(Cons.BASE_URL + "PostString").post(body).build();
        //3.将Request封装为Call对象
        Call call = okHttpClient.newCall(request);
        //4.执行Call
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e(TAG, "onFailure: " + e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result = response.body().string();
                Log.e(TAG, "onResponse: " + result);

                runOnUiThread(() -> ToastUtil.showAtOnce(MainActivity.this, result));

            }
        });
    }

    private void doPost() {

        //1.获取OkHttpClient对象
        OkHttpClient okHttpClient = new OkHttpClient();
        //2.构造RequestBody
        FormBody body = new FormBody.Builder().add("id", "26").build();
        Request request = new Request.Builder().url(Cons.BASE_URL + "swords/postFind").post(body).build();
        //3.将Request封装为Call对象
        Call call = okHttpClient.newCall(request);
        //4.执行Call
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e(TAG, "onFailure: " + e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result = response.body().string();
                Log.e(TAG, "onResponse: " + result);

                runOnUiThread(() -> ToastUtil.showAtOnce(MainActivity.this, result));

            }
        });
    }

    private void doGet() {
        //1.获取OkHttpClient对象
        OkHttpClient okHttpClient = new OkHttpClient();
        //2.获取Request对象
        Request request = new Request.Builder().get().url(Cons.BASE_URL + "swords/find/21").build();
        //3.将Request封装为Call对象
        Call call = okHttpClient.newCall(request);
        //4.执行Call
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e(TAG, "onFailure: " + e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result = response.body().string();
                Log.e(TAG, "onResponse: " + result);

                runOnUiThread(() -> ToastUtil.showAtOnce(MainActivity.this, result));

            }
        });
    }
}
