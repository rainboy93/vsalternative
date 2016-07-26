package vn.com.vshome.lightingcontrol;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import vn.com.vshome.BaseActivity;
import vn.com.vshome.R;
import vn.com.vshome.VSHome;
import vn.com.vshome.callback.DeviceSelectCallback;
import vn.com.vshome.callback.SceneSetUpCallback;
import vn.com.vshome.callback.TaskCallback;
import vn.com.vshome.database.DatabaseService;
import vn.com.vshome.database.LightingDevice;
import vn.com.vshome.database.Scene;
import vn.com.vshome.flexibleadapter.AbstractControlItem;
import vn.com.vshome.flexibleadapter.BaseAdapter;
import vn.com.vshome.flexibleadapter.ControlLayoutManager;
import vn.com.vshome.flexibleadapter.lightingcontrol.ControlEmptyItem;
import vn.com.vshome.flexibleadapter.lightingcontrol.ControlGroupItem;
import vn.com.vshome.flexibleadapter.lightingscene.DayItem;
import vn.com.vshome.networks.CommandMessage;
import vn.com.vshome.task.GetDeviceListTask;
import vn.com.vshome.utils.Logger;
import vn.com.vshome.utils.TimeOutManager;
import vn.com.vshome.utils.Utils;
import vn.com.vshome.view.ProgressHUD;
import vn.com.vshome.view.customview.WheelView;

public class LightingSceneActivity extends BaseActivity implements TaskCallback, View.OnClickListener,
        DeviceSelectCallback, FlexibleAdapter.OnItemClickListener, TimeOutManager.TimeOutCallback,
        SceneSetUpCallback {

    private RecyclerView mDayRecyclerView, mDeviceRecyclerView;
    private BaseAdapter mDayAdapter, mDeviceAdapter;
    private List<String> mListHours, mListMinutes, mListTimes, mListSwitches;

    private List<AbstractFlexibleItem> mListItems, mListDays;

    private WheelView mWheelHour, mWheelMinute, mWheelTime, mWheelSwitch;

    private GetDeviceListTask getDeviceListTask;

    private ImageButton mSave;

    private Scene scene;

    private Calendar calendar;

    private long mRoomId = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mRoomId = getIntent().getLongExtra("RoomId", 0);
        try {
            long sceneId = getIntent().getLongExtra("SceneId", -1);
            scene = Scene.findById(Scene.class, sceneId);
        } catch (Exception e) {
        }
        setContentView(R.layout.activity_lighting_scene);
        initView();
    }

    int hPosition, mPosition, tPosition, sPosition;

    private void initView() {
        calendar = GregorianCalendar.getInstance();
        int h;
        if (scene != null) {
            h = scene.hour;
            mPosition = scene.minute;
            sPosition = scene.schedule - 1;
        } else {
            h = calendar.get(Calendar.HOUR_OF_DAY);
            mPosition = calendar.get(Calendar.MINUTE);
            sPosition = 1;
        }
        if (h >= 12) {
            tPosition = 1;
        } else {
            tPosition = 0;
        }

        if (h == 0) {
            hPosition = 11;
        } else if (h > 12) {
            hPosition = h - 13;
        } else {
            hPosition = h - 1;
        }

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
        mListSwitches.add("OFF");
        mListSwitches.add("ON");
        mWheelSwitch.setItems(mListSwitches);

        mDayRecyclerView = (RecyclerView) findViewById(R.id.lighting_scene_day_recycler_view);
        LinearLayoutManager dayLinearLayoutManager = new LinearLayoutManager(this,
                LinearLayoutManager.HORIZONTAL, false);
        mDayRecyclerView.setLayoutManager(dayLinearLayoutManager);
        mListDays = DatabaseService.getListDayItem(scene);
        mDayAdapter = new BaseAdapter(mListDays);
        mDayRecyclerView.setAdapter(mDayAdapter);

        mSave = (ImageButton) findViewById(R.id.lighting_scene_button_save);
        mSave.setOnClickListener(this);

        initDeviceRecyclerView();

        mWheelHour.setSelection(hPosition);
        mWheelMinute.setSelection(mPosition);
        mWheelTime.setSelection(tPosition);
        mWheelSwitch.setSelection(sPosition);
    }

    private void initDeviceRecyclerView() {
        if (scene != null) {
            getDeviceListTask = new GetDeviceListTask((int) mRoomId, this, this, scene.getId().intValue());
        } else {
            getDeviceListTask = new GetDeviceListTask((int) mRoomId, this, this);
        }
        getDeviceListTask.execute(false);
    }

    @Override
    public void onTaskComplete() {
        if (getDeviceListTask != null) {
            mListItems = getDeviceListTask.getListDevice();
            mDeviceAdapter = new BaseAdapter(mListItems);
            mDeviceAdapter.initializeListeners(LightingSceneActivity.this);
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

    private boolean checkValidDevice() {
        int count = countDevice();
        if (count >= 24) {
            return false;
        } else {
            return true;
        }
    }

    private int countDevice() {
        int count = 0;
        for (AbstractFlexibleItem item : mListItems) {
            if (item instanceof AbstractControlItem && !(item instanceof ControlEmptyItem)) {
                count++;
            }
        }
        return count;
    }

    private int countDay() {
        int count = 0;
        for (AbstractFlexibleItem item : mListDays) {
            if (((DayItem) item).active == Scene.DAY_ON) {
                count++;
            }
        }
        return count;
    }

    private void saveScene() {
        CommandMessage saveScene = new CommandMessage();
        ArrayList<LightingDevice> devices = new ArrayList<>();
        for (AbstractFlexibleItem item : mListItems) {
            if (item instanceof AbstractControlItem && !(item instanceof ControlEmptyItem)) {
                AbstractControlItem controlItem = (AbstractControlItem) item;
                if (controlItem.isSelected) {
                    LightingDevice device = controlItem.device;
                    device.deviceState = controlItem.tempState;
                    devices.add(device);
                }
            }
        }
        if (scene == null) {
            Scene scene = new Scene();
            scene.monday = ((DayItem) mListDays.get(0)).active;
            scene.tuesday = ((DayItem) mListDays.get(1)).active;
            scene.wednesday = ((DayItem) mListDays.get(2)).active;
            scene.thursday = ((DayItem) mListDays.get(3)).active;
            scene.friday = ((DayItem) mListDays.get(4)).active;
            scene.saturday = ((DayItem) mListDays.get(5)).active;
            scene.sunday = ((DayItem) mListDays.get(6)).active;

            if (mWheelSwitch.getSeletedIndex() == 1) {
                scene.schedule = Scene.SCHEDULE_ON;
            } else {
                scene.schedule = Scene.SCHEDULE_OFF;
            }
            scene.minute = mWheelMinute.getSeletedIndex();
            int time = mWheelTime.getSeletedIndex();
            int hour = mWheelHour.getSeletedIndex();
            if (time == 1) {
                if (hour == 11) {
                    hour = 0;
                } else {
                    hour = hour + 13;
                }
            } else {
                hour = hour + 1;
            }
            scene.hour = hour;
            scene.roomId = (int) this.mRoomId;
            saveScene.setCreateScene(scene, devices);
        } else {
            scene.monday = ((DayItem) mListDays.get(0)).active;
            scene.tuesday = ((DayItem) mListDays.get(1)).active;
            scene.wednesday = ((DayItem) mListDays.get(2)).active;
            scene.thursday = ((DayItem) mListDays.get(3)).active;
            scene.friday = ((DayItem) mListDays.get(4)).active;
            scene.saturday = ((DayItem) mListDays.get(5)).active;
            scene.sunday = ((DayItem) mListDays.get(6)).active;

            if (mWheelSwitch.getSeletedIndex() == 1) {
                scene.schedule = Scene.SCHEDULE_ON;
            } else {
                scene.schedule = Scene.SCHEDULE_OFF;
            }
            scene.minute = mWheelMinute.getSeletedIndex();
            int time = mWheelTime.getSeletedIndex();
            int hour = mWheelHour.getSeletedIndex();
            if (time == 1) {
                if (hour == 11) {
                    hour = 0;
                } else {
                    hour = hour + 13;
                }
            } else {
                hour = hour + 1;
            }
            scene.hour = hour;
            scene.roomId = (int) this.mRoomId;
            saveScene.setEditScene(scene, devices);
        }

        VSHome.socketManager.receiveThread.setSceneSetUpCallback(this);
        ProgressHUD.showLoading(LightingSceneActivity.this);
        TimeOutManager.getInstance().startCountDown(this, 5);
        Logger.LogD(Arrays.toString(saveScene.getByteArray()));
        VSHome.socketManager.sendMessage(saveScene);
    }

    @Override
    public void onClick(View v) {
        if (v == mSave) {
            if (countDevice() == 0) {
                Utils.showErrorDialog(R.string.txt_error, R.string.txt_empty_scene, LightingSceneActivity.this);
                return;
            } else if (countDay() == 0) {
                Utils.showErrorDialog(R.string.txt_error, R.string.txt_null_date_scene, LightingSceneActivity.this);
                return;
            }
            saveScene();
        }
    }

    @Override
    public boolean onSelect() {
        return checkValidDevice();
    }

    @Override
    public boolean onItemClick(int position) {
        if (mDeviceAdapter.getItem(position) instanceof ControlGroupItem) {
            updateData();
        }
        return false;
    }

    public void updateData() {
        int count = mDeviceAdapter.getItemCount();
        for (int i = 0; i < count; i++) {
            if (mDeviceAdapter.getItem(i) instanceof ControlGroupItem) {
                mDeviceAdapter.notifyItemChanged(i);
            }
        }
    }

    @Override
    public void onTimeOut() {
        ProgressHUD.hideLoading(LightingSceneActivity.this);
        Utils.showErrorDialog("Lỗi", "Có lỗi xảy ra. Hãy thử lại.", LightingSceneActivity.this);
    }

    @Override
    public void onControl(int id) {

    }

    @Override
    public void onResponse(int status) {
        if (status == CommandMessage.STATUS_ERROR) {
            ProgressHUD.hideLoading(LightingSceneActivity.this);
            TimeOutManager.getInstance().cancelCountDown();
            Utils.showErrorDialog("Lỗi", "Có lỗi xảy ra. Hãy thử lại.", LightingSceneActivity.this);
            return;
        } else {
            ProgressHUD.hideLoading(LightingSceneActivity.this);
            TimeOutManager.getInstance().cancelCountDown();
            setResult(Activity.RESULT_OK);
            finish();
        }
    }

    @Override
    public void onBackPressed() {
        setResult(Activity.RESULT_CANCELED);
        super.onBackPressed();
    }
}