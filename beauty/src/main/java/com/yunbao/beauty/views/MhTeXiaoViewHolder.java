package com.yunbao.beauty.views;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.yunbao.beauty.R;
import com.yunbao.beauty.adapter.MeiYanTitleAdapter;
import com.yunbao.common.adapter.ViewPagerAdapter;
import com.yunbao.beauty.bean.MeiYanTypeBean;
import com.yunbao.beauty.interfaces.OnItemClickListener;
import com.yunbao.beauty.interfaces.OnTieZhiActionClickListener;
import com.yunbao.beauty.interfaces.OnTieZhiActionDownloadListener;
import com.yunbao.beauty.interfaces.OnTieZhiActionListener;
import com.yunbao.beauty.utils.WordUtil;
import com.meihu.beautylibrary.MHSDK;

import java.util.ArrayList;
import java.util.List;

public class MhTeXiaoViewHolder extends AbsMhChildViewHolder implements View.OnClickListener {

    private final  String  TAG = MhTeXiaoViewHolder.class.getName();
    private ViewPager mViewPager;
    private List<FrameLayout> mViewList;
    private MhTeXiaoChildViewHolder[] mViewHolders;
    private MeiYanTitleAdapter mTitleAdapter;
    private TextView mTip;

    public MhTeXiaoViewHolder(Context context, ViewGroup parentView) {
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
        return R.layout.view_beauty_mh_texiao;
    }

    @Override
    public void init() {
        findViewById(R.id.btn_hide).setOnClickListener(this);
        mTip = findViewById(R.id.tip);
        List<MeiYanTypeBean> typeList = new ArrayList<>();

//        if (MHSDK.isPro()){
//            typeList.add(new MeiYanTypeBean(R.string.beauty_mh_003));
//            typeList.add(new MeiYanTypeBean(R.string.beauty_mh_014));
//            typeList.add(new MeiYanTypeBean(R.string.beauty_mh_015));
//            typeList.add(new MeiYanTypeBean(R.string.beauty_mh_004));
//        }else{
//            typeList.add(new MeiYanTypeBean(R.string.beauty_mh_003));
//            typeList.add(new MeiYanTypeBean(R.string.beauty_mh_014));
//        }
        typeList.add(new MeiYanTypeBean(R.string.beauty_mh_003));
        typeList.add(new MeiYanTypeBean(R.string.beauty_mh_014));
        typeList.get(0).setChecked(true);
        RecyclerView titleRecyclerView = findViewById(R.id.title_recyclerView);
        titleRecyclerView.setLayoutManager(new GridLayoutManager(mContext, typeList.size(), GridLayoutManager.VERTICAL, false));
        mTitleAdapter = new MeiYanTitleAdapter(mContext, typeList);
        mTitleAdapter.setOnItemClickListener(new OnItemClickListener<MeiYanTypeBean>() {
            @Override
            public void onItemClick(MeiYanTypeBean bean, int position) {
                if (mViewPager != null) {
                    mViewPager.setCurrentItem(position, true);
                }
            }
        });
        titleRecyclerView.setAdapter(mTitleAdapter);
        mViewPager = findViewById(R.id.mh_meiyan_viewPager);
        mViewList = new ArrayList<>();
        int pageCount = typeList.size();
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
                if (mTitleAdapter != null) {
                    mTitleAdapter.setCheckedPosition(position);
                }
                loadPageData(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        mViewHolders = new MhTeXiaoChildViewHolder[pageCount];
        loadPageData(0);
    }


    private void loadPageData(int position) {
        if (mViewHolders == null) {
            return;
        }
        MhTeXiaoChildViewHolder vh = mViewHolders[position];
        if (vh == null) {
            if (mViewList != null && position < mViewList.size()) {
                FrameLayout parent = mViewList.get(position);
                if (parent == null) {
                    return;
                }
                if (position == 0) {
                    vh = new MhTeXiaoSpecialViewHolder(mContext, parent);
                }
                else if (position == 1) {
                    vh = new MhTeXiaoWaterViewHolder(mContext, parent);
                }
                else if (position == 2) {
                    vh = new MhTeXiaoActionViewHolder(mContext, parent);

                }else if (position == 3) {
                    vh = new MhTeXiaoHaHaViewHolder(mContext, parent);
                }
                if (vh == null) {
                    return;
                }
                mViewHolders[position] = vh;
                vh.addToParent();
            }
        }
        if (vh != null) {
            vh.loadData();
            vh.setOnTieZhiActionClickListener(new OnTieZhiActionClickListener() {
                @Override
                public void OnTieZhiActionClick(int action) {
                    if (mOnTieZhiActionClickListener != null){
                        mOnTieZhiActionClickListener.OnTieZhiActionClick(action);
                    }
                }
            });
            vh.setOnTieZhiActionListener(new OnTieZhiActionListener() {
                @Override
                public void OnTieZhiAction(int action) {
                    if (mOnTieZhiActionListener != null){
                        mOnTieZhiActionListener.OnTieZhiAction(action);
                    }
                }
            });
            vh.setOnTieZhiActionDownloadListener(new OnTieZhiActionDownloadListener() {
                @Override
                public void OnTieZhiActionDownload(int state) {
                     showTieZhiDownloadTip(state);
                }
            });
        }
    }

    private void showTieZhiDownloadTip(int state){
        if (state == 0){
            mTip.setText(WordUtil.getString(mContext,R.string.beauty_mh_texiao_action_downloading));
            mTip.setVisibility(View.VISIBLE);
        }else{
            mTip.setVisibility(View.INVISIBLE);
            mTip.setText("");
        }
    }

    public void setActionItemClick(int action){
        if (action == 0){
             if (mViewHolders != null && mViewHolders.length > 2 &&  mViewHolders[2] != null){
                 MhTeXiaoActionViewHolder mhTeXiaoActionViewHolder = (MhTeXiaoActionViewHolder) mViewHolders[2];
                 mhTeXiaoActionViewHolder.setItemClick(0);
             }
        }
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
}
