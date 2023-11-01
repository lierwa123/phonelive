package com.yunbao.beauty.views;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.SeekBar;
import android.widget.TextView;

import com.yunbao.beauty.R;
import com.yunbao.beauty.adapter.SimpleFilterAdapter;
import com.yunbao.beauty.bean.SimpleFilterBean;
import com.yunbao.beauty.interfaces.IBeautyViewHolder;
import com.yunbao.beauty.utils.SimpleDataManager;
import com.yunbao.common.interfaces.OnItemClickListener;
import com.yunbao.common.views.AbsViewHolder;

public class SimpleBeautyViewHolder extends AbsViewHolder implements IBeautyViewHolder, View.OnClickListener, OnItemClickListener<SimpleFilterBean> {

    private View mGroupBeauty;
    private View mGroupFilter;
    private TextView mTvMeiBai;
    private TextView mTvMoPi;
    private TextView mTvHongRun;
    private boolean mShowed;
    private VisibleListener mVisibleListener;

    public SimpleBeautyViewHolder(Context context, ViewGroup parentView) {
        super(context, parentView);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.view_beauty_simple;
    }

    @Override
    public void init() {
        mGroupBeauty = findViewById(R.id.group_beauty);
        mGroupFilter = findViewById(R.id.group_filter);
        findViewById(R.id.btn_hide).setOnClickListener(this);
        findViewById(R.id.btn_beauty).setOnClickListener(this);
        findViewById(R.id.btn_filter).setOnClickListener(this);
        SeekBar seekBarMeiBai = findViewById(R.id.seek_bar_meibai);
        SeekBar seekBarMoPi = findViewById(R.id.seek_bar_mopi);
        SeekBar seekBarHongRun = findViewById(R.id.seek_bar_hongrun);
        mTvMeiBai = findViewById(R.id.text_meibai);
        mTvMoPi = findViewById(R.id.text_mopi);
        mTvHongRun = findViewById(R.id.text_hongrun);
        SeekBar.OnSeekBarChangeListener listener = new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                int id = seekBar.getId();
                if (id == R.id.seek_bar_meibai) {
                    if (mTvMeiBai != null) {
                        mTvMeiBai.setText(String.valueOf(progress));
                    }
                    SimpleDataManager.getInstance().setMeiBai(progress);
                } else if (id == R.id.seek_bar_mopi) {
                    if (mTvMoPi != null) {
                        mTvMoPi.setText(String.valueOf(progress));
                    }
                    SimpleDataManager.getInstance().setMoPi(progress);
                } else if (id == R.id.seek_bar_hongrun) {
                    if (mTvHongRun != null) {
                        mTvHongRun.setText(String.valueOf(progress));
                    }
                    SimpleDataManager.getInstance().setHongRun(progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        };
        seekBarMeiBai.setOnSeekBarChangeListener(listener);
        seekBarMoPi.setOnSeekBarChangeListener(listener);
        seekBarHongRun.setOnSeekBarChangeListener(listener);

        seekBarMeiBai.setProgress(SimpleDataManager.getInstance().getMeiBai());
        seekBarMoPi.setProgress(SimpleDataManager.getInstance().getMoPi());
        seekBarHongRun.setProgress(SimpleDataManager.getInstance().getHongRun());


        RecyclerView filterRecyclerView = (RecyclerView) findViewById(R.id.filter_recyclerView);
        filterRecyclerView.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false));
        SimpleFilterAdapter adapter = new SimpleFilterAdapter(mContext);
        adapter.setOnItemClickListener(this);
        filterRecyclerView.setAdapter(adapter);
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
        removeFromParent();
        if (mVisibleListener != null) {
            mVisibleListener.onVisibleChanged(false);
        }
        mShowed = false;
        SimpleDataManager.getInstance().saveBeautyValue();
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
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.btn_hide) {
            hide();
        } else if (i == R.id.btn_beauty) {
            if (mGroupBeauty != null && mGroupBeauty.getVisibility() != View.VISIBLE) {
                mGroupBeauty.setVisibility(View.VISIBLE);
            }
            if (mGroupFilter != null && mGroupFilter.getVisibility() != View.GONE) {
                mGroupFilter.setVisibility(View.GONE);
            }
        } else if (i == R.id.btn_filter) {
            if (mGroupBeauty != null && mGroupBeauty.getVisibility() != View.GONE) {
                mGroupBeauty.setVisibility(View.GONE);
            }
            if (mGroupFilter != null && mGroupFilter.getVisibility() != View.VISIBLE) {
                mGroupFilter.setVisibility(View.VISIBLE);
            }
        }
    }


    @Override
    public void onItemClick(SimpleFilterBean bean, int position) {
        SimpleDataManager.getInstance().setFilter(bean.getFilterSrc());
    }
}
