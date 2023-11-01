package com.yunbao.beauty.views;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.yunbao.beauty.R;
import com.yunbao.beauty.interfaces.IBeautyClickListener;
import com.yunbao.beauty.interfaces.IBeautyViewHolder;
import com.yunbao.beauty.interfaces.OnBottomHideListener;
import com.yunbao.beauty.interfaces.OnBottomShowListener;
import com.yunbao.beauty.interfaces.OnCaptureListener;
import com.yunbao.beauty.interfaces.OnTieZhiActionClickListener;
import com.yunbao.beauty.interfaces.OnTieZhiActionListener;
import com.yunbao.beauty.interfaces.OnTieZhiClickListener;
import com.yunbao.beauty.utils.MhDataManager;
import com.yunbao.beauty.utils.WordUtil;
import com.meihu.beautylibrary.MHSDK;
import com.meihu.beautylibrary.manager.MHBeautyManager;
import com.yunbao.common.adapter.ViewPagerAdapter;
import com.yunbao.common.views.AbsViewHolder;

import java.util.ArrayList;
import java.util.List;

public class BeautyViewHolder extends AbsViewHolder implements IBeautyViewHolder, IBeautyClickListener {

    private ViewPager mViewPager;
    private List<FrameLayout> mViewList;
    private AbsMhChildViewHolder[] mViewHolders;
    private boolean mShowed;
    private VisibleListener mVisibleListener;
    private int mTargetIndex;
    private AbsMhChildViewHolder mAbsMhChildViewHolder;

    private OnCaptureListener mOnCaptureListener;
    private OnBottomShowListener mOnBottomShowListener;
    private TextView mTip;
    private Handler mHandler;

    public BeautyViewHolder(Context context, ViewGroup parentView) {
        super(context, parentView);
    }

    public void setOnCaptureListener(OnCaptureListener onCaptureListener) {
        mOnCaptureListener = onCaptureListener;
    }

    public void setOnBottomShowListener(OnBottomShowListener onBottomShowListener) {
        mOnBottomShowListener = onBottomShowListener;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.view_beauty;
    }

    @Override
    public void init() {
        mViewPager =  mContentView.findViewById(R.id.view_pager);
        mTip =  mContentView.findViewById(R.id.tip);
        int pageCount = 5;
        mViewList = new ArrayList<>();
        for (int i = 0; i < pageCount; i++) {
            FrameLayout frameLayout = new FrameLayout(mContext);
            frameLayout.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            mViewList.add(frameLayout);
        }
        if (pageCount > 1) {
            mViewPager.setOffscreenPageLimit(pageCount - 1);
        }
        mViewPager.setAdapter(new ViewPagerAdapter(mViewList));
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                loadPageData(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        mViewHolders = new AbsMhChildViewHolder[pageCount];
        loadPageData(0);
        mHandler = new Handler(Looper.getMainLooper());
    }

    private void loadPageData(int position) {
        if (mViewHolders == null) {
            return;
        }
        AbsMhChildViewHolder vh = mViewHolders[position];
        if (vh == null) {
            if (mViewList != null && position < mViewList.size()) {
                FrameLayout parent = mViewList.get(position);
                if (parent == null) {
                    return;
                }
                if (position == 0) {
                    vh = new MhMainViewHolder(mContext, parent);
                } else if (position == 1) {
                    vh = new MhTieZhiViewHolder(mContext, parent);
                } else if (position == 2) {
                    vh = new MhMeiYanViewHolder(mContext, parent);
                } else if (position == 3) {
//                    if (MHSDK.isPro()){
//                        vh = new MhMakeupViewHolder(mContext, parent);
//                    }else{
//                        vh = new MhTeXiaoViewHolder(mContext, parent);
//                    }
                    vh = new MhTeXiaoViewHolder(mContext, parent);
                } else if (position == 4) {
//                    if (MHSDK.isPro()){
//                        vh = new MhTeXiaoViewHolder(mContext, parent);
//                    }else{
//                        vh = new MhHaHaViewHolder(mContext, parent);
//                    }
                    vh = new MhHaHaViewHolder(mContext, parent);
                }
                if (vh == null) {
                    return;
                }
                mViewHolders[position] = vh;
                vh.addToParent();
                vh.setIBeautyClickListener(this);
            }
        }
        if (vh != null) {
            vh.loadData();
            vh.setOnBottomHideListener(new OnBottomHideListener() {
                @Override
                public void OnBottomStartHide() {
                    if (mAbsMhChildViewHolder != null){
                        mAbsMhChildViewHolder.hideSeekBar();
                    }
                }

                @Override
                public void OnBottomHide() {
                    if (mViewPager != null) {
                        mViewPager.setCurrentItem(0, false);
                    }

                    if (mOnBottomShowListener !=null ){
                        mOnBottomShowListener.onShow(false);
                    }
                }

                @Override
                public void OnBottomShow() {
                    if (mAbsMhChildViewHolder != null){
                        mAbsMhChildViewHolder.showSeekBar();
                    }
                }
            });
            if (mOnBottomShowListener != null){
                mOnBottomShowListener.onShow(true);
            }
            vh.setOnTieZhiClickListener(new OnTieZhiClickListener() {
                @Override
                public void OnTieZhiClick() {

//                    if (MHSDK.isPro()){
//                        if (mViewHolders != null && mViewHolders[4] != null){
//                            MhTeXiaoViewHolder mhTeXiaoViewHolder = (MhTeXiaoViewHolder) mViewHolders[4];
//                            mhTeXiaoViewHolder.setActionItemClick(0);
//                        }
//                        showActionTip(0);
//                    }

                }
            });
            vh.setOnTieZhiActionClickListener(new OnTieZhiActionClickListener() {
                @Override
                public void OnTieZhiActionClick(int action) {
                    enableUseFace(null);
                    if (mViewHolders != null && mViewHolders[1] != null){
                        MhTieZhiViewHolder mhTeXiaoViewHolder = (MhTieZhiViewHolder) mViewHolders[1];
                        mhTeXiaoViewHolder.clearCheckedPosition();
//                        if (action == 0){
//                            enableUseFace(null);
//                        }
                    }
                }
            });
            vh.setOnTieZhiActionListener(new OnTieZhiActionListener() {
                @Override
                public void OnTieZhiAction(int action) {
                    showActionTip(action);
                }
            });
            vh.showBottom();
        }
        mAbsMhChildViewHolder = vh;
    }


    private void enableUseFace(String stickerName){
        int useFace;
        if(TextUtils.isEmpty(stickerName)){
            useFace = 0;
        }else{
            useFace = 1;
        }
        MHBeautyManager mhBeautyManager =  MhDataManager.getInstance().getMHBeautyManager();
        if (mhBeautyManager != null){
            int[]  useFaces = mhBeautyManager.getUseFaces();
            useFaces[0] = useFace;
            mhBeautyManager.setUseFaces(useFaces);
        }
        MhDataManager.getInstance().setTieZhi(stickerName);
    }

    private void showActionTip(int action){
        if (action == 0){
            mTip.setText("");
            mTip.setVisibility(View.INVISIBLE);
        }else{
            mTip.setVisibility(View.VISIBLE);
            mTip.setText(getTipText(action));
        }
    }

    public void hideTip(){
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                if (mTip != null){
                    mTip.setText("");
                    mTip.setVisibility(View.INVISIBLE);
                }
            }
        });
    }

    private String getTipText(int action){
        int tipRes = 0;
        if(action == 1){
            tipRes  = R.string.beauty_mh_texiao_action_head_tip;
        }else if (action == 2){
            tipRes  = R.string.beauty_mh_texiao_action_mouth_tip;
        }else {
            tipRes  = R.string.beauty_mh_texiao_action_eye_tip;
        }
        return WordUtil.getString(mContext,tipRes);
    }

    @Override
    public void show() {
        if (mVisibleListener != null) {
            mVisibleListener.onVisibleChanged(true);
        }
        if (mParentView != null && mContentView != null) {
            ViewParent parent = mContentView.getParent();
            if (parent != null) {
                ((ViewGroup) parent).removeView(mContentView);
            }
            mParentView.addView(mContentView);
        }
        mShowed = true;
    }

    @Override
    public void hide() {
        if (mViewPager != null && mViewPager.getCurrentItem() != 0) {
            tabMain();
            return;
        }
        removeFromParent();
        if (mVisibleListener != null) {
            mVisibleListener.onVisibleChanged(false);
        }
        mShowed = false;
        MhDataManager.getInstance().saveBeautyValue();
    }

    @Override
    public boolean isShowed() {
        return mShowed;
    }

    @Override
    public void setVisibleListener(VisibleListener visibleListener) {
        mVisibleListener = visibleListener;
    }

    @Override
    public void hideView() {
        hide();
    }


    @Override
    public void tabMain() {
        mAbsMhChildViewHolder.hideBottom();
    }

    @Override
    public void tabTieZhi() {
        if (mViewPager != null) {
            mViewPager.setCurrentItem(1, false);
        }
    }

    @Override
    public void tabMeiYan() {
        if (mViewPager != null) {
            mViewPager.setCurrentItem(2, false);
        }
    }

    @Override
    public void tabMakeup() {
        if (mViewPager != null) {
            mViewPager.setCurrentItem(3, false);
        }
    }

    @Override
    public void tabTeXiao() {

        int selectItem = 3;

//        if (MHSDK.isPro()){
//            selectItem = 4;
//        }

        if (mViewPager != null) {
            mViewPager.setCurrentItem(selectItem, false);
        }
    }

    @Override
    public void tabHaHa() {
        if (mViewPager != null) {
            mViewPager.setCurrentItem(4, false);
        }
    }

    public View getCenterViewContainer(){
        if (mViewPager.getCurrentItem() == 0 ) {
            MhMainViewHolder mhMainViewHolder = (MhMainViewHolder)mViewHolders[0];
            return mhMainViewHolder.getCenterViewContainer();
        }else{
            return null;
        }
    }

    public ImageView getRecordView(){
        if (mViewPager.getCurrentItem() == 0 ) {
            MhMainViewHolder mhMainViewHolder = (MhMainViewHolder)mViewHolders[0];
            return mhMainViewHolder.getRecordView();
        }else{
            return null;
        }
    }

    public void showViewContainer(boolean isShow){

        if (mViewPager.getCurrentItem() != 0 ){
            return;
        }
        MhMainViewHolder mhMainViewHolder = (MhMainViewHolder)mViewHolders[0];
        mhMainViewHolder.showViewContainer(isShow);

    }


}
