package vn.com.vshome.flexibleadapter.lightingcontrol;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.IFilterable;
import eu.davidea.flexibleadapter.items.IHeader;
import eu.davidea.flexibleadapter.items.ISectionable;
import eu.davidea.viewholders.FlexibleViewHolder;
import vn.com.vshome.R;
import vn.com.vshome.flexibleadapter.AbstractControlItem;
import vn.com.vshome.flexibleadapter.AbstractModelItem;

public class ControlEmptyItem extends AbstractModelItem<ControlEmptyItem.ControlEmptyChildViewHolder>
        implements ISectionable<ControlEmptyItem.ControlEmptyChildViewHolder, IHeader>, IFilterable {

    private static final long serialVersionUID = 2519281529221244210L;

    /**
     * The header of this item
     */
    IHeader header;

    public ControlEmptyItem(String id) {
        super(id);

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
        return R.layout.empty_layout;
    }

    @Override
    public ControlEmptyChildViewHolder createViewHolder(FlexibleAdapter adapter, LayoutInflater inflater, ViewGroup parent) {
        return new ControlEmptyChildViewHolder(inflater.inflate(getLayoutRes(), parent, false), adapter);
    }

    @SuppressWarnings("deprecation")
    @Override
    public void bindViewHolder(FlexibleAdapter adapter, ControlEmptyChildViewHolder holder, int position, List payloads) {

    }

    @Override
    public boolean filter(String constraint) {
        return getTitle() != null && getTitle().toLowerCase().trim().contains(constraint);
    }

    class ControlEmptyChildViewHolder extends FlexibleViewHolder {


        public ControlEmptyChildViewHolder(View view, FlexibleAdapter adapter) {
            super(view, adapter);
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