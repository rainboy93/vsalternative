package vn.com.vshome.foscamsdk;

import com.fos.sdk.FrameData;

/**
 * Created by anlab on 9/9/16.
 */
public class CameraData {

    public int mediatype;
    public int  decfmt;
    public int	isKey;
    public int  frameTag;
    public int  picWidth;
    public int  picHeight;
    public int  frameRate;
    public int  videobitRate;
    public int  audiobitRate;
    public int  channel;
    public int	sampale;
    public long  pts;
    public int	 len;
    public byte[] data;

    public CameraData(FrameData frameData){
        mediatype = frameData.mediatype;
        decfmt = frameData.decfmt;
        isKey = frameData.isKey;
        frameTag = frameData.frameTag;
        picWidth = frameData.picWidth;
        picHeight = frameData.picHeight;
        frameRate = frameData.frameRate;
        videobitRate = frameData.videobitRate;
        audiobitRate = frameData.audiobitRate;
        channel = frameData.channel;
        sampale = frameData.sampale;
        pts = frameData.pts;
        len = frameData.len;
        data = frameData.data.clone();
    }
}
