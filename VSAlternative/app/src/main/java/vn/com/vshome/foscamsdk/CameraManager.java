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

    public boolean isPreviewing = false;
    public Camera currentCamera;

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
            if (cameraCallback != null) {
                session.isHasCore = true;
            }
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

    public void removeAllSessionExcept(Camera camera) {
        CameraSession session = mListSession.get(camera.getId());
        if (session != null) {
            session.amount = 1;
            session.isHasCore = false;
        }
    }

    public void removeAll(Camera camera) {
        CameraSession session = mListSession.get(camera.getId());
        if (session != null) {
            session.amount = 0;
            Logger.LogD("Remove session id " + camera.getId() + " count " + session.amount);
            session.isHasCore = false;
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
        if (camera == null) {
            return null;
        }
        try {
            Long id = camera.getId();
            return mListSession.get(id);
        } catch (Exception e) {
            return null;
        }
    }

    public void releaseCamera() {
        if (mListSession != null) {
            for (CameraSession session : mListSession.values()) {
                if (session != null && session.cameraThread != null) {
                    session.cameraThread.stopGetData();
                    session.cameraThread = null;
                }
            }
            mListSession.clear();
        }
    }
}
