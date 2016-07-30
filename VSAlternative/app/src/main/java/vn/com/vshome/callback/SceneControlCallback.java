package vn.com.vshome.callback;

/**
 * Created by anlab on 7/11/16.
 */
public interface SceneControlCallback {
    void onControl(int id, int mode);
    void onResponse(int cmd, int status);
}
