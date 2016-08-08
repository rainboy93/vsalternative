package vn.com.vshome.utils;

import android.app.Dialog;
import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.view.View;
import android.view.Window;

import com.balysv.materialripple.MaterialRippleLayout;

import java.io.FileNotFoundException;
import java.util.ArrayList;

import in.workarounds.typography.Button;
import in.workarounds.typography.TextView;
import vn.com.vshome.R;
import vn.com.vshome.VSHome;
import vn.com.vshome.callback.DialogCallback;
import vn.com.vshome.database.Camera;
import vn.com.vshome.database.DeviceState;
import vn.com.vshome.database.Floor;
import vn.com.vshome.database.LightingDevice;
import vn.com.vshome.database.Room;
import vn.com.vshome.database.Scene;
import vn.com.vshome.database.SceneDevice;

/**
 * Created by anlab on 7/5/16.
 */
public class Utils {

    public static void showErrorDialog(String title, String message,
                                       Context context) {
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.view_dialog_error);

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
        dialog.setContentView(R.layout.view_dialog_error);

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

    public static void showConfirmDialog(String title, String message,
                                         Context context, final DialogCallback callback) {
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.view_dialog_confirm);

        TextView mTitle = (TextView) dialog.findViewById(R.id.dialog_confirm_title);
        mTitle.setText(title);
        TextView mContent = (TextView) dialog.findViewById(R.id.dialog_confirm_content);
        mContent.setText(message);
        Button mCancel = (Button) dialog.findViewById(R.id.dialog_confirm_cancel_button);
        mCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });
        Button mOk = (Button) dialog.findViewById(R.id.dialog_confirm_confirm_button);
        mOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
                if (callback != null) {
                    callback.onConfirm();
                }
            }
        });
        dialog.show();
    }

    public final static int Byte2Unsigned(byte a) {
        return (a & 0xFF);
    }

    public final static int Int2Unsigned(int a) {
        return a & 0xFF;
    }

    public static void putDatabase(Context context, ArrayList<Floor> floors, int uid) {
        if (floors == null) {
            return;
        }

        Floor.deleteAll(Floor.class);
        Room.deleteAll(Room.class);
        LightingDevice.deleteAll(LightingDevice.class);
        Camera.deleteAll(Camera.class);
        Scene.deleteAll(Scene.class);
        SceneDevice.deleteAll(SceneDevice.class);

        for (Floor floor : floors) {
            floor.save();

            for (Room room : floor.rooms) {
                room.save();

                for (LightingDevice device : room.devices) {
                    DeviceState state = new DeviceState();
                    state.setId(device.getId());
                    state.state = 1;
                    state.param = 0;
                    state.param1 = 0;
                    state.param2 = 0;
                    state.param3 = 0;
                    state.save();
                    device.save();
                }

                for (Camera camera : room.foscams) {
                    camera.save();
                }
            }
        }

        Logger.LogD("Floor size " + Floor.listAll(Floor.class).size());
        Logger.LogD("Room size " + Room.listAll(Room.class).size());
        Logger.LogD("Device size " + LightingDevice.listAll(LightingDevice.class).size());
        Logger.LogD("FoscamCamera size " + Camera.listAll(Camera.class).size());
        Logger.LogD("State size " + DeviceState.listAll(DeviceState.class).size());

        PreferenceUtils.getInstance(context).setValue(PreferenceDefine.UID, uid);
        PreferenceUtils.getInstance(context).setValue(PreferenceDefine.VERSION_CODE,
                net.the4thdimension.android.Utils.getApplicationVersionCode(context));
    }

    public static void createRipple(Context context, View v) {
        MaterialRippleLayout.on(v).rippleColor(context.getResources().getColor(R.color.white))
                .rippleDelayClick(false)
                .rippleOverlay(true)
                .rippleAlpha(0.3f).create();
    }

    public static int getScreenWidth() {
        return Resources.getSystem().getDisplayMetrics().widthPixels;
    }

    public static String getTimeString(int c) {
        if (c >= 10)
            return String.valueOf(c);
        else
            return "0" + String.valueOf(c);
    }

    public static int getColor(int id) {
        return VSHome.activity.getResources().getColor(id);
    }

    public static String getString(int id) {
        return VSHome.activity.getResources().getString(id);
    }

    private static Bitmap decodeBitmap(Context context, Uri theUri,
                                       int sampleSize) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = sampleSize;

        AssetFileDescriptor fileDescriptor = null;
        try {
            fileDescriptor = context.getContentResolver()
                    .openAssetFileDescriptor(theUri, "r");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        Bitmap actuallyUsableBitmap = BitmapFactory.decodeFileDescriptor(
                fileDescriptor.getFileDescriptor(), null, options);

        return actuallyUsableBitmap;
    }

    public static Bitmap getImageResized(Context context, Uri selectedImage) {
        Bitmap bm = null;
        int[] sampleSizes = new int[]{5, 3, 2, 1};
        int i = 0;
        do {
            bm = decodeBitmap(context, selectedImage, sampleSizes[i]);
            i++;
        } while (bm.getWidth() < 400 && i < sampleSizes.length);
        return bm;
    }
}
