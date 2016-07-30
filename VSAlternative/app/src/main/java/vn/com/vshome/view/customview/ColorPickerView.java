package vn.com.vshome.view.customview;

import vn.com.vshome.R;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ComposeShader;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.RadialGradient;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.SweepGradient;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public class ColorPickerView extends View {

	private Paint colorWheelPaint;
	private Paint colorViewPaint;
	private Paint colorShadowPaint;

	private RectF colorPointerCoords;

	private Bitmap colorWheelBitmap;
	private Bitmap colorPointer;
	private Options options;

	private int colorWheelRadius;

	private boolean canDraw = true;

	public boolean isCanDraw() {
		return canDraw;
	}

	public void setCanDraw(boolean canDraw) {
		this.canDraw = canDraw;
	}

	private OnColorPickerListener mListener;

	private float[] colorHSV = new float[] { 0f, 0f, 1f };

	public ColorPickerView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	public ColorPickerView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public ColorPickerView(Context context) {
		super(context);
		init();
	}

	private void init() {

		colorWheelPaint = new Paint();
		colorWheelPaint.setAntiAlias(true);
		colorWheelPaint.setFilterBitmap(true);
		colorWheelPaint.setDither(true);

		colorViewPaint = new Paint();
		colorViewPaint.setAntiAlias(true);

		colorShadowPaint = new Paint();
		colorShadowPaint.setAntiAlias(true);
		colorShadowPaint.setColor(Color.parseColor("#11000000"));

		colorPointerCoords = new RectF();

		options = new Options();
		options.inSampleSize = 1;
		colorPointer = BitmapFactory.decodeResource(getResources(),
				R.drawable.rgb_control, options);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int widthSize = MeasureSpec.getSize(widthMeasureSpec);
		int heightSize = MeasureSpec.getSize(heightMeasureSpec);
		int size = Math.min(widthSize, heightSize);
		setMeasuredDimension(size, size);
	}

	@SuppressLint("DrawAllocation")
	@Override
	protected void onDraw(Canvas canvas) {

		int centerX = getWidth() / 2;
		int centerY = getHeight() / 2;

		colorWheelPaint.setFlags(Paint.FILTER_BITMAP_FLAG);
		canvas.drawBitmap(colorWheelBitmap, centerX - colorWheelRadius, centerY
				- colorWheelRadius, colorWheelPaint);

		colorViewPaint.setColor(Color.HSVToColor(colorHSV));

		float hueAngle = (float) Math.toRadians(colorHSV[0]);
		int colorPointX = (int) (-Math.cos(hueAngle) * colorHSV[1] * colorWheelRadius)
				+ centerX;
		int colorPointY = (int) (-Math.sin(hueAngle) * colorHSV[1] * colorWheelRadius)
				+ centerY;

		float pointerRadius = 0.3f * colorWheelRadius;
		int pointerX = (int) (colorPointX - pointerRadius / 2);
		int pointerY = (int) (colorPointY - pointerRadius / 2);

		colorPointerCoords.set(pointerX, pointerY, pointerX + pointerRadius,
				pointerY + pointerRadius);
		canvas.drawBitmap(colorPointer, null, colorPointerCoords,
				colorWheelPaint);
	}

	public void setOnColorPickerListener(OnColorPickerListener listener) {
		mListener = listener;
	}

	@Override
	protected void onSizeChanged(int width, int height, int oldw, int oldh) {
		colorWheelRadius = 9 * width / 20;
		colorWheelBitmap = createColorWheelBitmap(9 * width / 10,
				9 * height / 10);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (!canDraw) {
			return true;
		}
		colorHSV[2] = 1.0f;
		int action = event.getAction();
		switch (action) {
		case MotionEvent.ACTION_DOWN:
			int xd = (int) event.getX();
			int yd = (int) event.getY();
			int cxd = xd - getWidth() / 2;
			int cyd = yd - getHeight() / 2;
			double dd = Math.sqrt(cxd * cxd + cyd * cyd);

			if (dd <= colorWheelRadius - 10) {

				colorHSV[0] = (float) (Math.toDegrees(Math.atan2(cyd, cxd)) + 180f);
				colorHSV[1] = Math.max(0f,
						Math.min(1f, (float) (dd / colorWheelRadius)));
				if (mListener != null) {
					mListener.onColorPicker(colorHSV);
				}
				invalidate();
			}
			if (mListener != null) {
				mListener.onTouchDown(colorHSV);
			}
			return true;
		case MotionEvent.ACTION_MOVE:

			int x = (int) event.getX();
			int y = (int) event.getY();
			int cx = x - getWidth() / 2;
			int cy = y - getHeight() / 2;
			double d = Math.sqrt(cx * cx + cy * cy);

			if (d <= colorWheelRadius - 10) {

				colorHSV[0] = (float) (Math.toDegrees(Math.atan2(cy, cx)) + 180f);
				colorHSV[1] = Math.max(0f,
						Math.min(1f, (float) (d / colorWheelRadius)));
				if (mListener != null) {
					mListener.onColorPicker(colorHSV);
				}
				invalidate();
			}
			return true;
		case MotionEvent.ACTION_UP:
		case MotionEvent.ACTION_CANCEL:
			if (mListener != null) {
				mListener.onTouchUp(colorHSV);
			}
			break;
		}
		return super.onTouchEvent(event);
	}

	public void setColor(int color) {
		Color.colorToHSV(color, colorHSV);
		invalidate();
	}

	public int getColor() {
		return Color.HSVToColor(colorHSV);
	}

	@Override
	protected Parcelable onSaveInstanceState() {
		Bundle state = new Bundle();
		state.putFloatArray("color", colorHSV);
		state.putParcelable("super", super.onSaveInstanceState());
		return state;
	}

	@Override
	protected void onRestoreInstanceState(Parcelable state) {
		if (state instanceof Bundle) {
			Bundle bundle = (Bundle) state;
			colorHSV = bundle.getFloatArray("color");
			super.onRestoreInstanceState(bundle.getParcelable("super"));
		} else {
			super.onRestoreInstanceState(state);
		}
	}

	public interface OnColorPickerListener {
		void onColorPicker(float[] currentHSV);

		void onTouchDown(float[] currentHSV);

		void onTouchUp(float[] currentHSV);
	}

	private Bitmap createColorWheelBitmap(int width, int height) {

		Bitmap bitmap = Bitmap.createBitmap(width, height,
				Bitmap.Config.ARGB_8888);

		int colorCount = 12;
		int colorAngleStep = 360 / 12;
		int colors[] = new int[colorCount + 1];
		float hsv[] = new float[] { 0f, 1f, 1f };
		for (int i = 0; i < colors.length; i++) {
			hsv[0] = (i * colorAngleStep + 180) % 360;
			colors[i] = Color.HSVToColor(hsv);
		}
		colors[colorCount] = colors[0];

		SweepGradient sweepGradient = new SweepGradient(width / 2, height / 2,
				colors, null);
		RadialGradient radialGradient = new RadialGradient(width / 2,
				height / 2, colorWheelRadius, 0xFFFFFFFF, 0x00FFFFFF,
				Shader.TileMode.CLAMP);
		ComposeShader composeShader = new ComposeShader(sweepGradient,
				radialGradient, PorterDuff.Mode.SRC_OVER);

		colorWheelPaint.setShader(composeShader);

		Canvas canvas = new Canvas(bitmap);
		canvas.drawCircle(width / 2, height / 2, colorWheelRadius,
				colorWheelPaint);
		canvas.drawCircle(width / 2, height / 2, 9 * colorWheelRadius / 10,
				colorShadowPaint);

		return bitmap;
	}
}
