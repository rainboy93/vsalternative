package vn.com.vshome.flexibleadapter.security;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.List;

import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import eu.davidea.flexibleadapter.items.IExpandable;
import eu.davidea.flexibleadapter.items.IHeader;
import eu.davidea.viewholders.ExpandableViewHolder;
import in.workarounds.typography.TextView;
import vn.com.vshome.R;
import vn.com.vshome.database.Camera;
import vn.com.vshome.flexibleadapter.AbstractModelItem;
import vn.com.vshome.flexibleadapter.lightingcontrol.ControlRelayChildItem;
import vn.com.vshome.utils.Define;

public class CameraGroupItem
		extends AbstractModelItem<CameraGroupItem.CameraGroupHolder>
		implements IExpandable<CameraGroupItem.CameraGroupHolder, AbstractFlexibleItem>,
		IHeader<CameraGroupItem.CameraGroupHolder> {

	private static final long serialVersionUID = -1882711111814491060L;

	/* Flags for FlexibleAdapter */
	private boolean mExpanded = false;

	/* subItems list */
	private List<AbstractFlexibleItem> mListChildItems;

	public Camera camera;

	public CameraGroupItem(String id, Camera camera) {
		super(id);

		this.camera = camera;

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
	public List<AbstractFlexibleItem> getSubItems() {
		return mListChildItems;
	}

	public final boolean hasSubItems() {
		return mListChildItems != null && mListChildItems.size() > 0;
	}

	public boolean removeSubItem(ControlRelayChildItem item) {
		return item != null && mListChildItems.remove(item);
	}

	public boolean removeSubItem(int position) {
		if (mListChildItems != null && position >= 0 && position < mListChildItems.size()) {
			mListChildItems.remove(position);
			return true;
		}
		return false;
	}

	public void addSubItem(AbstractFlexibleItem childItem) {
		if (mListChildItems == null)
			mListChildItems = new ArrayList<>();
		mListChildItems.add(childItem);
	}

	public void addSubItem(int position, AbstractFlexibleItem childItem) {
		if (mListChildItems != null && position >= 0 && position < mListChildItems.size()) {
			mListChildItems.add(position, childItem);
		} else
			addSubItem(childItem);
	}

	@Override
	public int getLayoutRes() {
		return R.layout.lighting_control_device_group;
	}

	@Override
	public CameraGroupHolder createViewHolder(FlexibleAdapter adapter, LayoutInflater inflater, ViewGroup parent) {
		return new CameraGroupHolder(inflater.inflate(getLayoutRes(), parent, false), adapter);
	}

	@Override
	public void bindViewHolder(FlexibleAdapter adapter, CameraGroupHolder holder, int position, List payloads) {
		holder.mGroupName.setText(camera.deviceName);

		if(isExpanded()){
			holder.mGroupSelector.setSelected(true);
			holder.mContainer.setBackgroundResource(R.color.device_background);
			holder.mDivider.setVisibility(View.INVISIBLE);
		} else {
			holder.mGroupSelector.setSelected(false);
			holder.mContainer.setBackgroundResource(R.color.white);
			holder.mDivider.setVisibility(View.VISIBLE);
		}
	}

	class CameraGroupHolder extends ExpandableViewHolder {

		public TextView mGroupName;
		public ImageView mGroupIcon;
		public ImageButton mGroupSelector;
		public RelativeLayout mContainer;
		public View mDivider;

		public CameraGroupHolder(View view, FlexibleAdapter adapter) {
			super(view, adapter, true);//True for sticky
			mGroupName = (TextView) view.findViewById(R.id.lighting_control_device_group_name);
			mGroupIcon = (ImageView) view.findViewById(R.id.lighting_control_device_group_icon);
			mGroupIcon.setImageResource(R.drawable.capture_icon);
			mGroupSelector = (ImageButton) view.findViewById(R.id.lighting_control_device_group_selector);
			mGroupSelector.setEnabled(false);
			mContainer = (RelativeLayout) view.findViewById(R.id.lighting_control_device_group_container);
			mDivider = view.findViewById(R.id.lighting_control_device_group_divider);
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
		return "ListFloorGroupItem[" + super.toString() + "//SubItems" + mListChildItems + "]";
	}

}