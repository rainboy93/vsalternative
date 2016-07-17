package vn.com.vshome.database;

import java.util.ArrayList;
import java.util.List;

import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
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

    public static List<AbstractFlexibleItem> getListDeviceItem(int roomId, boolean isControl) {
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
                relayItem.isControl = isControl;
                relayItem.setHeader(group1);
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
                dimmerItem.isControl = isControl;
                dimmerItem.setHeader(group2);
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
                shutterRelayChildItem.isControl = isControl;
                shutterRelayChildItem.setHeader(group3);
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
                rgbChildItem.isControl = isControl;
                rgbChildItem.setHeader(group4);
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
                pirChildItem.isControl = isControl;
                pirChildItem.tempState.state = Define.STATE_DISBALE;
                pirChildItem.setHeader(group5);
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

        return list;
    }

    public static List<AbstractFlexibleItem> getListDayItem() {
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
        return list;
    }
}
