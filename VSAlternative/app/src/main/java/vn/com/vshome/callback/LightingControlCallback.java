package vn.com.vshome.callback;

/**
 * Created by anlab on 7/11/16.
 */
public interface LightingControlCallback {
    void onControl(int id, boolean isControlDevice);
    void onResponse(int status);
}
