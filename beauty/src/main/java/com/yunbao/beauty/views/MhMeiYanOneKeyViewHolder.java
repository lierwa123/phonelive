package com.yunbao.beauty.views;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.yunbao.beauty.R;
import com.yunbao.beauty.adapter.MhMeiYanOneKeyAdapter;
import com.yunbao.beauty.bean.MeiYanOneKeyBean;
import com.yunbao.beauty.bean.MeiYanValueBean;
import com.yunbao.beauty.interfaces.OnItemClickListener;
import com.yunbao.beauty.utils.MhDataManager;
import com.meihu.beautylibrary.manager.MHBeautyManager;

import java.util.ArrayList;
import java.util.List;

public class MhMeiYanOneKeyViewHolder extends MhMeiYanChildViewHolder implements OnItemClickListener<MeiYanOneKeyBean> {

    private MhMeiYanOneKeyAdapter mAdapter;

    public MhMeiYanOneKeyViewHolder(Context context, ViewGroup parentView) {
        super(context, parentView);
    }


    @Override
    public void init() {
        List<MeiYanOneKeyBean> list = new ArrayList<>();
        list.add(new MeiYanOneKeyBean(R.string.beauty_mh_no, R.mipmap.ic_onekey_no, true));
        list.add(new MeiYanOneKeyBean(R.string.beauty_mh_biaozhun, R.mipmap.ic_onekey_biaozhun));
        list.add(new MeiYanOneKeyBean(R.string.beauty_mh_youya, R.mipmap.ic_onekey_youya));
        list.add(new MeiYanOneKeyBean(R.string.beauty_mh_jingzhi, R.mipmap.ic_onekey_jingzhi));
        list.add(new MeiYanOneKeyBean(R.string.beauty_mh_keai, R.mipmap.ic_onekey_keai));
        list.add(new MeiYanOneKeyBean(R.string.beauty_mh_ziran, R.mipmap.ic_onekey_ziran));
        list.add(new MeiYanOneKeyBean(R.string.beauty_mh_wanghong, R.mipmap.ic_onekey_wanghong));
        list.add(new MeiYanOneKeyBean(R.string.beauty_mh_tuosu, R.mipmap.ic_onekey_tuosu));
        list.add(new MeiYanOneKeyBean(R.string.beauty_mh_gaoya, R.mipmap.ic_onekey_gaoya));
        RecyclerView recyclerView = (RecyclerView) mContentView;
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false));
        mAdapter = new MhMeiYanOneKeyAdapter(mContext, list);
        mAdapter.setOnItemClickListener(this);
        recyclerView.setAdapter(mAdapter);
    }

    @Override
    public void onItemClick(MeiYanOneKeyBean bean, int position) {
        if (mActionListener == null) {
            return;
        }
        int name = bean.getName();
        int useFace;

        if (name == R.string.beauty_mh_no) {
            mActionListener.changeProgress(false, 0, 0);
            MhDataManager.getInstance().useMeiYan().notifyMeiYanChanged();
            useFace = 0;
        } else {
            mActionListener.changeProgress(true, 100, bean.getProgress());
            useFace = 1;
        }
        MHBeautyManager mhBeautyManager =  MhDataManager.getInstance().getMHBeautyManager();
        if (mhBeautyManager != null){
            int[]  useFaces = mhBeautyManager.getUseFaces();
            useFaces[2] = useFace;
            mhBeautyManager.setUseFaces(useFaces);
        }
    }


    @Override
    public void onProgressChanged(float rate, int progress) {
        if (mAdapter == null) {
            return;
        }
        MeiYanOneKeyBean bean = mAdapter.getCheckedBean();
        if (bean != null) {
            bean.setProgress(progress);
            MeiYanValueBean valueBean = bean.calculateValue(rate);
            MhDataManager.getInstance()
                    .setOneKeyValue(valueBean)
                    .useOneKey()
                    .notifyMeiYanChanged();
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
        MeiYanOneKeyBean bean = mAdapter.getCheckedBean();
        if (bean != null) {
            onItemClick(bean, 0);
        } else {
            if (mActionListener != null) {
                mActionListener.changeProgress(false, 0, 0);
            }
        }
    }
}
