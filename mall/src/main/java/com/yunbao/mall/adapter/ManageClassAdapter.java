package com.yunbao.mall.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.yunbao.common.Constants;
import com.yunbao.common.adapter.RefreshAdapter;
import com.yunbao.common.utils.ToastUtil;
import com.yunbao.mall.R;
import com.yunbao.mall.bean.ManageClassBean;

import java.util.ArrayList;
import java.util.List;

public class ManageClassAdapter extends RefreshAdapter<ManageClassBean> {

    private View.OnClickListener mOnClickListener;
    private Drawable mCheckedDrawable;
    private Drawable mUnCheckedDrawable;

    public ManageClassAdapter(Context context, List<ManageClassBean> list) {
        super(context, list);
        mCheckedDrawable = ContextCompat.getDrawable(context, R.mipmap.ic_check_1);
        mUnCheckedDrawable = ContextCompat.getDrawable(context, R.mipmap.ic_check_0);
        mOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Object tag = v.getTag();
                if (tag == null) {
                    return;
                }
                int position = (int) tag;
                ManageClassBean bean = mList.get(position);
                if (bean.isChecked()) {
                    bean.setChecked(false);
                    notifyItemChanged(position, Constants.PAYLOAD);
                }
                else {
//                    if (getCheckedCount() < 3) {
//                        bean.setChecked(true);
//                        notifyItemChanged(position, Constants.PAYLOAD);
//                    } else {
//                        ToastUtil.show(R.string.mall_043);
//                    }
                    bean.setChecked(true);
                    notifyItemChanged(position, Constants.PAYLOAD);
                }
            }
        };
    }


    private int getCheckedCount() {
        int count = 0;
        for (ManageClassBean bean : mList) {
            if (bean.isChecked()) {
                count++;
            }
        }
        return count;
    }

    public ArrayList<ManageClassBean> getCheckedList() {
        ArrayList<ManageClassBean> list = null;
        for (ManageClassBean bean : mList) {
            if (bean.isChecked()) {
                if (list == null) {
                    list = new ArrayList<>();
                }
                list.add(bean);
            }
        }
        return list;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new Vh(mInflater.inflate(R.layout.item_manage_class, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder vh, int position, @NonNull List payloads) {
        Object payload = payloads.size() > 0 ? payloads.get(0) : null;
        ((Vh) vh).setData(mList.get(position), position, payload);
    }

    class Vh extends RecyclerView.ViewHolder {

        ImageView mImg;
        TextView mName;

        public Vh(@NonNull View itemView) {
            super(itemView);
            mImg = itemView.findViewById(R.id.img);
            mName = itemView.findViewById(R.id.name);
            itemView.setOnClickListener(mOnClickListener);
        }

        void setData(ManageClassBean bean, int position, Object payload) {
            if (payload == null) {
                itemView.setTag(position);
                mName.setText(bean.getName());
            }
            mImg.setImageDrawable(bean.isChecked() ? mCheckedDrawable : mUnCheckedDrawable);
        }
    }

}
