package vn.com.vshome.view.customview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;

import com.glidebitmappool.GlideBitmapPool;

public class CropableImageView extends View {

	private static int WIDTH = 1080;
	private static int HEIGHT = 1522;

	private ScaleGestureDetector mDetector;
	private GestureDetector mGestureDetector;

	private Bitmap mBitmap;
	private Paint mPaint;

	private Drawable drawable;

	public CropableImageView(Context context, AttributeSet attrs,
			int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		init();
	}

	public CropableImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public CropableImageView(Context context) {
		super(context);
		init();
	}

	public void setBitmap(Bitmap bitmap) {
		mDetector = new ScaleGestureDetector(getContext(), new ScaleListener());
		mGestureDetector = new GestureDetector(getContext(),
				new GestureListener());

		mBitmap = bitmap;
		drawable = new BitmapDrawable(getResources(), bitmap);
		drawable.setBounds(0, 0, drawable.getIntrinsicWidth(),
				drawable.getIntrinsicHeight());
		bmWidth = mBitmap.getWidth();
		bmHeight = mBitmap.getHeight();
		bitmapRectf.set(0, 0, WIDTH, HEIGHT);
		invalidate();
	}

	private int width, height, bmWidth, bmHeight;

	private void init() {

		bitmapRectf = new RectF();
		mPaint = new Paint();
		mPaint.setAntiAlias(true);
		mPaint.setFlags(Paint.ANTI_ALIAS_FLAG);
	}

	private float fScale = 1.0f;

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

	private float scale = 1.0f;

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		width = MeasureSpec.getSize(widthMeasureSpec);
		height = MeasureSpec.getSize(heightMeasureSpec);

		// if (height * 1.0f / HEIGHT <= width * 1.0f / WIDTH) {
		// float fScale = height * 1.0f / HEIGHT;
		// setMeasuredDimension((int) (WIDTH * fScale),
		// (int) (HEIGHT * fScale));
		// } else {
		// float fScale = width * 1.0f / WIDTH;
		// setMeasuredDimension((int) (WIDTH * fScale),
		// (int) (HEIGHT * fScale));
		// }

		setMeasuredDimension(width, height);

		if (mBitmap != null) {
			float scaleX = (float) width / (float) bmWidth;
			float scaleY = (float) height / (float) bmHeight;
			scale = Math.min(scaleX, scaleY);
			drawMatrix.setScale(scale, scale);
			scaleFactor = 1.0f;

			origWidth = scale * (float) bmWidth;
			origHeight = scale * (float) bmHeight;

			// Center the image
			redundantYSpace = (float) height - (scale * (float) bmHeight);
			redundantXSpace = (float) width - (scale * (float) bmWidth);
			redundantYSpace /= (float) 2;
			redundantXSpace /= (float) 2;

			translateX = redundantXSpace;
			translateY = redundantYSpace;

			drawMatrix.postTranslate(redundantXSpace, redundantYSpace);
			invalidate();
		}
	}

	private float origWidth, origHeight;
	private float redundantXSpace;
	private float redundantYSpace;

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		if (mBitmap != null) {
			canvas.drawBitmap(mBitmap, drawMatrix, mPaint);
		}

		mPaint.setColor(Color.WHITE);
		mPaint.setStyle(Style.STROKE);
		mPaint.setStrokeWidth(fScale * 10);
		canvas.drawRect(0, height / 2 - fScale * 335, width, height / 2
				+ fScale * 335, mPaint);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		mDetector.onTouchEvent(event);
		mGestureDetector.onTouchEvent(event);

		return true;
	}

	private RectF bitmapRectf;
	private float scaleFactor = 1.f;

	private class ScaleListener extends
			ScaleGestureDetector.SimpleOnScaleGestureListener {
		@Override
		public boolean onScale(ScaleGestureDetector detector) {
			Matrix transformationMatrix = new Matrix();
			float focusX = detector.getFocusX();
			float focusY = detector.getFocusY();

			transformationMatrix.postTranslate(-focusX, -focusY);

			float scale = detector.getScaleFactor();
			scaleFactor += scale - 1;

			boolean pass = true;
			if (scaleFactor < 0.5f) {
				scaleFactor -= scale - 1;
				pass = false;
			} else if (scaleFactor > 2.0f) {
				scaleFactor -= scale - 1;
				pass = false;
			}

			Log.d("dungnt", "Scale " + scaleFactor);

			if (pass) {
				transformationMatrix.postScale(scale, scale);
				float focusShiftX = focusX - lastFocusX;
				float focusShiftY = focusY - lastFocusY;
				transformationMatrix.postTranslate(focusX + focusShiftX, focusY
						+ focusShiftY);
				drawMatrix.postConcat(transformationMatrix);
				lastFocusX = focusX;
				lastFocusY = focusY;
				invalidate();
				return true;
			}
			return true;

		}

		@Override
		public boolean onScaleBegin(ScaleGestureDetector detector) {
			lastFocusX = detector.getFocusX();
			lastFocusY = detector.getFocusY();
			return true;
		}
	}

	Matrix drawMatrix = new Matrix();
	float lastFocusX;
	float lastFocusY;

	float translateX = 0;
	float translateY = 0;

	float[] m = new float[9];

	private class GestureListener extends
			GestureDetector.SimpleOnGestureListener {

		@Override
		public boolean onScroll(MotionEvent e1, MotionEvent e2,
				float distanceX, float distanceY) {

			drawMatrix.postTranslate(-distanceX, -distanceY);
			Log.d("dungnt", m[Matrix.MTRANS_X] + " " + m[Matrix.MTRANS_Y]);
			drawMatrix.getValues(m);
			if (m[Matrix.MTRANS_X] > width / 2
					|| m[Matrix.MTRANS_X] < width / 2 - scaleFactor * origWidth
					|| m[Matrix.MTRANS_Y] > height / 2
					|| m[Matrix.MTRANS_Y] < height / 2 - scaleFactor
							* origHeight) {
				drawMatrix.postTranslate(distanceX, distanceY);
				return false;
			}
			invalidate();
			return true;
		}

	}

	public Bitmap getBitmapFromView() {
		Bitmap returnedBitmap = GlideBitmapPool.getBitmap(getWidth(), getHeight(),
				Bitmap.Config.ARGB_8888);
		Canvas canvas = new Canvas(returnedBitmap);
		Drawable bgDrawable = getBackground();
		if (bgDrawable != null)
			bgDrawable.draw(canvas);
		else
			canvas.drawColor(Color.WHITE);
		draw(canvas);
		Bitmap result = Bitmap.createBitmap(returnedBitmap, (int) (fScale * 5),
				(int) (height / 2 - fScale * 330), (int) (width - fScale * 5),
				(int) (fScale * 660));
		returnedBitmap.recycle();
		return result;
	}
}
