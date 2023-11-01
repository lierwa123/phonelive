package com.yunbao.mall.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
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
import com.yunbao.common.utils.ToastUtil;
import com.yunbao.common.utils.WordUtil;
import com.yunbao.mall.R;
import com.yunbao.mall.http.MallHttpConsts;
import com.yunbao.mall.http.MallHttpUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * 添加站外商品
 */
public class GoodsAddOutSideActivity extends AbsActivity implements View.OnClickListener {

    public static void forward(Context context, String goodsId) {
        Intent intent = new Intent(context, GoodsAddOutSideActivity.class);
        intent.putExtra(Constants.MALL_GOODS_ID, goodsId);
        context.startActivity(intent);
    }


    private EditText mLink;
    private EditText mName;
    private EditText mPriceOrigin;
    private EditText mPriceNow;
    private EditText mDes;
    //    private View mBtnImgDel;
    private ImageView mImg;
    private File mImgFile;
    private View mBtnConfirm;
    private Dialog mLoading;
    private String mGoodsId;
    private String mImgUrl;
    private ImageResultCallback mImageResultCallback = new ImageResultCallback() {
        @Override
        public void beforeCamera() {

        }

        @Override
        public void onSuccess(File file) {
            if (file != null && file.exists()) {
                mImgFile = file;
                mImgUrl = null;
                if (mImg != null) {
                    ImgLoader.display(mContext, file, mImg);
                }
                setSubmitEnable();
            }
        }


        @Override
        public void onFailure() {
        }
    };


    @Override
    protected int getLayoutId() {
        return R.layout.activity_goods_out_side;
    }


    @Override
    protected void main() {
        setTitle(WordUtil.getString(R.string.mall_375));
        mLink = (EditText) findViewById(R.id.link);
        mName = (EditText) findViewById(R.id.name);
        mPriceOrigin = (EditText) findViewById(R.id.price_origin);
        mPriceNow = (EditText) findViewById(R.id.price_now);
        mDes = (EditText) findViewById(R.id.des);
        mImg = (ImageView) findViewById(R.id.img);
        mBtnConfirm = findViewById(R.id.btn_confirm);
        mBtnConfirm.setOnClickListener(this);
        findViewById(R.id.btn_img_add).setOnClickListener(this);
        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                setSubmitEnable();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        };
        mLink.addTextChangedListener(textWatcher);
        mName.addTextChangedListener(textWatcher);
        mPriceOrigin.addTextChangedListener(textWatcher);
        mPriceNow.addTextChangedListener(textWatcher);
        mDes.addTextChangedListener(textWatcher);
        mGoodsId = getIntent().getStringExtra(Constants.MALL_GOODS_ID);
        if (!TextUtils.isEmpty(mGoodsId)) {
            getGoodsInfo();
        }
    }


    /**
     * 获取商品详情，展示数据
     */
    private void getGoodsInfo() {
        MallHttpUtil.getGoodsInfo(mGoodsId, new HttpCallback() {
            @Override
            public void onSuccess(int code, String msg, String[] info) {
                if (code == 0 && info.length > 0) {
                    JSONObject obj = JSON.parseObject(info[0]);
                    JSONObject goodsInfo = obj.getJSONObject("goods_info");
                    if (mLink != null) {
                        mLink.setText(goodsInfo.getString("href"));
                    }
                    if (mName != null) {
                        mName.setText(goodsInfo.getString("name"));
                    }
                    if (mDes != null) {
                        mDes.setText(goodsInfo.getString("goods_desc"));
                    }
                    if (mPriceOrigin != null) {
                        mPriceOrigin.setText(goodsInfo.getString("original_price"));
                    }
                    if (mPriceNow != null) {
                        mPriceNow.setText(goodsInfo.getString("present_price"));
                    }
                    mImgUrl = goodsInfo.getString("thumbs");
                    JSONArray array = goodsInfo.getJSONArray("thumbs_format");
                    if (array != null && array.size() > 0) {
                        String thumb = array.getString(0);
                        if (mImg != null) {
                            ImgLoader.display(mContext, thumb, mImg);
                        }
//                        if (mBtnImgDel != null && mBtnImgDel.getVisibility() != View.VISIBLE) {
//                            mBtnImgDel.setVisibility(View.VISIBLE);
//                        }
                        setSubmitEnable();
                    }
                }
            }
        });
    }


    /**
     * 添加图片
     */
    private void addImage() {
        DialogUitl.showStringArrayDialog(mContext, new Integer[]{R.string.alumb, R.string.camera}, new DialogUitl.StringArrayDialogCallback() {
            @Override
            public void onItemClick(String text, int tag) {
                if (tag == R.string.camera) {
                    MediaUtil.getImageByCamera(GoodsAddOutSideActivity.this, false, mImageResultCallback);
                } else if (tag == R.string.alumb) {
                    MediaUtil.getImageByAlumb(GoodsAddOutSideActivity.this, false, mImageResultCallback);
                }
            }
        });
    }

    /**
     * 删除图片
     */
    private void deleteImage() {
        if (mImg != null) {
            mImg.setImageDrawable(null);
        }
        mImgUrl = null;
        mImgFile = null;
//        if (mBtnImgDel != null && mBtnImgDel.getVisibility() == View.VISIBLE) {
//            mBtnImgDel.setVisibility(View.INVISIBLE);
//        }
        setSubmitEnable();
    }


    private void setSubmitEnable() {
        if (mBtnConfirm != null) {
            String link = mLink.getText().toString().trim();
            String name = mName.getText().toString().trim();
            String priceNow = mPriceNow.getText().toString().trim();
            String priceOrigin = mPriceOrigin.getText().toString().trim();
            String des = mDes.getText().toString().trim();
            mBtnConfirm.setEnabled(!TextUtils.isEmpty(link)
                    && !TextUtils.isEmpty(name)
                    && !TextUtils.isEmpty(priceNow)
                    && !TextUtils.isEmpty(priceOrigin)
                    && !TextUtils.isEmpty(des)
                    && (mImgFile != null || !TextUtils.isEmpty(mImgUrl))
            );
        }
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.btn_img_add) {
            addImage();
        }
//        else if (i == R.id.btn_img_del) {
//            deleteImage();
//        }
        else if (i == R.id.btn_confirm) {
            submit();
        }
    }

    private void submit() {
        final String link = mLink.getText().toString().trim();
        final String name = mName.getText().toString().trim();
        final String priceOrigin = mPriceOrigin.getText().toString().trim();
        final String priceNow = mPriceNow.getText().toString().trim();
        final String des = mDes.getText().toString().trim();
        if (mImgFile != null && mImgFile.exists()) {
            final List<UploadBean> list = new ArrayList<>();
            list.add(new UploadBean(mImgFile, UploadBean.IMG));
            showLoading();
            UploadUtil.startUpload(new CommonCallback<UploadStrategy>() {
                @Override
                public void callback(UploadStrategy strategy) {
                    strategy.upload(list, true, new UploadCallback() {
                        @Override
                        public void onFinish(List<UploadBean> list, boolean success) {
                            if (!success) {
                                hideLoading();
                                return;
                            }
                            if (list != null && list.size() > 0) {
                                String remoteFileName = list.get(0).getRemoteFileName();
                                MallHttpUtil.setOutsideGoods(mGoodsId, link, name, priceOrigin, priceNow, des, remoteFileName, new HttpCallback() {
                                    @Override
                                    public void onSuccess(int code, String msg, String[] info) {
                                        if (code == 0) {
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
                        }
                    });
                }
            });

        } else {
            if (TextUtils.isEmpty(mImgUrl)) {
                if (TextUtils.isEmpty(mGoodsId)) {
                    ToastUtil.show(R.string.mall_108);
                }
            } else {
                MallHttpUtil.setOutsideGoods(mGoodsId, link, name, priceOrigin, priceNow, des, mImgUrl, new HttpCallback() {
                    @Override
                    public void onSuccess(int code, String msg, String[] info) {
                        if (code == 0) {
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
        }

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
    public void onDestroy() {
        MallHttpUtil.cancel(MallHttpConsts.GET_GOODS_INFO);
        MallHttpUtil.cancel(MallHttpConsts.SET_OUTSIDE_GOODS);
        hideLoading();
        super.onDestroy();
    }


}
