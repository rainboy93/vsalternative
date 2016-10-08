package vn.com.vshome;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import vn.com.vshome.activitymanager.TheActivityManager;
import vn.com.vshome.callback.DialogCallback;
import vn.com.vshome.communication.SocketManager;
import vn.com.vshome.roomselection.RoomSelectionActivity;
import vn.com.vshome.security.SecurityActivity;
import vn.com.vshome.user.UserActivity;
import vn.com.vshome.utils.Define;
import vn.com.vshome.utils.MiscUtils;
import vn.com.vshome.utils.Toaster;
import vn.com.vshome.utils.Utils;

public class MainActivity extends BaseActivity implements View.OnClickListener, DialogCallback {

    private ImageButton mButtonLight, mButtonSecurity, mButtonSensor, mButtonSun, mButtonUser, mButtonAdvance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
    }

    private void initView() {
        mButtonLight = (ImageButton) findViewById(R.id.main_button_light_control);
        mButtonSecurity = (ImageButton) findViewById(R.id.main_button_security);
        mButtonSensor = (ImageButton) findViewById(R.id.main_button_environment);
        mButtonSun = (ImageButton) findViewById(R.id.main_button_sun_energy);
        mButtonUser = (ImageButton) findViewById(R.id.main_button_user);
        mButtonAdvance = (ImageButton) findViewById(R.id.main_button_advance);

        mButtonLight.setOnClickListener(this);
        mButtonSecurity.setOnClickListener(this);
        mButtonSensor.setOnClickListener(this);
        mButtonSun.setOnClickListener(this);
        mButtonUser.setOnClickListener(this);
        mButtonAdvance.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent = null;
        if (v == mButtonLight) {
            intent = new Intent(this, RoomSelectionActivity.class);
        } else if (v == mButtonSecurity) {
            intent = new Intent(this, SecurityActivity.class);
        } else if (v == mButtonSensor) {
            Toaster.showMessage(this, Define.DEVICE_NOT_AVAILABLE);
        } else if (v == mButtonSun) {
            Toaster.showMessage(this, Define.DEVICE_NOT_AVAILABLE);
        } else if (v == mButtonUser) {
            intent = new Intent(this, UserActivity.class);
        } else if (v == mButtonAdvance) {
            Toaster.showMessage(this, Define.IN_DEVELOPING);
        }
        if (intent != null) {
            startActivity(intent);
        }
    }

    @Override
    public void onBackPressed() {
        Utils.showConfirmDialog("Thông báo",
                "Bạn có chắc chắn muốn thoát khỏi ứng dụng?", this);
    }

    @Override
    public void onConfirm() {
        MiscUtils.runOnBackgroundThread(new Runnable() {
            @Override
            public void run() {
                SocketManager.getInstance().destroySocket();
                TheActivityManager.getInstance().finishAll();
            }
        });
    }
}
