package vn.com.vshome.database;

import java.util.ArrayList;
import java.util.List;

import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
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
import vn.com.vshome.flexibleadapter.user.RoomItem;
import vn.com.vshome.utils.Define;

/**
 * Created by anlab on 7/8/16.
 */
public class DatabaseService {

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
        ControlGroupItem group1 = new ControlGroupItem(0 + "", 0);
        ControlGroupItem group2 = new ControlGroupItem(1 + "", 1);
        ControlGroupItem group3 = new ControlGroupItem(2 + "", 2);
        ControlGroupItem group4 = new ControlGroupItem(3 + "", 3);
        ControlGroupItem group5 = new ControlGroupItem(4 + "", 4);
        ControlGroupItem group6 = new ControlGroupItem(5 + "", 5);

        List<LightingDevice> relays = LightingDevice.find(
                LightingDevice.class, "room_id = ? and type_id = ?", new String(roomId + "")
                , new String(Define.DEVICE_TYPE_RELAY + ""));
        if (relays.size() > 0) {
            for (int i = 0; i < relays.size(); i++) {
                LightingDevice device = relays.get(i);
                ControlRelayChildItem relayItem = new ControlRelayChildItem("1-" + i, device.getId());
                relayItem.callback = callback;
                relayItem.isControl = isControl;
                relayItem.setHeader(group1);
                if (sceneDevices != null) {
                    for (SceneDevice sceneDevice : sceneDevices) {
                        if (sceneDevice.deviceId == relayItem.deviceId) {
                            relayItem.isSelected = true;
                            relayItem.tempState = new DeviceState();
                            relayItem.tempState.state = sceneDevice.state;
                            relayItem.tempState.param = sceneDevice.param;
                            relayItem.tempState.param1 = sceneDevice.param1;
                            relayItem.tempState.param2 = sceneDevice.param2;
                            relayItem.tempState.param3 = sceneDevice.param3;
                            break;
                        }
                    }
                }
                group1.addSubItem(relayItem);
            }
        } else {
            ControlEmptyItem emptyItem = new ControlEmptyItem("1-0");
            emptyItem.setHeader(group1);
            group1.addSubItem(emptyItem);
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
                dimmerItem.setHeader(group2);
                if (sceneDevices != null) {
                    for (SceneDevice sceneDevice : sceneDevices) {
                        if (sceneDevice.deviceId == dimmerItem.deviceId) {
                            dimmerItem.isSelected = true;
                            dimmerItem.tempState = new DeviceState();
                            dimmerItem.tempState.state = sceneDevice.state;
                            dimmerItem.tempState.param = sceneDevice.param;
                            dimmerItem.tempState.param1 = sceneDevice.param1;
                            dimmerItem.tempState.param2 = sceneDevice.param2;
                            dimmerItem.tempState.param3 = sceneDevice.param3;
                            break;
                        }
                    }
                }
                group2.addSubItem(dimmerItem);
            }
        } else {
            ControlEmptyItem emptyItem = new ControlEmptyItem("1-1");
            emptyItem.setHeader(group1);
            group2.addSubItem(emptyItem);
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
                shutterRelayChildItem.setHeader(group3);
                if (sceneDevices != null) {
                    for (SceneDevice sceneDevice : sceneDevices) {
                        if (sceneDevice.deviceId == shutterRelayChildItem.deviceId) {
                            shutterRelayChildItem.isSelected = true;
                            shutterRelayChildItem.tempState = new DeviceState();
                            shutterRelayChildItem.tempState.state = sceneDevice.state;
                            shutterRelayChildItem.tempState.param = sceneDevice.param;
                            shutterRelayChildItem.tempState.param1 = sceneDevice.param1;
                            shutterRelayChildItem.tempState.param2 = sceneDevice.param2;
                            shutterRelayChildItem.tempState.param3 = sceneDevice.param3;
                            break;
                        }
                    }
                }
                group3.addSubItem(shutterRelayChildItem);
            }
        } else {
            ControlEmptyItem emptyItem = new ControlEmptyItem("1-2");
            emptyItem.setHeader(group1);
            group3.addSubItem(emptyItem);
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
                rgbChildItem.setHeader(group4);
                if (sceneDevices != null) {
                    for (SceneDevice sceneDevice : sceneDevices) {
                        if (sceneDevice.deviceId == rgbChildItem.deviceId) {
                            rgbChildItem.isSelected = true;
                            rgbChildItem.tempState = new DeviceState();
                            rgbChildItem.tempState.state = sceneDevice.state;
                            rgbChildItem.tempState.param = sceneDevice.param;
                            rgbChildItem.tempState.param1 = sceneDevice.param1;
                            rgbChildItem.tempState.param2 = sceneDevice.param2;
                            rgbChildItem.tempState.param3 = sceneDevice.param3;
                            break;
                        }
                    }
                }
                group4.addSubItem(rgbChildItem);
            }
        } else {
            ControlEmptyItem emptyItem = new ControlEmptyItem("1-3");
            emptyItem.setHeader(group1);
            group4.addSubItem(emptyItem);
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
                pirChildItem.setHeader(group5);
                if (sceneDevices != null) {
                    for (SceneDevice sceneDevice : sceneDevices) {
                        if (sceneDevice.deviceId == pirChildItem.deviceId) {
                            pirChildItem.isSelected = true;
                            pirChildItem.tempState = new DeviceState();
                            pirChildItem.tempState.state = sceneDevice.state;
                            pirChildItem.tempState.param = sceneDevice.param;
                            pirChildItem.tempState.param1 = sceneDevice.param1;
                            pirChildItem.tempState.param2 = sceneDevice.param2;
                            pirChildItem.tempState.param3 = sceneDevice.param3;
                            break;
                        }
                    }
                }
                group5.addSubItem(pirChildItem);
            }
        } else {
            ControlEmptyItem emptyItem = new ControlEmptyItem("1-4");
            emptyItem.setHeader(group1);
            group5.addSubItem(emptyItem);
        }

        ControlEmptyItem emptyItem = new ControlEmptyItem("1-0");
        emptyItem.setHeader(group1);
        group6.addSubItem(emptyItem);

        list.add(group1);
        list.add(group2);
        list.add(group3);
        list.add(group4);
        list.add(group5);
        list.add(group6);

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
                if (roomCode != "" && roomCode.charAt(room.getId().intValue()) == '1') {
                    item.isSelected = true;
                } else {
                    item.isSelected = false;
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
}
