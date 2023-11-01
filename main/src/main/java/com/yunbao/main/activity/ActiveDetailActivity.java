package com.yunbao.main.activity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.ViewGroup;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.yunbao.common.CommonAppConfig;
import com.yunbao.common.Constants;
import com.yunbao.common.activity.AbsActivity;
import com.yunbao.common.adapter.RefreshAdapter;
import com.yunbao.common.bean.UserBean;
import com.yunbao.common.custom.CommonRefreshView;
import com.yunbao.common.custom.InterceptFrameLayout;
import com.yunbao.common.event.FollowEvent;
import com.yunbao.common.http.HttpCallback;
import com.yunbao.common.interfaces.OnItemClickListener;
import com.yunbao.common.utils.DialogUitl;
import com.yunbao.common.utils.KeyBoardUtil;
import com.yunbao.common.utils.StringUtil;
import com.yunbao.common.utils.ToastUtil;
import com.yunbao.common.utils.WordUtil;
import com.yunbao.common.views.InputViewHolder;
import com.yunbao.main.R;
import com.yunbao.main.adapter.ActiveCommentAdapter;
import com.yunbao.main.bean.ActiveBean;
import com.yunbao.main.bean.ActiveCommentBean;
import com.yunbao.main.event.ActiveCommentEvent;
import com.yunbao.main.http.MainHttpConsts;
import com.yunbao.main.http.MainHttpUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.Arrays;
import java.util.List;

/**
 * 动态详情页面
 */
public class ActiveDetailActivity extends AbsActivity implements ActiveCommentAdapter.ActionListener, OnItemClickListener<ActiveCommentBean>, KeyBoardUtil.KeyBoardHeightListener {

    public static void forward(Context context, ActiveBean activeBean) {
        Intent intent = new Intent(context, ActiveDetailActivity.class);
        intent.putExtra(Constants.ACTIVE_BEAN, activeBean);
        context.startActivity(intent);
    }

    private CommonRefreshView mRefreshView;
    private ActiveCommentAdapter mAdapter;
    private ActiveBean mActiveBean;
    private ActiveCommentBean mActiveCommentBean;
    private InputViewHolder mInputViewHolder;
    private KeyBoardUtil mKeyBoardUtil;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_active_detail;
    }

    @Override
    protected void main() {
        setTitle(WordUtil.getString(R.string.active_detail));
        EventBus.getDefault().register(this);
        mActiveBean = getIntent().getParcelableExtra(Constants.ACTIVE_BEAN);
        mInputViewHolder = new InputViewHolder(mContext,
                (ViewGroup) findViewById(R.id.input_container),
                false, R.layout.view_input_top_active, R.layout.view_input_face
        );
        mInputViewHolder.addToParent();
        mInputViewHolder.subscribeActivityLifeCycle();
        mInputViewHolder.setActionListener(new InputViewHolder.ActionListener() {
            @Override
            public void onSendClick(String text) {
                sendComment(text);
            }
        });
        InterceptFrameLayout groupIntercept = findViewById(R.id.group_intercept);
        groupIntercept.setOnInterceptListener(new InterceptFrameLayout.OnInterceptListener() {
            @Override
            public boolean onInterceptCall() {
                if (mInputViewHolder != null) {
                    return mInputViewHolder.hideKeyBoardFaceMore();
                }
                return false;
            }
        });
        mRefreshView = findViewById(R.id.refreshView);
        mRefreshView.setEmptyLayoutId(R.layout.view_no_data_active_comment);
        mRefreshView.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
        mRefreshView.setDataHelper(new CommonRefreshView.DataHelper<ActiveCommentBean>() {
            @Override
            public RefreshAdapter<ActiveCommentBean> getAdapter() {
                if (mAdapter == null) {
                    mAdapter = new ActiveCommentAdapter(mContext, mActiveBean);
                    mAdapter.setOnItemClickListener(ActiveDetailActivity.this);
                    mAdapter.setActionListener(ActiveDetailActivity.this);
                }
                return mAdapter;
            }

            @Override
            public void loadData(int p, HttpCallback callback) {
                if (mActiveBean != null) {
                    MainHttpUtil.getActiveComments(mActiveBean.getId(), p, callback);
                }
            }

            @Override
            public List<ActiveCommentBean> processData(String[] info) {
                JSONObject obj = JSON.parseObject(info[0]);
                if (mAdapter != null) {
                    mAdapter.setCommentNum(obj.getIntValue("comments"));
                }
                List<ActiveCommentBean> list = JSON.parseArray(obj.getString("commentlist"), ActiveCommentBean.class);
                for (ActiveCommentBean bean : list) {
                    if (bean != null) {
                        bean.setParentNode(true);
                    }
                }
                return list;
            }

            @Override
            public void onRefreshSuccess(List<ActiveCommentBean> list, int listCount) {
                if (mInputViewHolder != null) {
                    mInputViewHolder.clearEditText();
                    mInputViewHolder.setDefaultHint();
                }
                mActiveCommentBean = null;
            }

            @Override
            public void onRefreshFailure() {

            }

            @Override
            public void onLoadMoreSuccess(List<ActiveCommentBean> loadItemList, int loadItemCount) {

            }

            @Override
            public void onLoadMoreFailure() {

            }
        });
        mRefreshView.initData();
        mKeyBoardUtil = new KeyBoardUtil(mRefreshView, this);
    }


    /**
     * 发表评论
     */
    private void sendComment(String content) {
        if (TextUtils.isEmpty(content)) {
            ToastUtil.show(R.string.content_empty);
            return;
        }
        if (mActiveBean == null) {
            return;
        }
        String toUid = mActiveBean.getUid();
        String commentId = "0";
        String parentId = "0";
        if (mActiveCommentBean != null) {
            toUid = mActiveCommentBean.getUid();
            commentId = mActiveCommentBean.getCommentId();
            parentId = mActiveCommentBean.getId();
        }
        MainHttpUtil.activeComment(mActiveBean.getId(), toUid, commentId, parentId, content, new HttpCallback() {
            @Override
            public void onSuccess(int code, String msg, String[] info) {
                if (code == 0) {
                    if (info.length > 0) {
                        JSONObject obj = JSON.parseObject(info[0]);
                        EventBus.getDefault().post(new ActiveCommentEvent(mActiveBean.getId(), obj.getIntValue("comments")));
                        ToastUtil.show(msg);
                        if (mInputViewHolder != null) {
                            mInputViewHolder.hideKeyBoardFaceMore();
                            mInputViewHolder.clearEditText();
                            mInputViewHolder.setDefaultHint();
                        }
                        mActiveCommentBean = null;
                    }
                } else {
                    ToastUtil.show(msg);
                }
            }
        });
    }


    @Override
    public void onExpandClicked(final ActiveCommentBean commentBean) {
        final ActiveCommentBean parentNodeBean = commentBean.getParentNodeBean();
        if (parentNodeBean == null) {
            return;
        }
        MainHttpUtil.getActiveCommentReply(parentNodeBean.getId(), parentNodeBean.getChildPage(), new HttpCallback() {
            @Override
            public void onSuccess(int code, String msg, String[] info) {
                if (code == 0) {
                    List<ActiveCommentBean> list = JSON.parseArray(Arrays.toString(info), ActiveCommentBean.class);
                    if (list == null || list.size() == 0) {
                        return;
                    }
                    if (parentNodeBean.getChildPage() == 1) {
                        if (list.size() > 1) {
                            list = list.subList(1, list.size());
                        }
                    }
                    for (ActiveCommentBean bean : list) {
                        bean.setParentNodeBean(parentNodeBean);
                    }
                    List<ActiveCommentBean> childList = parentNodeBean.getChildList();
                    if (childList != null) {
                        childList.addAll(list);
                        if (childList.size() < parentNodeBean.getReplyNum()) {
                            parentNodeBean.setChildPage(parentNodeBean.getChildPage() + 1);
                        }
                        if (mAdapter != null) {
                            mAdapter.notifyDataSetChanged();
                        }
                    }
                }
            }
        });
    }


    @Override
    public void onCollapsedClicked(ActiveCommentBean commentBean) {
        ActiveCommentBean parentNodeBean = commentBean.getParentNodeBean();
        if (parentNodeBean == null) {
            return;
        }
        List<ActiveCommentBean> childList = parentNodeBean.getChildList();
        parentNodeBean.removeChild();
        parentNodeBean.setChildPage(1);
        if (mAdapter != null) {
            mAdapter.notifyDataSetChanged();
        }
    }

    /**
     * 长按事件
     */
    @Override
    public void onItemLongClick(final ActiveCommentBean bean) {
        if (mActiveBean == null) {
            return;
        }
        String uid = CommonAppConfig.getInstance().getUid();
        Integer[] arr = null;
        //是否是自己的视频
        String activeUid = mActiveBean.getUid();
        boolean isSelfVideo = !TextUtils.isEmpty(activeUid) && activeUid.equals(uid);
        //是否是自己的评论
        String commentUid = bean.getUid();
        boolean isSelfComment = !TextUtils.isEmpty(commentUid) && commentUid.equals(uid);
        if (isSelfVideo || isSelfComment) {
            arr = new Integer[]{R.string.copy, R.string.delete};
        } else {
            arr = new Integer[]{R.string.copy};
        }
        if (arr == null) {
            return;
        }
        DialogUitl.showStringArrayDialog(mContext, arr, new DialogUitl.StringArrayDialogCallback() {
            @Override
            public void onItemClick(String text, int tag) {
                if (tag == R.string.copy) {//复制评论
                    UserBean u = bean.getUserBean();
                    if (u != null) {
                        String content = StringUtil.contact("@", u.getUserNiceName(), ": ", bean.getContent());
                        ClipboardManager cm = (ClipboardManager) mContext.getSystemService(Context.CLIPBOARD_SERVICE);
                        ClipData clipData = ClipData.newPlainText("text", content);
                        cm.setPrimaryClip(clipData);
                        ToastUtil.show(R.string.copy_success);
                    }
                } else if (tag == R.string.delete) {
                    //删除评论
                    MainHttpUtil.deleteComment(mActiveBean.getId(), bean.getId(), bean.getUid(), new HttpCallback() {

                        @Override
                        public void onSuccess(int code, String msg, String[] info) {
                            if (code == 0) {
                                if (mRefreshView != null) {
                                    mRefreshView.initData();
                                }
                            }
                            ToastUtil.show(msg);
                        }
                    });

                }
            }
        });
    }


    @Override
    public void onItemClick(ActiveCommentBean bean, int position) {
        if (bean != null && !TextUtils.isEmpty(bean.getUid()) && bean.getUid().equals(CommonAppConfig.getInstance().getUid())) {
            ToastUtil.show(R.string.video_cannot_apply_self);
            return;
        }
        mActiveCommentBean = bean;
        if (mInputViewHolder != null) {
            if (bean != null) {
                UserBean replyUserBean = bean.getUserBean();//要回复的人
                if (replyUserBean != null) {
                    mInputViewHolder.clearEditText();
                    mInputViewHolder.setEditHint(StringUtil.contact(WordUtil.getString(R.string.video_comment_reply_2), replyUserBean.getUserNiceName()));
                }
            }
            mInputViewHolder.showKeyBoard();
        }
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onActiveCommentEvent(ActiveCommentEvent e) {
        if (mRefreshView != null) {
            mRefreshView.initData();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onFollowEvent(FollowEvent e) {
        if (mActiveBean != null && mActiveBean.getUid().equals(e.getToUid())) {
            if (mAdapter != null) {
                mAdapter.onFollowChanged(e.getIsAttention());
            }
        }
    }

    @Override
    public void onKeyBoardHeightChanged(int keyboardHeight) {
        if (mInputViewHolder != null) {
            mInputViewHolder.onKeyBoardHeightChanged(keyboardHeight);
        }
    }


    @Override
    protected void onDestroy() {
        if (mKeyBoardUtil != null) {
            mKeyBoardUtil.release();
        }
        mKeyBoardUtil = null;
        EventBus.getDefault().unregister(this);
        MainHttpUtil.cancel(MainHttpConsts.GET_ACTIVE_COMMENTS);
        MainHttpUtil.cancel(MainHttpConsts.ACTIVE_COMMENT);
        MainHttpUtil.cancel(MainHttpConsts.SET_ACTIVE_COMMENT_LIKE);
        MainHttpUtil.cancel(MainHttpConsts.GET_ACTIVE_COMMENT_REPLY);
        MainHttpUtil.cancel(MainHttpConsts.DELETE_COMMENT);
        if (mAdapter != null) {
            mAdapter.release();
        }
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        if (mInputViewHolder != null && mInputViewHolder.hideKeyBoardFaceMore()) {
            return;
        }
        super.onBackPressed();
    }
}
