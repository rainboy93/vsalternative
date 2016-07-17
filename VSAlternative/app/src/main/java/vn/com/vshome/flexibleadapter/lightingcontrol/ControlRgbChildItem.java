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
import vn.com.vshome.view.customview.ColorPickerView;

public class ControlRgbChildItem extends AbstractControlItem<ControlRgbChildItem.ControlRgbChildViewHolder>
        implements ISectionable<ControlRgbChildItem.ControlRgbChildViewHolder, IHeader>, IFilterable,
        ColorPickerView.OnColorPickerListener{

    private static final long serialVersionUID = 2519281529221244210L;

    /**
     * The header of this item
     */
    IHeader header;

    private long deviceId;

    public ControlRgbChildItem(String id, long deviceId) {
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
        return R.layout.lighting_control_device_rgb;
    }

    @Override
    public ControlRgbChildViewHolder createViewHolder(FlexibleAdapter adapter, LayoutInflater inflater, ViewGroup parent) {
        try {
            ControlLayoutManager layoutManager = (ControlLayoutManager) adapter.getRecyclerView().getLayoutManager();
            this.layoutManager = layoutManager;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ControlRgbChildViewHolder(inflater.inflate(getLayoutRes(), parent, false), adapter);
    }

    @SuppressWarnings("deprecation")
    @Override
    public void bindViewHolder(FlexibleAdapter adapter, ControlRgbChildViewHolder holder, int position, List payloads) {
        device = LightingDevice.findById(LightingDevice.class, deviceId);
        deviceState = LightingDevice.findById(DeviceState.class, deviceId);
        holder.mName.setText(device.name);
        holder.mColorPicker.setOnColorPickerListener(this);
        if (deviceState.state == Define.STATE_ON) {
            holder.mButtonControl.setSelected(true);
        } else if (deviceState.state == Define.STATE_OFF) {
            holder.mButtonControl.setSelected(false);
        }
    }

    @Override
    public boolean filter(String constraint) {
        return getTitle() != null && getTitle().toLowerCase().trim().contains(constraint);
    }

    @Override
    public void onColorPicker(float[] currentHSV) {

    }

    @Override
    public void onTouchDown(float[] currentHSV) {
        if(layoutManager != null){
            layoutManager.setScrollEnable(false);
        }
    }

    @Override
    public void onTouchUp(float[] currentHSV) {
        if(layoutManager != null){
            layoutManager.setScrollEnable(true);
        }
    }

    class ControlRgbChildViewHolder extends FlexibleViewHolder {

        public TextView mName;
        public ImageButton mButtonControl;
        public ColorPickerView mColorPicker;

        public ControlRgbChildViewHolder(View view, FlexibleAdapter adapter) {
            super(view, adapter);
            this.mName = (TextView) view.findViewById(R.id.lighting_control_rgb_name);
            this.mButtonControl = (ImageButton) view.findViewById(R.id.lighting_control_rgb_button_control);
            this.mColorPicker = (ColorPickerView) view.findViewById(R.id.lighting_control_rgb_color_picker);
            this.mColorPicker.getLayoutParams().width = 3 * Utils.getScreenWidth() / 5;
            this.mColorPicker.getLayoutParams().height = Utils.getScreenWidth();
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