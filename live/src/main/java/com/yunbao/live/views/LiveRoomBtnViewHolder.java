package com.yunbao.live.views;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.yunbao.common.adapter.ViewPagerAdapter;
import com.yunbao.common.utils.WordUtil;
import com.yunbao.common.views.AbsViewHolder;
import com.yunbao.live.R;
import com.yunbao.live.activity.LiveActivity;

import java.util.ArrayList;
import java.util.List;

public class LiveRoomBtnViewHolder extends AbsViewHolder implements View.OnClickListener {

    private boolean mShowPan;
    private int mPoolLevel;
    private ViewPager mViewPager;
    private PagerAdapter mPagerAdapter;
    private RadioGroup mRadioGroup;
    private List<View> mViewList;
//    private TextView mPrizePoolLevel;//奖池等级
//    private View mPrizePoolGuang;
    private Handler mHandler;

    public LiveRoomBtnViewHolder(Context context, ViewGroup parentView, boolean showPan, int prizePoolLevel) {
        super(context, parentView, showPan, prizePoolLevel);
    }

    @Override
    protected void processArguments(Object... args) {
        mShowPan = (boolean) args[0];
        mPoolLevel = (int) args[1];
    }

    @Override
    protected int getLayoutId() {
        return R.layout.view_live_room_btn;
    }

    @Override
    public void init() {
        mViewPager = findViewById(R.id.viewPager);
        mViewList = new ArrayList<>();
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View taskView = inflater.inflate(R.layout.view_live_room_task, mViewPager, false);
        taskView.findViewById(R.id.btn_task).setOnClickListener(this);
        mViewList.add(taskView);
        if (mPoolLevel >= 0) {
            View prizePool = inflater.inflate(R.layout.view_prize_pool, mViewPager, false);
            prizePool.findViewById(R.id.btn_prize_pool_level).setOnClickListener(this);
//            mPrizePoolLevel = prizePool.findViewById(R.id.prize_pool_level);
//            mPrizePoolGuang = prizePool.findViewById(R.id.prize_pool_level_guang);
//            mPrizePoolLevel.setText(String.format(WordUtil.getString(R.string.live_gift_prize_pool_3), mPoolLevel));
            mViewList.add(prizePool);
        }
        if (mShowPan) {
            View panView = inflater.inflate(R.layout.view_live_room_pan, mViewPager, false);
            panView.findViewById(R.id.btn_luck_pan).setOnClickListener(this);
            mViewList.add(panView);
        }
        mPagerAdapter = new ViewPagerAdapter(mViewList);
        mViewPager.setAdapter(mPagerAdapter);
        mRadioGroup = findViewById(R.id.radio_group);
        for (int i = 0, size = mViewList.size(); i < size; i++) {
            RadioButton radioButton = (RadioButton) inflater.inflate(R.layout.view_gift_indicator_2, mRadioGroup, false);
            radioButton.setId(i + 10000);
            if (i == 0) {
                radioButton.setChecked(true);
            }
            mRadioGroup.addView(radioButton);
        }
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                if (mRadioGroup != null) {
                    RadioButton radioButton = (RadioButton) mRadioGroup.getChildAt(position);
                    if (radioButton != null) {
                        radioButton.setChecked(true);
                    }
                }
                if (mHandler != null) {
                    mHandler.removeCallbacksAndMessages(null);
                    mHandler.sendEmptyMessageDelayed(0, 3000);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        if (mViewList.size() > 1) {
            mHandler = new Handler() {
                @Override
                public void handleMessage(Message msg) {
                    if (mViewPager != null && mPagerAdapter != null) {
                        int curItem = mViewPager.getCurrentItem();
                        if (curItem < mPagerAdapter.getCount() - 1) {
                            curItem++;
                        } else {
                            curItem = 0;
                        }
                        mViewPager.setCurrentItem(curItem, curItem!=0);
                    }
                }
            };
            mHandler.sendEmptyMessageDelayed(0, 3000);
        }
    }


    public void showPrizeLevel(int level) {
//        if (mPrizePoolLevel != null) {
//            mPrizePoolLevel.setText(String.format(WordUtil.getString(R.string.live_gift_prize_pool_3), level));
//        }
    }
//
//    public TextView getPrizePoolLevel() {
//        return mPrizePoolLevel;
//    }
//
//
//    public View getPrizePoolGuang() {
//        return mPrizePoolGuang;
//    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.btn_task) {
            ((LiveActivity) mContext).openDailyTaskWindow();
        } else if (i == R.id.btn_prize_pool_level) {
            ((LiveActivity) mContext).openPrizePoolWindow();
        } else if (i == R.id.btn_luck_pan) {
            ((LiveActivity) mContext).openLuckPanWindow();
        }
    }


    @Override
    public void onDestroy() {
        release();
        super.onDestroy();
    }

    public void release() {
        if (mHandler != null) {
            mHandler.removeCallbacksAndMessages(null);
        }
        mHandler = null;
    }
}
