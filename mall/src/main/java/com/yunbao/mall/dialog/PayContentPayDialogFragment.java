package com.yunbao.mall.dialog;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.yunbao.common.CommonAppConfig;
import com.yunbao.common.Constants;
import com.yunbao.common.HtmlConfig;
import com.yunbao.common.dialog.AbsDialogFragment;
import com.yunbao.common.http.CommonHttpConsts;
import com.yunbao.common.http.CommonHttpUtil;
import com.yunbao.common.http.HttpCallback;
import com.yunbao.common.pay.PayCallback;
import com.yunbao.common.pay.PayPresenter;
import com.yunbao.common.utils.MD5Util;
import com.yunbao.common.utils.StringUtil;
import com.yunbao.common.utils.ToastUtil;
import com.yunbao.mall.R;
import com.yunbao.mall.adapter.GoodsPayAdapter;
import com.yunbao.mall.bean.GoodsPayBean;
import com.yunbao.mall.http.MallHttpConsts;
import com.yunbao.mall.http.MallHttpUtil;

import java.util.List;

/**
 * 购买付费内容 付款弹窗
 */
public class PayContentPayDialogFragment extends AbsDialogFragment implements View.OnClickListener {

    private RecyclerView mRecyclerView;
    private GoodsPayAdapter mAdapter;
    private String mGoodsId;
    private double mMoneyVal;
    private String mGoodsNameVal;
    private ActionListener mActionListener;
    private PayPresenter mPayPresenter;

    @Override
    protected int getLayoutId() {
        return R.layout.dialog_pay_content_pay;
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
        Bundle bundle = getArguments();
        if (bundle == null) {
            return;
        }
        mGoodsId = bundle.getString(Constants.MALL_GOODS_ID);
        mMoneyVal = bundle.getDouble(Constants.MALL_ORDER_MONEY, 0);
        mGoodsNameVal = bundle.getString(Constants.MALL_GOODS_NAME);
        TextView money = findViewById(R.id.money);
        money.setText(String.valueOf(mMoneyVal));
        mRecyclerView = findViewById(R.id.recyclerView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));

        findViewById(R.id.btn_close).setOnClickListener(this);
        findViewById(R.id.btn_pay).setOnClickListener(this);

        MallHttpUtil.getPayContentPayList(new HttpCallback() {
            @Override
            public void onSuccess(int code, String msg, String[] info) {
                if (code == 0 && info.length > 0) {
                    JSONObject obj = JSON.parseObject(info[0]);

                    List<GoodsPayBean> payList = JSON.parseArray(obj.getString("paylist"), GoodsPayBean.class);
                    if (payList != null && payList.size() > 0) {
                        payList.get(0).setChecked(true);
                        mAdapter = new GoodsPayAdapter(mContext, payList, obj.getString("balance"));
                        if (mRecyclerView != null) {
                            mRecyclerView.setAdapter(mAdapter);
                        }
                        mPayPresenter = new PayPresenter((FragmentActivity) mContext);
                        mPayPresenter.setServiceNameAli(Constants.MALL_PAY_CONTENT_ALI);
                        mPayPresenter.setServiceNameWx(Constants.MALL_PAY_CONTENT_WX);
                        mPayPresenter.setServiceNamePaypal(Constants.MALL_PAY_CONTENT_PAYPAL);
                        mPayPresenter.setAliCallbackUrl(HtmlConfig.ALI_PAY_MALL_PAY_CONTENT);
                        mPayPresenter.setAliPartner(obj.getString("aliapp_partner"));
                        mPayPresenter.setAliSellerId(obj.getString("aliapp_seller_id"));
                        mPayPresenter.setAliPrivateKey(obj.getString("aliapp_key_android"));
                        mPayPresenter.setWxAppID(obj.getString("wx_appid"));
                        mPayPresenter.setPayCallback(new PayCallback() {
                            @Override
                            public void onSuccess() {
                                dismiss();
                                if (mActionListener != null) {
                                    mActionListener.onPaySuccess();
                                }
                            }

                            @Override
                            public void onFailed() {
                                ToastUtil.show(R.string.mall_367);
                            }
                        });
                    }
                }
            }
        });
    }

    public void setActionListener(ActionListener actionListener) {
        mActionListener = actionListener;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.btn_close) {
            dismiss();
        } else if (id == R.id.btn_pay) {
            pay();
        }
    }

    private void pay() {
        if (TextUtils.isEmpty(mGoodsId) || mContext == null || mAdapter == null) {
            return;
        }
        GoodsPayBean bean = mAdapter.getCheckedPayType();
        if (bean == null) {
            return;
        }
        if (Constants.PAY_TYPE_BALANCE.equals(bean.getId())) {//余额支付
            balancePay();
        } else {//支付宝和微信支付
            if (mPayPresenter == null) {
                return;
            }
            String time = String.valueOf(System.currentTimeMillis() / 1000);
            CommonAppConfig appConfig = CommonAppConfig.getInstance();
            String uid = appConfig.getUid();
            String token = appConfig.getToken();
            String sign = MD5Util.getMD5(StringUtil.contact("object_id=", mGoodsId, "&time=", time,
                    "&token=", token, "&uid=", uid, "&", CommonHttpUtil.SALT));
            String orderParams = StringUtil.contact(
                    "&uid=", uid,
                    "&token=", token,
                    "&time=", time,
                    "&sign=", sign,
                    "&object_id=", mGoodsId);
            mPayPresenter.pay(bean.getId(), String.valueOf(mMoneyVal), mGoodsNameVal, orderParams);
        }
    }

    /**
     * 余额支付
     */
    private void balancePay() {
        MallHttpUtil.buyPayContent(mGoodsId, new HttpCallback() {
            @Override
            public void onSuccess(int code, String msg, String[] info) {
                if (code == 0) {
                    dismiss();
                    if (mActionListener != null) {
                        mActionListener.onPaySuccess();
                    }
                }
                ToastUtil.show(msg);
            }
        });
    }


    @Override
    public void onDestroy() {
        MallHttpUtil.cancel(MallHttpConsts.GET_PAY_CONTENT_PAY_LIST);
        MallHttpUtil.cancel(MallHttpConsts.BUY_PAY_CONTENT);
        MallHttpUtil.cancel(CommonHttpConsts.GET_ALI_ORDER);
        MallHttpUtil.cancel(CommonHttpConsts.GET_WX_ORDER);
        if (mPayPresenter != null) {
            mPayPresenter.release();
        }
        mPayPresenter = null;
        mContext = null;
        mAdapter = null;
        super.onDestroy();
    }

    public interface ActionListener {
        void onPaySuccess();
    }


}
