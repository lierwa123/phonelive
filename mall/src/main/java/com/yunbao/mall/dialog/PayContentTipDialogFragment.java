package com.yunbao.mall.dialog;

import android.graphics.Color;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.TextView;

import com.yunbao.common.HtmlConfig;
import com.yunbao.common.activity.WebViewActivity;
import com.yunbao.common.dialog.AbsDialogFragment;
import com.yunbao.common.http.HttpCallback;
import com.yunbao.common.utils.DpUtil;
import com.yunbao.common.utils.ToastUtil;
import com.yunbao.mall.R;
import com.yunbao.mall.http.MallHttpUtil;

public class PayContentTipDialogFragment extends AbsDialogFragment implements View.OnClickListener {

    private String mDes;
    private String mTitle;
    private CheckBox mCheckBox;

    @Override
    protected int getLayoutId() {
        return R.layout.dialog_pay_content_tip;
    }

    @Override
    protected int getDialogStyle() {
        return R.style.dialog;
    }

    @Override
    protected boolean canCancel() {
        return true;
    }

    @Override
    protected void setWindowAttributes(Window window) {
        WindowManager.LayoutParams params = window.getAttributes();
        params.width = DpUtil.dp2px(270);
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        params.gravity = Gravity.CENTER;
        window.setAttributes(params);
    }

    public void setDes(String des, String title) {
        mDes = des;
        mTitle = title;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mCheckBox = findViewById(R.id.checkbox);
        findViewById(R.id.btn_apply).setOnClickListener(this);

        TextView contentTextView = findViewById(R.id.content);
        contentTextView.setText(mDes);
        contentTextView.setMovementMethod(LinkMovementMethod.getInstance());
        SpannableString span = new SpannableString(mTitle);
        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                if (widget instanceof TextView) {
                    ((TextView) widget).setHighlightColor(Color.TRANSPARENT);
                }
                WebViewActivity.forward(mContext, HtmlConfig.MALL_PAY_APPLY_TIP);
            }

            @Override
            public void updateDrawState(TextPaint ds) {
                ds.setUnderlineText(false);
            }
        };
        int titleLength = mTitle.length();
        span.setSpan(clickableSpan, 0, titleLength, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        span.setSpan(new ForegroundColorSpan(0xffff5878), 0, titleLength, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        contentTextView.append(span);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.btn_apply) {
            apply();
        }
    }

    private void apply() {
        if (!mCheckBox.isChecked()) {
            ToastUtil.show(R.string.mall_386);
            return;
        }
        MallHttpUtil.applyPayOpen(new HttpCallback() {
            @Override
            public void onSuccess(int code, String msg, String[] info) {
                if (code == 0) {
                    dismiss();
                }
                ToastUtil.show(msg);
            }
        });
    }
}
