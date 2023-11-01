package com.yunbao.main.activity;

import android.app.Dialog;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.yunbao.common.activity.AbsActivity;
import com.yunbao.common.glide.ImgLoader;
import com.yunbao.common.http.HttpCallback;
import com.yunbao.common.interfaces.CommonCallback;
import com.yunbao.common.interfaces.ImageResultCallback;
import com.yunbao.common.upload.UploadBean;
import com.yunbao.common.upload.UploadCallback;
import com.yunbao.common.upload.UploadStrategy;
import com.yunbao.common.upload.UploadUtil;
import com.yunbao.common.utils.DialogUitl;
import com.yunbao.common.utils.MediaUtil;
import com.yunbao.common.utils.ToastUtil;
import com.yunbao.common.utils.WordUtil;
import com.yunbao.main.R;
import com.yunbao.main.http.MainHttpConsts;
import com.yunbao.main.http.MainHttpUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * 家族申请
 */
public class FamilyApplyActivity extends AbsActivity implements View.OnClickListener {

    private EditText mFamilyName;
    private EditText mUserName;
    private EditText mCardNo;
    private EditText mChou;
    private EditText mFamilyDes;
    private ImageView mImgFront;
    private ImageView mImgBack;
    private ImageView mImgFamily;
    private File mImgFrontFile;
    private File mImgBackFile;
    private File mImgFamilyFile;
    private String mImgFrontUrl;
    private String mImgBackUrl;
    private String mImgFamilyUrl;
    private int mImgPosition;
    private Dialog mLoading;
    private ImageResultCallback mImageResultCallback = new ImageResultCallback() {
        @Override
        public void beforeCamera() {

        }

        @Override
        public void onSuccess(File file) {
            if (file != null && file.exists()) {
                if (mImgPosition == 0) {
                    mImgFrontFile = file;
                    if (mImgFront != null) {
                        ImgLoader.display(mContext, file, mImgFront);
                    }
                } else if (mImgPosition == 1) {
                    mImgBackFile = file;
                    if (mImgBack != null) {
                        ImgLoader.display(mContext, file, mImgBack);
                    }
                } else if (mImgPosition == 2) {
                    mImgFamilyFile = file;
                    if (mImgFamily != null) {
                        ImgLoader.display(mContext, file, mImgFamily);
                    }
                }
            } else {
                ToastUtil.show(com.yunbao.mall.R.string.file_not_exist);
            }
        }


        @Override
        public void onFailure() {
        }
    };

    @Override
    protected int getLayoutId() {
        return R.layout.activity_family_apply;
    }


    @Override
    protected void main() {
        setTitle(WordUtil.getString(R.string.a_064));
        mFamilyName = findViewById(R.id.family_name);
        mUserName = findViewById(R.id.user_name);
        mCardNo = findViewById(R.id.card_no);
        mChou = findViewById(R.id.chou);
        mFamilyDes = findViewById(R.id.family_des);
        mImgFront = findViewById(R.id.img_front);
        mImgBack = findViewById(R.id.img_back);
        mImgFamily = findViewById(R.id.img_family);
        findViewById(R.id.btn_img_front).setOnClickListener(this);
        findViewById(R.id.btn_img_back).setOnClickListener(this);
        findViewById(R.id.btn_img_family).setOnClickListener(this);
        findViewById(R.id.btn_submit).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.btn_img_front) {
            mImgPosition = 0;
            chooseImage();
        } else if (id == R.id.btn_img_back) {
            mImgPosition = 1;
            chooseImage();
        } else if (id == R.id.btn_img_family) {
            mImgPosition = 2;
            chooseImage();
        } else if (id == R.id.btn_submit) {
            submit();
        }
    }


    /**
     * 选择图片
     */
    private void chooseImage() {
        DialogUitl.showStringArrayDialog(mContext, new Integer[]{com.yunbao.mall.R.string.alumb, com.yunbao.mall.R.string.camera}, new DialogUitl.StringArrayDialogCallback() {
            @Override
            public void onItemClick(String text, int tag) {
                if (tag == com.yunbao.mall.R.string.camera) {
                    MediaUtil.getImageByCamera(FamilyApplyActivity.this, false, mImageResultCallback);
                } else if (tag == com.yunbao.mall.R.string.alumb) {
                    MediaUtil.getImageByAlumb(FamilyApplyActivity.this, false, mImageResultCallback);
                }
            }
        });
    }

    public void submit() {
        final String familyName = mFamilyName.getText().toString();
        if (TextUtils.isEmpty(familyName)) {
            ToastUtil.show(R.string.a_025);
            return;
        }
        final String userName = mUserName.getText().toString();
        if (TextUtils.isEmpty(userName)) {
            ToastUtil.show(R.string.a_027);
            return;
        }
        final String cardNo = mCardNo.getText().toString();
        if (TextUtils.isEmpty(cardNo)) {
            ToastUtil.show(R.string.a_012);
            return;
        }
        final String chou = mChou.getText().toString();
        if (TextUtils.isEmpty(chou)) {
            ToastUtil.show(R.string.a_035);
            return;
        }
        final String familyDes = mFamilyDes.getText().toString();
        if (TextUtils.isEmpty(familyDes)) {
            ToastUtil.show(R.string.a_036);
            return;
        }
        final List<UploadBean> uploadList = new ArrayList<>();
        if (mImgFrontFile != null) {
            UploadBean bean = new UploadBean(mImgFrontFile, UploadBean.IMG);
            bean.setTag(0);
            uploadList.add(bean);
        }
        if (mImgBackFile != null) {
            UploadBean bean = new UploadBean(mImgBackFile, UploadBean.IMG);
            bean.setTag(1);
            uploadList.add(bean);
        }
        if (mImgFamilyFile != null) {
            UploadBean bean = new UploadBean(mImgFamilyFile, UploadBean.IMG);
            bean.setTag(2);
            uploadList.add(bean);
        }
        if (mImgFrontFile == null || mImgBackFile == null || mImgFamilyFile == null) {
            ToastUtil.show(R.string.a_061);
            return;
        }
        showLoading();
        UploadUtil.startUpload(new CommonCallback<UploadStrategy>() {
            @Override
            public void callback(UploadStrategy strategy) {
                strategy.upload(uploadList, true, new UploadCallback() {
                    @Override
                    public void onFinish(List<UploadBean> list, boolean success) {
                        if (!success) {
                            hideLoading();
                            return;
                        }
                        for (UploadBean bean : list) {
                            int tag = (int) bean.getTag();
                            if (tag == 0) {
                                mImgFrontUrl = bean.getRemoteFileName();
                            } else if (tag == 1) {
                                mImgBackUrl = bean.getRemoteFileName();
                            } else if (tag == 2) {
                                mImgFamilyUrl = bean.getRemoteFileName();
                            }
                        }

                        MainHttpUtil.createFamily(familyName, userName, cardNo, chou, familyDes, mImgFrontUrl, mImgBackUrl, mImgFamilyUrl, new HttpCallback() {
                            @Override
                            public void onSuccess(int code, String msg, String[] info) {
                                if (code == 0) {
                                    setResult(RESULT_OK);
                                    finish();
                                }
                                ToastUtil.show(msg);
                            }

                            @Override
                            public void onFinish() {
                                hideLoading();
                            }
                        });
                    }
                });
            }
        });

    }


    @Override
    protected void onDestroy() {
        MainHttpUtil.cancel(MainHttpConsts.SET_AUTH);
        UploadUtil.cancelUpload();
        hideLoading();
        super.onDestroy();
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
}
