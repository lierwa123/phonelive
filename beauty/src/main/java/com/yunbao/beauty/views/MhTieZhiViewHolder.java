package com.yunbao.beauty.views;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.yunbao.beauty.R;
import com.yunbao.beauty.adapter.TieZhiTitleAdapter;
import com.yunbao.common.adapter.ViewPagerAdapter;
import com.yunbao.beauty.bean.TieZhiBean;
import com.yunbao.beauty.bean.TieZhiTypeBean;
import com.yunbao.beauty.interfaces.OnItemClickListener;
import com.yunbao.beauty.utils.MhDataManager;
import com.yunbao.beauty.utils.WordUtil;
import com.meihu.beautylibrary.MHSDK;
import com.meihu.beautylibrary.manager.MHBeautyManager;

import java.util.ArrayList;
import java.util.List;

public class MhTieZhiViewHolder extends AbsMhChildViewHolder implements View.OnClickListener, MhTieZhiChildViewHolder.ActionListener {

    private ViewPager mViewPager;
    private List<FrameLayout> mViewList;
    private MhTieZhiChildViewHolder[] mViewHolders;
    private List<TieZhiTypeBean> mTypeList;
    private TieZhiTitleAdapter mTitleAdapter;
    private String mCheckedTieZhiName;

    public MhTieZhiViewHolder(Context context, ViewGroup parentView) {
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
        return R.layout.view_beauty_mh_tiezhi;
    }

    @Override
    public void init() {
        findViewById(R.id.btn_hide).setOnClickListener(this);
        findViewById(R.id.capture).setOnClickListener(this);
        RecyclerView titleRecyclerView = findViewById(R.id.title_recyclerView);
        List<TieZhiTypeBean> typeList = new ArrayList<>();
        int[] tieZhiIds = MHSDK.getTieZhiIds();
        for (int i = 0, len = tieZhiIds.length; i < len; i++) {
            boolean isPro = false;
            String name = null;
            switch (tieZhiIds[i]) {
                case MHSDK.TIEZHI_BASIC_STICKER:
                    name = WordUtil.getString(MhDataManager.getInstance().getContext(),R.string.beauty_mh_005);
                    break;
                case MHSDK.TIEZHI_PRO_STICKER:
                    name = WordUtil.getString(MhDataManager.getInstance().getContext(),R.string.beauty_mh_006);
                    isPro = true;
                    break;
                case MHSDK.TIEZHI_BASIC_MASK:
                    name = WordUtil.getString(MhDataManager.getInstance().getContext(),R.string.beauty_mh_007);
                    break;
                case MHSDK.TIEZHI_PRO_MASK:
                    name = WordUtil.getString(MhDataManager.getInstance().getContext(),R.string.beauty_mh_008);
                    isPro = true;
                    break;
            }
            typeList.add(new TieZhiTypeBean(tieZhiIds[i], name, isPro));
        }
        typeList.get(0).setChecked(true);
        mTypeList = typeList;
        titleRecyclerView.setLayoutManager(new GridLayoutManager(mContext, typeList.size(), GridLayoutManager.VERTICAL, false));
        mTitleAdapter = new TieZhiTitleAdapter(mContext, typeList);
        mTitleAdapter.setOnItemClickListener(new OnItemClickListener<TieZhiTypeBean>() {
            @Override
            public void onItemClick(TieZhiTypeBean bean, int position) {
                if (mViewPager != null) {
                    mViewPager.setCurrentItem(position, true);
                }
            }
        });
        titleRecyclerView.setAdapter(mTitleAdapter);
        mViewPager = findViewById(R.id.mh_tiezhi_viewPager);
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
        mViewHolders = new MhTieZhiChildViewHolder[pageCount];
        loadPageData(0);

    }


    private void loadPageData(int position) {
        if (mViewHolders == null) {
            return;
        }
        MhTieZhiChildViewHolder vh = mViewHolders[position];
        if (vh == null) {
            if (mViewList != null && position < mViewList.size()) {
                FrameLayout parent = mViewList.get(position);
                if (parent == null) {
                    return;
                }
                vh = new MhTieZhiChildViewHolder(mContext, parent, mTypeList.get(position).getId());
                mViewHolders[position] = vh;
                vh.addToParent();
                vh.setActionListener(this);
            }
        }
        if (vh != null) {
            vh.loadData();
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


    @Override
    public void onTieZhiChecked(MhTieZhiChildViewHolder vh, TieZhiBean bean) {

        mCheckedTieZhiName = bean.getName();
        if (!TextUtils.isEmpty(mCheckedTieZhiName)){
            if (mOnTieZhiClickListener != null){
                mOnTieZhiClickListener.OnTieZhiClick();
            }
        }

        MhDataManager.getInstance().setTieZhi(bean.getName());

        for (MhTieZhiChildViewHolder viewHolder : mViewHolders) {
            if (viewHolder != null && viewHolder != vh) {
                viewHolder.setCheckedPosition(TextUtils.isEmpty(mCheckedTieZhiName) ? 0 : -1);
            }
        }

        int useFace;
        if(TextUtils.isEmpty(mCheckedTieZhiName)){
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

    }

    public void clearCheckedPosition(){
        for (MhTieZhiChildViewHolder viewHolder : mViewHolders) {
            if (viewHolder != null) {
                viewHolder.setCheckedPosition(0);
            }
        }
    }

    @Override
    public String getCheckedTieZhiName() {
        return mCheckedTieZhiName;
    }

}
