package vn.com.vshome.database;

import java.util.ArrayList;
import java.util.List;

import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import eu.davidea.flexibleadapter.items.IHeader;
import vn.com.vshome.callback.DeviceSelectCallback;
import vn.com.vshome.flexibleadapter.AbstractControlItem;
import vn.com.vshome.flexibleadapter.lightingcontrol.ControlDimmerChildItem;
import vn.com.vshome.flexibleadapter.lightingcontrol.ControlEmptyItem;
import vn.com.vshome.flexibleadapter.lightingcontrol.ControlGroupItem;
import vn.com.vshome.flexibleadapter.lightingcontrol.ControlPirChildItem;
import vn.com.vshome.flexibleadapter.lightingcontrol.ControlRelayChildItem;
import vn.com.vshome.flexibleadapter.lightingcontrol.ControlRgbChildItem;
import vn.com.vshome.flexibleadapter.lightingcontrol.ControlShutterRelayChildItem;
import vn.com.vshome.flexibleadapter.ListFloorChildItem;
import vn.com.vshome.flexibleadapter.ListFloorGroupItem;
import vn.com.vshome.flexibleadapter.lightingscene.DayItem;
import vn.com.vshome.flexibleadapter.roomselection.FloorItem;
import vn.com.vshome.flexibleadapter.security.CameraChildItem;
import vn.com.vshome.flexibleadapter.security.CameraGroupItem;
import vn.com.vshome.flexibleadapter.user.RoomItem;
import vn.com.vshome.utils.Define;

/**
 * Created by anlab on 7/8/16.
 */
public class DatabaseService {

    private static final int NUMBER_OF_TYPE = 6;

    public static List<AbstractFlexibleItem> getListFloorItem() {
        List<AbstractFlexibleItem> list = new ArrayList<>();
        List<Floor> listFloor = Floor.listAll(Floor.class);
        for (int i = 0; i < listFloor.size(); i++) {
            Floor floor = listFloor.get(i);
            List<Room> listRoom = floor.getListRoom();
            ListFloorGroupItem group = new ListFloorGroupItem(floor.getId() + "", floor.getId());

            for (int j = 0; j < listRoom.size(); j++) {
                Room room = listRoom.get(j);
                ListFloorChildItem child = new ListFloorChildItem(floor.getId() + "-" + room.getId(), room.getId());
                child.setHeader(group);
                group.addSubItem(child);
            }

            list.add(group);
        }
        return list;
    }

    public static List<AbstractFlexibleItem> getListDeviceItem(int roomId, boolean isControl,
                                                               DeviceSelectCallback callback,
                                                               List<AbstractFlexibleItem> previousList,
                                                               int sceneId) {
        List<SceneDevice> sceneDevices = null;
        if (!isControl && sceneId != -1) {
            Scene scene = Scene.findById(Scene.class, sceneId);
            if (scene != null) {
                sceneDevices = SceneDevice.find(SceneDevice.class,
                        "scene_id = ?", new String[]{scene.getId() + ""});
            }
        }

        List<AbstractFlexibleItem> list = new ArrayList<>();
        for (int i = 0; i < NUMBER_OF_TYPE; i++) {
            ControlGroupItem group = new ControlGroupItem(i + "", i);
            group.isControl = isControl;
            list.add(group);
        }

        List<LightingDevice> relays = LightingDevice.find(
                LightingDevice.class, "room_id = ? and type_id = ?", new String(roomId + "")
                , new String(Define.DEVICE_TYPE_RELAY + ""));
        if (relays.size() > 0) {
            for (int i = 0; i < relays.size(); i++) {
                LightingDevice device = relays.get(i);
                ControlRelayChildItem relayItem = new ControlRelayChildItem("1-" + i, device.getId());
                relayItem.callback = callback;
                relayItem.isControl = isControl;
                relayItem.setHeader((ControlGroupItem) list.get(0));
                findSelectedDevice(sceneDevices, relayItem);
                ((ControlGroupItem) list.get(0)).addSubItem(relayItem);
            }
        } else {
            ControlEmptyItem emptyItem = new ControlEmptyItem("1-0");
            emptyItem.setHeader((ControlGroupItem) list.get(0));
            ((ControlGroupItem) list.get(0)).addSubItem(emptyItem);
        }

        List<LightingDevice> dimmer = LightingDevice.find(
                LightingDevice.class, "room_id = ? and type_id = ?",
                new String(roomId + ""), new String(Define.DEVICE_TYPE_DIMMER + ""));
        if (dimmer.size() > 0) {
            for (int i = 0; i < dimmer.size(); i++) {
                LightingDevice device = dimmer.get(i);
                ControlDimmerChildItem dimmerItem = new ControlDimmerChildItem("1-" + i, device.getId());
                dimmerItem.callback = callback;
                dimmerItem.isControl = isControl;
                dimmerItem.setHeader((ControlGroupItem) list.get(1));
                findSelectedDevice(sceneDevices, dimmerItem);
                ((ControlGroupItem) list.get(1)).addSubItem(dimmerItem);
            }
        } else {
            ControlEmptyItem emptyItem = new ControlEmptyItem("1-1");
            emptyItem.setHeader((ControlGroupItem) list.get(1));
            ((ControlGroupItem) list.get(1)).addSubItem(emptyItem);
        }

        List<LightingDevice> shutterRelays = LightingDevice.find(
                LightingDevice.class, "room_id = ? and type_id = ?",
                new String(roomId + ""), new String(Define.DEVICE_TYPE_SHUTTER_RELAY + ""));
        if (shutterRelays.size() > 0) {
            for (int i = 0; i < shutterRelays.size(); i++) {
                LightingDevice device = shutterRelays.get(i);
                ControlShutterRelayChildItem shutterRelayChildItem = new ControlShutterRelayChildItem("1-" + i, device.getId());
                shutterRelayChildItem.callback = callback;
                shutterRelayChildItem.isControl = isControl;
                shutterRelayChildItem.setHeader((ControlGroupItem) list.get(2));
                findSelectedDevice(sceneDevices, shutterRelayChildItem);
                ((ControlGroupItem) list.get(2)).addSubItem(shutterRelayChildItem);
            }
        } else {
            ControlEmptyItem emptyItem = new ControlEmptyItem("1-2");
            emptyItem.setHeader((ControlGroupItem) list.get(2));
            ((ControlGroupItem) list.get(2)).addSubItem(emptyItem);
        }

        List<LightingDevice> rgb = LightingDevice.find(
                LightingDevice.class, "room_id = ? and type_id = ?",
                new String(roomId + ""), new String(Define.DEVICE_TYPE_RGB + ""));
        if (rgb.size() > 0) {
            for (int i = 0; i < rgb.size(); i++) {
                LightingDevice device = rgb.get(i);
                ControlRgbChildItem rgbChildItem = new ControlRgbChildItem("1-" + i, device.getId());
                rgbChildItem.callback = callback;
                rgbChildItem.isControl = isControl;
                rgbChildItem.setHeader((ControlGroupItem) list.get(3));
                findSelectedDevice(sceneDevices, rgbChildItem);
                ((ControlGroupItem) list.get(3)).addSubItem(rgbChildItem);
            }
        } else {
            ControlEmptyItem emptyItem = new ControlEmptyItem("1-3");
            emptyItem.setHeader((ControlGroupItem) list.get(3));
            ((ControlGroupItem) list.get(3)).addSubItem(emptyItem);
        }

        List<LightingDevice> pir = LightingDevice.find(
                LightingDevice.class, "room_id = ? and (type_id = ? or type_id = ?)",
                new String(roomId + ""),
                new String(Define.DEVICE_TYPE_PIR + ""), new String(Define.DEVICE_TYPE_WIR + ""));
        if (pir.size() > 0) {
            for (int i = 0; i < pir.size(); i++) {
                LightingDevice device = pir.get(i);
                ControlPirChildItem pirChildItem = new ControlPirChildItem("1-" + i, device.getId());
                pirChildItem.callback = callback;
                pirChildItem.isControl = isControl;
                pirChildItem.tempState.state = Define.STATE_DISBALE;
                pirChildItem.setHeader((ControlGroupItem) list.get(4));
                findSelectedDevice(sceneDevices, pirChildItem);
                ((ControlGroupItem) list.get(4)).addSubItem(pirChildItem);
            }
        } else {
            ControlEmptyItem emptyItem = new ControlEmptyItem("1-4");
            emptyItem.setHeader((ControlGroupItem) list.get(4));
            ((ControlGroupItem) list.get(4)).addSubItem(emptyItem);
        }

        ControlEmptyItem emptyItem = new ControlEmptyItem("1-0");
        emptyItem.setHeader((ControlGroupItem) list.get(5));
        ((ControlGroupItem) list.get(5)).addSubItem(emptyItem);

        if (previousList != null) {
            for (int i = 0; i < previousList.size(); i++) {
                AbstractFlexibleItem item = previousList.get(i);
                if (item instanceof ControlGroupItem && ((ControlGroupItem) item).isExpanded()) {
                    ((ControlGroupItem) list.get(i)).setExpanded(true);
                }
            }
        }

        return list;
    }

    private static void findSelectedDevice(List<SceneDevice> sceneDevices, AbstractControlItem controlItem) {
        if (sceneDevices != null) {
            for (SceneDevice sceneDevice : sceneDevices) {
                if (sceneDevice.deviceId == controlItem.deviceId) {
                    controlItem.isSelected = true;
                    controlItem.tempState = new DeviceState();
                    controlItem.tempState.state = sceneDevice.state;
                    controlItem.tempState.param = sceneDevice.param;
                    controlItem.tempState.param1 = sceneDevice.param1;
                    controlItem.tempState.param2 = sceneDevice.param2;
                    controlItem.tempState.param3 = sceneDevice.param3;
                    break;
                }
            }
        }
    }

    public static List<AbstractFlexibleItem> getListDayItem(Scene scene) {
        List<AbstractFlexibleItem> list = new ArrayList<>();
        for (int i = 0; i < 7; i++) {
            if (i == 6) {
                DayItem item = new DayItem(i + "", "CN");
                list.add(item);
            } else {
                DayItem item = new DayItem(i + "", "T" + (i + 2));
                list.add(item);
            }
        }
        if (scene != null) {
            int[] days = new int[]{
                    scene.monday, scene.tuesday, scene.wednesday, scene.thursday, scene.friday,
                    scene.saturday, scene.sunday
            };
            for (int i = 0; i < 7; i++) {
                ((DayItem) list.get(i)).active = days[i];
            }
        }
        return list;
    }

    public static List<AbstractFlexibleItem> getListRoomAddUser(User user) {
        String roomCode = "";
        if (user != null) {
            roomCode = user.roomControl;
        }
        List<AbstractFlexibleItem> list = new ArrayList<>();
        List<Room> rooms = Room.listAll(Room.class, "id");
        if (rooms != null && rooms.size() > 0) {
            for (int i = 0; i < rooms.size(); i++) {
                Room room = rooms.get(i);
                RoomItem item = new RoomItem(i + "", room);
                if (roomCode != "" && roomCode.charAt(room.getId().intValue() - 1) == '1') {
                    item.isSelected = true;
                } else {
                    item.isSelected = false;
                }
                if (user != null && user.priority == Define.PRIORITY_ADMIN) {
                    item.isAdmin = true;
                }
                list.add(item);
            }
        }
        return list;
    }

    public static List<AbstractFlexibleItem> getListFloorSelection() {
        List<AbstractFlexibleItem> list = new ArrayList<>();
        List<Floor> floors = Floor.listAll(Floor.class, "id");
        if (floors != null && floors.size() > 0) {
            int size = floors.size();
            for (int i = 0; i < size; i++) {
                Floor floor = floors.get(i);
                FloorItem item = new FloorItem(i, floor);
                if (i == 0) {
                    item.isSelected = true;
                } else {
                    item.isSelected = false;
                }
                list.add(item);
            }
        }
        if (floors.size() < 4) {
            for (int i = floors.size(); i < 4; i++) {
                FloorItem item = new FloorItem(i, null);
                list.add(item);
            }
        }
        return list;
    }

    public static List<AbstractFlexibleItem> getListCamera() {
        List<AbstractFlexibleItem> list = new ArrayList<>();
        List<Camera> cameras = Camera.listAll(Camera.class, "id");
        if (cameras != null && cameras.size() > 0) {
            for (int i = 0; i < cameras.size(); i++) {
                Camera camera = cameras.get(i);
                CameraGroupItem groupItem = new CameraGroupItem(i + "", camera);
                CameraChildItem childItem = new CameraChildItem(i + "", camera);
                childItem.setHeader(groupItem);
                groupItem.addSubItem(childItem);
                list.add(groupItem);
            }
        }
        return list;
    }
}
