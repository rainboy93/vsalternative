package vn.com.vshome.user;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;

import in.workarounds.typography.TextView;
import vn.com.vshome.BaseActivity;
import vn.com.vshome.R;
import vn.com.vshome.view.NonSwipeViewPager;

public class UserActivity extends BaseActivity implements OnClickListener {

    private NonSwipeViewPager mViewPager;
    private ImageButton mMenu;
    private ImageButton mHome;
    private TextView mTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        initView();
    }

    private void initView() {
        initActionBar();
        mViewPager = (NonSwipeViewPager) findViewById(R.id.user_view_pager);
    }

    private void initActionBar() {
        mMenu = (ImageButton) findViewById(R.id.action_bar_menu);
        mMenu.setImageResource(R.drawable.icon_back);
        mHome = (ImageButton) findViewById(R.id.action_bar_home);
        mTitle = (TextView) findViewById(R.id.action_bar_title);

        mMenu.setOnClickListener(this);
        mHome.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == mMenu) {
            onBackPressed();
        } else if (v == mHome) {
            onBackPressed();
        }
    }
}
