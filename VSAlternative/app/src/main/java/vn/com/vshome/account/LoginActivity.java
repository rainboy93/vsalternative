package vn.com.vshome.account;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;

import vn.com.vshome.BaseActivity;
import vn.com.vshome.R;
import vn.com.vshome.view.NonSwipeViewPager;

public class LoginActivity extends BaseActivity {

    private AccountViewPagerAdapter mAdapter;

    private NonSwipeViewPager mViewPager;

    private ViewPager.OnPageChangeListener mListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAdapter = new AccountViewPagerAdapter(getSupportFragmentManager());
        mViewPager = (NonSwipeViewPager) findViewById(R.id.account_view_pager);

        mListener = new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position == 1) {
                    FragmentSetting fragmentSetting = (FragmentSetting) mAdapter.instantiateItem(mViewPager, position);
                    fragmentSetting.refresh();
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        };

        try {
            mViewPager.removeOnPageChangeListener(mListener);
        } catch (Exception e) {
            e.printStackTrace();
        }

        mViewPager.addOnPageChangeListener(mListener);
        mViewPager.setAdapter(mAdapter);
    }

    public void changeToSetting() {
        mViewPager.setCurrentItem(1);
    }

    @Override
    public void onBackPressed() {
        if (mViewPager.getCurrentItem() != 0) {
            mViewPager.setCurrentItem(0, true);
        } else {
            super.onBackPressed();
        }
    }
}
