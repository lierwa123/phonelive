package com.yunbao.main.activity;

import android.content.Intent;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.yunbao.common.CommonAppConfig;
import com.yunbao.common.Constants;
import com.yunbao.common.activity.AbsActivity;
import com.yunbao.common.bean.UserBean;
import com.yunbao.common.http.HttpCallback;
import com.yunbao.common.utils.ToastUtil;
import com.yunbao.common.utils.WordUtil;
import com.yunbao.main.R;
import com.yunbao.main.http.MainHttpConsts;
import com.yunbao.main.http.MainHttpUtil;

/**
 * Created by cxf on 2018/9/29.
 * 设置签名
 */

public class EditSignActivity extends AbsActivity implements View.OnClickListener {

    private EditText mEditText;
    private View mBtnClear;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_edit_sign;
    }

    @Override
    protected void main() {
        setTitle(WordUtil.getString(R.string.edit_profile_update_sign));
        mEditText = (EditText) findViewById(R.id.edit);
        mEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (mBtnClear != null) {
                    if (TextUtils.isEmpty(s)) {
                        if (mBtnClear.getVisibility() == View.VISIBLE) {
                            mBtnClear.setVisibility(View.INVISIBLE);
                        }
                    } else {
                        if (mBtnClear.getVisibility() != View.VISIBLE) {
                            mBtnClear.setVisibility(View.VISIBLE);
                        }
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        mBtnClear = findViewById(R.id.btn_clear);
        mBtnClear.setOnClickListener(this);
        findViewById(R.id.btn_save).setOnClickListener(this);
        String content = getIntent().getStringExtra(Constants.SIGN);
        if (!TextUtils.isEmpty(content)) {
            mEditText.setText(content);
            mEditText.setSelection(content.length());
        }
    }


    private void clear() {
        if (mEditText != null) {
            mEditText.setText(null);
        }
    }

    private void save() {
        final String content = mEditText.getText().toString().trim();
        if (TextUtils.isEmpty(content)) {
            ToastUtil.show(R.string.edit_profile_sign_empty);
            return;
        }
        if(content.length()>20){
            ToastUtil.show(R.string.edit_profile_length_font);
            return;
        }
        MainHttpUtil.updateFields("{\"signature\":\"" + content + "\"}", new HttpCallback() {
            @Override
            public void onSuccess(int code, String msg, String[] info) {
                if (code == 0) {
                    if (info.length > 0) {
                        JSONObject obj = JSON.parseObject(info[0]);
                        ToastUtil.show(obj.getString("msg"));
                        UserBean u = CommonAppConfig.getInstance().getUserBean();
                        if (u != null) {
                            u.setSignature(content);
                        }
                        Intent intent = getIntent();
                        intent.putExtra(Constants.SIGN, content);
                        setResult(RESULT_OK, intent);
                        finish();
                    }
                } else {
                    ToastUtil.show(msg);
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        if (!canClick()) {
            return;
        }
        int id = v.getId();
        if (id == R.id.btn_clear) {
            clear();
        } else if (id == R.id.btn_save) {
            save();
        }
    }


    @Override
    protected void onDestroy() {
        MainHttpUtil.cancel(MainHttpConsts.UPDATE_FIELDS);
        super.onDestroy();
    }
}
