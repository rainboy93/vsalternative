package vn.com.vshome.utils;

import android.app.Activity;
import android.content.Context;
import android.widget.Toast;

/**
 * Created by anlab on 7/5/16.
 */
public class Toaster {
    private static Toast mToast;

    public static void showMessage(final Activity activity, final String msg){
        MiscUtils.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(mToast == null){
                    mToast = Toast.makeText(activity, "", Toast.LENGTH_LONG);
                }
                mToast.setText(msg);
                mToast.show();
            }
        });
    }
}
