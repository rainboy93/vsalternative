package vn.com.vshome.flexibleadapter.security;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;

import org.videolan.libvlc.EventHandler;
import org.videolan.libvlc.IVideoPlayer;
import org.videolan.libvlc.LibVLC;
import org.videolan.libvlc.LibVlcUtil;
import org.videolan.libvlc.Media;
import org.videolan.libvlc.MediaList;
import org.videolan.libvlc.NativeCrashHandler;
import org.videolan.libvlc.VideoView;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.IFilterable;
import eu.davidea.flexibleadapter.items.IHeader;
import eu.davidea.flexibleadapter.items.ISectionable;
import eu.davidea.viewholders.FlexibleViewHolder;
import in.workarounds.typography.TextView;
import onvif.sdk.TXOnvif;
import onvif.sdk.obj.DeviceCapability;
import onvif.sdk.obj.MediaProfilesInfo;
import onvif.sdk.obj.MediaStreamUri;
import vn.com.vshome.R;
import vn.com.vshome.activitymanager.TheActivityManager;
import vn.com.vshome.database.Camera;
import vn.com.vshome.flexibleadapter.AbstractItem;
import vn.com.vshome.utils.Define;
import vn.com.vshome.utils.Logger;
import vn.com.vshome.utils.MiscUtils;
import vn.com.vshome.utils.Utils;
import vn.com.vshome.view.customview.CameraControlView;
import vn.com.vshome.view.customview.CameraView;

public class CameraChildItem extends AbstractItem<CameraChildItem.CameraChildViewHolder>
        implements ISectionable<CameraChildItem.CameraChildViewHolder, IHeader>, IFilterable, View.OnClickListener {

    private static final long serialVersionUID = 2519284529221244210L;

    /**
     * The header of this item
     */
    IHeader header;

    public Camera camera;
    public boolean isError = false;
    public String errorMsg = "";

    public CameraChildItem(String id, Camera camera) {
        super(id);

        this.camera = camera;

//		setDraggable(true);
    }

    @Override
    public IHeader getHeader() {
        return header;
    }

    @Override
    public void setHeader(IHeader header) {
        this.header = header;
    }

    @Override
    public int getLayoutRes() {
        return R.layout.security_camera_child;
    }

    @Override
    public CameraChildViewHolder createViewHolder(FlexibleAdapter adapter, LayoutInflater inflater, ViewGroup parent) {
        return new CameraChildViewHolder(inflater.inflate(getLayoutRes(), parent, false), adapter);
    }

    @SuppressWarnings("deprecation")
    @Override
    public void bindViewHolder(final FlexibleAdapter adapter, final CameraChildViewHolder holder, final int position, List payloads) {

        if (isError) {
            holder.cameraControlView.setActive(false);
            holder.cameraControlView.setVisibility(View.GONE);
            holder.mWarn.setVisibility(View.VISIBLE);
            holder.mWarn.setText(errorMsg);
        } else {
            if (camera.deviceType == 1 || camera.deviceType == 2) {
                holder.cameraViewOnvif.setVisibility(View.INVISIBLE);
                holder.cameraView.setVisibility(View.VISIBLE);

                holder.cameraControlView.setActive(true);
                holder.cameraControlView.setVisibility(View.VISIBLE);
                holder.cameraView.setCamera(camera);
                holder.cameraView.startDraw();
                holder.cameraControlView.setCamera(camera);
                holder.mWarn.setVisibility(View.GONE);
            } else if (camera.deviceType == 3) {
                holder.cameraViewOnvif.setVisibility(View.VISIBLE);
                holder.cameraView.setVisibility(View.INVISIBLE);

                if (camera.isAuth) {
                    holder.cameraControlView.setActive(true);
                    holder.cameraControlView.setVisibility(View.VISIBLE);
                    holder.cameraControlView.setCamera(camera);
                    holder.mWarn.setVisibility(View.GONE);

                    String streamLink = "";
                    if (Define.NETWORK_TYPE == Define.NetworkType.LocalNetwork) {
                        streamLink = camera.localStreamLink;
                    } else if (Define.NETWORK_TYPE == Define.NetworkType.DnsNetwork) {
                        streamLink = camera.dnsStreamLink;
                    }
                    holder.play(streamLink);
                } else {
                    MiscUtils.runOnBackgroundThread(new Runnable() {
                        @Override
                        public void run() {
                            String deviceService = "";
                            if (Define.NETWORK_TYPE == Define.NetworkType.LocalNetwork) {
                                deviceService = camera.localDeviceService;
                            } else if (Define.NETWORK_TYPE == Define.NetworkType.DnsNetwork) {
                                deviceService = camera.dnsDeviceService;
                            }
                            DeviceCapability capability = TXOnvif.getInstance().getDeviceCapabilities(camera.username, camera.password, deviceService);
                            if (capability != null) {
                                String[] part = capability.getMediaService().split(":");
                                if (part.length > 0) {
                                    camera.localMediaService = part[0] + "://" + camera.ipAddress + ":" + part[2];
                                    camera.dnsMediaService = part[0] + "://" + camera.dnsAddress + ":" + part[2];
                                }

                                part = capability.getPtzService().split(":");
                                if (part.length > 0) {
                                    camera.localPtzService = part[0] + "://" + camera.ipAddress + ":" + part[2];
                                    camera.dnsPtzService = part[0] + "://" + camera.dnsAddress + ":" + part[2];
                                }

                                String cameraService = "";
                                if (Define.NETWORK_TYPE == Define.NetworkType.LocalNetwork) {
                                    cameraService = camera.localMediaService;
                                } else if (Define.NETWORK_TYPE == Define.NetworkType.DnsNetwork) {
                                    cameraService = camera.dnsMediaService;
                                }

                                ArrayList<MediaProfilesInfo> profiles = TXOnvif.getInstance().getMediaProfiles(camera.username, camera.password, cameraService);
                                if (profiles != null && profiles.size() > 0) {
                                    MediaProfilesInfo profile = profiles.get(0);
                                    camera.token = profile.getToken();
                                    ArrayList<MediaStreamUri> uris = TXOnvif.getInstance().getMediaStreamUri(camera.username, camera.password, cameraService);
                                    if (uris != null && uris.size() > 0) {
                                        MediaStreamUri uri = uris.get(0);
                                        part = uri.getStreamURI().split(":");
                                        if (part.length > 0) {
                                            camera.localStreamLink = part[0] + "://" + camera.username + ":" +
                                                    camera.password + "@" + camera.ipAddress + ":" + part[2];
                                            camera.dnsStreamLink = part[0] + "://" + camera.username + ":" +
                                                    camera.password + "@" + camera.dnsAddress + ":" + part[2];
                                        }
                                        camera.isAuth = true;
                                        camera.save();

                                        MiscUtils.runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                adapter.notifyDataSetChanged();
                                            }
                                        });
                                    }
                                }
                            }
                        }
                    });
                }
            }
        }

    }

    @Override
    public boolean filter(String constraint) {
        return getTitle() != null && getTitle().toLowerCase().trim().contains(constraint);
    }

    @Override
    public void onClick(View v) {

    }

    class CameraChildViewHolder extends FlexibleViewHolder implements SurfaceHolder.Callback, IVideoPlayer {

        public CameraView cameraView;
        public SurfaceView cameraViewOnvif;
        public CameraControlView cameraControlView;
        public TextView mWarn;
        public String media = "";

        private LibVLC libvlc;
        private EventHandler mEventHandler;
        private final static int VideoSizeChanged = -1;
        private SurfaceHolder holder;
        private int mVideoWidth;
        private int mVideoHeight;

        public CameraChildViewHolder(View view, FlexibleAdapter adapter) {
            super(view, adapter);
            cameraView = (CameraView) view.findViewById(R.id.security_child_camera_view);
            cameraViewOnvif = (SurfaceView) view.findViewById(R.id.security_child_camera_view_onvif);
            cameraView.getLayoutParams().width = Utils.getScreenWidth();
            cameraView.getLayoutParams().height = Utils.getScreenWidth() * 3 / 4;
            cameraControlView = (CameraControlView) view.findViewById(R.id.security_child_camera_control_view);
            mWarn = (TextView) view.findViewById(R.id.security_child_camera_warn);
//            createPlayer(view.getContext());
            holder = cameraViewOnvif.getHolder();
            holder.addCallback(this);
        }

        @Override
        public float getActivationElevation() {
            return 10f;
        }

        public void createPlayer(Context context) {
            releasePlayer();
            try {
                // Create a new media player
                libvlc = new LibVLC();
                mEventHandler = libvlc.getEventHandler();
                libvlc.init(context);
                libvlc.setHardwareAcceleration(LibVLC.HW_ACCELERATION_FULL);
                libvlc.setSubtitlesEncoding("");
                libvlc.setAout(LibVLC.AOUT_OPENSLES);
                libvlc.setTimeStretching(true);
                libvlc.setVerboseMode(false);
                libvlc.setNetworkCaching(300);
                NativeCrashHandler.getInstance().setOnNativeCrashListener(
                        nativecrashListener);
                libvlc.setVout(LibVLC.VOUT_ANDROID_WINDOW);
                LibVLC.restartInstance(context);
                mEventHandler.addHandler(mHandler);
                holder.setKeepScreenOn(true);
            } catch (Exception e) {
                Logger.LogD(e.toString());
            }
        }

        public void play(String media) {
            if(libvlc != null){
                MediaList list = libvlc.getMediaList();
                list.clear();
                list.add(new Media(libvlc, LibVLC.PathToURI(media)), false);
                libvlc.playIndex(0);
//                mute();
            } else {
                this.media = media;
            }
        }

        private Handler mHandler = new MyHandler(this);

        @Override
        public void surfaceCreated(SurfaceHolder holder) {
            createPlayer(TheActivityManager.getInstance().getCurrentActivity());
            libvlc.attachSurface(holder.getSurface(), this);
            if(!media.equals("")){
                play(media);
            }
        }

        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int width,
                                   int height) {
//            if (libvlc != null)
//                libvlc.attachSurface(holder.getSurface(), this);

        }

        @Override
        public void surfaceDestroyed(SurfaceHolder holder) {
            releasePlayer();
        }

        public void eventHardwareAccelerationError() {
            Logger.LogD("Error with hardware acceleration");
            releasePlayer();
        }

        @Override
        public void setSurfaceLayout(int width, int height, int visible_width,
                                     int visible_height, int sar_num, int sar_den) {
            Message msg = Message.obtain(mHandler, VideoSizeChanged, width,
                    height);
            msg.sendToTarget();
        }

        // Used only for old stuff
        @Override
        public int configureSurface(Surface surface, int width, int height,
                                    int hal) {
            Logger.LogD("configureSurface: width = " + width + ", height = "
                    + height);
            if (LibVlcUtil.isICSOrLater() || surface == null)
                return -1;
            if (width * height == 0)
                return 0;
            if (hal != 0)
                holder.setFormat(hal);
            holder.setFixedSize(width, height);
            return 1;
        }

        private class MyHandler extends Handler {
            private WeakReference<CameraChildViewHolder> mOwner;

            public MyHandler(CameraChildViewHolder owner) {
                mOwner = new WeakReference<>(owner);
            }

            @Override
            public void handleMessage(Message msg) {
                CameraChildViewHolder player = mOwner.get();

                // SamplePlayer events
                if (msg.what == VideoSizeChanged) {
                    player.setSize(msg.arg1, msg.arg2);
                    return;
                }

                // Libvlc events
                Bundle b = msg.getData();
                switch (b.getInt("event")) {
                    case EventHandler.MediaPlayerEndReached:
                        player.releasePlayer();
                        break;
                    case EventHandler.MediaPlayerPlaying:
                    case EventHandler.MediaPlayerPaused:
                    case EventHandler.MediaPlayerStopped:
                    default:
                        break;
                }
            }
        }

        public NativeCrashHandler.OnNativeCrashListener nativecrashListener = new NativeCrashHandler.OnNativeCrashListener() {

            @Override
            public void onNativeCrash() {
                Logger.LogD("nativecrash");
            }

        };

        public void releasePlayer() {
            if (libvlc == null)
                return;
            mEventHandler.removeHandler(mHandler);
            libvlc.stop();
            libvlc.detachSurface();
            libvlc.closeAout();
            libvlc.destroy();
            libvlc = null;

            mVideoWidth = 0;
            mVideoHeight = 0;
        }

        private void setSize(int width, int height) {
            if (libvlc != null) {
                libvlc.closeAout();
                libvlc.setVolume(0);
            }

            // Dimensions of the native video
            mVideoWidth = width;
            mVideoHeight = height;

            if (mVideoWidth * mVideoHeight <= 1)
                return;

            // Dimensions of the surface frame
            int surfaceFrameW = cameraViewOnvif.getMeasuredWidth();
            int surfaceFrameH = cameraViewOnvif.getMeasuredHeight();


            float videoAR = (float) mVideoWidth / (float) mVideoHeight;
            float surfaceFrameAr = (float) surfaceFrameW / (float) surfaceFrameH;

            int vidW = surfaceFrameW;
            int vidH = surfaceFrameH;

            if (surfaceFrameAr < videoAR)
                vidH = (int) (surfaceFrameW / videoAR);
            else
                vidW = (int) (surfaceFrameH * videoAR);

            // force surface buffer size
            if (holder != null) {
                holder.setFixedSize(mVideoWidth, mVideoHeight);

            } else {
                Logger.LogD("Holder was null");
            }


            // set display size
            ViewGroup.LayoutParams lp = cameraViewOnvif.getLayoutParams();
            lp.width = vidW;
            lp.height = vidH;
            cameraViewOnvif.setLayoutParams(lp);
            cameraViewOnvif.invalidate();
        }

        public void mute() {
            libvlc.closeAout();
            libvlc.setVolume(0);
        }
    }

    @Override
    public String toString() {
        return "ListFloorChildItem[" + super.toString() + "]";
    }
}