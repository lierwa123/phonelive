package com.yunbao.live.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.yunbao.common.Constants;
import com.yunbao.common.adapter.RefreshAdapter;
import com.yunbao.common.http.HttpCallback;
import com.yunbao.common.utils.ToastUtil;
import com.yunbao.common.utils.WordUtil;
import com.yunbao.live.R;
import com.yunbao.live.bean.DailyTaskBean;
import com.yunbao.live.http.LiveHttpUtil;

import java.util.List;

public class DailyTaskAdapter extends RefreshAdapter<DailyTaskBean> {

    private View.OnClickListener mOnClickListener;
    private String mStatus0;
    private String mStatus1;
    private String mStatus2;
    private int mColor0;
    private int mColor1;
    private Drawable mDrawable0;
    private Drawable mDrawable1;

    public DailyTaskAdapter(Context context, List<DailyTaskBean> list) {
        super(context, list);
        mOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = (int) v.getTag();
                DailyTaskBean bean = mList.get(position);
                if (bean.getStatus() == 1) {//可领取
                    final int finalPosition = position;
                    final DailyTaskBean finalBean = bean;
                    LiveHttpUtil.dailyTaskReward(bean.getId(), new HttpCallback() {
                        @Override
                        public void onSuccess(int code, String msg, String[] info) {
                            if (code == 0) {
                                finalBean.setStatus(2);
                                notifyItemChanged(finalPosition, Constants.PAYLOAD);
                            }
                            ToastUtil.show(msg);
                        }
                    });
                }
            }
        };
        mStatus0 = WordUtil.getString(R.string.daily_task_0);
        mStatus1 = WordUtil.getString(R.string.daily_task_1);
        mStatus2 = WordUtil.getString(R.string.daily_task_2);
        mColor0 = ContextCompat.getColor(mContext, R.color.gray3);
        mColor1 = ContextCompat.getColor(mContext, R.color.white);
        mDrawable0 = ContextCompat.getDrawable(mContext, R.drawable.bg_daily_task_03);
        mDrawable1 = ContextCompat.getDrawable(mContext, R.drawable.bg_daily_task_04);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new Vh(mInflater.inflate(R.layout.item_daily_task, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder vh, int i) {

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder vh, int i, @NonNull List payloads) {
        Object payload = payloads.size() > 0 ? payloads.get(0) : null;
        ((Vh) vh).setData(mList.get(i), i, payload);
    }

    class Vh extends RecyclerView.ViewHolder {

        TextView mTitle;
        TextView mTip;
        TextView mStatus;
        View mLine;

        public Vh(@NonNull View itemView) {
            super(itemView);
            mTitle = itemView.findViewById(R.id.title);
            mTip = itemView.findViewById(R.id.tip);
            mStatus = itemView.findViewById(R.id.btn_status);
            mLine = itemView.findViewById(R.id.line);
            mStatus.setOnClickListener(mOnClickListener);
        }

        void setData(DailyTaskBean bean, int position, Object payload) {
            if (payload == null) {
                mTitle.setText(bean.getTitle());
                mTip.setText(bean.getTip());
                mStatus.setTag(position);
                if (position == mList.size() - 1) {
                    if (mLine.getVisibility() == View.VISIBLE) {
                        mLine.setVisibility(View.INVISIBLE);
                    }
                } else {
                    if (mLine.getVisibility() != View.VISIBLE) {
                        mLine.setVisibility(View.VISIBLE);
                    }
                }
            }
            int status = bean.getStatus();
            if (status == 0) {
                mStatus.setText(mStatus0);
                mStatus.setBackground(null);
                mStatus.setTextColor(mColor0);
            } else {
                if (status == 1) {
                    mStatus.setText(mStatus1);
                    mStatus.setBackground(mDrawable1);
                } else {
                    mStatus.setText(mStatus2);
                    mStatus.setBackground(mDrawable0);
                }
                mStatus.setTextColor(mColor1);
            }
        }
    }
}
