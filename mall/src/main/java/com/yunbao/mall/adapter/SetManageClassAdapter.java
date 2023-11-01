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

import com.yunbao.common.adapter.RefreshAdapter;
import com.yunbao.mall.R;
import com.yunbao.mall.bean.ManageClassBean;

import java.util.List;

public class SetManageClassAdapter extends RefreshAdapter<ManageClassBean> {

    private Drawable mCheckedDrawable;
    private Drawable mUnCheckedDrawable;
    private int mCheckedColor;
    private int mUnCheckedColor;
    private View.OnClickListener mOnClickListener;
    private ActionListener mActionListener;

    public SetManageClassAdapter(Context context, List<ManageClassBean> list) {
        super(context, list);
        mCheckedColor = ContextCompat.getColor(context, R.color.textColor);
        mUnCheckedColor = ContextCompat.getColor(context, R.color.gray3);
        mCheckedDrawable = ContextCompat.getDrawable(context, R.mipmap.ic_check_1);
        mUnCheckedDrawable = ContextCompat.getDrawable(context, R.mipmap.ic_check_0);
        mOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = (int) v.getTag();
                ManageClassBean bean = mList.get(position);
                if (bean.getExist() == 1) {
                    return;
                }
                bean.setChecked(!bean.isChecked());
                notifyItemChanged(position);
                int count = 0;
                for (ManageClassBean bean1 : mList) {
                    if (bean1.isChecked()) {
                        count++;
                    }
                }
                if (mActionListener != null) {
                    mActionListener.onCheckChanged(count);
                }
            }
        };
    }

    public void setActionListener(ActionListener actionListener){
        mActionListener=actionListener;
    }


    public String getCheckedId() {
        StringBuilder sb = null;
        for (ManageClassBean bean : mList) {
            if (bean.isChecked()) {
                if (sb == null) {
                    sb = new StringBuilder();
                }
                sb.append(bean.getId());
                sb.append(",");
            }
        }
        if (sb != null) {
            String s = sb.toString();
            if (s.length() > 1) {
                s = s.substring(0, s.length() - 1);
            }
            return s;
        }
        return null;
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new Vh(mInflater.inflate(R.layout.item_set_manage_class, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        ((Vh) viewHolder).setData(mList.get(i), i);
    }

    class Vh extends RecyclerView.ViewHolder {

        ImageView mImg;
        TextView mText;

        public Vh(@NonNull View itemView) {
            super(itemView);
            mImg = itemView.findViewById(R.id.img);
            mText = itemView.findViewById(R.id.text);
            itemView.setOnClickListener(mOnClickListener);
        }

        void setData(ManageClassBean bean, int position) {
            itemView.setTag(position);
            mText.setText(bean.getName());
            if (bean.getExist() == 1) {
                mImg.setImageDrawable(null);
                mText.setTextColor(mUnCheckedColor);
            } else {
                mImg.setImageDrawable(bean.isChecked() ? mCheckedDrawable : mUnCheckedDrawable);
                mText.setTextColor(mCheckedColor);
            }
        }
    }


    public interface ActionListener {
        void onCheckChanged(int checkedCount);
    }

}
