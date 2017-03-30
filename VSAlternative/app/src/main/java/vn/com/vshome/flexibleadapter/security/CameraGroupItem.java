package vn.com.vshome.flexibleadapter.security;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
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
import vn.com.vshome.callback.CameraCallback;
import vn.com.vshome.database.Camera;
import vn.com.vshome.flexibleadapter.AbstractModelItem;
import vn.com.vshome.flexibleadapter.lightingcontrol.ControlRelayChildItem;
import vn.com.vshome.foscamsdk.CameraManager;
import vn.com.vshome.foscamsdk.CameraSession;

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
        return R.layout.security_camera_group;
    }

    @Override
    public CameraGroupHolder createViewHolder(FlexibleAdapter adapter, LayoutInflater inflater, ViewGroup parent) {
        return new CameraGroupHolder(inflater.inflate(getLayoutRes(), parent, false), adapter);
    }

    @Override
    public void bindViewHolder(final FlexibleAdapter adapter, CameraGroupHolder holder, final int position, List payloads) {
        holder.mName.setText(camera.deviceName);

        CameraSession session = CameraManager.getInstance().getCameraSession(camera);

        if (isExpanded()) {
            holder.mSelector.animate().rotation(90).setDuration(500)
                    .start();
            holder.mContainer.setBackgroundResource(R.color.device_background);
            holder.mDivider.setVisibility(View.GONE);

            if(camera.deviceType == 1 || camera.deviceType == 2){
                if (session == null || !session.isHasCore && (camera.deviceType == 1 || camera.deviceType == 2)) {
                    CameraManager.getInstance().addSession(camera, new CameraCallback() {
                        @Override
                        public void onError(String msg) {
                            ((CameraChildItem) mListChildItems.get(0)).isError = true;
                            ((CameraChildItem) mListChildItems.get(0)).errorMsg = msg;
                            adapter.notifyItemChanged(position + 1);
                        }

                        @Override
                        public void onSuccess() {
                            ((CameraChildItem) mListChildItems.get(0)).isError = false;
                            adapter.notifyItemChanged(position + 1);
                        }
                    });
                }
            } else if(camera.deviceType == 3){

            }
        } else {
            holder.mSelector.setRotation(0);
            holder.mSelector.setSelected(false);
            holder.mContainer.setBackgroundResource(R.color.white);
            holder.mDivider.setVisibility(View.VISIBLE);
            ((CameraChildItem) mListChildItems.get(0)).isError = false;
            if (session != null && session.isHasCore && (camera.deviceType == 1 || camera.deviceType == 2)) {
                if (CameraManager.getInstance().isPreviewing) {
                    CameraManager.getInstance().removeAllSessionExcept(camera);
                } else {
                    CameraManager.getInstance().removeAll(camera);
                }
            }
        }
    }

    class CameraGroupHolder extends ExpandableViewHolder {

        public TextView mName;
        public ImageButton mSelector;
        public RelativeLayout mContainer;
        public View mDivider;

        public CameraGroupHolder(View view, FlexibleAdapter adapter) {
            super(view, adapter, false);//True for sticky
            mName = (TextView) view.findViewById(R.id.security_group_camera_name);
            mSelector = (ImageButton) view.findViewById(R.id.security_group_arrow);
            mContainer = (RelativeLayout) view.findViewById(R.id.security_group_camera_container);
            mDivider = view.findViewById(R.id.security_group_camera_divider);
        }

        @Override
        protected boolean isViewExpandableOnClick() {
            return true;//true by default
        }

        @Override
        protected void expandView(int position) {
            super.expandView(position);
            if (mAdapter.isExpanded(position)) mAdapter.notifyItemChanged(position, true);
        }

        @Override
        protected void collapseView(int position) {
            super.collapseView(position);
            if (!mAdapter.isExpanded(position)) mAdapter.notifyItemChanged(position, true);
        }

    }

    @Override
    public String toString() {
        return "ListFloorGroupItem[" + super.toString() + "//SubItems" + mListChildItems + "]";
    }

}