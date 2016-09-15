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
import vn.com.vshome.flexibleadapter.ControlLayoutManager;
import vn.com.vshome.utils.Define;
import vn.com.vshome.utils.Utils;
import vn.com.vshome.view.customview.SliderView;

public class ControlDimmerChildItem extends AbstractControlItem<ControlDimmerChildItem.ControlDimmerChildViewHolder>
        implements ISectionable<ControlDimmerChildItem.ControlDimmerChildViewHolder, IHeader>, IFilterable,
        SliderView.OnControlListener, View.OnClickListener {

    private static final long serialVersionUID = 2519281529221244210L;

    /**
     * The header of this item
     */
    IHeader header;

    public ControlDimmerChildItem(String id, long deviceId) {
        super(id, deviceId);
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
        return R.layout.lighting_control_device_dimmer;
    }

    @Override
    public ControlDimmerChildViewHolder createViewHolder(FlexibleAdapter adapter, LayoutInflater inflater, ViewGroup parent) {
        return new ControlDimmerChildViewHolder(inflater.inflate(getLayoutRes(), parent, false), adapter);
    }

    @SuppressWarnings("deprecation")
    @Override
    public void bindViewHolder(final FlexibleAdapter adapter, ControlDimmerChildViewHolder holder, final int position, List payloads) {
        try {
            ControlLayoutManager layoutManager = (ControlLayoutManager) adapter.getRecyclerView().getLayoutManager();
            this.layoutManager = layoutManager;
        } catch (Exception e) {
            e.printStackTrace();
        }

        device = LightingDevice.findById(LightingDevice.class, deviceId);
        deviceState = DeviceState.findById(DeviceState.class, deviceId);
        holder.mName.setText(device.name);

        if (!isControl) {
            holder.mButtonSelect.setVisibility(View.VISIBLE);
            holder.mSlider.setCurrentProgress(tempState.param);

            if (tempState.state == Define.STATE_OFF) {
                holder.mButtonControl.setSelected(false);
            } else {
                holder.mButtonControl.setSelected(true);
            }

            if (isSelected) {
                holder.mCover.setVisibility(View.GONE);
                holder.mButtonControl.setEnabled(true);
                holder.mSlider.setCanDraw(true);
            } else {
                holder.mCover.setVisibility(View.VISIBLE);
                holder.mButtonControl.setEnabled(false);
                holder.mSlider.setCanDraw(false);
            }

            holder.mSlider.setOnControlListener(new SliderView.OnControlListener() {
                @Override
                public void onControlDown(SliderView seekBar) {
                    if (layoutManager != null) {
                        layoutManager.setScrollEnable(false);
                    }
                }

                @Override
                public void onControlUp(SliderView seekBar) {
                    if (layoutManager != null) {
                        layoutManager.setScrollEnable(true);
                    }

                    tempState.param = seekBar.getCurrentProgress();
                    if (tempState.param == 0) {
                        tempState.state = Define.STATE_OFF;
                    } else if (tempState.param == 100) {
                        tempState.state = Define.STATE_ON;
                    } else {
                        tempState.state = Define.STATE_PARAM;
                    }
                    adapter.notifyItemChanged(position);
                }

                @Override
                public void onMoving(SliderView seekBar) {

                }
            });

            holder.mButtonSelect.setSelected(isSelected);
            holder.mButtonSelect.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (callback != null && callback.onSelect()) {
                        isSelected = !isSelected;
                        adapter.notifyItemChanged(position);
                    }
                    adapter.updateItem(header, null);
                }
            });
            holder.mButtonControl.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (tempState.param == 0) {
                        tempState.state = Define.STATE_ON;
                        tempState.param = 100;
                    } else {
                        tempState.state = Define.STATE_OFF;
                        tempState.param = 0;
                    }
                    adapter.notifyItemChanged(position);
                }
            });
        } else {
            holder.mSlider.setCurrentProgress(deviceState.param);
            holder.mButtonControl.setOnClickListener(this);
            holder.mSlider.setOnControlListener(this);
            if (deviceState.state == Define.STATE_OFF) {
                holder.mButtonControl.setSelected(false);
            } else {
                holder.mButtonControl.setSelected(true);
            }
        }
    }

    @Override
    public boolean filter(String constraint) {
        return getTitle() != null && getTitle().toLowerCase().trim().contains(constraint);
    }

    @Override
    public void onControlDown(SliderView seekBar) {
        if (layoutManager != null) {
            layoutManager.setScrollEnable(false);
        }
    }

    @Override
    public void onControlUp(SliderView seekBar) {
        if (layoutManager != null) {
            layoutManager.setScrollEnable(true);
        }

        if (device == null) {
            return;
        }

        if (isControl) {
            LightingDevice d = new LightingDevice(this.device);
            if (d.deviceState == null) {
                d.deviceState = new DeviceState();
            }
            d.deviceState.state = Define.STATE_PARAM;
            d.deviceState.param = seekBar.getCurrentProgress();
            startSendControlMessage(d);
        }
    }

    @Override
    public void onMoving(SliderView seekBar) {

    }

    @Override
    public void onClick(View v) {
        if (device == null || deviceState == null) {
            return;
        }
        if (isControl) {
            LightingDevice d = new LightingDevice(this.device);
            if (d.deviceState == null) {
                d.deviceState = new DeviceState();
            }
            if (deviceState.state == Define.STATE_OFF) {
                d.deviceState.state = Define.STATE_ON;
            } else {
                d.deviceState.state = Define.STATE_OFF;
            }
            startSendControlMessage(d);
        }
    }

    class ControlDimmerChildViewHolder extends FlexibleViewHolder {

        public TextView mName;
        public ImageButton mButtonControl, mButtonSelect;
        public SliderView mSlider;
        public View mCover;

        public ControlDimmerChildViewHolder(View view, FlexibleAdapter adapter) {
            super(view, adapter);
            this.mName = (TextView) view.findViewById(R.id.lighting_control_dimmer_name);
            this.mName.getLayoutParams().width = 2 * Utils.getScreenWidth() / 5;
            this.mButtonControl = (ImageButton) view.findViewById(R.id.lighting_control_dimmer_button_control);
            this.mSlider = (SliderView) view.findViewById(R.id.lighting_control_dimmer_slider);
            this.mButtonSelect = (ImageButton) view.findViewById(R.id.lighting_control_dimmer_button_select);
            this.mCover = view.findViewById(R.id.lighting_control_dimmer_cover);
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