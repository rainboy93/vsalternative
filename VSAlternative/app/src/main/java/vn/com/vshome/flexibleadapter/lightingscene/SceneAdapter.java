package vn.com.vshome.flexibleadapter.lightingscene;

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
import vn.com.vshome.callback.SceneActionCallback;
import vn.com.vshome.database.Scene;
import vn.com.vshome.utils.Utils;
import vn.com.vshome.view.ButtonSceneControl;

public class SceneAdapter extends RecyclerSwipeAdapter<SceneAdapter.SceneViewHolder> {

    class SceneViewHolder extends RecyclerView.ViewHolder {
        SwipeLayout swipeLayout;
        public TextView sceneName;
        public TextView sceneSchedule;
        public TextView sceneDays;
        public ImageButton sceneToggle;
        public ImageView sceneClock;
        public ButtonSceneControl buttonOnOff;

        public ImageButton sceneEdit;
        public ImageButton sceneDelete;

        public SceneViewHolder(View itemView) {
            super(itemView);
            swipeLayout = (SwipeLayout) itemView.findViewById(R.id.swipe);
            sceneName = (TextView) itemView.findViewById(R.id.lighting_scene_name);
            sceneSchedule = (TextView) itemView.findViewById(R.id.lighting_scene_schedule);
            sceneDays = (TextView) itemView.findViewById(R.id.lighting_scene_active_day);
            sceneToggle = (ImageButton) itemView.findViewById(R.id.lighting_scene_button_active);
            sceneClock = (ImageView) itemView.findViewById(R.id.lighting_scene_clock_icon);
            buttonOnOff = (ButtonSceneControl) itemView.findViewById(R.id.lighting_scene_button_control);

            sceneEdit = (ImageButton) itemView.findViewById(R.id.lighting_scene_button_edit);
            sceneDelete = (ImageButton) itemView.findViewById(R.id.lighting_scene_button_delete);
        }
    }

    private Context mContext;
    private ArrayList<Scene> mDataset;

    //protected SwipeItemRecyclerMangerImpl mItemManger = new SwipeItemRecyclerMangerImpl(this)

    public SceneAdapter(Context context, ArrayList<Scene> objects, SceneActionCallback callback) {
        this.mContext = context;
        this.mDataset = objects;
        this.callback = callback;
    }

    @Override
    public SceneViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.scene_item, parent, false);
        return new SceneViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final SceneViewHolder viewHolder, final int position) {
        final Scene item = mDataset.get(position);
        viewHolder.swipeLayout.setShowMode(SwipeLayout.ShowMode.PullOut);

        viewHolder.sceneName.setText(item.name);
        viewHolder.sceneDays.setText(getScheduleDays(item));
        viewHolder.sceneSchedule.setText(getScheduleTime(item));

        if (item.schedule != Scene.SCHEDULE_OFF) {
            viewHolder.sceneToggle.setSelected(true);
            viewHolder.sceneSchedule.setTextColor(Utils
                    .getColor(R.color.schedule_on));
            viewHolder.sceneClock.setImageResource(R.drawable.light_clock_icon);
            viewHolder.sceneDays.setTextColor(Utils
                    .getColor(R.color.schedule_on));

        } else {
            viewHolder.sceneToggle.setSelected(false);
            viewHolder.sceneSchedule.setTextColor(Utils
                    .getColor(R.color.gray));
            viewHolder.sceneClock.setImageResource(R.drawable.gray_clock_icon);
            viewHolder.sceneDays.setTextColor(Utils
                    .getColor(R.color.gray));
        }

        viewHolder.sceneToggle.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (callback != null) {
                    callback.onScheduleChange(position);
                }
            }
        });

        viewHolder.sceneEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (callback != null) {
                    callback.onEdit(position);
                }
            }
        });

        viewHolder.sceneDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (callback != null) {
                    callback.onDelete(position);
                }
            }
        });

        viewHolder.buttonOnOff.setOnControlOnOffListener(new ButtonSceneControl.OnControlOnOffListener() {
            @Override
            public void OnControlOn() {
                if (callback != null) {
                    callback.onActive(position, true);
                }
            }

            @Override
            public void OnControlOff() {
                if (callback != null) {
                    callback.onActive(position, false);
                }
            }
        });

        mItemManger.bindView(viewHolder.itemView, position);
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    @Override
    public int getSwipeLayoutResourceId(int position) {
        return R.id.swipe;
    }

    private String getScheduleDays(Scene lightingScene) {
        String result = "T: ";
        result += lightingScene.monday == Scene.DAY_ON ? "2 " : "";
        result += lightingScene.tuesday == Scene.DAY_ON ? "3 "
                : "";
        result += lightingScene.wednesday == Scene.DAY_ON ? "4 "
                : "";
        result += lightingScene.thursday == Scene.DAY_ON ? "5 "
                : "";
        result += lightingScene.friday == Scene.DAY_ON ? "6 " : "";
        result += lightingScene.saturday == Scene.DAY_ON ? "7 "
                : "";
        result += lightingScene.sunday == Scene.DAY_ON ? "CN" : "";
        return result;
    }

    private String getScheduleTime(Scene lightingScene) {
        String result = "";
        int hour = lightingScene.hour;
        int h = hour;
        int minute = lightingScene.minute;
        if (hour > 12) {
            hour = hour - 12;
        }
        result += Utils.getTimeString(hour) + ":"
                + Utils.getTimeString(minute);
        result += (h >= 12) ? " PM" : " AM";
        return result;
    }

    public void updateData(ArrayList<Scene> listScene) {
        this.mDataset = listScene;
    }

    private SceneActionCallback callback;
}
