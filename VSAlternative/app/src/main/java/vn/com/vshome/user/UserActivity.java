package vn.com.vshome.user;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;

import in.workarounds.typography.TextView;
import vn.com.vshome.BaseActivity;
import vn.com.vshome.R;
import vn.com.vshome.VSHome;
import vn.com.vshome.callback.UserControlCallback;
import vn.com.vshome.database.User;
import vn.com.vshome.networks.CommandMessage;
import vn.com.vshome.utils.Define;
import vn.com.vshome.utils.TimeOutManager;
import vn.com.vshome.utils.Toaster;
import vn.com.vshome.view.NonSwipeViewPager;
import vn.com.vshome.view.ProgressHUD;

public class UserActivity extends BaseActivity implements OnClickListener, UserControlCallback,
        TimeOutManager.TimeOutCallback {

    private NonSwipeViewPager mViewPager;
    private ImageButton mMenu;
    private ImageButton mHome;
    private TextView mTitle;
    private UserViewPagerAdapter mAdapter;
    public User mNewUser, mEditUser;

    @Override
    protected void onStart() {
        super.onStart();
        VSHome.socketManager.receiveThread.setUserCallback(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        initView();
        User.deleteAll(User.class);
        if (VSHome.currentUser.priority == Define.PRIORITY_ADMIN) {
            getUserList();
        } else {
            VSHome.currentUser.save();
            mViewPager.setAdapter(mAdapter);
        }
    }

    private void initView() {
        initActionBar();
        mViewPager = (NonSwipeViewPager) findViewById(R.id.user_view_pager);
        mAdapter = new UserViewPagerAdapter(getSupportFragmentManager());
    }

    private void initActionBar() {
        mMenu = (ImageButton) findViewById(R.id.action_bar_menu);
        mMenu.setImageResource(R.drawable.icon_back);
        mHome = (ImageButton) findViewById(R.id.action_bar_home);
        mTitle = (TextView) findViewById(R.id.action_bar_title);
        mTitle.setText("Xin chào ");

        mMenu.setOnClickListener(this);
        mHome.setOnClickListener(this);
    }

    private void getUserList() {
        CommandMessage getUserList = new CommandMessage();
        getUserList.cmd = CommandMessage.CMD_GET_USER_LIST;

        ProgressHUD.showLoading(this);
        TimeOutManager.getInstance().startCountDown(this, 10);

        VSHome.socketManager.sendMessage(getUserList);
    }

    public void setFragment(int index) {
        mViewPager.setCurrentItem(index);
    }

    @Override
    public void onBackPressed() {
        if (mViewPager.getCurrentItem() != 0) {
            mViewPager.setCurrentItem(0);
        } else
            super.onBackPressed();
    }

    @Override
    protected void onStop() {
        try {
            VSHome.socketManager.receiveThread.setUserCallback(null);
        } catch (Exception e) {

        }
        super.onStop();
    }

    @Override
    public void onClick(View v) {
        if (v == mMenu) {
            onBackPressed();
        } else if (v == mHome) {
            onBackPressed();
        }
    }

    @Override
    public void onControl(int id) {

    }

    @Override
    public void onResponse(int cmd, int status) {

    }

    @Override
    public void onGetUserListDone() {
        ProgressHUD.hideLoading(this);
        TimeOutManager.getInstance().cancelCountDown();
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mViewPager.setAdapter(mAdapter);
            }
        });
    }

    @Override
    public void onTimeOut() {
        ProgressHUD.hideLoading(this);
        Toaster.showMessage(this, "Có lỗi xảy ra. Hãy thử lại.");
        finish();
    }
}
