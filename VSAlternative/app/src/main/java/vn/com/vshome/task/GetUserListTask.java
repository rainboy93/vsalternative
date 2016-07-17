package vn.com.vshome.task;

import android.os.AsyncTask;

import java.util.ArrayList;
import java.util.List;

import vn.com.vshome.callback.TaskCallback;
import vn.com.vshome.database.User;

/**
 * Created by anlab on 7/15/16.
 */
public class GetUserListTask extends AsyncTask<String, String, String>{

    private TaskCallback callback;
    private List<User> users;

    public List<User> getUsers(){
        if(users == null){
            users = new ArrayList<>();
        }
        return users;
    }

    public GetUserListTask(TaskCallback callback){
        this.callback = callback;
    }

    @Override
    protected String doInBackground(String... params) {
        users = User.listAll(User.class, "id");
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
