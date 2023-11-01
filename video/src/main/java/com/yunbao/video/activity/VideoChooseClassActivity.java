package com.yunbao.video.activity;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.yunbao.common.Constants;
import com.yunbao.common.activity.AbsActivity;
import com.yunbao.common.bean.VideoClassBean;
import com.yunbao.common.interfaces.OnItemClickListener;
import com.yunbao.common.utils.WordUtil;
import com.yunbao.video.R;
import com.yunbao.video.adapter.VideoChooseClassAdapter;

public class VideoChooseClassActivity extends AbsActivity implements OnItemClickListener<VideoClassBean> {

    private RecyclerView mRecyclerView;


    @Override
    protected int getLayoutId() {
        return R.layout.activity_video_choose_class;
    }

    @Override
    protected void main() {
        setTitle(WordUtil.getString(R.string.video_choose_class));
        int videoClassId = getIntent().getIntExtra(Constants.VIDEO_ID, 0);
        mRecyclerView = findViewById(R.id.recyclerView);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
        VideoChooseClassAdapter adapter = new VideoChooseClassAdapter(mContext, videoClassId);
        adapter.setOnItemClickListener(this);
        mRecyclerView.setAdapter(adapter);
    }

    @Override
    public void onItemClick(VideoClassBean bean, int position) {
        Intent intent = new Intent();
        intent.putExtra(Constants.VIDEO_ID, bean.getId());
        intent.putExtra(Constants.CLASS_NAME, bean.getName());
        setResult(RESULT_OK, intent);
        finish();
    }
}
