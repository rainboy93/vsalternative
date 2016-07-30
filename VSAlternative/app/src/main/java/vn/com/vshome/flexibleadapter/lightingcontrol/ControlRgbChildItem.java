package vn.com.vshome.flexibleadapter.lightingcontrol;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.ImageView;

import java.util.List;

import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.IFilterable;
import eu.davidea.flexibleadapter.items.IHeader;
import eu.davidea.flexibleadapter.items.ISectionable;
import eu.davidea.viewholders.FlexibleViewHolder;
import in.workarounds.typography.Button;
import in.workarounds.typography.TextView;
import vn.com.vshome.R;
import vn.com.vshome.VSHome;
import vn.com.vshome.database.DeviceState;
import vn.com.vshome.database.LightingDevice;
import vn.com.vshome.flexibleadapter.AbstractControlItem;
import vn.com.vshome.flexibleadapter.ControlLayoutManager;
import vn.com.vshome.networks.CommandMessage;
import vn.com.vshome.utils.Define;
import vn.com.vshome.utils.Utils;
import vn.com.vshome.view.customview.ColorPickerView;

public class ControlRgbChildItem extends AbstractControlItem<ControlRgbChildItem.ControlRgbChildViewHolder>
        implements ISectionable<ControlRgbChildItem.ControlRgbChildViewHolder, IHeader>, IFilterable,
        View.OnClickListener {

    private static final long serialVersionUID = 2519281529221244210L;

    /**
     * The header of this item
     */
    IHeader header;

    public ControlRgbChildItem(String id, long deviceId) {
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
    public void bindViewHolder(final FlexibleAdapter adapter, final ControlRgbChildViewHolder holder, final int position, List payloads) {
        device = LightingDevice.findById(LightingDevice.class, deviceId);
        holder.mName.setText(device.name);

        if (!isControl) {
            holder.mButtonSelect.setVisibility(View.VISIBLE);
            if (tempState.state == Define.STATE_OFF) {
                holder.mButtonControl.setSelected(false);
            } else {
                holder.mButtonControl.setSelected(true);
            }

            if (isSelected) {
                holder.mCover.setVisibility(View.GONE);
                holder.mButtonControl.setEnabled(true);
                holder.mColorPicker.setCanDraw(true);
            } else {
                holder.mCover.setVisibility(View.VISIBLE);
                holder.mButtonControl.setEnabled(false);
                holder.mColorPicker.setCanDraw(false);
            }
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
                    if (tempState.state == Define.STATE_OFF) {
                        tempState.state = Define.STATE_ON;
                    } else {
                        tempState.state = Define.STATE_OFF;
                    }
                    adapter.updateItem(ControlRgbChildItem.this, null);
                }
            });
            int color = Color.rgb(tempState.param1, tempState.param2, tempState.param3);
            setPreviewColor(color, holder);
            holder.mColorPicker.setColor(color);
            holder.mColorPicker.setCanDraw(true);
            holder.mColorPicker.setOnColorPickerListener(new ColorPickerView.OnColorPickerListener() {
                @Override
                public void onColorPicker(float[] currentHSV) {
                    int color = Color.HSVToColor(currentHSV);
                    setPreviewColor(color, holder);
                }

                @Override
                public void onTouchDown(float[] currentHSV) {
                    if (layoutManager != null) {
                        layoutManager.setScrollEnable(false);
                    }
                    int color = Color.HSVToColor(currentHSV);
                    setPreviewColor(color, holder);
                }

                @Override
                public void onTouchUp(float[] currentHSV) {
                    if (layoutManager != null) {
                        layoutManager.setScrollEnable(true);
                    }
                    int color = Color.HSVToColor(currentHSV);
                    setPreviewColor(color, holder);
                    tempState.state = Define.STATE_PARAM;
                    tempState.param1 = Color.red(color);
                    tempState.param2 = Color.green(color);
                    tempState.param3 = Color.blue(color);
                }
            });
        } else {
            deviceState = DeviceState.findById(DeviceState.class, deviceId);
            int color = Color.rgb(deviceState.param1, deviceState.param2, deviceState.param3);
            setPreviewColor(color, holder);
            holder.mColorPicker.setColor(color);
            holder.mColorPicker.setCanDraw(true);
            holder.mColorPicker.setOnColorPickerListener(new ColorPickerView.OnColorPickerListener() {
                @Override
                public void onColorPicker(float[] currentHSV) {
                    int color = Color.HSVToColor(currentHSV);
                    setPreviewColor(color, holder);
                }

                @Override
                public void onTouchDown(float[] currentHSV) {
                    if (layoutManager != null) {
                        layoutManager.setScrollEnable(false);
                    }
                    int color = Color.HSVToColor(currentHSV);
                    setPreviewColor(color, holder);
                }

                @Override
                public void onTouchUp(float[] currentHSV) {
                    if (layoutManager != null) {
                        layoutManager.setScrollEnable(true);
                    }
                    int color = Color.HSVToColor(currentHSV);
                    adapter.updateItem(ControlRgbChildItem.this, null);
                    showSelectColorDialog(color);
                }
            });
            if (deviceState.state == Define.STATE_OFF) {
                holder.mButtonControl.setSelected(false);
            } else {
                holder.mButtonControl.setSelected(true);
            }
            holder.mButtonControl.setOnClickListener(this);
        }
    }

    private void setPreviewColor(int color, ControlRgbChildViewHolder holder) {
        holder.mRed.setText(" " + Color.red(color));
        holder.mGreen.setText(" " + Color.green(color));
        holder.mBlue.setText(" " + Color.blue(color));
        holder.mSelectedColor.setBackgroundColor(color);
    }

    @Override
    public boolean filter(String constraint) {
        return getTitle() != null && getTitle().toLowerCase().trim().contains(constraint);
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

    class ControlRgbChildViewHolder extends FlexibleViewHolder {

        public TextView mName, mRed, mGreen, mBlue;
        public ImageButton mButtonControl, mButtonSelect;
        public View mCover;
        public ColorPickerView mColorPicker;
        public ImageView mSelectedColor;

        public ControlRgbChildViewHolder(View view, FlexibleAdapter adapter) {
            super(view, adapter);
            this.mName = (TextView) view.findViewById(R.id.lighting_control_rgb_name);
            this.mButtonControl = (ImageButton) view.findViewById(R.id.lighting_control_rgb_button_control);
            this.mColorPicker = (ColorPickerView) view.findViewById(R.id.lighting_control_rgb_color_picker);
            this.mColorPicker.getLayoutParams().width = 3 * Utils.getScreenWidth() / 5;
            this.mColorPicker.getLayoutParams().height = Utils.getScreenWidth();
            this.mButtonSelect = (ImageButton) view.findViewById(R.id.lighting_control_rgb_button_select);
            this.mCover = view.findViewById(R.id.lighting_control_rgb_cover);
            this.mSelectedColor = (ImageView) view.findViewById(R.id.lighting_control_rgb_color_selected);
            this.mRed = (TextView) view.findViewById(R.id.lighting_control_rgb_text_red);
            this.mGreen = (TextView) view.findViewById(R.id.lighting_control_rgb_text_green);
            this.mBlue = (TextView) view.findViewById(R.id.lighting_control_rgb_text_blue);
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

    private void showSelectColorDialog(final int color) {
        final Dialog dialog = new Dialog(VSHome.activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.view_dialog_select_color);

        ImageView selectedColor = (ImageView) dialog.findViewById(R.id.dialog_select_color);
        Button buttonConfirm = (Button) dialog.findViewById(R.id.dialog_select_color_button_confirm);
        ImageButton buttonCancel = (ImageButton) dialog.findViewById(R.id.dialog_select_color_button_cancel);

        selectedColor.setBackgroundColor(color);
        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });

        buttonConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
                LightingDevice d = new LightingDevice(device);
                if (d.deviceState == null) {
                    d.deviceState = new DeviceState();
                }
                d.deviceState.state = Define.STATE_PARAM;
                d.deviceState.param1 = Color.red(color);
                d.deviceState.param2 = Color.green(color);
                d.deviceState.param3 = Color.blue(color);
                startSendControlMessage(d);
            }
        });

        dialog.show();
    }
}