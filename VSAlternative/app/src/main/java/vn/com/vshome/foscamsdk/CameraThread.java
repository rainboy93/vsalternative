package vn.com.vshome.foscamsdk;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.SystemClock;

import com.fos.sdk.ConnectType;
import com.fos.sdk.FosDecFmt;
import com.fos.sdk.FosResult;
import com.fos.sdk.FosSdkJNI;
import com.fos.sdk.FrameData;
import com.fos.sdk.IPCType;
import com.fos.sdk.StreamType;

import java.nio.ByteBuffer;

import vn.com.vshome.callback.CameraCallback;
import vn.com.vshome.database.Camera;
import vn.com.vshome.utils.Define;

/**
 * Created by anlab on 8/3/16.
 */
public class CameraThread extends Thread {

    public boolean isRunning = true;
    private FrameData videoData = null;
    private ByteBuffer buffer = null;
    public Bitmap mBit = null;
    private CameraCallback cameraCallback;

    private Camera camera;
    private int handler = -1;
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
                            handler, StreamType.FOSSTREAM_MAIN,
                            2000);
//                    if (isRetryLogin) {
//                        Intent intent = new Intent("Error");
//                        intent.putExtra("Content", "");
//                        VSHome.mActivity.sendBroadcast(intent);
//                    }
                    if (state == 0 && isRunning) {
                        startGetData();
                    }
                    break;
                case FosResult.FOSCMDRET_EXCEEDMAXUSR:
                    break;
                default:
                    break;
            }
        }

        if (state > 0 && state != FosResult.FOSCMDRET_INTERFACE_CANCEL_BYUSR) {
            String content = "";
            if (state == 3) {
                content = "Truy cập vượt quá giới hạn";
            } else {
                content = "Camera gặp sự cố";
            }
            if (cameraCallback != null) {
                cameraCallback.onError(content);
            }
        }
    }

    private void logOut() {
        FosSdkJNI.Logout(handler, 2000);
        FosSdkJNI.Release(handler);
    }

    private void startGetData() {
        try {
            FosSdkJNI.GetVideoData(handler,
                    videoData, FosDecFmt.FOSDECTYPE_RGBA32);
            if (videoData != null && videoData.len > 0 && videoData.data != null) {
                buffer = ByteBuffer.wrap(videoData.data);
                if (mBit == null || videoData.picWidth != mBit.getWidth() || videoData.picHeight != mBit.getHeight()) {
                    if (videoData.picWidth > 0 && videoData.picHeight > 0) {
                        mBit = Bitmap.createBitmap(videoData.picWidth,
                                videoData.picHeight, Bitmap.Config.ARGB_8888);
                    }
                }
                if (mBit != null) {
                    mBit.copyPixelsFromBuffer(buffer);
                    buffer.rewind();
                }
            }

            SystemClock.sleep(20);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void stopGetData() {
        logOut();
        isRunning = false;
        interrupt();
    }
}
