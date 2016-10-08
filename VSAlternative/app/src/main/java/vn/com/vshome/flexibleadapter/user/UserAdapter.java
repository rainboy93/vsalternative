package vn.com.vshome.flexibleadapter.user;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.daimajia.swipe.SwipeLayout;
import com.daimajia.swipe.adapters.RecyclerSwipeAdapter;

import java.util.ArrayList;

import in.workarounds.typography.TextView;
import vn.com.vshome.R;
import vn.com.vshome.VSHome;
import vn.com.vshome.communication.SocketManager;
import vn.com.vshome.database.User;
import vn.com.vshome.networks.CommandMessage;
import vn.com.vshome.utils.Define;
import vn.com.vshome.utils.MiscUtils;
import vn.com.vshome.utils.TimeOutManager;
import vn.com.vshome.view.ProgressHUD;

public class UserAdapter extends RecyclerSwipeAdapter<UserAdapter.UserViewHolder> implements TimeOutManager.TimeOutCallback {

    @Override
    public void onTimeOut() {
        ProgressHUD.hideLoading((Activity) mContext);
    }

    private int currentUserControl = -1;

    class UserViewHolder extends RecyclerView.ViewHolder {
            SwipeLayout swipeLayout;
            TextView userName;
            ImageButton buttonControl;

        public ImageButton userEdit;
        public ImageButton userDelete;

        public UserViewHolder(View itemView) {
            super(itemView);
            swipeLayout = (SwipeLayout) itemView.findViewById(R.id.user_swipe);

            userName = (TextView) itemView.findViewById(R.id.user_name);
            buttonControl = (ImageButton) itemView.findViewById(R.id.user_button_control);

            userEdit = (ImageButton) itemView.findViewById(R.id.user_button_edit);
            userDelete = (ImageButton) itemView.findViewById(R.id.user_button_delete);
        }
    }

    private Context mContext;
    private ArrayList<User> mDataset;

    //protected SwipeItemRecyclerMangerImpl mItemManger = new SwipeItemRecyclerMangerImpl(this)

    public UserAdapter(Context context, ArrayList<User> objects) {
        this.mContext = context;
        this.mDataset = objects;
    }

    public void updateData(ArrayList<User> objects) {
        this.mDataset = objects;
    }

    @Override
    public UserViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_item, parent, false);
        return new UserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final UserViewHolder viewHolder, final int position) {
        final User user = User.findById(User.class, mDataset.get(position).getId());
        viewHolder.swipeLayout.setShowMode(SwipeLayout.ShowMode.PullOut);
        viewHolder.userName.setText(user.username);
        mItemManger.bindView(viewHolder.itemView, position);
        if (user.priority == Define.PRIORITY_ADMIN
                || (VSHome.currentUser != null && VSHome.currentUser.priority == Define.PRIORITY_USER)) {
            viewHolder.buttonControl.setEnabled(false);
            viewHolder.buttonControl.setAlpha(0.5f);
            viewHolder.userDelete.setVisibility(View.GONE);
        } else {
            viewHolder.buttonControl.setEnabled(true);
            viewHolder.buttonControl.setAlpha(1.0f);
            viewHolder.userDelete.setVisibility(View.VISIBLE);
        }

        if (user.status == Define.USER_STATUS_ENABLE) {
            viewHolder.buttonControl.setSelected(true);
        } else {
            viewHolder.buttonControl.setSelected(false);
        }

        viewHolder.buttonControl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SocketManager.getInstance().receiveThread.currentUserId = user.getId().intValue();
                currentUserControl = position;
                CommandMessage changeStatus = new CommandMessage();
                changeStatus.setChangeUserStatus(user);
                ProgressHUD.showLoading((Activity) mContext);
                TimeOutManager.getInstance().startCountDown(UserAdapter.this, 5);
                SocketManager.getInstance().sendMessage(changeStatus);
            }
        });

        viewHolder.userDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (callback != null) {
                    callback.onDeleteUser(user, position);
                }
            }
        });

        viewHolder.userEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (callback != null) {
                    callback.onEditUser(user, position);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    @Override
    public int getSwipeLayoutResourceId(int position) {
        return R.id.user_swipe;
    }

    public void updateUserRow() {
        if (currentUserControl >= 0 && currentUserControl < mDataset.size()) {
            MiscUtils.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    notifyItemChanged(currentUserControl);
                    currentUserControl = -1;
                }
            });
        }
    }

    public void setUserControlCallback(UserControlCallback callback) {
        this.callback = callback;
    }

    private UserControlCallback callback;

    public interface UserControlCallback {
        void onEditUser(User user, int position);

        void onDeleteUser(User user, int position);
    }
}
