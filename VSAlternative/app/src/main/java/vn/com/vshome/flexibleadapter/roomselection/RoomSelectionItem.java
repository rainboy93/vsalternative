package vn.com.vshome.flexibleadapter.roomselection;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.github.siyamed.shapeimageview.mask.PorterShapeImageView;

import net.the4thdimension.android.Utils;

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
import vn.com.vshome.database.Room;
import vn.com.vshome.flexibleadapter.AbstractModelItem;
import vn.com.vshome.utils.Define;

public class RoomSelectionItem extends AbstractModelItem<RoomSelectionItem.RoomSelectionViewHolder>
        implements ISectionable<RoomSelectionItem.RoomSelectionViewHolder, IHeader>, IFilterable {

    private static final long serialVersionUID = 2519281529221244210L;

    /**
     * The header of this item
     */
    IHeader header;
    public Room mRoom;
    private int position;
    private RoomSelectionCallback callback;

    public RoomSelectionItem(int id, Room room, RoomSelectionCallback callback) {
        super(id + "");
        this.position = id;
        this.mRoom = room;
        this.callback = callback;
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
        return R.layout.room_selection_item;
    }

    @Override
    public RoomSelectionViewHolder createViewHolder(FlexibleAdapter adapter, LayoutInflater inflater, ViewGroup parent) {
        return new RoomSelectionViewHolder(inflater.inflate(getLayoutRes(), parent, false), adapter);
    }

    @SuppressWarnings("deprecation")
    @Override
    public void bindViewHolder(FlexibleAdapter adapter, RoomSelectionViewHolder holder, final int position, List payloads) {
        holder.mRoomName.setText(mRoom.name);
        String path = Define.BASE_IMAGE_PATH + File.separator + mRoom.getId().intValue()
                + File.separator + "Image.png";
        Glide.with(VSHome.activity)
                .load(path)
                .fitCenter()
                .placeholder(R.drawable.no_image_place_holder)
                .crossFade()
                .skipMemoryCache(true)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .into(holder.mRoomImage);
        holder.mCapture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(callback != null){
                    callback.onCapture(position, mRoom);
                }
            }
        });

        holder.mRoomImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(callback != null){
                    callback.onSelect(mRoom);
                }
            }
        });
    }

    @Override
    public boolean filter(String constraint) {
        return getTitle() != null && getTitle().toLowerCase().trim().contains(constraint);
    }

    class RoomSelectionViewHolder extends FlexibleViewHolder {

        public PorterShapeImageView mRoomImage;
        public TextView mRoomName;
        public ImageButton mCapture;

        public RoomSelectionViewHolder(View view, FlexibleAdapter adapter) {
            super(view, adapter);
            mRoomImage = (PorterShapeImageView) view.findViewById(R.id.room_selection_image);
            mRoomName = (TextView) view.findViewById(R.id.room_selection_name);
            mCapture = (ImageButton) view.findViewById(R.id.room_selection_capture);
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