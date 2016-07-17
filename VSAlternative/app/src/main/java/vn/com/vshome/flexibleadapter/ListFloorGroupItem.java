package vn.com.vshome.flexibleadapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.IExpandable;
import eu.davidea.flexibleadapter.items.IHeader;
import eu.davidea.viewholders.ExpandableViewHolder;
import in.workarounds.typography.TextView;
import vn.com.vshome.R;
import vn.com.vshome.database.Floor;

public class ListFloorGroupItem
		extends AbstractControlItem<ListFloorGroupItem.ListFloorGroupHolder>
		implements IExpandable<ListFloorGroupItem.ListFloorGroupHolder, ListFloorChildItem>,
		IHeader<ListFloorGroupItem.ListFloorGroupHolder> {

	private static final long serialVersionUID = -1882711111814491060L;

	/* Flags for FlexibleAdapter */
	private boolean mExpanded = false;

	/* subItems list */
	private List<ListFloorChildItem> mListFloorChildItems;

	private long floorId;

	public ListFloorGroupItem(String id, Long floorId) {
		super(id);

		this.floorId = floorId;

		setDraggable(false);
		setHidden(false);
		setExpanded(false);
		setSelectable(false);
	}

	@Override
	public boolean isExpanded() {
		return mExpanded;
	}

	@Override
	public void setExpanded(boolean expanded) {
		mExpanded = expanded;
	}

	@Override
	public int getExpansionLevel() {
		return 0;
	}

	@Override
	public List<ListFloorChildItem> getSubItems() {
		return mListFloorChildItems;
	}

	public final boolean hasSubItems() {
		return mListFloorChildItems != null && mListFloorChildItems.size() > 0;
	}

	public boolean removeSubItem(ListFloorChildItem item) {
		return item != null && mListFloorChildItems.remove(item);
	}

	public boolean removeSubItem(int position) {
		if (mListFloorChildItems != null && position >= 0 && position < mListFloorChildItems.size()) {
			mListFloorChildItems.remove(position);
			return true;
		}
		return false;
	}

	public void addSubItem(ListFloorChildItem listFloorChildItem) {
		if (mListFloorChildItems == null)
			mListFloorChildItems = new ArrayList<ListFloorChildItem>();
		mListFloorChildItems.add(listFloorChildItem);
	}

	public void addSubItem(int position, ListFloorChildItem listFloorChildItem) {
		if (mListFloorChildItems != null && position >= 0 && position < mListFloorChildItems.size()) {
			mListFloorChildItems.add(position, listFloorChildItem);
		} else
			addSubItem(listFloorChildItem);
	}

	@Override
	public int getLayoutRes() {
		return R.layout.lighting_list_floor_group;
	}

	@Override
	public ListFloorGroupHolder createViewHolder(FlexibleAdapter adapter, LayoutInflater inflater, ViewGroup parent) {
		return new ListFloorGroupHolder(inflater.inflate(getLayoutRes(), parent, false), adapter);
	}

	@Override
	public void bindViewHolder(FlexibleAdapter adapter, ListFloorGroupHolder holder, int position, List payloads) {
		holder.mFloorName.setText(Floor.findById(Floor.class, floorId).name);
	}

	class ListFloorGroupHolder extends ExpandableViewHolder {

		public TextView mFloorName;

		public ListFloorGroupHolder(View view, FlexibleAdapter adapter) {
			super(view, adapter, false);//True for sticky
			mFloorName = (TextView) view.findViewById(R.id.lighting_list_floor_group_name);
		}

		@Override
		protected boolean isViewExpandableOnClick() {
			return true;//true by default
		}

		@Override
		protected void expandView(int position) {
			super.expandView(position);
			//Let's notify the item has been expanded
			if (mAdapter.isExpanded(position)) mAdapter.notifyItemChanged(position, true);
		}

		@Override
		protected void collapseView(int position) {
			super.collapseView(position);
			//Let's notify the item has been collapsed
			if (!mAdapter.isExpanded(position)) mAdapter.notifyItemChanged(position, true);
		}

	}

	@Override
	public String toString() {
		return "ListFloorGroupItem[" + super.toString() + "//SubItems" + mListFloorChildItems + "]";
	}

}