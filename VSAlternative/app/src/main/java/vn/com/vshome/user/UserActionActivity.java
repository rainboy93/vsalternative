package vn.com.vshome.user;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;

import java.util.List;

import eu.davidea.flexibleadapter.common.SmoothScrollLinearLayoutManager;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import in.workarounds.typography.Button;
import in.workarounds.typography.EditText;
import in.workarounds.typography.TextView;
import vn.com.vshome.BaseActivity;
import vn.com.vshome.R;
import vn.com.vshome.VSHome;
import vn.com.vshome.callback.UserControlCallback;
import vn.com.vshome.database.DatabaseService;
import vn.com.vshome.database.User;
import vn.com.vshome.flexibleadapter.BaseAdapter;
import vn.com.vshome.flexibleadapter.user.RoomItem;
import vn.com.vshome.networks.CommandMessage;
import vn.com.vshome.utils.Define;
import vn.com.vshome.utils.PasswordFlowerMask;
import vn.com.vshome.utils.TimeOutManager;
import vn.com.vshome.utils.Toaster;
import vn.com.vshome.view.ProgressHUD;

public class UserActionActivity extends BaseActivity implements View.OnClickListener,
        TimeOutManager.TimeOutCallback, UserControlCallback {

    private List<AbstractFlexibleItem> mListRoom;
    private RecyclerView mRecyclerView;
    private BaseAdapter mAdapter;
    private ImageButton mSave, mEditPassword;
    private EditText mUsername, mPassword;
    private User mUser;
    private int requestCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_action);
        initView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        VSHome.socketManager.receiveThread.setUserCallback(this);
    }

    @Override
    protected void onPause() {
        VSHome.socketManager.receiveThread.setUserCallback(null);
        super.onPause();
    }

    private void initView() {

        mUser = (User) getIntent().getSerializableExtra(Define.INTENT_USER);

        mEditPassword = (ImageButton) findViewById(R.id.user_button_change_password);

        if (mUser != null) {
            requestCode = Define.CODE_USER_EDIT;
            mEditPassword.setVisibility(View.VISIBLE);
            mEditPassword.setOnClickListener(this);
        } else {
            requestCode = Define.CODE_USER_CREATE;
            mEditPassword.setVisibility(View.GONE);
        }

        mRecyclerView = (RecyclerView) findViewById(R.id.user_list_room);
        mListRoom = DatabaseService.getListRoomAddUser(mUser);
        mAdapter = new BaseAdapter(mListRoom);
        mRecyclerView.setLayoutManager(new SmoothScrollLinearLayoutManager(this));
        mRecyclerView.setHasFixedSize(true);
        ((SimpleItemAnimator) mRecyclerView.getItemAnimator()).setSupportsChangeAnimations(false);
        mRecyclerView.setAdapter(mAdapter);

        mSave = (ImageButton) findViewById(R.id.user_button_save);
        mSave.setOnClickListener(this);

        mUsername = (EditText) findViewById(R.id.user_username);
        mPassword = (EditText) findViewById(R.id.user_password);

        if (requestCode == Define.CODE_USER_EDIT) {
            mUsername.setText(mUser.username);
            mPassword.setTransformationMethod(new PasswordFlowerMask());
            mPassword.setText("password");
            mUsername.setEnabled(false);
            mPassword.setEnabled(false);
        }
    }

    @Override
    public void onClick(View v) {
        if (v == mSave) {
            if (requestCode == Define.CODE_USER_CREATE) {
                String username = mUsername.getText().toString();
                String password = mPassword.getText().toString();

                if (username.equals("") || password.equals("")) {
                    Toaster.showMessage(UserActionActivity.this, "Bạn chưa nhập tên hoặc mật khẩu!");
                    return;
                }
                if (password.length() < 4 || password.length() > 12) {
                    Toaster.showMessage(UserActionActivity.this, "Hãy nhập vào mật khẩu từ 4-12 kí tự!");
                    return;
                }

                List<User> list = User.find(User.class, "username = ?", new String[]{username});
                if (list != null && list.size() > 0) {
                    Toaster.showMessage(UserActionActivity.this, "Tên người dùng đã tồn tại!");
                    return;
                }

                CommandMessage newUser = new CommandMessage();
                mUser = new User();
                mUser.username = mUsername.getText().toString();
                mUser.password = mPassword.getText().toString();
                mUser.priority = Define.PRIORITY_USER;
                mUser.status = Define.USER_STATUS_ENABLE;
                newUser.setCreateUser(mUser, getRoomCode());
                mUser.roomControl = roomString.toString();
                ProgressHUD.showLoading(this);
                TimeOutManager.getInstance().startCountDown(this, 5);
                VSHome.socketManager.sendMessage(newUser);
            } else {
                if (VSHome.currentUser != null && VSHome.currentUser.priority == Define.PRIORITY_USER) {
                    setResult(Activity.RESULT_CANCELED);
                    finish();
                    return;
                }

                CommandMessage editUser = new CommandMessage();
                editUser.setUpdateRoomUser(mUser, getRoomCode());
                mUser.roomControl = roomString.toString();
                ProgressHUD.showLoading(this);
                TimeOutManager.getInstance().startCountDown(this, 5);
                VSHome.socketManager.sendMessage(editUser);
            }
        } else if (v == mEditPassword) {
            showChangePasswordDialog();
        }
    }

    private StringBuilder roomString;

    private int[] getRoomCode() {
        roomString = new StringBuilder();
        int[] result = new int[10];
        int[] binary = new int[80];
        for (int i = 0; i < mListRoom.size(); i++) {
            RoomItem item = (RoomItem) mListRoom.get(i);
            if (item.isSelected) {
                binary[item.room.getId().intValue() - 1] = 1;
            }
        }

        for (int i = 0; i < 10; i++) {
            StringBuilder str = new StringBuilder();
            for (int j = 0; j < 8; j++) {
                str.append(binary[i * 8 + j] + "");
                roomString.append(binary[i * 8 + j] + "");
            }
            int decimal = Integer.parseInt(str.toString(), 2);
            result[i] = decimal;
        }
        return result;
    }

    @Override
    public void onTimeOut() {
        ProgressHUD.hideLoading(UserActionActivity.this);
        Toaster.showMessage(UserActionActivity.this, "Có lỗi xảy ra. Hãy thử lại.");
    }

    @Override
    public void onControl(int id) {

    }

    @Override
    public void onResponse(int cmd, int status) {
        ProgressHUD.hideLoading(this);
        TimeOutManager.getInstance().cancelCountDown();
        switch (cmd) {
            case CommandMessage.CMD_ADD_NEW_USER:
            case CommandMessage.CMD_UPDATE_USER_ROOM:
                if (status == CommandMessage.STATUS_ERROR) {
                    Toaster.showMessage(UserActionActivity.this, "Có lỗi xảy ra. Hãy thử lại.");
                    return;
                }
                long id = mUser.save();
                Intent intent = new Intent();
                intent.putExtra("UserId", (int) id);
                setResult(Activity.RESULT_OK, intent);
                finish();
                break;
            case CommandMessage.CMD_UPDATE_USER_PASSWORD:
                if (status == CommandMessage.STATUS_ERROR) {
                    Toaster.showMessage(UserActionActivity.this, "Cập nhật mật khẩu không thành công!");
                } else {
                    Toaster.showMessage(UserActionActivity.this, "Cập nhật mật khẩu thành công!");
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void onGetUserListDone() {

    }

    private void showChangePasswordDialog() {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.view_dialog_change_password);

        ImageButton buttonCancel = (ImageButton) dialog.findViewById(R.id.dialog_change_password_button_cancel);
        Button buttonSave = (Button) dialog.findViewById(R.id.dialog_change_password_button_confirm);
        EditText oldPassword = (EditText) dialog.findViewById(R.id.dialog_change_password_old_password);
        oldPassword.setTransformationMethod(new PasswordFlowerMask());
        final EditText newPassword = (EditText) dialog.findViewById(R.id.dialog_change_password_new_password);
        newPassword.setTransformationMethod(new PasswordFlowerMask());

        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });

        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String np = newPassword.getText().toString();
                if (np.equals("")) {
                    Toaster.showMessage(UserActionActivity.this, "Hãy nhập vào mật khẩu mới!");
                    return;
                } else if (np.length() < 4 || np.length() > 12) {
                    Toaster.showMessage(UserActionActivity.this, "Hãy nhập vào mật khẩu từ 4-12 kí tự!");
                    return;
                }
                dialog.cancel();
                CommandMessage changePassword = new CommandMessage();
                changePassword.setUpdatePassword(mUser.username, newPassword.getText().toString());
                ProgressHUD.showLoading(UserActionActivity.this);
                TimeOutManager.getInstance().startCountDown(UserActionActivity.this, 5);
                VSHome.socketManager.sendMessage(changePassword);
            }
        });
        dialog.show();
    }
}
