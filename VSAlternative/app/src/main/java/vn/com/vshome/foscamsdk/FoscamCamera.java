package vn.com.vshome.foscamsdk;

/**
 * ����ͷ��
 * 
 * @author FuS
 * @date 2014-4-15 ����7:31:02
 */
public class FoscamCamera {
	/** camera�����ͣ�MJ */
	public static final int TYPE_CAMERA_MJ = 0x0;
	/** camera�����ͣ�H264 */
	public static final int TYPE_CAMERA_H264 = 0x1;
	/** camera�����ͣ�P2P */
	public static final int TYPE_CAMERA_P2P = 0x2;
	/** ������ */
	public static final int TYPE_STREAM_MAIN = 0x1;
	/** ������ */
	public static final int TYPE_STREAM_SUB = 0x0;
	/** ����ģʽ���Զ� */
	public static final int TYPE_IR_AUTO = 0x0;
	/** ����ģʽ���ֶ� */
	public static final int TYPE_IR_MANUAL = 0x1;
	/** ����ģʽ�������ֶ��� */
	public static final int TYPE_IR_ON = 0x2;
	/** ����ģʽ���أ��ֶ��� */
	public static final int TYPE_IR_OFF = 0x3;

	/** ��ݿ���seqno�ֶ�ֵ */
	private int dbid = -1;
	/** name */
	private String name = "";
	/** ���� */
	private int type = TYPE_CAMERA_H264;
	/** ip */
	private String ip = "";
	/** ddns */
	private String ddns = "";
	/** http�˿� */
	private int httpPort = 0;
	/** ý��˿�(������) */
	private int mediaPort = 0;
	/** uid */
	private String uid = "";
	/** mac */
	private String mac = "";
	/** �Ƿ����� */
	private boolean isOnline = false;
	/** 0����������1�������� */
	private int streamType = TYPE_STREAM_SUB;
	/** ����ͷ��¼�˻��� */
	private String loginName = "";
	/** ����ͷ��¼���� */
	private String loginPwd = "";
	/** ��Ʒ�ͺ���ƣ�FosBaby,FI9821P�� */
	private String productName = "";
	/** ��Ʒ��һ����Ϣ */
	private CameraProductAllInfo productAllInfo = null;
	/** �������ϴ洢��ipcΨһ��ʶ */
	private String ipcid = "";
	
	/** ��û���� usertag 
      * 2   Ϊдusertag��IPC�ɹ�
	 *  1   Ϊдusertag��IPC ���ɹ�
	 *  0  û��дusertag��IPC 
	 * */
	private int hasUserTag   = 0;

	// �ⲿ���ֶε���ʵֵ��Ҫʵʱ��ipc�˻�ȡ�����Բ��ش洢����ݿ���
	/** ����ģʽ��0�Զ���1�ֶ���2�����ֶ�ģʽ�£���3�أ��ֶ�ģʽ�£� */
	private int irmode = TYPE_IR_AUTO;
	/** �Ƿ��������ŵ� */
	private boolean isConnected = false;

	public FoscamCamera() {
	}

	/**
	 * ���캯���ʾcamera�Ǵ���ݿ��ж�ȡ�ģ�
	 * 
	 * @param seqno
	 *            ��ݿ��е�seqno�ֶ�ֵ
	 */
	public FoscamCamera(int seqno) {
		this.dbid = seqno;
	}

	/**
	 * ���캯��
	 * 
	 * @param name
	 *            �豸���
	 * @param type
	 *            �豸���ͣ�0��MJ��1��H264��2��P2P��
	 * @param ip
	 *            ip
	 * @param streamType
	 *            ������0����������1����������
	 * @param httpPort
	 * @param mediaPort
	 * @param userName
	 * @param pwd
	 * @param uid
	 */
	public FoscamCamera(String name, int type, String ip, int streamType, int httpPort, int mediaPort, String userName, String pwd, String uid) {
		this.name = name;
		this.type = type;
		this.ip = ip;
		this.streamType = streamType;
		this.httpPort = httpPort;
		this.mediaPort = mediaPort;
		this.loginName = userName;
		this.loginPwd = pwd;
		this.uid = uid;
	}

	// set
	/** ��������ͷ��seqno */
	public void setDBID(int dbid) {
		this.dbid = dbid;
	}

	/** ��������ͷ��name */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * ��������ͷ��type
	 * 
	 * @param type
	 *            0��MJ��1��H264����2��P2P��
	 */
	public void setType(int type) {
		this.type = type;
	}

	/** ��������ͷip */
	public void setIP(String ip) {
		this.ip = ip;
	}

	/** ��������ͷddns */
	public void setDDNS(String ddns) {
		this.ddns = ddns;
	}

	/** ��������ͷhttpport */
	public void setHttpPort(int port) {
		if (port < 0) {
			port = 0;
		}
		this.httpPort = port;
	}

	/** ��������ͷý��port */
	public void setMediaPort(int port) {
		if (port < 0) {
			port = 0;
		}
		this.mediaPort = port;
	}

	/** ��������ͷmac��ַ */
	public void setMAC(String mac) {
		this.mac = mac;
	}

	/** ��������ͷuid */
	public void setUID(String uid) {
		this.uid = uid;
	}

	/** ��������ͷ����or���� */
	public void setOnline(boolean online) {
		this.isOnline = online;
	}

	/**
	 * ��������ͷ��������
	 * 
	 * @param streamType
	 *            0����������1��������
	 */
	public void setStreamType(int streamType) {
		this.streamType = streamType;
	}

	/** ��������ͷ�ĵ�¼�˻��� */
	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}

	/** ��������ͷ�ĵ�¼���� */
	public void setLoginPwd(String loginPwd) {
		this.loginPwd = loginPwd;
	}

	/**
	 * ���ú���ģʽ
	 * 
	 * @param irMode
	 *            0�Զ���1�ֶ���2����3��
	 */
	public void setIRMode(int irMode) {
		this.irmode = irMode;
	}

	/**
	 * ����ipc�������
	 * 
	 * @param connected
	 *            true�����ӣ�false���Ͽ�
	 */
	public void setIsConnected(boolean connected) {
		this.isConnected = connected;
	}

	/** �����豸�Ĳ�Ʒ��ƣ�����FosBaby,FI9821P�� */
	public void setProductName(String productName) {
		this.productName = productName;
	}

	/** �豸�İ汾��һ����Ϣ�����֧�֣���Ϊnull�� */
	public void setProductAllInfo(CameraProductAllInfo productAllInfo) {
		this.productAllInfo = productAllInfo;
	}

	/** ����ipc��ipcid��ipcid��ʾ��ipc�洢���Ʒ�������Ψһ��ʶ�������Ʒ���������ͬһ��ipc�Զ�����̬������uid,ip:port,ddns:port�����棬�����������һ��ipc�ж��ipcid�� */
	public void setIpcID(String ipcID) {
		this.ipcid = ipcID;
	}
	/**
	 * 
	 * @param Ҫ�յ�usertag  
	 *    2   Ϊдusertag��IPC�ɹ�
	 *    1   Ϊдusertag��IPC ���ɹ�
	 *    0  û��дusertag��IPC 
	 */
	public void setHasUserTag(int hasUserTag) {
		this.hasUserTag = hasUserTag;
	}

	// get
	/** ��ȡ����ͷ��name */
	public String getName() {
		return this.name;
	}

	/**
	 * ��ȡ����ͷ��type
	 * 
	 * @return 0��MJ��1��H264����2��P2P��
	 */
	public int getType() {
		return this.type;
	}

	/** ��ȡ����ͷip��port */
	public String getIP() {
		return this.ip;
	}

	/** ��ȡ����ͷddns��port */
	public String getDDNS() {
		return this.ddns;
	}

	public int getHttpPort() {
		return this.httpPort;
	}

	public int getMediaPort() {
		return this.mediaPort;
	}

	/** ��ȡ����ͷuid */
	public String getUID() {
		return this.uid;
	}

	/** ��ȡ����ͷmac��ַ */
	public String getMAC() {
		return this.mac;
	}

	/** ����ͷ�Ƿ����� */
	public boolean isOnline() {
		return this.isOnline;
	}

	/**
	 * ��ȡ����ͷ��������
	 * 
	 * @return 0����������1��������
	 */
	public int getStreamType() {
		return this.streamType;
	}

	/** ��ȡ����ͷ�ĵ�¼�˻��� */
	public String getLoginName() {
		return this.loginName;
	}

	/** ��ȡ����ͷ�ĵ�¼���� */
	public String getLoginPwd() {
		return this.loginPwd;
	}

	/** ��ȡ��ݿ��е�seqno�ֶ�ֵ������-1��ʾ������ͷ���Ǵ���ݿ��л�ȡ�ģ� */
	public int getDBID() {
		return this.dbid;
	}

	/**
	 * ��ȡ����ģʽ
	 * 
	 * @return 0�Զ���1�ֶ���2����3��
	 */
	public int getIRMode() {
		return this.irmode;
	}

	/** ��ȡipc�Ƿ����� */
	public boolean getIsConnected() {
		return this.isConnected;
	}

	/** ��ȡ�豸�Ĳ�Ʒ��ƣ�����FosBaby,FI9821P�� */
	public String getProductName() {
		return this.productName;
	}

	/** �豸�İ汾��һ����Ϣ�����֧�֣���Ϊnull�� */
	public CameraProductAllInfo getProductAllInfo() {
		return this.productAllInfo;
	}

	/** ��ȡipc��ipcid��ipcid��ʾ��ipc�洢���Ʒ�������Ψһ��ʶ�������Ʒ���������ͬһ��ipc�Զ�����̬������uid,ip:port,ddns:port�����棬�����������һ��ipc�ж��ipcid�� */
	public String getIpcID() {
		return this.ipcid;
	}

	/**
	 * @return
	 *  2   Ϊдusertag��IPC�ɹ�
	 *  1   Ϊдusertag��IPC ���ɹ�
	 *  0  û��дusertag��IPC 
	 */
	public int getHasUserTag() {
		return hasUserTag;
	}

}
