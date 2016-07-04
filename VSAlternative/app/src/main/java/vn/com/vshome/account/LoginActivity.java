package vn.com.vshome.account;

import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;

import java.util.List;

import vn.com.vshome.BaseActivity;
import vn.com.vshome.R;

public class LoginActivity extends BaseActivity {

    private AccountViewPagerAdapter mAdapter;

    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAdapter = new AccountViewPagerAdapter(getSupportFragmentManager());
        mViewPager = (ViewPager) findViewById(R.id.account_view_pager);
        mViewPager.setAdapter(mAdapter);


    }
}
