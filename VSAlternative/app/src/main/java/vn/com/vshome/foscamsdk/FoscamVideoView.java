package vn.com.vshome.foscamsdk;

import vn.com.vshome.VSHome;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

public class FoscamVideoView extends View implements Runnable {

    private boolean isDraw = true;
    private boolean isFirstDraw = true;

    private RectF rectF = new RectF();

    public int surfaceWidth = 0;
    public int surfaceHeight = 0;
    private int imgWidth = 0;
    private int imgHeight = 0;

    private boolean isFullScreen = false;

    private Thread drawThread;

    private int id;

    public FoscamVideoView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

    }

    public FoscamVideoView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setID(int id) {
        this.id = id;
    }

    public void setFullScreen() {
        isFullScreen = true;
    }

    public void stopStream() {
        isDraw = false;

        if (drawThread != null) {
            try {
                drawThread.interrupt();
                drawThread = null;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public FoscamVideoView(Context context) {
        super(context);
    }

    public void startStream() {
        drawThread = new Thread(this);
        drawThread.start();
    }

    @Override
    public void run() {
        while (isDraw) {


            SystemClock.sleep(10);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

    }
}
