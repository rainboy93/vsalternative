package vn.com.vshome.flexibleadapter.lightingcontrol;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import java.util.List;

import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.IFilterable;
import eu.davidea.flexibleadapter.items.IHeader;
import eu.davidea.flexibleadapter.items.ISectionable;
import eu.davidea.viewholders.FlexibleViewHolder;
import in.workarounds.typography.TextView;
import vn.com.vshome.R;
import vn.com.vshome.VSHome;
import vn.com.vshome.database.DeviceState;
import vn.com.vshome.database.LightingDevice;
import vn.com.vshome.flexibleadapter.AbstractControlItem;
import vn.com.vshome.flexibleadapter.ControlLayoutManager;
import vn.com.vshome.networks.CommandMessage;
import vn.com.vshome.utils.Define;
import vn.com.vshome.utils.TimeOutManager;
import vn.com.vshome.utils.Toaster;
import vn.com.vshome.utils.Utils;
import vn.com.vshome.view.ProgressHUD;
import vn.com.vshome.view.customview.BlindView;

public class ControlShutterRelayChildItem extends AbstractControlItem<ControlShutterRelayChildItem.ControlShutterRelayChildViewHolder>
        implements ISectionable<ControlShutterRelayChildItem.ControlShutterRelayChildViewHolder, IHeader>, IFilterable,
        BlindView.OnBlindControlListener {

    private static final long serialVersionUID = 2519281529221244210L;

    /**
     * The header of this item
     */
    IHeader header;

    private long deviceId;

    public ControlShutterRelayChildItem(String id, long deviceId) {
        super(id);

        this.deviceId = deviceId;

//		setDraggable(true);
    }

    @Override
    public IHeader getHeader() {
        return header;
    }

    @Override
    public void setHeader(IHeader header) {
        this.header = header;
    }

    @Override
    public int getLayoutRes() {
        return R.layout.lighting_control_device_shutter_relay;
    }

    @Override
    public ControlShutterRelayChildViewHolder createViewHolder(FlexibleAdapter adapter, LayoutInflater inflater, ViewGroup parent) {
        try {
            ControlLayoutManager layoutManager = (ControlLayoutManager) adapter.getRecyclerView().getLayoutManager();
            this.layoutManager = layoutManager;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ControlShutterRelayChildViewHolder(inflater.inflate(getLayoutRes(), parent, false), adapter);
    }

    @SuppressWarnings("deprecation")
    @Override
    public void bindViewHolder(final FlexibleAdapter adapter, ControlShutterRelayChildViewHolder holder, final int position, List payloads) {
        device = LightingDevice.findById(LightingDevice.class, deviceId);
        deviceState = LightingDevice.findById(DeviceState.class, deviceId);
        holder.mName.setText(device.name);


        if(!isControl){
            holder.mButtonSelect.setVisibility(View.VISIBLE);
            if (tempState.state == Define.STATE_ON) {
                holder.mButtonControl.setSelected(true);
            } else if (tempState.state == Define.STATE_OFF) {
                holder.mButtonControl.setSelected(false);
            }

            if(isSelected){
                holder.mCover.setVisibility(View.GONE);
                holder.mButtonControl.setEnabled(true);
            } else {
                holder.mCover.setVisibility(View.VISIBLE);
                holder.mButtonControl.setEnabled(false);
            }

            holder.mSlider.setOnControlListener(new BlindView.OnBlindControlListener() {
                @Override
                public void onControlPlay(BlindView seekBar) {

                }

                @Override
                public void onControlDown() {
                    if (layoutManager != null) {
                        layoutManager.setScrollEnable(false);
                    }
                }

                @Override
                public void onControlUp(BlindView seekBar) {
                    if (layoutManager != null) {
                        layoutManager.setScrollEnable(true);
                    }
                    int progress = seekBar.getCurrentProgress();
                    if(progress == 0){

                    } else if(progress == 100){

                    } else {

                    }
                    tempState.param = progress;
                }
            });

            holder.mButtonSelect.setSelected(isSelected);
            holder.mButtonSelect.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    isSelected = !isSelected;
                    adapter.notifyItemChanged(position);
                }
            });
            holder.mButtonControl.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (tempState.state == Define.STATE_ON) {
                        tempState.state = Define.STATE_OFF;
                    } else if (tempState.state == Define.STATE_OFF) {
                        tempState.state = Define.STATE_ON;
                    }
                    adapter.notifyItemChanged(position);
                }
            });
        } else {
            holder.mSlider.setProgress(deviceState.param);
            holder.mSlider.setOnControlListener(this);
            if (deviceState.state == Define.STATE_OPENING) {
                holder.mButtonControl.setSelected(true);
                holder.mSlider.setType("MỞ RÈM");
                holder.mSlider.play(true);
            } else if (deviceState.state == Define.STATE_CLOSING) {
                holder.mButtonControl.setSelected(false);
                holder.mSlider.setType("ĐÓNG RÈM");
                holder.mSlider.play(false);
            } else if(deviceState.state == Define.STATE_STOP){
                holder.mSlider.play(false);
            }
        }
    }

    @Override
    public boolean filter(String constraint) {
        return getTitle() != null && getTitle().toLowerCase().trim().contains(constraint);
    }

    @Override
    public void onControlPlay(BlindView seekBar) {
        if (device == null) {
            return;
        }

        int progress = seekBar.getCurrentProgress();

        if(Math.abs(progress - deviceState.param) <= 2){
            return;
        }

        if(isControl){
            LightingDevice d = new LightingDevice(this.device);
            d.deviceState.state = Define.STATE_PARAM;
            d.deviceState.param = progress;
            startSendControlMessage(d);
        }
    }

    @Override
    public void onControlDown() {
        if (layoutManager != null) {
            layoutManager.setScrollEnable(false);
        }
    }

    @Override
    public void onControlUp(BlindView seekBar) {
        if (layoutManager != null) {
            layoutManager.setScrollEnable(true);
        }
    }

    class ControlShutterRelayChildViewHolder extends FlexibleViewHolder {

        public TextView mName;
        public ImageButton mButtonControl, mButtonSelect;
        public View mCover;
        public BlindView mSlider;

        public ControlShutterRelayChildViewHolder(View view, FlexibleAdapter adapter) {
            super(view, adapter);
            this.mName = (TextView) view.findViewById(R.id.lighting_control_shutter_relay_name);
            this.mName.getLayoutParams().width = 2 * Utils.getScreenWidth() / 5;
            this.mButtonControl = (ImageButton) view.findViewById(R.id.lighting_control_shutter_relay_button_control);
            this.mSlider = (BlindView) view.findViewById(R.id.lighting_control_shutter_relay_slider);
            this.mSlider.getLayoutParams().height = 4 * Utils.getScreenWidth() / 5;
            this.mButtonSelect = (ImageButton) view.findViewById(R.id.lighting_control_shutter_relay_button_select);
            this.mCover = view.findViewById(R.id.lighting_control_shutter_relay_cover);
        }

        @Override
        public float getActivationElevation() {
            return 10f;
        }
    }

    @Override
    public String toString() {
        return "ListFloorChildItem[" + super.toString() + "]";
    }
}