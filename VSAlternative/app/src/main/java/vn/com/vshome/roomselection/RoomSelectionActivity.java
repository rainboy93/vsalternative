package vn.com.vshome.roomselection;

import android.support.v7.app.AppCompatActivity;

import android.os.Bundle;

import vn.com.vshome.BaseActivity;
import vn.com.vshome.R;
import vn.com.vshome.view.NonSwipeViewPager;


public class RoomSelectionActivity extends BaseActivity {

    private RoomSelectionAdapter mAdapter;
    private NonSwipeViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room_selection);

        mAdapter = new RoomSelectionAdapter(getSupportFragmentManager());
        mViewPager = (NonSwipeViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mAdapter);

    }
}
