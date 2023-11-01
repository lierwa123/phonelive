package com.yunbao.mall.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
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
 * 买家 退款详情
 */
public class BuyerRefundDetailActivity extends AbsActivity implements View.OnClickListener {

    public static void forward(Context context, String orderId) {
        Intent intent = new Intent(context, BuyerRefundDetailActivity.class);
        intent.putExtra(Constants.MALL_ORDER_ID, orderId);
        context.startActivity(intent);
    }

    private String mOrderId;
    private TextView mStatusName;
    private TextView mStatusTime;
    private TextView mStatusTip;
    private View mGroupBtn;
    private View mBtnOffical;
    private View mBtnRefundAgain;
    private ImageView mGoodsThumb;
    private TextView mGoodsName;
    private TextView mGoodsPrice;
    private TextView mGoodsSpecName;
    private TextView mGoodsNum;
    private TextView mRefundType;
    private TextView mMoney;
    private TextView mReason;
    private TextView mApplyTime;
    private TextView mProblem;
    private TextView mRejectReason;
    private TextView mRejectDesc;
    private View mGroupRejectReason;
    private View mGroupRejectDesc;
    private String mMoneySymbol;
    private JSONObject mOrderInfo;
    private JSONObject mShopInfo;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_buyer_refund_detail;
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
        mBtnOffical = findViewById(R.id.btn_offical);
        mBtnRefundAgain = findViewById(R.id.btn_apply_again);
        mGoodsThumb = findViewById(R.id.goods_thumb);
        mGoodsName = findViewById(R.id.goods_name);
        mGoodsPrice = findViewById(R.id.goods_price);
        mGoodsSpecName = findViewById(R.id.goods_spec_name);
        mGoodsNum = findViewById(R.id.goods_num);
        mRefundType = findViewById(R.id.refund_type);
        mMoney = findViewById(R.id.money);
        mReason = findViewById(R.id.reason);
        mApplyTime = findViewById(R.id.apply_time);
        mProblem = findViewById(R.id.problem);
        mRejectReason = findViewById(R.id.reject_reason);
        mRejectDesc = findViewById(R.id.reject_desc);
        mGroupRejectReason = findViewById(R.id.group_reject_reason);
        mGroupRejectDesc = findViewById(R.id.group_reject_desc);

        mBtnOffical.setOnClickListener(this);
        mBtnRefundAgain.setOnClickListener(this);
        findViewById(R.id.btn_cancel_apply).setOnClickListener(this);
        findViewById(R.id.btn_kefu).setOnClickListener(this);
        findViewById(R.id.btn_call_phone).setOnClickListener(this);
        findViewById(R.id.btn_talk_history).setOnClickListener(this);

        getData();
    }

    private void getData() {

        MallHttpUtil.getBuyerRefundDetail(mOrderId, new HttpCallback() {
            @Override
            public void onSuccess(int code, String msg, String[] info) {
                if (code == 0 && info.length > 0) {
                    JSONObject obj = JSON.parseObject(info[0]);
                    JSONObject refundInfo = obj.getJSONObject("refund_info");
                    JSONObject orderInfo = obj.getJSONObject("order_info");
                    mOrderInfo = orderInfo;
                    mShopInfo = obj.getJSONObject("shop_info");
                    if (mStatusName != null) {
                        mStatusName.setText(refundInfo.getString("status_name"));
                    }
                    if (mStatusTime != null) {
                        mStatusTime.setText(refundInfo.getString("status_time"));
                    }
                    int refundStatus = refundInfo.getIntValue("status");
                    if (refundStatus == 0) {
                        if (mGroupBtn != null && mGroupBtn.getVisibility() != View.VISIBLE) {
                            mGroupBtn.setVisibility(View.VISIBLE);
                        }
                        if (mStatusTip != null && mStatusTip.getVisibility() != View.GONE) {
                            mStatusTip.setVisibility(View.GONE);
                        }
                        if (refundInfo.getIntValue("is_platform") == 1) {
                            if (mBtnOffical != null && mBtnOffical.getVisibility() != View.VISIBLE) {
                                mBtnOffical.setVisibility(View.VISIBLE);
                            }
                        } else {
                            if (mBtnOffical != null && mBtnOffical.getVisibility() != View.GONE) {
                                mBtnOffical.setVisibility(View.GONE);
                            }
                        }
                        if (refundInfo.getIntValue("is_reapply") == 1) {
                            if (mBtnRefundAgain != null && mBtnRefundAgain.getVisibility() != View.VISIBLE) {
                                mBtnRefundAgain.setVisibility(View.VISIBLE);
                            }
                        } else {
                            if (mBtnRefundAgain != null && mBtnRefundAgain.getVisibility() != View.GONE) {
                                mBtnRefundAgain.setVisibility(View.GONE);
                            }
                        }
                    } else {
                        if (mGroupBtn != null && mGroupBtn.getVisibility() != View.GONE) {
                            mGroupBtn.setVisibility(View.GONE);
                        }
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
                    if (mProblem != null) {
                        mProblem.setText(refundInfo.getString("content"));
                    }
                    if (refundInfo.getIntValue("shop_result") == -1 && refundInfo.getIntValue("is_platform_interpose") == 0) {
                        if (mGroupRejectReason != null) {
                            if (mGroupRejectReason.getVisibility() != View.VISIBLE) {
                                mGroupRejectReason.setVisibility(View.VISIBLE);
                            }
                            mRejectReason.setText(refundInfo.getString("shop_refuse_reason"));
                        }
                        if (mGroupRejectDesc != null) {
                            if (mGroupRejectDesc.getVisibility() != View.VISIBLE) {
                                mGroupRejectDesc.setVisibility(View.VISIBLE);
                            }
                            mRejectDesc.setText(refundInfo.getString("shop_handle_desc"));
                        }
                    } else {
                        if (mGroupRejectReason != null && mGroupRejectReason.getVisibility() != View.GONE) {
                            mGroupRejectReason.setVisibility(View.GONE);
                        }
                        if (mGroupRejectDesc != null && mGroupRejectDesc.getVisibility() != View.GONE) {
                            mGroupRejectDesc.setVisibility(View.GONE);
                        }
                    }
                }
            }
        });
    }


    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.btn_offical) {
            BuyerRefundOfficialActivity.forward(mContext, mOrderId);
        } else if (id == R.id.btn_apply_again) {
            refundAgain();
        } else if (id == R.id.btn_cancel_apply) {
            cancelRefund();
        } else if (id == R.id.btn_kefu) {
            if (Constants.MALL_PLAT_UID.equals(mOrderInfo.getString("shop_uid"))) {
                DialogUitl.showSimpleTipDialog(mContext, null, true, WordUtil.getString(R.string.mall_412), true);
            } else {
                forwardChat();
            }
        } else if (id == R.id.btn_call_phone) {
            callPhone();
        } else if (id == R.id.btn_talk_history) {
            talkHistory();
        }
    }


    private void refundAgain() {
        MallHttpUtil.buyerApplyRefundAgain(mOrderId, new HttpCallback() {
            @Override
            public void onSuccess(int code, String msg, String[] info) {
                if (code == 0) {
                    getData();
                }
                ToastUtil.show(msg);
            }
        });
    }

    /**
     * 协商历史
     */
    private void talkHistory() {
        String url = StringUtil.contact(HtmlConfig.MALL_REFUND_HISTORY, "orderid=", mOrderId, "&user_type=buyer");
        WebViewActivity.forward(mContext, url);
    }

    /**
     * 取消退款
     */
    private void cancelRefund() {
        new DialogUitl.Builder(mContext)
                .setContent(WordUtil.getString(R.string.mall_276))
                .setCancelable(true)
                .setBackgroundDimEnabled(true)
                .setClickCallback(new DialogUitl.SimpleCallback() {
                    @Override
                    public void onConfirmClick(Dialog dialog, String content) {
                        MallHttpUtil.buyerCancelRefund(mOrderId, new HttpCallback() {
                            @Override
                            public void onSuccess(int code, String msg, String[] info) {
                                if (code == 0) {
                                    finish();
                                }
                                ToastUtil.show(msg);
                            }
                        });
                    }
                })
                .build()
                .show();

    }


    /**
     * 拨打电话
     */
    private void callPhone() {
        if (TextUtils.isEmpty(mOrderId) || mShopInfo == null) {
            return;
        }
        Intent intent = new Intent(Intent.ACTION_DIAL);
        Uri data = Uri.parse(StringUtil.contact("tel:", mShopInfo.getString("service_phone")));
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
        ImHttpUtil.getImUserInfo(mOrderInfo.getString("shop_uid"), new HttpCallback() {
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
            getData();
        }
    }

    @Override
    protected void onDestroy() {
        MallHttpUtil.cancel(MallHttpConsts.GET_BUYER_REFUND_DETAIL);
        MallHttpUtil.cancel(MallHttpConsts.BUYER_CANCEL_REFUND);
        MallHttpUtil.cancel(MallHttpConsts.BUYER_APPLY_REFUND_AGAIN);
        MallHttpUtil.cancel(ImHttpConsts.GET_IM_USER_INFO);
        super.onDestroy();
    }

}
