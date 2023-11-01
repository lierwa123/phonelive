package com.yunbao.mall.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.View;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.yunbao.common.activity.AbsActivity;
import com.yunbao.common.http.HttpCallback;
import com.yunbao.common.utils.ToastUtil;
import com.yunbao.common.utils.WordUtil;
import com.yunbao.mall.R;
import com.yunbao.mall.adapter.SetManageClassAdapter;
import com.yunbao.mall.bean.ManageClassBean;
import com.yunbao.mall.http.MallHttpConsts;
import com.yunbao.mall.http.MallHttpUtil;

import java.util.Arrays;
import java.util.List;

public class SetManageClassActivity extends AbsActivity implements SetManageClassAdapter.ActionListener, View.OnClickListener {

    public static void forward(Context context) {
        context.startActivity(new Intent(context, SetManageClassActivity.class));
    }

    private View mGroupCheck;
    private View mGroupCheckFail;
    private RecyclerView mRecyclerView;
    private SetManageClassAdapter mAdapter;
    private TextView mBtnSubmit;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_set_manage_class;
    }

    @Override
    protected void main() {
        setTitle(WordUtil.getString(R.string.mall_399));
        mGroupCheck = findViewById(R.id.group_check);
        mGroupCheckFail = findViewById(R.id.group_check_fail);
        TextView textFail = findViewById(R.id.text_fail);
        mBtnSubmit = findViewById(R.id.btn_submit);
        mBtnSubmit.setOnClickListener(this);
        String contentFail = WordUtil.getString(R.string.mall_397);
        String submit = WordUtil.getString(R.string.mall_398);
        SpannableString spannableString = new SpannableString(contentFail);
        int startIndex = contentFail.indexOf(submit);
        if (startIndex >= 0) {
            ClickableSpan clickableSpan = new ClickableSpan() {

                @Override
                public void updateDrawState(TextPaint ds) {
                    super.updateDrawState(ds);
                    ds.setColor(0xff3399ee);
                    ds.setUnderlineText(false);
                }

                @Override
                public void onClick(View widget) {
                    if (mGroupCheckFail != null) {
                        mGroupCheckFail.setVisibility(View.INVISIBLE);
                    }
                    loadList();
                }
            };
            int endIndex = startIndex + submit.length();
            spannableString.setSpan(clickableSpan, startIndex, endIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        textFail.setText(spannableString);
        textFail.setMovementMethod(LinkMovementMethod.getInstance());//不设置 没有点击事件
        textFail.setHighlightColor(Color.TRANSPARENT); //设置点击后的颜色为透明
        mRecyclerView = findViewById(R.id.recyclerView);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));

        MallHttpUtil.checkApplyBusinessCategory(new HttpCallback() {
            @Override
            public void onSuccess(int code, String msg, String[] info) {
                if (code == 0 && info.length > 0) {
                    JSONObject obj = JSON.parseObject(info[0]);
                    int status = obj.getIntValue("status");
                    if (status == 1) {// 0（审核中） 1（审核通过和未提交过申请）   2（审核失败）
                        loadList();
                    } else {
                        if (status == 0) {
                            if (mGroupCheck != null) {
                                mGroupCheck.setVisibility(View.VISIBLE);
                            }
                        } else {
                            if (mGroupCheckFail != null) {
                                mGroupCheckFail.setVisibility(View.VISIBLE);
                            }
                        }
                    }
                }
            }
        });
    }


    private void loadList() {
        MallHttpUtil.getBusinessCategory(new HttpCallback() {
            @Override
            public void onSuccess(int code, String msg, String[] info) {
                if (code == 0 && mRecyclerView != null) {
                    List<ManageClassBean> list = JSON.parseArray(Arrays.toString(info), ManageClassBean.class);
                    mAdapter = new SetManageClassAdapter(mContext, list);
                    mAdapter.setActionListener(SetManageClassActivity.this);
                    mRecyclerView.setAdapter(mAdapter);
                }
            }
        });
    }

    @Override
    public void onCheckChanged(int checkedCount) {
        if (mBtnSubmit != null) {
            mBtnSubmit.setEnabled(checkedCount > 0);
        }
    }


    @Override
    protected void onDestroy() {
        MallHttpUtil.cancel(MallHttpConsts.CHECK_APPLY_BUSINESS_CATEGORY);
        MallHttpUtil.cancel(MallHttpConsts.GET_BUSINESS_CATEGORY);
        MallHttpUtil.cancel(MallHttpConsts.APPLY_BUSINESS_CATEGORY);
        super.onDestroy();
    }

    @Override
    public void onClick(View v) {
        if (mAdapter == null) {
            return;
        }
        String classId = mAdapter.getCheckedId();
        if (TextUtils.isEmpty(classId)) {
            return;
        }
        MallHttpUtil.applyBusinessCategory(classId, new HttpCallback() {
            @Override
            public void onSuccess(int code, String msg, String[] info) {
                if (code == 0) {
                    finish();
                }
                ToastUtil.show(msg);
            }
        });
    }
}
