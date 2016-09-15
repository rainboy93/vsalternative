package vn.com.vshome.lightingcontrol;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.common.SmoothScrollLinearLayoutManager;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import vn.com.vshome.R;
import vn.com.vshome.VSHome;
import vn.com.vshome.callback.LightingControlCallback;
import vn.com.vshome.callback.TaskCallback;
import vn.com.vshome.database.DatabaseService;
import vn.com.vshome.flexibleadapter.AbstractControlItem;
import vn.com.vshome.flexibleadapter.BaseAdapter;
import vn.com.vshome.flexibleadapter.ControlLayoutManager;
import vn.com.vshome.flexibleadapter.lightingcontrol.ControlEmptyItem;
import vn.com.vshome.flexibleadapter.lightingcontrol.ControlGroupItem;
import vn.com.vshome.networks.CommandMessage;
import vn.com.vshome.task.GetDeviceListTask;
import vn.com.vshome.utils.Define;
import vn.com.vshome.utils.Logger;
import vn.com.vshome.utils.TimeOutManager;
import vn.com.vshome.utils.Toaster;
import vn.com.vshome.utils.Utils;
import vn.com.vshome.view.ProgressHUD;

/**
 * Created by anlab on 7/7/16.
 */
public class DeviceFragment extends BaseControlFragment implements LightingControlCallback, TaskCallback {

    private RecyclerView mRecyclerView;
    private BaseAdapter mDeviceAdapter;
    private List<AbstractFlexibleItem> mListItems;
    private GetDeviceListTask getDeviceListTask;

    @Override
    public void onResume() {
        super.onResume();
        if (VSHome.socketManager != null && VSHome.socketManager.receiveThread != null) {
            VSHome.socketManager.receiveThread.setLightingCallback(this);
        }
    }

    @Override
    public void onPause() {
        if (VSHome.socketManager != null && VSHome.socketManager.receiveThread != null) {
            VSHome.socketManager.receiveThread.setLightingCallback(null);
        }
        super.onPause();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.lighting_control_device_fragment, container, false);
        initView(rootView);
        return rootView;
    }

    public static DeviceFragment newInstance(long floorID, long roomID) {
        DeviceFragment fragment = new DeviceFragment();
        Bundle args = new Bundle();
        args.putLong(Define.INTENT_FLOOR_ID, floorID);
        args.putLong(Define.INTENT_ROOM_ID, roomID);
        fragment.setArguments(args);
        return fragment;
    }

    private void initView(View v) {
        floorId = getArguments().getLong(Define.INTENT_FLOOR_ID);
        roomId = getArguments().getLong(Define.INTENT_ROOM_ID);
        resetData(floorId, roomId);
        mRecyclerView = (RecyclerView) v.findViewById(R.id.lighting_control_device_recycler_view);
    }

    @Override
    public void resetData(long floorID, long roomID) {
        Logger.LogD("Device " + floorID + " " + roomID);
        this.floorId = floorID;
        this.roomId = roomID;

        getDeviceListTask = new GetDeviceListTask((int) roomID, this, mListItems);
        getDeviceListTask.execute(true);

//        mListItems = DatabaseService.getListDeviceItem((int) this.roomId, true);
    }

    @Override
    public void onControl(final int id, boolean isControlDevice) {
        if (mListItems == null) {
            return;
        }
        if (isControlDevice) {
            TimeOutManager.getInstance().cancelCountDown();
            ProgressHUD.hideLoading(getActivity());
        } else if(VSHome.socketManager.receiveThread.isSceneControl){
            VSHome.socketManager.receiveThread.isSceneControl = false;
            TimeOutManager.getInstance().cancelCountDown();
            TimeOutManager.getInstance().startCountDown(new TimeOutManager.TimeOutCallback() {
                @Override
                public void onTimeOut() {
                    ProgressHUD.hideLoading(getActivity());
                }
            }, 1);
        }

        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                for (AbstractFlexibleItem item : mListItems) {
                    if(!(item instanceof ControlGroupItem)){
                        continue;
                    }
                    ControlGroupItem groupItem = (ControlGroupItem) item;
                    for (AbstractFlexibleItem subItem : groupItem.getSubItems()) {
                        if (subItem instanceof ControlEmptyItem) {
                            continue;
                        }
                        AbstractControlItem modelItem = (AbstractControlItem) subItem;
                        if (id == modelItem.deviceId) {
                            mDeviceAdapter.updateItem(subItem, null);
                            return;
                        }
                    }
                }
            }
        });
    }

    @Override
    public void onResponse(final int id, int status) {
        if (!((LightingControlActivity) getActivity()).isDevice()) {
            return;
        }
        if (status == CommandMessage.STATUS_ERROR) {
            TimeOutManager.getInstance().cancelCountDown();
            ProgressHUD.hideLoading(getActivity());
            Toaster.showMessage(getActivity(), Utils.getString(R.string.txt_no_response));
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    for (AbstractFlexibleItem item : mListItems) {
                        ControlGroupItem groupItem = (ControlGroupItem) item;
                        for (final AbstractFlexibleItem subItem : groupItem.getSubItems()) {
                            AbstractControlItem modelItem = (AbstractControlItem) subItem;
                            if (id == modelItem.deviceId) {
                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        mDeviceAdapter.updateItem(subItem, null);
                                    }
                                });
                                return;
                            }
                        }
                    }
                }
            });
        }
    }

    @Override
    public void onTaskComplete() {
        if (getDeviceListTask != null) {
            mListItems = getDeviceListTask.getListDevice();
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (mDeviceAdapter != null) {
                        mDeviceAdapter.updateDataSet(mListItems);
                    } else {
                        mDeviceAdapter = new BaseAdapter(mListItems);
                        mDeviceAdapter.setDisplayHeadersAtStartUp(true)
                                .setAutoCollapseOnExpand(true)
                                .setAutoScrollOnExpand(true)
                                .setRemoveOrphanHeaders(false)
                                .setAnimationOnScrolling(true)
                                .setAnimationOnReverseScrolling(true);
                        mDeviceAdapter.enableStickyHeaders();
                        mRecyclerView.setLayoutManager(new ControlLayoutManager(getActivity()));
                        mRecyclerView.setAdapter(mDeviceAdapter);
                        mRecyclerView.setHasFixedSize(true);
                        mRecyclerView.setItemAnimator(new DefaultItemAnimator() {
                            @Override
                            public boolean canReuseUpdatedViewHolder(RecyclerView.ViewHolder viewHolder) {
                                return true;
                            }
                        });
                    }
                }
            });
        }
    }
}
