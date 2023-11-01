package com.yunbao.mall.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.yunbao.common.Constants;
import com.yunbao.common.activity.AbsActivity;
import com.yunbao.common.glide.ImgLoader;
import com.yunbao.common.http.HttpCallback;
import com.yunbao.common.utils.StringUtil;
import com.yunbao.common.utils.ToastUtil;
import com.yunbao.common.utils.WordUtil;
import com.yunbao.mall.R;
import com.yunbao.mall.bean.RefundReasonBean;
import com.yunbao.mall.dialog.BuyerRefundReasonDialogFragment;
import com.yunbao.mall.http.MallHttpConsts;
import com.yunbao.mall.http.MallHttpUtil;

import java.util.Arrays;
import java.util.List;

/**
 * 买家申请退款
 */
public class BuyerRefundApplyActivity extends AbsActivity implements View.OnClickListener {

//    public static void forward(Context context, String orderId) {
//        Intent intent = new Intent(context, BuyerRefundApplyActivity.class);
//        intent.putExtra(Constants.MALL_ORDER_ID, orderId);
//        ((Activity) context).startActivityForResult(intent, 0);
//    }

    private String mOrderId;
    private ImageView mGoodsThumb;
    private TextView mGoodsName;
    private TextView mGoodsPrice;
    private TextView mGoodsSpecName;
    private TextView mGoodsNum;
    private TextView mRefundReason;
    private TextView mRefundMoney;
    private EditText mEditText;
    private TextView mCount;
    private View mBtnRefundAll;//退货退款
    private String mMoneySymbol;
    private int mRefundType;//退款类型 0 仅退款 1 退货退款
    private List<RefundReasonBean> mReasonList;
    private String mReasonId;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_buyer_refund_apply;
    }

    @Override
    protected void main() {
        setTitle(WordUtil.getString(R.string.mall_262));
        mOrderId = getIntent().getStringExtra(Constants.MALL_ORDER_ID);
        mMoneySymbol = WordUtil.getString(R.string.money_symbol);
        mGoodsThumb = findViewById(R.id.goods_thumb);
        mGoodsName = findViewById(R.id.goods_name);
        mGoodsPrice = findViewById(R.id.goods_price);
        mGoodsSpecName = findViewById(R.id.goods_spec_name);
        mGoodsNum = findViewById(R.id.goods_num);
        mRefundReason = findViewById(R.id.refund_reason);
        mRefundMoney = findViewById(R.id.refund_money);
        mEditText = findViewById(R.id.edit);
        mEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (mCount != null) {
                    mCount.setText(StringUtil.contact(String.valueOf(s.length()), "/300"));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        mCount = findViewById(R.id.count);
        findViewById(R.id.btn_refund_only).setOnClickListener(this);
        mBtnRefundAll = findViewById(R.id.btn_refund_all);
        mBtnRefundAll.setOnClickListener(this);
        findViewById(R.id.btn_refund_reason).setOnClickListener(this);
        findViewById(R.id.btn_submit).setOnClickListener(this);

        MallHttpUtil.getBuyerOrderDetail(mOrderId, new HttpCallback() {

            @Override
            public void onSuccess(int code, String msg, String[] info) {
                if (code == 0 && info.length > 0) {
                    JSONObject obj = JSON.parseObject(info[0]);
                    JSONObject orderInfo = obj.getJSONObject("order_info");
                    int status = orderInfo.getIntValue("status");
                    if (status > Constants.MALL_ORDER_STATUS_WAIT_SEND) {
                        if (mBtnRefundAll != null && mBtnRefundAll.getVisibility() != View.VISIBLE) {
                            mBtnRefundAll.setVisibility(View.VISIBLE);
                        }
                    }
                    if (mGoodsThumb != null) {
                        ImgLoader.display(mContext, orderInfo.getString("spec_thumb_format"), mGoodsThumb);
                    }
                    if (mGoodsName != null) {
                        mGoodsName.setText(orderInfo.getString("goods_name"));
                    }
                    if (mGoodsPrice != null) {
                        mGoodsPrice.setText(StringUtil.contact(mMoneySymbol, orderInfo.getString("price")));
                    }
                    if (mGoodsSpecName != null) {
                        mGoodsSpecName.setText(orderInfo.getString("spec_name"));
                    }
                    if (mGoodsNum != null) {
                        mGoodsNum.setText(StringUtil.contact("x", orderInfo.getString("nums")));
                    }
                    if (mRefundMoney != null) {
                        mRefundMoney.setText(StringUtil.contact(mMoneySymbol, orderInfo.getString("total")));
                    }

                }
            }
        });
    }


    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.btn_refund_only) {
            mRefundType = 0;
        } else if (id == R.id.btn_refund_all) {
            mRefundType = 1;
        } else if (id == R.id.btn_refund_reason) {
            chooseRefundReason();
        } else if (id == R.id.btn_submit) {
            submit();
        }
    }

    /**
     * 选择退款原因
     */
    private void chooseRefundReason() {
        if (mReasonList == null) {
            MallHttpUtil.getRefundReasonList(new HttpCallback() {
                @Override
                public void onSuccess(int code, String msg, String[] info) {
                    if (code == 0) {
                        mReasonList = JSON.parseArray(Arrays.toString(info), RefundReasonBean.class);
                        showReasonDialog();
                    }
                }
            });
        } else {
            showReasonDialog();
        }
    }

    private void showReasonDialog() {
        BuyerRefundReasonDialogFragment fragment = new BuyerRefundReasonDialogFragment();
        fragment.setList(mReasonList);
        fragment.show(getSupportFragmentManager(), "BuyerRefundReasonDialogFragment");
    }


    public void setRefundReason(RefundReasonBean bean) {
        mReasonId = bean.getId();
        if (mRefundReason != null) {
            mRefundReason.setText(bean.getName());
        }
    }


    private void submit() {
        if (TextUtils.isEmpty(mReasonId)) {
            ToastUtil.show(R.string.mall_263);
            return;
        }
        String content = mEditText.getText().toString().trim();
        MallHttpUtil.buyerApplyRefund(mOrderId, content, mReasonId, mRefundType, new HttpCallback() {
            @Override
            public void onSuccess(int code, String msg, String[] info) {
                if (code == 0) {
                    setResult(RESULT_OK);
                    finish();
                }
                ToastUtil.show(msg);
            }
        });
    }


    @Override
    protected void onDestroy() {
        MallHttpUtil.cancel(MallHttpConsts.GET_BUYER_ORDER_DETAIL);
        MallHttpUtil.cancel(MallHttpConsts.GET_REFUND_REASON_LIST);
        MallHttpUtil.cancel(MallHttpConsts.BUYER_APPLY_REFUND);
        super.onDestroy();
    }

}
