package vn.com.vshome.foscamsdk;

/**
 * ��Ϣ����
 * 
 * @author FuS
 * @date 2014-4-23 ����1:39:37
 */
public class Msg {
	// //////////////////////////////////////
	// ��1000��ʼ��ÿ������ռ��20λ��ʣ�µ�����Ԥ���ֶΣ�������չ
	// //////////////////////////////////////
	/** δ֪�쳣 */
	public static final int UNKNOW = 99999;
	// ����camera
	/** Camera������ */
	public static final int CAMERA_SEARCHING = 1000;
	/** Camera�������� */
	public static final int CAMERA_SEARCHFINISH = 1001;
	/** Camera������ʱ���� */
	public static final int CAMERA_SEARCHTIMEOUT = 1002;
	/** mycameras�����У�ͨ������б���ļӺ�����µ�����ͷ */
	public static final int CAMERA_NEW_ADD = 1003;
	/** Camera������ */
	public static final int CAMERA_DISCONNECTED = 1004;
	/** Camera���� */
	public static final int CAMERA_CONNECTED = 1005;

	// Wifi
	/** ���ڴ�Wifi */
	public static final int WIFI_OPENING = 1020;
	/** ��������Wifi */
	public static final int WIFI_SEARCHING = 1021;
	/** ����Wifiʧ�� */
	public static final int WIFI_SEARCH_FAIL = 1022;
	/** ��������Wifi */
	public static final int WIFI_CONNECTTING = 1023;
	/** ����Wifiʧ�� */
	public static final int WIFI_CONNECT_FAIL = 1024;
	/** ����Wifi�ɹ� */
	public static final int WIFI_CONNECT_SUCC = 1025;

	// �Զ�����Camear
	/** ���ڵ�¼Camear */
	public static final int CAMERA_LOGINING = 1040;
	/** ��¼Camearʧ�� */
	public static final int CAMERA_LOGIN_FAIL = 1041;
	/** ��¼Camear�ɹ� */
	public static final int CAMERA_LOGIN_SUCC = 1042;
	/** ��¼��ʱ */
	public static final int CAMERA_LOGIN_TIMEOUT = 1043;
	/** �豸��Ϣ��macΪ�� */
	public static final int CAMERA_DEV_INFO_OR_MAC_IS_NULL = 1044;

	// ����֡
	/** ���ڼ���֡ */
	public static final int FRAME_LOADING = 1060;
	/** ֡���سɹ� */
	public static final int FRAME_LOADING_SUCC = 1061;
	/** ֡����ʧ�� */
	public static final int FRAME_LOADING_FAIL = 1062;

	// ����ͷ����
	/** wifi û�� */
	public static final int WIFI_NOT_OPEN = 1080;
	/** �޸����� */
	public static final int MODIFY_PWD = 1081;

	// ����
	/** ����ͼƬ��׼��ɾ��ͼƬ */
	public static final int OTHER_DELETE_PHOTO = 1100;
	/** ������Ƶ��׼��ɾ����Ƶ */
	public static final int OTHER_DELETE_VIDEO = 1101;
	/** �����澯��Ϣ��׼��ɾ���澯��Ϣ */
	public static final int OTHER_DELETE_WARNING_MSG = 1102;
	/** ����ϵͳ��Ϣ��׼��ɾ��ϵͳ��Ϣ */
	public static final int OTHER_DELETE_SYSTEM_MSG = 1103;
	/** ���� ɾ������ */
	public static final int OTHER_HIDE_CHECHBOX = 1104;
	/** ˢ��camera�б� */
	public static final int OTHER_REFRESH_CAMERA_LIST = 1105;

	// ��Ƶ����
	/** �Ѿ����յ���Ƶ���ݣ�׼������ */
	public static final int VIDEO_READY_PLAY = 1120;
	/** ��Ƶ��Ҫ�������� */
	public static final int VIDEO_RECONNECTED = 1121;
	/** ��Ƶ���ŵĹ����У���Ҫץ��һ��ͼƬ */
	public static final int VIDEO_SNAP_SUCC = 1122;
	/** ��Ƶ���ŵĹ����У�¼��ʱ��Ҫץ��һ��ͼƬ */
	public static final int RECORD_SNAP_SUCC = 1123;
	/** ץȡһ֡��������ƵԤ������ʱ����ҪĬ��ץȡһ֡��Ϊ���� */
	public static final int SNAP_SUCC = 1124;

	/** ¼��button ���±��� */
	public static final int IMAGE_RECORD = 1140;
	public static final int IMAGE_UNRECORD = 1141;

	/** ��ȡipc �����õ�wifi */
	public static final int GET_WIFI_SUCC = 1160;
	public static final int GET_WIFI_FAIL = 1161;
	/**����Fosbaby usertag***/
	public static final int SET_USER_TAG_SUCC = 1162;
	public static final int SET_USER_TAG_FAIL = 1163;
	/**����Fosbaby usertag***/
	public static final int SET_PUSH_SERVER_SUCC = 1164;
	public static final int SET_PUSH_SERVER_FAIL = 1165;
	
	
	

	/** ˢ����ʾcamara name��TextView **/
	public static final int NEW_NAME = 1180;

	/** ��ʾ����UI popwindow **/
	public static final int SHOW_HELP_UI = 1200;

	// fosbaby wifi ����
	/** ����fosbaby wifiʧ�� */
	public static final int FOSBABY_WIFI_CONN_FAIL = 1220;
	/** ����fosbaby wifi�ɹ� */
	public static final int FOSBABY_WIFI_CONN_SUCC = 1221;
	/** ����fosbaby wifi�� */
	public static final int FOSBABY_WIFI_CONNNING = 1222;

	// �Ʒ������

	// /** δ֪���� */
	public static final int CLOUD_UNKNOW_ERR = 1240;
	// /** �ɹ� */
	// public static final int CLOUD_SUCCESS = 1241;
	// /** ʧ�� */
	// public static final int CLOUD_FAILED = 1242;
	// /** ��ʱtoken���� */
	// public static final int CLOUD_TEMP_TOKEN_ERROR = 1243;
	// /** Access Token���� */
	// public static final int CLOUD_ACCESS_TOKEN_ERROR = 1244;
	// /** ��֤����� */
	// public static final int CLOUD_CAPTCHA_ERROR = 1245;
	// /** ����Ϊ�� */
	// public static final int CLOUD_PARAMETER_EMPTY = 1246;
	// /** ��Ч���� */
	// public static final int CLOUD_INVALID_PARAMETER = 1247;
	// /** Refresh Token���� */
	// public static final int CLOUD_REFRESH_TOKEN_ERROR = 1248;
	// /** json�ַ�����ʽ���� */
	// public static final int CLOUD_JSON_FORMAT_ERROR = 1249;
	// /** Access Token�ѹ��� */
	// public static final int CLOUD_ACCESS_TOKEN_EXPIRED = 1250;
	// /** û��Ȩ�� */
	// public static final int CLOUD_NO_PERMISSIONS = 1251;
	// /** �ļ��ϴ�ʧ�� */
	// public static final int CLOUD_FILE_UPLOAD_FAILED = 1252;
	// /** ��ȡ�ļ���׺��ʧ�� */
	// public static final int CLOUD_GET_FILE_POSTFIX_FAILED = 1253;
	// /** �ʼ����ͳɹ� */
	// public static final int CLOUD_EMAIL_SUCCESS = 1254;
	// /** �ʼ�����ʧ�� */
	// public static final int CLOUD_EMAIL_FAILED = 1255;
	// /** �ʼ�����Ϊ�� */
	// public static final int CLOUD_EMAIL_SUBJECT_EMPTY = 1256;
	// /** �ʼ�����Ϊ�� */
	// public static final int CLOUD_EMAIL_MESSAGE_EMPTY = 1257;
	// /** SQL�쳣 */
	// public static final int CLOUD_SQLEXCEPTION = 1258;
	// /** SQL����ʧ�� */
	// public static final int CLOUD_CONNECT_FAILED = 1259;
	// /** IO�쳣 */
	// public static final int CLOUD_IOEXCEPTION = 1260;
	// /** �Ҳ���class�쳣������˵�����ȱ�� */
	// public static final int CLOUD_CLASSNOTFOUNDEXCEPTION = 1261;
	// /** �������ڴ������Ժ�... */
	// public static final int CLOUD_SUBMISSION_PROCESSING = 1262;
	// /** ����δ��Ӧ */
	// public static final int CLOUD_SERVICE_NOREPONSE = 1263;
	// /** ϵͳ��æ */
	// public static final int CLOUD_SYSTEM_BUSY = 1264;
	// /** ��api��δʵ�� */
	// public static final int CLOUD_SERVICE_NOT_IMPLEMENT = 1265;
	// /** �˺Ų����� */
	// public static final int CLOUD_ACCOUNT_NOT_EXISTS = 1266;
	// /** �˺��Ѵ��� */
	// public static final int CLOUD_ACCOUNT_EXISTS = 1267;
	// /** �˺�Ϊ�� */
	// public static final int CLOUD_ACCOUNT_EMPTY = 1268;
	// /** �˺Ÿ�ʽ���� */
	// public static final int CLOUD_ACCOUNT_FORMAT_ERROR = 1269;
	// /** ����Ϊ�� */
	// public static final int CLOUD_PASSWORD_EMPTY = 1270;
	// /** ������� */
	// public static final int CLOUD_PASSWORD_ERROR = 1271;
	// /** �������벻һ�� */
	// public static final int CLOUD_PWD_MATCH_ERROR = 1272;
	// /** �����ʽ���� */
	// public static final int CLOUD_PWD_FORMAT_ERROR = 1273;
	// /** ����Ϊ�� */
	// public static final int CLOUD_EMAIL_EMPTY = 1274;
	// /** �����ʽ���� */
	// public static final int CLOUD_EMAIL_FORMAT_ERROR = 1275;
	// /** ���䲻���ڣ��˻�ע�ᣩ */
	// public static final int CLOUD_EMAIL_NOT_EXISTS = 1276;
	// /** �����Ѵ��ڣ��˻�ע�ᣩ */
	// public static final int CLOUD_EMAIL_EXISTS = 1277;
	// /** �˺������䲻ƥ�䣨�������룩 */
	// public static final int CLOUD_EMAIL_MATCH_ERROR = 1278;
	// /** ��Ч�����루�˻�ע�ᣩ */
	// public static final int CLOUD_INVALID_INVITATIONCODE = 1279;
	// /** �˺��Ѽ���˻���� */
	// public static final int CLOUD_ACCOUNT_ALREADY_ACTIVATION = 1280;
	// /** ��Ч�����루�˻���� */
	// public static final int CLOUD_INVALID_ACTIVATIONCODE = 1281;
	// /** ��Ч��Ȩ�루�������룩 */
	// public static final int CLOUD_INVALID_AUTHCODE = 1282;
	// /** �¾�����δ�ı䣨�������룩 */
	// public static final int CLOUD_PWD_NO_CHANGE = 1283;
	// /** UidΪ�գ�Uid��½�� */
	// public static final int CLOUD_UID_EMPTY = 1284;
	// /** Uid��ʽ����Uid��½�� */
	// public static final int CLOUD_UID_FORMAT_ERROR = 1285;
	// /** �������ѹ��� */
	// public static final int CLOUD_ACTIVATIONCODE_EXPIRED = 1286;
	// /** ��֧�ֵ���Ȩ���� */
	// public static final int CLOUD_UNSUPPORTED_GRANT_TYPE = 1287;
	// /** �����ߵ�appId������ */
	// public static final int CLOUD_CLIENT_ID_NOT_EXISTS = 1288;
	// /** �����ߵ�appIdΪ�� */
	// public static final int CLOUD_CLIENT_ID_EMPTY = 1289;
	// /** �����ߵ�appId��ʽ���� */
	// public static final int CLOUD_CLIENT_ID_FORMAT_ERROR = 1290;
	// /** �����ߵ�secret������ */
	// public static final int CLOUD_CLIENT_SECRET_NOT_EXISTS = 1291;
	// /** �����ߵ�secretΪ�� */
	// public static final int CLOUD_CLIENT_SECRET_EMPTY = 1292;
	// /** �����ߵ�secret��ʽ���� */
	// public static final int CLOUD_CLIENT_SECRET_FORMAT_ERROR = 1293;
	// /** �����ߵ�secret���� */
	// public static final int CLOUD_CLIENT_SECRET_ERROR = 1294;
	// /** ������Ϊ�� */
	// public static final int CLOUD_INVITATIONCODE_EMPTY = 1295;
	// /** �������ʽ���� */
	// public static final int CLOUD_INVITATIONCODE_FORMAT_ERROR = 1296;
	// /** ��������� */
	// public static final int CLOUD_INVITATIONCODE_EXPIRED = 1297;
	// /** ������Ϊ�� */
	// public static final int CLOUD_ACTIVATIONCODE_EMPTY = 1298;
	// /** �������ʽ���� */
	// public static final int CLOUD_ACTIVATIONCODE_FORMAT_ERROR = 1299;
	// /** �˺�δ���� */
	// public static final int CLOUD_ACCOUNT_NOT_ACTIVATION = 1300;
	// /** ��Ȩ��Ϊ�� */
	// public static final int CLOUD_AUTHCODE_EMPTY = 1301;
	// /** ��Ȩ���ʽ���� */
	// public static final int CLOUD_AUTHCODE_FORMAT_ERROR = 1302;
	// /** ��Ȩ����� */
	// public static final int CLOUD_AUTHCODE_EXPIRED = 1303;
	// /** ��Ȩ����� */
	// public static final int CLOUD_AUTHCODE_EXISTS = 1304;
	// /** ���Բ��������� */
	// public static final int CLOUD_LANGUAGE_NOT_EXISTS = 1305;
	// /** ��֧�ֵ��������� */
	// public static final int CLOUD_UNSUPPORTED_LANGUAGE_TYPE = 1306;
	// /** �����ַ��Ч */
	// public static final int CLOUD_INVALID_EMAIL = 1307;
	// /** �û���������� */
	// public static final int CLOUD_ACCOUNT_PASSWORD_ERROR = 1308;
	// /** �豸������ */
	// public static final int CLOUD_DEV_NOT_EXISTS = 1309;
	// /** �豸�Ѵ��� */
	// public static final int CLOUD_DEV_EXISTS = 1310;
	// /** �豸Ϊ�� */
	// public static final int CLOUD_DEV_EMPTY = 1311;
	// /** �豸��ʽ���� */
	// public static final int CLOUD_DEV_FORMAT_ERROR = 1312;
	// /** �豸�˺�Ϊ�� */
	// public static final int CLOUD_DEV_ACCOUNT_EMPTY = 1313;
	// /** �豸�˺Ÿ�ʽ���� */
	// public static final int CLOUD_DEV_ACCOUNT_FORMAT_ERROR = 1314;
	// /** �豸����Ϊ�� */
	// public static final int CLOUD_DEV_PASSWORD_EMPTY = 1315;
	// /** �豸�����ʽ���� */
	// public static final int CLOUD_DEV_PWD_FORMAT_ERROR = 1316;
	// /** �豸���ﵽ���� */
	// public static final int CLOUD_MAX_DEVS_REACHED = 1317;

	/** δ���� */
	public static final int CLOUD_UNPROCESS = 1400;
	/** �ɹ� */
	public static final int CLOUD_SUCCESS = 1401;
	/** ʧ�� */
	public static final int CLOUD_FAILED = 1402;
	/** ������ */
	public static final int CLOUD_PROCESSING = 1403;
	/** Access Token���� */
	public static final int CLOUD_ACCESS_TOKEN_ERROR = 1404;
	/** ����Ϊ�� */
	public static final int CLOUD_PARAMETER_EMPTY = 1405;
	/** ��Ч���� */
	public static final int CLOUD_INVALID_PARAMETER = 1406;
	/** Refresh Token���� */
	public static final int CLOUD_REFRESH_TOKEN_ERROR = 1407;
	/** json�ַ�����ʽ���� */
	public static final int CLOUD_JSON_FORMAT_ERROR = 1408;
	/** Access Token�ѹ��� */
	public static final int CLOUD_ACCESS_TOKEN_EXPIRED = 1409;
	/** û��Ȩ�� */
	public static final int CLOUD_NO_PERMISSIONS = 1410;
	/** ��Ȩ��֤ʧ�� */
	public static final int CLOUD_AUTH_CERTIFICATION_FAILED = 1411;
	/** ��ʱtoken���� */
	public static final int CLOUD_TEMP_TOKEN_ERROR = 1412;
	/** ��֤����� */
	public static final int CLOUD_CAPTCHA_ERROR = 1413;
	/** ��˹��ķ�ڲ����� */
	public static final int CLOUD_SYSTEM_ERROR = 1414;
	/** http����contentType���� */
	public static final int CLOUD_HTTP_CONTENT_TYPE_IMPROPER = 1415;
	/** http�����GET/POST��ʽ���� */
	public static final int CLOUD_HTTP_REQUEST_MODE_IMPROPER = 1416;
	/** �ʼ����ͳɹ� */
	public static final int CLOUD_EMAIL_SUCCESS = 1417;
	/** �ʼ�����ʧ�� */
	public static final int CLOUD_EMAIL_FAILED = 1418;
	/** �ʼ�����Ϊ�� */
	public static final int CLOUD_EMAIL_SUBJECT_EMPTY = 1419;
	/** �ʼ�����Ϊ�� */
	public static final int CLOUD_EMAIL_MESSAGE_EMPTY = 1420;
	/** SQL�쳣 */
	public static final int CLOUD_SQLEXCEPTION = 1421;
	/** SQL����ʧ�� */
	public static final int CLOUD_CONNECT_FAILED = 1422;
	/** IO�쳣 */
	public static final int CLOUD_IOEXCEPTION = 1423;
	/** �Ҳ���class�쳣������˵�����ȱ�� */
	public static final int CLOUD_CLASSNOTFOUNDEXCEPTION = 1424;
	/** �������ڴ������Ժ�... */
	public static final int CLOUD_SUBMISSION_PROCESSING = 1425;
	/** ����δ��Ӧ */
	public static final int CLOUD_SERVICE_NOREPONSE = 1426;
	/** ϵͳ��æ */
	public static final int CLOUD_SYSTEM_BUSY = 1427;
	/** ��api��δʵ�� */
	public static final int CLOUD_SERVICE_NOT_IMPLEMENT = 1428;
	// ################## �Ż������붨�� ##################
	/** �����쳣 */
	public static final int CLOUD_PT_ONLINE_DISK_EXCEPTION = 1429;
	/** �����Ѵ��� */
	public static final int CLOUD_PT_DUPLICATE_NAME = 1430;
	// ################## �˻������붨�� ##################
	/** username not exists */
	public static final int CLOUD_ACCOUNT_USERNAEM_NOT_EXISTS = 1431;
	/** username already exists */
	public static final int CLOUD_ACCOUNT_USERNAME_ALREADY_EXISTS = 1432;
	/** userId not match */
	public static final int CLOUD_ACCOUNT_USERID_NOT_EXISTS = 1433;
	/** password not match */
	public static final int CLOUD_ACCOUNT_PASSWORD_NOT_MATCH = 1434;
	/** user ipcsetting already exists */
	public static final int CLOUD_ACCOUNT_USER_IPC_SETTING_ALREADY_EXISTS = 1435;
	/** user ipcsetting not exists */
	public static final int CLOUD_ACCOUNT_USER_IPC_SETTING_NOT_EXISTS = 1436;
	/** user invitationcode invalid */
	public static final int CLOUD_ACCOUNT_USER_INVITATION_CODE_INVALID = 1437;
	/** resetpwd code invalid */
	public static final int CLOUD_ACCOUNT_USER_RESET_PASSWORD_CODE_INVALID = 1438;
	/** activation code invalid */
	public static final int CLOUD_ACCOUNT_USER_ACTIVATION_CODE_INVALID = 1439;
	/** user account has not be activated */
	public static final int CLOUD_ACCOUNT_NOT_ACTIVATED = 1456;
	/** user account already activated */
	public static final int CLOUD_ACCOUNT_ALREADY_ACTIVATED = 1457;
	/** email send error */
	public static final int CLOUD_EMAIL_SEND_ERROR = 1458;

	// ################## ��Ȩ��֤�����붨�� ##################
	/** appkey not exists */
	public static final int CLOUD_AUTH_APPKEY_NOT_EXISTS = 1440;
	/** appsecret not match appkey */
	public static final int CLOUD_AUTH_APPSECRET_NOT_MATCH = 1441;
	/** openid not exists */
	public static final int CLOUD_AUTH_OPENID_NOT_EXISTS = 1442;
	/** openid not match appkey */
	public static final int CLOUD_AUTH_OPENID_NOT_MATCH = 1443;
	/** accesstoken not match openid */
	public static final int CLOUD_AUTH_ACCESSTOKEN_NOT_MATCH = 1444;
	/** accesstoken expired */
	public static final int CLOUD_AUTH_ACCESSTOKEN_EXPIRED = 1445;
	/** refreshtoken not match openid */
	public static final int CLOUD_AUTH_REFRESHTOEKN_NOT_MATCH = 1446;
	/** refreshtoken expired */
	public static final int CLOUD_AUTH_REFRESHTOEKN_EXPIRED = 1447;
	/** username not exists */
	public static final int CLOUD_AUTH_USERNAME_NOT_EXISTS = 1448;
	/** password not match */
	public static final int CLOUD_AUTH_PASSWORD_NOT_MATCH = 1449;
	/** api has not be authorized to current user */
	public static final int CLOUD_AUTH_API_HAS_NOT_BE_AUTHORIZED = 1450;
	/** user account has not be activated */
	public static final int CLOUD_AUTH_USER_ACCOUNT_HAS_NOT_BE_ACTIVATED = 1451;
	// ################## ���ͷ����붨�� ##################
	/** �豸�Ѷ��� */
	public static final int CLOUD_IPC_ALREADY_SUBSCRIBED = 1452;
	/** �豸δ���� */
	public static final int CLOUD_IPC_NOT_SUBSCRIBED = 1453;
	/** �û�������tag */
	public static final int CLOUD_USER_TAG_ALREADY_EXISTS = 1454;
	/** �û�tag������ */
	public static final int CLOUD_USER_TAG_NOT_EXISTS = 1455;
	/** ���Ĳ�����ʱ **/
	public static final int GET_PUSH_MSG_TIMEOUT = 1456;
	/** Ҫ�����Ĳ�����mac Ϊ�գ� ���ܶ��� **/
	public static final int CAMERA_MAC_IS_NULL = 1457;
	/** Ҫ�����Ĳ�����mac Ϊ�գ� ���ܶ��� **/
	public static final int PARSE_CAMERA_MAC_ERROR = 1458;
	/**��ȡuser tag,��Ϣ���ĵ�ʱ���õ�**/
	public static final int GET_USER_TAG = 1459;
	/**���ص�usertag Ϊ��**/
	public static final int USER_TAG_IS_NULL  = 1460;
	/**�ɹ����ص�usertag **/
	public static final int GET_USER_TAG_SUCC  = 1461;
	/**��ȡusertag ����δ֪���� **/
	public static final int GET_USER_TAG_UNKNOWN_ERR  = 1462;
	/**��ȡ����״̬ ����δ֪���� **/
	public static final int GET_SUBSCRIBESTATUS_UNKNOW_ERR  = 1463;
	/**��ȡ����״̬ �ɹ� **/
	public static final int GET_SUBSCRIBESTATUS_SUCC  = 1464;
	/**��ȡ����״̬ ʧ�� **/
	public static final int GET_SUBSCRIBESTATUS_FAIL  = 1465;
	/**mac list Ϊ�� **/
	public static final int MAC_LIST_IS_NULL  = 1466;
	/**g_cameras Ϊ�� **/
	public static final int GLOBAL_CAMERA_SIZE_IS_ZERO  = 1467;

	// ��1459��ʼ

	/** ��ʾ���ȿ� */
	public static final int SHOW_OPEN_MSGPUSH_PROGRESS_DIALOG = 1460;
	/** ��Ҫ��ʾ���ȿ� */
	public static final int SHOW_CLOSE_MSGPUSH_PROGRESS_DIALOG = 1461;

	// ################## ���ķ����붨�� ##################
	/** �豸�Ѷ��� */
	public static final int FC_IPC_ALREADY_SUBSCRIBED = 1480;
	/** �豸δ���� */
	public static final int FC_IPC_NOT_SUBSCRIBED = 1481;
	/** �û�������tag */
	public static final int FC_USER_TAG_ALREADY_EXISTS = 1482;
	/** �û�tag������ */
	public static final int FC_USER_TAG_NOT_EXISTS = 1483;
	/** ���ĳɹ� ***/
	public static final int OPEN_MSGPUSH_SUCC = 1484;
	/** ����ʧ�� ***/
	public static final int OPEN_MSGPUSH_FAIL = 1485;
	/*** ȡ�����ĳɹ� **/
	public static final int CLOSE_MSGPUSH_SUCC = 1486;
	/*** ȡ������ʧ�� **/
	public static final int CLOSE_MSGPUSH_FAIL = 1487;

	// �ٶ��������
	/** �ɹ� */
	public static final int BDC_SUCCESS = 1600;
	/** ʧ�� */
	public static final int BDC_FAIL = 1601;
	/** �������� */
	public static final int BDC_PARAM_ERR = 1602;
	/** �������� */
	public static final int BDC_NETWORK_ERR = 1603;
	/** ��ȡbms������ʧ�� */
	public static final int BDC_BMS_SERVER_FAIL = 1604;
	/** ��ǰ�û�û�и��豸��Ȩ�� */
	public static final int BDC_NO_PERMISSION = 1605;
	/** ǩ������ */
	public static final int BDC_SIGNATURES_ERR = 1606;
	/** �豸������ */
	public static final int BDC_DEVICE_NOT_EXIST = 1607;
	/** �豸������ */
	public static final int GET_AUDIO_DATA_SUCC = 1608;

}
