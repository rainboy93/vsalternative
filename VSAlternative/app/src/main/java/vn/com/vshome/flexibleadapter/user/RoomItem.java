package vn.com.vshome.flexibleadapter.user;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
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
import vn.com.vshome.database.Room;
import vn.com.vshome.flexibleadapter.AbstractModelItem;
import vn.com.vshome.utils.Define;

public class RoomItem extends AbstractModelItem<RoomItem.RoomViewHolder>
        implements ISectionable<RoomItem.RoomViewHolder, IHeader>, IFilterable {

    private static final long serialVersionUID = 2519281529221244210L;

    /**
     * The header of this item
     */
    IHeader header;
    public Room room;
    public boolean isSelected;
    public boolean isAdmin = false;

    public RoomItem(String id, Room room) {
        super(id);
        this.room = room;
        isSelected = false;
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
        return R.layout.room_item;
    }

    @Override
    public RoomViewHolder createViewHolder(FlexibleAdapter adapter, LayoutInflater inflater, ViewGroup parent) {
        return new RoomViewHolder(inflater.inflate(getLayoutRes(), parent, false), adapter);
    }

    @SuppressWarnings("deprecation")
    @Override
    public void bindViewHolder(final FlexibleAdapter adapter, RoomViewHolder holder, final int position, List payloads) {
        holder.mRoomName.setText(room.name);
        if (isAdmin || isSelected) {
            holder.mButtonControl.setSelected(true);
        } else {
            holder.mButtonControl.setSelected(false);
        }

        if (isAdmin || (VSHome.currentUser != null && VSHome.currentUser.priority != Define.PRIORITY_ADMIN)) {
            holder.mButtonControl.setOnClickListener(null);
            holder.mButtonControl.setAlpha(0.5f);
        } else {
            holder.mButtonControl.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    isSelected = !isSelected;
                    adapter.notifyItemChanged(position);
                }
            });
            holder.mButtonControl.setAlpha(1.0f);
        }
    }

    @Override
    public boolean filter(String constraint) {
        return getTitle() != null && getTitle().toLowerCase().trim().contains(constraint);
    }

    class RoomViewHolder extends FlexibleViewHolder {

        public ImageButton mButtonControl;
        public TextView mRoomName;

        public RoomViewHolder(View view, FlexibleAdapter adapter) {
            super(view, adapter);
            mButtonControl = (ImageButton) view.findViewById(R.id.user_room_button_control);
            mRoomName = (TextView) view.findViewById(R.id.user_room_name);
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