package com.yunbao.mall.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.yunbao.common.CommonAppConfig;
import com.yunbao.common.Constants;
import com.yunbao.common.HtmlConfig;
import com.yunbao.common.activity.AbsActivity;
import com.yunbao.common.activity.WebViewActivity;
import com.yunbao.common.glide.ImgLoader;
import com.yunbao.common.http.HttpCallback;
import com.yunbao.common.utils.DialogUitl;
import com.yunbao.common.utils.StringUtil;
import com.yunbao.common.utils.ToastUtil;
import com.yunbao.common.utils.WordUtil;
import com.yunbao.im.activity.ChatRoomActivity;
import com.yunbao.im.bean.ImUserBean;
import com.yunbao.im.http.ImHttpConsts;
import com.yunbao.im.http.ImHttpUtil;
import com.yunbao.mall.R;
import com.yunbao.mall.http.MallHttpConsts;
import com.yunbao.mall.http.MallHttpUtil;

/**
 * 卖家 退款详情
 */
public class SellerRefundDetailActivity extends AbsActivity implements View.OnClickListener {

    public static void forward(Context context, String orderId) {
        Intent intent = new Intent(context, SellerRefundDetailActivity.class);
        intent.putExtra(Constants.MALL_ORDER_ID, orderId);
        context.startActivity(intent);
    }

    private String mOrderId;
    private TextView mStatusName;
    private TextView mStatusTime;
    private TextView mStatusTip;
    private View mGroupBtn;
    private ImageView mGoodsThumb;
    private TextView mGoodsName;
    private TextView mGoodsPrice;
    private TextView mGoodsSpecName;
    private TextView mGoodsNum;
    private TextView mBuyerName;
    private TextView mOrderStatusName;
    private TextView mRefundType;
    private TextView mMoney;
    private TextView mReason;
    private TextView mApplyTime;
    private TextView mOrderNo;
    private TextView mProblem;
    private String mMoneySymbol;
    private JSONObject mOrderInfo;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_seller_refund_detail;
    }

    @Override
    protected void main() {
        setTitle(WordUtil.getString(R.string.mall_202));
        mOrderId = getIntent().getStringExtra(Constants.MALL_ORDER_ID);
        mMoneySymbol = WordUtil.getString(R.string.money_symbol);
        mStatusName = findViewById(R.id.status_name);
        mStatusTime = findViewById(R.id.status_time);
        mStatusTip = findViewById(R.id.status_tip);
        mGroupBtn = findViewById(R.id.group_btn);
        mGoodsThumb = findViewById(R.id.goods_thumb);
        mGoodsName = findViewById(R.id.goods_name);
        mGoodsPrice = findViewById(R.id.goods_price);
        mGoodsSpecName = findViewById(R.id.goods_spec_name);
        mGoodsNum = findViewById(R.id.goods_num);
        mBuyerName = findViewById(R.id.buyer_name);
        mOrderStatusName = findViewById(R.id.order_status_name);
        mRefundType = findViewById(R.id.refund_type);
        mMoney = findViewById(R.id.money);
        mReason = findViewById(R.id.reason);
        mApplyTime = findViewById(R.id.apply_time);
        mOrderNo = findViewById(R.id.order_no);
        mProblem = findViewById(R.id.problem);

        findViewById(R.id.btn_reject).setOnClickListener(this);
        findViewById(R.id.btn_agree).setOnClickListener(this);
        findViewById(R.id.btn_kefu).setOnClickListener(this);
        findViewById(R.id.btn_call_phone).setOnClickListener(this);
        findViewById(R.id.btn_talk_history).setOnClickListener(this);


        MallHttpUtil.getSellerRefundDetail(mOrderId, new HttpCallback() {
            @Override
            public void onSuccess(int code, String msg, String[] info) {
                if (code == 0 && info.length > 0) {
                    JSONObject obj = JSON.parseObject(info[0]);
                    JSONObject refundInfo = obj.getJSONObject("refund_info");
                    JSONObject orderInfo = obj.getJSONObject("order_info");
                    mOrderInfo = orderInfo;
                    if (mStatusName != null) {
                        mStatusName.setText(refundInfo.getString("status_name"));
                    }
                    if (mStatusTime != null) {
                        mStatusTime.setText(refundInfo.getString("status_time"));
                    }
                    int refundStatus = refundInfo.getIntValue("status");
                    int canHandle = refundInfo.getIntValue("can_handle");
                    if (refundStatus == 0 && canHandle == 1) {
                        if (mGroupBtn != null) {
                            mGroupBtn.setVisibility(View.VISIBLE);
                        }
                    } else {
                        if (mStatusTip != null) {
                            if (mStatusTip.getVisibility() != View.VISIBLE) {
                                mStatusTip.setVisibility(View.VISIBLE);
                            }
                            mStatusTip.setText(refundInfo.getString("status_desc"));
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
                    if (mBuyerName != null) {
                        mBuyerName.setText(orderInfo.getString("username"));
                    }
                    if (mOrderStatusName != null) {
                        mOrderStatusName.setText(orderInfo.getString("status_name"));
                    }
                    if (mRefundType != null) {
                        mRefundType.setText(refundInfo.getIntValue("type") == 0 ? R.string.mall_255 : R.string.mall_256);
                    }
                    if (mMoney != null) {
                        mMoney.setText(StringUtil.contact(mMoneySymbol, orderInfo.getString("price")));
                    }
                    if (mReason != null) {
                        mReason.setText(refundInfo.getString("reason"));
                    }
                    if (mApplyTime != null) {
                        mApplyTime.setText(refundInfo.getString("addtime"));
                    }
                    if (mOrderNo != null) {
                        mOrderNo.setText(orderInfo.getString("orderno"));
                    }
                    if (mProblem != null) {
                        mProblem.setText(refundInfo.getString("content"));
                    }

                }
            }
        });
    }


    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.btn_reject) {
            reject();
        } else if (id == R.id.btn_agree) {
            agree();
        } else if (id == R.id.btn_kefu) {
            forwardChat();
        } else if (id == R.id.btn_call_phone) {
            callPhone();
        } else if (id == R.id.btn_talk_history) {
            talkHistory();
        }
    }

    /**
     * 协商历史
     */
    private void talkHistory() {
        String url = StringUtil.contact(HtmlConfig.MALL_REFUND_HISTORY, "orderid=", mOrderId, "&user_type=seller");
        WebViewActivity.forward(mContext, url);
    }

    /**
     * 同意退款
     */
    private void agree() {
        MallHttpUtil.sellerSetRefund(mOrderId, 1, null, null, new HttpCallback() {
            @Override
            public void onSuccess(int code, String msg, String[] info) {
                if (code == 0) {
                    finish();
                }
                ToastUtil.show(msg);
            }
        });
    }

    /**
     * 拒绝退款
     */
    private void reject() {
        new DialogUitl.Builder(mContext)
                .setIsHideTitle(true)
                .setConfirmColor(ContextCompat.getColor(mContext, R.color.gray3))
                .setCancelColor(ContextCompat.getColor(mContext, R.color.global))
                .setContent(WordUtil.getString(R.string.mall_280))
                .setCancelable(true)
                .setBackgroundDimEnabled(true)
                .setConfrimString(WordUtil.getString(R.string.mall_285))
                .setClickCallback(new DialogUitl.SimpleCallback() {
                    @Override
                    public void onConfirmClick(Dialog dialog, String content) {
                        SellerRejectRefundActivity.forward(mContext, mOrderId);
                    }
                })
                .build()
                .show();
    }


    /**
     * 拨打电话
     */
    private void callPhone() {
        if (TextUtils.isEmpty(mOrderId) || mOrderInfo == null) {
            return;
        }
        Intent intent = new Intent(Intent.ACTION_DIAL);
        Uri data = Uri.parse(StringUtil.contact("tel:", mOrderInfo.getString("phone")));
        intent.setData(data);
        startActivity(intent);
    }

    /**
     * 客服 私信聊天
     */
    private void forwardChat() {
        if (TextUtils.isEmpty(mOrderId) || mOrderInfo == null) {
            return;
        }
        ImHttpUtil.getImUserInfo(mOrderInfo.getString("uid"), new HttpCallback() {
            @Override
            public void onSuccess(int code, String msg, String[] info) {
                if (code == 0 && info.length > 0) {
                    ImUserBean bean = JSON.parseObject(info[0], ImUserBean.class);
                    if (bean != null) {
                        ChatRoomActivity.forward(mContext, bean, bean.getAttent() == 1, false);
                    }
                }
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            finish();
        }
    }

    @Override
    protected void onDestroy() {
        MallHttpUtil.cancel(MallHttpConsts.GET_SELLER_REFUND_DETAIL);
        MallHttpUtil.cancel(MallHttpConsts.SELLER_SET_REFUND);
        MallHttpUtil.cancel(ImHttpConsts.GET_IM_USER_INFO);
        super.onDestroy();
    }

}
