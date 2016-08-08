package vn.com.vshome.view.customview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.os.Bundle;
import android.os.Parcelable;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import vn.com.vshome.R;
import vn.com.vshome.database.Camera;
import vn.com.vshome.foscamsdk.CameraManager;
import vn.com.vshome.foscamsdk.CameraSession;

/**
 * Created by hung on 12/2/15.
 */
public class CameraView extends View {

    private static final int WIDTH = 640;
    private static final int HEIGHT = 480;

    private float fScale = 1.0f;

    private Camera camera;
    private boolean isDrawing = false;
    private RectF rectF = new RectF();

    private CameraSession cameraSession;

    public void setCamera(Camera camera) {
        this.camera = camera;
    }

    public void startDraw() {
        isDrawing = true;
        invalidate();
    }

    private void setRect() {
        int bitW = cameraSession.cameraThread.mBit.getWidth();
        int bitH = cameraSession.cameraThread.mBit.getHeight();
        if (bitW / (fScale * WIDTH) > bitH / (fScale * HEIGHT)) {
            int h = (int) (fScale * HEIGHT);
            int w = bitW * h / bitH;
            rectF.set(fScale * WIDTH / 2 - w / 2, fScale * HEIGHT / 2 - h / 2,
                    fScale * WIDTH / 2 + w / 2, fScale * HEIGHT / 2 + h / 2);
        } else {
            int w = (int) (fScale * WIDTH);
            int h = bitH * w / bitW;
            rectF.set(fScale * WIDTH / 2 - w / 2, fScale * HEIGHT / 2 - h / 2,
                    fScale * WIDTH / 2 + w / 2, fScale * HEIGHT / 2 + h / 2);
        }
    }

    public CameraView(Context context) {
        super(context);
        init();
    }

    public CameraView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CameraView(Context context, AttributeSet attrs,
                      int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {

    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right,
                            int bottom) {
        int width = right - left;
        int height = bottom - top;
        if (height * 1.0f / HEIGHT <= width * 1.0f / WIDTH) {
            fScale = height * 1.0f / HEIGHT;
        } else {
            fScale = width * 1.0f / WIDTH;
        }
        super.onLayout(changed, left, top, right, bottom);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        if (height * 1.0f / HEIGHT <= width * 1.0f / WIDTH) {
            float fScale = height * 1.0f / HEIGHT;
            setMeasuredDimension((int) (WIDTH * fScale),
                    (int) (HEIGHT * fScale));
        } else {
            float fScale = width * 1.0f / WIDTH;
            setMeasuredDimension((int) (WIDTH * fScale),
                    (int) (HEIGHT * fScale));
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawColor(Color.BLACK);

        if (isDrawing) {
            if (cameraSession == null) {
                cameraSession = CameraManager.getInstance().getCameraSession(camera);
            } else {
                if (cameraSession.cameraThread != null
                        && cameraSession.cameraThread.mBit != null) {
                    setRect();
                    canvas.drawBitmap(cameraSession.cameraThread.mBit, null, rectF, null);
                }
            }
            SystemClock.sleep(20);
            invalidate();
        }
    }
}
