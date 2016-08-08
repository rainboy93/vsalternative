package vn.com.vshome.security;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.common.SmoothScrollLinearLayoutManager;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import in.workarounds.typography.Button;
import in.workarounds.typography.EditText;
import vn.com.vshome.R;
import vn.com.vshome.database.Camera;
import vn.com.vshome.database.DatabaseService;
import vn.com.vshome.flexibleadapter.BaseAdapter;
import vn.com.vshome.flexibleadapter.ControlLayoutManager;
import vn.com.vshome.flexibleadapter.lightingcontrol.ControlGroupItem;
import vn.com.vshome.flexibleadapter.security.CameraGroupItem;
import vn.com.vshome.utils.PreferenceDefine;
import vn.com.vshome.utils.PreferenceUtils;
import vn.com.vshome.utils.Utils;

/**
 * Created by anlab on 7/4/16.
 */
public class SecurityFragment extends Fragment implements View.OnKeyListener {
    private static final String ARG_SECTION_NUMBER = "section_number";

    private RecyclerView mRecyclerView;
    private BaseAdapter mAdapter;
    private List<AbstractFlexibleItem> listCamera;

    public SecurityFragment() {
    }

    public static SecurityFragment newInstance(int sectionNumber) {
        SecurityFragment fragment = new SecurityFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.security_fragment, container, false);

        initView(rootView);

        return rootView;
    }

    private void initView(View view) {

//        for (int i = 0; i < 3; i++) {
//            Camera camera = new Camera();
//            camera.username = "test";
//            camera.password = "test";
//            camera.ipAddress = "asdfasdf";
//            camera.dnsAddress = "asdfasdfasdf";
//            camera.localPort = 1324;
//            camera.webPort = 1241;
//            camera.deviceType = 2;
//            camera.streamType = 1;
//            camera.deviceName = "dddd";
//            camera.roomId = 1;
//            camera.save();
//        }
        mRecyclerView = (RecyclerView) view.findViewById(R.id.security_list_camera);
        listCamera = DatabaseService.getListCamera();
        mAdapter = new BaseAdapter(listCamera);
        mAdapter.setDisplayHeadersAtStartUp(true)
                .setAutoCollapseOnExpand(true)
                .setAutoScrollOnExpand(true)
                .setRemoveOrphanHeaders(false)
                .setAnimationOnScrolling(true)
                .setAnimationOnReverseScrolling(true);
        mRecyclerView.setLayoutManager(new SmoothScrollLinearLayoutManager(getActivity()));
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator() {
            @Override
            public boolean canReuseUpdatedViewHolder(RecyclerView.ViewHolder viewHolder) {
                return true;
            }
        });
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        getView().setFocusableInTouchMode(true);
        getView().requestFocus();
        getView().setOnKeyListener(this);
    }

    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            mAdapter.collapseAll();
            mAdapter.notifyDataSetChanged();
            getActivity().onBackPressed();
            return true;
        } else {
            return false;
        }
    }
}
