package vn.com.vshome.utils;

import android.content.Context;
import net.the4thdimension.android.SharedPreferenceManager;

/**
 * Created by anlab on 7/4/16.
 */
public class PreferenceUtils {
    private static final String SHARED_PREFERENCE_NAME = "VSHome";

    private static SharedPreferenceManager sharedPreferenceManager;

    public static SharedPreferenceManager getInstance(Context context){
        if(sharedPreferenceManager == null){
            sharedPreferenceManager = new SharedPreferenceManager(context, SHARED_PREFERENCE_NAME);
        }
        return sharedPreferenceManager;
    }
}
