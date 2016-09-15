package vn.com.vshome.view;

import vn.com.vshome.VSHome;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v4.view.GestureDetectorCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

import com.fos.sdk.FosSdkJNI;
import com.fos.sdk.PtzCmd;

public class GestureImageView extends ImageView implements
        GestureDetector.OnGestureListener, GestureDetector.OnDoubleTapListener {
    private GestureDetectorCompat mDetector;
    private OnControlListener mListener;
    private int id;
    private boolean isFullScreen = false;

    public void setFullScreen() {
        isFullScreen = true;
    }

    public void setId(int id) {
        this.id = id;
    }

    public GestureImageView(Context context, AttributeSet attrs,
                            int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public GestureImageView(Context context) {
        super(context);
        init();
    }

    public GestureImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public void setOnControlListener(OnControlListener listener) {
        mListener = listener;
    }

    @SuppressLint("ClickableViewAccessibility")
    private void init() {
        mDetector = new GestureDetectorCompat(getContext(), this);
        setOnTouchListener(new OnTouchListener() {

            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                boolean detectedUp = event.getAction() == MotionEvent.ACTION_UP
                        || event.getAction() == MotionEvent.ACTION_CANCEL;
                if (!mDetector.onTouchEvent(event) && detectedUp) {
                    if(mListener != null){
                        mListener.onStop();
                    }
                }
                return true;
            }
        });
    }

    @Override
    public boolean onDoubleTap(MotionEvent e) {
        if(mListener != null){
            mListener.onDoubleTouch();
        }
        return true;
    }

    @Override
    public boolean onDoubleTapEvent(MotionEvent e) {
        return true;
    }

    @Override
    public boolean onSingleTapConfirmed(MotionEvent e) {
        if(mListener != null){
            mListener.onControl();
        }
        return true;
    }

    @Override
    public boolean onDown(MotionEvent e) {
        return true;
    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
                           float velocityY) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
                            float distanceY) {
        return false;
    }

    @Override
    public void onShowPress(MotionEvent e) {
        if(mListener != null){
            mListener.onControl();
        }
    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        return false;
    }

    public interface OnControlListener {
        void onDoubleTouch();

        void onControl();

        void onStop();
    }
}
