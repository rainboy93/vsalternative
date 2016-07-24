package vn.com.vshome.user;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import java.util.ArrayList;
import java.util.List;

import eu.davidea.flexibleadapter.common.SmoothScrollLinearLayoutManager;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import in.workarounds.typography.EditText;
import vn.com.vshome.R;
import vn.com.vshome.VSHome;
import vn.com.vshome.database.DatabaseService;
import vn.com.vshome.database.Room;
import vn.com.vshome.database.User;
import vn.com.vshome.flexibleadapter.BaseAdapter;
import vn.com.vshome.flexibleadapter.user.RoomItem;
import vn.com.vshome.networks.CommandMessage;
import vn.com.vshome.utils.Define;
import vn.com.vshome.utils.TimeOutManager;
import vn.com.vshome.utils.Toaster;
import vn.com.vshome.view.ProgressHUD;

/**
 * Created by anlab on 7/4/16.
 */
public class UserActionFragment extends Fragment implements View.OnClickListener {
    private static final String ARG_SECTION_NUMBER = "section_number";

    private List<AbstractFlexibleItem> mListRoom;
    private RecyclerView mRecyclerView;
    private BaseAdapter mAdapter;
    private ImageButton mSave;
    private EditText mUsername, mPassword;

    public UserActionFragment() {
    }

    public static UserActionFragment newInstance(int sectionNumber) {
        UserActionFragment fragment = new UserActionFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.user_action_fragment, container, false);
        initView(rootView);
        return rootView;
    }

    private void initView(View view) {
        mRecyclerView = (RecyclerView) view.findViewById(R.id.user_list_room);
        mListRoom = DatabaseService.getListRoomAddUser();
        mAdapter = new BaseAdapter(mListRoom);
        mRecyclerView.setLayoutManager(new SmoothScrollLinearLayoutManager(getActivity()));
        mRecyclerView.setHasFixedSize(true);
        ((SimpleItemAnimator) mRecyclerView.getItemAnimator()).setSupportsChangeAnimations(false);
        mRecyclerView.setAdapter(mAdapter);

        mSave = (ImageButton) view.findViewById(R.id.user_button_save);
        mSave.setOnClickListener(this);

        mUsername = (EditText) view.findViewById(R.id.user_username);
        mPassword = (EditText) view.findViewById(R.id.user_password);
    }

    @Override
    public void onClick(View v) {
        if (v == mSave) {
            CommandMessage newUser = new CommandMessage();
            User user = new User();
            user.username = mUsername.getText().toString();
            user.password = mPassword.getText().toString();
            user.priority = Define.PRIORITY_USER;
            newUser.setCreateUser(user, getRoomCode());
            user.roomControl = roomString.toString();
            ((UserActivity)getActivity()).mNewUser = user;
            ProgressHUD.showLoading(getActivity());
            TimeOutManager.getInstance().startCountDown(new TimeOutManager.TimeOutCallback() {
                @Override
                public void onTimeOut() {
                    ProgressHUD.hideLoading(getActivity());
                    Toaster.showMessage(getActivity(), "asdf");
                }
            }, 5);
            VSHome.socketManager.sendMessage(newUser);
        }
    }

    private StringBuilder roomString;

    private int[] getRoomCode() {
        roomString = new StringBuilder();
        int[] result = new int[10];
        int[] binary = new int[80];
        for (int i = 0; i < mListRoom.size(); i++) {
            RoomItem item = (RoomItem) mListRoom.get(i);
            if (item.isSelected) {
                binary[item.room.getId().intValue() - 1] = 1;
            }
        }

        for (int i = 0; i < 10; i++) {
            StringBuilder str = new StringBuilder();
            for (int j = 0; j < 8; j++) {
                str.append(binary[i * 8 + j] + "");
                roomString.append(binary[i * 8 + j] + "");
            }
            int decimal = Integer.parseInt(str.toString(), 2);
            result[i] = decimal;
        }
        return result;
    }
}