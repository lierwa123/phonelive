package com.yunbao.main.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.umeng.analytics.MobclickAgent;
import com.yunbao.common.CommonAppConfig;
import com.yunbao.common.Constants;
import com.yunbao.common.activity.AbsActivity;
import com.yunbao.common.http.CommonHttpUtil;
import com.yunbao.common.http.HttpCallback;
import com.yunbao.common.utils.DialogUitl;
import com.yunbao.common.utils.L;
import com.yunbao.common.utils.ToastUtil;
import com.yunbao.common.utils.WordUtil;
import com.yunbao.im.utils.ImMessageUtil;
import com.yunbao.im.utils.ImPushUtil;
import com.yunbao.main.R;
import com.yunbao.main.http.MainHttpConsts;
import com.yunbao.main.http.MainHttpUtil;

/**
 * 注销账号
 */
public class CancelAccountActivity extends AbsActivity implements View.OnClickListener {

    public static void forward(Context context, String url) {
        Intent intent = new Intent(context, CancelAccountActivity.class);
        intent.putExtra(Constants.URL, url);
        context.startActivity(intent);
    }

    private ProgressBar mProgressBar;
    private WebView mWebView;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_cancel_account;
    }

    @Override
    protected void main() {
        setTitle(WordUtil.getString(R.string.cancel_account_0));
        findViewById(R.id.btn_confirm).setOnClickListener(this);
        findViewById(R.id.btn_cancel).setOnClickListener(this);
        String url = getIntent().getStringExtra(Constants.URL);
        L.e("H5--->" + url);
        mProgressBar = findViewById(com.yunbao.common.R.id.progressbar);
        mWebView = new WebView(mContext);
        mWebView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        mWebView.setOverScrollMode(View.OVER_SCROLL_NEVER);
        ViewGroup container = findViewById(R.id.container);
        container.addView(mWebView);
        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });
        mWebView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                if (newProgress == 100) {
                    mProgressBar.setVisibility(View.GONE);
                } else {
                    mProgressBar.setProgress(newProgress);
                }
            }
        });
        mWebView.getSettings().setJavaScriptEnabled(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mWebView.getSettings().setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }
        mWebView.loadUrl(url);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.btn_confirm) {
            cancelAccount();
        } else if (id == R.id.btn_cancel) {
            finish();
        }
    }

    private void cancelAccount() {
        new DialogUitl.Builder(mContext)
                .setContent(WordUtil.getString(R.string.cancel_account_2))
                .setBackgroundDimEnabled(true)
                .setCancelable(true)
                .setClickCallback(new DialogUitl.SimpleCallback() {
                    @Override
                    public void onConfirmClick(Dialog dialog, String content) {
                        MainHttpUtil.cancelAccount(new HttpCallback() {
                            @Override
                            public void onSuccess(int code, String msg, String[] info) {
                                if (code == 0) {
                                    finish();
                                    CommonHttpUtil.updatePushId("");
                                    CommonAppConfig.getInstance().clearLoginInfo();
                                    //退出极光
                                    ImMessageUtil.getInstance().logoutImClient();
                                    ImPushUtil.getInstance().logout();
                                    //友盟统计登出
                                    MobclickAgent.onProfileSignOff();
                                    LoginActivity.forward();
                                }
                                ToastUtil.show(msg);
                            }
                        });
                    }
                })
                .build()
                .show();


    }


    @Override
    protected void onDestroy() {
        MainHttpUtil.cancel(MainHttpConsts.CANCEL_ACCOUNT);
        if (mWebView != null) {
            ViewGroup parent = (ViewGroup) mWebView.getParent();
            if (parent != null) {
                parent.removeView(mWebView);
            }
            mWebView.destroy();
        }
        super.onDestroy();
    }
}
