package com.yunbao.live.dialog;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.SparseIntArray;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.yunbao.common.dialog.AbsDialogFragment;
import com.yunbao.live.R;
import com.yunbao.live.adapter.LiveVoiceFaceAdapter;
import com.yunbao.live.bean.LiveVoiceFaceBean;
import com.yunbao.live.utils.LiveIconUtil;

import java.util.ArrayList;
import java.util.List;

public class LiveVoiceFaceFragment extends AbsDialogFragment {
    @Override
    protected int getLayoutId() {
        return R.layout.dialog_live_voice_face;
    }

    @Override
    protected int getDialogStyle() {
        return R.style.dialog2;
    }

    @Override
    protected boolean canCancel() {
        return true;
    }

    @Override
    protected void setWindowAttributes(Window window) {
        window.setWindowAnimations(R.style.bottomToTopAnim);
        WindowManager.LayoutParams params = window.getAttributes();
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        params.gravity = Gravity.BOTTOM;
        window.setAttributes(params);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        final List<View> viewList = new ArrayList<>();
        SparseIntArray map = LiveIconUtil.getVoiceRoomFace();
        int fromIndex = 0;
        int size = map.size();
        int pageCount = size / 8;
        if (size % 8 > 0) {
            pageCount++;
        }
        LayoutInflater inflater = LayoutInflater.from(mContext);
        for (int i = 0; i < pageCount; i++) {
            RecyclerView recyclerView = (RecyclerView) inflater.inflate(R.layout.view_gift_page, null, false);
            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(new GridLayoutManager(mContext, 4, GridLayoutManager.VERTICAL, false));
            int endIndex = fromIndex + 8;
            if (endIndex > size) {
                endIndex = size;
            }
            List<LiveVoiceFaceBean> subList = new ArrayList<>();
            for (int j = fromIndex; j < endIndex; j++) {
                subList.add(new LiveVoiceFaceBean(map.keyAt(j), map.valueAt(j)));
            }
            LiveVoiceFaceAdapter adapter = new LiveVoiceFaceAdapter(mContext,inflater, subList);
            recyclerView.setAdapter(adapter);
            viewList.add(recyclerView);
            fromIndex = endIndex;
        }

        ViewPager viewPager = findViewById(R.id.viewPager);
        viewPager.setOffscreenPageLimit(viewList.size() + 1);
        viewPager.setAdapter(new PagerAdapter() {
            @Override
            public int getCount() {
                return viewList.size();
            }

            @Override
            public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
                return view == o;
            }

            @Override
            public Object instantiateItem(ViewGroup container, int position) {
                View view = viewList.get(position);
                container.addView(view);
                return view;
            }

            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {
                container.removeView(viewList.get(position));
            }

        });
        final RadioGroup radioGroup = findViewById(R.id.radio_group);
        for (int i = 0; i < pageCount; i++) {
            RadioButton radioButton = (RadioButton) inflater.inflate(R.layout.view_gift_indicator, radioGroup, false);
            radioButton.setId(i + 10000);
            if (i == 0) {
                radioButton.setChecked(true);
            }
            radioGroup.addView(radioButton);
        }
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                if (radioGroup != null) {
                    RadioButton radioButton = (RadioButton) radioGroup.getChildAt(position);
                    if (radioButton != null) {
                        radioButton.setChecked(true);
                    }
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }
}
