package com.yunbao.mall.dialog;

import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.yunbao.common.dialog.AbsDialogFragment;
import com.yunbao.common.utils.DpUtil;
import com.yunbao.mall.R;

public class PayContentResultDialogFragment extends AbsDialogFragment implements View.OnClickListener {

    private int mStatus;
    private String mDes;
    private String mTitle;

    @Override
    protected int getLayoutId() {
        return R.layout.dialog_pay_content_result;
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

    public void setResult(int status, String des, String title) {
        mStatus = status;
        mDes = des;
        mTitle = title;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        findViewById(R.id.btn_close).setOnClickListener(this);
        ImageView img = findViewById(R.id.img);
        TextView titleTextView = findViewById(R.id.title);
        TextView desTextView = findViewById(R.id.des);
        img.setImageResource(mStatus == 0 ? R.mipmap.pay_01 : R.mipmap.pay_02);
        titleTextView.setText(mTitle);
        desTextView.setText(mDes);
    }

    @Override
    public void onClick(View v) {
        dismiss();
    }

}
