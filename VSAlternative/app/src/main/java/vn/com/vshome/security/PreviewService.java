package vn.com.vshome.security;

import android.app.Service;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import vn.com.vshome.R;
import vn.com.vshome.database.Camera;
import vn.com.vshome.foscamsdk.CameraManager;
import vn.com.vshome.utils.Define;
import vn.com.vshome.utils.Utils;
import vn.com.vshome.view.customview.CameraView;

public class PreviewService extends Service {

    private WindowManager mWindowManager;
    private CameraView cameraView;
    private Camera camera;
    private boolean mIsFloatingViewAttached = false;
    private WindowManager.LayoutParams params;
    private ImageButton closeButton;
    private RelativeLayout container;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if(intent.getExtras() != null){
            camera = (Camera) intent.getExtras().getSerializable(Define.INTENT_CAMERA);
        }
        if(!mIsFloatingViewAttached){
            mWindowManager.addView(cameraView, cameraView.getLayoutParams());
        }
        if(cameraView != null){
            CameraManager.getInstance().addSession(camera, null);
            cameraView.setCamera(camera);
            cameraView.startDraw();
        }

        params.x = Utils.getScreenWidth() / 2;
        params.y = Utils.getScreenHeight() - Utils.getScreenWidth() * 3 / 8;
        mWindowManager.updateViewLayout(cameraView, params);

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mWindowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        cameraView = new CameraView(this);
        params = new WindowManager.LayoutParams(
                Utils.getScreenWidth() / 2 + Utils.getScreenWidth() / 10,
                Utils.getScreenWidth() * 3 / 8 + Utils.getScreenWidth() / 10,
                WindowManager.LayoutParams.TYPE_PHONE,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT);

        params.gravity = Gravity.TOP | Gravity.LEFT;

        mWindowManager.addView(cameraView, params);

        closeButton = new ImageButton(this);
        closeButton.setImageResource(R.drawable.x_gray);

        container = new RelativeLayout(this);


        cameraView.setOnTouchListener(new View.OnTouchListener() {
            private int initialX;
            private int initialY;
            private float initialTouchX;
            private float initialTouchY;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        initialX = params.x;
                        initialY = params.y;
                        initialTouchX = event.getRawX();
                        initialTouchY = event.getRawY();
                        return true;
                    case MotionEvent.ACTION_UP:
                        return true;
                    case MotionEvent.ACTION_MOVE:
                        params.x = initialX + (int) (event.getRawX() - initialTouchX);
                        params.y = initialY + (int) (event.getRawY() - initialTouchY);
                        if(params.x < 0){
                            params.x = 0;
                        }
                        if(params.x > Utils.getScreenWidth() / 2){
                            params.x = Utils.getScreenWidth() / 2;
                        }
                        if(params.y < 0){
                            params.y = 0;
                        }
                        if(params.y > (Utils.getScreenHeight() - Utils.getScreenWidth() * 3 / 8)){
                            params.y = Utils.getScreenHeight() - Utils.getScreenWidth() * 3 / 8;
                        }
                        mWindowManager.updateViewLayout(cameraView, params);
                        return true;
                }
                return false;
            }
        });

        mIsFloatingViewAttached = true;
    }

    public void removeView() {
        CameraManager.getInstance().removeSession(camera);
        if (cameraView != null){
            mWindowManager.removeView(cameraView);
            mIsFloatingViewAttached = false;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        removeView();
    }
}
