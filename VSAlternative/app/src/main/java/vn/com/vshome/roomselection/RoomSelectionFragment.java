package vn.com.vshome.roomselection;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;

import java.util.ArrayList;
import java.util.List;

import eu.davidea.flexibleadapter.common.SmoothScrollLinearLayoutManager;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import gun0912.tedbottompicker.TedBottomPicker;
import vn.com.vshome.CropActivity;
import vn.com.vshome.R;
import vn.com.vshome.VSHome;
import vn.com.vshome.callback.RoomSelectionCallback;
import vn.com.vshome.database.Room;
import vn.com.vshome.flexibleadapter.BaseAdapter;
import vn.com.vshome.flexibleadapter.roomselection.RoomSelectionItem;
import vn.com.vshome.lightingcontrol.LightingControlActivity;
import vn.com.vshome.utils.Define;
import vn.com.vshome.utils.Utils;

/**
 * Created by anlab on 7/22/16.
 */
public class RoomSelectionFragment extends Fragment implements RoomSelectionCallback {
    private static final String ARG_SECTION_NUMBER = "room_id";

    private ArrayList<AbstractFlexibleItem> mListRoom;
    private RecyclerView mRecyclerView;
    private BaseAdapter mAdapter;
    private PermissionListener permissionListener;
    private TedPermission tedPermission;
    private TedBottomPicker imagePicker;

    public RoomSelectionFragment() {
    }

    public static RoomSelectionFragment newInstance(int sectionNumber) {
        RoomSelectionFragment fragment = new RoomSelectionFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_room_selection, container, false);
        initView(rootView);
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    private void initView(View view) {
        mRecyclerView = (RecyclerView) view.findViewById(R.id.room_recycler_view);

        int id = getArguments().getInt(ARG_SECTION_NUMBER, 0);
        List<Room> list = Room.find(Room.class, "floor_id = ?", new String[]{id + ""}, null, "id", null);
        mListRoom = new ArrayList<>();
        if (list != null && list.size() > 0) {
            for (int i = 0; i < list.size(); i++) {
                Room room = list.get(i);
                RoomSelectionItem item = new RoomSelectionItem(i, room, this);
                mListRoom.add(item);
            }
        }
        mAdapter = new BaseAdapter(mListRoom);
        mRecyclerView.setLayoutManager(new SmoothScrollLinearLayoutManager(getActivity()));
        ((SimpleItemAnimator) mRecyclerView.getItemAnimator()).setSupportsChangeAnimations(false);
        mRecyclerView.setAdapter(mAdapter);

        permissionListener = new PermissionListener() {
            @Override
            public void onPermissionGranted() {
                imagePicker = new TedBottomPicker.Builder(getActivity())
                        .setOnImageSelectedListener(new TedBottomPicker.OnImageSelectedListener() {
                            @Override
                            public void onImageSelected(Uri uri) {
                                VSHome.isTakePhoto = false;
                                selectedUri = uri;
                                goToCrop();
                            }
                        })
                        .setOnErrorListener(new TedBottomPicker.OnErrorListener() {
                            @Override
                            public void onError(String message) {
                                VSHome.isTakePhoto = false;
                            }
                        })
                        .setPeekHeight(getResources().getDisplayMetrics().heightPixels / 2)
                        .create();
                if (imagePicker != null) {
                    imagePicker.show(getActivity().getSupportFragmentManager());
                }
            }

            @Override
            public void onPermissionDenied(ArrayList<String> deniedPermissions) {
                VSHome.isTakePhoto = false;
            }
        };
    }

    @Override
    public void onSelect(Room room) {
        if (VSHome.currentUser != null && VSHome.currentUser.priority != Define.PRIORITY_ADMIN
                && VSHome.currentUser.roomControl.charAt(room.getId().intValue() - 1) != '1') {
            Utils.showErrorDialog(R.string.txt_error, R.string.txt_room_priority, getActivity());
            return;
        }

        Intent intent = new Intent(getActivity(), LightingControlActivity.class);
        intent.putExtra(Define.INTENT_ROOM_ID, room.getId());
        intent.putExtra(Define.INTENT_FLOOR_ID, (long) room.floorID);
        startActivity(intent);
        getActivity().finish();
    }

    @Override
    public void onCapture(int position, Room room) {
        editPosition = position;
        currentRoom = room;
        VSHome.isTakePhoto = true;
        tedPermission = new TedPermission(getActivity())
                .setPermissionListener(permissionListener)
                .setDeniedMessage("If you reject permission,you can not use this service\n\nPlease turn on permissions at [Setting] > [Permission]")
                .setPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        tedPermission.check();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Define.CODE_CROP_PICTURE && resultCode == Activity.RESULT_OK) {
            if (editPosition >= 0 && editPosition < mAdapter.getItemCount()) {
                mAdapter.notifyItemChanged(editPosition);
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private int editPosition;
    private Room currentRoom;

    private Uri selectedUri;

    private void goToCrop() {
        if (selectedUri != null) {
            Intent intent = new Intent(getActivity(), CropActivity.class);
            intent.putExtra("URI", selectedUri.toString());
            intent.putExtra(Define.INTENT_FLOOR_ID, currentRoom.floorID);
            intent.putExtra(Define.INTENT_ROOM_ID, currentRoom.getId().intValue());
            startActivityForResult(intent, Define.CODE_CROP_PICTURE);
        }
    }
}
