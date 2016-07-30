package vn.com.vshome.foscamsdk;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.media.AudioRecord;
import android.os.Handler;

/**
 * ϵͳȫ����
 * 
 * @author FuS
 * @date 2014-4-9 ����10:27:30
 */
public class Global {
	/** ������������� */
	public static int containerHeight = 0;
	/** ������������� */
	public static int containerWidth = 0;

	/** ��Ļ�ߣ����أ� */
	public static int screenHeight = 0;
	/** ��Ļ�����أ� */
	public static int screenWidth = 0;

	
	public static AudioRecord mAudioRecord;
	/** �û���ӵ�е�camera�б� */
	public static ArrayList<Camera> g_camera = new ArrayList<Camera>();
	// public static Map<String, Camera> g_camera = new LinkedHashMap<String,
	// Camera>();

	/** ��ʷactiviy(Ŀǰֻ��¼����Ƶ���ŵĽ��棬���Ҫɾ��cameraʱ����Ҫ����Ƶ���ŵ�activity finish) */
	public static ArrayList<Activity> activityTask = new ArrayList<Activity>();

	/** �����Ƿ��������� */
	public static boolean ISRUNNING = false;
	
	/** ����ipc���Ӻ� ���ص� handler flag */
	public static int mHandlerNo = -3;

	/** ȫ��handler���õ��ײ㷵�ص���Ϣ�󣬴��ݸ���Ϣ����Ӧ�Ľ��� */
	public static Handler mHandler = null;

	/** ��ǰʹ�õ���Ƶ����ͷ */
	public static Camera currentPlayCamera = null;

	/** �ļ������գ�¼����־�ȣ���ŵ�Ŀ¼��sdĿ¼+/FoscamApp/�� */
	public static String SDPath = "";
	
	/** ��ǰ��Ƶ�Ƿ�� */
	public static boolean isAudioOpenOrNot = false;
	
	/** ��Ƶ���ſؼ��Ŀ� */
	public static boolean isRecordOrNot = false; 
	/** ��Ƶ���ſؼ��Ŀ� */
	public static int videoScreenWidth = 0;
	/** ��Ƶ���ſؼ��ĸ� */
	public static int videoScreenHeight = 0;
	/** �Ƿ���� */
	public static boolean isLandspace = false;
	
	/** һ�������У��Ƿ��Ѿ����յ�����Ƶ���� ,�Ͽ���������һ�� */
	public static int OPEN_VIDEO_STATE = -1;
	/** һ�������У��Ƿ��Ѿ����յ�����Ƶ���� ,�Ͽ���������һ�� */
	public static boolean isReceiveData = false;
	/** �Ƿ���Ҫץ��ͼƬ�����ץ�İ�ťʱ�� */
	public static boolean isNeedSnap = false;
	/** �Ƿ���Ҫץ��ͼƬ��¼��ʱ����Ҫץ��һ��ͼƬ��Ϊ¼���ļ�������ͼ�� */
	public static boolean isNeedSnapRecord = false;
	/** �Ƿ���Ҫץ��ͼƬ��������ƵԤ���������Ҫץ��һ��ͼƬ��Ϊ����ͼƬ�� */
	public static boolean isNeedSnaps = false;

	/** ���ڱ���camera list ��ȡ�� ֡ **/
	public static HashMap<Camera, Drawable> FRAME_IMAGE = new HashMap<Camera, Drawable>();
	/** Ҫץ�ĵ�ͼƬ */
	public static Bitmap snapBmp = null;
	/*** ��Ļ�ܶȣ����ڴ� dip�� px֮���ת�� **/
	public static float scale = 0f;

	public static boolean isIPCconect = false;
	/** ��ƽ̨���õ����� */
	public static String cloudPlatLanguage = "CHS";
	/** ע�����˻���ʱ���Ƿ���Ҫ���������롣�����ʶ�Ǵ��Ʒ������ϻ�ȡ�ġ�ǰ�ڻ�Ҫ��ʹ�������룬���ڿ��ܻ��Ƴ��� */
	public static boolean isNeedInvitCode = false;
	/** ���˻���¼�󷵻ص���ʱtoken */
	public static String accessToken = "";
	/** ˢ�µ�¼������Ҫ������ */
	public static String refreshToken = "";
	/** OPENID���û���ݱ�ʾ�� */
	public static String openId = "";
	/** ��¼���ƵĹ���ʱ�䣨�룩 */
	public static int tokenExpires = 0;
	/** �Ƿ��Ѿ���½ */
	public static boolean isLogin = false;
	/** android�豸��Ψһ��ʶ������mac��ַ�����macΪ�գ���ȡһ��uuidֵ */
	public static String deviceUnique = "";
	/** ������Ϣ�õ� */
	public static String userTag = "";

	/**** IPCamera1 IP��ַ����İ���UI��û����ʾ�� ***/
	public static boolean IPCamera1_hasHelpIPShow = false;
	/**** IPCamera1 UID����İ���UI��û����ʾ�� ***/
	public static boolean IPCamera1_hasHelpUIDShow = false;
	/**** Fosbaby_conn_2 ����İ���UI��û����ʾ�� ***/
	public static boolean Fosbaby_conn2_hasHelpShow = false;
	/**** Fosbaby_disconn2 ����İ���UI��û����ʾ�� ***/
	public static boolean Fosbaby_disconn2_hasHelpShow = false;
	/**** Fosbaby_disconn3 ����İ���UI��û����ʾ�� ***/
	public static boolean Fosbaby_disconn3_hasHelpShow = false;

	/** �Ƿ���Ҫ��ӡ��־ */
	public static boolean isNeedLog = true;

	// ########### SharedPreferences������Ϣ begin ###########
	/**
	 * SharedPreferences�б����������Ϣ�����ڴ���Ҳ����һ�ݣ�����ȡ�ã��ڳ������Ӧ�ö�ȡ���ڴ��У���<br>
	 * ע��:�޸�SharedPreferences������Ϣʱ��Ҫͬʱ�޸��ڴ��е����ݡ�<br>
	 * SharedPreferences���ڴ��б���ı���ͳһ��sp_��Ϊǰ׺
	 */
	/** �Ƿ�ֻ����wifi������Ԥ����Ƶ */
	public static boolean sp_isWifiOnly = false;
	/** �Ƿ���ʾ�������� */
	public static boolean sp_showHelpUI = true;
	/** �Ƿ���Ҫ����������Ϣ */
	public static boolean sp_isOpenMsgPush = true;
	/** �Ƿ�ɾ����ʷͼƬ��¼����ɾ��ipc��ʱ�� */
	public static boolean sp_isDelHistoryFile = false;
	/** ����ͷ�ת */
	public static boolean sp_horizontalChecked = false;
	/** ����ͷ�ת */
	public static boolean sp_verticalChecked = false;
	/** ���camera�ķ�ʽ 0��uid��1��ddns��2��ip */
	public static int sp_ipcAddType = 0;
	// ########### SharedPreferences������Ϣ end ###########
}
