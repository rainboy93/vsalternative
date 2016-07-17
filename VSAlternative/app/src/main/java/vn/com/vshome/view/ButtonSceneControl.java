package vn.com.vshome.view;

import vn.com.vshome.R;
import vn.com.vshome.utils.Utils;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;

public class ButtonSceneControl extends RelativeLayout implements OnTouchListener {

	private View mOn, mOff;
	private ImageView mButtonOnOff;

	private OnControlOnOffListener mListener;

	private Context mContext;

	public ButtonSceneControl(Context context) {
		super(context);
		mContext = context;
		initView();
	}

	public ButtonSceneControl(Context context, AttributeSet attrs) {
		super(context, attrs);
		mContext = context;
		initView();
	}

	public ButtonSceneControl(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		mContext = context;
		initView();
	}

	private void initView() {
		inflate(getContext(), R.layout.view_button_on_off, this);

		mButtonOnOff = (ImageView) findViewById(R.id.button_on_off);
		mOn = findViewById(R.id.button_on);
		mOff = findViewById(R.id.button_off);
		mOn.setOnTouchListener(this);
		mOff.setOnTouchListener(this);
		Utils.createRipple(getContext(), mOn);
		Utils.createRipple(getContext(), mOff);
	}

	public void setOnControlOnOffListener(
			OnControlOnOffListener controlOnOffListener) {
		mListener = controlOnOffListener;
	}

	public interface OnControlOnOffListener {
		public void OnControlOn();

		public void OnControlOff();
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {

		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			switch (v.getId()) {
			case R.id.button_on:
				mButtonOnOff.setImageResource(R.drawable.button_scene_on);
				break;

			case R.id.button_off:
				mButtonOnOff.setImageResource(R.drawable.button_scene_off);
				break;
			default:
				break;
			}
			break;
		case MotionEvent.ACTION_UP:
		case MotionEvent.ACTION_CANCEL:
			mButtonOnOff.setImageResource(R.drawable.button_scene_default);
			switch (v.getId()) {
			case R.id.button_on:
				if (mListener != null) {
					mListener.OnControlOn();
				}
				break;

			case R.id.button_off:
				if (mListener != null) {
					mListener.OnControlOff();
				}
				break;
			default:
				break;
			}
			break;

		default:
			break;
		}

		return true;
	}
}
