package com.yunbao.main.activity;

import android.view.View;
import android.widget.TextView;

import com.yunbao.common.CommonAppConfig;
import com.yunbao.common.Constants;
import com.yunbao.common.activity.AbsActivity;
import com.yunbao.im.utils.ImMessageUtil;
import com.yunbao.im.utils.ImPushUtil;
import com.yunbao.main.R;

/**
 * Created by cxf on 2017/10/9.
 * 登录失效的时候以dialog形式弹出的activity
 */
public class LoginInvalidActivity extends AbsActivity implements View.OnClickListener {

    @Override
    protected int getLayoutId() {
        return R.layout.activity_login_invalid;
    }

    @Override
    protected void main() {
        TextView textView = (TextView) findViewById(R.id.content);
        String tip = getIntent().getStringExtra(Constants.TIP);
        textView.setText(tip);
        findViewById(R.id.btn_confirm).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        CommonAppConfig.getInstance().clearLoginInfo();
        //退出极光
        ImMessageUtil.getInstance().logoutImClient();
        ImPushUtil.getInstance().logout();
        LoginActivity.forward();
        finish();
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
    }
}
