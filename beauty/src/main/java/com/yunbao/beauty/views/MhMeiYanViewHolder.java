package com.yunbao.beauty.views;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.yunbao.beauty.R;
import com.yunbao.beauty.adapter.MeiYanTitleAdapter;
import com.yunbao.common.adapter.ViewPagerAdapter;
import com.yunbao.beauty.bean.MeiYanTypeBean;
import com.yunbao.beauty.custom.TextSeekBar;
import com.yunbao.beauty.interfaces.OnItemClickListener;
import com.meihu.beautylibrary.MHSDK;

import java.util.ArrayList;
import java.util.List;

public class MhMeiYanViewHolder extends AbsMhChildViewHolder implements View.OnClickListener, TextSeekBar.ActionListener, MhMeiYanChildViewHolder.ActionListener {

    private TextSeekBar mSeekBar;
    private ViewPager mViewPager;
    private List<FrameLayout> mViewList;
    private MhMeiYanChildViewHolder[] mViewHolders;
    private List<MeiYanTypeBean> mTypeList;
    private MeiYanTitleAdapter mTitleAdapter;

    private MhMeiYanChildViewHolder mMhMeiYanChildViewHolder;

    public MhMeiYanViewHolder(Context context, ViewGroup parentView) {
        super(context, parentView);
    }

    @Override
    public void showSeekBar() {
        if (mMhMeiYanChildViewHolder != null){
            mMhMeiYanChildViewHolder.showSeekBar();
        }
    }

    @Override
    public void hideSeekBar() {
        if (mSeekBar != null){
            mSeekBar.setVisibility(View.GONE);
        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.view_beauty_mh_meiyan;
    }

    @Override
    public void init() {
        findViewById(R.id.btn_hide).setOnClickListener(this);
        mSeekBar = findViewById(R.id.seek_bar);
        mSeekBar.setActionListener(this);
        List<MeiYanTypeBean> typeList = new ArrayList<>();
        if (MHSDK.isSimple()) {
            typeList.add(new MeiYanTypeBean(R.string.beauty_mh_010));
        } else if (MHSDK.isPro()){
            typeList.add(new MeiYanTypeBean(R.string.beauty_mh_010));
            typeList.add(new MeiYanTypeBean(R.string.beauty_mh_011));
            typeList.add(new MeiYanTypeBean(R.string.beauty_mh_012));
            typeList.add(new MeiYanTypeBean(R.string.beauty_mh_013));
        }else {
            typeList.add(new MeiYanTypeBean(R.string.beauty_mh_010));
            typeList.add(new MeiYanTypeBean(R.string.beauty_mh_011));
            typeList.add(new MeiYanTypeBean(R.string.beauty_mh_013));
        }
        typeList.get(0).setChecked(true);
        mTypeList = typeList;
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
        mViewHolders = new MhMeiYanChildViewHolder[pageCount];
        loadPageData(0);
    }


    private void loadPageData(int position) {
        if (mViewHolders == null) {
            return;
        }
        MhMeiYanChildViewHolder vh = mViewHolders[position];
        if (vh == null) {
            if (mViewList != null && position < mViewList.size()) {
                FrameLayout parent = mViewList.get(position);
                if (parent == null) {
                    return;
                }
                int name = mTypeList.get(position).getName();
                if (name == R.string.beauty_mh_010) {
                    vh = new MhMeiYanBeautyViewHolder(mContext, parent);
                } else if (name == R.string.beauty_mh_011) {
                    vh = new MhMeiYanShapeViewHolder(mContext, parent);
                } else if (name == R.string.beauty_mh_012) {
                    vh = new MhMeiYanOneKeyViewHolder(mContext, parent);
                } else if (name == R.string.beauty_mh_013) {
                    vh = new MhMeiYanFilterViewHolder(mContext, parent);
                }
                if (vh == null) {
                    return;
                }
                mViewHolders[position] = vh;
                vh.addToParent();
                vh.setActionListener(this);
            }
        }
        if (vh != null) {
            vh.loadData();
            vh.showSeekBar();
        }
        mMhMeiYanChildViewHolder = vh;
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

    @Override
    public void onProgressChanged(float rate, int progress) {
        if (mViewHolders != null && mViewPager != null) {
            MhMeiYanChildViewHolder vh = mViewHolders[mViewPager.getCurrentItem()];
            if (vh != null) {
                vh.onProgressChanged(rate, progress);
            }
        }
    }


    @Override
    public void changeProgress(boolean visible, int max, int progress) {
        if (mSeekBar != null) {
            if (visible) {
                if (mSeekBar.getVisibility() != View.VISIBLE) {
                    mSeekBar.setVisibility(View.VISIBLE);
                }
                mSeekBar.setMax(max);
                mSeekBar.setProgress(progress);
            } else {
                if (mSeekBar.getVisibility() != View.GONE) {
                    mSeekBar.setVisibility(View.GONE);
                }
            }
        }
    }
}
