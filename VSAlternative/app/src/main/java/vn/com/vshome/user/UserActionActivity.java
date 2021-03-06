package vn.com.vshome.user;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
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
import vn.com.vshome.activitymanager.TheActivityManager;
import vn.com.vshome.callback.UserControlCallback;
import vn.com.vshome.communication.SocketManager;
import vn.com.vshome.database.DatabaseService;
import vn.com.vshome.database.User;
import vn.com.vshome.flexibleadapter.BaseAdapter;
import vn.com.vshome.flexibleadapter.user.RoomItem;
import vn.com.vshome.networks.CommandMessage;
import vn.com.vshome.utils.Define;
import vn.com.vshome.utils.PasswordFlowerMask;
import vn.com.vshome.utils.TimeOutManager;
import vn.com.vshome.utils.Toaster;
import vn.com.vshome.utils.Utils;
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

        initActionBar();
        initView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        try {
            SocketManager.getInstance().receiveThread.setUserCallback(this);
        } catch (NullPointerException ex) {

        }
    }

    @Override
    protected void onPause() {
        try {
            SocketManager.getInstance().receiveThread.setUserCallback(null);
        } catch (NullPointerException ex) {

        }
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

    private ImageButton mMenu;
    private ImageButton mHome;
    private TextView mTitle;

    private void initActionBar() {
        mMenu = (ImageButton) findViewById(R.id.action_bar_menu);
        mMenu.setImageResource(R.drawable.icon_back);
        mHome = (ImageButton) findViewById(R.id.action_bar_home);
        mTitle = (TextView) findViewById(R.id.action_bar_title);
        mTitle.setText("Xin chào " + VSHome.currentUser.username);

        mMenu.setOnClickListener(this);
        mHome.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == mSave) {
            if (requestCode == Define.CODE_USER_CREATE) {
                String username = mUsername.getText().toString();
                String password = mPassword.getText().toString();

                if (username.equals("") || password.equals("")) {
                    Toaster.showMessage(UserActionActivity.this,
                            Utils.getString(R.string.warn_toast_fill_username_password));
                    return;
                }
                if (password.length() < 4 || password.length() > 12) {
                    Toaster.showMessage(UserActionActivity.this,
                            Utils.getString(R.string.warn_toast_wrong_password_format));
                    return;
                }

                List<User> list = User.find(User.class, "username = ?", new String[]{username});
                if (list != null && list.size() > 0) {
                    Toaster.showMessage(UserActionActivity.this, getResources().getString(R.string.warn_toast_user_exist));
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
                SocketManager.getInstance().sendMessage(newUser);
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
                SocketManager.getInstance().sendMessage(editUser);
            }
        } else if (v == mEditPassword) {
            showChangePasswordDialog();
        } else if (v == mMenu) {
            onBackPressed();
        } else if (v == mHome) {
            onBackPressed();
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
        Toaster.showMessage(UserActionActivity.this, Utils.getString(R.string.warn_dialog_error_happen));
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
                    Toaster.showMessage(UserActionActivity.this, Utils.getString(R.string.warn_dialog_error_happen));
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
                    Toaster.showMessage(UserActionActivity.this,
                            Utils.getString(R.string.warn_toast_update_password_unsuccess));
                } else {
                    Toaster.showMessage(UserActionActivity.this,
                            Utils.getString(R.string.warn_toast_update_password_success));
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
                    Toaster.showMessage(UserActionActivity.this,
                            Utils.getString(R.string.warn_toast_input_new_password));
                    return;
                } else if (np.length() < 4 || np.length() > 12) {
                    Toaster.showMessage(UserActionActivity.this,
                            Utils.getString(R.string.warn_toast_wrong_password_length));
                    return;
                }
                dialog.cancel();
                CommandMessage changePassword = new CommandMessage();
                changePassword.setUpdatePassword(mUser.username, newPassword.getText().toString());
                ProgressHUD.showLoading(UserActionActivity.this);
                TimeOutManager.getInstance().startCountDown(UserActionActivity.this, 5);
                SocketManager.getInstance().sendMessage(changePassword);
            }
        });

        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                Utils.hideKeyboard(TheActivityManager.getInstance().getCurrentActivity());
            }
        });

        dialog.show();
    }
}
