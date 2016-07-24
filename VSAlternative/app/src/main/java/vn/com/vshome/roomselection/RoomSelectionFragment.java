package vn.com.vshome.roomselection;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import net.the4thdimension.android.ImageUtils;
import net.the4thdimension.android.ViewUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import eu.davidea.flexibleadapter.common.SmoothScrollLinearLayoutManager;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import in.workarounds.typography.TextView;
import vn.com.vshome.CropActivity;
import vn.com.vshome.R;
import vn.com.vshome.callback.RoomSelectionCallback;
import vn.com.vshome.database.Room;
import vn.com.vshome.flexibleadapter.BaseAdapter;
import vn.com.vshome.flexibleadapter.roomselection.RoomSelectionItem;
import vn.com.vshome.lightingcontrol.LightingControlActivity;
import vn.com.vshome.utils.Define;

/**
 * Created by anlab on 7/22/16.
 */
public class RoomSelectionFragment extends Fragment implements RoomSelectionCallback {
    private static final String ARG_SECTION_NUMBER = "room_id";

    private ArrayList<AbstractFlexibleItem> mListRoom;
    private RecyclerView mRecyclerView;
    private BaseAdapter mAdapter;

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
    }

    @Override
    public void onSelect(Room room) {
        Intent intent = new Intent(getActivity(), LightingControlActivity.class);
        intent.putExtra(Define.INTENT_ROOM_ID, room.getId().intValue());
        intent.putExtra(Define.INTENT_FLOOR_ID, room.floorID);
        startActivity(intent);
        getActivity().finish();
    }

    @Override
    public void onCapture(int position, Room room) {
        editPosition = position;
        currentRoom = room;
        showChooseActionDialog(room);
    }

    private int editPosition;
    private Room currentRoom;

    private Uri selectedUri;

    private void showChooseActionDialog(final Room room) {
        final Dialog dialog = new Dialog(getActivity());

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        dialog.setContentView(R.layout.dialog_choose_intent);

        TextView camera = (TextView) dialog
                .findViewById(R.id.dialog_choose_camera);
        TextView gallery = (TextView) dialog
                .findViewById(R.id.dialog_choose_gallery);
        camera.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                dialog.dismiss();
                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                selectedUri = Uri.fromFile(new File(Environment.getExternalStorageDirectory()
                        + File.separator + "Temp.png"));
                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, selectedUri);
                startActivityForResult(cameraIntent, Define.CODE_TAKE_PICTURE);
            }
        });
        gallery.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                dialog.dismiss();
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent,
                        "Select Picture"), Define.CODE_SELECT_PICTURE);
            }
        });

        dialog.show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == Define.CODE_CROP_PICTURE && resultCode == Activity.RESULT_OK) {
            if(mAdapter != null){
                mAdapter.notifyItemChanged(editPosition);
            }
        } else {
            if (requestCode == Define.CODE_SELECT_PICTURE && resultCode == Activity.RESULT_OK) {
                selectedUri = data.getData();
            } else if (requestCode == Define.CODE_TAKE_PICTURE && resultCode == Activity.RESULT_OK) {

            }
            if (selectedUri != null) {
                Intent intent = new Intent(getActivity(), CropActivity.class);
                intent.putExtra("URI", selectedUri.toString());
                intent.putExtra(Define.INTENT_FLOOR_ID, currentRoom.floorID);
                intent.putExtra(Define.INTENT_ROOM_ID, currentRoom.getId().intValue());
                startActivityForResult(intent, Define.CODE_CROP_PICTURE);
            }
        }
    }
}