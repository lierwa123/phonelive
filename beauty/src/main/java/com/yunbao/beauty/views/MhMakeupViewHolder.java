package com.yunbao.beauty.views;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.yunbao.beauty.R;
import com.yunbao.beauty.adapter.MhMakeupAdapter;
import com.yunbao.beauty.bean.MakeupBean;
import com.yunbao.beauty.interfaces.OnItemClickListener;
import com.yunbao.beauty.utils.MhDataManager;
import com.meihu.beautylibrary.MHSDK;
import com.meihu.beautylibrary.manager.MHBeautyManager;

import java.util.ArrayList;
import java.util.List;

public class MhMakeupViewHolder extends AbsMhChildViewHolder implements View.OnClickListener, OnItemClickListener<MakeupBean> {

    private boolean mMakeupLipstick;
    private boolean mMakeupEyelash;
    private boolean mMakeupEyeliner;
    private boolean mMakeupEyebrow;
    private boolean mMakeupBlush;

    public MhMakeupViewHolder(Context context, ViewGroup parentView) {
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
        return R.layout.view_beauty_mh_makeup;
    }

    @Override
    public void init() {

        findViewById(R.id.btn_hide).setOnClickListener(this);

        List<MakeupBean> list = new ArrayList<>();
        list.add(new MakeupBean(R.string.beauty_mh_makeup_none, R.mipmap.ic_makeup_no_0, R.mipmap.ic_makeup_no_1, MHSDK.MAKEUP_NONE));
        list.add(new MakeupBean(R.string.beauty_mh_makeup_jiemao, R.mipmap.ic_makeup_jiemao_0, R.mipmap.ic_makeup_jiemao_1,MHSDK.MAKEUP_EYELASH));
        list.add(new MakeupBean(R.string.beauty_mh_makeup_chuncai, R.mipmap.ic_makeup_chuncai_0, R.mipmap.ic_makeup_chuncai_1,MHSDK.MAKEUP_LIPSTICK));
        list.add(new MakeupBean(R.string.beauty_mh_makeup_saihong, R.mipmap.ic_makeup_saihong_0, R.mipmap.ic_makeup_saihong_1,MHSDK.MAKEUP_BLUSH));
//        list.add(new MakeupBean(R.string.beauty_mh_makeup_yanxian, R.mipmap.ic_makeup_yanxian_0, R.mipmap.ic_makeup_yanxian_1,MHSDK.MAKEUP_EYELINER));

        RecyclerView recyclerView = findViewById(R.id.makeup_recyclerView);
        recyclerView.setLayoutManager(new GridLayoutManager(mContext, list.size(), GridLayoutManager.VERTICAL, false));
        MhMakeupAdapter adapter  = new MhMakeupAdapter(mContext, list);
        adapter.setOnItemClickListener(this);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onClick(View v) {
        if (mIBeautyClickListener == null) {
            return;
        }
        int i = v.getId();
        if (i == R.id.btn_hide) {
            mIBeautyClickListener.tabMain();
        }
    }

    private boolean getMakeupEnable(){
        return mMakeupLipstick ||  mMakeupEyelash || mMakeupEyeliner || mMakeupEyebrow || mMakeupBlush;
    }

    private void setMakeupEnable(MakeupBean bean){
        boolean enable = bean.isChecked();
        switch (bean.getMakeupId()){
            case MHSDK.MAKEUP_NONE:
                mMakeupLipstick = false;
                mMakeupEyelash = false;
                mMakeupEyeliner = false;
                mMakeupEyebrow = false;
                mMakeupBlush = false;
                break;
            case MHSDK.MAKEUP_LIPSTICK:
                mMakeupLipstick = enable;
                break;
            case MHSDK.MAKEUP_EYELASH:
                mMakeupEyelash = enable;
                break;
            case MHSDK.MAKEUP_EYELINER:
                mMakeupEyeliner = enable;
                break;
            case MHSDK.MAKEUP_EYEBROW:
                mMakeupEyebrow = enable;
                break;
            case MHSDK.MAKEUP_BLUSH:
                mMakeupBlush = enable;
                break;
        }
    }

    @Override
    public void onItemClick(MakeupBean bean, int position) {

        int useFace = 0;
        if(bean.getMakeupId() == MHSDK.MAKEUP_NONE){
            useFace = 0;
        }else{
            setMakeupEnable(bean);
            boolean enable = getMakeupEnable();
            if (enable){
                useFace = 1;
            }else{
                useFace = 0;
            }
        }
        MHBeautyManager mhBeautyManager =  MhDataManager.getInstance().getMHBeautyManager();
        if (mhBeautyManager != null){
            int[]  useFaces = mhBeautyManager.getUseFaces();
            useFaces[5] = useFace;
            mhBeautyManager.setUseFaces(useFaces);
        }

        MhDataManager.getInstance().setMakeup(bean.getMakeupId(),bean.isChecked());

    }
}
