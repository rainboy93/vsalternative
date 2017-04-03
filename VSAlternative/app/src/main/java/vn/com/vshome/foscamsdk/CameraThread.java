package vn.com.vshome.foscamsdk;

import android.os.SystemClock;

import com.fos.sdk.ConnectType;
import com.fos.sdk.FosDecFmt;
import com.fos.sdk.FosResult;
import com.fos.sdk.FosSdkJNI;
import com.fos.sdk.IPCType;
import com.fos.sdk.StreamType;

import vn.com.vshome.R;
import vn.com.vshome.callback.CameraCallback;
import vn.com.vshome.database.Camera;
import vn.com.vshome.utils.Define;
import vn.com.vshome.utils.MiscUtils;
import vn.com.vshome.utils.Utils;

/**
 * Created by anlab on 8/3/16.
 */
public class CameraThread extends Thread {

    public boolean isRunning = true;
    private CameraData videoData = null;
    public CameraData publicData = null;
    private CameraCallback cameraCallback;

    private Camera camera;
    public int handler = -1;
    private int state = -1;
    private int permissionFlag = -1;

    public CameraThread(Camera camera) {
        this.camera = camera;
    }

    public void setCameraCallback(CameraCallback cameraCallback) {
        this.cameraCallback = cameraCallback;
    }

    @Override
    public void run() {
        logIn();
        if (state == 0) {
            videoData = new CameraData();
            while (isRunning) {
                startGetData();
            }
        }
    }

    private void logIn() {
        String address = "";
        String username = camera.username;
        String password = camera.password;
        int port = 0;
        if (Define.NETWORK_TYPE == Define.NetworkType.LocalNetwork) {
            address = camera.ipAddress;
            port = camera.localPort;
        } else {
            address = camera.dnsAddress;
            port = camera.webPort;
        }
        handler = FosSdkJNI.Create(address, "", username, password, port, port, IPCType.FOSIPC_H264,
                ConnectType.FOSCNTYPE_IP);
        if (handler > 0) {
            state = FosSdkJNI.Login(handler,
                    permissionFlag, 3000);
            switch (state) {
                case 0:
                    state = FosSdkJNI.OpenVideo(
                            handler, StreamType.FOSSTREAM_SUB,
                            1000);
                    break;
                default:
                    break;
            }
        }

        if (state > 0 && state != FosResult.FOSCMDRET_INTERFACE_CANCEL_BYUSR) {
            String content = "";
            if (state == 3) {
                content = Utils.getString(R.string.warn_camera_max_user_exceed);
            } else {
                content = Utils.getString(R.string.warn_camera_error);
            }
            if (cameraCallback != null) {
                cameraCallback.onError(content);
            }
        }
    }

    private void logOut() {
        MiscUtils.runOnBackgroundThread(new Runnable() {
            @Override
            public void run() {
                FosSdkJNI.Logout(handler, 1000);
            }
        });
    }

    private void startGetData() {
        try {
            FosSdkJNI.GetVideoData(handler,
                    videoData, FosDecFmt.FOSDECTYPE_RGBA32);
            if (videoData != null) {
                publicData = videoData.clone();
            }
            SystemClock.sleep(5);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void stopGetData() {
        logOut();
        isRunning = false;
        videoData = null;
        publicData = null;
        interrupt();
    }
}
