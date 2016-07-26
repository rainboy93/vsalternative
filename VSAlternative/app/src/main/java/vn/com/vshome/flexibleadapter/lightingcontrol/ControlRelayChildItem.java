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
import vn.com.vshome.database.DeviceState;
import vn.com.vshome.database.LightingDevice;
import vn.com.vshome.flexibleadapter.AbstractControlItem;
import vn.com.vshome.utils.Define;

public class ControlRelayChildItem extends AbstractControlItem<ControlRelayChildItem.ControlRelayChildViewHolder>
        implements ISectionable<ControlRelayChildItem.ControlRelayChildViewHolder, IHeader>, IFilterable,View.OnClickListener {

    private static final long serialVersionUID = 2519281529221244210L;

    /**
     * The header of this item
     */
    IHeader header;


    public ControlRelayChildItem(String id, long deviceId) {
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
        return R.layout.lighting_control_device_relay;
    }

    @Override
    public ControlRelayChildViewHolder createViewHolder(FlexibleAdapter adapter, LayoutInflater inflater, ViewGroup parent) {
        return new ControlRelayChildViewHolder(inflater.inflate(getLayoutRes(), parent, false), adapter);
    }

    @SuppressWarnings("deprecation")
    @Override
    public void bindViewHolder(final FlexibleAdapter adapter, ControlRelayChildViewHolder holder, final int position, List payloads) {
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
            holder.mButtonSelect.setSelected(isSelected);
            holder.mButtonSelect.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(callback != null && callback.onSelect()){
                        isSelected = !isSelected;
                        adapter.notifyItemChanged(position);
                    }
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
            holder.mButtonControl.setOnClickListener(this);
            if (deviceState.state == Define.STATE_ON) {
                holder.mButtonControl.setSelected(true);
            } else if (deviceState.state == Define.STATE_OFF) {
                holder.mButtonControl.setSelected(false);
            }
        }
    }

    @Override
    public boolean filter(String constraint) {
        return getTitle() != null && getTitle().toLowerCase().trim().contains(constraint);
    }

    @Override
    public void onClick(View v) {
        if(device == null || deviceState == null){
            return;
        }
        if(isControl){
            LightingDevice d = new LightingDevice(this.device);
            if(d.deviceState == null){
                d.deviceState = new DeviceState();
            }
            if (deviceState.state == Define.STATE_ON) {
                d.deviceState.state =  Define.STATE_OFF;
            } else if (deviceState.state == Define.STATE_OFF) {
                d.deviceState.state =  Define.STATE_ON;
            }
            startSendControlMessage(d);
        }
    }

    class ControlRelayChildViewHolder extends FlexibleViewHolder {

        public TextView mName;
        public ImageButton mButtonControl, mButtonSelect;
        public View mCover;

        public ControlRelayChildViewHolder(View view, FlexibleAdapter adapter) {
            super(view, adapter);
            this.mName = (TextView) view.findViewById(R.id.lighting_control_relay_name);
            this.mButtonControl = (ImageButton) view.findViewById(R.id.lighting_control_relay_button_control);
            this.mButtonSelect = (ImageButton) view.findViewById(R.id.lighting_control_relay_button_select);
            this.mCover = view.findViewById(R.id.lighting_control_relay_cover);
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