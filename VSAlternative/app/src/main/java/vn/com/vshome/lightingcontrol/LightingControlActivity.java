package vn.com.vshome.lightingcontrol;

import android.graphics.Color;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.balysv.materialripple.MaterialRippleLayout;

import in.workarounds.typography.Button;
import in.workarounds.typography.TextView;
import vn.com.vshome.R;
import vn.com.vshome.utils.Utils;
import vn.com.vshome.view.NonSwipeViewPager;

public class LightingControlActivity extends AppCompatActivity implements View.OnClickListener{

    private boolean isDevice = true;

    private Button mButtonDevice, mButtonScene;

    private NonSwipeViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(savedInstanceState != null){
            isDevice = savedInstanceState.getBoolean(STATE_IS_DEVICE);
        }

        setContentView(R.layout.activity_lighting_control);

        initView();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.lighting_control_drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    private NavigationView navigationView;
    private DrawerLayout drawerLayout;

    private ImageButton mMenu, mHome;
    private TextView mTitle;

    private LinearLayout mContainer;

    private LightingViewPagerAdapter mAdapter;

    private void initView(){
        initActionBar();

        mButtonDevice = (Button) findViewById(R.id.lighting_control_button_device);
        mButtonScene = (Button) findViewById(R.id.lighting_control_button_scene);


        mAdapter = new LightingViewPagerAdapter(getSupportFragmentManager());

        mViewPager = (NonSwipeViewPager) findViewById(R.id.lighting_control_view_pager);
        mViewPager.setAdapter(mAdapter);

        Utils.createRipple(this, mButtonDevice);
        Utils.createRipple(this, mButtonScene);

        mButtonDevice.setOnClickListener(this);
        mButtonScene.setOnClickListener(this);

        if(isDevice){
            setToDevice();
        } else {
            setToScene();
        }
    }

    private void initActionBar(){
        mMenu = (ImageButton) findViewById(R.id.action_bar_menu);
        mHome = (ImageButton) findViewById(R.id.action_bar_home);
        mTitle = (TextView) findViewById(R.id.action_bar_title);
        mContainer = (LinearLayout) findViewById(R.id.lighting_control_container);

        mMenu.setOnClickListener(this);
        mHome.setOnClickListener(this);

        drawerLayout = (DrawerLayout) findViewById(R.id.lighting_control_drawer_layout);
        drawerLayout.setScrimColor(Color.TRANSPARENT);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, R.string.navigation_drawer_open, R.string.navigation_drawer_close){
            @Override
            public void onDrawerSlide(View drawerView, final float slideOffset) {
                super.onDrawerSlide(drawerView, slideOffset);
                drawerLayout.post(new Runnable() {
                    @Override
                    public void run() {
                        mContainer.setTranslationX(slideOffset * navigationView.getWidth());
                    }
                });
            }
        };
        drawerLayout.setDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.lighting_control_navigation_view);
    }

    private void toggleMenu(){
        if(drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            drawerLayout.openDrawer(GravityCompat.START);
        }
    }

    private void setToDevice(){
        isDevice = true;
        mButtonScene.setSelected(false);
        mButtonDevice.setSelected(true);
        mViewPager.setCurrentItem(0, true);
    }
    private void setToScene(){
        isDevice = false;
        mButtonScene.setSelected(true);
        mButtonDevice.setSelected(false);
        mViewPager.setCurrentItem(1, true);
    }

    @Override
    public void onClick(View v) {
        if(v == mMenu){
            toggleMenu();
        } else if(v == mHome){
            finishAffinity();
        } else if(v == mButtonDevice){
            setToDevice();
        } else if(v == mButtonScene){
            setToScene();
        }
    }

    private final String STATE_IS_DEVICE = "IsDevice";

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        outState.putBoolean(STATE_IS_DEVICE, isDevice);
        super.onSaveInstanceState(outState, outPersistentState);
    }
}
