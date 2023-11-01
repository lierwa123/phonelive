package com.yunbao.mall.views;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.yunbao.common.glide.ImgLoader;
import com.yunbao.common.views.AbsCommonViewHolder;
import com.yunbao.mall.R;
import com.yunbao.mall.activity.PayContentPubActivity;
import com.yunbao.mall.bean.PayContentVideoBean;

/**
 * 单视频
 */
public class PayContentSingleViewHolder extends AbsCommonViewHolder implements View.OnClickListener {

    private ImageView mImg;
    private View mBtnDel;
    private PayContentVideoBean mBean;

    public PayContentSingleViewHolder(Context context, ViewGroup parentView) {
        super(context, parentView);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.view_pay_content_single;
    }

    @Override
    public void init() {
        mImg = findViewById(R.id.img);
        mBtnDel = findViewById(R.id.btn_del);
        findViewById(R.id.btn_upload).setOnClickListener(this);
        mBtnDel.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.btn_del) {
            deleteImage();
        } else if (id == R.id.btn_upload) {
            ((PayContentPubActivity) mContext).chooseVideo();
        }
    }

    public void setFilePath(String filePath, String duration) {
        if (mBean == null) {
            mBean = new PayContentVideoBean();
        }
        mBean.setFilePath(filePath);
        mBean.setDuration(duration);
        if (mImg != null) {
            ImgLoader.displayVideoThumb(mContext, filePath, mImg);
        }
        if (mBtnDel != null && mBtnDel.getVisibility() != View.VISIBLE) {
            mBtnDel.setVisibility(View.VISIBLE);
        }
    }

    private void deleteImage() {
        if (mBean != null) {
            mBean.clear();
        }
        if (mImg != null) {
            mImg.setImageDrawable(null);
        }
        if (mBtnDel != null && mBtnDel.getVisibility() == View.VISIBLE) {
            mBtnDel.setVisibility(View.INVISIBLE);
        }
    }

    public PayContentVideoBean getPayContentVideoBean(){
        return mBean;
    }

}
