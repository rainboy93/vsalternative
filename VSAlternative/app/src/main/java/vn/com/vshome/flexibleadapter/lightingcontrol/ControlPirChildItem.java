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
import vn.com.vshome.networks.CommandMessage;
import vn.com.vshome.utils.Define;
import vn.com.vshome.utils.TimeOutManager;
import vn.com.vshome.utils.Toaster;
import vn.com.vshome.view.ProgressHUD;

public class ControlPirChildItem extends AbstractControlItem<ControlPirChildItem.ControlPirChildViewHolder>
        implements ISectionable<ControlPirChildItem.ControlPirChildViewHolder, IHeader>, IFilterable, View.OnClickListener {

    private static final long serialVersionUID = 2519281529221244210L;

    /**
     * The header of this item
     */
    IHeader header;

    private long deviceId;

    public ControlPirChildItem(String id, long deviceId) {
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
        return R.layout.lighting_control_device_pir;
    }

    @Override
    public ControlPirChildViewHolder createViewHolder(FlexibleAdapter adapter, LayoutInflater inflater, ViewGroup parent) {
        return new ControlPirChildViewHolder(inflater.inflate(getLayoutRes(), parent, false), adapter);
    }

    @SuppressWarnings("deprecation")
    @Override
    public void bindViewHolder(final FlexibleAdapter adapter, ControlPirChildViewHolder holder, final int position, List payloads) {
        device = LightingDevice.findById(LightingDevice.class, deviceId);
        deviceState = LightingDevice.findById(DeviceState.class, deviceId);
        holder.mName.setText(device.name);

        if(!isControl){
            holder.mButtonSelect.setVisibility(View.VISIBLE);
            if (tempState.state == Define.STATE_ENABLE) {
                holder.mButtonControl.setSelected(true);
            } else if (tempState.state == Define.STATE_DISBALE) {
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
                    isSelected = !isSelected;
                    adapter.notifyItemChanged(position);
                }
            });
            holder.mButtonControl.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (tempState.state == Define.STATE_ENABLE) {
                        tempState.state = Define.STATE_DISBALE;
                    } else if (tempState.state == Define.STATE_DISBALE) {
                        tempState.state = Define.STATE_ENABLE;
                    }
                    adapter.notifyItemChanged(position);
                }
            });
        } else {
            if (deviceState.state == Define.STATE_ENABLE) {
                holder.mButtonControl.setSelected(true);
            } else if (deviceState.state == Define.STATE_DISBALE) {
                holder.mButtonControl.setSelected(false);
            }
            holder.mButtonControl.setOnClickListener(this);
        }
    }

    @Override
    public boolean filter(String constraint) {
        return getTitle() != null && getTitle().toLowerCase().trim().contains(constraint);
    }

    @Override
    public void onClick(View v) {
        if(device == null){
            return;
        }
        if(isControl){
            LightingDevice d = new LightingDevice(this.device);
            if(d.deviceState == null){
                d.deviceState = new DeviceState();
            }
            if (device.deviceState.state == Define.STATE_ENABLE) {
                d.deviceState.state =  Define.STATE_DISBALE;
            } else if (device.deviceState.state == Define.STATE_DISBALE) {
                d.deviceState.state =  Define.STATE_ENABLE;
            }
            startSendControlMessage(d);
        }
    }

    class ControlPirChildViewHolder extends FlexibleViewHolder {

        public TextView mName;
        public ImageButton mButtonControl, mButtonSelect;
        public View mCover;

        public ControlPirChildViewHolder(View view, FlexibleAdapter adapter) {
            super(view, adapter);
            this.mName = (TextView) view.findViewById(R.id.lighting_control_pir_name);
            this.mButtonControl = (ImageButton) view.findViewById(R.id.lighting_control_pir_button_control);
            this.mButtonSelect = (ImageButton) view.findViewById(R.id.lighting_control_pir_button_select);
            this.mCover = view.findViewById(R.id.lighting_control_pir_cover);
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