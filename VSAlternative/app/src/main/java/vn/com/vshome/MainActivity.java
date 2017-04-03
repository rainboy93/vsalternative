package vn.com.vshome;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
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
    private static final int DRAW_OVERLAY_REQUEST_CODE = 1234;

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
//            intent = new Intent(this, SecurityActivity.class);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
                    && !Settings.canDrawOverlays(this)) {
                Utils.showConfirmDialog(Utils.getString(R.string.title_dialog_notification),
                        Utils.getString(R.string.warn_draw_over_other_app), new DialogCallback() {
                    @Override
                    public void onConfirm() {
                        checkPermission();
                    }
                });
            } else {
                intent = new Intent(this, SecurityActivity.class);
            }
        } else if (v == mButtonSensor) {
            Toaster.showMessage(this, Utils.getString(R.string.warn_toast_device_not_available));
        } else if (v == mButtonSun) {
            Toaster.showMessage(this, Utils.getString(R.string.warn_toast_device_not_available));
        } else if (v == mButtonUser) {
            intent = new Intent(this, UserActivity.class);
        } else if (v == mButtonAdvance) {
            Toaster.showMessage(this, Utils.getString(R.string.warn_toast_in_development));
        }
        if (intent != null) {
            startActivity(intent);
        }
    }

    @Override
    public void onBackPressed() {
        Utils.showConfirmDialog(Utils.getString(R.string.title_dialog_notification),
                Utils.getString(R.string.warn_quit_app), this);
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

    private void checkPermission() {
        Intent i = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                Uri.parse("package:" + getPackageName()));
        VSHome.isTakePhoto = true;
        startActivityForResult(i, DRAW_OVERLAY_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == DRAW_OVERLAY_REQUEST_CODE) {
            VSHome.isTakePhoto = false;
            ((VSHome)getApplication()).cancelDelayKill();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
                    && !Settings.canDrawOverlays(this)) {
                super.onActivityResult(requestCode, resultCode, data);
            } else {
                Intent intent = new Intent(this, SecurityActivity.class);
                startActivity(intent);
            }
        }
    }
}
