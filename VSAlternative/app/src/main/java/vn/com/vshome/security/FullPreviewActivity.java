package vn.com.vshome.security;

import android.content.Intent;
import android.os.Bundle;

import vn.com.vshome.BaseActivity;
import vn.com.vshome.R;
import vn.com.vshome.database.Camera;
import vn.com.vshome.foscamsdk.CameraManager;
import vn.com.vshome.utils.Define;
import vn.com.vshome.utils.Utils;
import vn.com.vshome.view.customview.CameraControlView;
import vn.com.vshome.view.customview.CameraView;

public class FullPreviewActivity extends BaseActivity {

    private CameraView cameraView;
    private CameraControlView cameraControlView;

    private Camera camera;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_preview);

        if (getIntent().getExtras() != null) {
            Long id = getIntent().getExtras().getLong(Define.INTENT_CAMERA, 0);
            camera = Camera.findById(Camera.class, id);
        }

        if (Utils.isMyServiceRunning(PreviewService.class)) {
            sendBroadcast(new Intent("StartFullScreen"));
        }

        initView();
    }

    private void initView() {
        cameraView = (CameraView) findViewById(R.id.full_screen_camera_view);

        cameraControlView = (CameraControlView) findViewById(R.id.full_screen_camera_control_view);
        cameraControlView.setActive(true);

        CameraManager.getInstance().addSession(camera, null);
        cameraView.setCamera(camera);
        cameraView.setFullScreen();
        cameraView.startDraw();
        cameraControlView.setCamera(camera);
        cameraControlView.setFullScreen();
    }

    @Override
    public void onBackPressed() {
        CameraManager.getInstance().removeSession(camera);
        super.onBackPressed();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
