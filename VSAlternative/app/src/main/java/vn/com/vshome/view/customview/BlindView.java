package vn.com.vshome.view.customview;

import java.text.DecimalFormat;
import java.util.Timer;
import java.util.TimerTask;

import vn.com.vshome.R;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.BitmapFactory.Options;
import android.os.Handler;
import android.os.Message;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;


public class BlindView extends View {

    private float fScale = 1.0f;

    private int stateToSave;

    private Paint mPointerPaint;
    private Paint mTextTopPaint;
    private Paint mTextBotPaint;
    private Paint mMainCirclePaint;
    private Paint mProgressCirclePaint;
    private Paint mOuterCirclePaint;
    private Paint mCrossPaint;
    private Paint mPaint;
    private Path path;

    private RectF rectF = new RectF();
    private Shader shader;
    private float mCurrentAngle = 0;
    private int mCurrentProgress = 0;

    private static final float START_ANGLE = 270;
    private static final int WIDTH = 1153;
    private static final int HEIGHT = 694;
    private static final int BLIND_HEIGHT = 166;
    private static final int CENTER_X = WIDTH / 2;
    private static final int CENTER_Y = HEIGHT / 2;

    private static final String MAIN_CIRCLE_COLOR = "#4b4a4a";
    private static final String PROGRESS_CIRCLE_COLOR = "#0289c6";
    private static final String OUTER_CIRCLE_COLOR = "#e9e9e9";

    private static final int MAIN_CIRCLE_STROKE = 70;
    private static final int MAIN_CIRCLE_SIZE = 466;
    private static final int OUTER_CIRCLE_STROKE = 26;
    private static final int OUTER_CIRCLE_SIZE = 578;
    private static final int POINTER_SIZE = 80;
    private static final int CONTROL_SIZE = 150;

    private Bitmap mBackgroundBitmap;
    private Bitmap mPointerBitmap;
    private Bitmap mControlBitmap;

    private boolean canDraw = true;
    private boolean canControl = true;

    public boolean isCanDraw() {
        return canDraw;
    }

    public void setCanControl(boolean canControl) {
        this.canControl = canControl;
    }

    public void setCanDraw(boolean canDraw) {
        this.canDraw = canDraw;
    }

    private Options options;

    private DecimalFormat format = new DecimalFormat("0");

    private String mType = "ÐÓNG RÈM";

    public BlindView(Context context) {
        super(context);
        init();
    }

    public BlindView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public BlindView(Context context, AttributeSet attrs,
                     int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void initPaint() {
        shader = new LinearGradient(
                fScale * (WIDTH / 2 - MAIN_CIRCLE_SIZE / 2), fScale * HEIGHT
                / 2, fScale * (WIDTH / 2 + MAIN_CIRCLE_SIZE / 2),
                fScale * HEIGHT / 2, Color.parseColor("#26b7f9"),
                Color.parseColor(PROGRESS_CIRCLE_COLOR), Shader.TileMode.CLAMP);

        mMainCirclePaint = new Paint();
        mMainCirclePaint.setAntiAlias(true);
        mMainCirclePaint.setFilterBitmap(true);
        mMainCirclePaint.setStrokeWidth(fScale * MAIN_CIRCLE_STROKE / 2);
        mMainCirclePaint.setStrokeCap(Paint.Cap.ROUND);
        mMainCirclePaint.setStyle(Paint.Style.STROKE);
        mMainCirclePaint.setColor(Color.parseColor(MAIN_CIRCLE_COLOR));
        mMainCirclePaint.setFlags(Paint.ANTI_ALIAS_FLAG);

        mProgressCirclePaint = new Paint();
        mProgressCirclePaint.setAntiAlias(true);
        mProgressCirclePaint.setFilterBitmap(true);
        mProgressCirclePaint.setStrokeWidth(fScale * MAIN_CIRCLE_STROKE / 2);
        mProgressCirclePaint.setStrokeCap(Paint.Cap.ROUND);
        mProgressCirclePaint.setStyle(Paint.Style.STROKE);
        mProgressCirclePaint.setShader(shader);
        mProgressCirclePaint.setColor(Color.parseColor(PROGRESS_CIRCLE_COLOR));
        mProgressCirclePaint.setFlags(Paint.ANTI_ALIAS_FLAG);

        mPointerPaint = new Paint();
        mPointerPaint.setAntiAlias(true);
        mPointerPaint.setFilterBitmap(true);
        mPointerPaint.setFlags(Paint.FILTER_BITMAP_FLAG);

        mTextBotPaint = new Paint();
        mTextBotPaint.setAntiAlias(true);
        mTextBotPaint.setFilterBitmap(true);
        mTextBotPaint.setTextSize(40 * fScale);
        mTextBotPaint.setColor(Color.parseColor(PROGRESS_CIRCLE_COLOR));

        mTextTopPaint = new Paint();
        mTextTopPaint.setAntiAlias(true);
        mTextTopPaint.setFilterBitmap(true);
        mTextTopPaint.setColor(Color.parseColor(PROGRESS_CIRCLE_COLOR));
        mTextTopPaint.setTextSize(fScale * 70);
        mTextTopPaint.setFlags(Paint.ANTI_ALIAS_FLAG);

        mOuterCirclePaint = new Paint();
        mOuterCirclePaint.setAntiAlias(true);
        mOuterCirclePaint.setStyle(Paint.Style.STROKE);
        mOuterCirclePaint.setStrokeWidth(fScale * OUTER_CIRCLE_STROKE);
        mOuterCirclePaint.setColor(Color.parseColor(OUTER_CIRCLE_COLOR));
        mOuterCirclePaint.setFlags(Paint.ANTI_ALIAS_FLAG);

        mCrossPaint = new Paint();
        mCrossPaint.setAntiAlias(true);
        mCrossPaint.setStyle(Paint.Style.STROKE);
        mCrossPaint.setStrokeWidth(fScale * 5);
        mCrossPaint.setPathEffect(new DashPathEffect(new float[]{5, 10}, 0));
        mCrossPaint.setColor(Color.parseColor("#bfbfbf"));
        mCrossPaint.setFlags(Paint.ANTI_ALIAS_FLAG);

        mPaint = new Paint();
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(Color.WHITE);

        path = new Path();
    }

    private void init() {
        options = new Options();
        options.inSampleSize = 1;

        mPointerBitmap = BitmapFactory.decodeResource(getResources(),
                R.drawable.slider_control);
        mBackgroundBitmap = BitmapFactory.decodeResource(getResources(),
                R.drawable.shuter_relay_open_arrow, options);
        mControlBitmap = BitmapFactory.decodeResource(getResources(),
                R.drawable.shutter_relay_play);
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
        initPaint();
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
        path.moveTo(100 * fScale, 130 * fScale);
        path.lineTo(WIDTH * fScale - 100 * fScale, HEIGHT * fScale - 130 * fScale);
        canvas.drawPath(path, mCrossPaint);

        path.moveTo(100 * fScale, HEIGHT * fScale - 130 * fScale);
        path.lineTo(WIDTH * fScale - 100 * fScale, 130 * fScale);
        canvas.drawPath(path, mCrossPaint);

        canvas.drawCircle(fScale * WIDTH / 2, fScale * HEIGHT / 2, fScale
                        * (MAIN_CIRCLE_SIZE / 2 - MAIN_CIRCLE_STROKE / 2),
                mPaint);

        if (mBackgroundBitmap != null) {
            rectF.set(0, fScale * (CENTER_Y - BLIND_HEIGHT / 2),
                    fScale * WIDTH, fScale * (CENTER_Y + BLIND_HEIGHT / 2));
            canvas.drawBitmap(mBackgroundBitmap, null, rectF, mPointerPaint);
        }

        canvas.drawCircle(fScale * CENTER_X, fScale * CENTER_Y, fScale
                        * (OUTER_CIRCLE_SIZE - OUTER_CIRCLE_STROKE) / 2,
                mOuterCirclePaint);

        canvas.drawCircle(fScale * WIDTH / 2, fScale * HEIGHT / 2, fScale
                        * (MAIN_CIRCLE_SIZE / 2 - MAIN_CIRCLE_STROKE / 2),
                mMainCirclePaint);

        rectF.set(
                fScale
                        * (WIDTH / 2 - MAIN_CIRCLE_SIZE / 2 + MAIN_CIRCLE_STROKE / 2),
                fScale
                        * (HEIGHT / 2 - MAIN_CIRCLE_SIZE / 2 + MAIN_CIRCLE_STROKE / 2),
                fScale
                        * (WIDTH / 2 + MAIN_CIRCLE_SIZE / 2 - MAIN_CIRCLE_STROKE / 2),
                fScale
                        * (HEIGHT / 2 + MAIN_CIRCLE_SIZE / 2 - MAIN_CIRCLE_STROKE / 2));
        canvas.drawArc(rectF, START_ANGLE, mCurrentAngle, false,
                mProgressCirclePaint);

        if (mPointerBitmap != null) {
            rectF.set(fScale * (WIDTH / 2 - POINTER_SIZE / 2), fScale
                    * (HEIGHT / 2 - MAIN_CIRCLE_SIZE / 2 + MAIN_CIRCLE_STROKE
                    / 2 - POINTER_SIZE / 2), fScale
                    * (WIDTH / 2 + POINTER_SIZE / 2), fScale
                    * (HEIGHT / 2 - MAIN_CIRCLE_SIZE / 2 + MAIN_CIRCLE_STROKE
                    / 2 + POINTER_SIZE / 2));
            canvas.save();
            canvas.rotate(mCurrentAngle, fScale * WIDTH / 2, fScale * HEIGHT
                    / 2);
            canvas.drawBitmap(mPointerBitmap, null, rectF, mPointerPaint);
            canvas.restore();
        }

        if (mControlBitmap != null) {
            rectF.set(fScale * (CENTER_X - CONTROL_SIZE / 2), fScale
                    * (CENTER_Y - CONTROL_SIZE / 2), fScale
                    * (CENTER_X + CONTROL_SIZE / 2), fScale
                    * (CENTER_Y + CONTROL_SIZE / 2));
            canvas.drawBitmap(mControlBitmap, null, rectF, mPointerPaint);
        }

        drawTextCentred(canvas, mTextTopPaint, mCurrentProgress + "%", fScale
                * CENTER_X, fScale * (CENTER_Y - 120));
        drawTextCentred(canvas, mTextBotPaint, mType, fScale * CENTER_X, fScale
                * (CENTER_Y + 100));
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        if (!canDraw) {
            return true;
        }

        float x = event.getX();
        float y = event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (isControlSlider(x, y)) {
                    Log.d("dungnt", "Control");
                    if (mListener != null) {
                        mListener.onControlPlay(this);
                    }
                    return false;
                }

                if (!canControl) {
                    return false;
                }

                if (!isActive) {
                    return true;
                }
                if (!isCorrectTouch(x, y)) {
                    return false;
                }
                if (mListener != null) {
                    mListener.onControlDown();
                }
                calculateAngle(x, y);
                Log.d("dungnt", "Touch Ok");
                break;
            case MotionEvent.ACTION_MOVE:
                if (!canControl) {
                    return false;
                }
                if (!isActive) {
                    return true;
                }
                if (isOutSideTouch(x, y)) {

                } else {
                    calculateAngle(x, y);
                }
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                if (!canControl) {
                    return false;
                }
                if (!isActive) {
                    return true;
                }
                if (mListener != null) {
                    mListener.onControlUp(this);
                }
                break;
            default:
                break;
        }
        return true;
    }

    private boolean isPlay = false;

    public boolean isPlay() {
        return this.isPlay;
    }

    public void play(boolean isPlay) {
        this.isPlay = isPlay;
        if (isPlay) {
            mControlBitmap = BitmapFactory.decodeResource(getResources(),
                    R.drawable.shutter_relay_stop);
            // invalidate();
            playAnimation(mType.equalsIgnoreCase("MỞ RÈM"));
        } else {
            mControlBitmap = BitmapFactory.decodeResource(getResources(),
                    R.drawable.shutter_relay_play);
            // invalidate();
            stopAnimation();
        }
    }

    private boolean isCorrectTouch(float x, float y) {
        float distanceToCenter = (x - fScale * CENTER_X)
                * (x - fScale * CENTER_X) + (y - fScale * CENTER_Y)
                * (y - fScale * CENTER_Y);
        distanceToCenter = (float) Math.sqrt(distanceToCenter);
        if (distanceToCenter > fScale
                * (MAIN_CIRCLE_SIZE / 2 - MAIN_CIRCLE_STROKE)
                && distanceToCenter < fScale * (MAIN_CIRCLE_SIZE / 2)) {
            return true;
        }
        return false;
    }

    private boolean isControlSlider(float x, float y) {
        if (x < fScale * (CENTER_X - CONTROL_SIZE / 2)
                || y < fScale * (CENTER_Y - CONTROL_SIZE / 2)
                || x > fScale * (CENTER_X + CONTROL_SIZE / 2)
                || y > fScale * (CENTER_Y + CONTROL_SIZE / 2)) {
            return false;
        }
        return true;
    }

    private boolean isOutSideTouch(float x, float y) {
        if (x < 0 || y < 0 || x > fScale * WIDTH || y > fScale * HEIGHT) {
            return true;
        }
        return false;
    }

    private void calculateAngle(float x, float y) {
        mCurrentAngle = (float) ((Math.atan2(y - fScale * CENTER_Y, x - fScale
                * CENTER_X)
                / Math.PI * 180) % 360);
        mCurrentAngle -= 270;
        if (mCurrentAngle < -360) {
            mCurrentAngle += 360;
        }
        mCurrentProgress = calculateProgress();
        invalidate();
    }

    private int calculateProgress() {
        return (int) (-100 * mCurrentAngle / 359);
    }

    public void setType(String type) {
        mType = type;
        if (type.equalsIgnoreCase("MỞ RÈM")) {
            mBackgroundBitmap = BitmapFactory.decodeResource(getResources(),
                    R.drawable.shuter_relay_open_arrow, options);
        } else {
            mBackgroundBitmap = BitmapFactory.decodeResource(getResources(),
                    R.drawable.shutter_relay_close_arrow, options);
        }
        invalidate();
    }

    private Timer timer;
    private int count;

    public void stopAnimation() {
        try {
            Log.d("dungnt", "Stop animation");
            if (timer != null) {
                timer.cancel();
                timer = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        setType(mType);
    }

    public void playAnimation(final boolean isOpen) {
        if (timer != null) {
            try {
                timer.cancel();
            } catch (Exception e) {

            }
            timer = null;
        }
        if (timer == null) {
            timer = new Timer();
        }
        count = 0;
        timer.scheduleAtFixedRate(new TimerTask() {

            @Override
            public void run() {
                switch (count % 3) {
                    case 0:
                        mBackgroundBitmap = isOpen ? BitmapFactory.decodeResource(
                                getResources(),
                                R.drawable.shuter_relay_open_arrow1, options)
                                : BitmapFactory.decodeResource(getResources(),
                                R.drawable.shuter_relay_close_arrow1,
                                options);
                        break;
                    case 1:
                        mBackgroundBitmap = isOpen ? BitmapFactory.decodeResource(
                                getResources(),
                                R.drawable.shuter_relay_open_arrow2, options)
                                : BitmapFactory.decodeResource(getResources(),
                                R.drawable.shuter_relay_close_arrow2,
                                options);
                        break;
                    case 2:
                        mBackgroundBitmap = isOpen ? BitmapFactory.decodeResource(
                                getResources(),
                                R.drawable.shuter_relay_open_arrow3, options)
                                : BitmapFactory.decodeResource(getResources(),
                                R.drawable.shuter_relay_close_arrow3,
                                options);
                        break;
                    default:
                        break;
                }
                handler.sendEmptyMessage(0);
                count++;
            }
        }, 0, 200);
    }

    final Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            postInvalidate();
            invalidate();
        }
    };

    public void setProgress(int progress) {
        Log.d("dungnt", "Progress: " + progress);
        mCurrentAngle = -360 * progress / 100;
        mCurrentProgress = progress;
        invalidate();
    }

    public int getCurrentProgress() {
        return mCurrentProgress;
    }

    private void drawTextCentred(Canvas canvas, Paint paint, String text,
                                 float cx, float cy) {
        Rect textBounds = new Rect();
        paint.getTextBounds(text, 0, text.length(), textBounds);
        canvas.drawText(text, cx - textBounds.exactCenterX(),
                cy - textBounds.exactCenterY(), paint);
    }

    public void setOnControlListener(OnBlindControlListener listener) {
        mListener = listener;
    }

    private OnBlindControlListener mListener;

    public interface OnBlindControlListener {
        void onControlPlay(BlindView seekBar);

        void onControlDown();

        void onControlUp(BlindView seekBar);
    }

    @Override
    public Parcelable onSaveInstanceState() {
        // begin boilerplate code that allows parent classes to save deviceState
        Parcelable superState = super.onSaveInstanceState();

        SavedState ss = new SavedState(superState);
        ss.stateToSave = this.stateToSave;

        return ss;
    }

    @Override
    public void onRestoreInstanceState(Parcelable state) {
        // begin boilerplate code so parent classes can restore deviceState
        if (!(state instanceof SavedState)) {
            super.onRestoreInstanceState(state);
            return;
        }

        SavedState ss = (SavedState) state;
        super.onRestoreInstanceState(ss.getSuperState());
        // end

        this.stateToSave = ss.stateToSave;
    }

    static class SavedState extends BaseSavedState {
        int stateToSave;

        SavedState(Parcelable superState) {
            super(superState);
        }

        private SavedState(Parcel in) {
            super(in);
            this.stateToSave = in.readInt();
        }

        @Override
        public void writeToParcel(Parcel out, int flags) {
            super.writeToParcel(out, flags);
            out.writeInt(this.stateToSave);
        }

        // required field that makes Parcelables from a Parcel
        public static final Creator<SavedState> CREATOR = new Creator<SavedState>() {
            public SavedState createFromParcel(Parcel in) {
                return new SavedState(in);
            }

            public SavedState[] newArray(int size) {
                return new SavedState[size];
            }
        };
    }

    private boolean isActive = true;

    public void setActive(boolean isActive) {
        this.isActive = isActive;
    }
}
