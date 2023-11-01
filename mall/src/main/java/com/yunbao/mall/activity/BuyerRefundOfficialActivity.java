package com.yunbao.mall.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.yunbao.common.Constants;
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
import com.yunbao.common.utils.StringUtil;
import com.yunbao.common.utils.ToastUtil;
import com.yunbao.common.utils.WordUtil;
import com.yunbao.mall.R;
import com.yunbao.mall.bean.RefundReasonBean;
import com.yunbao.mall.dialog.OfficialRefundReasonDialogFragment;
import com.yunbao.mall.http.MallHttpConsts;
import com.yunbao.mall.http.MallHttpUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 退款 平台介入
 */
public class BuyerRefundOfficialActivity extends AbsActivity implements View.OnClickListener {

    public static void forward(Context context, String orderId) {
        Intent intent = new Intent(context, BuyerRefundOfficialActivity.class);
        intent.putExtra(Constants.MALL_ORDER_ID, orderId);
        ((Activity) context).startActivityForResult(intent, 0);
    }

    private String mOrderId;
    private TextView mReason;
    private EditText mEditText;
    private TextView mCount;
    private ImageView mImg;
    private View mBtnDel;
    private File mImgFile;
    private Dialog mLoading;
    private List<RefundReasonBean> mReasonList;
    private String mReasonId;
    private ImageResultCallback mImageResultCallback = new ImageResultCallback() {
        @Override
        public void beforeCamera() {

        }

        @Override
        public void onSuccess(File file) {
            if (file != null && file.exists()) {
                mImgFile = file;
                if (mImg != null) {
                    ImgLoader.display(mContext, file, mImg);
                }
                if (mBtnDel != null && mBtnDel.getVisibility() != View.VISIBLE) {
                    mBtnDel.setVisibility(View.VISIBLE);
                }
            } else {
                ToastUtil.show(R.string.file_not_exist);
            }
        }


        @Override
        public void onFailure() {
        }
    };

    @Override
    protected int getLayoutId() {
        return R.layout.activity_buyer_refund_official;
    }


    @Override
    protected void main() {
        setTitle(WordUtil.getString(R.string.mall_286));
        mOrderId = getIntent().getStringExtra(Constants.MALL_ORDER_ID);
        mReason = findViewById(R.id.reason);
        mEditText = findViewById(R.id.edit);
        mCount = findViewById(R.id.count);
        mEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (mCount != null) {
                    mCount.setText(StringUtil.contact(String.valueOf(s.length()), "/300"));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        mCount = findViewById(R.id.count);
        mImg = findViewById(R.id.img);
        mBtnDel = findViewById(R.id.btn_del);
        findViewById(R.id.btn_reason).setOnClickListener(this);
        findViewById(R.id.btn_submit).setOnClickListener(this);
        findViewById(R.id.btn_add_img).setOnClickListener(this);
        mBtnDel.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.btn_reason) {
            chooseRejectReason();
        } else if (id == R.id.btn_submit) {
            submit();
        } else if (id == R.id.btn_add_img) {
            chooseImage();
        } else if (id == R.id.btn_del) {
            deleteImgFile();
        }


    }

    /**
     * 删除图片
     */
    private void deleteImgFile() {
        mImgFile = null;
        if (mImg != null) {
            mImg.setImageDrawable(null);
        }
        if (mBtnDel != null && mBtnDel.getVisibility() == View.VISIBLE) {
            mBtnDel.setVisibility(View.INVISIBLE);
        }
    }


    /**
     * 选择图片
     */
    private void chooseImage() {
        if (mImgFile != null) {
            return;
        }
        DialogUitl.showStringArrayDialog(mContext, new Integer[]{R.string.alumb, R.string.camera}, new DialogUitl.StringArrayDialogCallback() {
            @Override
            public void onItemClick(String text, int tag) {
                if (tag == R.string.camera) {
                    MediaUtil.getImageByCamera(BuyerRefundOfficialActivity.this,false,mImageResultCallback);
                } else if (tag == R.string.alumb) {
                    MediaUtil.getImageByAlumb(BuyerRefundOfficialActivity.this,false,mImageResultCallback);
                }
            }
        });
    }


    /**
     * 选择申诉原因
     */
    private void chooseRejectReason() {
        if (mReasonList == null) {
            MallHttpUtil.getOfficialRefundReason(new HttpCallback() {
                @Override
                public void onSuccess(int code, String msg, String[] info) {
                    if (code == 0) {
                        mReasonList = JSON.parseArray(Arrays.toString(info), RefundReasonBean.class);
                        showReasonDialog();
                    }
                }
            });
        } else {
            showReasonDialog();
        }
    }

    private void showReasonDialog() {
        OfficialRefundReasonDialogFragment fragment = new OfficialRefundReasonDialogFragment();
        fragment.setList(mReasonList);
        fragment.show(getSupportFragmentManager(), "OfficialRefundReasonDialogFragment");
    }


    public void setRefundReason(RefundReasonBean bean) {
        mReasonId = bean.getId();
        if (mReason != null) {
            mReason.setText(bean.getName());
        }
    }

    private void submit() {
        if (TextUtils.isEmpty(mReasonId)) {
            ToastUtil.show(R.string.mall_284);
            return;
        }
        if (mImgFile != null && mImgFile.exists()) {
            uploadFile();
        } else {
            doSubmit(null);
        }
    }

    private void doSubmit(String thumb) {
        String content = mEditText.getText().toString().trim();
        MallHttpUtil.buyerApplyOfficialRefund(mOrderId, mReasonId, content, thumb, new HttpCallback() {
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


    private void uploadFile() {
        final List<UploadBean> uploadList = new ArrayList<>();
        uploadList.add(new UploadBean(mImgFile, UploadBean.IMG));
        showLoading();
        UploadUtil.startUpload(new CommonCallback<UploadStrategy>() {
            @Override
            public void callback(UploadStrategy strategy) {
                strategy.upload(uploadList, true, new UploadCallback() {

                    @Override
                    public void onFinish(List<UploadBean> list, boolean success) {
                        hideLoading();
                        if (success) {
                            String thumb = list.get(0).getRemoteFileName();
                            doSubmit(thumb);
                        } else {
                            doSubmit(null);
                        }
                    }
                });
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
        MallHttpUtil.cancel(MallHttpConsts.GET_OFFICIAL_REFUND_REASON);
        MallHttpUtil.cancel(MallHttpConsts.BUYER_APPLY_OFFICIAL_REFUND);
        UploadUtil.cancelUpload();
        hideLoading();
        super.onDestroy();
    }
}
