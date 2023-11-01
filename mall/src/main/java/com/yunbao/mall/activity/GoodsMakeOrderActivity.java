package com.yunbao.mall.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.yunbao.common.Constants;
import com.yunbao.common.activity.AbsActivity;
import com.yunbao.common.glide.ImgLoader;
import com.yunbao.common.http.HttpCallback;
import com.yunbao.common.utils.CalculateUtil;
import com.yunbao.common.utils.StringUtil;
import com.yunbao.common.utils.ToastUtil;
import com.yunbao.common.utils.WordUtil;
import com.yunbao.mall.R;
import com.yunbao.mall.bean.BuyerAddressBean;
import com.yunbao.mall.bean.GoodsSpecBean;
import com.yunbao.mall.dialog.GoodsPayDialogFragment;
import com.yunbao.mall.http.MallHttpConsts;
import com.yunbao.mall.http.MallHttpUtil;

import java.util.Arrays;
import java.util.List;

public class GoodsMakeOrderActivity extends AbsActivity implements View.OnClickListener, GoodsPayDialogFragment.ActionListener {

    public static void forward(Context context, String shopName, String goodsId, String goodsName, GoodsSpecBean goodsSpecBean, int count, double postage, String liveUid) {
        Intent intent = new Intent(context, GoodsMakeOrderActivity.class);
        intent.putExtra(Constants.MALL_SHOP_NAME, shopName);
        intent.putExtra(Constants.MALL_GOODS_ID, goodsId);
        intent.putExtra(Constants.MALL_GOODS_NAME, goodsName);
        intent.putExtra(Constants.MALL_GOODS_SPEC, goodsSpecBean);
        intent.putExtra(Constants.MALL_GOODS_COUNT, count);
        intent.putExtra(Constants.MALL_POSTAGE, postage);
        intent.putExtra(Constants.LIVE_UID, liveUid);
        context.startActivity(intent);
    }

    private View mGroupAddress;
    private View mTipAddress;
    private TextView mName;
    private TextView mAddress;
    private TextView mShopName;
    private ImageView mGoodsThumb;
    private TextView mGoodsName;
    private TextView mGoodsSpec;
    private TextView mGoodsPirce;
    private TextView mCount;
    private View mBtnReduce;
    private View mBtnAdd;
    //    private View mPostageFree;
    private TextView mPostage;
    private TextView mPriceAll;
    private TextView mMoney;
    private EditText mMessage;//留言

    private String mGoodsId;
    private String mGoodsNameVal;
    private GoodsSpecBean mSpecBean;
    private double mPostageVal;
    private String mMoneySymbol;
    private int mCountVal;
    private double mGoodsPriceVal;
    private double mMoneyVal;
    private BuyerAddressBean mBuyerAddressBean;
    private String mOrderId;
    private String mLiveUid;


    @Override
    protected int getLayoutId() {
        return R.layout.activity_goods_make_order;
    }

    @Override
    protected void main() {
        setTitle(WordUtil.getString(R.string.mall_187));
        Intent intent = getIntent();
        mGoodsId = intent.getStringExtra(Constants.MALL_GOODS_ID);
        mLiveUid = intent.getStringExtra(Constants.LIVE_UID);
        GoodsSpecBean specBean = intent.getParcelableExtra(Constants.MALL_GOODS_SPEC);
        mSpecBean = specBean;
        mCountVal = intent.getIntExtra(Constants.MALL_GOODS_COUNT, 1);
        mPostageVal = intent.getDoubleExtra(Constants.MALL_POSTAGE, 0);
        String shopName = intent.getStringExtra(Constants.MALL_SHOP_NAME);
        mGoodsNameVal = intent.getStringExtra(Constants.MALL_GOODS_NAME);
        mMoneySymbol = WordUtil.getString(R.string.money_symbol);
        mGoodsPriceVal = Double.parseDouble(specBean.getPrice());

        mGroupAddress = findViewById(R.id.group_address);
        mTipAddress = findViewById(R.id.address_tip);
        mName = findViewById(R.id.name);
        mAddress = findViewById(R.id.address);
        mShopName = findViewById(R.id.shop_name);
        mGoodsThumb = findViewById(R.id.thumb);
        mGoodsName = findViewById(R.id.goods_name);
        mGoodsSpec = findViewById(R.id.goods_spec);
        mGoodsPirce = findViewById(R.id.goods_price);
        mCount = findViewById(R.id.count);
        mBtnReduce = findViewById(R.id.btn_reduce);
        mBtnAdd = findViewById(R.id.btn_add);
//        mPostageFree = findViewById(R.id.postage_free);
        mPostage = findViewById(R.id.postage);
        mPriceAll = findViewById(R.id.pirce_all);
        mMoney = findViewById(R.id.money);
        mMessage = findViewById(R.id.msg);

        mBtnReduce.setOnClickListener(this);
        mBtnAdd.setOnClickListener(this);
        findViewById(R.id.btn_address).setOnClickListener(this);
        findViewById(R.id.btn_submit).setOnClickListener(this);

        mShopName.setText(shopName);
        ImgLoader.display(mContext, specBean.getThumb(), mGoodsThumb);
        mGoodsName.setText(mGoodsNameVal);
        mGoodsSpec.setText(String.format(WordUtil.getString(R.string.mall_188), specBean.getName()));
        mGoodsPirce.setText(StringUtil.contact(mMoneySymbol, specBean.getPrice()));
        mPostage.setText(StringUtil.contact(mMoneySymbol, String.valueOf(mPostageVal)));
//        if (mPostageVal <= 0) {
//            mPostageFree.setVisibility(View.VISIBLE);
//        }
        showPrice();
        MallHttpUtil.getBuyerAddress(new HttpCallback() {
            @Override
            public void onSuccess(int code, String msg, String[] info) {
                if (code == 0 && info.length > 0) {
                    List<BuyerAddressBean> addressList = JSON.parseArray(Arrays.toString(info), BuyerAddressBean.class);
                    for (BuyerAddressBean bean : addressList) {
                        if (bean.getIsDefault() == 1) {
                            showAddress(bean);
                            break;
                        }
                    }
                }
            }
        });
    }

    private void showAddress(BuyerAddressBean bean) {
        if (bean != null) {
            mBuyerAddressBean = bean;
            if (mGroupAddress != null && mGroupAddress.getVisibility() != View.VISIBLE) {
                mGroupAddress.setVisibility(View.VISIBLE);
            }
            if (mTipAddress != null && mTipAddress.getVisibility() == View.VISIBLE) {
                mTipAddress.setVisibility(View.INVISIBLE);
            }
            if (mName != null) {
                mName.setText(StringUtil.contact(bean.getName(), "  ", bean.getPhoneNum()));
            }
            if (mAddress != null) {
                mAddress.setText(StringUtil.contact(bean.getProvince(), " ", bean.getCity(), " ", bean.getZone(), " ", bean.getAddress()));
            }
        }
    }

    private void showPrice() {
        double allPirce = CalculateUtil.multiply2(String.valueOf(mGoodsPriceVal), String.valueOf(mCountVal));
        mMoneyVal = allPirce + mPostageVal;
        mPriceAll.setText(String.valueOf(allPirce));
        mCount.setText(String.valueOf(mCountVal));
        mMoney.setText(StringUtil.contact(mMoneySymbol, String.valueOf(mMoneyVal)));
        mBtnReduce.setEnabled(mCountVal > 1);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.btn_address) {
            chooseAddress();
        } else if (id == R.id.btn_add) {
            add();
        } else if (id == R.id.btn_reduce) {
            reduce();
        } else if (id == R.id.btn_submit) {
            submit();
        }
    }

    private void reduce() {
        mCountVal--;
        showPrice();
    }

    private void add() {
        mCountVal++;
        showPrice();
    }

    private void chooseAddress() {
        Intent intent = new Intent(mContext, BuyerAddressActivity.class);
        intent.putExtra(Constants.MALL_BUYER_ADDRESS, true);
        startActivityForResult(intent, 100);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (requestCode == 100 && resultCode == RESULT_OK && intent != null) {
            BuyerAddressBean bean = intent.getParcelableExtra(Constants.MALL_BUYER_ADDRESS);
            showAddress(bean);
        }
    }

    private void submit() {
        if (!TextUtils.isEmpty(mOrderId)) {
            showPayDialog();
            return;
        }
        if (mBuyerAddressBean == null) {
            ToastUtil.show(R.string.mall_189);
            return;
        }
        String msg = mMessage.getText().toString().trim();
        MallHttpUtil.buyerCreateOrder(mBuyerAddressBean.getId(), mGoodsId, mSpecBean.getId(), mCountVal, msg,mLiveUid,
                new HttpCallback() {
                    @Override
                    public void onSuccess(int code, String msg, String[] info) {
                        if (code == 0 && info.length > 0) {
                            if (mBtnAdd != null) {
                                mBtnAdd.setEnabled(false);
                            }
                            if (mBtnReduce != null) {
                                mBtnReduce.setEnabled(false);
                            }
                            mOrderId = JSON.parseObject(info[0]).getString("orderid");
                            showPayDialog();
                        }
                        ToastUtil.show(msg);
                    }
                }
        );
    }


    /**
     * 打开支付弹窗
     */
    private void showPayDialog() {
        GoodsPayDialogFragment fragment = new GoodsPayDialogFragment();
        Bundle bundle = new Bundle();
        bundle.putString(Constants.MALL_ORDER_ID, mOrderId);
        bundle.putDouble(Constants.MALL_ORDER_MONEY, mMoneyVal);
        bundle.putString(Constants.MALL_GOODS_NAME, mGoodsNameVal);
        fragment.setArguments(bundle);
        fragment.setActionListener(this);
        fragment.show(getSupportFragmentManager(), "GoodsPayDialogFragment");
    }

    @Override
    protected void onDestroy() {
        MallHttpUtil.cancel(MallHttpConsts.GET_BUYER_ADDRESS);
        MallHttpUtil.cancel(MallHttpConsts.BUYER_CREATE_ORDER);
        super.onDestroy();
    }

    @Override
    public void onPayResult(boolean paySuccess) {
        BuyerOrderDetailActivity.forward(mContext, mOrderId);
        finish();
    }
}
