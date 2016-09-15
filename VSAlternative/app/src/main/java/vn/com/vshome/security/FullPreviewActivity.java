package vn.com.vshome.security;

import android.content.Intent;
import android.os.Bundle;

import vn.com.vshome.BaseActivity;
import vn.com.vshome.R;
import vn.com.vshome.VSHome;
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

        if(getIntent().getExtras() != null){
            camera = (Camera) getIntent().getExtras().getSerializable(Define.INTENT_CAMERA);
        }

        initView();
    }

    private void initView(){
        cameraView = (CameraView) findViewById(R.id.full_screen_camera_view);
        cameraControlView = (CameraControlView) findViewById(R.id.full_screen_camera_control_view);

        CameraManager.getInstance().addSession(camera, null);
        cameraView.setCamera(camera);
        cameraView.startDraw();
        cameraControlView.setCamera(camera);
        cameraControlView.setFullScreen();
    }

    @Override
    protected void onDestroy() {
        CameraManager.getInstance().removeSession(camera);
        if(CameraManager.getInstance().isPreviewing){
            if(!Utils.isMyServiceRunning(PreviewService.class)){
                Intent intent = new Intent(VSHome.activity, PreviewService.class);
                intent.putExtra(Define.INTENT_CAMERA, camera);
                VSHome.activity.startService(intent);
            }
        }
        super.onDestroy();
    }
}
