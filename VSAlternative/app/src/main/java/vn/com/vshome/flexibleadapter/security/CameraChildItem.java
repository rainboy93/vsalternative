package vn.com.vshome.flexibleadapter.security;

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
import vn.com.vshome.database.Camera;
import vn.com.vshome.flexibleadapter.AbstractItem;
import vn.com.vshome.utils.Utils;
import vn.com.vshome.view.customview.CameraControlView;
import vn.com.vshome.view.customview.CameraView;

public class CameraChildItem extends AbstractItem<CameraChildItem.CameraChildViewHolder>
        implements ISectionable<CameraChildItem.CameraChildViewHolder, IHeader>, IFilterable, View.OnClickListener {

    private static final long serialVersionUID = 2519284529221244210L;

    /**
     * The header of this item
     */
    IHeader header;

    public Camera camera;
    public boolean isError = false;
    public String errorMsg = "";

    public CameraChildItem(String id, Camera camera) {
        super(id);

        this.camera = camera;

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
        return R.layout.security_camera_child;
    }

    @Override
    public CameraChildViewHolder createViewHolder(FlexibleAdapter adapter, LayoutInflater inflater, ViewGroup parent) {
        return new CameraChildViewHolder(inflater.inflate(getLayoutRes(), parent, false), adapter);
    }

    @SuppressWarnings("deprecation")
    @Override
    public void bindViewHolder(final FlexibleAdapter adapter, CameraChildViewHolder holder, final int position, List payloads) {
        if (isError) {
            holder.cameraControlView.setVisibility(View.GONE);
            holder.mWarn.setVisibility(View.VISIBLE);
            holder.mWarn.setText(errorMsg);
        } else {
            holder.cameraControlView.setVisibility(View.VISIBLE);
            holder.cameraView.setCamera(camera);
            holder.cameraView.startDraw();
            holder.cameraControlView.setCamera(camera);
            holder.mWarn.setVisibility(View.GONE);
        }
    }

    @Override
    public boolean filter(String constraint) {
        return getTitle() != null && getTitle().toLowerCase().trim().contains(constraint);
    }

    @Override
    public void onClick(View v) {

    }

    class CameraChildViewHolder extends FlexibleViewHolder {

        public CameraView cameraView;
        public CameraControlView cameraControlView;
        public TextView mWarn;

        public CameraChildViewHolder(View view, FlexibleAdapter adapter) {
            super(view, adapter);
            cameraView = (CameraView) view.findViewById(R.id.security_child_camera_view);
            cameraView.getLayoutParams().height = Utils.getScreenWidth() * 3 / 4;
            cameraControlView = (CameraControlView) view.findViewById(R.id.security_child_camera_control_view);
            mWarn = (TextView) view.findViewById(R.id.security_child_camera_warn);
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