package vn.com.vshome.view.customview;

import vn.com.vshome.R;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by hung on 12/2/15.
 */
public class CircleSeekBar extends View {

	private float fScale = 1.0f;

	private Paint mPointerPaint;
	private Paint mTextTopPaint;
	private Paint mTextBotPaint;
	private Paint mMainCirclePaint;
	private Paint mProgressCirclePaint;
	private Paint mOuterCirclePaint;

	private RectF rectF = new RectF();
	private Shader shader;
	private float mCurrentAngle = 0;

	private static final float START_ANGLE = 270;
	private static final int WIDTH = 796;
	private static final int HEIGHT = 694;
	private static final int CENTER_X = WIDTH / 2;
	private static final int CENTER_Y = HEIGHT / 2;

	private static final String MAIN_CIRCLE_COLOR = "#4b4a4a";
	private static final String PROGRESS_CIRCLE_COLOR = "#0289c6";
	private static final String OUTER_CIRCLE_COLOR = "#e9e9e9";

	private static final int MAIN_CIRCLE_STROKE = 70;
	private static final int MAIN_CIRCLE_SIZE = 466;
	private static final int OUTER_CIRCLE_STROKE = 26;
	private static final int OUTER_CIRCLE_SIZE = 617;
	private static final int POINTER_SIZE = 80;

	private Bitmap mBackgroundBitmap;
	private Bitmap mPointerBitmap;

	private String mType = "TỐI DẦN";

	private Options options;

	public CircleSeekBar(Context context) {
		super(context);
		init();
	}

	public CircleSeekBar(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public CircleSeekBar(Context context, AttributeSet attrs,
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
		mTextBotPaint.setTextSize(50 * fScale);
		mTextBotPaint.setColor(Color.parseColor(PROGRESS_CIRCLE_COLOR));

		mTextTopPaint = new Paint();
		mTextTopPaint.setAntiAlias(true);
		mTextTopPaint.setFilterBitmap(true);
		mTextTopPaint.setColor(Color.parseColor(PROGRESS_CIRCLE_COLOR));
		mTextTopPaint.setTextSize(fScale * 100);
		mTextTopPaint.setFlags(Paint.ANTI_ALIAS_FLAG);

		mOuterCirclePaint = new Paint();
		mOuterCirclePaint.setAntiAlias(true);
		mOuterCirclePaint.setStyle(Paint.Style.STROKE);
		mOuterCirclePaint.setStrokeWidth(fScale * OUTER_CIRCLE_STROKE);
		mOuterCirclePaint.setColor(Color.parseColor(OUTER_CIRCLE_COLOR));
		mOuterCirclePaint.setFlags(Paint.ANTI_ALIAS_FLAG);
	}

	private void init() {
		options = new Options();
		options.inSampleSize = 2;

		mPointerBitmap = BitmapFactory.decodeResource(getResources(),
				R.drawable.slider_control);
		mBackgroundBitmap = BitmapFactory.decodeResource(getResources(),
				R.drawable.shutter_relay_background, options);
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
		rectF.set(0, 0, fScale * WIDTH, fScale * HEIGHT);
		if (mBackgroundBitmap != null) {
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

		rectF.set(fScale * (WIDTH / 2 - POINTER_SIZE / 2),
				fScale
						* (HEIGHT / 2 - MAIN_CIRCLE_SIZE / 2
								+ MAIN_CIRCLE_STROKE / 2 - POINTER_SIZE / 2),
				fScale * (WIDTH / 2 + POINTER_SIZE / 2), fScale
						* (HEIGHT / 2 - MAIN_CIRCLE_SIZE / 2
								+ MAIN_CIRCLE_STROKE / 2 + POINTER_SIZE / 2));
		canvas.save();
		canvas.rotate(mCurrentAngle, fScale * WIDTH / 2, fScale * HEIGHT / 2);
		canvas.drawBitmap(mPointerBitmap, null, rectF, mPointerPaint);
		canvas.restore();

		drawTextCentred(canvas, mTextTopPaint, calculateProgress() + " %",
				fScale * CENTER_X, fScale * (CENTER_Y - 50));
		drawTextCentred(canvas, mTextBotPaint, mType, fScale * CENTER_X, fScale
				* (CENTER_Y + 30));
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {

		float x = event.getX();
		float y = event.getY();

		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
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
			if (isOutSideTouch(x, y)) {

			} else {
				calculateAngle(x, y);
			}
			break;
		case MotionEvent.ACTION_UP:
			if (mListener != null) {
				mListener.onControlUp();
			}
			break;
		default:
			break;
		}
		return true;
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
		invalidate();
	}

	private int calculateProgress() {
		return (int) (-100 * mCurrentAngle / 359);
	}

	private void drawTextCentred(Canvas canvas, Paint paint, String text,
			float cx, float cy) {
		Rect textBounds = new Rect();
		paint.getTextBounds(text, 0, text.length(), textBounds);
		canvas.drawText(text, cx - textBounds.exactCenterX(),
				cy - textBounds.exactCenterY(), paint);
	}

	public void setProgress(int progress) {
		mCurrentAngle = -360 * progress / 100;
		invalidate();
	}

	public void setType(String type) {
		mType = type;
		invalidate();
	}

	public void setOnControlListener(OnControlListener listener) {
		mListener = listener;
	}

	private OnControlListener mListener;

	public interface OnControlListener {
		public void onControlDown();

		public void onControlUp();
	}
}
