package com.yunbao.mall.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.yunbao.common.CommonAppConfig;
import com.yunbao.common.Constants;
import com.yunbao.common.activity.AbsActivity;
import com.yunbao.common.glide.ImgLoader;
import com.yunbao.common.http.HttpCallback;
import com.yunbao.common.interfaces.ActivityResultCallback;
import com.yunbao.common.interfaces.CommonCallback;
import com.yunbao.common.interfaces.ImageResultCallback;
import com.yunbao.common.upload.UploadBean;
import com.yunbao.common.upload.UploadCallback;
import com.yunbao.common.upload.UploadStrategy;
import com.yunbao.common.upload.UploadUtil;
import com.yunbao.common.utils.ActivityResultUtil;
import com.yunbao.common.utils.CityUtil;
import com.yunbao.common.utils.DialogUitl;
import com.yunbao.common.utils.MediaUtil;
import com.yunbao.common.utils.StringUtil;
import com.yunbao.common.utils.ToastUtil;
import com.yunbao.common.utils.WordUtil;
import com.yunbao.mall.R;
import com.yunbao.mall.bean.ManageClassBean;
import com.yunbao.mall.http.MallHttpConsts;
import com.yunbao.mall.http.MallHttpUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import cn.qqtheme.framework.entity.City;
import cn.qqtheme.framework.entity.County;
import cn.qqtheme.framework.entity.Province;
import cn.qqtheme.framework.picker.AddressPicker;

/**
 * 店铺申请
 */
public class ShopApplyActivity extends AbsActivity {

    public static void forward(Context context, String cardNo, String realName, boolean applyFailed) {
        Intent intent = new Intent(context, ShopApplyActivity.class);
        intent.putExtra(Constants.UID, cardNo);
        intent.putExtra(Constants.NICK_NAME, realName);
        intent.putExtra(Constants.MALL_APPLY_FAILED, applyFailed);
        context.startActivity(intent);
    }


    private TextView mName;//姓名
    private TextView mCardNo;//身份证号
    private TextView mManageClassName;//经营类目
    private EditText mManageName;//经营者名字
    private EditText mManagePhoneNum;//经营者手机号
    private TextView mManageArea;//经营者所在地区
    private EditText mManageAddress;//经营者详细地址
    private View mIndicatorKefu;
    private EditText mKefuPhoneNum;//客服电话
    private View mRefundGroup;//退货人信息
    private EditText mRefundName;//退货人名字
    private EditText mRefundPhoneNum;//退货人手机号
    private TextView mRefundArea;//退货人所在地区
    private EditText mRefundAddress;//退货人详细地址
    private View mIndicatorRefund;
    private ImageView mImgLicense;//营业执照
    private ImageView mImgOther;//其他证件
    private ImageView mTargetImageView;
    private TextView mTargetArea;

    private List<ManageClassBean> mManageClassList;//经营类目
    private String mProvinceVal;//经营者所在省
    private String mCityVal;//经营者所在市
    private String mZoneVal;//经营者所在区
    private String mRefundProvinceVal;//退货人所在省
    private String mRefundCityVal;//退货人所在市
    private String mRefundZoneVal;//退货人所在区
    private File mImgLicenseFile;//营业执照图片
    private File mImgOtherFile;//其他证件图片
    private String mImgLicenseUrl = "";//营业执照图片
    private String mImgOtherUrl = "";//其他证件图片
    private Dialog mLoading;
    private ImageResultCallback mImageResultCallback = new ImageResultCallback() {
        @Override
        public void beforeCamera() {

        }

        @Override
        public void onSuccess(File file) {
            if (file != null && file.exists()) {
                if (mTargetImageView != null) {
                    if (mTargetImageView == mImgLicense) {
                        mImgLicenseFile = file;
                    } else if (mTargetImageView == mImgOther) {
                        mImgOtherFile = file;
                    }
                    ImgLoader.display(mContext, file, mTargetImageView);
                }
            }
        }


        @Override
        public void onFailure() {
        }
    };

    @Override
    protected int getLayoutId() {
        return R.layout.activity_shop_apply;
    }



    @Override
    protected void main() {
        setTitle(WordUtil.getString(R.string.mall_014));
        mName = findViewById(R.id.name);//姓名
        mCardNo = findViewById(R.id.card_no);//身份证号
        mManageClassName = findViewById(R.id.manage_class_name);//经营类目
        mManageName = findViewById(R.id.manage_name);//经营者名字
        mManagePhoneNum = findViewById(R.id.manage_phone_num);//经营者手机号
        mManageArea = findViewById(R.id.manage_area);//经营者所在地区
        mManageAddress = findViewById(R.id.manage_address);//经营者详细地址
        mIndicatorKefu = findViewById(R.id.indicator_kefu);
        mKefuPhoneNum = findViewById(R.id.kefu_phone_num);//客服电话
        mRefundGroup = findViewById(R.id.refund_group);//退货人信息
        mRefundName = findViewById(R.id.refund_name);//退货人名字
        mRefundPhoneNum = findViewById(R.id.refund_phone_num);//退货人手机号
        mRefundArea = findViewById(R.id.refund_area);//退货人所在地区
        mRefundAddress = findViewById(R.id.refund_address);//退货人详细地址
        mIndicatorRefund = findViewById(R.id.indicator_refund);
        mImgLicense = findViewById(R.id.img_license);//营业执照
        mImgOther = findViewById(R.id.img_other);//其他证件
        TextView idVal = findViewById(R.id.id_val);
        idVal.setText(CommonAppConfig.getInstance().getUid());
        Intent intent = getIntent();
        String cardNo = intent.getStringExtra(Constants.UID);
        String realName = intent.getStringExtra(Constants.NICK_NAME);
        mName.setText(realName);
        mCardNo.setText(cardNo);
        //审核失败，显示原来的信息
        boolean applyFailed = intent.getBooleanExtra(Constants.MALL_APPLY_FAILED, false);
        if (applyFailed) {
            MallHttpUtil.getShopApplyInfo(new HttpCallback() {
                @Override
                public void onSuccess(int code, String msg, String[] info) {
                    if (code == 0 && info.length > 0) {
                        JSONObject obj = JSON.parseObject(info[0]);
                        if (mName != null) {
                            mName.setText(obj.getString("username"));
                        }
                        if (mCardNo != null) {
                            mCardNo.setText(obj.getString("cardno"));
                        }
                        if (mManageName != null) {
                            mManageName.setText(obj.getString("contact"));
                        }
                        if (mManagePhoneNum != null) {
                            mManagePhoneNum.setText(obj.getString("phone"));
                        }
                        mProvinceVal = obj.getString("province");
                        mCityVal = obj.getString("city");
                        mZoneVal = obj.getString("area");
                        if (mManageArea != null) {
                            mManageArea.setText(StringUtil.contact(mProvinceVal, " ", mCityVal, " ", mZoneVal));
                        }
                        if (mManageAddress != null) {
                            mManageAddress.setText(obj.getString("address"));
                        }
                        if (mKefuPhoneNum != null) {
                            mKefuPhoneNum.setText(obj.getString("service_phone"));
                        }
                        if (mRefundName != null) {
                            mRefundName.setText(obj.getString("receiver"));
                        }
                        if (mRefundPhoneNum != null) {
                            mRefundPhoneNum.setText(obj.getString("receiver_phone"));
                        }
                        mRefundProvinceVal = obj.getString("receiver_province");
                        mRefundCityVal = obj.getString("receiver_city");
                        mRefundZoneVal = obj.getString("receiver_area");
                        if (mRefundArea != null) {
                            mRefundArea.setText(StringUtil.contact(mRefundProvinceVal, " ", mRefundCityVal, " ", mRefundZoneVal));
                        }
                        if (mRefundAddress != null) {
                            mRefundAddress.setText(obj.getString("receiver_address"));
                        }
                        mImgLicenseUrl = obj.getString("certificate");
                        mImgOtherUrl = obj.getString("other");
                        if (mImgLicense != null) {
                            ImgLoader.display(mContext, obj.getString("certificate_format"), mImgLicense);
                        }
                        if (mImgOther != null) {
                            ImgLoader.display(mContext, obj.getString("other_format"), mImgOther);
                        }
                    }
                }
            });
        }

    }

    public void shopApplyClick(View v) {
        int i = v.getId();
        if (i == R.id.btn_manage_class) {//经营类目
            chooseManageClass();
        } else if (i == R.id.btn_manage_area) {//经营者所在地区
            mTargetArea = mManageArea;
            chooseCity();
        } else if (i == R.id.btn_kefu_info) {//客服信息
            toggleShow(mKefuPhoneNum, mIndicatorKefu);
        } else if (i == R.id.btn_refund_info) {//退货信息
            toggleShow(mRefundGroup, mIndicatorRefund);
        } else if (i == R.id.btn_refund_area) {//退货人所在地区
            mTargetArea = mRefundArea;
            chooseCity();
        } else if (i == R.id.btn_img_license) {//营业执照
            mTargetImageView = mImgLicense;
            chooseImage();
        } else if (i == R.id.btn_img_other) {//其他证件
            mTargetImageView = mImgOther;
            chooseImage();
        } else if (i == R.id.btn_bond) {//缴纳保证金
            setBond();
        } else if (i == R.id.btn_submit) {//提交
            submit();
        }
    }

    /**
     * 客服信息 退货信息 隐藏显示切换
     */
    private void toggleShow(View view, View indicator) {
        if (view.getVisibility() == View.VISIBLE) {
            view.setVisibility(View.GONE);
            indicator.setRotation(90);
        } else {
            view.setVisibility(View.VISIBLE);
            indicator.setRotation(270);
        }
    }

    /**
     * 选择经营类目
     */
    private void chooseManageClass() {
        Intent intent = new Intent(mContext, ManageClassActivity.class);
        if (mManageClassList != null && mManageClassList.size() > 0) {
            ArrayList<String> list = new ArrayList<>();
            for (ManageClassBean bean : mManageClassList) {
                list.add(bean.getId());
            }
            intent.putStringArrayListExtra(Constants.MALL_APPLY_MANAGE_CLASS, list);
        }
        ActivityResultUtil.startActivityForResult(this, intent, new ActivityResultCallback() {
            @Override
            public void onSuccess(Intent intent) {
                if (intent != null) {
                    mManageClassList = intent.getParcelableArrayListExtra(Constants.MALL_APPLY_MANAGE_CLASS);
                    if (mManageClassName != null) {
                        mManageClassName.setText(R.string.mall_045);
                    }
                }
            }
        });
    }


    /**
     * 选择图片
     */
    private void chooseImage() {
        DialogUitl.showStringArrayDialog(mContext, new Integer[]{R.string.alumb, R.string.camera}, new DialogUitl.StringArrayDialogCallback() {
            @Override
            public void onItemClick(String text, int tag) {
                if (tag == R.string.camera) {
                    MediaUtil.getImageByCamera(ShopApplyActivity.this, false, mImageResultCallback);
                } else if (tag == R.string.alumb) {
                    MediaUtil.getImageByAlumb(ShopApplyActivity.this, false, mImageResultCallback);
                }
            }
        });
    }


    /**
     * 缴纳保证金
     */
    private void setBond() {
        MallHttpUtil.getBondStatus(new HttpCallback() {
            @Override
            public void onSuccess(int code, String msg, String[] info) {
                if (code == 0 && info.length > 0) {
                    JSONObject obj = JSON.parseObject(info[0]);
                    if (obj.getIntValue("bond_status") == 0) {
                        Intent intent = new Intent(mContext, ShopApplyBondActivity.class);
                        intent.putExtra(Constants.MALL_APPLY_BOND, obj.getString("shop_bond"));
                        startActivity(intent);
                    } else {
                        ToastUtil.show(R.string.mall_054);
                    }
                }
            }
        });
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
                if (mTargetArea != null) {
                    String provinceName = province.getAreaName();
                    String cityName = city.getAreaName();
                    String zoneName = county.getAreaName();
                    mTargetArea.setText(StringUtil.contact(provinceName, " ", cityName, " ", zoneName));
                    if (mTargetArea == mManageArea) {
                        mProvinceVal = provinceName;
                        mCityVal = cityName;
                        mZoneVal = zoneName;
                    } else if (mTargetArea == mRefundArea) {
                        mRefundProvinceVal = provinceName;
                        mRefundCityVal = cityName;
                        mRefundZoneVal = zoneName;
                    }
                }
            }
        });
    }

    /**
     * 提交表单
     */
    private void submit() {
        //姓名
        final String name = mName.getText().toString().trim();
        if (TextUtils.isEmpty(name)) {
            ToastUtil.show(R.string.mall_055);
            return;
        }
        //身份证号
        final String cardNo = mCardNo.getText().toString().trim();
        if (TextUtils.isEmpty(cardNo)) {
            ToastUtil.show(R.string.mall_056);
            return;
        }
        //经营类目
        if (mManageClassList == null || mManageClassList.size() == 0) {
            ToastUtil.show(R.string.mall_044);
            return;
        }
        //经营者名字
        final String manageName = mManageName.getText().toString().trim();
        if (TextUtils.isEmpty(manageName)) {
            ToastUtil.show(R.string.mall_057);
            return;
        }
        //经营者手机号
        final String managePhoneNum = mManagePhoneNum.getText().toString().trim();
        if (TextUtils.isEmpty(managePhoneNum)) {
            ToastUtil.show(R.string.mall_058);
            return;
        }
        //经营者所在地区
        final String manageArea = mManageArea.getText().toString().trim();
        if (TextUtils.isEmpty(manageArea)) {
            ToastUtil.show(R.string.mall_059);
            return;
        }
        //经营者详细地址
        final String manageAddress = mManageAddress.getText().toString().trim();
        if (TextUtils.isEmpty(manageAddress)) {
            ToastUtil.show(R.string.mall_060);
            return;
        }
        //营业执照图片
        if (mImgLicenseFile == null && TextUtils.isEmpty(mImgLicenseUrl)) {
            ToastUtil.show(R.string.mall_061);
            return;
        }
        //经营类目
        StringBuilder sb = new StringBuilder();
        for (ManageClassBean bean : mManageClassList) {
            sb.append(bean.getId());
            sb.append(",");
        }
        String manageClassId = sb.toString();
        if (manageClassId.length() > 0) {
            manageClassId = manageClassId.substring(0, manageClassId.length() - 1);
        }
        final String manageClassIdFinal = manageClassId;
        //客服电话
        String kefuPhoneNum = mKefuPhoneNum.getText().toString().trim();
        if (TextUtils.isEmpty(kefuPhoneNum)) {
            kefuPhoneNum = managePhoneNum;
        }
        final String kefuPhoneNumFinal = kefuPhoneNum;
        //退货人名字
        String refundName = mRefundName.getText().toString().trim();
        if (TextUtils.isEmpty(refundName)) {
            refundName = manageName;
        }
        final String refundNameFinal = refundName;
        //退货人手机号
        String refundPhoneNum = mRefundPhoneNum.getText().toString().trim();
        if (TextUtils.isEmpty(refundPhoneNum)) {
            refundPhoneNum = managePhoneNum;
        }
        final String refundPhoneNumFinal = refundPhoneNum;
        //退货人所在省
        String refundProvince = mRefundProvinceVal;
        if (TextUtils.isEmpty(refundProvince)) {
            refundProvince = mProvinceVal;
        }
        final String refundProvinceFinal = refundProvince;
        //退货人所在市
        String refundCity = mRefundCityVal;
        if (TextUtils.isEmpty(refundCity)) {
            refundCity = mCityVal;
        }
        final String refundCityFinal = refundCity;
        //退货人所在区
        String refundZone = mRefundZoneVal;
        if (TextUtils.isEmpty(refundZone)) {
            refundZone = mZoneVal;
        }
        final String refundZoneFinal = refundZone;
        //退货人详细地址
        String refundAddress = mRefundAddress.getText().toString().trim();
        if (TextUtils.isEmpty(refundAddress)) {
            refundAddress = manageAddress;
        }
        final String refundAddressFinal = refundAddress;

        //检查是否缴纳了保证金
        MallHttpUtil.getBondStatus(new HttpCallback() {
            @Override
            public void onSuccess(int code, String msg, String[] info) {
                if (code == 0 && info.length > 0) {
                    JSONObject obj = JSON.parseObject(info[0]);
                    if (obj.getIntValue("bond_status") == 0) {
                        ToastUtil.show(R.string.mall_062);
                    } else {
                        if (mImgLicenseFile != null || mImgOtherFile != null) {
                            final List<UploadBean> uploadList = new ArrayList<>();
                            if (mImgLicenseFile != null) {
                                uploadList.add(new UploadBean(mImgLicenseFile, UploadBean.IMG));
                            }
                            if (mImgOtherFile != null) {
                                uploadList.add(new UploadBean(mImgOtherFile, UploadBean.IMG));
                            }
                            //上传图片开始
                            showLoading();
                            UploadUtil.startUpload(new CommonCallback<UploadStrategy>() {
                                @Override
                                public void callback(UploadStrategy strategy) {
                                    strategy.upload(uploadList, true, new UploadCallback() {
                                        @Override
                                        public void onFinish(List<UploadBean> list, boolean success) {
                                            if (mImgLicenseFile != null) {
                                                mImgLicenseUrl = list.get(0).getRemoteFileName();
                                                if (list.size() > 1) {
                                                    mImgOtherUrl = list.get(1).getRemoteFileName();
                                                }
                                            } else {
                                                mImgOtherUrl = list.get(0).getRemoteFileName();
                                            }
                                            //上传图片结束
                                            MallHttpUtil.applyShop(
                                                    name,
                                                    cardNo,
                                                    manageClassIdFinal,
                                                    manageName,
                                                    managePhoneNum,
                                                    mProvinceVal,
                                                    mCityVal,
                                                    mZoneVal,
                                                    manageAddress,
                                                    kefuPhoneNumFinal,
                                                    refundNameFinal,
                                                    refundPhoneNumFinal,
                                                    refundProvinceFinal,
                                                    refundCityFinal,
                                                    refundZoneFinal,
                                                    refundAddressFinal,
                                                    mImgLicenseUrl,
                                                    mImgOtherUrl,
                                                    new HttpCallback() {
                                                        @Override
                                                        public void onSuccess(int code, String msg, String[] info) {
                                                            hideLoading();
                                                            if (code == 0) {
                                                                finish();
                                                            }
                                                            ToastUtil.show(msg);
                                                        }
                                                    }
                                            );


                                        }
                                    });
                                }
                            });

                        } else {
                            MallHttpUtil.applyShop(
                                    name,
                                    cardNo,
                                    manageClassIdFinal,
                                    manageName,
                                    managePhoneNum,
                                    mProvinceVal,
                                    mCityVal,
                                    mZoneVal,
                                    manageAddress,
                                    kefuPhoneNumFinal,
                                    refundNameFinal,
                                    refundPhoneNumFinal,
                                    refundProvinceFinal,
                                    refundCityFinal,
                                    refundZoneFinal,
                                    refundAddressFinal,
                                    mImgLicenseUrl,
                                    mImgOtherUrl,
                                    new HttpCallback() {
                                        @Override
                                        public void onSuccess(int code, String msg, String[] info) {
                                            hideLoading();
                                            if (code == 0) {
                                                finish();
                                            }
                                            ToastUtil.show(msg);
                                        }
                                    }
                            );
                        }
                    }
                }
            }
        });
    }


    private void showLoading() {
        if (mLoading == null) {
            mLoading = DialogUitl.loadingDialog(mContext, WordUtil.getString(R.string.video_pub_ing));
        }
        if (!mLoading.isShowing()) {
            mLoading.show();
        }
    }


    private void hideLoading() {
        if (mLoading != null && mLoading.isShowing()) {
            mLoading.dismiss();
        }
    }

    @Override
    protected void onDestroy() {
        MallHttpUtil.cancel(MallHttpConsts.GET_BOND_STATUS);
        MallHttpUtil.cancel(MallHttpConsts.APPLY_SHOP);
        MallHttpUtil.cancel(MallHttpConsts.GET_SHOP_APPLY_INFO);
        UploadUtil.cancelUpload();
        hideLoading();
        mLoading = null;
        super.onDestroy();
    }
}
