package vn.com.vshome.view.customview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.RectF;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.view.View;

import com.fos.sdk.FrameData;
import com.glidebitmappool.GlideBitmapPool;

import java.nio.ByteBuffer;

import vn.com.vshome.database.Camera;
import vn.com.vshome.foscamsdk.CameraManager;
import vn.com.vshome.foscamsdk.CameraSession;
import vn.com.vshome.utils.MiscUtils;


public class CameraView extends View implements Runnable {

    private static final int WIDTH = 640;
    private static final int HEIGHT = 480;

    private float fScale = 1.0f;

    private Camera camera;
    private boolean isDrawing = false;
    private RectF rectF = new RectF();

    private CameraSession cameraSession;

    private ByteBuffer buffer = null;
    private FrameData cameraData = null;
    private Bitmap mBit;

    private int surfaceWidth, surfaceHeight;

    private boolean isFullScreen = false;

    public void setFullScreen() {
        isFullScreen = true;
    }

    public void setCamera(Camera camera) {
        this.camera = camera;
    }

    public void startDraw() {
        isDrawing = true;
        MiscUtils.runOnBackgroundThread(this, 10);
    }

    private void setRect() {
        int bitW = mBit.getWidth();
        int bitH = mBit.getHeight();
//        if (bitW / (fScale * WIDTH) < bitH / (fScale * HEIGHT)) {
//            int h = (int) (fScale * HEIGHT);
//            int w = bitW * h / bitH;
//            rectF.set(fScale * WIDTH / 2 - w / 2, fScale * HEIGHT / 2 - h / 2,
//                    fScale * WIDTH / 2 + w / 2, fScale * HEIGHT / 2 + h / 2);
//        } else {
        int w = (int) (fScale * WIDTH);
        int h = bitH * w / bitW;
        rectF.set(0, surfaceHeight / 2 - h / 2,
                fScale * WIDTH / 2 + w / 2, surfaceHeight / 2 + h / 2);
//        }
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
        surfaceWidth = right - left;
        surfaceHeight = bottom - top;
        fScale = surfaceWidth * 1.0f / WIDTH;
        super.onLayout(changed, left, top, right, bottom);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        if (isFullScreen) {

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
            try {
                if (mBit == null || mBit.getWidth() != cameraData.picWidth || mBit.getHeight() != cameraData.picHeight) {
                    mBit = GlideBitmapPool.getBitmap(cameraData.picWidth, cameraData.picHeight, Bitmap.Config.ARGB_8888);
                }
                buffer.rewind();
                mBit.copyPixelsFromBuffer(buffer);
                canvas.drawBitmap(mBit, null, rectF, null);
            } catch (Exception e) {
                canvas.drawColor(Color.BLACK);
            }
        }
    }

    @Override
    protected void onWindowVisibilityChanged(int visibility) {
        super.onWindowVisibilityChanged(visibility);
        if (visibility != View.VISIBLE) {
            if (mBit != null) {
                GlideBitmapPool.putBitmap(mBit);
                mBit = null;
                buffer = null;
            }
        } else {

        }
    }

    @Override
    public void run() {
        try {
            cameraSession = CameraManager.getInstance().getCameraSession(camera);
            cameraData = cameraSession.cameraThread.publicData;
            if (buffer == null) {
                buffer = ByteBuffer.wrap(cameraData.data);
            }
            setRect();

        } catch (Exception ex) {

        }
        postInvalidate();
        SystemClock.sleep(5);
        MiscUtils.runOnBackgroundThread(this);
    }
}
