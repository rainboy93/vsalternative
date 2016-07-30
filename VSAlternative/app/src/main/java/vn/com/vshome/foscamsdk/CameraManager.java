package vn.com.vshome.foscamsdk;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import vn.com.vshome.database.Camera;

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

    public void addSession(Camera camera) {
        CameraSession session = mListSession.get(camera.getId());
        if (session == null) {
            session = new CameraSession();
            session.amount = 1;
            mListSession.put(camera.getId(), session);
        } else {
            session.amount++;
        }
    }

    public void removeSession(Camera camera) {
        CameraSession session = mListSession.get(camera.getId());
        if (session != null) {
            session.amount--;
            if (session.amount <= 0) {
                mListSession.remove(camera.getId());
            }
        }
    }
}
