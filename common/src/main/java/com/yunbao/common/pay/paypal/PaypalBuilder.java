package com.yunbao.common.pay.paypal;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.braintreepayments.api.dropin.DropInRequest;
import com.braintreepayments.api.dropin.DropInResult;
import com.braintreepayments.api.models.PayPalRequest;
import com.yunbao.common.R;
import com.yunbao.common.http.CommonHttpUtil;
import com.yunbao.common.http.HttpCallback;
import com.yunbao.common.interfaces.ActivityResultCallback;
import com.yunbao.common.pay.PayCallback;
import com.yunbao.common.utils.ActivityResultUtil;
import com.yunbao.common.utils.DialogUitl;
import com.yunbao.common.utils.ToastUtil;
import com.yunbao.common.utils.WordUtil;

public class PaypalBuilder {

    private FragmentActivity mActivity;
    private PayCallback mPayCallback;
    private String mOrderParams;//订单获取订单需要的参数
    private String mMoney;//要支付的金额
    private String mGoodsName;//商品名称
    private String mBuyType;
    private String mBraintreeToken;
    private String mOrderId;

    public PaypalBuilder(FragmentActivity activity) {
        mActivity = activity;
    }

    public void setMoney(String money) {
        mMoney = money;
    }

    public void setGoodsName(String goodsName) {
        mGoodsName = goodsName;
    }

    public void setOrderParams(String orderParams) {
        mOrderParams = orderParams;
    }


    public void setPayCallback(PayCallback callback) {
        mPayCallback = callback;
    }

    public void setBuyType(String buyType) {
        mBuyType = buyType;
    }

    public void setBraintreeToken(String braintreeToken) {
        mBraintreeToken = braintreeToken;
    }

    public void pay() {
        CommonHttpUtil.getPaypalOrder(mOrderParams, new HttpCallback() {
            @Override
            public void onSuccess(int code, String msg, String[] info) {
                if (code == 0 && info.length > 0) {
                    JSONObject obj = JSON.parseObject(info[0]);
                    mOrderId = obj.getString("orderid");
                    PayPalRequest payPalRequest = new PayPalRequest(mMoney);
                    payPalRequest.localeCode("en_US");
                    payPalRequest.currencyCode("USD");
                    payPalRequest.displayName(mGoodsName);
                    payPalRequest.intent(PayPalRequest.INTENT_SALE);
                    DropInRequest dropInRequest = new DropInRequest()
                            .amount(mMoney)
                            .paypalRequest(payPalRequest)
                            .clientToken(mBraintreeToken);
                    ActivityResultUtil.startActivityForResult(mActivity, dropInRequest.getIntent(mActivity), new ActivityResultCallback() {
                        @Override
                        public void onSuccess(Intent intent) {

                        }

                        @Override
                        public void onResult(int resultCode, Intent data) {
                            switch (resultCode) {
                                case Activity.RESULT_OK:
                                    DropInResult result = data.getParcelableExtra(DropInResult.EXTRA_DROP_IN_RESULT);
                                    if (result != null && result.getPaymentMethodNonce() != null) {
                                        String nonce = result.getPaymentMethodNonce().getNonce();
                                        CommonHttpUtil.braintreeCallback(mOrderId, mBuyType, nonce, mMoney, new HttpCallback() {
                                            @Override
                                            public void onSuccess(int code, String msg, String[] info) {
                                                if (code == 0) {
                                                    if (mPayCallback != null) {
                                                        mPayCallback.onSuccess();
                                                    }
                                                } else {
                                                    ToastUtil.show(msg);
                                                }
                                            }

                                            @Override
                                            public boolean showLoadingDialog() {
                                                return true;
                                            }

                                            @Override
                                            public Dialog createLoadingDialog() {
                                                return DialogUitl.loadingDialog(mActivity);
                                            }
                                        });
                                    } else {
                                        ToastUtil.show(R.string.pay_fail);
                                    }
                                    break;
                                case Activity.RESULT_CANCELED:
                                    ToastUtil.show(R.string.pay_cancel);
                                    break;
                                default:
//                                    Exception error = (Exception) data.getSerializableExtra(DropInActivity.EXTRA_ERROR);
                                    ToastUtil.show(WordUtil.getString(R.string.pay_fail));
                                    break;
                            }
                        }
                    });
                } else {
                    ToastUtil.show(msg);
                }
            }

        });
    }
}
