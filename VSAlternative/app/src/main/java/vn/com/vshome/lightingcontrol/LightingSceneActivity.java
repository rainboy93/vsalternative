package vn.com.vshome.lightingcontrol;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import eu.davidea.flexibleadapter.common.SmoothScrollLinearLayoutManager;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import vn.com.vshome.BaseActivity;
import vn.com.vshome.R;
import vn.com.vshome.callback.TaskCallback;
import vn.com.vshome.database.DatabaseService;
import vn.com.vshome.flexibleadapter.BaseAdapter;
import vn.com.vshome.flexibleadapter.ControlLayoutManager;
import vn.com.vshome.task.GetDeviceListTask;
import vn.com.vshome.utils.Utils;
import vn.com.vshome.view.customview.WheelView;

public class LightingSceneActivity extends BaseActivity implements TaskCallback {

    private RecyclerView mDayRecyclerView, mDeviceRecyclerView;
    private BaseAdapter mDayAdapter, mDeviceAdapter;
    private List<String> mListHours, mListMinutes, mListTimes, mListSwitches;

    private List<AbstractFlexibleItem> mListItems;

    private WheelView mWheelHour, mWheelMinute, mWheelTime, mWheelSwitch;

    private GetDeviceListTask getDeviceListTask;

    private int mRoomId = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lighting_scene);

        initView();
    }

    private void initView() {
        int screenW = Utils.getScreenWidth();

        // Init wheel view for hours
        mWheelHour = (WheelView) findViewById(R.id.lighting_scene_wheel_hour);
        mWheelHour.getLayoutParams().width = screenW / 4;
        mListHours = new ArrayList<>();
        for (int i = 1; i <= 12; i++) {
            mListHours.add(i + "");
        }
        mWheelHour.setItems(mListHours);

        // Init wheel view for minutes
        mWheelMinute = (WheelView) findViewById(R.id.lighting_scene_wheel_minute);
        mWheelMinute.getLayoutParams().width = screenW / 4;
        mListMinutes = new ArrayList<>();
        for (int i = 0; i < 60; i++) {
            mListMinutes.add(i + "");
        }
        mWheelMinute.setItems(mListMinutes);

        // Init wheel view for time AM or PM
        mWheelTime = (WheelView) findViewById(R.id.lighting_scene_wheel_time);
        mWheelTime.getLayoutParams().width = screenW / 4;
        mListTimes = new ArrayList<>();
        mListTimes.add("AM");
        mListTimes.add("PM");
        mWheelTime.setItems(mListTimes);

        // Init wheel view for active scene or not
        mWheelSwitch = (WheelView) findViewById(R.id.lighting_scene_wheel_switch);
        mWheelSwitch.getLayoutParams().width = screenW / 4;
        mListSwitches = new ArrayList<>();
        mListSwitches.add("ON");
        mListSwitches.add("OFF");
        mWheelSwitch.setItems(mListSwitches);

        mDayRecyclerView = (RecyclerView) findViewById(R.id.lighting_scene_day_recycler_view);
        LinearLayoutManager dayLinearLayoutManager = new LinearLayoutManager(this,
                LinearLayoutManager.HORIZONTAL, false);
        mDayRecyclerView.setLayoutManager(dayLinearLayoutManager);
        mDayAdapter = new BaseAdapter(DatabaseService.getListDayItem());
        mDayRecyclerView.setAdapter(mDayAdapter);

        initDeviceRecyclerView();
    }

    private void initDeviceRecyclerView(){
        getDeviceListTask = new GetDeviceListTask(mRoomId, this);
        getDeviceListTask.execute(false);
    }

    @Override
    public void onTaskComplete() {
        if(getDeviceListTask != null) {
            mListItems = getDeviceListTask.getListDevice();
            mDeviceAdapter = new BaseAdapter(mListItems);
            mDeviceAdapter.setDisplayHeadersAtStartUp(true)
                    .setAutoCollapseOnExpand(true)
                    .setAutoScrollOnExpand(true)
                    .setRemoveOrphanHeaders(false)
                    .setAnimationOnScrolling(true)
                    .setAnimationOnReverseScrolling(true);
            mDeviceAdapter.enableStickyHeaders();
            mDeviceRecyclerView = (RecyclerView) findViewById(R.id.lighting_scene_recycler_view);
            mDeviceRecyclerView.setLayoutManager(new ControlLayoutManager(this));
            mDeviceRecyclerView.setAdapter(mDeviceAdapter);
            mDeviceRecyclerView.setHasFixedSize(true);
            mDeviceRecyclerView.setItemAnimator(new DefaultItemAnimator() {
                @Override
                public boolean canReuseUpdatedViewHolder(RecyclerView.ViewHolder viewHolder) {
                    return true;
                }
            });
        }
    }
}