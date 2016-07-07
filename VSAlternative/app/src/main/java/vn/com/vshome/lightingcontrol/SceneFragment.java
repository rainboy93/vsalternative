package vn.com.vshome.lightingcontrol;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import vn.com.vshome.R;

/**
 * Created by anlab on 7/7/16.
 */
public class SceneFragment extends BaseControlFragment{

    private static final String ARG_FLOOR_ID = "FloorID";
    private static final String ARG_ROOM_ID = "RoomID";

    private FloatingActionButton mAdd;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.lighting_control_scene_fragment, container, false);
        initView(rootView);
        return rootView;
    }

    public static SceneFragment newInstance(int floorID, int roomID) {
        SceneFragment fragment = new SceneFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_FLOOR_ID, floorID);
        args.putInt(ARG_ROOM_ID, roomID);
        fragment.setArguments(args);
        return fragment;
    }

    private void initView(View v){
        mAdd = (FloatingActionButton) v.findViewById(R.id.lighting_control_scene_button_add);
        mAdd.setPadd
    }

    @Override
    public void freshData(int floorID, int roomID) {

    }
}
