package com.yunbao.mall.activity;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.yunbao.common.Constants;
import com.yunbao.common.activity.AbsActivity;
import com.yunbao.common.http.HttpCallback;
import com.yunbao.common.utils.StringUtil;
import com.yunbao.common.utils.WordUtil;
import com.yunbao.mall.R;
import com.yunbao.mall.http.MallHttpConsts;
import com.yunbao.mall.http.MallHttpUtil;

/**
 * 卖家退货地址管理
 */
public class SellerAddressActivity extends AbsActivity implements View.OnClickListener {

    public static void forward(Context context) {
        context.startActivity(new Intent(context, SellerAddressActivity.class));
    }

    private TextView mName;
    private TextView mAddress;
    private String mNameVal;
    private String mPhoneNumVal;
    private String mRefundProvinceVal;//退货人所在省
    private String mRefundCityVal;//退货人所在市
    private String mRefundZoneVal;//退货人所在区
    private String mAddressVal;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_seller_address;
    }

    @Override
    protected void main() {
        setTitle(WordUtil.getString(R.string.mall_148));
        mName = findViewById(R.id.name);
        mAddress = findViewById(R.id.address);
        findViewById(R.id.btn_edit).setOnClickListener(this);
        getData();
    }

    private void getData() {
        MallHttpUtil.getRefundAddress(new HttpCallback() {
            @Override
            public void onSuccess(int code, String msg, String[] info) {
                if (code == 0 && info.length > 0) {
                    JSONObject obj = JSON.parseObject(info[0]);
                    mNameVal = obj.getString("receiver");
                    mPhoneNumVal = obj.getString("receiver_phone");
                    mRefundProvinceVal = obj.getString("receiver_province");
                    mRefundCityVal = obj.getString("receiver_city");
                    mRefundZoneVal = obj.getString("receiver_area");
                    mAddressVal = obj.getString("receiver_address");

                    if (mName != null) {
                        mName.setText(StringUtil.contact(mNameVal, "  ", mPhoneNumVal));
                    }
                    if (mAddress != null) {
                        mAddress.setText(StringUtil.contact(
                                mRefundProvinceVal, " ",
                                mRefundCityVal, " ",
                                mRefundZoneVal, " ",
                                mAddressVal
                        ));
                    }
                }
            }
        });
    }


    @Override
    public void onClick(View v) {
        Intent intent = new Intent(mContext, SellerAddressEditActivity.class);
        intent.putExtra(Constants.MALL_REFUND_NAME, mNameVal);
        intent.putExtra(Constants.MALL_REFUND_PHONE, mPhoneNumVal);
        intent.putExtra(Constants.MALL_REFUND_PROVINCE, mRefundProvinceVal);
        intent.putExtra(Constants.MALL_REFUND_CITY, mRefundCityVal);
        intent.putExtra(Constants.MALL_REFUND_ZONE, mRefundZoneVal);
        intent.putExtra(Constants.MALL_REFUND_ADDRESS, mAddressVal);
        startActivityForResult(intent, 100);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == RESULT_OK) {
            getData();
        }
    }

    @Override
    protected void onDestroy() {
        MallHttpUtil.cancel(MallHttpConsts.GET_REFUND_ADDRESS);
        super.onDestroy();
    }
}
