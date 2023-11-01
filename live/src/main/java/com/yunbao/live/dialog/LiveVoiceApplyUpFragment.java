package com.yunbao.live.dialog;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.yunbao.common.CommonAppConfig;
import com.yunbao.common.Constants;
import com.yunbao.common.bean.UserBean;
import com.yunbao.common.dialog.AbsDialogFragment;
import com.yunbao.common.glide.ImgLoader;
import com.yunbao.common.http.HttpCallback;
import com.yunbao.common.utils.DpUtil;
import com.yunbao.common.utils.ToastUtil;
import com.yunbao.common.utils.WordUtil;
import com.yunbao.live.R;
import com.yunbao.live.activity.LiveAnchorActivity;
import com.yunbao.live.activity.LiveAudienceActivity;
import com.yunbao.live.adapter.LiveVoiceApplyUpAdapter;
import com.yunbao.live.http.LiveHttpConsts;
import com.yunbao.live.http.LiveHttpUtil;

import java.util.List;

/**
 * 语音聊天室 观众申请上麦
 */
public class LiveVoiceApplyUpFragment extends AbsDialogFragment implements View.OnClickListener, LiveVoiceApplyUpAdapter.ActionListener {

    private TextView mTitle;
    private TextView mBtnApply;
    private String mStream;
    private boolean mIsAnchor;
    private boolean mApply;

    @Override
    protected int getLayoutId() {
        return R.layout.dialog_live_voice_apply;
    }

    @Override
    protected int getDialogStyle() {
        return R.style.dialog2;
    }

    @Override
    protected boolean canCancel() {
        return true;
    }

    @Override
    protected void setWindowAttributes(Window window) {
        window.setWindowAnimations(R.style.bottomToTopAnim);
        WindowManager.LayoutParams params = window.getAttributes();
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.height = DpUtil.dp2px(340);
        params.gravity = Gravity.BOTTOM;
        window.setAttributes(params);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle == null) {
            return;
        }
        mStream = bundle.getString(Constants.LIVE_STREAM);
        mIsAnchor = bundle.getBoolean(Constants.ANCHOR);
        mTitle = findViewById(R.id.title);
        if (mIsAnchor) {
            findViewById(R.id.group_bottom).setVisibility(View.GONE);
        } else {
            mBtnApply = findViewById(R.id.btn_apply);
        }
        LiveHttpUtil.getVoiceMicApplyList(mStream, new HttpCallback() {
            @Override
            public void onSuccess(int code, String msg, String[] info) {
                if (code == 0 && info.length > 0) {
                    JSONObject obj = JSON.parseObject(info[0]);
                    List<UserBean> list = JSON.parseArray(obj.getString("apply_list"), UserBean.class);
                    if (mIsAnchor) {
                        if (mTitle != null) {
                            mTitle.setText(String.format(WordUtil.getString(R.string.a_044), list.size()));
                        }
                        RecyclerView recyclerView = findViewById(R.id.recyclerView);
                        if (recyclerView != null) {
                            recyclerView.setVisibility(View.VISIBLE);
                            recyclerView.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
                            LiveVoiceApplyUpAdapter adapter = new LiveVoiceApplyUpAdapter(mContext, list, true);
                            adapter.setActionListener(LiveVoiceApplyUpFragment.this);
                            recyclerView.setAdapter(adapter);
                        }
                    } else {
                        int position = obj.getIntValue("position");
                        mApply = position > 0;//已经申请上麦了
                        if (mApply) {
                            if (mTitle != null) {
                                mTitle.setText(String.format(WordUtil.getString(R.string.a_043), position));
                            }
                            if (mBtnApply != null) {
                                mBtnApply.setOnClickListener(LiveVoiceApplyUpFragment.this);
                                mBtnApply.setText(R.string.a_042);
                                mBtnApply.setTextColor(ContextCompat.getColor(mContext, R.color.gray3));
                            }
                            RecyclerView recyclerView = findViewById(R.id.recyclerView);
                            if (recyclerView != null) {
                                recyclerView.setVisibility(View.VISIBLE);
                                recyclerView.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
                                recyclerView.setAdapter(new LiveVoiceApplyUpAdapter(mContext, list, false));
                            }
                        } else {
                            if (mTitle != null) {
                                mTitle.setText(R.string.a_040);
                            }
                            if (mBtnApply != null) {
                                mBtnApply.setOnClickListener(LiveVoiceApplyUpFragment.this);
                                mBtnApply.setText(R.string.a_041);
                                mBtnApply.setTextColor(ContextCompat.getColor(mContext, R.color.blue1));
                            }
                            findViewById(R.id.group_no_apply).setVisibility(View.VISIBLE);
                            ImageView avatar = findViewById(R.id.avatar);
                            TextView name = findViewById(R.id.name);
                            UserBean u = CommonAppConfig.getInstance().getUserBean();
                            if (u != null) {
                                ImgLoader.displayAvatar(mContext, u.getAvatar(), avatar);
                                name.setText(u.getUserNiceName());
                            }

                        }
                    }

                }
            }
        });


    }

    @Override
    public void onClick(View v) {
        if (mApply) {
            LiveHttpUtil.cancelVoiceLiveMic(mStream, new HttpCallback() {
                @Override
                public void onSuccess(int code, String msg, String[] info) {
                    if (code == 0) {
                        dismiss();
                    }
                    ToastUtil.show(msg);
                }
            });
        } else {
            LiveHttpUtil.applyVoiceLiveMic(mStream, new HttpCallback() {
                @Override
                public void onSuccess(int code, String msg, String[] info) {
                    if (code == 0) {
                        if (mContext != null) {
                            ((LiveAudienceActivity) mContext).applyMicUp();
                        }
                        dismiss();
                    }
                    ToastUtil.show(msg);
                }
            });
        }

    }


    /**
     * 主播同意/拒绝用户上麦申请
     *
     * @param isAgree 0 拒绝 1 同意
     */
    @Override
    public void onAgreeUpMicClick(final UserBean bean, int isAgree) {
        LiveHttpUtil.handleVoiceMicApply(mStream, bean.getId(), isAgree, new HttpCallback() {
            @Override
            public void onSuccess(int code, String msg, String[] info) {
                if (code == 0 && info.length > 0) {
                    if (mContext != null) {
                        int position = JSON.parseObject(info[0]).getIntValue("position");
                        ((LiveAnchorActivity) mContext).handleMicUpApply(bean, position);
                    }
                    dismiss();
                }
                ToastUtil.show(msg);
            }
        });
    }

    @Override
    public void onDestroy() {
        mContext = null;
        LiveHttpUtil.cancel(LiveHttpConsts.APPLY_VOICE_LIVE_MIC);
        LiveHttpUtil.cancel(LiveHttpConsts.CANCEL_VOICE_LIVE_MIC);
        LiveHttpUtil.cancel(LiveHttpConsts.HANDLE_VOICE_MIC_APPLY);
        super.onDestroy();
    }

}
