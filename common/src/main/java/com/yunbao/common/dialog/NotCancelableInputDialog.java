package com.yunbao.common.dialog;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

import com.yunbao.common.R;
import com.yunbao.common.utils.DpUtil;

/**
 * Created by cxf on 2019/5/29.
 */

public class NotCancelableInputDialog extends AbsDialogFragment implements View.OnClickListener {

    private ActionListener mActionListener;
    private String mTitle;
    private String mConfirmString;
    private EditText mEditText;

    @Override
    protected int getLayoutId() {
        return R.layout.dialog_input_2;
    }

    @Override
    protected int getDialogStyle() {
        return R.style.dialog;
    }

    @Override
    protected boolean canCancel() {
        return false;
    }

    @Override
    protected void setWindowAttributes(Window window) {
        WindowManager.LayoutParams params = window.getAttributes();
        params.width = DpUtil.dp2px(280);
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        params.gravity = Gravity.CENTER;
        window.setAttributes(params);
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Dialog dialog = getDialog();
        if (dialog != null) {
            dialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
                @Override
                public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                    if (keyCode == KeyEvent.KEYCODE_BACK) {
                        return true;
                    }
                    return false;
                }
            });
        }
        if (!TextUtils.isEmpty(mTitle)) {
            TextView tvTitle = mRootView.findViewById(R.id.title);
            tvTitle.setText(mTitle);
        }
        mEditText = mRootView.findViewById(R.id.content);
        TextView btnConfirm = mRootView.findViewById(R.id.btn_confirm);
        if (!TextUtils.isEmpty(mConfirmString)) {
            btnConfirm.setText(mConfirmString);
        }
        btnConfirm.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        if (mEditText != null) {
            String content = mEditText.getText().toString();
            if (mActionListener != null) {
                mActionListener.onConfirmClick(content, this);
            }
        }
    }

    public void setActionListener(ActionListener actionListener) {
        mActionListener = actionListener;
    }


    public void setTitle(String title) {
        mTitle = title;
    }

    public void setConfirmString(String confirmString) {
        mConfirmString = confirmString;
    }

    public interface ActionListener {
        void onConfirmClick(String content, DialogFragment dialog);
    }

    @Override
    public void onDestroy() {
        mActionListener = null;
        super.onDestroy();
    }
}
