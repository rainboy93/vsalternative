package vn.com.vshome.user;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import java.util.ArrayList;
import java.util.List;

import vn.com.vshome.R;
import vn.com.vshome.VSHome;
import vn.com.vshome.callback.UserControlCallback;
import vn.com.vshome.database.User;
import vn.com.vshome.flexibleadapter.user.UserAdapter;
import vn.com.vshome.networks.CommandMessage;
import vn.com.vshome.utils.Define;
import vn.com.vshome.utils.TimeOutManager;
import vn.com.vshome.utils.Toaster;
import vn.com.vshome.view.ProgressHUD;

/**
 * Created by anlab on 7/4/16.
 */
public class UserFragment extends Fragment implements View.OnClickListener, UserAdapter.UserControlCallback,
        UserControlCallback, TimeOutManager.TimeOutCallback {
    private static final String ARG_SECTION_NUMBER = "section_number";

    private RecyclerView mRecyclerView;
    private FloatingActionButton mAdd;
    private UserAdapter mAdapter;
    private ArrayList<User> mListUser;
    private ImageButton mShutDown;

    public UserFragment() {
    }

    public static UserFragment newInstance(int sectionNumber) {
        UserFragment fragment = new UserFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.user_fragment, container, false);
        initView(rootView);
        return rootView;
    }

    private void initView(View view) {
        mRecyclerView = (RecyclerView) view.findViewById(R.id.user_recycler_view);
        ((SimpleItemAnimator) mRecyclerView.getItemAnimator()).setSupportsChangeAnimations(false);

        mAdd = (FloatingActionButton) view.findViewById(R.id.user_button_add);
        mAdd.setOnClickListener(this);

        mShutDown = (ImageButton) view.findViewById(R.id.user_button_shut_down);
        mShutDown.setOnClickListener(this);

        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (dy > 0)
                    mAdd.hide();
                else if (dy < 0)
                    mAdd.show();
            }
        });

        List<User> list = User.listAll(User.class, "id");
        mListUser = new ArrayList<>();
        if (list != null && list.size() > 0) {
            mListUser.addAll(list);
        }
        mAdapter = new UserAdapter(getActivity(), mListUser);
        mAdapter.setUserControlCallback(this);
        VSHome.socketManager.receiveThread.setUserCallback(this);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public void onClick(View v) {
        if (v == mAdd) {
            ((UserActivity) getActivity()).setFragment(1);
        } else if(v == mShutDown){
            if(VSHome.currentUser.priority == Define.PRIORITY_ADMIN){
                CommandMessage shutDown = new CommandMessage(CommandMessage.CMD_DISCONNECT_ALL_USER);
                ProgressHUD.showLoading(getActivity());
                TimeOutManager.getInstance().startCountDown(this, 5);
                VSHome.socketManager.sendMessage(shutDown);
            } else {
                VSHome.socketManager.destroySocket();
                VSHome.restart();
            }
        }
    }

    public void updateUserRow() {
        if (mAdapter != null) {
            mAdapter.updateUserRow();
        }
    }

    private int currentPosition = -1;

    @Override
    public void onStop() {
        super.onStop();
        try {
            VSHome.socketManager.receiveThread.setUserCallback(null);
        } catch (Exception e){

        }
    }

    @Override
    public void onEditUser(User user, int position) {
        ((UserActivity)getActivity()).mEditUser = user;
        currentPosition = position;
        ((UserActivity)getActivity()).setFragment(1);
    }

    @Override
    public void onDeleteUser(User user, int position) {
        ((UserActivity)getActivity()).mEditUser = user;
        currentPosition = position;
    }

    @Override
    public void onControl(int id) {

    }

    @Override
    public void onResponse(int cmd, int status) {
        ProgressHUD.hideLoading(getActivity());
        TimeOutManager.getInstance().cancelCountDown();
        if (status == CommandMessage.STATUS_ERROR) {
            return;
        }
        switch (cmd) {
            case CommandMessage.CMD_ADD_NEW_USER:
                ((UserActivity) getActivity()).mNewUser.save();
                List<User> list = User.listAll(User.class, "id");
                mListUser = new ArrayList<>();
                if (list != null && list.size() > 0) {
                    mListUser.addAll(list);
                }
                if (mAdapter != null) {
                    mAdapter.updateData(mListUser);
                    mAdapter.notifyItemChanged(mListUser.size() - 2);
                }
                ((UserActivity) getActivity()).mNewUser = null;
                break;
            case CommandMessage.CMD_UPDATE_USER_PASSWORD:
            case CommandMessage.CMD_UPDATE_USER_PRIORITY:
            case CommandMessage.CMD_UPDATE_USER_STATUS:
            case CommandMessage.CMD_UPDATE_USER_ROOM:
                ((UserActivity) getActivity()).mEditUser.save();
                if (mAdapter != null && currentPosition != -1) {
                    mAdapter.notifyItemChanged(currentPosition);
                }
                currentPosition = -1;
                ((UserActivity) getActivity()).mEditUser = null;
                break;
            case CommandMessage.CMD_DELETE_USER:
                ((UserActivity) getActivity()).mEditUser.delete();
                if(mAdapter != null && currentPosition != -1){
                    mAdapter.notifyItemRemoved(currentPosition);
                }
                currentPosition = -1;
                ((UserActivity) getActivity()).mEditUser = null;
                break;

            default:
                break;
        }
    }

    @Override
    public void onGetUserListDone() {

    }

    @Override
    public void onTimeOut() {
        ProgressHUD.hideLoading(getActivity());
        Toaster.showMessage(getActivity(), "Có lỗi xảy ra. Hãy thử lại.");
    }
}