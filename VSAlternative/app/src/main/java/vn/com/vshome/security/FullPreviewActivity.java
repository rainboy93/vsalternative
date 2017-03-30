package vn.com.vshome.security;

import android.content.Intent;
import android.os.Bundle;
import android.view.SurfaceView;
import android.view.View;

import org.videolan.libvlc.EventHandler;
import org.videolan.libvlc.VideoView;

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
    private SurfaceView cameraViewOnvif;
    private Camera camera;
    private VideoView videoView;

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
        cameraViewOnvif = (SurfaceView) findViewById(R.id.full_screen_camera_view_onvif);
        cameraControlView = (CameraControlView) findViewById(R.id.full_screen_camera_control_view);
        cameraControlView.setActive(true);

        if(camera.deviceType == 1 || camera.deviceType == 2){
            cameraView.setVisibility(View.VISIBLE);
            cameraViewOnvif.setVisibility(View.GONE);

            CameraManager.getInstance().addSession(camera, null);
            cameraView.setCamera(camera);
            cameraView.setFullScreen();
            cameraView.startDraw();
        } else if(camera.deviceType == 3){
            cameraView.setVisibility(View.GONE);
            cameraViewOnvif.setVisibility(View.VISIBLE);

            String streamLink = "";
            if(Define.NETWORK_TYPE == Define.NetworkType.LocalNetwork){
                streamLink = camera.localStreamLink;
            } else if(Define.NETWORK_TYPE == Define.NetworkType.DnsNetwork){
                streamLink = camera.dnsStreamLink;
            }

            videoView = new VideoView(cameraViewOnvif, streamLink, this,
                    EventHandler.getInstance(), null);
            videoView.createPlayer();
        }

        cameraControlView.setCamera(camera);
        cameraControlView.setFullScreen();
    }

    @Override
    public void onBackPressed() {
        if(camera.deviceType == 1 || camera.deviceType == 2){
            CameraManager.getInstance().removeSession(camera);
        } else if(camera.deviceType == 3){

        }
        super.onBackPressed();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
