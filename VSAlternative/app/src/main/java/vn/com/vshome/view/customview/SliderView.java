package vn.com.vshome.view.customview;

import vn.com.vshome.R;
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
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by hung on 12/2/15.
 */
public class SliderView extends View {

	private static final int WIDTH = 493;
	private static final int HEIGHT = 80;

	private static final int MAIN_HEIGHT = 50;
	private static final int PROGRESS_HEIGHT = 35;

	private Paint mMainPaint;
	private Paint mProgressPaint;
	private Paint mCirclePaint;

	private Shader shader;

	private Bitmap mPointerBitmap;

	private int mCurrentProgress = 0;

	private RectF mPointerRectF;

	private float fScale = 1.0f;

	private boolean canDraw = true;

	public boolean isCanDraw() {
		return canDraw;
	}

	public void setCanDraw(boolean canDraw) {
		this.canDraw = canDraw;
	}

	public SliderView(Context context) {
		super(context);
		init();
	}

	public SliderView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public SliderView(Context context, AttributeSet attrs,
			int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		init();
	}

	private void initPaint() {
		shader = new LinearGradient(fScale * HEIGHT / 2, fScale * HEIGHT / 2,
				fScale * (WIDTH - HEIGHT / 2), fScale * HEIGHT / 2,
				Color.parseColor("#0288c5"), Color.parseColor("#27b8fa"),
				Shader.TileMode.CLAMP);

		mMainPaint = new Paint();
		mMainPaint.setAntiAlias(true);
		mMainPaint.setStyle(Paint.Style.STROKE);
		mMainPaint.setStrokeCap(Paint.Cap.ROUND);
		mMainPaint.setStrokeJoin(Paint.Join.ROUND);
		mMainPaint.setStrokeWidth(fScale * MAIN_HEIGHT);
		mMainPaint.setFlags(Paint.ANTI_ALIAS_FLAG);
		mMainPaint.setColor(Color.parseColor("#cfcfcf"));

		mProgressPaint = new Paint();
		mProgressPaint.setAntiAlias(true);
		mProgressPaint.setStyle(Paint.Style.STROKE);
		mProgressPaint.setStrokeCap(Paint.Cap.ROUND);
		mProgressPaint.setStrokeJoin(Paint.Join.ROUND);
		mProgressPaint.setStrokeWidth(fScale * PROGRESS_HEIGHT);
		mProgressPaint.setFlags(Paint.ANTI_ALIAS_FLAG);
		mProgressPaint.setShader(shader);

		mCirclePaint = new Paint();
		mCirclePaint.setAntiAlias(true);
	}

	private void init() {
		mPointerBitmap = BitmapFactory.decodeResource(getResources(),
				R.drawable.slider_control);
		mPointerRectF = new RectF();
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
		canvas.drawLine(fScale * HEIGHT / 2, fScale * HEIGHT / 2, fScale
				* (WIDTH - HEIGHT / 2), fScale * HEIGHT / 2, mMainPaint);
		mCirclePaint.setColor(Color.parseColor("#cfcfcf"));
		canvas.drawCircle(fScale * HEIGHT / 2, fScale * HEIGHT / 2, fScale
				* MAIN_HEIGHT / 2, mCirclePaint);
		canvas.drawCircle(fScale * (WIDTH - HEIGHT / 2), fScale * HEIGHT / 2,
				fScale * MAIN_HEIGHT / 2, mCirclePaint);

		canvas.drawLine(fScale * HEIGHT / 2, fScale * HEIGHT / 2, fScale
				* (HEIGHT / 2 + mCurrentProgress * (WIDTH - HEIGHT) / 100),
				fScale * HEIGHT / 2, mProgressPaint);

		mCirclePaint.setColor(Color.parseColor("#0288c5"));
		canvas.drawCircle(fScale * HEIGHT / 2, fScale * HEIGHT / 2, fScale
				* PROGRESS_HEIGHT / 2, mCirclePaint);

		mPointerRectF.set(fScale * (mCurrentProgress * (WIDTH - HEIGHT) / 100),
				0, fScale
						* (mCurrentProgress * (WIDTH - HEIGHT) / 100 + HEIGHT),
				fScale * HEIGHT);
		canvas.drawBitmap(mPointerBitmap, null, mPointerRectF, null);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {

		float x = event.getX();
		float y = event.getY();

		if (!canDraw) {
			return true;
		}

		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			if (mListener != null) {
				mListener.onControlDown(this);
			}
			calculateProgress(x, y);
			invalidate();
			break;
		case MotionEvent.ACTION_MOVE:
			calculateProgress(x, y);
			invalidate();
			Log.d("dungnt", "Moving");
			break;
		case MotionEvent.ACTION_UP:
		case MotionEvent.ACTION_CANCEL:
			if (mListener != null) {
				mListener.onControlUp(this);
			}
			performClick();
			break;
		default:
			break;
		}

		return true;
	}

	@Override
	public boolean performClick() {
		// TODO Auto-generated method stub
		super.performClick();
		return true;
	}

	private void calculateProgress(float x, float y) {
		float startX = fScale * HEIGHT / 2;
		float endX = fScale * (WIDTH - HEIGHT / 2);
		float totalProgress = endX - startX;
		if (x < startX) {
			x = startX;
		}
		if (x > endX) {
			x = endX;
		}
		mCurrentProgress = (int) (100 * (x - startX) / totalProgress);
		Log.d("dungnt", "Current progress: " + mCurrentProgress);
	}

	public int getCurrentProgress() {
		return mCurrentProgress;
	}

	public void setCurrentProgress(int progress) {
		mCurrentProgress = progress;
		invalidate();
	}

	public void setOnControlListener(OnControlListener listener) {
		mListener = listener;
	}

	private OnControlListener mListener;

	public interface OnControlListener {
		public void onControlDown(SliderView seekBar);

		public void onControlUp(SliderView seekBar);

		void onMoving(SliderView seekBar);
	}

	@Override
	protected Parcelable onSaveInstanceState() {
		Bundle state = new Bundle();
		state.putInt("progress", mCurrentProgress);
		state.putParcelable("super", super.onSaveInstanceState());
		return state;
	}

	@Override
	protected void onRestoreInstanceState(Parcelable state) {
		if (state instanceof Bundle) {
			Bundle bundle = (Bundle) state;
			mCurrentProgress = bundle.getInt("progress");
			super.onRestoreInstanceState(bundle.getParcelable("super"));
		} else {
			super.onRestoreInstanceState(state);
		}
	}
}
