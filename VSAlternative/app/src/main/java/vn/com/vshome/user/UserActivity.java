package vn.com.vshome.user;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;
import android.text.Html;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageButton;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import in.workarounds.typography.Button;
import in.workarounds.typography.TextView;
import vn.com.vshome.BaseActivity;
import vn.com.vshome.R;
import vn.com.vshome.VSHome;
import vn.com.vshome.callback.DialogCallback;
import vn.com.vshome.callback.UserControlCallback;
import vn.com.vshome.communication.SocketManager;
import vn.com.vshome.database.User;
import vn.com.vshome.dialog.DeleteDialog;
import vn.com.vshome.flexibleadapter.user.UserAdapter;
import vn.com.vshome.foscamsdk.CameraManager;
import vn.com.vshome.networks.CommandMessage;
import vn.com.vshome.utils.Define;
import vn.com.vshome.utils.MiscUtils;
import vn.com.vshome.utils.TimeOutManager;
import vn.com.vshome.utils.Toaster;
import vn.com.vshome.utils.Utils;
import vn.com.vshome.view.ProgressHUD;

public class UserActivity extends BaseActivity implements OnClickListener, UserControlCallback,
        TimeOutManager.TimeOutCallback, UserAdapter.UserControlCallback {

    private ImageButton mMenu;
    private ImageButton mHome;
    private TextView mTitle;
    private User mCurrentUser;
    private int currentPosition = -1;

    private RecyclerView mRecyclerView;
    private FloatingActionButton mAdd;
    private UserAdapter mAdapter;
    private ArrayList<User> mListUser;
    private ImageButton mShutDown;

    @Override
    protected void onStart() {
        super.onStart();
        try {
            SocketManager.getInstance().receiveThread.setUserCallback(this);
        } catch (Exception e) {

        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        try {
            SocketManager.getInstance().receiveThread.setUserCallback(this);
        } catch (Exception e) {

        }
    }

    @Override
    protected void onPause() {
        try {
            SocketManager.getInstance().receiveThread.setUserCallback(null);
        } catch (Exception e) {

        }
        super.onPause();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        initActionBar();
        initView();

        User.deleteAll(User.class);
        if (VSHome.currentUser.priority == Define.PRIORITY_ADMIN) {
            getUserList();
        } else {
            VSHome.currentUser.save();
            loadUserList();
        }
    }

    private void initView() {
        mRecyclerView = (RecyclerView) findViewById(R.id.user_recycler_view);
        ((SimpleItemAnimator) mRecyclerView.getItemAnimator()).setSupportsChangeAnimations(false);

        mAdd = (FloatingActionButton) findViewById(R.id.user_button_add);
        if (VSHome.currentUser.priority != Define.PRIORITY_ADMIN) {
            mAdd.setVisibility(View.GONE);
        } else {
            mAdd.setOnClickListener(this);
        }

        mShutDown = (ImageButton) findViewById(R.id.user_button_shut_down);
        mShutDown.setOnClickListener(this);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator() {
            @Override
            public boolean canReuseUpdatedViewHolder(RecyclerView.ViewHolder viewHolder) {
                return true;
            }
        });
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (dy > 0)
                    mAdd.hide();
                else if (dy < 0)
                    mAdd.show();
            }
        });
    }

    private void initActionBar() {
        mMenu = (ImageButton) findViewById(R.id.action_bar_menu);
        mMenu.setImageResource(R.drawable.icon_back);
        mHome = (ImageButton) findViewById(R.id.action_bar_home);
        mTitle = (TextView) findViewById(R.id.action_bar_title);
        mTitle.setText("Xin chào " + VSHome.currentUser.username);

        mMenu.setOnClickListener(this);
        mHome.setOnClickListener(this);
    }

    private void getUserList() {
        CommandMessage getUserList = new CommandMessage();
        getUserList.cmd = CommandMessage.CMD_GET_USER_LIST;

        ProgressHUD.showLoading(this);
        TimeOutManager.getInstance().startCountDown(this, 10);

        SocketManager.getInstance().sendMessage(getUserList);
    }

    private void loadUserList() {
        List<User> list = User.listAll(User.class, "id");
        mListUser = new ArrayList<>();
        if (list != null && list.size() > 0) {
            mListUser.addAll(list);
        }
        mAdapter = new UserAdapter(this, mListUser);
        mAdapter.setUserControlCallback(this);
        SocketManager.getInstance().receiveThread.setUserCallback(this);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public void onClick(View v) {
        if (v == mMenu || v == mHome) {
            onBackPressed();
        } else if (v == mAdd) {
            Intent intent = new Intent(this, UserActionActivity.class);
            startActivityForResult(intent, Define.CODE_USER_CREATE);
        } else if (v == mShutDown) {
            showConfirmDisconnectDialog();
        }
    }

    @Override
    public void onControl(int id) {

    }

    @Override
    public void onResponse(int cmd, int status) {
        ProgressHUD.hideLoading(this);
        TimeOutManager.getInstance().cancelCountDown();
        if (status == CommandMessage.STATUS_ERROR) {
            Utils.showConfirmDialog("Lỗi", "Có lỗi xảy ra. Thử lại?",
                    new DialogCallback() {
                        @Override
                        public void onConfirm() {
                            User.deleteAll(User.class);
                            getUserList();
                        }
                    });
            return;
        }
        switch (cmd) {
            case CommandMessage.CMD_UPDATE_USER_STATUS:
                MiscUtils.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mAdapter.notifyItemRangeChanged(0, mAdapter.getItemCount());
                    }
                });
                break;
            case CommandMessage.CMD_DELETE_USER:
                MiscUtils.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mCurrentUser = mListUser.remove(currentPosition);
                        mCurrentUser.delete();
                        mCurrentUser = null;
                        mAdapter.notifyItemRemoved(currentPosition);
                        mAdapter.notifyItemRangeChanged(0, mAdapter.getItemCount());
                        currentPosition = -1;
                    }
                });
                break;

            default:
                break;
        }
    }

    @Override
    public void onGetUserListDone() {
        ProgressHUD.hideLoading(this);
        TimeOutManager.getInstance().cancelCountDown();
        MiscUtils.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                loadUserList();
            }
        });
    }

    @Override
    public void onTimeOut() {
        ProgressHUD.hideLoading(this);
        Utils.showConfirmDialog("Lỗi", "Có lỗi xảy ra. Thử lại?",
                new DialogCallback() {
                    @Override
                    public void onConfirm() {
                        User.deleteAll(User.class);
                        getUserList();
                    }
                });
    }

    @Override
    public void onEditUser(User user, int position) {
        mAdapter.closeAllItems();
        Intent intent = new Intent(this, UserActionActivity.class);
        intent.putExtra(Define.INTENT_USER, user);
        currentPosition = position;
        startActivityForResult(intent, Define.CODE_USER_EDIT);
    }

    @Override
    public void onDeleteUser(User user, int position) {
        mAdapter.closeAllItems();
        for (int i = 0; i < mListUser.size(); i++) {
            if (user.getId().intValue() == mListUser.get(i).getId().intValue()) {
                currentPosition = i;
                break;
            }
        }
        showConfirmDeleteDialog(user);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_CANCELED) {
            currentPosition = -1;
            return;
        }

        int id = data.getIntExtra("UserId", 0);
        User user = User.findById(User.class, id);
        if (requestCode == Define.CODE_USER_EDIT) {
            mListUser.set(currentPosition, user);
            if (mAdapter != null) {
                mAdapter.updateData(mListUser);
                mAdapter.notifyItemChanged(currentPosition);
                currentPosition = -1;
            }
        } else if (requestCode == Define.CODE_USER_CREATE) {
            mListUser.add(user);
            if (mAdapter != null) {
                mAdapter.updateData(mListUser);
                mAdapter.notifyItemInserted(mListUser.size() - 1);
                mAdapter.notifyItemRangeChanged(0, mAdapter.getItemCount());
            }
        }
    }

    private void showConfirmDeleteDialog(final User user) {
        DeleteDialog deleteDialog = new DeleteDialog();
        String s = "Bạn muốn xóa <b>" + user.username.toUpperCase(Locale.US) + "</b>?";
        deleteDialog.setContent(s);
        deleteDialog.setCallback(new DialogCallback() {
            @Override
            public void onConfirm() {
                CommandMessage deleteUser = new CommandMessage();
                deleteUser.setDeleteUser(user.username);
                ProgressHUD.showLoading(UserActivity.this);
                TimeOutManager.getInstance().startCountDown(new TimeOutManager.TimeOutCallback() {
                    @Override
                    public void onTimeOut() {
                        ProgressHUD.hideLoading(UserActivity.this);
                        Utils.showErrorDialog("Lỗi", "Có lỗi xảy ra. Hãy thử lại.");
                    }
                }, 5);
                SocketManager.getInstance().sendMessage(deleteUser);
            }
        });
        deleteDialog.show(getFragmentManager(), DeleteDialog.class.getSimpleName());
    }

    private void showConfirmDisconnectDialog() {
        DeleteDialog disconnectDialog = new DeleteDialog();
        String s = VSHome.currentUser.priority == Define.PRIORITY_ADMIN ?
                "Bạn muốn thoát toàn bộ các tài khoản?" : "Bạn muốn thoát tài khoản " + VSHome.currentUser.username
                + "?";
        disconnectDialog.setContent(s);
        disconnectDialog.setIcon(R.drawable.default_user_avatar);
        disconnectDialog.setCallback(new DialogCallback() {
            @Override
            public void onConfirm() {
                if (VSHome.currentUser.priority == Define.PRIORITY_ADMIN) {
                    CommandMessage shutDown = new CommandMessage(CommandMessage.CMD_DISCONNECT_ALL_USER);
                    ProgressHUD.showLoading(UserActivity.this);
                    TimeOutManager.getInstance().startCountDown(UserActivity.this, 5);
                    SocketManager.getInstance().sendMessage(shutDown);
                } else {
                    SocketManager.getInstance().destroySocket();
                    Utils.restart();
                }
            }
        });
        disconnectDialog.show(getFragmentManager(), DeleteDialog.class.getSimpleName());
    }
}
