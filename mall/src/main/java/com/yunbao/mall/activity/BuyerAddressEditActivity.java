package com.yunbao.mall.activity;

import android.app.Dialog;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
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
import com.yunbao.mall.bean.BuyerAddressBean;
import com.yunbao.mall.http.MallHttpConsts;
import com.yunbao.mall.http.MallHttpUtil;

import java.util.ArrayList;

import cn.qqtheme.framework.entity.City;
import cn.qqtheme.framework.entity.County;
import cn.qqtheme.framework.entity.Province;
import cn.qqtheme.framework.picker.AddressPicker;

/**
 * 买家 增加收货地址
 */
public class BuyerAddressEditActivity extends AbsActivity implements View.OnClickListener {


    private BuyerAddressBean mBean;
    private EditText mName;
    private EditText mPhoneNum;
    private TextView mArea;
    private EditText mAddress;
    private CheckBox mCheckBox;
    private View mBtnDel;

    private String mProvinceVal;
    private String mCityVal;
    private String mZoneVal;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_buyer_address_edit;
    }

    @Override
    protected void main() {
        mName = findViewById(R.id.name);
        mPhoneNum = findViewById(R.id.phone_num);
        mArea = findViewById(R.id.area);
        mAddress = findViewById(R.id.address);
        mCheckBox = findViewById(R.id.checkbox);
        mBtnDel = findViewById(R.id.btn_del);
        findViewById(R.id.btn_submit).setOnClickListener(this);
        findViewById(R.id.btn_area).setOnClickListener(this);
        BuyerAddressBean bean = getIntent().getParcelableExtra(Constants.MALL_BUYER_ADDRESS);
        if (bean != null) {
            setTitle(WordUtil.getString(R.string.mall_162));
            mBean = bean;
            mName.setText(bean.getName());
            mPhoneNum.setText(bean.getPhoneNum());
            mProvinceVal = bean.getProvince();
            mCityVal = bean.getCity();
            mZoneVal = bean.getZone();
            mArea.setText(StringUtil.contact(mProvinceVal, " ", mCityVal, " ", mZoneVal));
            mAddress.setText(bean.getAddress());
            mCheckBox.setChecked(bean.getIsDefault() == 1);
            mBtnDel.setVisibility(View.VISIBLE);
            mBtnDel.setOnClickListener(this);
        } else {
            setTitle(WordUtil.getString(R.string.mall_162));
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.btn_submit) {
            if (mBean != null) {
                modifyAddress();
            } else {
                addAddress();
            }
        } else if (id == R.id.btn_area) {
            chooseCity();
        } else if (id == R.id.btn_del) {
            deleteAddress();
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
                mProvinceVal = provinceName;
                mCityVal = cityName;
                mZoneVal = zoneName;
                mArea.setText(StringUtil.contact(provinceName, " ", cityName, " ", zoneName));
            }
        });
    }


    /**
     * 新增收货地址
     */
    private void addAddress() {
        String name = mName.getText().toString().trim();
        if (TextUtils.isEmpty(name)) {
            ToastUtil.show(R.string.mall_150);
            return;
        }
        String phoneNum = mPhoneNum.getText().toString().trim();
        if (TextUtils.isEmpty(phoneNum)) {
            ToastUtil.show(R.string.mall_151);
            return;
        }
        String area = mArea.getText().toString().trim();
        if (TextUtils.isEmpty(area)) {
            ToastUtil.show(R.string.mall_152);
            return;
        }
        String address = mAddress.getText().toString().trim();
        if (TextUtils.isEmpty(address)) {
            ToastUtil.show(R.string.mall_153);
            return;
        }
        MallHttpUtil.buyerAddAddress(
                name,
                phoneNum,
                mProvinceVal,
                mCityVal,
                mZoneVal,
                address,
                mCheckBox.isChecked() ? "1" : "0",
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


    /**
     * 编辑收货地址
     */
    private void modifyAddress() {
        if (mBean == null) {
            return;
        }
        String name = mName.getText().toString().trim();
        if (TextUtils.isEmpty(name)) {
            ToastUtil.show(R.string.mall_150);
            return;
        }
        String phoneNum = mPhoneNum.getText().toString().trim();
        if (TextUtils.isEmpty(phoneNum)) {
            ToastUtil.show(R.string.mall_151);
            return;
        }
        String area = mArea.getText().toString().trim();
        if (TextUtils.isEmpty(area)) {
            ToastUtil.show(R.string.mall_152);
            return;
        }
        String address = mAddress.getText().toString().trim();
        if (TextUtils.isEmpty(address)) {
            ToastUtil.show(R.string.mall_153);
            return;
        }
        MallHttpUtil.buyerModifyAddress(
                name,
                phoneNum,
                mProvinceVal,
                mCityVal,
                mZoneVal,
                address,
                mCheckBox.isChecked() ? "1" : "0",
                mBean.getId(),
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


    /**
     * 删除收货地址
     */
    private void deleteAddress() {
        if (mBean == null) {
            return;
        }
        new DialogUitl.Builder(mContext)
                .setContent(WordUtil.getString(R.string.mall_163))
                .setCancelable(true)
                .setBackgroundDimEnabled(true)
                .setClickCallback(new DialogUitl.SimpleCallback() {
                    @Override
                    public void onConfirmClick(Dialog dialog, String content) {
                        MallHttpUtil.buyerDeleteAddress(mBean.getId(), new HttpCallback() {
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
                })
                .build()
                .show();
    }

    @Override
    protected void onDestroy() {
        MallHttpUtil.cancel(MallHttpConsts.BUYER_ADD_ADDRESS);
        MallHttpUtil.cancel(MallHttpConsts.BUYER_DELETE_ADDRESS);
        MallHttpUtil.cancel(MallHttpConsts.BUYER_MODIFY_ADDRESS);
        super.onDestroy();
    }
}
