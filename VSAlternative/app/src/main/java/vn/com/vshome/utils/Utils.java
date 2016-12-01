package vn.com.vshome.utils;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.AssetFileDescriptor;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;

import java.io.File;
import java.io.FileNotFoundException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import in.workarounds.typography.Button;
import in.workarounds.typography.TextView;
import vn.com.vshome.R;
import vn.com.vshome.VSHome;
import vn.com.vshome.activitymanager.TheActivityManager;
import vn.com.vshome.callback.DialogCallback;
import vn.com.vshome.database.Camera;
import vn.com.vshome.database.DeviceState;
import vn.com.vshome.database.Floor;
import vn.com.vshome.database.LightingDevice;
import vn.com.vshome.database.Room;
import vn.com.vshome.database.Scene;
import vn.com.vshome.database.SceneDevice;
import vn.com.vshome.dialog.ConfirmDialog;
import vn.com.vshome.dialog.ErrorDialog;
import vn.com.vshome.view.MaterialRippleLayout;

/**
 * Created by anlab on 7/5/16.
 */
public class Utils {

    public static void showErrorDialog(Object title, Object message) {
        ErrorDialog errorDialog = new ErrorDialog();
        errorDialog.setTitle(title instanceof String ? (String) title : getString((int) title));
        errorDialog.setContent(message instanceof String ? (String) message : getString((int) message));
        errorDialog.show(TheActivityManager.getInstance().getCurrentActivity().getFragmentManager(), ErrorDialog.class.getSimpleName());
    }

    public static void showConfirmDialog(String title, String message, final DialogCallback callback) {
        ConfirmDialog confirmDialog = new ConfirmDialog();
        confirmDialog.setTitle(title);
        confirmDialog.setContent(message);
        confirmDialog.setCallback(callback);
        confirmDialog.show(TheActivityManager.getInstance().getCurrentActivity().getFragmentManager()
                , ConfirmDialog.class.getSimpleName());
    }

    public final static int Byte2Unsigned(byte a) {
        return (a & 0xFF);
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
                getApplicationVersionCode(context));
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

    public static int getScreenHeight() {
        return Resources.getSystem().getDisplayMetrics().heightPixels;
    }

    public static int getStatusBarHeight(Context context) {
        Class<?> c = null;
        Object obj = null;
        Field field = null;
        int x = 0, statusBarHeight = 0;
        try {
            c = Class.forName("com.android.internal.R$dimen");
            obj = c.newInstance();
            field = c.getField("status_bar_height");
            x = Integer.parseInt(field.get(obj).toString());
            statusBarHeight = context.getResources().getDimensionPixelSize(x);
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        return statusBarHeight;
    }

    public static String getTimeString(int c) {
        if (c >= 10)
            return String.valueOf(c);
        else
            return "0" + String.valueOf(c);
    }

    public static int getColor(int id) {
        return TheActivityManager.getInstance().getCurrentActivity().getResources().getColor(id);
    }

    public static String getString(int id) {
        return TheActivityManager.getInstance().getCurrentActivity().getResources().getString(id);
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

    public static boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) TheActivityManager.getInstance().getCurrentActivity().getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    public static int getApplicationVersionCode(Context ctx) {
        int versionCode = 0;
        try {
            versionCode = ctx.getPackageManager().getPackageInfo(ctx.getPackageName(), 0).versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return versionCode;
    }

    public static void hideKeyboard(Context context) {
        try {
            InputMethodManager inputManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManager.hideSoftInputFromWindow(((Activity) context).getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_IMPLICIT_ONLY);
            inputManager.hideSoftInputFromWindow(((Activity) context).getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        } catch (Exception e) {
            Logger.LogD("Can't hide keyboard");
        }
    }

    public static boolean isAppInBackground(Context context) {
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> appProcesses = activityManager.getRunningAppProcesses();
        if (appProcesses == null)
            return true;
        for (ActivityManager.RunningAppProcessInfo appProcess : appProcesses) {
            if (appProcess.processName.equals(context.getPackageName())) {
                return appProcess.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_BACKGROUND;
            }
        }
        return false;
    }

    public static void restart() {
        Intent launchIntent = TheActivityManager.getInstance().getCurrentActivity()
                .getPackageManager()
                .getLaunchIntentForPackage(TheActivityManager.getInstance().getCurrentActivity().getPackageName());
        if (launchIntent != null) {
            launchIntent.putExtra(Define.INTENT_RESTART, true);
            launchIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            TheActivityManager.getInstance().getCurrentActivity().startActivity(launchIntent);
        }
    }

    public static void trimCache(Context context) {
        try {
            File dir = context.getCacheDir();
            if (dir != null && dir.isDirectory()) {
                deleteDir(dir);
            }
        } catch (Exception e) {
        }
    }

    public static boolean deleteDir(File dir) {
        if (dir != null && dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
        }
        return dir.delete();
    }
}
