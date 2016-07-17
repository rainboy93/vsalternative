package vn.com.vshome.lightingcontrol;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.daimajia.swipe.util.Attributes;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import vn.com.vshome.R;
import vn.com.vshome.database.Scene;
import vn.com.vshome.flexibleadapter.lightingscene.SceneAdapter;
import vn.com.vshome.utils.Logger;

/**
 * Created by anlab on 7/7/16.
 */
public class SceneFragment extends BaseControlFragment{

    private static final String ARG_FLOOR_ID = "FloorID";
    private static final String ARG_ROOM_ID = "RoomID";

    private FloatingActionButton mAdd;
    private RecyclerView mRecyclerView;
    private SceneAdapter mAdapter;
    private ArrayList<Scene> mListScene;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.lighting_control_scene_fragment, container, false);
        initView(rootView);
        return rootView;
    }

    public static SceneFragment newInstance(long floorID, long roomID) {
        SceneFragment fragment = new SceneFragment();
        Bundle args = new Bundle();
        args.putLong(ARG_FLOOR_ID, floorID);
        args.putLong(ARG_ROOM_ID, roomID);
        fragment.setArguments(args);
        return fragment;
    }

    private void initView(View v){
        resetData(floorId, roomId);

        mAdd = (FloatingActionButton) v.findViewById(R.id.lighting_control_scene_button_add);
        mAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), LightingSceneActivity.class);
                getActivity().startActivityForResult(intent, 1000);
            }
        });

        mRecyclerView = (RecyclerView) v.findViewById(R.id.lighting_control_scene_recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mAdapter = new SceneAdapter(getActivity(), mListScene);
        mAdapter.setMode(Attributes.Mode.Single);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener(){
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy){
                if (dy > 0)
                    mAdd.hide();
                else if (dy < 0)
                    mAdd.show();
            }
        });
    }

    @Override
    public void resetData(long floorID, long roomID) {
        Logger.LogD("Scene " +  floorID + " " + roomID);
        this.floorId = floorID;
        this.roomId = roomID;

        mListScene = new ArrayList<>();

        List<Scene> scenes = Scene.listAll(Scene.class, "id");
        if(scenes != null && scenes.size() > 0){
            mListScene.addAll(scenes);
        }

        if(mAdapter != null){
            mAdapter.notifyDataSetChanged();
        }
    }
}
