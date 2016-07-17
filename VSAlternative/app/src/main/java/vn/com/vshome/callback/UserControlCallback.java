package vn.com.vshome.callback;

/**
 * Created by anlab on 7/11/16.
 */
public interface UserControlCallback {
    void onControl(int id);
    void onResponse(int status);
    void onGetUserListDone();
}
