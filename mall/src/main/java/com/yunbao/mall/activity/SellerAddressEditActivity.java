package com.yunbao.mall.activity;

import android.app.Dialog;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.yunbao.common.CommonAppConfig;
import com.yunbao.common.Constants;
import com.yunbao.common.activity.AbsActivity;
import com.yunbao.common.http.HttpCallback;
import com.yunbao.common.interfaces.CommonCallback;
import com.yunbao.common.utils.CityUtil;
import com.yunbao.common.utils.DialogUitl;
import com.yunbao.common.utils.StringUtil;
import com.yunbao.common.utils.ToastUtil;
import com.yunbao.common.utils.WordUtil;
import com.yunbao.mall.R;
import com.yunbao.mall.http.MallHttpUtil;

import java.util.ArrayList;

import cn.qqtheme.framework.entity.City;
import cn.qqtheme.framework.entity.County;
import cn.qqtheme.framework.entity.Province;
import cn.qqtheme.framework.picker.AddressPicker;

/**
 * 卖家 编辑退货地址
 */
public class SellerAddressEditActivity extends AbsActivity implements View.OnClickListener {

    private EditText mRefundName;//退货人名字
    private EditText mRefundPhoneNum;//退货人手机号
    private TextView mRefundArea;//退货人所在地区
    private EditText mRefundAddress;//退货人详细地址

    private String mRefundProvinceVal;//退货人所在省
    private String mRefundCityVal;//退货人所在市
    private String mRefundZoneVal;//退货人所在区


    @Override
    protected int getLayoutId() {
        return R.layout.activity_seller_address_edit;
    }

    @Override
    protected void main() {
        setTitle(WordUtil.getString(R.string.mall_149));
        mRefundName = findViewById(R.id.refund_name);//退货人名字
        mRefundPhoneNum = findViewById(R.id.refund_phone_num);//退货人手机号
        mRefundArea = findViewById(R.id.refund_area);//退货人所在地区
        mRefundAddress = findViewById(R.id.refund_address);//退货人详细地址
        findViewById(R.id.btn_refund_area).setOnClickListener(this);
        findViewById(R.id.btn_submit).setOnClickListener(this);
        Intent intent = getIntent();
        mRefundName.setText(intent.getStringExtra(Constants.MALL_REFUND_NAME));
        mRefundPhoneNum.setText(intent.getStringExtra(Constants.MALL_REFUND_PHONE));
        mRefundAddress.setText(intent.getStringExtra(Constants.MALL_REFUND_ADDRESS));
        mRefundProvinceVal = intent.getStringExtra(Constants.MALL_REFUND_PROVINCE);
        mRefundCityVal = intent.getStringExtra(Constants.MALL_REFUND_CITY);
        mRefundZoneVal = intent.getStringExtra(Constants.MALL_REFUND_ZONE);
        mRefundArea.setText(StringUtil.contact(mRefundProvinceVal, " ", mRefundCityVal, " ", mRefundZoneVal));


    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.btn_refund_area) {
            chooseCity();
        } else if (id == R.id.btn_submit) {
            submit();
        }

    }


    /**
     * 选择地区
     */
    private void chooseCity() {
        ArrayList<Province> list = CityUtil.getInstance().getCityList();
        if (list == null || list.size() == 0) {
            final Dialog loading = DialogUitl.loadingDialog(mContext);
            loading.show();
            CityUtil.getInstance().getCityListFromAssets(new CommonCallback<ArrayList<Province>>() {
                @Override
                public void callback(ArrayList<Province> newList) {
                    loading.dismiss();
                    if (newList != null) {
                        showChooseCityDialog(newList);
                    }
                }
            });
        } else {
            showChooseCityDialog(list);
        }
    }

    /**
     * 选择地区
     */
    private void showChooseCityDialog(ArrayList<Province> list) {
        String province = CommonAppConfig.getInstance().getProvince();
        String city = CommonAppConfig.getInstance().getCity();
        String district = CommonAppConfig.getInstance().getDistrict();
        DialogUitl.showCityChooseDialog(this, list, province, city, district, new AddressPicker.OnAddressPickListener() {
            @Override
            public void onAddressPicked(Province province, final City city, County county) {
                String provinceName = province.getAreaName();
                String cityName = city.getAreaName();
                String zoneName = county.getAreaName();
                mRefundProvinceVal = provinceName;
                mRefundCityVal = cityName;
                mRefundZoneVal = zoneName;
                mRefundArea.setText(StringUtil.contact(provinceName, " ", cityName, " ", zoneName));
            }
        });
    }

    private void submit() {
        String name = mRefundName.getText().toString().trim();
        if (TextUtils.isEmpty(name)) {
            ToastUtil.show(R.string.mall_150);
            return;
        }
        String phoneNum = mRefundPhoneNum.getText().toString().trim();
        if (TextUtils.isEmpty(phoneNum)) {
            ToastUtil.show(R.string.mall_151);
            return;
        }
        String area = mRefundArea.getText().toString().trim();
        if (TextUtils.isEmpty(area)) {
            ToastUtil.show(R.string.mall_152);
            return;
        }
        String address = mRefundAddress.getText().toString().trim();
        if (TextUtils.isEmpty(address)) {
            ToastUtil.show(R.string.mall_153);
            return;
        }

        MallHttpUtil.modifyRefundAddress(
                name,
                phoneNum,
                mRefundProvinceVal,
                mRefundCityVal,
                mRefundZoneVal,
                address,
                new HttpCallback() {
                    @Override
                    public void onSuccess(int code, String msg, String[] info) {
                        if (code == 0) {
                            setResult(RESULT_OK);
                            finish();
                        }
                        ToastUtil.show(msg);
                    }
                }
        );

    }
}
