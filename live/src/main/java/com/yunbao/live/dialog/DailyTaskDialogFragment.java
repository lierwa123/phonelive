package com.yunbao.live.dialog;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.yunbao.common.dialog.AbsDialogFragment;
import com.yunbao.common.http.HttpCallback;
import com.yunbao.live.R;
import com.yunbao.live.adapter.DailyTaskAdapter;
import com.yunbao.live.bean.DailyTaskBean;
import com.yunbao.live.http.LiveHttpConsts;
import com.yunbao.live.http.LiveHttpUtil;

import java.util.List;

public class DailyTaskDialogFragment extends AbsDialogFragment {

    private String mLiveUid;

    @Override
    protected int getLayoutId() {
        return R.layout.dialog_daily_task;
    }

    @Override
    protected int getDialogStyle() {
        return R.style.dialog;
    }

    @Override
    protected boolean canCancel() {
        return true;
    }

    @Override
    protected void setWindowAttributes(Window window) {
        WindowManager.LayoutParams params = window.getAttributes();
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.height = WindowManager.LayoutParams.MATCH_PARENT;
        params.horizontalMargin = 0.1f;
        params.verticalMargin = 0.1f;
        window.setAttributes(params);
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        findViewById(R.id.btn_close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        LiveHttpUtil.getDailyTask(mLiveUid, 1, new HttpCallback() {
            @Override
            public void onSuccess(int code, String msg, String[] info) {
                if (code == 0 && info.length > 0) {
                    JSONObject obj = JSON.parseObject(info[0]);
                    TextView tip = findViewById(R.id.tip);
                    tip.setText(obj.getString("tip_m"));
                    RecyclerView recyclerView = findViewById(R.id.recyclerView);
                    recyclerView.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
                    List<DailyTaskBean> list = JSON.parseArray(obj.getString("list"), DailyTaskBean.class);
                    recyclerView.setAdapter(new DailyTaskAdapter(mContext, list));
                }
            }
        });
    }

    public void setLiveUid(String liveUid) {
        mLiveUid = liveUid;
    }

    @Override
    public void onDestroy() {
        LiveHttpUtil.cancel(LiveHttpConsts.GET_DAILY_TASK);
        LiveHttpUtil.cancel(LiveHttpConsts.DAILY_TASK_REWARD);
        super.onDestroy();
    }
}
