package vn.com.vshome.flexibleadapter.roomselection;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.github.siyamed.shapeimageview.mask.PorterShapeImageView;

import java.io.File;
import java.util.List;

import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.IFilterable;
import eu.davidea.flexibleadapter.items.IHeader;
import eu.davidea.flexibleadapter.items.ISectionable;
import eu.davidea.viewholders.FlexibleViewHolder;
import in.workarounds.typography.TextView;
import vn.com.vshome.R;
import vn.com.vshome.VSHome;
import vn.com.vshome.callback.RoomSelectionCallback;
import vn.com.vshome.database.Floor;
import vn.com.vshome.database.Room;
import vn.com.vshome.flexibleadapter.AbstractModelItem;
import vn.com.vshome.utils.Define;
import vn.com.vshome.utils.Utils;

public class FloorItem extends AbstractModelItem<FloorItem.RoomSelectionViewHolder>
        implements ISectionable<FloorItem.RoomSelectionViewHolder, IHeader>, IFilterable {

    private static final long serialVersionUID = 2519281529221244210L;

    /**
     * The header of this item
     */
    IHeader header;
    public Floor mFloor;
    public boolean isSelected = false;

    public FloorItem(int id, Floor floor) {
        super(id + "");
        this.mFloor = floor;
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
        return R.layout.floor_item;
    }

    @Override
    public RoomSelectionViewHolder createViewHolder(FlexibleAdapter adapter, LayoutInflater inflater, ViewGroup parent) {
        return new RoomSelectionViewHolder(inflater.inflate(getLayoutRes(), parent, false), adapter);
    }

    @SuppressWarnings("deprecation")
    @Override
    public void bindViewHolder(FlexibleAdapter adapter, RoomSelectionViewHolder holder, final int position, List payloads) {
        holder.mNumber.setText((position + 1) + "");
        if (isSelected) {
            holder.mContainer.getLayoutParams().width = 2 * Utils.getScreenWidth() / 5;
            holder.mContainer.setBackgroundResource(R.drawable.floor_selected);
            holder.mFloor.setVisibility(View.VISIBLE);
            holder.mNumber.setBackgroundResource(R.drawable.circle_blue);
            holder.mNumber.setTextColor(Color.parseColor("#27b8fa"));
        } else {
            holder.mContainer.getLayoutParams().width = Utils.getScreenWidth() / 5;
            holder.mFloor.setVisibility(View.GONE);
            holder.mNumber.setBackgroundResource(R.drawable.circle_white);
            holder.mNumber.setTextColor(Color.WHITE);
            switch (position % 4) {
                case 0:
                    holder.mContainer.setBackgroundResource(R.drawable.floor_1);
                    break;
                case 1:
                    holder.mContainer.setBackgroundResource(R.drawable.floor_2);
                    break;
                case 2:
                    holder.mContainer.setBackgroundResource(R.drawable.floor_3);
                    break;
                case 3:
                    holder.mContainer.setBackgroundResource(R.drawable.floor_4);
                    break;
                default:
                    break;
            }
        }
    }

    @Override
    public boolean filter(String constraint) {
        return getTitle() != null && getTitle().toLowerCase().trim().contains(constraint);
    }

    class RoomSelectionViewHolder extends FlexibleViewHolder {

        public LinearLayout mContainer;
        public TextView mFloor, mNumber;

        public RoomSelectionViewHolder(View view, FlexibleAdapter adapter) {
            super(view, adapter);
            mContainer = (LinearLayout) view.findViewById(R.id.floor_item_container);
            mFloor = (TextView) view.findViewById(R.id.floor_item_floor);
            mNumber = (TextView) view.findViewById(R.id.floor_item_number);
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