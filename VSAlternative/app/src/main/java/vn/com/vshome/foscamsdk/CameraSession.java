package vn.com.vshome.foscamsdk;

import vn.com.vshome.database.Camera;

/**
 * Created by anlab on 7/29/16.
 */
public class CameraSession {
    public Camera camera;
    public int amount = 0;
    public CameraThread cameraThread;
    public int handler = -1;
    public boolean isHasCore = false;
}
