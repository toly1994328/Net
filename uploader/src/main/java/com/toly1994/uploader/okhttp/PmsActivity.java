package com.toly1994.uploader.okhttp;

import android.os.Bundle;

import top.toly.zutils.core.base.PermissionActivity;

/**
 * 作者：张风捷特烈<br/>
 * 时间：2018/10/16 0016:11:03<br/>
 * 邮箱：1981462002@qq.com<br/>
 * 说明：
 */
public class PmsActivity extends PermissionActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        applyPermissions(_WRITE_EXTERNAL_STORAGE());
    }

    @Override
    protected void permissionOk() {
        finish();
    }
}
