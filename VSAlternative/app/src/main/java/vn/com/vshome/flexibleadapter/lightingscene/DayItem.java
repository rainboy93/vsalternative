package vn.com.vshome.flexibleadapter.lightingscene;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;

import java.util.List;

import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.IFilterable;
import eu.davidea.flexibleadapter.items.IHeader;
import eu.davidea.flexibleadapter.items.ISectionable;
import eu.davidea.viewholders.FlexibleViewHolder;
import in.workarounds.typography.ToggleButton;
import vn.com.vshome.R;
import vn.com.vshome.flexibleadapter.AbstractModelItem;
import vn.com.vshome.utils.Utils;

public class DayItem extends AbstractModelItem<DayItem.DayViewHolder>
        implements ISectionable<DayItem.DayViewHolder, IHeader>, IFilterable, CompoundButton.OnCheckedChangeListener {

    private static final long serialVersionUID = 2519281529221244210L;

    /**
     * The header of this item
     */
    IHeader header;
    private String day;

    public DayItem(String id, String day) {
        super(id);
        this.day = day;
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
        return R.layout.lighting_scene_day_item;
    }

    @Override
    public DayViewHolder createViewHolder(FlexibleAdapter adapter, LayoutInflater inflater, ViewGroup parent) {
        return new DayViewHolder(inflater.inflate(getLayoutRes(), parent, false), adapter);
    }

    @SuppressWarnings("deprecation")
    @Override
    public void bindViewHolder(FlexibleAdapter adapter, DayViewHolder holder, int position, List payloads) {
        holder.mDayToggle.setText(day);
        holder.mDayToggle.setTextOn(day);
        holder.mDayToggle.setTextOff(day);
        holder.mDayToggle.setOnCheckedChangeListener(this);
    }

    @Override
    public boolean filter(String constraint) {
        return getTitle() != null && getTitle().toLowerCase().trim().contains(constraint);
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//        if (isChecked) {
//            buttonView.setTextColor(Color.WHITE);
//        } else {
//            buttonView.setTextColor(buttonView.getContext().getResources().getColor(R.color.blueActionbar));
//        }
    }

    class DayViewHolder extends FlexibleViewHolder {

        public ToggleButton mDayToggle;
        private RelativeLayout mDayContainer;

        public DayViewHolder(View view, FlexibleAdapter adapter) {
            super(view, adapter);
            this.mDayContainer = (RelativeLayout) view.findViewById(R.id.lighting_scene_day_container);
            int size = Utils.getScreenWidth() / 8;
            this.mDayContainer.getLayoutParams().width = size;
            this.mDayContainer.getLayoutParams().height = size;
            this.mDayToggle = (ToggleButton) view.findViewById(R.id.lighting_scene_day_toggle);
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