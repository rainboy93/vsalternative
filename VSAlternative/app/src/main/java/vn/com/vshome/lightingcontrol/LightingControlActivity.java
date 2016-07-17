package vn.com.vshome.lightingcontrol;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.common.DividerItemDecoration;
import eu.davidea.flexibleadapter.common.SmoothScrollLinearLayoutManager;
import eu.davidea.flexibleadapter.items.IFlexible;
import in.workarounds.typography.Button;
import in.workarounds.typography.TextView;
import vn.com.vshome.R;
import vn.com.vshome.database.DatabaseService;
import vn.com.vshome.database.Room;
import vn.com.vshome.flexibleadapter.BaseAdapter;
import vn.com.vshome.flexibleadapter.ListFloorChildItem;
import vn.com.vshome.utils.Define;
import vn.com.vshome.utils.Utils;
import vn.com.vshome.view.NonSwipeViewPager;

public class LightingControlActivity extends AppCompatActivity implements View.OnClickListener,
        FlexibleAdapter.OnItemClickListener{

    private boolean isDevice = true;

    private Button mButtonDevice, mButtonScene;

    private NonSwipeViewPager mViewPager;

    private TextView mMenuTitle;
    private RecyclerView mMenuRecyclerView;
    private BaseAdapter mMenuAdapter;
    private long floorId, roomId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(savedInstanceState != null){
            isDevice = savedInstanceState.getBoolean(STATE_IS_DEVICE);
        } else {
            Bundle bundle = getIntent().getExtras();
            if(bundle != null){
                floorId = bundle.getLong(Define.INTENT_FLOOR_ID, 0);
                roomId = bundle.getLong(Define.INTENT_ROOM_ID, 0);
            }
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case 1000:
                if(resultCode == RESULT_OK){

                }
                break;
            default:
                break;
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


        mAdapter = new LightingViewPagerAdapter(getSupportFragmentManager(), this.floorId, this.roomId);

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

        mMenuTitle = (TextView) findViewById(R.id.lighting_control_list_floor_title);
        mMenuRecyclerView = (RecyclerView) findViewById(R.id.lighting_control_list_floor_recycler_view);

        mMenuAdapter = new BaseAdapter(DatabaseService.getListFloorItem());
        FlexibleAdapter.OnItemClickListener itemClickListener = this;
        mMenuAdapter.initializeListeners(itemClickListener);
        //Experimenting NEW features (v5.0.0)
        mMenuAdapter.setAutoCollapseOnExpand(false)
                .setAutoScrollOnExpand(true)
                .setRemoveOrphanHeaders(false)
                .setAnimationOnScrolling(true)
                .setAnimationOnReverseScrolling(true);
        mMenuRecyclerView.setLayoutManager(new SmoothScrollLinearLayoutManager(this));
        mMenuRecyclerView.setAdapter(mMenuAdapter);
        mMenuRecyclerView.setHasFixedSize(true);
        mMenuRecyclerView.setItemAnimator(new DefaultItemAnimator() {
            @Override
            public boolean canReuseUpdatedViewHolder(RecyclerView.ViewHolder viewHolder) {
                return true;
            }
        });
        mMenuRecyclerView.addItemDecoration(new DividerItemDecoration(this,
                R.drawable.divider, 0));//Increase to add gap between sections (Works only with LinearLayout!)
        //mRecyclerView.setItemAnimator(new SlideInRightAnimator());
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

    @Override
    public boolean onItemClick(int position) {
        IFlexible flexibleItem = mMenuAdapter.getItem(position);
        if(flexibleItem instanceof ListFloorChildItem){
            ListFloorChildItem childItem = (ListFloorChildItem) flexibleItem;
            Room room = childItem.getRoom();

            if(room != null && mAdapter != null){
                this.floorId = room.floorID;
                this.roomId = room.getId();
                mAdapter.resetData(floorId, roomId);
                mAdapter.notifyDataSetChanged();
            }
        }
        return false;
    }
}
