package vn.com.vshome.foscamsdk;

import com.fos.sdk.ConnectType;
import com.fos.sdk.FosSdkJNI;
import com.fos.sdk.IPCType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import vn.com.vshome.VSHome;
import vn.com.vshome.callback.CameraCallback;
import vn.com.vshome.database.Camera;
import vn.com.vshome.utils.Define;
import vn.com.vshome.utils.Logger;

public class CameraManager {

    private static CameraManager cameraManager;
    private Map<Long, CameraSession> mListSession;

    public static CameraManager getInstance() {
        if (cameraManager == null) {
            cameraManager = new CameraManager();
        }
        return cameraManager;
    }

    public CameraManager() {
        mListSession = new HashMap<>();
    }

    public void addSession(Camera camera, CameraCallback cameraCallback) {
        CameraSession session = mListSession.get(camera.getId());
        if (session == null) {
            session = new CameraSession();
            session.amount = 1;
            session.cameraThread = new CameraThread(camera);
            session.cameraThread.setCameraCallback(cameraCallback);
            session.cameraThread.start();
            session.isHasCore = true;
            mListSession.put(camera.getId(), session);
        } else {
            session.amount++;
        }
        Logger.LogD("Add session id " + camera.getId() + " count " + session.amount);
    }

    public void removeSession(Camera camera) {
        CameraSession session = mListSession.get(camera.getId());
        if (session != null) {
            session.amount--;
            Logger.LogD("Remove session " + session.amount);
            if (session.amount <= 0) {
                session.cameraThread.stopGetData();
                session.cameraThread = null;
                mListSession.remove(camera.getId());
            }
        }
    }

    public void removeSession(Camera camera, boolean isCore) {
        CameraSession session = mListSession.get(camera.getId());
        if (session != null) {
            session.amount--;
            Logger.LogD("Remove session id " + camera.getId() + " count " + session.amount);
            session.isHasCore = false;
            if (session.amount <= 0) {
                session.cameraThread.stopGetData();
                session.cameraThread = null;
                mListSession.remove(camera.getId());
            }
        }
    }

    public CameraSession getCameraSession(Camera camera) {
        if(camera == null){
            return null;
        }
        long id = camera.getId();
        return mListSession.get(id);
    }
}
