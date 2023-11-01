package com.yunbao.mall.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
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
import com.yunbao.common.utils.DialogUitl;
import com.yunbao.common.utils.MediaUtil;
import com.yunbao.common.utils.ToastUtil;
import com.yunbao.common.utils.WordUtil;
import com.yunbao.mall.R;
import com.yunbao.mall.bean.PayContentVideoBean;
import com.yunbao.mall.http.MallHttpConsts;
import com.yunbao.mall.http.MallHttpUtil;
import com.yunbao.mall.views.PayContentChooseVideoViewHolder;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class PayContentPubActivity extends AbsActivity implements View.OnClickListener {

    public static void forward(Context context) {
        context.startActivity(new Intent(context, PayContentPubActivity.class));
    }

    private TextView mContentClassName;//内容类别
    private EditText mContentTitle;//内容标题
    private EditText mContentDes;//内容简介
    private EditText mUserIntro;//个人介绍
    private EditText mPrice;//内容价格
    private ImageView mCoverImg;//封面图
    private View mBtnDelCoverImg;//封面图删除按钮
    private TextView mVideoCountTip;//视频数量提示
    private TextView mVideoCount;//视频数量
    private String mClassId;//内容类别Id
    private File mCoverImageFile;
    private List<PayContentVideoBean> mVideoList;
    private Dialog mLoading;
    private PayContentChooseVideoViewHolder mChooseVideoViewHolder;
    private ImageResultCallback mImageResultCallback = new ImageResultCallback() {
        @Override
        public void beforeCamera() {

        }

        @Override
        public void onSuccess(File file) {
            if (file != null && file.exists()) {
                mCoverImageFile = file;
                if (mCoverImg != null) {
                    ImgLoader.display(mContext, file, mCoverImg);
                }
                if (mBtnDelCoverImg != null && mBtnDelCoverImg.getVisibility() != View.VISIBLE) {
                    mBtnDelCoverImg.setVisibility(View.VISIBLE);
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
        return R.layout.activity_pay_content_pub;
    }



    @Override
    protected void main() {
        setTitle(WordUtil.getString(R.string.mall_315));
        mContentClassName = findViewById(R.id.content_class_name);//内容类别
        mContentTitle = findViewById(R.id.content_title);//内容标题
        mContentDes = findViewById(R.id.content_des);//内容简介
        mUserIntro = findViewById(R.id.user_intro);//个人介绍
        mPrice = findViewById(R.id.price);//内容价格
        mCoverImg = findViewById(R.id.cover_img);//封面图
        mBtnDelCoverImg = findViewById(R.id.btn_del_cover_img);//封面图删除按钮
        mVideoCountTip = findViewById(R.id.video_count_tip);//视频数量提示
        mVideoCount = findViewById(R.id.video_count);//视频数量
        findViewById(R.id.btn_content_class).setOnClickListener(this);
        findViewById(R.id.btn_cover_img).setOnClickListener(this);
        mBtnDelCoverImg.setOnClickListener(this);
        findViewById(R.id.btn_choose_video).setOnClickListener(this);
        findViewById(R.id.btn_submit).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.btn_content_class) {
            chooseClass();
        } else if (id == R.id.btn_cover_img) {
            chooseImage();
        } else if (id == R.id.btn_del_cover_img) {
            delCoverImage();
        } else if (id == R.id.btn_choose_video) {
            openChooseVideo();
        } else if (id == R.id.btn_submit) {
            submit();
        }
    }

    /**
     * 选择分类
     */
    private void chooseClass() {
        Intent i = new Intent(mContext, PayContentClassActivity.class);
        i.putExtra(Constants.CLASS_ID, mClassId);
        ActivityResultUtil.startActivityForResult(PayContentPubActivity.this, i, new ActivityResultCallback() {
            @Override
            public void onSuccess(Intent intent) {
                if (intent != null) {
                    mClassId = intent.getStringExtra(Constants.CLASS_ID);
                    if (mContentClassName != null) {
                        mContentClassName.setText(intent.getStringExtra(Constants.CLASS_NAME));
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
                    MediaUtil.getImageByCamera(PayContentPubActivity.this,false,mImageResultCallback);
                } else if (tag == R.string.alumb) {
                    MediaUtil.getImageByAlumb(PayContentPubActivity.this,false,mImageResultCallback);
                }
            }
        });
    }

    /**
     * 删除封面图片
     */
    private void delCoverImage() {
        mCoverImageFile = null;
        if (mCoverImg != null) {
            mCoverImg.setImageDrawable(null);
        }
        if (mBtnDelCoverImg != null && mBtnDelCoverImg.getVisibility() == View.VISIBLE) {
            mBtnDelCoverImg.setVisibility(View.INVISIBLE);
        }
    }

    /**
     * 选择视频
     */
    private void openChooseVideo() {
        if (mChooseVideoViewHolder == null) {
            mChooseVideoViewHolder = new PayContentChooseVideoViewHolder(mContext, (ViewGroup) findViewById(R.id.root));
            mChooseVideoViewHolder.subscribeActivityLifeCycle();
            mChooseVideoViewHolder.addToParent();
        }
        mChooseVideoViewHolder.show();

    }

    public void chooseVideo() {
        if (mChooseVideoViewHolder != null) {
            mChooseVideoViewHolder.chooseVideo();
        }
    }

    public void setVideoList(PayContentVideoBean bean) {
        if (mVideoList == null) {
            mVideoList = new ArrayList<>();
        }
        mVideoList.clear();
        mVideoList.add(bean);
        mVideoCount.setText("1");
        mVideoCountTip.setText(R.string.mall_327);
    }

    public void setVideoList(List<PayContentVideoBean> list) {
        if (mVideoList == null) {
            mVideoList = new ArrayList<>();
        }
        mVideoList.clear();
        mVideoList.addAll(list);
        mVideoCount.setText(String.valueOf(mVideoList.size()));
        mVideoCountTip.setText(R.string.mall_328);
    }

    @Override
    public void onBackPressed() {
        if (mChooseVideoViewHolder != null && mChooseVideoViewHolder.isShowed()) {
            mChooseVideoViewHolder.hide();
            return;
        }
        super.onBackPressed();
    }

    private void submit() {
        if (TextUtils.isEmpty(mClassId)) {
            ToastUtil.show(R.string.mall_335);
            return;
        }
        final String title = mContentTitle.getText().toString().trim();
        if (TextUtils.isEmpty(title)) {
            ToastUtil.show(R.string.mall_336);
            return;
        }
        final String des = mContentDes.getText().toString().trim();
        if (TextUtils.isEmpty(des)) {
            ToastUtil.show(R.string.mall_337);
            return;
        }
        final String userIntro = mUserIntro.getText().toString().trim();
        if (TextUtils.isEmpty(userIntro)) {
            ToastUtil.show(R.string.mall_338);
            return;
        }
        final String price = mPrice.getText().toString().trim();
        if (TextUtils.isEmpty(price)) {
            ToastUtil.show(R.string.mall_339);
            return;
        }
        if (mCoverImageFile == null || !mCoverImageFile.exists()) {
            ToastUtil.show(R.string.mall_340);
            return;
        }
        if (mVideoList == null || mVideoList.size() == 0) {
            ToastUtil.show(R.string.mall_333);
            return;
        }
        final List<UploadBean> uploadList = new ArrayList<>();
        uploadList.add(new UploadBean(mCoverImageFile, UploadBean.IMG));
        for (PayContentVideoBean bean : mVideoList) {
            UploadBean uploadBean = new UploadBean(new File(bean.getFilePath()), UploadBean.VIDEO);
            uploadBean.setTag(bean);
            uploadList.add(uploadBean);
        }
        showLoading();
        UploadUtil.startUpload(new CommonCallback<UploadStrategy>() {
            @Override
            public void callback(UploadStrategy strategy) {
                strategy.upload(uploadList, true, new UploadCallback() {

                    @Override
                    public void onFinish(List<UploadBean> list, boolean success) {
                        hideLoading();
                        if (!success) {
                            return;
                        }
                        String thumb = null;
                        for (UploadBean bean : list) {
                            if (bean.getType() == UploadBean.IMG) {
                                thumb = bean.getRemoteFileName();
                            } else {
                                PayContentVideoBean payContentVideoBean = (PayContentVideoBean) bean.getTag();
                                payContentVideoBean.setUploadResultUrl(bean.getRemoteFileName());
                            }
                        }

                        JSONArray array = new JSONArray();
                        for (int i = 0, size = mVideoList.size(); i < size; i++) {
                            PayContentVideoBean bean = mVideoList.get(i);
                            JSONObject obj = new JSONObject();
                            obj.put("video_id", String.valueOf(i + 1));
                            obj.put("video_url", bean.getUploadResultUrl());
                            obj.put("video_title", bean.getTitle());
                            obj.put("is_see", bean.getIsSee());
                            obj.put("video_length", bean.getDuration());
                            array.add(obj);
                        }
                        String videoJson = array.toJSONString();
//                L.e("videoJson----------> " + videoJson);
                        MallHttpUtil.publishPayContent(mClassId, title, des, userIntro, price, thumb, videoJson, mVideoList.size() > 1 ? 1 : 0, new HttpCallback() {
                            @Override
                            public void onSuccess(int code, String msg, String[] info) {
                                if (code == 0) {
                                    finish();
                                }
                                ToastUtil.show(msg);
                            }
                        });
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
        MallHttpUtil.cancel(MallHttpConsts.PUBLISH_PAY_CONTENT);
        hideLoading();
        super.onDestroy();
    }
}
