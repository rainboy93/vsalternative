package vn.com.vshome.utils;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.Window;

import com.balysv.materialripple.MaterialRippleLayout;

import java.util.ArrayList;

import in.workarounds.typography.Button;
import in.workarounds.typography.TextView;
import vn.com.vshome.R;
import vn.com.vshome.database.TblCamera;
import vn.com.vshome.database.TblFloor;
import vn.com.vshome.database.TblLightingDevice;
import vn.com.vshome.database.TblRoom;
import vn.com.vshome.database.TblScene;
import vn.com.vshome.database.TblSceneDevice;
import vn.com.vshome.database.model.CameraDevice;
import vn.com.vshome.database.model.Floor;
import vn.com.vshome.database.model.LightingDevice;
import vn.com.vshome.database.model.Room;

/**
 * Created by anlab on 7/5/16.
 */
public class Utils {

    public static void showErrorDialog(String title, String message,
                                       Context context) {
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_error);

        TextView mTitle = (TextView) dialog.findViewById(R.id.dialog_error_title);
        mTitle.setText(title);
        TextView mContent = (TextView) dialog.findViewById(R.id.dialog_error_content);
        mContent.setText(message);
        Button mCancel = (Button) dialog.findViewById(R.id.dialog_error_cancel_button);
        mCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });
        dialog.show();
    }

    public static void showErrorDialog(int title, int message,
                                       Context context) {
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_error);

        TextView mTitle = (TextView) dialog.findViewById(R.id.dialog_error_title);
        mTitle.setText(context.getResources().getString(title));
        TextView mContent = (TextView) dialog.findViewById(R.id.dialog_error_content);
        mContent.setText(context.getResources().getString(message));
        Button mCancel = (Button) dialog.findViewById(R.id.dialog_error_cancel_button);
        mCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });
        dialog.show();
    }

    public final static int Byte2Unsigned(byte a) {
        return a & 0xFF;
    }

    public final static int Int2Unsigned(int a) {
        return a & 0xFF;
    }

    public static void putDatabase(Context context, ArrayList<Floor> floors, int uid){
        if(floors == null){
            return;
        }

        TblFloor.deleteAll(TblFloor.class);
        TblRoom.deleteAll(TblRoom.class);
        TblLightingDevice.deleteAll(TblLightingDevice.class);
        TblCamera.deleteAll(TblCamera.class);
        TblScene.deleteAll(TblScene.class);
        TblSceneDevice.deleteAll(TblSceneDevice.class);

        for(Floor floor : floors){
            TblFloor fl = new TblFloor();
            fl.setFloor(floor);
            fl.save();

            for(Room room : floor.rooms){
                TblRoom r = new TblRoom();
                r.setRoom(room);
                r.save();

                for(LightingDevice device : room.devices){
                    TblLightingDevice d = new TblLightingDevice();
                    d.setLightingDevice(device);
                    d.save();
                }

                for(CameraDevice camera : room.foscams){
                    TblCamera c = new TblCamera();
                    c.setCamera(camera);
                    c.save();
                }
            }
        }

        Logger.LogD("Floor size " + TblFloor.listAll(TblFloor.class).size());
        Logger.LogD("Room size " + TblRoom.listAll(TblRoom.class).size());
        Logger.LogD("Device size " + TblLightingDevice.listAll(TblLightingDevice.class).size());
        Logger.LogD("Camera size " + TblCamera.listAll(TblCamera.class).size());

        PreferenceUtils.getInstance(context).setValue(PreferenceDefine.UID, uid);
        PreferenceUtils.getInstance(context).setValue(PreferenceDefine.VERSION_CODE,
                net.the4thdimension.android.Utils.getApplicationVersionCode(context));
    }

    public static void createRipple(Context context, View v){
        MaterialRippleLayout.on(v).rippleColor(context.getResources().getColor(R.color.white))
                .rippleOverlay(true)
                .rippleAlpha(0.3f).create();
    }
}
