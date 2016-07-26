package vn.com.vshome.security;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import vn.com.vshome.R;
import vn.com.vshome.view.NonSwipeViewPager;

public class SecurityActivity extends AppCompatActivity {

    private SecurityViewPagerAdapter mAdapter;

    private NonSwipeViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_security);

        mAdapter = new SecurityViewPagerAdapter(getSupportFragmentManager());

        mViewPager = (NonSwipeViewPager) findViewById(R.id.room_selection_view_pager);
        mViewPager.setAdapter(mAdapter);
    }
}
