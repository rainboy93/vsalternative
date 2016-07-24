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
public class DeviceFragment extends BaseControlFragment implements FlexibleAdapter.OnItemClickListener,
        LightingControlCallback, TaskCallback {

    private RecyclerView mRecyclerView;
    private BaseAdapter mDeviceAdapter;
    private List<AbstractFlexibleItem> mListItems;
    private GetDeviceListTask getDeviceListTask;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (VSHome.socketManager != null && VSHome.socketManager.receiveThread != null) {
            VSHome.socketManager.receiveThread.setLightingCallback(this);
        }
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
        resetData(1, 1);
        mRecyclerView = (RecyclerView) v.findViewById(R.id.lighting_control_device_recycler_view);
    }

    @Override
    public void resetData(long floorID, long roomID) {
        Logger.LogD("Device " + floorID + " " + roomID);
        this.floorId = floorID;
        this.roomId = roomID;

        getDeviceListTask = new GetDeviceListTask((int) roomID, this);
        getDeviceListTask.execute(true);

//        mListItems = DatabaseService.getListDeviceItem((int) this.roomId, true);
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
    public void onControl(int id, boolean isControlDevice) {
        if (mListItems == null) {
            return;
        }
        if (isControlDevice) {
            TimeOutManager.getInstance().cancelCountDown();
            ProgressHUD.hideLoading(getActivity());
        }

        for (AbstractFlexibleItem item : mListItems) {
            ControlGroupItem groupItem = (ControlGroupItem) item;
            for (AbstractFlexibleItem subItem : groupItem.getSubItems()) {
                AbstractControlItem modelItem = (AbstractControlItem) subItem;
                if (id == modelItem.device.getId().intValue()) {
                    mDeviceAdapter.updateItem(subItem, null);
                    return;
                }
            }
        }
    }

    @Override
    public void onResponse(int id, int status) {
        if (status == CommandMessage.STATUS_ERROR) {
            TimeOutManager.getInstance().cancelCountDown();
            ProgressHUD.hideLoading(getActivity());
            Toaster.showMessage(getActivity(), Utils.getString(R.string.txt_no_response));
            for (AbstractFlexibleItem item : mListItems) {
                ControlGroupItem groupItem = (ControlGroupItem) item;
                for (AbstractFlexibleItem subItem : groupItem.getSubItems()) {
                    AbstractControlItem modelItem = (AbstractControlItem) subItem;
                    if (id == modelItem.device.getId().intValue()) {
                        mDeviceAdapter.updateItem(subItem, null);
                        return;
                    }
                }
            }
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
                        mDeviceAdapter.updateDataSet(mListItems, true);
                    } else {
                        mDeviceAdapter = new BaseAdapter(mListItems);
                        FlexibleAdapter.OnItemClickListener listener = DeviceFragment.this;
                        mDeviceAdapter.initializeListeners(listener);
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
