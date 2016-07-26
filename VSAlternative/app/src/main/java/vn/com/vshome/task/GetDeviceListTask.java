package vn.com.vshome.task;

import android.os.AsyncTask;

import java.util.List;

import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import vn.com.vshome.callback.DeviceSelectCallback;
import vn.com.vshome.callback.TaskCallback;
import vn.com.vshome.database.DatabaseService;

/**
 * Created by anlab on 7/15/16.
 */
public class GetDeviceListTask extends AsyncTask<Boolean, String, String>{

    private boolean isControl = true;
    private List<AbstractFlexibleItem> items;
    private int roomId;
    private TaskCallback callback;
    private DeviceSelectCallback selectCallback;
    private List<AbstractFlexibleItem> previousList;
    private int sceneId = -1;

    public List<AbstractFlexibleItem> getListDevice(){
        return items;
    }

    public GetDeviceListTask(int roomId, TaskCallback callback, List<AbstractFlexibleItem> previousList){
        this.roomId = roomId;
        this.callback = callback;
        this.previousList = previousList;
    }

    public GetDeviceListTask(int roomId, TaskCallback callback, DeviceSelectCallback selectCallback,
                             int sceneId){
        this.roomId = roomId;
        this.callback = callback;
        this.selectCallback = selectCallback;
        this.sceneId = sceneId;
    }

    public GetDeviceListTask(int roomId, TaskCallback callback, DeviceSelectCallback selectCallback){
        this.roomId = roomId;
        this.callback = callback;
        this.selectCallback = selectCallback;
    }

    @Override
    protected String doInBackground(Boolean... params) {
        isControl = params[0];

        items = DatabaseService.getListDeviceItem(roomId, isControl, selectCallback, previousList, sceneId);

        return null;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        if(callback != null){
            callback.onTaskComplete();
        }
    }
}
