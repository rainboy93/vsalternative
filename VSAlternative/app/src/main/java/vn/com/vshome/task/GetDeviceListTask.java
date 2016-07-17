package vn.com.vshome.task;

import android.os.AsyncTask;

import java.util.List;

import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
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

    public List<AbstractFlexibleItem> getListDevice(){
        return items;
    }

    public GetDeviceListTask(int roomId, TaskCallback callback){
        this.roomId = roomId;
        this.callback = callback;
    }

    @Override
    protected String doInBackground(Boolean... params) {
        isControl = params[0];

        items = DatabaseService.getListDeviceItem(roomId, isControl);

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
