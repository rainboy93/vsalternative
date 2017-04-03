package vn.com.vshome.security;

import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import in.workarounds.typography.Button;
import in.workarounds.typography.TextView;
import vn.com.vshome.BaseActivity;
import vn.com.vshome.R;
import vn.com.vshome.utils.Define;
import vn.com.vshome.utils.Toaster;
import vn.com.vshome.utils.Utils;
import vn.com.vshome.view.NonSwipeViewPager;

public class SecurityActivity extends BaseActivity implements View.OnClickListener,
        ViewPager.OnPageChangeListener {

    private SecurityViewPagerAdapter mAdapter;

    private NonSwipeViewPager mViewPager;

    private Button mButtonCamera, mButtonMotionSensor, mButtonOpenDoorSensor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_security);

        initActionBar();
        initView();
    }

    private void initView() {
        mButtonCamera = (Button) findViewById(R.id.security_button_camera);
        mButtonMotionSensor = (Button) findViewById(R.id.security_button_motion_sensor);
        mButtonOpenDoorSensor = (Button) findViewById(R.id.security_button_open_door_sensor);

        mButtonCamera.setOnClickListener(this);
        mButtonMotionSensor.setOnClickListener(this);
        mButtonOpenDoorSensor.setOnClickListener(this);

        mAdapter = new SecurityViewPagerAdapter(getSupportFragmentManager());

        mViewPager = (NonSwipeViewPager) findViewById(R.id.room_selection_view_pager);
        mViewPager.setOnPageChangeListener(this);
        mViewPager.setAdapter(mAdapter);
    }

    private ImageButton mBack, mHome;
    private TextView mTitle;

    private void initActionBar() {
        mBack = (ImageButton) findViewById(R.id.action_bar_menu);
        mHome = (ImageButton) findViewById(R.id.action_bar_home);
        mTitle = (TextView) findViewById(R.id.action_bar_title);
        mTitle.setText("An ninh");

        mBack.setOnClickListener(this);
        mBack.setImageResource(R.drawable.icon_back);
        mHome.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == mButtonCamera) {
            mViewPager.setCurrentItem(0);
        } else if (v == mButtonMotionSensor) {
            Toaster.showMessage(this, Utils.getString(R.string.warn_toast_device_not_available));
        } else if (v == mButtonOpenDoorSensor) {
            Toaster.showMessage(this, Utils.getString(R.string.warn_toast_device_not_available));
        } else if (v == mBack) {
            if (mViewPager.getCurrentItem() == 0) {
                ((SecurityFragment) mAdapter.getItem(0)).destroyView();
            }
        } else if (v == mHome) {
            if (mViewPager.getCurrentItem() == 0) {
                ((SecurityFragment) mAdapter.getItem(0)).destroyView();
            }
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        switch (position) {
            case 0:
//                mButtonCamera.setBackgroundColor(Utils.getColor(R.color.tab_press));
                break;
            case 1:

                break;
            case 2:

                break;
            default:
                break;
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
