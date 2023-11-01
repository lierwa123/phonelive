package com.yunbao.live.dialog;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

import com.yunbao.common.CommonAppConfig;
import com.yunbao.common.Constants;
import com.yunbao.common.dialog.AbsDialogFragment;
import com.yunbao.common.utils.DpUtil;
import com.yunbao.common.utils.MD5Util;
import com.yunbao.common.utils.ToastUtil;
import com.yunbao.common.utils.WordUtil;
import com.yunbao.live.R;

public class LiveRoomCheckDialogFragment extends AbsDialogFragment implements View.OnClickListener {

    private int mLiveType;
    private String mLiveTypeVal;
    private EditText mEditText;
    private ActionListener mActionListener;

    @Override
    protected int getLayoutId() {
        if (mLiveType == Constants.LIVE_TYPE_PWD) {
            return R.layout.dialog_live_room_pwd;
        }
        return R.layout.dialog_live_room_pay;
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
        params.width = DpUtil.dp2px(250);
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        params.gravity = Gravity.CENTER;
        window.setAttributes(params);
    }


    public void setLiveType(int liveType, String liveTypeVal) {
        mLiveType = liveType;
        mLiveTypeVal = liveTypeVal;
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
        findViewById(R.id.btn_close).setOnClickListener(this);
        findViewById(R.id.btn_next).setOnClickListener(this);
        findViewById(R.id.btn_confirm).setOnClickListener(this);
        TextView tip = findViewById(R.id.tip);
        if (mLiveType == Constants.LIVE_TYPE_PWD) {
            mEditText = findViewById(R.id.edit);
            tip.setText(WordUtil.getString(R.string.live_room_type_pwd));
        } else {
            String coinName = CommonAppConfig.getInstance().getCoinName();
            if (mLiveType == Constants.LIVE_TYPE_PAY) {
                tip.setText(String.format(WordUtil.getString(R.string.live_room_type_pay), mLiveTypeVal, coinName));
            } else if (mLiveType == Constants.LIVE_TYPE_TIME) {
                tip.setText(String.format(WordUtil.getString(R.string.live_room_type_time), mLiveTypeVal, coinName));
            }
        }

    }

    @Override
    public void onClick(View v) {
        if (mActionListener == null) {
            return;
        }
        int i = v.getId();
        if (i == R.id.btn_close) {
            dismiss();
            mActionListener.onCloseClick();
        } else if (i == R.id.btn_next) {
            dismiss();
            mActionListener.onNextClick();
        } else if (i == R.id.btn_confirm) {
            if (mLiveType == Constants.LIVE_TYPE_PWD) {
                if (mEditText != null) {
                    String content = mEditText.getText().toString().trim();
                    if (TextUtils.isEmpty(content)) {
                        ToastUtil.show(WordUtil.getString(R.string.live_input_password));
                        return;
                    }
                    String password = MD5Util.getMD5(content);
                    if (!TextUtils.isEmpty(password) && password.equalsIgnoreCase(mLiveTypeVal)) {
                        dismiss();
                        mActionListener.onConfirmClick();
                    } else {
                        ToastUtil.show(WordUtil.getString(R.string.live_password_error));
                    }
                }
            } else {
                dismiss();
                mActionListener.onConfirmClick();
            }
        }

    }

    public void setActionListener(ActionListener actionListener) {
        mActionListener = actionListener;
    }


    public interface ActionListener {
        void onCloseClick();

        void onConfirmClick();

        void onNextClick();
    }


}
