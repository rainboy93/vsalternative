package vn.com.vshome.security;

import vn.com.vshome.R;
import vn.com.vshome.activitymanager.TheActivityManager;
import vn.com.vshome.database.Camera;
import vn.com.vshome.flexibleadapter.security.CameraChildItem;
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
import android.graphics.SurfaceTexture;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.v4.view.GestureDetectorCompat;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import org.videolan.libvlc.EventHandler;
import org.videolan.libvlc.IVideoPlayer;
import org.videolan.libvlc.LibVLC;
import org.videolan.libvlc.LibVlcUtil;
import org.videolan.libvlc.Media;
import org.videolan.libvlc.MediaList;
import org.videolan.libvlc.NativeCrashHandler;
import org.videolan.libvlc.VideoView;

import java.lang.ref.WeakReference;

public class PreviewService extends Service implements
        GestureDetector.OnDoubleTapListener, OnGestureListener,SurfaceHolder.Callback, IVideoPlayer {

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
    private SurfaceView cameraViewOnvif;
    private Camera camera;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent.getExtras() != null && camera == null) {
            Long id = intent.getExtras().getLong(Define.INTENT_CAMERA, 0);
            camera = Camera.findById(Camera.class, id);
        }
        if(camera.deviceType == 1 || camera.deviceType == 2){
            CameraManager.getInstance().addSession(camera, null);
            if (mVideoView != null) {
                mVideoView.setCamera(camera);
                mVideoView.startDraw();
            }
        } else if(camera.deviceType == 3){

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
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
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
        height = Utils.getScreenHeight() - Utils.getStatusBarHeight(getApplicationContext());

        mParentLayout = new RelativeLayout(getApplicationContext());

        RelativeLayout mVideoContainer = new RelativeLayout(
                getApplicationContext());
        mVideoContainer.setBackgroundColor(Color.parseColor("#27b8fa"));
        mVideoContainer.setPadding(PADDING_WIDTH,
                PADDING_WIDTH, PADDING_WIDTH,
                PADDING_WIDTH);
        camera = CameraManager.getInstance().currentCamera;
        if(camera.deviceType == 1 || camera.deviceType == 2){
            mVideoView = new CameraView(getApplicationContext());
            mVideoContainer.addView(mVideoView, new LayoutParams(
                    (int) (width * SCALE), (int) (3 * SCALE * width / 4)));
            mVideoView.setBackgroundColor(Color.BLACK);
        } else if(camera.deviceType == 3){
            cameraViewOnvif = new SurfaceView(this);
            holder = cameraViewOnvif.getHolder();
            holder.addCallback(this);
            holder.setFormat(PixelFormat.TRANSLUCENT);
            cameraViewOnvif.setZOrderMediaOverlay(true);
            mVideoContainer.addView(cameraViewOnvif, new LayoutParams(
                    (int) (width * SCALE), (int) (3 * SCALE * width / 4)));
        }

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
        params.y = (int) (height - 3 * SCALE * width / 4 - 40);
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

                            if (x < -width / 4) {
                                x = -width / 4;
                            }
                            if (x > 3 * width / 4 - 40) {
                                x = 3 * width / 4 - 40;
                            }
                            if (y < -3 * width / 16) {
                                y = -3 * width / 16;
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

        if(camera.deviceType == 1 || camera.deviceType == 2){
            CameraManager.getInstance().removeSession(camera);
        }
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

    private LibVLC libvlc;
    private EventHandler mEventHandler;
    private final static int VideoSizeChanged = -1;
    private SurfaceHolder holder;
    private int mVideoWidth;
    private int mVideoHeight;

    public void createPlayer(Context context) {
        releasePlayer();
        try {
            // Create a new media player
            libvlc = new LibVLC();
            mEventHandler = libvlc.getEventHandler();
            libvlc.init(context);
            libvlc.setHardwareAcceleration(LibVLC.HW_ACCELERATION_FULL);
            libvlc.setSubtitlesEncoding("");
            libvlc.setAout(LibVLC.AOUT_OPENSLES);
            libvlc.setTimeStretching(true);
            libvlc.setVerboseMode(false);
            libvlc.setNetworkCaching(300);
            NativeCrashHandler.getInstance().setOnNativeCrashListener(
                    nativecrashListener);
            libvlc.setVout(LibVLC.VOUT_ANDROID_WINDOW);
            LibVLC.restartInstance(context);
            mEventHandler.addHandler(mHandler);
            holder.setKeepScreenOn(true);
        } catch (Exception e) {
            Log.e("dungnt", e.toString());
        }
    }

    public void play(String media) {
        if(libvlc != null){
            MediaList list = libvlc.getMediaList();
            list.clear();
            list.add(new Media(libvlc, LibVLC.PathToURI(media)), false);
            libvlc.playIndex(0);
//                mute();
        }
    }

    private Handler mHandler = new MyHandler(this);

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        createPlayer(TheActivityManager.getInstance().getCurrentActivity());
        libvlc.attachSurface(holder.getSurface(), this);
        String streamLink = "";
        if(Define.NETWORK_TYPE == Define.NetworkType.LocalNetwork){
            streamLink = camera.localStreamLink;
        } else if(Define.NETWORK_TYPE == Define.NetworkType.DnsNetwork){
            streamLink = camera.dnsStreamLink;
        }
        play(streamLink);
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width,
                               int height) {
//            if (libvlc != null)
//                libvlc.attachSurface(holder.getSurface(), this);

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        releasePlayer();
    }

    public void eventHardwareAccelerationError() {
        Log.e("dungnt", "Error with hardware acceleration");
        releasePlayer();
    }

    @Override
    public void setSurfaceLayout(int width, int height, int visible_width,
                                 int visible_height, int sar_num, int sar_den) {
        Message msg = Message.obtain(mHandler, VideoSizeChanged, width,
                height);
        msg.sendToTarget();
    }

    // Used only for old stuff
    @Override
    public int configureSurface(Surface surface, int width, int height,
                                int hal) {
        Log.d("", "configureSurface: width = " + width + ", height = "
                + height);
        if (LibVlcUtil.isICSOrLater() || surface == null)
            return -1;
        if (width * height == 0)
            return 0;
        if (hal != 0)
            holder.setFormat(hal);
        holder.setFixedSize(width, height);
        return 1;
    }

    private class MyHandler extends Handler {
        private WeakReference<PreviewService> mOwner;

        public MyHandler(PreviewService owner) {
            mOwner = new WeakReference<>(owner);
        }

        @Override
        public void handleMessage(Message msg) {
            PreviewService player = mOwner.get();

            // SamplePlayer events
            if (msg.what == VideoSizeChanged) {
//                player.setSize(msg.arg1, msg.arg2);
                return;
            }

            // Libvlc events
            Bundle b = msg.getData();
            switch (b.getInt("event")) {
                case EventHandler.MediaPlayerEndReached:
                    player.releasePlayer();
                    break;
                case EventHandler.MediaPlayerPlaying:
                case EventHandler.MediaPlayerPaused:
                case EventHandler.MediaPlayerStopped:
                default:
                    break;
            }
        }
    }

    public NativeCrashHandler.OnNativeCrashListener nativecrashListener = new NativeCrashHandler.OnNativeCrashListener() {

        @Override
        public void onNativeCrash() {
            Log.e("vlcdebug", "nativecrash");
        }

    };

    public void releasePlayer() {
        if (libvlc == null)
            return;
        mEventHandler.removeHandler(mHandler);
        libvlc.stop();
        libvlc.detachSurface();
        libvlc.closeAout();
        libvlc.destroy();
        libvlc = null;

        mVideoWidth = 0;
        mVideoHeight = 0;
    }

    private void setSize(int width, int height) {
        if (libvlc != null) {
            libvlc.closeAout();
            libvlc.setVolume(0);
        }

        // Dimensions of the native video
        mVideoWidth = width;
        mVideoHeight = height;

        if (mVideoWidth * mVideoHeight <= 1)
            return;

        // Dimensions of the surface frame
        int surfaceFrameW = cameraViewOnvif.getMeasuredWidth();
        int surfaceFrameH = cameraViewOnvif.getMeasuredHeight();


        float videoAR = (float) mVideoWidth / (float) mVideoHeight;
        float surfaceFrameAr = (float) surfaceFrameW / (float) surfaceFrameH;

        int vidW = surfaceFrameW;
        int vidH = surfaceFrameH;

        if (surfaceFrameAr < videoAR)
            vidH = (int) (surfaceFrameW / videoAR);
        else
            vidW = (int) (surfaceFrameH * videoAR);

        // force surface buffer size
        if (holder != null) {
            holder.setFixedSize(mVideoWidth, mVideoHeight);

        } else {
            Log.e("dungnt", "Holder was null");
        }


        // set display size
        ViewGroup.LayoutParams lp = cameraViewOnvif.getLayoutParams();
        lp.width = vidW;
        lp.height = vidH;
        cameraViewOnvif.setLayoutParams(lp);
        cameraViewOnvif.invalidate();
    }

    public void mute() {
        libvlc.closeAout();
        libvlc.setVolume(0);
    }

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
