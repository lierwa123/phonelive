package com.yunbao.beauty.views;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.yunbao.beauty.R;
import com.yunbao.beauty.adapter.MhMeiYanAdapter;
import com.yunbao.beauty.bean.MeiYanBean;
import com.yunbao.beauty.bean.MeiYanValueBean;
import com.yunbao.beauty.interfaces.OnItemClickListener;
import com.yunbao.beauty.utils.MhDataManager;

import java.util.ArrayList;
import java.util.List;

public class MhMeiYanBeautyViewHolder extends MhMeiYanChildViewHolder implements OnItemClickListener<MeiYanBean> {

    private MhMeiYanAdapter mAdapter;

    public MhMeiYanBeautyViewHolder(Context context, ViewGroup parentView) {
        super(context, parentView);
    }

    @Override
    public void init() {
        List<MeiYanBean> list = new ArrayList<>();
        list.add(new MeiYanBean(R.string.beauty_mh_no, R.mipmap.ic_meiyan_no_0, R.mipmap.ic_meiyan_no_1));
        list.add(new MeiYanBean(R.string.beauty_mh_meibai, R.mipmap.ic_meiyan_meibai_0, R.mipmap.ic_meiyan_meibai_1));
        list.add(new MeiYanBean(R.string.beauty_mh_mopi, R.mipmap.ic_meiyan_mopi_0, R.mipmap.ic_meiyan_mopi_1));
        list.add(new MeiYanBean(R.string.beauty_mh_hongrun, R.mipmap.ic_meiyan_hongrun_0, R.mipmap.ic_meiyan_hongrun_1));
        list.add(new MeiYanBean(R.string.beauty_mh_liangdu, R.mipmap.ic_meiyan_liangdu_0, R.mipmap.ic_meiyan_liangdu_1));
        RecyclerView recyclerView = (RecyclerView) mContentView;
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false));
        mAdapter = new MhMeiYanAdapter(mContext, list);
        mAdapter.setOnItemClickListener(this);
        recyclerView.setAdapter(mAdapter);
    }

    @Override
    public void onItemClick(MeiYanBean bean, int position) {
        if (mActionListener == null) {
            return;
        }
        MeiYanValueBean valueBean = MhDataManager.getInstance().getMeiYanValue();
        if (valueBean == null) {
            return;
        }
        int name = bean.getName();
        if (name == R.string.beauty_mh_no) {
            mActionListener.changeProgress(false, 0, 0);
            valueBean.setMeiBai(0);
            valueBean.setMoPi(0);
            valueBean.setHongRun(0);
            MhDataManager.getInstance().useMeiYan().notifyMeiYanChanged();
        } else if (name == R.string.beauty_mh_meibai) {
            mActionListener.changeProgress(true, 9, valueBean.getMeiBai());
        } else if (name == R.string.beauty_mh_mopi) {
            mActionListener.changeProgress(true, 9, valueBean.getMoPi());
        } else if (name == R.string.beauty_mh_hongrun) {
            mActionListener.changeProgress(true, 9, valueBean.getHongRun());
        } else if (name == R.string.beauty_mh_liangdu) {
            mActionListener.changeProgress(true, 100, valueBean.getLiangDu());
        }
    }

    @Override
    public void onProgressChanged(float rate, int progress) {
        if (mAdapter == null) {
            return;
        }
        int name = mAdapter.getCheckedName();
        if (name == R.string.beauty_mh_meibai) {
            MhDataManager.getInstance().setMeiBai(progress);
        } else if (name == R.string.beauty_mh_mopi) {
            MhDataManager.getInstance().setMoPi(progress);
        } else if (name == R.string.beauty_mh_hongrun) {
            MhDataManager.getInstance().setHongRun(progress);
        } else if (name == R.string.beauty_mh_liangdu) {
            MhDataManager.getInstance().setLiangDu(progress);
        }

    }


    @Override
    public void showSeekBar() {
        if (mAdapter == null) {
            if (mActionListener != null) {
                mActionListener.changeProgress(false, 0, 0);
            }
            return;
        }
        MeiYanBean bean = mAdapter.getCheckedBean();
        if (bean != null) {
            onItemClick(bean, 0);
        } else {
            if (mActionListener != null) {
                mActionListener.changeProgress(false, 0, 0);
            }
        }
    }
}
