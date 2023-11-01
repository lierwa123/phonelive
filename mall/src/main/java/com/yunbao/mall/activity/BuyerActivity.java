package com.yunbao.mall.activity;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.yunbao.common.CommonAppConfig;
import com.yunbao.common.Constants;
import com.yunbao.common.activity.AbsActivity;
import com.yunbao.common.bean.ConfigBean;
import com.yunbao.common.bean.UserBean;
import com.yunbao.common.glide.ImgLoader;
import com.yunbao.common.http.HttpCallback;
import com.yunbao.common.utils.ToastUtil;
import com.yunbao.mall.R;
import com.yunbao.mall.http.MallHttpConsts;
import com.yunbao.mall.http.MallHttpUtil;

/**
 * 买家页面
 */
public class BuyerActivity extends AbsActivity {

    public static void forward(Context context) {
        context.startActivity(new Intent(context, BuyerActivity.class));
    }

    private TextView mCountPay;
    private TextView mCountSend;
    private TextView mCountReceive;
    private TextView mCountComment;
    private TextView mCountRefund;
    private View mBtnOpenShop;//我要开店
    private int mApplyStatus;//  -1 无申请记录  0 审核中  1 通过  2 拒绝
    private boolean mFromSeller;
    private String mApplyReason;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_buyer;
    }

    @Override
    protected void main() {
        ConfigBean configBean = CommonAppConfig.getInstance().getConfig();
        if (configBean != null) {
            String shopName = configBean.getShopSystemName();
            TextView title = findViewById(R.id.title);
            title.setText(shopName);
        }
        mCountPay = findViewById(R.id.count_pay);
        mCountSend = findViewById(R.id.count_send);
        mCountReceive = findViewById(R.id.count_receive);
        mCountComment = findViewById(R.id.count_comment);
        mCountRefund = findViewById(R.id.count_refund);
        mBtnOpenShop = findViewById(R.id.btn_open);
        ImageView avatar = findViewById(R.id.avatar);
        TextView name = findViewById(R.id.name);
        UserBean u = CommonAppConfig.getInstance().getUserBean();
        if (u != null) {
            ImgLoader.display(mContext, u.getAvatar(), avatar);
            name.setText(u.getUserNiceName());
        }
        mFromSeller = getIntent().getBooleanExtra(Constants.MALL_GOODS_FROM_SHOP, false);
        if (mFromSeller) {
            findViewById(R.id.tip).setVisibility(View.VISIBLE);
            findViewById(R.id.btn_back_2).setVisibility(View.VISIBLE);
        }
    }


    private void getData() {
        MallHttpUtil.getBuyerHome(new HttpCallback() {
            @Override
            public void onSuccess(int code, String msg, String[] info) {
                if (code == 0 && info.length > 0) {
                    JSONObject obj = JSON.parseObject(info[0]);
                    showCount(mCountPay, obj.getIntValue("wait_payment"));//待付款
                    showCount(mCountSend, obj.getIntValue("wait_shipment"));//待发货
                    showCount(mCountReceive, obj.getIntValue("wait_receive"));//待收货
                    showCount(mCountComment, obj.getIntValue("wait_evaluate"));//待评价
                    showCount(mCountRefund, obj.getIntValue("refund"));//退款
                    mApplyStatus = obj.getIntValue("apply_status");
                    mApplyReason = obj.getString("apply_reason");
                    if (mBtnOpenShop != null) {
                        if (mApplyStatus == 1) {
                            if (mBtnOpenShop.getVisibility() == View.VISIBLE) {
                                mBtnOpenShop.setVisibility(View.INVISIBLE);
                            }
                        } else {
                            if (mBtnOpenShop.getVisibility() != View.VISIBLE) {
                                mBtnOpenShop.setVisibility(View.VISIBLE);
                            }
                        }

                    }
                }
            }
        });
    }


    /**
     * 显示待付款，待发货等订单个数
     */
    private void showCount(TextView textView, int count) {
        if (textView != null) {
            if (count > 0) {
                if (textView.getVisibility() != View.VISIBLE) {
                    textView.setVisibility(View.VISIBLE);
                }
                textView.setText(String.valueOf(count));
            } else {
                if (textView.getVisibility() == View.VISIBLE) {
                    textView.setVisibility(View.INVISIBLE);
                }
            }
        }
    }

    public void buyerClick(View v) {
        int id = v.getId();
        if (id == R.id.btn_record) {//浏览记录
            GoodsRecordActivity.forward(mContext);
        } else if (id == R.id.btn_address) {//我的地址
            BuyerAddressActivity.forward(mContext);
        } else if (id == R.id.btn_account) {//账户余额
            BuyerAccountActivity.forward(mContext);
        } else if (id == R.id.btn_my_order) {//我的订单
            BuyerOrderActivity.forward(mContext, 0);
        } else if (id == R.id.btn_pay) {//待付款
            BuyerOrderActivity.forward(mContext, 1);
        } else if (id == R.id.btn_send) {//待发货
            BuyerOrderActivity.forward(mContext, 2);
        } else if (id == R.id.btn_receive) {//待收货
            BuyerOrderActivity.forward(mContext, 3);
        } else if (id == R.id.btn_comment) {//待评价
            BuyerOrderActivity.forward(mContext, 4);
        } else if (id == R.id.btn_refund) {//退款
            BuyerOrderActivity.forward(mContext, 5);
        } else if (id == R.id.btn_open) {//我要开店
            clickOpenShop();
        } else if (id == R.id.btn_back_2) {
            onBackPressed();
        }

    }


    @Override
    public void backClick(View v) {
        if (mFromSeller) {
            setResult(RESULT_OK);
            finish();
        } else {
            super.backClick(v);
        }
    }

    private void clickOpenShop() {
        if (mApplyStatus == -1) {//  -1 无申请记录
            applyShop();
        } else if (mApplyStatus == 0) {//0 审核中
            ShopApplyResultActivity.forward(mContext, false, mApplyReason);
        } else if (mApplyStatus == 2) {//2 拒绝
            ShopApplyResultActivity.forward(mContext, true, mApplyReason);
        }
    }

    /**
     * 申请店铺
     */
    private void applyShop() {
        MallHttpUtil.getUserAuthInfo(new HttpCallback() {
            @Override
            public void onSuccess(int code, String msg, String[] info) {
                if (code == 0) {
                    if (info.length > 0) {
                        JSONObject obj = JSON.parseObject(info[0]);
                        ShopApplyActivity.forward(mContext, obj.getString("cer_no"), obj.getString("real_name"), false);
                    }
                } else {
                    ToastUtil.show(msg);
                }
            }
        });
    }


    @Override
    protected void onResume() {
        super.onResume();
        getData();
    }

    @Override
    protected void onDestroy() {
        MallHttpUtil.cancel(MallHttpConsts.GET_BUYER_HOME);
        MallHttpUtil.cancel(MallHttpConsts.GET_USER_AUTH_INFO);
        super.onDestroy();
    }

}
