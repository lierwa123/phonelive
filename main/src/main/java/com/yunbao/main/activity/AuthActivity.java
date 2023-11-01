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
import com.yunbao.main.dialog.AuthExampleDialogFragment;
import com.yunbao.main.http.MainHttpConsts;
import com.yunbao.main.http.MainHttpUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class AuthActivity extends AbsActivity implements View.OnClickListener {

    private EditText mName;
    private EditText mPhone;
    private EditText mCardNo;
    private ImageView mImgFront;
    private ImageView mImgBack;
    private ImageView mImgFrontHand;
    private File mImgFrontFile;
    private File mImgBackFile;
    private File mImgFrontHandFile;
    private String mImgFrontUrl;
    private String mImgBackUrl;
    private String mImgFrontHandUrl;
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
                    mImgFrontHandFile = file;
                    if (mImgFrontHand != null) {
                        ImgLoader.display(mContext, file, mImgFrontHand);
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
        return R.layout.activity_auth;
    }


    @Override
    protected void main() {
        setTitle(WordUtil.getString(R.string.a_019));
        mName = findViewById(R.id.name);
        mPhone = findViewById(R.id.phone);
        mCardNo = findViewById(R.id.card_no);
        mImgFront = findViewById(R.id.img_front);
        mImgBack = findViewById(R.id.img_back);
        mImgFrontHand = findViewById(R.id.img_front_hand);
        findViewById(R.id.btn_img_front).setOnClickListener(this);
        findViewById(R.id.btn_img_back).setOnClickListener(this);
        findViewById(R.id.btn_img_front_hand).setOnClickListener(this);
        findViewById(R.id.btn_submit).setOnClickListener(this);
        findViewById(R.id.btn_example).setOnClickListener(this);
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
        } else if (id == R.id.btn_img_front_hand) {
            mImgPosition = 2;
            chooseImage();
        } else if (id == R.id.btn_submit) {
            submit();
        } else if (id == R.id.btn_example) {
            AuthExampleDialogFragment fragment = new AuthExampleDialogFragment();
            fragment.show(getSupportFragmentManager(), "AuthExampleDialogFragment");
        }
    }


    /**
     * 选择图片
     */
    private void chooseImage() {
        DialogUitl.showStringArrayDialog(mContext, new Integer[]{com.yunbao.mall.R.string.alumb, com.yunbao.mall.R.string.camera}, new DialogUitl.StringArrayDialogCallback() {
            @Override
            public void onItemClick(String text, int tag) {
                if (tag == R.string.camera) {
                    MediaUtil.getImageByCamera(AuthActivity.this, false, mImageResultCallback);
                } else if (tag == R.string.alumb) {
                    MediaUtil.getImageByAlumb(AuthActivity.this, false, mImageResultCallback);
                }
            }
        });
    }

    public void submit() {
        final String name = mName.getText().toString();
        if (TextUtils.isEmpty(name)) {
            ToastUtil.show(R.string.a_008);
            return;
        }
        final String phone = mPhone.getText().toString();
        if (TextUtils.isEmpty(phone)) {
            ToastUtil.show(R.string.a_010);
            return;
        }
        final String cardNo = mCardNo.getText().toString();
        if (TextUtils.isEmpty(cardNo)) {
            ToastUtil.show(R.string.a_012);
            return;
        }
        if (mImgFrontFile == null || mImgBackFile == null || mImgFrontHandFile == null) {
            ToastUtil.show(R.string.a_061);
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
        if (mImgFrontHandFile != null) {
            UploadBean bean = new UploadBean(mImgFrontHandFile, UploadBean.IMG);
            bean.setTag(2);
            uploadList.add(bean);
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
                                mImgFrontHandUrl = bean.getRemoteFileName();
                            }
                        }

                        MainHttpUtil.setAuth(name, phone, cardNo, mImgFrontUrl, mImgBackUrl, mImgFrontHandUrl, new HttpCallback() {
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
