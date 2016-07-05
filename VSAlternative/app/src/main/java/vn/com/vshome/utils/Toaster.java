package vn.com.vshome.utils;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by anlab on 7/5/16.
 */
public class Toaster {
    private static Toast mToast;

    public static void showMessage(Context context, String msg){
        if(mToast == null){
            mToast = Toast.makeText(context, "", Toast.LENGTH_LONG);
        }
        mToast.setText(msg);
        mToast.show();
    }
}
