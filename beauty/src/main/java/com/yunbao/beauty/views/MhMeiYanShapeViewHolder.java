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
import com.meihu.beautylibrary.MHSDK;
import com.meihu.beautylibrary.manager.MHBeautyManager;

import java.util.ArrayList;
import java.util.List;

public class MhMeiYanShapeViewHolder extends MhMeiYanChildViewHolder implements OnItemClickListener<MeiYanBean> {

    private MhMeiYanAdapter mAdapter;

    public MhMeiYanShapeViewHolder(Context context, ViewGroup parentView) {
        super(context, parentView);
    }

    @Override
    public void init() {
        List<MeiYanBean> list = new ArrayList<>();
        if (MHSDK.isPro()) {
            list.add(new MeiYanBean(R.string.beauty_mh_no, R.mipmap.ic_meiyan_no_0, R.mipmap.ic_meiyan_no_1));
            list.add(new MeiYanBean(R.string.beauty_mh_dayan, R.mipmap.ic_meiyan_dayan_0, R.mipmap.ic_meiyan_dayan_1));
            list.add(new MeiYanBean(R.string.beauty_mh_shoulian, R.mipmap.ic_meiyan_shoulian_0, R.mipmap.ic_meiyan_shoulian_1));
            list.add(new MeiYanBean(R.string.beauty_mh_zuixing, R.mipmap.ic_meiyan_zuixing_0, R.mipmap.ic_meiyan_zuixing_1));
            list.add(new MeiYanBean(R.string.beauty_mh_shoubi, R.mipmap.ic_meiyan_shoubi_0, R.mipmap.ic_meiyan_shoubi_1));
            list.add(new MeiYanBean(R.string.beauty_mh_xiaba, R.mipmap.ic_meiyan_xiaba_0, R.mipmap.ic_meiyan_xiaba_1));
            list.add(new MeiYanBean(R.string.beauty_mh_etou, R.mipmap.ic_meiyan_etou_0, R.mipmap.ic_meiyan_etou_1));
            list.add(new MeiYanBean(R.string.beauty_mh_meimao, R.mipmap.ic_meiyan_meimao_0, R.mipmap.ic_meiyan_meimao_1));
            list.add(new MeiYanBean(R.string.beauty_mh_yanjiao, R.mipmap.ic_meiyan_yanjiao_0, R.mipmap.ic_meiyan_yanjiao_1));
            list.add(new MeiYanBean(R.string.beauty_mh_yanju, R.mipmap.ic_meiyan_yanju_0, R.mipmap.ic_meiyan_yanju_1));
            list.add(new MeiYanBean(R.string.beauty_mh_kaiyanjiao, R.mipmap.ic_meiyan_kaiyanjiao_0, R.mipmap.ic_meiyan_kaiyanjiao_1));
            list.add(new MeiYanBean(R.string.beauty_mh_xuelian, R.mipmap.ic_meiyan_xuelian_0, R.mipmap.ic_meiyan_xuelian_1));
            list.add(new MeiYanBean(R.string.beauty_mh_changbi, R.mipmap.ic_meiyan_changbi_0, R.mipmap.ic_meiyan_changbi_1));
        } else {
            list.add(new MeiYanBean(R.string.beauty_mh_no, R.mipmap.ic_meiyan_no_0, R.mipmap.ic_meiyan_no_1, true));
            list.add(new MeiYanBean(R.string.beauty_mh_dayan, R.mipmap.ic_meiyan_dayan_0, R.mipmap.ic_meiyan_dayan_1));
            list.add(new MeiYanBean(R.string.beauty_mh_shoulian, R.mipmap.ic_meiyan_shoulian_0, R.mipmap.ic_meiyan_shoulian_1));
            list.add(new MeiYanBean(R.string.beauty_mh_zuixing, R.mipmap.ic_meiyan_zuixing_0, R.mipmap.ic_meiyan_zuixing_1));
            list.add(new MeiYanBean(R.string.beauty_mh_shoubi, R.mipmap.ic_meiyan_shoubi_0, R.mipmap.ic_meiyan_shoubi_1));
            list.add(new MeiYanBean(R.string.beauty_mh_xiaba, R.mipmap.ic_meiyan_xiaba_0, R.mipmap.ic_meiyan_xiaba_1));
            list.add(new MeiYanBean(R.string.beauty_mh_etou, R.mipmap.ic_meiyan_etou_0, R.mipmap.ic_meiyan_etou_1));
        }

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

        int useFace;

        if (name == R.string.beauty_mh_no){
            useFace = 0;
        }else{
            useFace = 1;
        }
        MHBeautyManager mhBeautyManager =  MhDataManager.getInstance().getMHBeautyManager();
        if (mhBeautyManager != null){
            int[]  useFaces = mhBeautyManager.getUseFaces();
            useFaces[1] = useFace;
            mhBeautyManager.setUseFaces(useFaces);
        }

        if (name == R.string.beauty_mh_no) {
            mActionListener.changeProgress(false, 0, 0);
            valueBean.setDaYan(0);
            valueBean.setMeiMao(0);
            valueBean.setYanJu(0);
            valueBean.setYanJiao(0);
            valueBean.setShouLian(0);
            valueBean.setZuiXing(0);
            valueBean.setShouBi(0);
            valueBean.setXiaBa(0);
            valueBean.setETou(0);
            valueBean.setChangBi(0);
            valueBean.setXueLian(0);
            valueBean.setKaiYanJiao(0);
            MhDataManager.getInstance().useMeiYan().notifyMeiYanChanged();
        } else if (name == R.string.beauty_mh_dayan) {
            mActionListener.changeProgress(true, 100, valueBean.getDaYan());
        } else if (name == R.string.beauty_mh_meimao) {
            mActionListener.changeProgress(true, 100, valueBean.getMeiMao());
        } else if (name == R.string.beauty_mh_yanju) {
            mActionListener.changeProgress(true, 100, valueBean.getYanJu());
        } else if (name == R.string.beauty_mh_yanjiao) {
            mActionListener.changeProgress(true, 100, valueBean.getYanJiao());
        } else if (name == R.string.beauty_mh_shoulian) {
            mActionListener.changeProgress(true, 100, valueBean.getShouLian());
        } else if (name == R.string.beauty_mh_zuixing) {
            mActionListener.changeProgress(true, 100, valueBean.getZuiXing());
        } else if (name == R.string.beauty_mh_shoubi) {
            mActionListener.changeProgress(true, 100, valueBean.getShouBi());
        } else if (name == R.string.beauty_mh_xiaba) {
            mActionListener.changeProgress(true, 100, valueBean.getXiaBa());
        } else if (name == R.string.beauty_mh_etou) {
            mActionListener.changeProgress(true, 100, valueBean.getETou());
        } else if (name == R.string.beauty_mh_changbi) {
            mActionListener.changeProgress(true, 100, valueBean.getChangBi());
        } else if (name == R.string.beauty_mh_xuelian) {
            mActionListener.changeProgress(true, 100, valueBean.getXueLian());
        } else if (name == R.string.beauty_mh_kaiyanjiao) {
            mActionListener.changeProgress(true, 100, valueBean.getKaiYanJiao());
        }
    }


    @Override
    public void onProgressChanged(float rate, int progress) {
        if (mAdapter == null) {
            return;
        }
        int name = mAdapter.getCheckedName();
        if (name == R.string.beauty_mh_dayan) {
            MhDataManager.getInstance().setDaYan(progress);
        } else if (name == R.string.beauty_mh_meimao) {
            MhDataManager.getInstance().setMeiMao(progress);
        } else if (name == R.string.beauty_mh_yanju) {
            MhDataManager.getInstance().setYanJu(progress);
        } else if (name == R.string.beauty_mh_yanjiao) {
            MhDataManager.getInstance().setYanJiao(progress);
        } else if (name == R.string.beauty_mh_shoulian) {
            MhDataManager.getInstance().setShouLian(progress);
        } else if (name == R.string.beauty_mh_zuixing) {
            MhDataManager.getInstance().setZuiXing(progress);
        } else if (name == R.string.beauty_mh_shoubi) {
            MhDataManager.getInstance().setShouBi(progress);
        } else if (name == R.string.beauty_mh_xiaba) {
            MhDataManager.getInstance().setXiaBa(progress);
        } else if (name == R.string.beauty_mh_etou) {
            MhDataManager.getInstance().setETou(progress);
        } else if (name == R.string.beauty_mh_changbi) {
            MhDataManager.getInstance().setChangBi(progress);
        } else if (name == R.string.beauty_mh_xuelian) {
            MhDataManager.getInstance().setXueLian(progress);
        } else if (name == R.string.beauty_mh_kaiyanjiao) {
            MhDataManager.getInstance().setKaiYanJiao(progress);
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
        }else{
            if (mActionListener != null) {
                mActionListener.changeProgress(false, 0, 0);
            }
        }
    }
}
