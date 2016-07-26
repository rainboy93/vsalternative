package vn.com.vshome.lightingcontrol;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.daimajia.swipe.util.Attributes;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import in.workarounds.typography.Button;
import in.workarounds.typography.TextView;
import vn.com.vshome.R;
import vn.com.vshome.VSHome;
import vn.com.vshome.callback.SceneActionCallback;
import vn.com.vshome.callback.SceneControlCallback;
import vn.com.vshome.database.Scene;
import vn.com.vshome.flexibleadapter.lightingscene.SceneAdapter;
import vn.com.vshome.networks.CommandMessage;
import vn.com.vshome.utils.Logger;
import vn.com.vshome.utils.TimeOutManager;
import vn.com.vshome.utils.Utils;
import vn.com.vshome.view.ProgressHUD;

/**
 * Created by anlab on 7/7/16.
 */
public class SceneFragment extends BaseControlFragment implements SceneActionCallback, SceneControlCallback {

    private static final String ARG_FLOOR_ID = "FloorID";
    private static final String ARG_ROOM_ID = "RoomID";

    private FloatingActionButton mAdd;
    private RecyclerView mRecyclerView;
    private SceneAdapter mAdapter;
    private ArrayList<Scene> mListScene;
    private LinearLayout mEmptyLayout;

    @Override
    public void onResume() {
        super.onResume();
        try {
            VSHome.socketManager.receiveThread.setSceneCallback(this);
        } catch (Exception e) {

        }
    }

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

    private void initView(View v) {
        mEmptyLayout = (LinearLayout) v.findViewById(R.id.lighting_control_scene_empty_layout);

        floorId = getArguments().getLong(ARG_FLOOR_ID);
        roomId = getArguments().getLong(ARG_ROOM_ID);

        resetData(floorId, roomId);

        mAdd = (FloatingActionButton) v.findViewById(R.id.lighting_control_scene_button_add);
        mAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), LightingSceneActivity.class);
                intent.putExtra("RoomId", roomId);
                startActivityForResult(intent, 1000);
            }
        });

        mRecyclerView = (RecyclerView) v.findViewById(R.id.lighting_control_scene_recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mAdapter = new SceneAdapter(getActivity(), mListScene, this);
        mAdapter.setMode(Attributes.Mode.Single);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (dy > 0)
                    mAdd.hide();
                else if (dy < 0)
                    mAdd.show();
            }
        });
    }

    @Override
    public void resetData(long floorID, long roomID) {
        Logger.LogD("Scene " + floorID + " " + roomID);
        this.floorId = floorID;
        this.roomId = roomID;

        mListScene = new ArrayList<>();

        List<Scene> scenes = Scene.listAll(Scene.class, "id");
        if (scenes != null && scenes.size() > 0) {
            mListScene.addAll(scenes);
            mEmptyLayout.setVisibility(View.GONE);
        } else {
            mEmptyLayout.setVisibility(View.VISIBLE);
        }

        if (mAdapter != null) {
            mAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_CANCELED) {
            return;
        }
        switch (requestCode) {
            case 1000:
                break;
            case 1001:
                break;
            default:
                break;
        }
    }

    @Override
    public void onActive(int position, boolean turnOn) {

    }

    @Override
    public void onScheduleChange(int position) {
        CommandMessage sceneScheduleUpdate = new CommandMessage();
        sceneScheduleUpdate.setSceneSchedule(mListScene.get(position));
    }

    @Override
    public void onEdit(int position) {
        mAdapter.closeAllItems();
        Intent intent = new Intent(getActivity(), LightingSceneActivity.class);
        intent.putExtra("SceneId", mListScene.get(position).getId());
        intent.putExtra("RoomId", roomId);
        startActivityForResult(intent, 1001);
    }

    @Override
    public void onDelete(int position) {
        Scene scene = mListScene.get(position);
        mAdapter.closeAllItems();
        showConfirmDeleteDialog(scene);
    }

    @Override
    public void onControl(int id, int mode) {
        mAdd.show();
        switch (mode) {
            case 1:
                Scene sceneAdd = Scene.findById(Scene.class, id);
                if (sceneAdd != null) {
                    mListScene.add(sceneAdd);
                    mAdapter.updateData(mListScene);
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mAdapter.notifyItemInserted(mListScene.size() - 2);
                            if (mEmptyLayout.getVisibility() == View.VISIBLE) {
                                mEmptyLayout.setVisibility(View.GONE);
                            }
                        }
                    });
                }
                break;
            case 2:
                for (int i = 0; i < mListScene.size(); i++) {
                    final int finalI = i;
                    Scene sceneDelete = mListScene.get(i);
                    if (sceneDelete.getId().intValue() == id) {
                        mListScene.remove(sceneDelete);
                        mAdapter.updateData(mListScene);
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                mAdapter.notifyItemRemoved(finalI);
                                if (mListScene.size() == 0 && mEmptyLayout.getVisibility() == View.GONE) {
                                    mEmptyLayout.setVisibility(View.VISIBLE);
                                }
                            }
                        });
                        break;
                    }
                }
                break;
            case 3:
                Scene sceneEdit = Scene.findById(Scene.class, id);
                if (sceneEdit != null) {
                    for (int i = 0; i < mListScene.size(); i++) {
                        final int finalI = i;
                        if (mListScene.get(i).getId().intValue() == id) {
                            mListScene.set(i, sceneEdit);
                            mAdapter.updateData(mListScene);
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    mAdapter.notifyItemChanged(finalI);
                                }
                            });
                            break;
                        }
                    }
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void onResponse(int status) {
        mAdd.show();
        switch (status) {
            case CommandMessage.CMD_SCENE_DELETE:
                ProgressHUD.hideLoading(getActivity());
                TimeOutManager.getInstance().cancelCountDown();
                if (status == CommandMessage.STATUS_ERROR) {
                    Utils.showErrorDialog(R.string.txt_error, R.string.txt_delete_scene_fail, getActivity());
                }
                break;
            default:
                break;
        }
    }

    private void showConfirmDeleteDialog(final Scene scene) {
        final Dialog dialog = new Dialog(getActivity());
        dialog.setCancelable(true);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.view_dialog_delete_scene);

        ImageButton buttonCancel = (ImageButton) dialog.findViewById(R.id.dialog_delete_scene_button_cancel);
        Button buttonConfirm = (Button) dialog.findViewById(R.id.dialog_delete_scene_button_confirm);
        TextView content = (TextView) dialog.findViewById(R.id.dialog_delete_scene_text);
        String s = "Bạn muốn xóa <b>" + scene.name.toUpperCase(Locale.US) + "</b>?";
        content.setText(Html.fromHtml(s));

        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });

        buttonConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
                CommandMessage deleteScene = new CommandMessage();
                deleteScene.setDeleteScene(scene.getId().intValue());
                ProgressHUD.showLoading(getActivity());
                TimeOutManager.getInstance().startCountDown(new TimeOutManager.TimeOutCallback() {
                    @Override
                    public void onTimeOut() {
                        ProgressHUD.hideLoading(getActivity());
                        Utils.showErrorDialog("Lỗi", "Có lỗi xảy ra. Hãy thử lại.", getActivity());
                    }
                }, 3);
                VSHome.socketManager.sendMessage(deleteScene);
            }
        });

        dialog.show();
    }
}
