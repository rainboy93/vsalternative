package vn.com.vshome.foscamsdk;

import com.fos.sdk.FrameData;

/**
 * Created by anlab on 9/9/16.
 */
public class CameraData extends FrameData implements Cloneable {
    public CameraData clone() throws CloneNotSupportedException {
        return (CameraData) super.clone();
    }
}
