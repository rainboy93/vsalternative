package vn.com.vshome.foscamsdk;

import java.nio.ByteBuffer;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import com.bumptech.glide.Glide;
import com.fos.sdk.FosSdkJNI;
import com.fos.sdk.FrameData;
import com.glidebitmappool.GlideBitmapPool;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.Bitmap.Config;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import vn.com.vshome.database.Camera;

/**
 * ��Ƶ���ſؼ�
 * 
 * @author FuS
 * @date 2014-5-5 ����8:52:01
 */
public class VideoSurfaceView extends SurfaceView implements SurfaceHolder.Callback {
	final static String TAG = "VideoSurfaceView";
	/** image width */
	final int maxWidth = 2000;
	/** image height */
	final int maxHeight = 2000;

	private SurfaceHolder sfh;
	private Canvas mCanvas;

	public int dlen = 0;
	/** ipc�����ӵľ�� */
	public int cameraHandlerNo = 0;
	/** ��Ƶ���� */
	private FrameData videoData = null;
	/** ��Ƶ����Buff */
	private ByteBuffer buffer = null;

	private Camera camera;

	public void setCamera(Camera camera){
		this.camera = camera;
	}

	/** lock the bitmap */
	public static Lock mLock = new ReentrantLock();

	private DrawThread mDrawThread;
	private boolean isDraw = false;
	/** �յ���Ƶ����ʱ�������Ƿ�Ϊ��һ���յ����� */
	public boolean isFirstGetData = true;

	/** Ҫ�ü���ͼƬ�������ΪNull����Ĭ����ʾ����ͼƬ */
	private Rect src;
	/** ͼƬ��ʾ�ڻ����ϵ����򣨾�����ʾ�� */
	private Rect dst;
	private Bitmap mBit = null;
	private Bitmap mDrawBit = null;

	/** surface view width */
	public int surfaceWidth = 0;
	/** surface view height */
	public int surfaceHeight = 0;
	/** ������ʾͼƬ�Ŀ� */
	private int imgWidth = 0;
	/** ������ʾͼƬ�ĸ� */
	private int imgHeight = 0;

	public VideoSurfaceView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initVideoSurfaceView(context);
	}

	public VideoSurfaceView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initVideoSurfaceView(context);
	}

	public VideoSurfaceView(Context context) {
		super(context);
		initVideoSurfaceView(context);
	}

	@Override
	public void surfaceCreated(SurfaceHolder arg0) {
		videoData = new FrameData();
		mDrawThread = new DrawThread();
		startDraw();
	}

	public void startDraw() {
		if(!mDrawThread.isAlive())
		{
			mDrawThread.start();
		}
		isDraw = true;
	}

	@Override
	public void surfaceChanged(SurfaceHolder arg0, int arg1, int arg2, int arg3) {
		initVideoSurfaceViewWH();
		if (mBit != null) {
			calcImgSize(mBit.getWidth(), mBit.getHeight());
		}
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder arg0) {
		stopDraw();
	}

	public void stopDraw() {
		// �ر��߳�
		if (videoData != null) {
			videoData.data = null;
			videoData = null;
		}
		isDraw = false;
		if (mDrawThread != null) {
			while (true) {
				try {
					mDrawThread.join();
					break;
				} catch (InterruptedException e) {
					// retry
				}
			}
		}
	}

	/** initialize surface view */
	private void initVideoSurfaceView(Context context) {
		sfh = this.getHolder();
		sfh.addCallback(this);
		mCanvas = new Canvas();
		mBit = null;
		src = new Rect();
		dst = new Rect();
		

		initVideoSurfaceViewWH();
	}

	/** ��ʼ���ؼ��Ŀ�� */
	private void initVideoSurfaceViewWH() {
		surfaceWidth = this.getWidth();
		surfaceHeight = this.getHeight();
	}

	/**
	 * ����surface��ʵ�ʿ�ߣ�������ʾͼƬ�Ŀ��
	 * <p>
	 * ��ͼƬ�Ŀ�߽��еȱ����ź������������<br>
	 * 1.ͼƬ�Ŀ�ߵ���surface�Ŀ�ߣ�<br>
	 * 2.ͼƬ�Ŀ��surface�Ŀ� ���� ͼƬ�ĸ߱�surface�ĸߣ���ʱӦ����surface�Ŀ�Ϊ��������ͼƬ�Ŀ�߽������ţ���<br>
	 * 3.ͼƬ�ĸ߱�surface�ĸ� ���� ͼƬ�Ŀ��surface�Ŀ���ʱӦ����surface�ĸ�Ϊ����������Ƭ�Ŀ�߽������ţ���
	 * 
	 * @param srcPicWidth
	 *            ԭʼͼƬ�Ŀ�
	 * @param srcPicHeight
	 *            ԭʼͼƬ�ĸ�
	 */
	private void calcImgSize(int srcPicWidth, int srcPicHeight) {
		if (surfaceHeight * srcPicWidth >= surfaceWidth * srcPicHeight) {// ��surfaceWidth������ʵ��ͼƬ�ĸ�
			imgWidth = surfaceWidth;
			imgHeight = surfaceWidth * srcPicHeight / srcPicWidth;
		} else {// ��surfaceHeight������ʵ��ͼƬ�ĸ�
			imgHeight = surfaceHeight;
			imgWidth = surfaceHeight * srcPicWidth / srcPicHeight;
		}
	}

	/** draw thread */
	class DrawThread extends Thread {
		@Override
		public void run() {
			
			while (isDraw) {
				if (!sfh.getSurface().isValid()) {
					break;
				}
				try {
					CameraSession session = CameraManager.getInstance().getCameraSession(camera);
					mCanvas = sfh.lockCanvas();
					videoData = session.cameraThread.listData.remove(0);
					if(videoData.len > 0)
					{
						buffer = ByteBuffer.wrap(videoData.data);
					//	Log.i(TAG, "buffer capacity"+ buffer.capacity());
						int videoDataLar = 0 ;
						if (mBit == null || mBit.getWidth() != videoData.picWidth || mBit.getHeight() != videoData.picHeight) 
						{
//							mBit = Bitmap.createBitmap(videoData.picWidth, videoData.picHeight, Config.ARGB_8888);
							mBit = GlideBitmapPool.getBitmap(videoData.picWidth, videoData.picHeight, Config.ARGB_8888);
						}
						mBit.copyPixelsFromBuffer(buffer);
						buffer.rewind();
						synchronized (mBit) {
							if (mBit != null) {
								calcImgSize(mBit.getWidth(), mBit.getHeight());
							// �޶�ͼƬ������ߣ�����OOM
								if (imgWidth <= maxWidth && imgHeight <= maxHeight) {
									mDrawBit = Bitmap.createScaledBitmap(mBit, imgWidth, imgHeight, false);
								}
							} else {
								mDrawBit = null;
							}
							if (mDrawBit != null && mCanvas != null) {
								initDrawRang();								
								mCanvas.drawColor(Color.BLACK);
								mCanvas.drawBitmap(mDrawBit, src, dst, null);
						}
					 }
					}
				} catch (Exception ex) {
					ex.printStackTrace();
				} finally {
					if (mCanvas != null) {
						sfh.unlockCanvasAndPost(mCanvas);
					}
				}
			}
		}
	}

	/** ��ʼ����ʾ������ */
	private void initDrawRang() {
		src.left = 0;
		src.top = 0;
		src.right = imgWidth;
		src.bottom = imgHeight;

		dst.left = (int) (surfaceWidth / 2 - imgWidth / 2);
		dst.top = (int) (surfaceHeight / 2 - imgHeight / 2);
		dst.right = imgWidth + dst.left;
		dst.bottom = imgHeight + dst.top;
	}
}
