package vn.com.vshome.flexibleadapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.IFilterable;
import eu.davidea.flexibleadapter.items.IHeader;
import eu.davidea.flexibleadapter.items.ISectionable;
import eu.davidea.viewholders.FlexibleViewHolder;
import in.workarounds.typography.TextView;
import vn.com.vshome.R;
import vn.com.vshome.database.Room;

public class ListFloorChildItem extends AbstractControlItem<ListFloorChildItem.ChildViewHolder>
		implements ISectionable<ListFloorChildItem.ChildViewHolder, IHeader>, IFilterable {

	private static final long serialVersionUID = 2519281529221244210L;

	/**
	 * The header of this item
	 */
	IHeader header;

	private long roomId;
	private Room room;

	public ListFloorChildItem(String id, long roomId) {
		super(id);

		this.roomId = roomId;

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
		return R.layout.lighting_list_floor_child;
	}

	@Override
	public ChildViewHolder createViewHolder(FlexibleAdapter adapter, LayoutInflater inflater, ViewGroup parent) {
		return new ChildViewHolder(inflater.inflate(getLayoutRes(), parent, false), adapter);
	}

	@SuppressWarnings("deprecation")
	@Override
	public void bindViewHolder(FlexibleAdapter adapter, ChildViewHolder holder, int position, List payloads) {
		room = Room.findById(Room.class, roomId);
		holder.mRoomName.setText(room.name);
	}

	@Override
	public boolean filter(String constraint) {
		return getTitle() != null && getTitle().toLowerCase().trim().contains(constraint);
	}

	class ChildViewHolder extends FlexibleViewHolder {

		public TextView mRoomName;

		public ChildViewHolder(View view, FlexibleAdapter adapter) {
			super(view, adapter);
			this.mRoomName = (TextView) view.findViewById(R.id.lighting_list_floor_child_name);
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

	public Room getRoom(){
		return room;
	}
}