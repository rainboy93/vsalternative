package vn.com.vshome.callback;

import vn.com.vshome.database.Room;

/**
 * Created by anlab on 7/22/16.
 */
public interface RoomSelectionCallback {
    void onSelect(Room room);
    void onCapture(int position, Room room);
}
