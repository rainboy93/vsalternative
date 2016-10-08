package vn.com.vshome.security;

import vn.com.vshome.R;
import vn.com.vshome.activitymanager.TheActivityManager;
import vn.com.vshome.database.Camera;
import vn.com.vshome.foscamsdk.CameraManager;
import vn.com.vshome.utils.Define;
import vn.com.vshome.utils.Utils;
import vn.com.vshome.view.customview.CameraView;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.support.v4.view.GestureDetectorCompat;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;

public class PreviewService extends Service implements
        GestureDetector.OnDoubleTapListener, OnGestureListener {

    private final float SCALE = 1f / 2;
    private static final int PADDING_WIDTH = 5;
    private static final int CLOSE_BUTTON_WIDTH = 80;
    private static final int CLOSE_BUTTON_HEIGHT = 80;

    private WindowManager windowManager;

    private RelativeLayout mParentLayout;
    int width;
    int height;

    private GestureDetectorCompat mDetector;

    private CameraView mVideoView;
    private Camera camera;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent.getExtras() != null) {
            Long id = intent.getExtras().getLong(Define.INTENT_CAMERA, 0);
            camera = Camera.findById(Camera.class, id);
        }
        CameraManager.getInstance().addSession(camera, null);
        if (mVideoView != null) {
            mVideoView.setCamera(camera);
            mVideoView.startDraw();
        }
        return Service.START_NOT_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    final WindowManager.LayoutParams params = new WindowManager.LayoutParams(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.TYPE_PHONE,
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
            PixelFormat.TRANSLUCENT);

    @Override
    public void onCreate() {
        super.onCreate();

        Log.d("dungnt", "start preview");

        previewReceiver = new PreviewReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(Define.INTENT_FULL_SCREEN);
        filter.addAction(Define.INTENT_PREVIEW);
        filter.addAction(Define.INTENT_STOP_PREVIEW);
        getApplication().registerReceiver(previewReceiver, filter);

        CameraManager.getInstance().isPreviewing = true;

        mDetector = new GestureDetectorCompat(getApplicationContext(), this);
        windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        width = Utils.getScreenWidth();
        height = Utils.getScreenHeight();

        mParentLayout = new RelativeLayout(getApplicationContext());

        RelativeLayout mVideoContainer = new RelativeLayout(
                getApplicationContext());
        mVideoContainer.setBackgroundColor(Color.parseColor("#27b8fa"));
        mVideoContainer.setPadding(PADDING_WIDTH,
                PADDING_WIDTH, PADDING_WIDTH,
                PADDING_WIDTH);

        mVideoView = new CameraView(getApplicationContext());
        mVideoContainer.addView(mVideoView, new LayoutParams(
                (int) (width * SCALE), (int) (3 * SCALE * width / 4)));
        mVideoView.setBackgroundColor(Color.BLACK);

        final ImageView imageView = new ImageView(getApplicationContext());
        imageView.setAdjustViewBounds(true);
        imageView.setImageResource(R.drawable.button_close);

        View view = new View(getApplicationContext());
        view.setBackgroundColor(Color.TRANSPARENT);

        mParentLayout.addView(view, (int) (width * SCALE), (int) (3 * SCALE
                * width / 4));

        RelativeLayout.LayoutParams p1 = new RelativeLayout.LayoutParams(
                (int) (width * SCALE), (int) (3 * SCALE * width / 4));
        p1.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        p1.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        mParentLayout.addView(mVideoContainer, p1);

        RelativeLayout.LayoutParams p2 = new RelativeLayout.LayoutParams(
                CLOSE_BUTTON_WIDTH,
                CLOSE_BUTTON_HEIGHT);
        p2.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        mParentLayout.addView(imageView, p2);

        params.gravity = Gravity.TOP | Gravity.LEFT;
        params.x = width / 2 - 40;
        params.y = (int) (height - 3 * SCALE * width / 4);
        params.width = (int) (SCALE * width + CLOSE_BUTTON_WIDTH / 2);
        params.height = (int) (3 * SCALE * width / 4 + CLOSE_BUTTON_HEIGHT / 2);

        windowManager.addView(mParentLayout, params);

        imageView.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                CameraManager.getInstance().isPreviewing = false;
                stopSelf();
            }
        });
        try {
            mParentLayout.setOnTouchListener(new OnTouchListener() {
                private WindowManager.LayoutParams paramsF = params;
                private int initialX;
                private int initialY;
                private float initialTouchX;
                private float initialTouchY;

                @SuppressLint("ClickableViewAccessibility")
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    mDetector.onTouchEvent(event);
                    switch (event.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                            initialX = paramsF.x;
                            initialY = paramsF.y;
                            initialTouchX = event.getRawX();
                            initialTouchY = event.getRawY();
                            return true;
                        case MotionEvent.ACTION_UP:
                        case MotionEvent.ACTION_CANCEL:
                            return true;
                        case MotionEvent.ACTION_MOVE:
                            int x = initialX
                                    + (int) (event.getRawX() - initialTouchX);
                            int y = initialY
                                    + (int) (event.getRawY() - initialTouchY);

                            if (x < 0) {
                                x = 0;
                            }
                            if (x > width / 2 - 40) {
                                x = width / 2 - 40;
                            }
                            if (y < 0) {
                                y = 0;
                            }
                            if (y > (int) (height - 3 * SCALE * width / 4)) {
                                y = (int) (height - 3 * SCALE * width / 4);
                            }

                            paramsF.x = x;
                            paramsF.y = y;
                            try {
                                windowManager.updateViewLayout(mParentLayout,
                                        paramsF);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            return true;
                    }
                    return true;
                }

            });
        } catch (Exception e) {

        }
    }

    @Override
    public void onDestroy() {
        hidePreview();
        if (mParentLayout != null) {
            try {
                windowManager.removeView(mParentLayout);
                mParentLayout = null;
            } catch (Exception e) {
            }
        }
        Log.d("dungnt", "Stop perview");
        try {
            getApplication().unregisterReceiver(previewReceiver);
        } catch (Exception e) {

        }
        CameraManager.getInstance().removeSession(camera);
        super.onDestroy();
    }

    @Override
    public boolean onDoubleTapEvent(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onSingleTapConfirmed(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onDoubleTap(MotionEvent e) {
        hidePreview();
        Intent intent = new Intent(TheActivityManager.getInstance().getCurrentActivity(), FullPreviewActivity.class);
        intent.putExtra(Define.INTENT_CAMERA, camera.getId());
        TheActivityManager.getInstance().getCurrentActivity().startActivity(intent);
        return true;
    }

    @Override
    public boolean onDown(MotionEvent arg0) {
        return false;
    }

    @Override
    public boolean onFling(MotionEvent arg0, MotionEvent arg1, float arg2,
                           float arg3) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent arg0) {

    }

    @Override
    public boolean onScroll(MotionEvent arg0, MotionEvent arg1, float arg2,
                            float arg3) {
        return false;
    }

    @Override
    public void onShowPress(MotionEvent arg0) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent arg0) {
        return false;
    }

    private void hidePreview() {
        if (mParentLayout == null) {
            return;
        }
        mParentLayout.setVisibility(View.GONE);
        WindowManager.LayoutParams paramF = params;
        paramF.width = 1;
        paramF.height = 1;
        windowManager.updateViewLayout(mParentLayout, paramF);
    }

    private PreviewReceiver previewReceiver;

    public class PreviewReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {

            if (intent.getAction().equals(Define.INTENT_FULL_SCREEN)) {
                hidePreview();
            } else if (intent.getAction().equals(Define.INTENT_PREVIEW)) {
                Log.d("Preview", "StartPreviewing");
                if (mParentLayout == null) {
                    return;
                }
                mParentLayout.setVisibility(View.VISIBLE);
                WindowManager.LayoutParams paramF = params;
                paramF.width = (int) (SCALE * width + CLOSE_BUTTON_WIDTH / 2);
                paramF.height = (int) (3 * SCALE * width / 4 + CLOSE_BUTTON_HEIGHT / 2);
                windowManager.updateViewLayout(mParentLayout, paramF);
            } else if (intent.getAction().equals(Define.INTENT_STOP_PREVIEW)) {
                stopSelf();
            }
        }
    }
}
