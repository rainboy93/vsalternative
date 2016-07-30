package vn.com.vshome.flexibleadapter.security;

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
import vn.com.vshome.flexibleadapter.AbstractModelItem;
import vn.com.vshome.utils.Define;

public class CameraChildItem extends AbstractModelItem<CameraChildItem.ControlRelayChildViewHolder>
        implements ISectionable<CameraChildItem.ControlRelayChildViewHolder, IHeader>, IFilterable,View.OnClickListener {

    private static final long serialVersionUID = 2519281529221244210L;

    /**
     * The header of this item
     */
    IHeader header;

    private long deviceId;

    public CameraChildItem(String id, long deviceId) {
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

    }

    @Override
    public boolean filter(String constraint) {
        return getTitle() != null && getTitle().toLowerCase().trim().contains(constraint);
    }

    @Override
    public void onClick(View v) {

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