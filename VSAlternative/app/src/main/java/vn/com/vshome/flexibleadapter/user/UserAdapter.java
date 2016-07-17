package vn.com.vshome.flexibleadapter.user;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.daimajia.swipe.SwipeLayout;
import com.daimajia.swipe.adapters.RecyclerSwipeAdapter;

import java.util.ArrayList;

import in.workarounds.typography.TextView;
import vn.com.vshome.R;
import vn.com.vshome.database.Scene;
import vn.com.vshome.database.User;
import vn.com.vshome.utils.Utils;
import vn.com.vshome.view.ButtonSceneControl;

public class UserAdapter extends RecyclerSwipeAdapter<UserAdapter.UserViewHolder> {

    class UserViewHolder extends RecyclerView.ViewHolder {
        SwipeLayout swipeLayout;
        TextView userName;
        ImageButton buttonControl;


        public ImageButton sceneEdit;
        public ImageButton sceneDelete;

        public UserViewHolder(View itemView) {
            super(itemView);
            swipeLayout = (SwipeLayout) itemView.findViewById(R.id.user_swipe);

            userName = (TextView) itemView.findViewById(R.id.user_name);
            buttonControl = (ImageButton) itemView.findViewById(R.id.user_button_control);

            sceneEdit = (ImageButton) itemView.findViewById(R.id.lighting_scene_button_edit);
            sceneDelete = (ImageButton) itemView.findViewById(R.id.lighting_scene_button_delete);
        }
    }

    private Context mContext;
    private ArrayList<User> mDataset;

    //protected SwipeItemRecyclerMangerImpl mItemManger = new SwipeItemRecyclerMangerImpl(this)

    public UserAdapter(Context context, ArrayList<User> objects) {
        this.mContext = context;
        this.mDataset = objects;
    }

    @Override
    public UserViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_item, parent, false);
        return new UserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final UserViewHolder viewHolder, final int position) {
        User user = mDataset.get(position);
        viewHolder.swipeLayout.setShowMode(SwipeLayout.ShowMode.PullOut);

        viewHolder.userName.setText(user.username);

        mItemManger.bindView(viewHolder.itemView, position);
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    @Override
    public int getSwipeLayoutResourceId(int position) {
        return R.id.user_swipe;
    }
}
