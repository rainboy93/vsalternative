package vn.com.vshome.account;

import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;

import java.util.List;

import vn.com.vshome.BaseActivity;
import vn.com.vshome.R;
import vn.com.vshome.view.NonSwipeViewPager;

public class LoginActivity extends BaseActivity {

    private AccountViewPagerAdapter mAdapter;

    private NonSwipeViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        if (getIntent().getExtras() != null && getIntent().getExtras().getBoolean("EXIT", false)) {
            finish();
        }

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
