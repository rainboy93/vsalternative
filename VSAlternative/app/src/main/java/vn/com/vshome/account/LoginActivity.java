package vn.com.vshome.account;

import android.os.Bundle;

import vn.com.vshome.BaseActivity;
import vn.com.vshome.R;
import vn.com.vshome.view.NonSwipeViewPager;

public class LoginActivity extends BaseActivity {

    private AccountViewPagerAdapter mAdapter;

    private NonSwipeViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAdapter = new AccountViewPagerAdapter(getSupportFragmentManager());
        mViewPager = (NonSwipeViewPager) findViewById(R.id.account_view_pager);
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
