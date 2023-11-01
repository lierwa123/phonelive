package com.yunbao.mall.activity;

import android.app.Dialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.yunbao.common.Constants;
import com.yunbao.common.activity.AbsActivity;
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
 * 卖家 订单详情
 */
public class SellerOrderDetailActivity extends AbsActivity {

    public static void forward(Context context, String orderId) {
        Intent intent = new Intent(context, SellerOrderDetailActivity.class);
        intent.putExtra(Constants.MALL_ORDER_ID, orderId);
        context.startActivity(intent);
    }

    private String mOrderId;
    private TextView mStatusName;
    private TextView mStatusTip;
    private TextView mWuliuStatus;
    private TextView mWuliuTip;
    private View mGroupWuliu;
    private TextView mBuyerName;
    private TextView mBuyerAddress;
    private View mGroupMsg;
    private TextView mBuyerMsg;
    private ImageView mGoodsThumb;
    private TextView mGoodsName;
    private TextView mGoodsPrice;
    private TextView mGoodsSpecName;
    private TextView mGoodsNum;
    private TextView mPostage;
    private TextView mMoney;
    private TextView mOrderNo;
    private TextView mOrderMakeTime;
    private TextView mOrderPayType;
    private TextView mOrderPayTime;
    private ViewGroup mGroupBottom;
    private String mMoneySymbol;
    private JSONObject mOrderInfo;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_seller_order_detail;
    }

    @Override
    protected void main() {
        mStatusName = findViewById(R.id.status_name);
        mStatusTip = findViewById(R.id.status_tip);
        mWuliuStatus = findViewById(R.id.wuliu_status);
        mWuliuTip = findViewById(R.id.wuliu_tip);
        mGroupWuliu = findViewById(R.id.group_wuliu);
        mBuyerName = findViewById(R.id.buyer_name);
        mBuyerAddress = findViewById(R.id.buyer_address);
        mGroupMsg = findViewById(R.id.group_msg);
        mBuyerMsg = findViewById(R.id.buyer_msg);
        mGoodsThumb = findViewById(R.id.goods_thumb);
        mGoodsName = findViewById(R.id.goods_name);
        mGoodsPrice = findViewById(R.id.goods_price);
        mGoodsSpecName = findViewById(R.id.goods_spec_name);
        mGoodsNum = findViewById(R.id.goods_num);
        mPostage = findViewById(R.id.postage);
        mMoney = findViewById(R.id.money);
        mOrderNo = findViewById(R.id.order_no);
        mOrderMakeTime = findViewById(R.id.order_make_time);
        mOrderPayType = findViewById(R.id.order_pay_type);
        mOrderPayTime = findViewById(R.id.order_pay_time);
        mGroupBottom = findViewById(R.id.group_bottom);
        mOrderId = getIntent().getStringExtra(Constants.MALL_ORDER_ID);
        mMoneySymbol = WordUtil.getString(R.string.money_symbol);

        getData();
    }


    private void getData() {
        MallHttpUtil.getSellerOrderDetail(mOrderId, new HttpCallback() {
            @Override
            public void onSuccess(int code, String msg, String[] info) {
                if (code == 0 && info.length > 0) {
                    JSONObject obj = JSON.parseObject(info[0]);
                    JSONObject orderInfo = obj.getJSONObject("order_info");
                    mOrderInfo = orderInfo;
                    int orderStatus = orderInfo.getIntValue("status");
                    if (mStatusName != null) {
                        mStatusName.setText(orderInfo.getString("status_name"));
                    }
                    if (mStatusTip != null) {
                        mStatusTip.setText(orderInfo.getString("status_desc"));
                    }
                    String wuliuString = obj.getString("express_info");
                    if (orderStatus > 1 && !"[]".equals(wuliuString)) {
                        JSONObject wuliuInfo = JSON.parseObject(wuliuString);
                        if (mGroupWuliu != null) {
                            mGroupWuliu.setVisibility(View.VISIBLE);
                        }
                        if (mWuliuStatus != null) {
                            mWuliuStatus.setText(wuliuInfo.getString("state_name"));
                        }
                        if (mWuliuTip != null) {
                            mWuliuTip.setText(wuliuInfo.getString("desc"));
                        }
                    } else {
                        if (mGroupWuliu != null && mGroupWuliu.getVisibility() != View.GONE) {
                            mGroupWuliu.setVisibility(View.GONE);
                        }
                    }
                    if (mBuyerName != null) {
                        mBuyerName.setText(StringUtil.contact(orderInfo.getString("username"), " ", orderInfo.getString("phone")));
                    }
                    if (mBuyerAddress != null) {
                        mBuyerAddress.setText(orderInfo.getString("address_format"));
                    }
                    if (orderStatus > 0) {
                        String msgContent = orderInfo.getString("message");
                        if (TextUtils.isEmpty(msgContent)) {
                            if (mGroupMsg != null && mGroupMsg.getVisibility() != View.GONE) {
                                mGroupMsg.setVisibility(View.GONE);
                            }
                        } else {
                            if (mGroupMsg != null && mGroupMsg.getVisibility() != View.VISIBLE) {
                                mGroupMsg.setVisibility(View.VISIBLE);
                            }
                            if (mBuyerMsg != null) {
                                mBuyerMsg.setText(msgContent);
                            }
                        }
                    } else {
                        if (mGroupMsg != null && mGroupMsg.getVisibility() != View.GONE) {
                            mGroupMsg.setVisibility(View.GONE);
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
                    if (mPostage != null) {
                        mPostage.setText(StringUtil.contact(mMoneySymbol, orderInfo.getString("postage")));
                    }
                    if (mMoney != null) {
                        mMoney.setText(StringUtil.contact(mMoneySymbol, orderInfo.getString("total")));
                    }
                    String orderNo = orderInfo.getString("orderno");
                    if (mOrderNo != null) {
                        mOrderNo.setText(String.format(WordUtil.getString(R.string.mall_232), orderNo));
                    }
                    if (mOrderMakeTime != null) {
                        mOrderMakeTime.setText(String.format(WordUtil.getString(R.string.mall_250), orderInfo.getString("addtime")));
                    }
                    if (orderStatus > 0) {
                        if (mOrderPayType != null) {
                            if (mOrderPayType.getVisibility() != View.VISIBLE) {
                                mOrderPayType.setVisibility(View.VISIBLE);
                            }
                            mOrderPayType.setText(String.format(WordUtil.getString(R.string.mall_251), orderInfo.getString("type_name")));
                        }
                        if (mOrderPayTime != null) {
                            if (mOrderPayTime.getVisibility() != View.VISIBLE) {
                                mOrderPayTime.setVisibility(View.VISIBLE);
                            }
                            mOrderPayTime.setText(String.format(WordUtil.getString(R.string.mall_252), orderInfo.getString("paytime")));
                        }
                    } else {
                        if (mOrderPayType != null && mOrderPayType.getVisibility() == View.VISIBLE) {
                            mOrderPayType.setVisibility(View.GONE);
                        }
                        if (mOrderPayTime != null && mOrderPayTime.getVisibility() == View.VISIBLE) {
                            mOrderPayTime.setVisibility(View.GONE);
                        }
                    }
                    if (mGroupBottom != null) {
                        if (mGroupBottom.getChildCount() > 0) {
                            mGroupBottom.removeAllViews();
                        }
                        int bottomViewId = 0;
                        if (orderStatus == Constants.MALL_ORDER_STATUS_CLOSE) {//-1已关闭
                            bottomViewId = R.layout.view_seller_order_bottom_close;
                        }
                        if (bottomViewId != 0) {
                            LayoutInflater.from(mContext).inflate(bottomViewId, mGroupBottom, true);
                        }
                    }
                }
            }
        });
    }

    public void sellerOrderDetailClick(View v) {
        int id = v.getId();
        if (id == R.id.btn_kefu) {//联系买家
            forwardChat();
        } else if (id == R.id.btn_call_phone) {//打电话
            callPhone();
        } else if (id == R.id.btn_copy) {//复制
            copyOrderNo();
        } else if (id == R.id.btn_delete_order) {//删除订单
            onDeleteClick();
        }

    }

    /**
     * 复制订单号
     */
    private void copyOrderNo() {
        if (TextUtils.isEmpty(mOrderId) || mOrderInfo == null) {
            return;
        }
        ClipboardManager cm = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
        ClipData clipData = ClipData.newPlainText("text", mOrderInfo.getString("orderno"));
        cm.setPrimaryClip(clipData);
        ToastUtil.show(R.string.copy_success);
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


    /**
     * 删除订单
     */
    private void onDeleteClick() {
        if (TextUtils.isEmpty(mOrderId)) {
            return;
        }
        new DialogUitl.Builder(mContext)
                .setContent(WordUtil.getString(R.string.mall_370))
                .setCancelable(true)
                .setBackgroundDimEnabled(true)
                .setClickCallback(new DialogUitl.SimpleCallback() {
                    @Override
                    public void onConfirmClick(Dialog dialog, String content) {
                        MallHttpUtil.sellerDeleteOrder(mOrderId, new HttpCallback() {
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


    @Override
    protected void onDestroy() {
        MallHttpUtil.cancel(MallHttpConsts.GET_SELLER_ORDER_DETAIL);
        MallHttpUtil.cancel(MallHttpConsts.SELLER_DELETE_ORDER);
        MallHttpUtil.cancel(ImHttpConsts.GET_IM_USER_INFO);
        super.onDestroy();
    }
}
