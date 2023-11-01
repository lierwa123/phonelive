package com.yunbao.beauty.views;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.yunbao.beauty.R;
import com.meihu.beautylibrary.MHSDK;

public class MhMainViewHolder extends AbsMhChildViewHolder implements View.OnClickListener {

    private View mLlCenterContainer;
    private ImageView mIvRecord;

    private View mTieZhi;
    private View mMeiYan;
    private View mMakeup;
    private View mTeXiao;
    private View mHaHa;

    public MhMainViewHolder(Context context, ViewGroup parentView) {
        super(context, parentView);
    }

    @Override
    public void showSeekBar() {

    }

    @Override
    public void hideSeekBar() {

    }

    @Override
    protected int getLayoutId() {
        return R.layout.view_beauty_mh_main;
    }

    @Override
    public void init() {

        mLlCenterContainer = findViewById(R.id.ll_center_container);
        mIvRecord = findViewById(R.id.iv_record);

        mTieZhi = findViewById(R.id.btn_tie_zhi);
        mMeiYan = findViewById(R.id.btn_mei_yan);
        mMakeup = findViewById(R.id.btn_makeup);
        mTeXiao = findViewById(R.id.btn_te_xiao);
        mHaHa = findViewById(R.id.btn_haha);

        findViewById(R.id.btn_hide).setOnClickListener(this);
        mTieZhi.setOnClickListener(this);
        mMeiYan.setOnClickListener(this);
        mMakeup.setOnClickListener(this);
        mTeXiao.setOnClickListener(this);
        mHaHa.setOnClickListener(this);

//        if (MHSDK.isPro()){
//            mMakeup.setVisibility(View.VISIBLE);
//            mHaHa.setVisibility(View.GONE);
//        }else{
//            mMakeup.setVisibility(View.GONE);
//            mHaHa.setVisibility(View.VISIBLE);
//        }
        mMakeup.setVisibility(View.GONE);
        mHaHa.setVisibility(View.VISIBLE);

//        if (MHSDK.isSimple()){
//            mTieZhi.setVisibility(View.INVISIBLE);
//            mTeXiao.setVisibility(View.INVISIBLE);
//            mHaHa.setVisibility(View.INVISIBLE);
//            mMakeup.setVisibility(View.INVISIBLE);
//        }

    }

    @Override
    public void onClick(View v) {
        if (mIBeautyClickListener == null) {
            return;
        }
        int i = v.getId();
        if (i == R.id.btn_hide) {
            mIBeautyClickListener.hideView();
        } else if (i == R.id.btn_tie_zhi) {
            mIBeautyClickListener.tabTieZhi();
        } else if (i == R.id.btn_mei_yan) {
            mIBeautyClickListener.tabMeiYan();
        } else if (i == R.id.btn_te_xiao) {
            mIBeautyClickListener.tabTeXiao();
        } else if (i == R.id.btn_haha) {
            mIBeautyClickListener.tabHaHa();
        } else if (i == R.id.btn_makeup) {
            mIBeautyClickListener.tabMakeup();
        }
    }

    public View getCenterViewContainer(){
        return mLlCenterContainer;
    }


    public ImageView getRecordView(){
        return mIvRecord;
    }


    public void showViewContainer(boolean isShow){

        if (isShow){
            mTieZhi.setVisibility(View.VISIBLE);
            mMeiYan.setVisibility(View.VISIBLE);
            mMakeup.setVisibility(View.VISIBLE);
            mTeXiao.setVisibility(View.VISIBLE);
            mHaHa.setVisibility(View.VISIBLE);
        }else{
            mTieZhi.setVisibility(View.INVISIBLE);
            mMeiYan.setVisibility(View.INVISIBLE);
            mMakeup.setVisibility(View.INVISIBLE);
            mTeXiao.setVisibility(View.INVISIBLE);
            mHaHa.setVisibility(View.INVISIBLE);
        }

    }


}
