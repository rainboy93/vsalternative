package vn.com.vshome.utils;


import android.os.Handler;

/**
 * Created by anlab on 7/6/16.
 */
public class TimeOutManager {
    private Handler handler;
    private static TimeOutManager timeOutManager;
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            if (callback != null) {
                callback.onTimeOut();
            }
        }
    };

    public static TimeOutManager getInstance() {
        if (timeOutManager == null) {
            timeOutManager = new TimeOutManager();
        }
        return timeOutManager;
    }

    public TimeOutManager() {
        handler = new Handler();
    }

    public void startCountDown(TimeOutCallback timeOutCallback, int second) {
        this.callback = timeOutCallback;
        handler.postDelayed(runnable, 1000 * second);
    }

    public void cancelCountDown(){
        if(handler != null){
            try {
                handler.removeCallbacks(runnable);
            } catch (Exception e){

            }
        }
    }

    private TimeOutCallback callback;

    public interface TimeOutCallback {
        void onTimeOut();
    }
}
