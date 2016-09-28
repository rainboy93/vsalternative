package vn.com.vshome.view.customview;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayout;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.fos.sdk.FosSdkJNI;
import com.fos.sdk.PtzCmd;

import vn.com.vshome.R;
import vn.com.vshome.VSHome;
import vn.com.vshome.database.Camera;
import vn.com.vshome.foscamsdk.CameraManager;
import vn.com.vshome.foscamsdk.CameraSession;
import vn.com.vshome.security.CameraControlThread;
import vn.com.vshome.security.PreviewService;
import vn.com.vshome.security.FullPreviewActivity;
import vn.com.vshome.utils.Define;
import vn.com.vshome.utils.Logger;
import vn.com.vshome.utils.Utils;
import vn.com.vshome.view.GestureImageView;

/**
 * Created by anlab on 7/27/16.
 */
public class CameraControlView extends GridLayout {

    private GestureImageView mLeftUp, mUp, mRightUp, mLeft, mMinimize, mRight, mLeftDown, mDown, mRightDown;
    private boolean isFullScreen = false;
    private int handler = -1;
    private Camera camera;
    private boolean isActive = false;

    public void setActive(boolean isActive){
        this.isActive = isActive;
    }

    public CameraControlView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initView();
    }

    public CameraControlView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public CameraControlView(Context context) {
        super(context);
        initView();
    }

    public void setCamera(Camera camera) {
        this.camera = camera;
    }

    public void setFullScreen() {
        mMinimize.setVisibility(View.INVISIBLE);
        isFullScreen = true;
    }

    private void initView() {
        inflate(getContext(), R.layout.view_camera_control, this);

        mLeftUp = (GestureImageView) findViewById(R.id.camera_control_left_up);
        mUp = (GestureImageView) findViewById(R.id.camera_control_up);
        mRightUp = (GestureImageView) findViewById(R.id.camera_control_right_up);
        mLeft = (GestureImageView) findViewById(R.id.camera_control_left);
        mMinimize = (GestureImageView) findViewById(R.id.camera_control_minimize);
        mRight = (GestureImageView) findViewById(R.id.camera_control_right);
        mLeftDown = (GestureImageView) findViewById(R.id.camera_control_left_down);
        mDown = (GestureImageView) findViewById(R.id.camera_control_down);
        mRightDown = (GestureImageView) findViewById(R.id.camera_control_right_down);

        setting(mLeftUp);
        setting(mUp);
        setting(mRightUp);
        setting(mLeft);
        setting(mMinimize);
        setting(mRight);
        setting(mLeftDown);
        setting(mDown);
        setting(mRightDown);
    }

    private void setting(final GestureImageView gestureImageView) {

        gestureImageView.setOnControlListener(new GestureImageView.OnControlListener() {
            @Override
            public void onDoubleTouch() {
                if(!isActive){
                    return;
                }
                if (isFullScreen && VSHome.activity instanceof FullPreviewActivity) {
                    VSHome.activity.onBackPressed();
                } else {
                    Intent intent = new Intent(VSHome.activity, FullPreviewActivity.class);
                    intent.putExtra(Define.INTENT_CAMERA, camera.getId());
                    VSHome.activity.startActivity(intent);
                }
                CameraControlThread.getInstance().sendControl(handler, PtzCmd.FOSPTZ_STOP);
            }

            @Override
            public void onControl() {
                if(!isActive){
                    return;
                }
                if (gestureImageView != mMinimize) {
                    handleLongPress(gestureImageView);
                }
            }

            @Override
            public void onMinimize() {
                if(!isActive){
                    return;
                }
                if (gestureImageView == mMinimize && !isFullScreen) {
                    if (!Utils.isMyServiceRunning(PreviewService.class)) {
                        Intent intent = new Intent(VSHome.activity, PreviewService.class);
                        intent.putExtra(Define.INTENT_CAMERA, camera.getId());
                        VSHome.activity.startService(intent);
                    }
                }
            }

            @Override
            public void onStop() {
                if(!isActive){
                    return;
                }
                if (gestureImageView != mMinimize) {
                    CameraControlThread.getInstance().sendControl(handler, PtzCmd.FOSPTZ_STOP);
                }
            }
        });
    }

    private void handleLongPress(GestureImageView gestureImageView) {
        CameraSession session = CameraManager.getInstance().getCameraSession(camera);
        if (handler != session.cameraThread.handler) {
            handler = session.cameraThread.handler;
        }

        if (handler < 0) {
            return;
        }

        if (gestureImageView == mLeftUp) {
            CameraControlThread.getInstance().sendControl(handler, PtzCmd.FOSPTZ_LEFT_UP);
        } else if (gestureImageView == mUp) {
            CameraControlThread.getInstance().sendControl(handler, PtzCmd.FOSPTZ_UP);
        } else if (gestureImageView == mRightUp) {
            CameraControlThread.getInstance().sendControl(handler, PtzCmd.FOSPTZ_RIGHT_UP);
        } else if (gestureImageView == mLeft) {
            CameraControlThread.getInstance().sendControl(handler, PtzCmd.FOSPTZ_LEFT);
        } else if (gestureImageView == mRight) {
            CameraControlThread.getInstance().sendControl(handler, PtzCmd.FOSPTZ_RIGHT);
        } else if (gestureImageView == mLeftDown) {
            CameraControlThread.getInstance().sendControl(handler, PtzCmd.FOSPTZ_LEFT_DOWN);
        } else if (gestureImageView == mDown) {
            CameraControlThread.getInstance().sendControl(handler, PtzCmd.FOSPTZ_DOWN);
        } else if (gestureImageView == mRightDown) {
            CameraControlThread.getInstance().sendControl(handler, PtzCmd.FOSPTZ_RIGHT_DOWN);
        }
    }
}
