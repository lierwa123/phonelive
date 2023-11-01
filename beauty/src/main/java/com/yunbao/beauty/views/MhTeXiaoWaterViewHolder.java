package com.yunbao.beauty.views;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.yunbao.beauty.R;
import com.yunbao.beauty.adapter.MhTeXiaoWaterAdapter;
import com.yunbao.beauty.bean.TeXiaoWaterBean;
import com.yunbao.beauty.interfaces.OnItemClickListener;
import com.yunbao.beauty.utils.MhDataManager;
import com.meihu.beautylibrary.MHSDK;

import java.util.ArrayList;
import java.util.List;

public class MhTeXiaoWaterViewHolder extends MhTeXiaoChildViewHolder implements OnItemClickListener<TeXiaoWaterBean> {

    public MhTeXiaoWaterViewHolder(Context context, ViewGroup parentView) {
        super(context, parentView);
    }


    @Override
    public void init() {
        List<TeXiaoWaterBean> list = new ArrayList<>();
        list.add(new TeXiaoWaterBean(R.mipmap.ic_mh_none, 0, MHSDK.WATER_NONE, true));
        list.add(new TeXiaoWaterBean(R.mipmap.ic_water_thumb_0, R.mipmap.ic_water_res_0, MHSDK.WATER_TOP_LEFT));
        list.add(new TeXiaoWaterBean(R.mipmap.ic_water_thumb_1, R.mipmap.ic_water_res_1, MHSDK.WATER_TOP_RIGHT));
        list.add(new TeXiaoWaterBean(R.mipmap.ic_water_thumb_2, R.mipmap.ic_water_res_2, MHSDK.WATER_BOTTOM_LEFT));
        list.add(new TeXiaoWaterBean(R.mipmap.ic_water_thumb_3, R.mipmap.ic_water_res_3, MHSDK.WATER_BOTTOM_RIGHT));
        RecyclerView recyclerView = (RecyclerView) mContentView;
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false));
        MhTeXiaoWaterAdapter adapter = new MhTeXiaoWaterAdapter(mContext, list);
        adapter.setOnItemClickListener(this);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onItemClick(TeXiaoWaterBean bean, int position) {
//        Bitmap bitmap = null;
//        if (bean.getRes() == 0) {
//            bitmap = Bitmap.createBitmap(1, 1, Bitmap.Config.ALPHA_8);
//        } else {
//            bitmap = BitmapFactory.decodeResource(MhDataManager.getInstance().getContext().getResources(), bean.getRes());
//        }
//        if (bitmap != null) {
//            MhDataManager.getInstance().setWater(bitmap, bean.getPositon());
//        }
        MhDataManager.getInstance().setWater(bean.getRes(), bean.getPositon());
    }


}
