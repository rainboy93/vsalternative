package vn.com.vshome.callback;

/**
 * Created by anlab on 7/26/16.
 */
public interface SceneActionCallback {
    void onActive(int position, boolean turnOn);
    void onScheduleChange(int position);
    void onEdit(int position);
    void onDelete(int position);
}
