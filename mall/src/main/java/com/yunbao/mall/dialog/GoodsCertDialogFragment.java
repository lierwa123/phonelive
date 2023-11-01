package com.yunbao.mall.dialog;

import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.yunbao.common.dialog.AbsDialogFragment;
import com.yunbao.mall.R;

public class GoodsCertDialogFragment extends AbsDialogFragment implements View.OnClickListener {

    @Override
    protected int getLayoutId() {
        return R.layout.dialog_goods_cert;
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
        window.setWindowAnimations(R.style.bottomToTopAnim);
        WindowManager.LayoutParams params = window.getAttributes();
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        params.gravity = Gravity.BOTTOM;
        window.setAttributes(params);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        findViewById(R.id.btn_close).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        dismiss();
    }
}
