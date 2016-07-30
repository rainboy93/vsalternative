package vn.com.vshome.view.customview;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayout;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.alexvasilkov.gestures.GestureController;
import com.alexvasilkov.gestures.views.GestureImageView;

import vn.com.vshome.R;

/**
 * Created by anlab on 7/27/16.
 */
public class CameraControlView extends GridLayout {

    private GestureImageView mLeftUp, mUp, mRightUp, mLeft, mMinimize, mRight, mLeftDown, mDown, mRightDown;
    private boolean isFullScreen = false;

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

    public void setFullScreen() {
        mMinimize.setVisibility(View.VISIBLE);
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
        gestureImageView.getController().getSettings()
                .setPanEnabled(false)
                .setZoomEnabled(false)
                .setFillViewport(false);
        gestureImageView.getController().setLongPressEnabled(true);

        gestureImageView.getController().setOnGesturesListener(new GestureController.OnGestureListener() {
            @Override
            public void onDown(@NonNull MotionEvent e) {

            }

            @Override
            public void onUpOrCancel(@NonNull MotionEvent e) {
                if (gestureImageView != mMinimize) {

                }
            }

            @Override
            public boolean onSingleTapUp(@NonNull MotionEvent e) {
                return false;
            }

            @Override
            public boolean onSingleTapConfirmed(@NonNull MotionEvent e) {
                if (gestureImageView == mMinimize && !isFullScreen) {

                }
                return true;
            }

            @Override
            public void onLongPress(@NonNull MotionEvent e) {
                handleLongPress(gestureImageView);
            }

            @Override
            public boolean onDoubleTap(@NonNull MotionEvent e) {
                return false;
            }
        });
    }

    private void handleLongPress(GestureImageView gestureImageView) {
        if (gestureImageView == mLeftUp) {

        } else if (gestureImageView == mUp) {

        } else if (gestureImageView == mRightUp) {

        } else if (gestureImageView == mLeft) {

        } else if (gestureImageView == mRight) {

        } else if (gestureImageView == mLeftDown) {

        } else if (gestureImageView == mDown) {

        } else if (gestureImageView == mRightDown) {

        }
    }
}
